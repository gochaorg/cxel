package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.eval.score.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Вариант вызова метода/функции
 */
public class Call
    implements
        PreparedCall, ArgsCasing, InvariantArgs, PrimitiveCastArgs, CastLooseDataArgs,
        CovariantArgs, ImplicitArgs, ParameterCount
{
    //region Конструкторы
    /**
     * Конструктор по умолчанию
     */
    public Call(){
    }

    /**
     * Конструктор
     * @param meth вызываемая функция
     */
    public Call(TypedFn meth){
        this.fn = meth;
    }

    /**
     * Конфигурирование экземпляра
     * @param conf конфигурация
     * @return self ссылка
     */
    public Call configure(Consumer<Call> conf){
        if( conf != null ) conf.accept(this);
        return this;
    }
    //endregion
    //region inputArgs : List<Object> - Входящие аргументы
    /**
     * Входящие аргументы
     */
    protected List<Object> inputArgs;

    /**
     * Возвращает входящие аргументы
     * @return входящие аргументы
     */
    public List<Object> getInputArgs() {
        return inputArgs;
    }

    /**
     * Указывает входящие аргументы
     * @param inputArgs входящие аргументы
     */
    public void setInputArgs(List<Object> inputArgs) {
        this.inputArgs = inputArgs;
    }
    //endregion
    //region method : TypedFn

    /**
     * Вызываемая функция
     */
    protected TypedFn fn;

    /**
     * Возвращает вызываемую функцию
     * @return вызываемая функция
     */
    public TypedFn getFn(){
        return fn;
    }

    /**
     * Указывает вызываемую функцию
     * @param fn вызываемая функция
     */
    public void setFn(TypedFn fn){
        this.fn = fn;
    }
    //endregion

    //region args : List<ArgPass>
    /**
     * Возвращает список передаваемых аргументов
     */
    protected List<ArgPass> args;

    /**
     * Возвращает список передаваемых аргументов
     * @return список передаваемых аргументов
     */
    public List<ArgPass> getArgs(){
        if( args != null ){
            return args;
        }
        args = new ArrayList<>();
        return args;
    }

    /**
     * Указывает список передаваемых аргументов
     * @param args список передаваемых аргументов
     */
    public void setArgs( List<ArgPass> args ){
        this.args = args;
    }
    //endregion

    //region callable() : boolean

    /**
     * Возвращает признак что можно сделать вызов функции {@link #call()}
     * @return true - допускается при заданных аргументах вызвать {@link #call()}, false - не допускается
     */
    public boolean callable(){
        List<ArgPass> args = getArgs();
        if( args == null ) return false;

        TypedFn method = getFn();
        if( method == null ) return false;

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

    /**
     * Вызов функии с заданными аргументами
     * @return результат вызова
     */
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
