package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.Objects;

/**
 * Оператор {@link xyz.cofe.cxel.Keyword#And}
 */
public class AndOperator extends BaseOperator {
    //region null references
    @FnName("&&")
    public static Object and( Object left, Object right ){
        if( left==null )return null;
        if( right==null ){
            if( left instanceof Undef )return left;
            if( left instanceof Boolean && Objects.equals(false,left) )return left;
            if( left instanceof Number && ((Number)left).doubleValue()==0 )return left;
            if( left instanceof Number && Double.isNaN(((Number)left).doubleValue()) )return left;
            if( left instanceof Number )return null;
            if( left instanceof String && ((String)left).length()==0 )return left;
        }
        return right;
    }
    //endregion

    @FnName("&&")
    public static Object and( Undef left, Object right ){
        if( left!=null )throw new IllegalArgumentException("left!=null");
        return left;
    }

    @FnName("&&")
    public static Object and( Boolean left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        return left ? right : left;
    }

    @FnName("&&")
    public static Object and( Number left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        double n = left.doubleValue();
        if( n==0 )return left;
        if( Double.isNaN(n) )return left;
        return right;
    }


    @FnName("&&")
    public static Object and( String left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        if( left.length()==0 )return left;
        return right;
    }

    @FnName("&&")
    public static Object and( Object left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return left;
        return right;
    }

    @FnName("&&")
    public static Object and( Undef left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left;
    }
    //endregion

    //region boolean types
    @FnName("&&")
    public static Object and( Boolean left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        return left ? right : left;
    }

    @FnName("&&")
    public static Object and( Undef left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left;
    }

    @FnName("&&")
    public static Object and( Number left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        double n = left.doubleValue();
        if( Double.isNaN(n) )return n;
        if( n==0 )return n;
        return right;
    }

    @FnName("&&")
    public static Object and( String left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left.length()==0 )return left;
        return right;
    }

    @FnName("&&")
    public static Object and( Object left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return null;
        return right;
    }

    @FnName("&&")
    public static Boolean and( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left && right;
    }
    //endregion
}
