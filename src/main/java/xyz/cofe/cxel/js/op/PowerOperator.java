package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.List;


@SuppressWarnings({ "rawtypes", "unused", "ConstantConditions" })
public class PowerOperator extends BaseOperator {
    @FnName("**") public static Double pow(Double left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow(left, right);
    }

    @FnName("**") public static Double pow(Object left, Object right){
        return Math.pow( (left == null ? 0.0 : Double.NaN) , (right == null ? 0.0 : Double.NaN) );
    }

    @FnName("**") public static Double pow(Object left, Double right){
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( (left == null ? 0.0 : Double.NaN) , right );
    }

    @FnName("**") public static Double pow(Double left, Object right){
        if( left==null )throw new IllegalArgumentException("left==null");
        return Math.pow( left , (right == null ? 0.0 : Double.NaN) );
    }

    @FnName("**") public static Double pow(List left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , toNumber(right) );
    }

    @FnName("**") public static Double pow(List left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , right );
    }

    @FnName("**") public static Double pow(Double left, List right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( left , toNumber(right) );
    }

    @FnName("**") public static Double pow(Boolean left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , toNumber(right) );
    }

    @FnName("**") public static Double pow(Double left, Boolean right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( left , toNumber(right) );
    }

    @FnName("**") public static Double pow(Boolean left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , right );
    }

    @FnName("**") public static Double pow(String left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , toNumber(right) );
    }

    @FnName("**") public static Double pow(String left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( toNumber(left) , right );
    }

    @FnName("**") public static Double pow(Double left, String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Math.pow( left , toNumber(right) );
    }
}
