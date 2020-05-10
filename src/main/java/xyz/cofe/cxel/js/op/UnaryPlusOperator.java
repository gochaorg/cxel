package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;

public class UnaryPlusOperator {
    @FnName("+")
    public static Double uplus( Double value ){
        if( value==null )return +0.0;
        return value;
    }
}
