package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.JsLexer;
import xyz.cofe.cxel.js.Undef;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("rawtypes")
public class EqualsOperator extends BaseOperator {
    @FnName("==")
    public static Boolean eq( Boolean left, Boolean right ){
        return Objects.equals(left, right);
    }

    @FnName("==")
    public static Boolean eq( List left, List right ){
        return Objects.equals(left,right);
    }

    @SuppressWarnings("ConstantConditions")
    @FnName("==")
    public static Boolean eq(Object left, Object right){
        if( left==null && right==null )return true;
        if( left!=null && right==null )return false;
        if( left==null && right!=null )return false;

        if( left==right )return true;
        if( Objects.equals(left,right) )return true;
        
        return false;
    }

    //region String
    @FnName("==")
    public static Boolean eq( String left, Boolean right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        Boolean b = left.length()!=0;
        return Objects.equals(b,right);
    }

    @FnName("==")
    public static Boolean eq( Boolean left, String right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return eq( right, left );
    }

    @FnName("==")
    public static Boolean eq( String left, String right ){
        return Objects.equals(left,right);
    }

    @FnName("==")
    public static Boolean eq( String left, Object right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )return false;
        if( right instanceof List ){
            return Objects.equals(left, toString((List)right));
        }
        return false;
    }

    @FnName("==")
    public static Boolean eq( Object left, String right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        return eq( right, left );
    }
    //endregion

    //region double
    @FnName("==")
    public static Boolean eq(Boolean left, Double right){
        return eq( right, left );
    }

    @FnName("==")
    public static Boolean eq(Double left, Boolean right){
        if( left==null ) left = 0.0;
        Double r = toNumber(right);
        return eq( left, r );
    }

    @FnName("==")
    public static Boolean eq(Object left, Double right){
        return eq( right, left );
    }

    @FnName("==")
    public static Boolean eq(Double left, Object right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( Double.isNaN(left) )return false;
        if( right==null ){
            return eq( left, 0.0 );
        }
        return false;
    }

    @FnName("==")
    public static Boolean eq(Double left,String right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        if( Double.isNaN(left) )return false;

        Double rnum = toNumber(right);
        return left.equals(rnum);
    }

    @FnName("==")
    public static Boolean eq(String left,Double right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return eq( right, left );
    }

    @SuppressWarnings({ "rawtypes", "WrapperTypeMayBePrimitive" })
    @FnName("==")
    public static Boolean eq( Double left, List right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        if( Double.isNaN(left) )return false;

        Double num = toNumber(right);
        if( Double.isNaN(num) )return false;

        return left.equals(num);
    }

    @SuppressWarnings({ "rawtypes" })
    @FnName("==")
    public static Boolean eq( List left, Double right ){
        return eq( right, left );
    }

    @SuppressWarnings("ConstantConditions")
    @FnName("==")
    public static Boolean eq( Double left, Double right ){
        if( left==null && right==null )return true;
        if( left!=null && right==null )return false;
        if( left==null && right!=null )return false;
        if( Double.isNaN(left) )return false;
        if( Double.isNaN(right) )return false;
        return left.equals(right);
    }
    //endregion

    //region undefined
    @FnName("==")
    public static Boolean eq(Undef left, Undef right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return true;
    }

    @FnName("==")
    public static Boolean eq(Object left, Undef right){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return true;
        return Undef.instance.equals(left);
    }

    @FnName("==")
    public static Boolean eq(Undef left, Object right){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )return true;
        return Undef.instance.equals(right);
    }
    //endregion
}
