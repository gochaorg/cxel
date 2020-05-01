package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unused" })
public class RShiftOpeator extends BaseOperator {
    public static Double rshift( Object left, Object right ){ return 0.0; }
    @FnName(">>") public static Double rshift( Boolean left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        long lft = left ? 1 : 0;
        return (double) lft;
    }
    @FnName(">>") public static Double rshift( Object left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        return (double) 0.0;
    }
    @FnName(">>") public static Double rshift( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left ? 1 : 0;
        long rht = right ? 1 : 0;
        return (double) (lft >> rht);
    }
    @FnName(">>") public static Double rshift( Boolean left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left ? 1 : 0;
        return (double) (lft >> right.longValue());
    }
    @FnName(">>") public static Double rshift( Double left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left.longValue();
        long rht = right ? 1 : 0;
        return (double) (lft >> rht);
    }
    @FnName(">>") public static Double rshift( Double left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left.longValue();
        long rht = right.longValue();
        return (double) (lft >> rht);
    }
    @FnName(">>") public static Double rshift( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        return (double) ( ((long)dlft) >> right.longValue());
    }
    @FnName(">>") public static Double rshift( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >> ((long)drht) );
    }
    @FnName(">>") public static Double rshift( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = left;
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >> ((long)drht) );
    }
    @FnName(">>") public static Double rshift( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >> ((long)drht) );
    }
    @FnName(">>") public static Double rshift( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >> ((long)drht) );
    }
    @FnName(">>") public static Double rshift( List left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        //if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = 0;
        return (double) ( ((long)dlft) >> ((long)drht) );
    }
    @FnName(">>") public static Double rshift( Object left, List right ){
        //if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return (double) 0;
    }
}
