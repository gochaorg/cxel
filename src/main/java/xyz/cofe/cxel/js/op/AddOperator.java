package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

// TODO fuck js
// > [1] + true
// '1true'
// > [1] - true
// 0


@SuppressWarnings({ "rawtypes", "unused" })
public class AddOperator extends BaseOperator {
    @FnName("+")
    public static Object add( Undef left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( List left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Undef left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Boolean left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Undef left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Double left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Undef left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Object left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Undef left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return Double.NaN;
    }

    @FnName("+")
    public static Object add( Undef left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left.toString() + right;
    }

    @FnName("+")
    public static Object add( String left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left + right.toString();
    }

    @FnName("+")
    public static Object add( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toString(left) + right;
    }

    @FnName("+")
    public static Object add( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left + toString(right);
    }

    @FnName("+")
    public static Object add( Object left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return right ? 1.0 : 0.0;
        return OBJECT_LITERAL + right.toString();
    }

    @FnName("+")
    public static Object add( Boolean left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null ) return add( left ? 1.0 : 0.0, 0.0 );
        return left.toString() + OBJECT_LITERAL;
    }

    @FnName("+")
    public static Object add( Boolean left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return add( left ? 1.0 : 0.0, right ? 1.0 : 0.0 );
    }

    @FnName("+")
    public static Object add( Double left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return add( left, right ? 1.0 : 0.0 );
    }

    @FnName("+")
    public static Object add( Boolean left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return add( left ? 1.0 : 0.0, right );
    }

    @FnName("+")
    public static Object add( Object left, Double right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return add( 0.0, right );
        return add( OBJECT_LITERAL, right.toString() );
    }

    @FnName("+")
    public static Object add( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return add( OBJECT_LITERAL, right.toString() );
    }

    @FnName("+")
    public static Object add( Double left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )return add( left, 0.0 );
        return add( left.toString(),OBJECT_LITERAL );
    }

    @FnName("+")
    public static Object add( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return add( left.toString(), toString((List) right) );
    }

    @FnName("+")
    public static Object add( Double left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( Double.isNaN(left) )return left;
        if( Double.isNaN(right) )return right;
        return left + right;
    }

    @FnName("+")
    public static Object add( Object left, Object right ){
        if( left==null ){
            if( right==null )return 0;
            return "null"+OBJECT_LITERAL;
        }else{
            if( right==null )return OBJECT_LITERAL+"null";
            return OBJECT_LITERAL +OBJECT_LITERAL;
        }
    }

    @FnName("+")
    public static String add( String left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left + right;
    }

    @FnName("+")
    public static String add( String left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        String rht = right==null ? "null" : OBJECT_LITERAL;
        return left + rht;
    }

    @FnName("+")
    public static String add( Object left, String right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        String lft = left==null ? "null" : OBJECT_LITERAL;
        return lft + right;
    }

    @FnName("+")
    public static String add( String left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left + toString(right);
    }

    @FnName("+")
    public static String add( List left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toString(left) + right;
    }

    @FnName("+")
    public static String add( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return toString(left) + toString(right);
    }
}
