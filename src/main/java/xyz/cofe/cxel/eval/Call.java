package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.eval.score.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Вариант вызова метода
 */
public class Call
    implements
        PreparedCall, ArgsCasing, InvariantArgs, PrimitiveCastArgs, CastLooseDataArgs,
        CovariantArgs, ImplicitArgs, ParameterCount
{
    public Call(){
    }

    public Call(Object inst, TypedFn meth ){
        this.instance = inst;
        this.method = meth;
    }

    public Call configure(Consumer<Call> conf ){
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
    protected TypedFn method;

    public TypedFn getMethod(){
        return method;
    }

    public void setMethod( TypedFn method ){
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

        TypedFn method = getMethod();
        if( method == null ) return false;

//        Object inst = getInstance();
//        boolean methIsStatic = (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
//        if( !methIsStatic && inst == null ){
//            return false;
//        }

        List<ArgPass> nonNullArgs = args.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if( nonNullArgs.stream().anyMatch(m -> !m.isPassable()) ) return false;

        Map<Integer, Boolean> argPassable = new LinkedHashMap<>();
        for( int i = 0; i < method.getParametersType().length; i++ ) argPassable.put(i, false);
        nonNullArgs.forEach(m -> {
            argPassable.put(m.getIndex(), m.isPassable());
        });

        return argPassable.values().stream().allMatch(e -> e);
    }
    //endregion

    //region call()
    @Override
    public Object call(){
        if( !callable() ) throw new EvalError("can't calling");

        TypedFn method = getMethod();
        Object[] params = new Object[method.getParametersType().length];

        getArgs().stream().filter(Objects::nonNull).forEach(pa -> {
            if( pa.getIndex() >= 0 && pa.getIndex() < params.length )
                params[pa.getIndex()] = pa.getArg();
        });

        Object inst = getInstance();

        return method.call(inst, params);
    }
    //endregion

    @Override
    public int argsCasing(){
        TypedFn method = getMethod();
        if( method!=null ){
            return Math.abs(method.getParametersType().length - getArgs().size());
        }else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public int invariantArgs(){
        return (int)getArgs().stream().filter(ArgPass::isInvarant).count();
    }

    @Override
    public int primitiveCastArgs(){
        return (int)getArgs().stream().filter(ArgPass::isPrimitiveCast).count();
    }

    @Override
    public int castLooseDataArgs(){
        return (int)getArgs().stream().filter(ArgPass::isCastLooseData).count();
    }

    @Override
    public int covariantArgs(){
        return (int)getArgs().stream().filter(ArgPass::isCovariant).count();
    }

    @Override
    public int implicitArgs(){
        return (int)getArgs().stream().filter(ArgPass::isImplicit).count();
    }

    @Override
    public int parameterCount(){
        TypedFn method = getMethod();
        if( method!=null ){
            return method.getParametersType().length;
        }
        return 0;
    }
}
