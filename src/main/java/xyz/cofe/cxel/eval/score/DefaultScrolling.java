package xyz.cofe.cxel.eval.score;

import xyz.cofe.cxel.eval.*;

import java.util.function.Consumer;

/**
 * Оценка приоритетата вызова между множеством значений
 */
public class DefaultScrolling implements CallScoring<PreparedCall> {
    public DefaultScrolling(){
    }

    public DefaultScrolling( DefaultScrolling sample ){
        if( sample!=null ){
            paramCountPowerOffset = sample.paramCountPowerOffset;
            invariantPower = sample.invariantPower;
            primitiveCastPower = sample.primitiveCastPower;
            covariantPower = sample.covariantPower;
            impicitPower = sample.impicitPower;
            castLooseDataPower = sample.castLooseDataPower;
            argsCasingPower = sample.argsCasingPower;
            contextPreparedCallsOffset = sample.contextPreparedCallsOffset;
            reflectPreparedCallsOffset = sample.reflectPreparedCallsOffset;
        }
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public DefaultScrolling clone(){
        return new DefaultScrolling(this);
    }

    protected DefaultScrolling cloneAndConf( Consumer<DefaultScrolling> conf ){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        DefaultScrolling c = clone();
        conf.accept(c);
        return c;
    }

    protected int paramCountPowerOffset = 1;
    public int paramCountPowerOffset(){ return paramCountPowerOffset; }
    public DefaultScrolling paramCountPowerOffset(int v){ return cloneAndConf(c->c.paramCountPowerOffset=v); }

    protected int invariantPower = 0;
    public int invariantPower(){ return invariantPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для данных передаваемых юез изменения типа
     * @param v вес, значение по умолчанию - 0
     * @return клон с новыми значениями
     */
    public DefaultScrolling invariantPower(int v){ return cloneAndConf(c->c.invariantPower=v); }

    protected int primitiveCastPower = 1;
    public int primitiveCastPower(){ return primitiveCastPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для преобразования примитивных числовых типов данных аргументов функции
     * @param v вес, значение по умолчанию - 1
     * @return клон с новыми значениями
     */
    public DefaultScrolling primitiveCastPower(int v){ return cloneAndConf(c->c.primitiveCastPower=v); }

    protected int covariantPower = 2;
    public int covariantPower(){ return covariantPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для ко-вариантных типов данных аргументов функции
     * @param v вес, значение по умолчанию - 2
     * @return клон с новыми значениями
     */
    public DefaultScrolling covariantPower(int v){ return cloneAndConf(c->c.covariantPower=v); }

    protected int impicitPower = 3;
    public int impicitPower(){ return impicitPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для неявного преобразования типов данных аргументов функции
     * @param v вес, значение по умолчанию - 3
     * @return клон с новыми значениями
     */
    public DefaultScrolling impicitPower(int v){ return cloneAndConf(c->c.impicitPower=v); }

    protected int castLooseDataPower = 4;
    public int castLooseDataPower(){ return castLooseDataPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для преобразования примитвных типов данных, с потерей точности аргументов функции
     * @param v вес, значение по умолчанию - 4
     * @return клон с новыми значениями
     */
    public DefaultScrolling castLooseDataPower(int v){ return cloneAndConf(c->c.castLooseDataPower=v); }

    protected int argsCasingPower = 5;
    public int argsCasingPower(){ return argsCasingPower; }

    /**
     * Клонирует и изменяет вес (степень - {@link Math#pow(double, double)})
     * для суммы отклонений кол-ва передаваемых и принимаемых параметров
     * @param v вес, значение по умолчанию - 5
     * @return клон с новыми значениями
     */
    public DefaultScrolling argsCasingPower(int v){ return cloneAndConf(c->c.argsCasingPower=v); }

    protected int contextPreparedCallsOffset = 0;
    public int contextPreparedCallsOffset(){ return contextPreparedCallsOffset; }
    public DefaultScrolling contextPreparedCallsOffset(int v){ return cloneAndConf(c->c.contextPreparedCallsOffset=v); }

    protected int reflectPreparedCallsOffset = 1;
    public int reflectPreparedCallsOffset(){ return reflectPreparedCallsOffset; }
    public DefaultScrolling reflectPreparedCallsOffset(int v){ return cloneAndConf(c->c.reflectPreparedCallsOffset=v); }

    @Override
    public int calculate(PreparedCall preparedCall, PreparingCalls scope ){
        if( preparedCall==null )throw new IllegalArgumentException( "preparedCall==null" );
        int score = 0;

        if( scope instanceof ContextPreparingCalls ){
            score += contextPreparedCallsOffset;
        }

        if( scope instanceof ReflectPreparingCalls ){
            score += reflectPreparedCallsOffset;
        }

        if( preparedCall instanceof ParameterCount ){
            int pcount = ((ParameterCount) preparedCall).parameterCount()+ paramCountPowerOffset;

            if( preparedCall instanceof InvariantArgs ){
                int invCalls = ((InvariantArgs) preparedCall).invariantArgs();
                score += invCalls*Math.pow(pcount,invariantPower);
            }

            if( preparedCall instanceof PrimitiveCastArgs ){
                int primCastCalls = ((PrimitiveCastArgs) preparedCall).primitiveCastArgs();
                score += primCastCalls*Math.pow(pcount,primitiveCastPower);
            }

            if( preparedCall instanceof CovariantArgs ){
                int coCalls = ((CovariantArgs) preparedCall).covariantArgs();
                score += coCalls*Math.pow(pcount,covariantPower);
            }

            if( preparedCall instanceof ImplicitArgs ){
                int implCalls = ((ImplicitArgs) preparedCall).implicitArgs();
                score += implCalls*Math.pow(pcount,impicitPower);
            }

            if( preparedCall instanceof CastLooseDataArgs ){
                int loseDataCalls = ((CastLooseDataArgs) preparedCall).castLooseDataArgs();
                score += loseDataCalls*Math.pow(pcount,castLooseDataPower);
            }

            if( preparedCall instanceof ArgsCasing ){
                int argsCasing = ((ArgsCasing) preparedCall).argsCasing();
                score += argsCasing*Math.pow(pcount+1,argsCasingPower);
            }
        }

        return score;
    }
}
