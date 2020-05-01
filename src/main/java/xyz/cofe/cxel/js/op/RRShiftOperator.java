package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.List;

@SuppressWarnings({ "rawtypes", "unused" })
public class RRShiftOperator extends BaseOperator {
    public static Double rrshift( Object left, Object right ){ return 0.0; }
    @FnName(">>>") public static Double rrshift( Boolean left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        long lft = left ? 1 : 0;
        return (double) lft;
    }
    @FnName(">>>") public static Double rrshift( Object left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        return (double) 0.0;
    }
    @FnName(">>>") public static Double rrshift( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left ? 1 : 0;
        long rht = right ? 1 : 0;
        return (double) (lft >>> rht);
    }
    @FnName(">>>") public static Double rrshift( Boolean left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left ? 1 : 0;
        return (double) (lft >>> right.longValue());
    }
    @FnName(">>>") public static Double rrshift( Double left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left.longValue();
        long rht = right ? 1 : 0;
        return (double) (lft >>> rht);
    }
    @FnName(">>>") public static Double rrshift( Double left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        long lft = left.longValue();
        long rht = right.longValue();
        return (double) (lft >>> rht);
    }
    @FnName(">>>") public static Double rrshift( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        return (double) ( ((long)dlft) >>> right.longValue());
    }
    @FnName(">>>") public static Double rrshift( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >>> ((long)drht) );
    }
    @FnName(">>>") public static Double rrshift( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = left;
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >>> ((long)drht) );
    }
    @FnName(">>>") public static Double rrshift( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >>> ((long)drht) );
    }
    @FnName(">>>") public static Double rrshift( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = toNumber(right);
        return (double) ( ((long)dlft) >>> ((long)drht) );
    }
    @FnName(">>>") public static Double rrshift( List left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        //if( right==null )throw new IllegalArgumentException("right==null");
        double dlft = toNumber(left);
        double drht = 0;
        return (double) ( ((long)dlft) >>> ((long)drht) );
    }
    @FnName(">>>") public static Double rrshift( Object left, List right ){
        //if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return (double) 0;
    }
}
