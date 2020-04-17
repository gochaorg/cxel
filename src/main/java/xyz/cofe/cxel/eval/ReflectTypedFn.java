package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class ReflectTypedFn implements TypedFn {
    public ReflectTypedFn(Method method){
        if( method==null )throw new IllegalArgumentException("method==null");
        this.method = method;
    }

    private final Method method;

    public Method getMethod(){ return method; }

    public boolean isStatic(){
        return (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
    }

    @Override
    public Type[] getParameterTypes() {
        return method.getGenericParameterTypes();
    }

    @Override
    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    @Override
    public Object call(Object inst, Object[] args) {
        try {
            return method.invoke(inst, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EvalError(e);
        }
    }
}
