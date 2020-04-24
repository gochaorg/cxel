package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.Objects;

public class StrongEqualsOperator {
    @SuppressWarnings("ConstantConditions")
    @FnName("===")
    public static Boolean eq( Object left, Object right ){
        if( left == null && right == null ) return true;
        if( left != null && right == null ) return false;
        if( left == null && right != null ) return false;
        if( left == right ) return true;
        if( Objects.equals(left, right) ) return true;
        if( left instanceof Number && right instanceof Number ){
            double nl = ((Number) left).doubleValue();
            double nr = ((Number) right).doubleValue();
            return nl==nr;
        }
        return false;
    }

    @FnName("!==")
    public static Boolean nEq( Object left, Object right ){
        return !eq(left,right);
    }
}
