package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

public class BitOrOperator extends BitOperator {
    @FnName("|")
    public static Double bitOr( Object left, Object right ){
        long l = toBit(left);
        long r = toBit(right);
        long bits = l | r;
        return (double)bits;
    }
}
