package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings({ "rawtypes", "SimplifiableConditionalExpression", "ConstantConditions" })
public class LessOperator extends BaseOperator {
    @FnName("<")
    public static Boolean less( Object left, Object right ){
        if( left==null && right==null )return false;
        return false;
    }

    @FnName("<")
    public static Boolean less( List left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        Optional<Object> f = first(left);
        if( !f.isPresent() )return less( 0.0, right );
        if( f.get() instanceof Number )return less( ((Number)f.get()).doubleValue(), right );
        if( f.get() instanceof String )return less( toNumber((String)f.get()), right );
        if( f.get() instanceof Boolean )return less( toNumber((Boolean)f.get()), right );
        if( f.get()==null )return less( 0.0, right );
        return false;
    }

    @FnName("<")
    public static Boolean less( List left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        Optional<Object> lft = first(left);
        if( lft!=null && lft.isPresent() && lft.get()!=null ){
            Object l = lft.get();
            if( l instanceof Number ){
                return ((Double)l) < toNumber(right);
            }else if( l instanceof String ){
                return less( (String)l, toString(right) );
            }
        }
        return toNumber(left) < toNumber(right);
    }

    @FnName("<")
    public static Boolean less( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return !less( right, left );
    }

    @FnName("<")
    public static Boolean less( List left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        Boolean bleft = null;
        Optional<Object> f = first(left);
        if( !f.isPresent() ){
            bleft = false;
        }else{
            Object o = f.get();
            bleft = o==null ? false : ((o instanceof Boolean)? (Boolean)o : false);
        }

        if( !right )return false;
        return !bleft;
    }

    @FnName("<")
    public static Boolean less( Boolean left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return !less( right, left );
    }

    @FnName("<")
    public static Boolean less( String left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left.compareTo(right) < 0;
    }

    @FnName("<")
    public static Boolean less( Object left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return right;
        return false;
    }

    @FnName("<")
    public static Boolean less( Undef left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        return false;
    }

    @FnName("<")
    public static Boolean less( Boolean left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        return false;
    }

    @FnName("<")
    public static Boolean less( Boolean left, Boolean right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )throw new IllegalArgumentException("left==null");
        if( left && right )return false;
        if( left && !right )return false;
        if( !left && right )return true;
        if( !left && !right )return false;

        return false;
    }

    @FnName("<")
    public static Boolean less( Boolean left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( Double.isNaN(right) )return false;
        double n = left ? 1 : 0;
        return n < right;
    }

    @FnName("<")
    public static Boolean less( Double left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( Double.isNaN(left) )return false;
        double n = right ? 1 : 0;
        return left < n;
    }

    @FnName("<")
    public static Boolean less( Double left, Double right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( Double.isNaN(left) )return false;
        if( Double.isNaN(right) )return false;
        return left < right;
    }

    //region Undef
    @FnName("<")
    public static Boolean less( Object left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        return false;
    }

    @FnName("<")
    public static Boolean less( Undef left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        return false;
    }

    @FnName("<") public static Boolean less( Undef left, Undef right ){ return false; }
    //endregion
}
