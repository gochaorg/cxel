package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Вариант вызова метода
 */
public class ReflectCall {
    public ReflectCall(){
    }

    public ReflectCall( Object inst, Method meth ){
        this.instance = inst;
        this.method = meth;
    }

    public ReflectCall configure( Consumer<ReflectCall> conf ){
        if( conf != null ) conf.accept(this);
        return this;
    }

    //region instance : Object
    protected Object instance;

    public Object getInstance(){
        return instance;
    }

    public void setInstance( Object instance ){
        this.instance = instance;
    }
    //endregion

    //region method : Method
    protected Method method;

    public Method getMethod(){
        return method;
    }

    public void setMethod( Method method ){
        this.method = method;
    }
    //endregion

    //region args : List<ArgPass>
    protected List<ArgPass> args;

    public List<ArgPass> getArgs(){
        if( args != null ){
            return args;
        }
        args = new ArrayList<>();
        return args;
    }

    public void setArgs( List<ArgPass> args ){
        this.args = args;
    }
    //endregion

    //region callable() : boolean
    public boolean callable(){
        List<ArgPass> args = getArgs();
        if( args == null ) return false;

        Method method = getMethod();
        if( method == null ) return false;

        Object inst = getInstance();
        boolean methIsStatic = (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
        if( !methIsStatic && inst == null ){
            return false;
        }

        List<ArgPass> nonNullArgs = args.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if( nonNullArgs.stream().anyMatch(m -> !m.isPassable()) ) return false;

        Map<Integer, Boolean> argPassable = new LinkedHashMap<>();
        for( int i = 0; i < method.getParameterCount(); i++ ) argPassable.put(i, false);
        nonNullArgs.forEach(m -> {
            argPassable.put(m.getIndex(), m.isPassable());
        });

        return argPassable.values().stream().allMatch(e -> e);
    }
    //endregion

    //region call()
    public Object call(){
        if( !callable() ) throw new EvalError("can't calling");

        Method method = getMethod();
        Object[] params = new Object[method.getParameterCount()];

        getArgs().stream().filter(Objects::nonNull).forEach(pa -> {
            if( pa.getIndex() >= 0 && pa.getIndex() < params.length )
                params[pa.getIndex()] = pa.getArg();
        });

        Object inst = getInstance();

        try{
            return method.invoke(inst, params);
        }catch( IllegalAccessException | InvocationTargetException e ){
            throw new EvalError(e);
        }
    }
    //endregion
}
