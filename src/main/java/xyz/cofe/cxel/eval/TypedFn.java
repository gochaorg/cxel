package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.fn.*;

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
            public Type[] getParameterTypes() {
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
            public Type[] getParameterTypes() {
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
            public Type[] getParameterTypes() {
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
            public Type[] getParameterTypes() {
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
            public Type[] getParameterTypes() {
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

    static <INST,A0,A1,A2,A3,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<Z> retType,
            Fn5<INST,A0,A1,A2,A3,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<4 )throw new IllegalArgumentException("args.length<4");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<Z> retType,
            Fn6<INST,A0,A1,A2,A3,A4,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<5 )throw new IllegalArgumentException("args.length<5");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,A5,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<A5> a5,
            Class<Z> retType,
            Fn7<INST,A0,A1,A2,A3,A4,A5,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( a5==null )throw new IllegalArgumentException("a5==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4, a5 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<6 )throw new IllegalArgumentException("args.length<6");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null,
                    args[5]!=null ? a5.cast(args[5]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,A5,A6,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<A5> a5,
            Class<A6> a6,
            Class<Z> retType,
            Fn8<INST,A0,A1,A2,A3,A4,A5,A6,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( a5==null )throw new IllegalArgumentException("a5==null");
        if( a6==null )throw new IllegalArgumentException("a6==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4, a5, a6 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<7 )throw new IllegalArgumentException("args.length<7");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null,
                    args[5]!=null ? a5.cast(args[5]) : null,
                    args[6]!=null ? a6.cast(args[6]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,A5,A6,A7,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<A5> a5,
            Class<A6> a6,
            Class<A7> a7,
            Class<Z> retType,
            Fn9<INST,A0,A1,A2,A3,A4,A5,A6,A7,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( a5==null )throw new IllegalArgumentException("a5==null");
        if( a6==null )throw new IllegalArgumentException("a6==null");
        if( a7==null )throw new IllegalArgumentException("a7==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4, a5, a6, a7 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<8 )throw new IllegalArgumentException("args.length<8");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null,
                    args[5]!=null ? a5.cast(args[5]) : null,
                    args[6]!=null ? a6.cast(args[6]) : null,
                    args[7]!=null ? a7.cast(args[7]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,A5,A6,A7,A8,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<A5> a5,
            Class<A6> a6,
            Class<A7> a7,
            Class<A8> a8,
            Class<Z> retType,
            Fn10<INST,A0,A1,A2,A3,A4,A5,A6,A7,A8,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( a5==null )throw new IllegalArgumentException("a5==null");
        if( a6==null )throw new IllegalArgumentException("a6==null");
        if( a7==null )throw new IllegalArgumentException("a7==null");
        if( a8==null )throw new IllegalArgumentException("a8==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4, a5, a6, a7, a8 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<9 )throw new IllegalArgumentException("args.length<8");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null,
                    args[5]!=null ? a5.cast(args[5]) : null,
                    args[6]!=null ? a6.cast(args[6]) : null,
                    args[7]!=null ? a7.cast(args[7]) : null,
                    args[8]!=null ? a8.cast(args[8]) : null
                );
            }
        };
    }

    static <INST,A0,A1,A2,A3,A4,A5,A6,A7,A8,A9,Z> TypedFn method(
            Class<INST> inst,
            Class<A0> a0,
            Class<A1> a1,
            Class<A2> a2,
            Class<A3> a3,
            Class<A4> a4,
            Class<A5> a5,
            Class<A6> a6,
            Class<A7> a7,
            Class<A8> a8,
            Class<A9> a9,
            Class<Z> retType,
            Fn11<INST,A0,A1,A2,A3,A4,A5,A6,A7,A8,A9,Z> fn
    ){
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( inst==null )throw new IllegalArgumentException("inst==null");
        if( a0==null )throw new IllegalArgumentException("a0==null");
        if( a1==null )throw new IllegalArgumentException("a1==null");
        if( a2==null )throw new IllegalArgumentException("a2==null");
        if( a3==null )throw new IllegalArgumentException("a3==null");
        if( a4==null )throw new IllegalArgumentException("a4==null");
        if( a5==null )throw new IllegalArgumentException("a5==null");
        if( a6==null )throw new IllegalArgumentException("a6==null");
        if( a7==null )throw new IllegalArgumentException("a7==null");
        if( a8==null )throw new IllegalArgumentException("a8==null");
        if( a9==null )throw new IllegalArgumentException("a9==null");
        if( retType==null )throw new IllegalArgumentException("retType==null");

        return new TypedFn() {
            @Override
            public Type[] getParameterTypes() {
                return new Type[]{ a0, a1, a2, a3, a4, a5, a6, a7, a8, a9 };
            }

            @Override
            public Type getReturnType() {
                return retType;
            }

            @Override
            public Object call(Object instance, Object[] args) {
                if( args==null )throw new IllegalArgumentException("args==null");
                if( args.length<10 )throw new IllegalArgumentException("args.length<10");
                return fn.apply(
                    instance!=null ? inst.cast(instance) : null,
                    args[0]!=null ? a0.cast(args[0]) : null,
                    args[1]!=null ? a1.cast(args[1]) : null,
                    args[2]!=null ? a2.cast(args[2]) : null,
                    args[3]!=null ? a3.cast(args[3]) : null,
                    args[4]!=null ? a4.cast(args[4]) : null,
                    args[5]!=null ? a5.cast(args[5]) : null,
                    args[6]!=null ? a6.cast(args[6]) : null,
                    args[7]!=null ? a7.cast(args[7]) : null,
                    args[8]!=null ? a8.cast(args[8]) : null,
                    args[9]!=null ? a9.cast(args[9]) : null
                );
            }
        };
    }
}
