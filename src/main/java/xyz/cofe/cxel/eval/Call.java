package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.eval.score.*;

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

    public Call(TypedFn meth){
        this.fn = meth;
    }

    public Call configure(Consumer<Call> conf){
        if( conf != null ) conf.accept(this);
        return this;
    }

    protected List<Object> inputArgs;

    public List<Object> getInputArgs() {
        return inputArgs;
    }

    public void setInputArgs(List<Object> inputArgs) {
        this.inputArgs = inputArgs;
    }

    //region method : TypedFn
    protected TypedFn fn;

    public TypedFn getFn(){
        return fn;
    }

    public void setFn(TypedFn fn){
        this.fn = fn;
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

        TypedFn method = getFn();
        if( method == null ) return false;

//        Object inst = getInstance();
//        boolean methIsStatic = (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
//        if( !methIsStatic && inst == null ){
//            return false;
//        }

        List<ArgPass> nonNullArgs = args.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if( nonNullArgs.stream().anyMatch(m -> !m.isPassable()) ) return false;

        Map<Integer, Boolean> argPassable = new LinkedHashMap<>();
        for(int i = 0; i < method.getParameterTypes().length; i++ ) argPassable.put(i, false);
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

        TypedFn f = getFn();
        Object[] params = new Object[f.getParameterTypes().length];

        getArgs().stream().filter(Objects::nonNull).forEach(pa -> {
            if( pa.getIndex() >= 0 && pa.getIndex() < params.length )
                params[pa.getIndex()] = pa.getArg();
        });

        return f.call(params);
    }
    //endregion

    @Override
    public int argsCasing(){
        TypedFn method = getFn();
        List<Object> inptArgs = getInputArgs();
        if( method!=null && inptArgs!=null ){
            return Math.abs(method.getParameterTypes().length - inptArgs.size());
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
        TypedFn method = getFn();
        if( method!=null ){
            return method.getParameterTypes().length;
        }
        return 0;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Call");

        sb.append(" paramsCount=").append(parameterCount());
        sb.append(" argsCasing=").append(argsCasing());
        sb.append(" invariantArgs=").append(invariantArgs());
        sb.append(" primitiveCastArgs=").append(primitiveCastArgs());
        sb.append(" castLooseDataArgs=").append(castLooseDataArgs());
        sb.append(" covariantArgs=").append(covariantArgs());
        sb.append(" implicitArgs=").append(implicitArgs());

        TypedFn tf = fn;
        if( tf!=null ){
            if( tf instanceof ReflectTypedFn ){
                sb.append(" reflect method=").append(((ReflectTypedFn) tf).getMethod());
            }else{
                sb.append(" method=").append(tf);
            }
        }

        return sb.toString();
    }
}
