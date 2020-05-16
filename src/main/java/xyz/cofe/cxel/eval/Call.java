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
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public Call(Call sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        this.args.addAll(sample.args);
        this.fn = sample.fn;
        this.inputArgs = sample.inputArgs;
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

    /**
     * Клонирование
     * @return клон
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Call clone(){
        return new Call(this);
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
     * @param newfn вызываемая функция
     */
    public Call fn(TypedFn newfn){
        return clone().configure( c->c.fn = newfn );
    }
    //endregion
    //region аргументы вызова
    /**
     * Возвращает список передаваемых аргументов
     */
    protected final List<ArgPass> args = new ArrayList<>();

    /**
     * Возвращает кол-во аргументов
     * @return кол-во аргументов
     */
    public int getArgsCount(){ return args.size(); }

    /**
     * Возвращает аргумент по индексу
     * @param argIndex индекс аргумента
     * @return аргумент
     */
    public ArgPass getArg( int argIndex ){
        return args.get(argIndex);
    }

    /**
     * Добавляет аргумент
     * @param arg аргумент
     * @return self ссылка
     */
    public Call addArg( ArgPass arg ){
        if( arg==null )throw new IllegalArgumentException("arg==null");
        args.add(arg.setCall(this));
        return this;
    }

    /**
     * Удаляет аргумент
     * @param idx индекс аргумента
     * @return self ссылка
     */
    public Call removeArg( int idx ){
        //return clone().configure( c->c.args.remove(idx) );
        args.remove(idx);
        return this;
    }

    /**
     * Указывает значение аргумента
     * @param idx индекс аргумента
     * @param arg ардгумент
     * @return self ссылка
     */
    public Call setArg( int idx, ArgPass arg ){
        if( arg==null )throw new IllegalArgumentException("arg==null");
        //return clone().configure( c->c.args.set(idx,arg) );
        args.set(idx,arg);
        return this;
    }
    //endregion
    //region callable() : boolean - проверка возможности вызова
    /**
     * Возвращает признак что можно сделать вызов функции {@link #call(List)}
     * @return true - допускается при заданных аргументах вызвать {@link #call(List)}, false - не допускается
     */
    public boolean callable(){
        List<ArgPass> args = this.args;

        TypedFn method = getFn();
        if( method == null ) return false;

        List<ArgPass> nonNullArgs = args.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if( nonNullArgs.stream().anyMatch(m -> !m.isPassable()) ) return false;

        Map<Integer, Boolean> argPassable = new LinkedHashMap<>();
        for(int i = 0; i < method.getParameterTypes().length; i++ ) argPassable.put(i, false);
        nonNullArgs.forEach(m -> {
            argPassable.put(m.index(), m.isPassable());
        });

        return argPassable.values().stream().allMatch(e -> e);
    }
    //endregion
    //region call( List ) : object - вызов метода/функции
    /**
     * Вызов функии с заданными аргументами
     * @return результат вызова
     */
    @Override
    public Object call( List<Object> inputArgs ){
        this.inputArgs = inputArgs;
        if( !callable() ) throw new EvalError("can't calling");

        TypedFn f = getFn();
        Object[] params = new Object[f.getParameterTypes().length];

        args.stream().filter(Objects::nonNull).forEach(pa -> {
            if( pa.index() >= 0 && pa.index() < params.length )
                params[pa.index()] = pa.arg();
        });

        //Object res = f.call(params);
        //System.out.println("call "+f+" input: "+this.inputArgs+" output: "+Arrays.asList(params)+" result: "+res);
        //return res;

        return f.call(params);
    }
    //endregion

    //region характеристики
    protected Boolean cacheable;

    /**
     * Возвращает факт возможности повторного вызова
     * с новыми аргментами без перестройки нового {@link Call}
     * @return true - можно повторно вызывать
     */
    public boolean cacheable(){
        if( cacheable!=null )return cacheable;
        cacheable = args.stream().filter(ArgPass::isCacheable).count()>0;
        return cacheable;
    }

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
        return (int)args.stream().filter(ArgPass::isInvarant).count();
    }

    @Override
    public int primitiveCastArgs(){
        return (int)args.stream().filter(ArgPass::isPrimitiveCast).count();
    }

    @Override
    public int castLooseDataArgs(){
        return (int)args.stream().filter(ArgPass::isCastLooseData).count();
    }

    @Override
    public int covariantArgs(){
        return (int)args.stream().filter(ArgPass::isCovariant).count();
    }

    @Override
    public int implicitArgs(){
        return (int)args.stream().filter(ArgPass::isImplicit).count();
    }

    @Override
    public int parameterCount(){
        TypedFn method = getFn();
        if( method!=null ){
            return method.getParameterTypes().length;
        }
        return 0;
    }
    //endregion

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
