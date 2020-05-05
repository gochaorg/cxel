package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.List;


@SuppressWarnings({ "rawtypes", "unused", "ConstantConditions" })
public class ModuloOperator extends BaseOperator {
    @FnName("%") public static Double mod(Double left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left % right;
    }

    @FnName("%") public static Double mod(Object left, Object right){
        return (left == null ? 0.0 : Double.NaN) % (right == null ? 0.0 : Double.NaN);
    }

    @FnName("%") public static Double mod(Object left, Double right){
        if( right==null )throw new IllegalArgumentException("right==null");
        return (left == null ? 0.0 : Double.NaN) % right;
    }

    @FnName("%") public static Double mod(Double left, Object right){
        if( left==null )throw new IllegalArgumentException("left==null");
        return left % (right == null ? 0.0 : Double.NaN);
    }

    @FnName("%") public static Double mod(List left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % toNumber(right);
    }

    @FnName("%") public static Double mod(List left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % right;
    }

    @FnName("%") public static Double mod(Double left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left % toNumber(right);
    }

    @FnName("%") public static Double mod(Boolean left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % toNumber(right);
    }

    @FnName("%") public static Double mod(Double left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left % toNumber(right);
    }

    @FnName("%") public static Double mod(Boolean left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % right;
    }

    @FnName("%") public static Double mod(String left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % toNumber(right);
    }

    @FnName("%") public static Double mod(String left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) % right;
    }

    @FnName("%") public static Double mod(Double left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left % toNumber(right);
    }
}
