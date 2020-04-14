package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.fn.Fn2;
import xyz.cofe.fn.Fn3;
import xyz.cofe.fn.Fn4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
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
    Type[] getParametersType();

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
    Object call( Object inst, Object[] args );

    /**
     * Создание экземпляра для существующего статического метода
     * @param method метод
     * @return Экземпляр
     */
    static TypedFn of(Method method){
        if( method==null )throw new IllegalArgumentException("method==null");
        return new ReflectTypedFn(method);
    }

    static <Z> TypedFn of(Class<Z> retType, Supplier<Z> fn){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");
        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return new Type[0];
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object inst, Object[] args) {
                return fn.get();
            }
        };
    }

    static <INST,Z> TypedFn method(Class<INST> inst, Class<Z> retType, Function<INST,Z> fn){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");
        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return new Type[]{};
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( instance!=null ) {
                    return fn.apply(inst.cast(instance));
                }
                return fn.apply(null);
            }
        };
    }

    static <INST,A0,Z> TypedFn method(Class<INST> inst, Class<A0> a0, Class<Z> retType, Fn2<INST,A0,Z> fn){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");
        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return new Type[]{ a0 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<1 )throw new IllegalArgumentException("args.length<1");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null
                );
            }
        };
    }

    static <INST,A0,A1,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<Z> retType,
            Fn3<INST,A0,A1,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return new Type[]{ a0, a1 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<2 )throw new IllegalArgumentException("args.length<2");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<Z> retType,
            Fn4<INST,A0,A1,A2,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return new Type[]{ a0, a1, a2 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<3 )throw new IllegalArgumentException("args.length<3");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null
                );
            }
        };
    }
}
