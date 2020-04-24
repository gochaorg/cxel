package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

public class BitXorOperator extends BitOperator {
    @FnName("^")
    public static Double bitOr( Object left, Object right ){
        long l = toBit(left);
        long r = toBit(right);
        long bits = l ^ r;
        return (double)bits;
    }
}
