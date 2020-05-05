package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unused" })
public class SubOperator extends BaseOperator {
    @FnName("-") public static Object sub( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( Double left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Undef left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Object left, Double right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( left==null ? 0.0 : Double.NaN, right );
    }

    @FnName("-") public static Object sub( Double left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        return sub( left, right==null ? 0.0 : Double.NaN );
    }

    @FnName("-") public static Object sub( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( Undef left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Boolean left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( left, toNumber(right) );
    }

    @FnName("-") public static Object sub( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), right );
    }

    @FnName("-") public static Object sub( String left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( List left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( String left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return sub( toNumber(left), toNumber(right) );
    }

    @FnName("-") public static Object sub( String left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Undef left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Double left, Double right ){
        if( left==null && right==null )return 0.0;
        if( left==null && right!=null )return 0.0 - right;
        if( left!=null && right==null )return left;
        if( Double.isNaN(left) )return left;
        if( Double.isNaN(right) )return right;
        return left - right;
    }

    @FnName("-") public static Object sub( Object left, Object right ){
        if( left==null && right==null )return 0.0;
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Object left, Undef right ){
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Undef left, Object right ){
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Undef left, Undef right ){
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Double left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        double rht = toNumber(right);
        return sub(left,rht);
    }

    @FnName("-") public static Object sub( String left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        double lft = toNumber(left);
        return sub(lft,right);
    }

    @FnName("-") public static Object sub( String left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        double lft = toNumber(left);
        if( right==null )return lft;
        return Double.NaN;
    }

    @FnName("-") public static Object sub( Object left, String right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        double rht = toNumber(right);
        if( left==null )return rht;
        return Double.NaN;
    }

    @FnName("-") public static Object sub( String left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double lft = toNumber(left);
        if( Double.isNaN(lft) )return lft;
        return lft - (right ? 1.0 : 0.0);
    }

    @FnName("-") public static Object sub( Boolean left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        double rht = toNumber(right);
        if( Double.isNaN(rht) )return rht;

        return (left ? 1.0 : 0.0) - rht;
    }
}
