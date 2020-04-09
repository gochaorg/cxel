package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.ast.BooleanAST;
import xyz.cofe.cxel.ast.NullAST;
import xyz.cofe.cxel.ast.NumberAST;
import xyz.cofe.cxel.ast.StringAST;
import xyz.cofe.cxel.eval.op.*;
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
    protected Optional<Object> tryRead( String name ){
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

    //region Вызов метода
    private static boolean isPrimitiveNumber( Class<?> t ){
        if( t==byte.class )return true;
        if( t==short.class )return true;
        if( t==int.class )return true;
        if( t==long.class )return true;
        if( t==float.class )return true;
        return t == double.class;
    }

    private static class NumCast {
        public Class<?> targetType;
        public Object targetValue;
        public Number sourceNumber;
        public boolean looseData;
        public boolean sameType = false;

        public NumCast(Class<?> targetType, Object targetValue, Number sourceNumber, boolean looseData){
            this.targetType = targetType;
            this.targetValue = targetValue;
            this.sourceNumber = sourceNumber;
            this.looseData = looseData;
        }
    }

    private static NumCast tryCast( Class<?> primitiveNumber, Number someNum ){
        if( someNum==null )throw new IllegalArgumentException( "someNum==null" );
        if( primitiveNumber==null )throw new IllegalArgumentException( "primitiveNumber==null" );
        if( !primitiveNumber.isPrimitive() )throw new IllegalArgumentException( "!primitiveNumber.isPrimitive()" );
        if( !isPrimitiveNumber(primitiveNumber) )throw new IllegalArgumentException( "!isPrimitiveNumber(primitiveNumber)" );

        if( primitiveNumber==byte.class ){
            if( someNum instanceof Byte ){
                NumCast ncast =new NumCast(primitiveNumber, (byte)(Byte)someNum, someNum, false );
                ncast.sameType = true;
                return ncast;
            }else if( someNum instanceof Integer ){
                int v = (int)(Integer)someNum;
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else if( someNum instanceof Short ){
                short v = (short)(Short)someNum;
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else if( someNum instanceof Long ){
                long v = someNum.longValue();
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else{
                return new NumCast(primitiveNumber, (byte)someNum.byteValue(), someNum, true);
            }
        }else if( primitiveNumber==int.class ){
            if( someNum instanceof Byte || someNum instanceof Short || someNum instanceof Integer ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.intValue(), someNum, false);
                ncast.sameType = someNum instanceof Integer;
                return ncast;
            }else{
                if( someNum instanceof Long || someNum instanceof BigInteger ){
                    long v = someNum.longValue();
                    boolean loose = v > Integer.MAX_VALUE || v < Integer.MIN_VALUE;
                    return new NumCast(primitiveNumber, someNum.intValue(), someNum, loose);
                }else{
                    return new NumCast(primitiveNumber, someNum.intValue(), someNum, true);
                }
            }
        }else if( primitiveNumber==short.class ){
            if( someNum instanceof Byte || someNum instanceof Short ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.shortValue(), someNum, false);
                ncast.sameType = someNum instanceof Short;
                return ncast;
            }else{
                if( someNum instanceof Integer || someNum instanceof Long || someNum instanceof BigInteger ){
                    long v = someNum.longValue();
                    boolean loose = v > Short.MAX_VALUE || v < Short.MIN_VALUE;
                    return new NumCast(primitiveNumber, someNum.shortValue(), someNum, loose);
                }else{
                    return new NumCast(primitiveNumber, someNum.shortValue(), someNum, true);
                }
            }
        }else if( primitiveNumber==long.class ){
            if( someNum instanceof Long || someNum instanceof Integer || someNum instanceof Short || someNum instanceof Byte ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.longValue(), someNum, false);
                ncast.sameType = someNum instanceof Long;
                return ncast;
            }else if( someNum instanceof BigInteger ){
                BigInteger v = (BigInteger)someNum;
                boolean loose = v.compareTo(BigInteger.valueOf(Long.MAX_VALUE))>0 ||
                                v.compareTo(BigInteger.valueOf(Long.MIN_VALUE))<0;
                return new NumCast(primitiveNumber, someNum.longValue(), someNum, loose);
            }else{
                return new NumCast(primitiveNumber, someNum.longValue(), someNum, true);
            }
        }else if( primitiveNumber==float.class ){
            if( someNum instanceof Float || someNum instanceof Double ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.floatValue(), someNum, false);
                ncast.sameType = someNum instanceof Float;
                return ncast;
            }else{
                return new NumCast(primitiveNumber, someNum.floatValue(), someNum, true);
            }
        }else if( primitiveNumber==double.class ){
            if( someNum instanceof Double ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.doubleValue(), someNum, false);
                ncast.sameType = true;
                return ncast;
            }else{
                return new NumCast(primitiveNumber, someNum.doubleValue(), someNum, true);
            }
        }

        return null;
    }

    /**
     * Вызов метода
     * @param inst экземпляр объекта или null
     * @param method имя метода или имя оператора
     * @param args аргументы
     * @return результат вызова
     */
    public Object call( Object inst, String method, List<Object> args ){
        if( method==null )throw new IllegalArgumentException( "method==null" );

        Eterable<Method> methods = null;

        if( inst!=null ){
            methods = Eterable.of(inst.getClass().getMethods()).filter( m->m.getName().equals(method) );
            if( methods.count()<1 ){
                throw new EvalError("method '"+method+"' not found in "+inst.getClass());
            }
        }else{
            Optional<Object> m = tryRead(method);
            if( !m.isPresent() || !(m.get() instanceof StaticMethods) ){
                throw new EvalError("function '"+method+"' not found");
            }
            methods = Eterable.of((StaticMethods)m.get());
        }

        if( methods.count()<1 ){
            throw new EvalError("method/function '"+method+"' not found");
        }

        List<ReflectCall> rcalls = methods.map( m->{
            ReflectCall rcall = null;
            Parameter[] params = m.getParameters();
            if( params.length==0 ){
                rcall = new ReflectCall(inst,m);
            }else{
                rcall = new ReflectCall(inst,m);
                for( int pi=0; pi<params.length; pi++ ){
                    Parameter p = params[pi];
                    if( pi>=args.size() ){
                        // Значение параметра не определено
                        rcall.getArgs().add( ArgPass.unpassable(pi, p.getType(), null));
                    }else{
                        Class<?> pt = p.getType();

                        Object arg = args.get(pi);
                        Class<?> at = arg!=null ? arg.getClass() : Object.class;
                        if( arg==null && pt.isPrimitive() ){
                            // Значение параметра не может быть null
                            rcall.getArgs().add( ArgPass.unpassable(pi,pt,null) );
                        }else {
                            if( pt.equals(at) ){
                                // Полное совпадение типа
                                rcall.getArgs().add( ArgPass.invariant(pi,pt,arg) );
                            }else if( pt.isAssignableFrom(at) ){
                                // Коваринтное совпадение типов
                                rcall.getArgs().add( ArgPass.covar(pi,pt,arg) );
                            }else{
                                if( isPrimitiveNumber(pt) && arg instanceof Number ){
                                    // Парамтер представлен примитивным числом и передано число
                                    NumCast ncast = tryCast(pt,(Number)arg);
                                    if( ncast!=null ){
                                        if( ncast.sameType ){
                                            rcall.getArgs().add(
                                                ArgPass.invariant(pi, pt, ncast.targetValue)
                                                    .primitiveCast(true)
                                                    .castLooseData(false)
                                            );
                                        }else {
                                            rcall.getArgs().add(
                                                ArgPass.covar(pi, pt, ncast.targetValue)
                                                    .primitiveCast(true)
                                                    .castLooseData(ncast.looseData)
                                            );
                                        }
                                    }else {
                                        Object v = pt.cast(arg);
                                        rcall.getArgs().add(ArgPass.covar(pi, pt, v));
                                    }
                                }else{
                                    // Нет совместимых вариантов
                                    rcall.getArgs().add(ArgPass.unpassable(pi, pt, arg));
                                }
                            }
                        }
                    }
                }
            }
            return rcall;
        }).filter(ReflectCall::callable)
        .toList();

        if( rcalls.isEmpty() ){
            throw new EvalError(
                "can't call "+method+(inst!=null ? " of "+inst.getClass() : "")+
                " callable method not found"
            );
        }

        if( rcalls.size()>1 ){
            // Есть несколько вариантов вызова
            // Ведем подсчет очков
            List<Tuple2<ReflectCall,Integer>> scoredRCalls = rcalls.stream().map( c -> {
                int pcount = c.getMethod().getParameterCount();

                int argsCasing = Math.abs(c.getMethod().getParameterCount() - c.getArgs().size());

                int invCalls = (int)c.getArgs().stream().filter(ArgPass::isInvarant).count();
                int primCastCalls = (int)c.getArgs().stream().filter(ArgPass::isPrimitiveCast).count();
                int loseDataCalls = (int)c.getArgs().stream().filter(ArgPass::isCastLooseData).count();
                int coCalls = (int)c.getArgs().stream().filter(ArgPass::isCovariant).count();
                int implCalls = (int)c.getArgs().stream().filter(ArgPass::isImplicit).count();

                int score = (int)(
                    invCalls*Math.pow(pcount,0) +
                    primCastCalls*Math.pow(pcount,1) +
                    coCalls*Math.pow(pcount,2) +
                    implCalls*Math.pow(pcount,3) +
                    loseDataCalls*Math.pow(pcount,4) +
                    argsCasing*Math.pow(pcount,5)
                );

                return Tuple2.of(c,score);
            }).collect(Collectors.toList());

            // находим минимальное кол-во очков
            //noinspection OptionalGetWithoutIsPresent
            int minScoreValue = scoredRCalls.stream().mapToInt(Tuple2::b).min().getAsInt();

            // берем случаи с минимальным кол-вом очков
            List<Tuple2<ReflectCall,Integer>> minScore =
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
}
