package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

import static xyz.cofe.cxel.js.op.LessOperator.*;
import static xyz.cofe.cxel.js.op.EqualsOperator.*;

// TODO: fuck js
// > let a = {}
// undefined
// > let b = {}
// > a < b
// false
// > a <= b
// true
// > a == b
// false

@SuppressWarnings({ "rawtypes", "unused" })
public class LessOrEqualsOperator {
    @FnName("<=")
    public static Boolean lessOrEq(Object left, Object right){
        if( left!=null && right!=null )return true;
        return false;
    }

    @FnName("<=")
    public static Boolean lessOrEq(Double left, Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( String left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Object left, Boolean right ){
        //if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Undef left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Boolean left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Boolean left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Double left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Object left, Undef right ){
        //if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Undef left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        //if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }

    @FnName("<=")
    public static Boolean lessOrEq( Undef left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return less(left,right) || eq(left, right);
    }
}
