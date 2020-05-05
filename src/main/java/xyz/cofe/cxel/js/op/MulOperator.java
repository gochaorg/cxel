package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;


@SuppressWarnings({ "rawtypes", "unused", "ConstantConditions" })
public class MulOperator extends BaseOperator {
    @FnName("*") public static Double mul(Double left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left * right;
    }

    @FnName("*") public static Double mul(Object left, Object right){
        return (left == null ? 0.0 : Double.NaN) * (right == null ? 0.0 : Double.NaN);
    }

    @FnName("*") public static Double mul(Object left, Double right){
        if( right==null )throw new IllegalArgumentException("right==null");
        return (left == null ? 0.0 : Double.NaN) * right;
    }

    @FnName("*") public static Double mul(Double left, Object right){
        if( left==null )throw new IllegalArgumentException("left==null");
        return left * (right == null ? 0.0 : Double.NaN);
    }

    @FnName("*") public static Double mul(List left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * toNumber(right);
    }

    @FnName("*") public static Double mul(List left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * right;
    }

    @FnName("*") public static Double mul(Double left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left * toNumber(right);
    }

    @FnName("*") public static Double mul(Boolean left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * toNumber(right);
    }

    @FnName("*") public static Double mul(Double left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left * toNumber(right);
    }

    @FnName("*") public static Double mul(Boolean left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * right;
    }

    @FnName("*") public static Double mul(String left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * toNumber(right);
    }

    @FnName("*") public static Double mul(String left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toNumber(left) * right;
    }

    @FnName("*") public static Double mul(Double left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left * toNumber(right);
    }
}
