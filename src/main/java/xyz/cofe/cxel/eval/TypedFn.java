package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.fn.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Функция с типами
 */
public interface TypedFn {
    /**
     * Возвращает тип аргументов
     * @return тип аргументов
     */
    Type[] getParameterTypes();

    /**
     * Возврщает тип реузльтата
     * @return тип результата
     */
    Type getReturnType();

    /**
     * Вызов функции
     * @param args аргументы
     * @return результат вызова
     */
    Object call( Object[] args );

    /**
     * Создание экземпляра для существующего статического метода
     * @param method метод
     * @return Экземпляр
     */
    static TypedFn of(Method method){
        if( method==null )throw new IllegalArgumentException("method==null");
        return new ReflectTypedFn(method);
    }

    static <Z> TypedFn of(Class<Z> returnType, Supplier<Z> fn){
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        return new TypedFn() {
            private final Type[] params = new Type[]{  };

            @Override
            public Type[] getParameterTypes() {
                return params;
            }

            @Override
            public Type getReturnType() {
                return returnType;
            }

            @Override
            public Object call(Object[] args) {
                return fn.get();
            }
        };
    }

    static <A0,Z> TypedFn of( Class<A0> targ0, Class<Z> returnType, Function<A0,Z> fn ){
        if( targ0==null )throw new IllegalArgumentException("targ0==null");
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        return new TypedFn() {
            private final Type[] params = new Type[]{ targ0 };

            @Override
            public Type[] getParameterTypes() {
                return params;
            }

            @Override
            public Type getReturnType() {
                return returnType;
            }

            @Override
            public Object call(Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<1 )throw new IllegalArgumentException("args.length<1");
                return fn.apply( args[0]!=null ? targ0.cast(args[0]) : null );
            }
        };
    }

    static <A0,A1,Z> TypedFn of( Class<A0> targ0, Class<A1> targ1, Class<Z> returnType, BiFunction<A0,A1,Z> fn ){
        if( targ0==null )throw new IllegalArgumentException("targ0==null");
        if( targ1==null )throw new IllegalArgumentException("targ1==null");
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        return new TypedFn() {
            private final Type[] params = new Type[]{ targ0, targ1 };

            @Override
            public Type[] getParameterTypes() {
                return params;
            }

            @Override
            public Type getReturnType() {
                return returnType;
            }

            @Override
            public Object call(Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<2 )throw new IllegalArgumentException("args.length<2");
                return fn.apply(
                    args[0]!=null ? targ0.cast(args[0]) : null ,
                    args[1]!=null ? targ1.cast(args[1]) : null
                );
            }
        };
    }
}
