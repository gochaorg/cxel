package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;

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
        if( isStatic() ){
            return method.getGenericParameterTypes();
        }else {
            Type[] params = method.getGenericParameterTypes();

            Type[] res = new Type[params.length + 1];
            System.arraycopy(params, 0, res, 1, params.length);

            res[0] = method.getDeclaringClass();
            return res;
        }
    }

    @Override
    public Type getReturnType() {
        return method.getGenericReturnType();
    }

    private MethodHandle mhandle = null;
    private boolean disableLookup = false;
    private boolean disableMHandleInvoke = false;

    @Override
    public Object call(Object[] args) {
        if( mhandle==null && method!=null && !disableLookup ){
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            try{
                mhandle = lookup.unreflect(method);
            } catch( IllegalAccessException e ){
                e.printStackTrace();
                disableLookup = true;
            }
        }

        if( mhandle!=null && !disableMHandleInvoke && isStatic() ){
            try{
                return mhandle.invokeWithArguments(args);
            } catch( Throwable throwable ){
                throwable.printStackTrace();
                disableMHandleInvoke = true;
            }
        }

        try {
            if( isStatic() ){
                return method.invoke(null, args);
            }else {
                Object inst = args.length > 0 ? args[0] : null;
                Object[] nargs = args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : args;
                return method.invoke(inst, nargs);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new EvalError(e);
        }
    }

    @Override
    public String toString() {
        return ReflectTypedFn.class.getSimpleName()+" "+method;
    }
}
