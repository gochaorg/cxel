package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.ast.BooleanAST;
import xyz.cofe.cxel.ast.NullAST;
import xyz.cofe.cxel.ast.NumberAST;
import xyz.cofe.cxel.ast.StringAST;
import xyz.cofe.cxel.eval.score.DefaultScrolling;
import xyz.cofe.fn.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
    private void bindStatic( String name, Consumer<StaticMethods> conf ){
        if( name==null )throw new IllegalArgumentException("name==null");
        if( conf==null )throw new IllegalArgumentException("conf==null");

        ReadWriteLock rwlock = rwlockOf(name);
        try {
            rwlock.writeLock().lock();
            Object oStMeths = variables.get(name);
            if( !(oStMeths instanceof StaticMethods) ){
                oStMeths = new StaticMethods();
                variables.put(name,oStMeths);
            }

            StaticMethods stMeths = (StaticMethods)oStMeths;
            conf.accept(stMeths);
        } finally{
            rwlock.writeLock().unlock();
        }
    }

    /**
     * Добавление статичного метода как глобальную функцию
     * @param name имя функции, может быть null, тогда будет использоваться имя метода
     * @param method метод
     */
    public EvalContext bindStaticMethod( String name, Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        if( name==null )name = method.getName();
        bindStatic(name, st->st.add(method));
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
            if( fnName==null )continue;
            for( String name : fnName.value() ){
                bindStaticMethod(name, m);
            }
        }
        return this;
    }

    public <Z> EvalContext bindFn( String name, Class<Z> returnType, Supplier<Z> fn ){
        if( name==null )throw new IllegalArgumentException("name==null");
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        bindStatic(name, st->st.add(TypedFn.of(returnType,fn)));
        return this;
    }

    public <A0,Z> EvalContext bindFn( String name, Class<A0> targ0, Class<Z> returnType, Function<A0,Z> fn ){
        if( name==null )throw new IllegalArgumentException("name==null");
        if( targ0==null )throw new IllegalArgumentException("targ0==null");
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        bindStatic(name, st->st.add(TypedFn.of(targ0,returnType,fn)));
        return this;
    }

    public <A0,A1,Z> EvalContext bindFn( String name, Class<A0> targ0,  Class<A1> targ1, Class<Z> returnType, BiFunction<A0,A1,Z> fn ){
        if( name==null )throw new IllegalArgumentException("name==null");
        if( targ0==null )throw new IllegalArgumentException("targ0==null");
        if( targ1==null )throw new IllegalArgumentException("targ1==null");
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        bindStatic(name, st->st.add(TypedFn.of(targ0,targ1,returnType,fn)));
        return this;
    }
    //endregion

    //region Вызов метода
    //region reflectPreparingCalls - вызов метода/функции
    protected volatile ReflectPreparingCalls reflectCalls;
    public ReflectPreparingCalls reflectPreparingCalls(){
        if( reflectCalls !=null )return reflectCalls;
        synchronized (this){
            if( reflectCalls !=null )return reflectCalls;
            reflectCalls = new ReflectPreparingCalls(this);
            return reflectCalls;
        }
    }
    //endregion
    //region contextPrepatingCalls - вызов метода/функции
    protected volatile ContextPreparingCalls contextPreparingCalls;
    public ContextPreparingCalls contextPreparingCalls(){
        if( contextPreparingCalls !=null )return contextPreparingCalls;
        synchronized (this){
            if( contextPreparingCalls !=null )return contextPreparingCalls;
            contextPreparingCalls = new ContextPreparingCalls(this);
            return contextPreparingCalls;
        }
    }
    //endregion

    protected volatile CallScoring<? super PreparedCall> scoring;
    public CallScoring<? super PreparedCall> scoring(){
        if( scoring!=null )return scoring;
        synchronized( this ){
            if( scoring!=null )return scoring = scoring;
            scoring = new DefaultScrolling();
            return scoring;
        }
    }
    public void scoring(CallScoring<? super PreparedCall> scoring){
        if( scoring==null )throw new IllegalArgumentException("scoring==null");
        this.scoring = scoring;
    }

    /**
     * Вызов метода
     * @param method имя метода или имя оператора
     * @param args аргументы
     * @return результат вызова
     */
    public Object call( String method, List<Object> args ){
        if( method==null )throw new IllegalArgumentException( "method==null" );

        PreparingCalls ctxPCalls = contextPreparingCalls();
        PreparingCalls rflPCalls = reflectPreparingCalls();

        Set<PreparingCalls> scopes = new LinkedHashSet<>();
        scopes.add(ctxPCalls);
        scopes.add(rflPCalls);

        Map<PreparedCall,PreparingCalls> calls = new LinkedHashMap<>();
        for( PreparingCalls prepare : scopes ){
            List<? extends PreparedCall> pcalls = prepare.prepare(method,args);
            for( PreparedCall pcall : pcalls ){
                calls.put(pcall, prepare);
            }
        }
        int summaryCalls = calls.size();

        if( summaryCalls<1 ){
            throw new EvalError(
                "can't call "+method+
                " callable method not found"+
                    ( args!=null && !args.isEmpty() ? ", for args[ "+(
                        args.stream().map( a-> (a==null ? "null" : a+" : "+a.getClass() )
                    ).reduce("", (a,b)->a.length()>0 ? a+", "+b : b) )+" ]" : "")
            );
        }

        if( summaryCalls>1 ){
            // Есть несколько вариантов вызова
            // Ведем подсчет очков
            CallScoring<? super PreparedCall> scoring = scoring();

            List<Tuple2<? extends PreparedCall,Integer>> scoredRCalls = calls.keySet().stream().map( c -> {
                return Tuple2.of(c,scoring.calculate(c, calls.get(c)));
            }).collect(Collectors.toList());

            // находим минимальное кол-во очков
            //noinspection OptionalGetWithoutIsPresent
            int minScoreValue = scoredRCalls.stream().mapToInt(Tuple2::b).min().getAsInt();

            // берем случаи с минимальным кол-вом очков
            List<Tuple2<? extends PreparedCall,Integer>> minScore =
                scoredRCalls.stream().filter( r->r.b()==minScoreValue ).collect(Collectors.toList());

            if( minScore.size()>1 ){
                // найдены два или более равнозначных варианта
                StringBuilder sb = new StringBuilder();
                for( int pi=0; pi<minScore.size(); pi++ ){
                    sb.append(pi).append(": ");
                    sb.append("weight=").append(minScore.get(pi).b());
                    sb.append(" prepared call: ");

                    PreparedCall pcall = minScore.get(pi).a();
                    if( pcall instanceof Call ){
                        Call cl = (Call)pcall;
                        sb.append("scope=").append( calls.get(cl) );
                        sb.append(" method=").append(cl.getFn());
                        sb.append(" args(").append(cl.getArgs().size()).append("):");
                        for( int ai=0;ai<cl.getArgs().size();ai++ ){
                            sb.append("\n");
                            sb.append("arg[").append("]:");
                            ArgPass apss = cl.getArgs().get(ai);
                            sb.append(" idx=").append(apss.getIndex());
                            sb.append(" inputType=").append(apss.getInputType());
                            sb.append(" passable=").append(apss.isPassable());
                            sb.append(" invariant=").append(apss.isInvarant());
                            sb.append(" covariant=").append(apss.isCovariant());
                            sb.append(" implicit=").append(apss.isImplicit());
                            sb.append(" primtvcst=").append(apss.isPrimitiveCast());
                            sb.append(" cstloose=").append(apss.isCastLooseData());
                            if( apss.getArg()!=null ){
                                sb.append(" argType=").append(apss.getArg().getClass());
                                sb.append(" argValue=").append(apss.getArg());
                            }else{
                                sb.append(" argValue=").append("null");
                            }
                        }
                        sb.append("\n");
                    }else{
                        sb.append(pcall);
                    }

                    sb.append("\n"+"................................."+"\n");
                }
                throw new EvalError(
                    "can't call "+method+
                        " ambiguous methods calls found:\n"+sb
                );
            }else if( minScore.size()<1 ){
                // нету ни одного подходящего варианта
                throw new EvalError(
                    "can't call "+method+
                        " callable method not found"+
                        ( args!=null && !args.isEmpty() ? ", for args[ "+(
                            args.stream().map( a-> (a==null ? "null" : a+" : "+a.getClass() )
                            ).reduce("", (a,b)->a.length()>0 ? a+", "+b : b) )+" ]" : "")
                );
            }

            // Вызываем тот вариант, который ближе всего к правде
            return minScore.get(0).a().call();
        }

        PreparedCall pcall = calls.keySet().iterator().next();
        return pcall.call();
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
                    return m.invoke(base);
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

    //region list - создание списка
    /**
     * Создание списка элементов
     * @param items список элементов
     * @return список
     */
    public List<Object> list( Object[] items ){
        ArrayList<Object> lst = new ArrayList<>();
        if( items!=null ){
            lst.addAll(Arrays.asList(items));
        }
        return lst;
    }
    //endregion
    //region map - создание карты
    /**
     * Создание карты
     */
    public interface MapBuilder {
        /**
         * Добавление пары ключ/значение
         * @param key ключ
         * @param val значение
         * @return SELF ссылка на MapBuilder
         */
        MapBuilder put( Object key, Object val );

        /**
         * Получение итоговой карты
         * @return карта
         */
        Map<Object,Object> build();
    }

    /**
     * Создаение карты
     * @return строитель карты
     */
    public MapBuilder map(){
        return new MapBuilder() {
            @SuppressWarnings("FieldMayBeFinal")
            private Map<Object,Object> map = new LinkedHashMap<>();

            @Override
            public MapBuilder put(Object key, Object val) {
                map.put(key,val);
                return this;
            }

            @Override
            public Map<Object, Object> build() {
                return map;
            }
        };
    }
    //endregion
    //region condition():boolean - проверка значений в условиях (if /?)
    /**
     * Проверка значения условия (if / ?)
     * @param value значение в условии
     * @return реузльтат проверки
     */
    public boolean condition( Object value ){
        if( value==null )return false;
        if( value instanceof Boolean ){
            return (Boolean)value;
        }
        if( value instanceof Number ){
            if( value instanceof Double )return ((Double)value)==0.0;
            if( value instanceof Float )return ((Float)value)==0.0f;
            return ((Number)value).doubleValue()==0.0;
        }
        if( value instanceof String ){
            return ((String)value).length()>0;
        }
        return true;
    }
    //endregion
}
