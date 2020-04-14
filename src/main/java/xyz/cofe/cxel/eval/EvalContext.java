package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.ast.BooleanAST;
import xyz.cofe.cxel.ast.NullAST;
import xyz.cofe.cxel.ast.NumberAST;
import xyz.cofe.cxel.ast.StringAST;
import xyz.cofe.cxel.eval.op.*;
import xyz.cofe.cxel.eval.score.DefaultScrolling;
import xyz.cofe.fn.Tuple2;
import xyz.cofe.iter.Eterable;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Контекст интерпретации.
 *
 * <p>
 *     Контекст содержит в себе:
 *     <ul>
 *         <li>Переменные - см {@link #read(String)}, {@link #bind(String, Object)}</li>
 *         <li>Глобальные операторы -
 *         Каждый оператор (+,-,*, и т.д.) является функцией от двух аргументов
 *         <br>
 *         см. {@link #bindStaticMethod(Method)}, {@link #bindStaticMethods(Class)}
 *         </li>
 *     </ul>
 * </p>
 */
public class EvalContext {
    //region create & conf
    /**
     * Конструктор по умолчанию
     */
    public EvalContext(){
    }

    /**
     * Конструктор
     * @param vars началное значение переменных
     */
    public EvalContext( Map<String,Object> vars ){
        if( vars!=null ){
            vars.forEach( (k,v) -> {
                if( k!=null ){
                    variables.put(k,v);
                }
            } );
        }
    }

    /**
     * Конструктор
     * @param vars началное значение переменных
     */
    public EvalContext( Iterable<Tuple2<String,Object>> vars ){
        if( vars!=null ){
            vars.forEach( p -> {
                if( p!=null && p.a()!=null ){
                    variables.put(p.a(), p.b());
                }
            } );
        }
    }

    /**
     * Конфигуриование
     * @param conf конфигурация
     * @return self ссылка
     */
    public EvalContext configure( Consumer<EvalContext> conf ){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }
    //endregion

    //region read / write variables
    private final Map<String,Object> variables = new ConcurrentHashMap<>();
    private final Map<Integer, ReadWriteLock> varLocks = new ConcurrentHashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private final int maxVarLocks = 1024;

    /**
     * Получение ключа для блокировки переменной
     * @param name имя переменной
     * @return Ключ
     */
    private Integer rwlockKey( String name ){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        return name.hashCode() % maxVarLocks;
    }
    private ReadWriteLock rwlockOf( String name ){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        return varLocks.computeIfAbsent(rwlockKey(name), k->new ReentrantReadWriteLock());
    }
    private void drop( String name ){
        if( name==null )throw new IllegalArgumentException( "name==null" );

        ReadWriteLock rwLock = rwlockOf(name);
        try{
            rwLock.writeLock().lock();
            variables.remove(name);
        } finally{
            rwLock.writeLock().unlock();
            varLocks.remove(rwlockKey(name));
        }
    }
    private void write( String name, Object value ){
        if( name==null )throw new IllegalArgumentException( "name==null" );

        ReadWriteLock rwLock = rwlockOf(name);
        try{
            rwLock.writeLock().lock();
            variables.put(name, value);
        } finally{
            rwLock.writeLock().unlock();
        }
    }

    /**
     * Попытка чтения переменной
     * @param name имя переменной
     * @return значение переменной если она определена
     */
    public Optional<Object> tryRead( String name ){
        if( name==null )throw new IllegalArgumentException( "name==null" );

        ReadWriteLock rwLock = rwlockOf(name);
        try{
            rwLock.readLock().lock();
            if( variables.containsKey(name) ){
                return Optional.ofNullable(variables.get(name));
            }else{
                return Optional.empty();
            }
        } finally{
            rwLock.readLock().unlock();
        }
    }

    /**
     * Определение пременной и запись значения в переменную
     * @param name имя переменной
     * @param value значение
     */
    public EvalContext bind( String name, Object value ){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        write(name,value);
        return this;
    }

    /**
     * Чтение переменной
     * @param name имя переменной
     * @return значение переменной
     */
    public Object read( String name ){
        if( name==null )throw new IllegalArgumentException( "name==null" );
        Optional<Object> value = tryRead(name);
        if( value==null || !value.isPresent() ){
            throw new EvalError("variable '"+name+"' not found");
        }
        return value.get();
    }
    //endregion

    //region bind static methods
    /**
     * Добавление статичного метода как глобальную функцию
     * @param name имя функции, может быть null, тогда будет использоваться имя метода
     * @param method метод
     */
    public EvalContext bindStaticMethod( String name, Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        if( name==null )name = method.getName();
        ReadWriteLock rwlock = rwlockOf(name);
        try {
            rwlock.writeLock().lock();
            Object oStMeths = variables.get(name);
            if( !(oStMeths instanceof StaticMethods) ){
                oStMeths = new StaticMethods();
                variables.put(name,oStMeths);
            }

            StaticMethods stMeths = (StaticMethods)oStMeths;
            stMeths.add(method);
        } finally{
            rwlock.writeLock().unlock();
        }
        return this;
    }

    /**
     * Добавление статичного метода как глобальную функцию
     * @param method метод
     */
    public EvalContext bindStaticMethod( Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        return bindStaticMethod(null, method);
    }

    /**
     * Добавление статичных методов. <br>
     * Методы должны быть помечены аннотацией {@link FnName} и должны быть статичными. <br>
     * Пример: <br>
     * <code>
     * &#64;FnName("+") <br>
     * public static int add( int a, int b ){ <br>
     *   return a+b; <br>
     * }
     * </code>
     * @param cls Класс контейнер статичных методов
     */
    public EvalContext bindStaticMethods( Class<?> cls ){
        if( cls==null )throw new IllegalArgumentException( "cls==null" );
        for( Method m : cls.getMethods() ){
            if( (m.getModifiers() & Modifier.STATIC) != Modifier.STATIC ){
                continue;
            }

            FnName fnName = m.getAnnotation(FnName.class);
            for( String name : fnName.value() ){
                bindStaticMethod(name, m);
            }
        }
        return this;
    }
    //endregion

    private volatile PreparingCalls preparingCalls;
    private PreparingCalls preparingCalls(){
        if( preparingCalls!=null )return preparingCalls;
        synchronized (this){
            if( preparingCalls!=null )return preparingCalls;
            preparingCalls = new ReflectPreparingCalls(this);
            return preparingCalls;
        }
    }

    //region Вызов метода
    /**
     * Вызов метода
     * @param inst экземпляр объекта или null
     * @param method имя метода или имя оператора
     * @param args аргументы
     * @return результат вызова
     */
    public Object call( Object inst, String method, List<Object> args ){
        if( method==null )throw new IllegalArgumentException( "method==null" );

        List<? extends PreparedCall> rcalls = preparingCalls().prepare(inst,method,args);

        if( rcalls.isEmpty() ){
            throw new EvalError(
                "can't call "+method+(inst!=null ? " of "+inst.getClass() : "")+
                " callable method not found"
            );
        }

        if( rcalls.size()>1 ){
            // Есть несколько вариантов вызова
            // Ведем подсчет очков
            CallScoring<? super PreparedCall> scoring = new DefaultScrolling();

            List<Tuple2<? extends PreparedCall,Integer>> scoredRCalls = rcalls.stream().map( c -> {
                return Tuple2.of(c,scoring.calculate(c));
            }).collect(Collectors.toList());

            // находим минимальное кол-во очков
            //noinspection OptionalGetWithoutIsPresent
            int minScoreValue = scoredRCalls.stream().mapToInt(Tuple2::b).min().getAsInt();

            // берем случаи с минимальным кол-вом очков
            List<Tuple2<? extends PreparedCall,Integer>> minScore =
                scoredRCalls.stream().filter( r->r.b()==minScoreValue ).collect(Collectors.toList());

            if( minScore.size()>1 ){
                // найдены два или более равнозначных варианта
                throw new EvalError(
                    "can't call "+method+(inst!=null ? " of "+inst.getClass() : "")+
                        " ambiguous methods calls found"
                );
            }else if( minScore.size()<1 ){
                // нету ни одного подходящего варианта
                throw new EvalError(
                    "can't call "+method+(inst!=null ? " of "+inst.getClass() : "")+
                        " callable method not found"
                );
            }

            // Вызываем тот вариант, который ближе всего к правде
            return minScore.get(0).a().call();
        }

        return rcalls.get(0).call();
    }
    //endregion

    /**
     * Чтение свойства
     * @param base Объект, чье свойство интересует
     * @param propertyName имя свойства
     * @return значение свойства
     */
    public Object get( Object base, String propertyName ){
        if( propertyName==null )throw new IllegalArgumentException( "propertyName==null" );
        if( base==null )throw new IllegalArgumentException( "can't read property '"+propertyName+"' of null" );
        if( base instanceof Map ){
            return ((Map<?,?>)base).get(propertyName);
        }

        try{
            BeanInfo bi = Introspector.getBeanInfo(base.getClass());
            for( PropertyDescriptor pd : bi.getPropertyDescriptors() ){
                if( propertyName.equals(pd.getName()) && pd.getReadMethod()!=null ){
                    Method m = pd.getReadMethod();
                    Object r = m.invoke(base);
                    return r;
                }
            }
        }catch( InvocationTargetException | IllegalAccessException | IntrospectionException e ){
            throw new EvalError(e);
        }

        throw new EvalError("can't resolve property '"+propertyName+"' for obj of type "+base.getClass());
    }

    /**
     * Чтение элемента массива
     * @param base Объект (список или массив), чей элемент интересует
     * @param idx индекс элемента
     * @return значение
     */
    public Object getAt( Object base, Object idx ){
        if( base==null )throw new IllegalArgumentException( "can't read index value '"+idx+"' of null" );

        if( base instanceof Map ){
            return ((Map<?,?>)base).get(idx);
        }

        if( base instanceof List ){
            if( idx instanceof Number ){
                return ((List<?>)base).get(((Number)idx).intValue());
            }
        }

        if( base.getClass().isArray() ){
            if( idx instanceof Number ){
                Array.get(base, ((Number)idx).intValue() );
            }
        }

        if( idx instanceof String ){
            return get(base, (String)idx);
        }

        throw new EvalError("can't get element with idx="+idx+" for obj of type "+base.getClass());
    }

    //region Добавление стандартных операторов
    {
        // Добавление стандартных операторов
        bindStaticMethods(EqualsOprations.class);
        bindStaticMethods(BoolOperations.class);
        bindStaticMethods(ByteOperators.class);
        bindStaticMethods(ShortOperators.class);
        bindStaticMethods(IntegerOperators.class);
        bindStaticMethods(LongOperators.class);
        bindStaticMethods(FloatOperators.class);
        bindStaticMethods(DoubleOperators.class);
        bindStaticMethods(UnaryOperations.class);
    }
    //endregion

    //region Интерпретация литералов
    /**
     * Интерпретация числа
     * @param ast число (токен/лексема)
     * @return число (значение)
     */
    public Object number( NumberAST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        return ast.value();
    }

    /**
     * Интерпретация булево
     * @param ast булево (токен/лексема)
     * @return булево (значение)
     */
    public Object bool( BooleanAST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        return ast.value();
    }

    /**
     * Интерпретация строки
     * @param ast строка (токен/лексема)
     * @return строка (значение)
     */
    public Object string( StringAST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        return ast.value();
    }

    /**
     * Интерпретация null литерала
     * @param ast null литерал (токен/лексема)
     * @return null (значение)
     */
    public Object nullLiteral( NullAST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        return ast.value();
    }
    //endregion
}
