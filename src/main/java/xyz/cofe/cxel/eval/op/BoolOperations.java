package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

public class BoolOperations {
    @FnName("|") public static boolean or( boolean a, boolean b ){ return a || b; }
    @FnName("|") public static Boolean or( Boolean a, Boolean b ){ return a || b; }

    @FnName("&") public static boolean and( boolean a, boolean b ){ return a && b; }
    @FnName("&") public static Boolean and( Boolean a, Boolean b ){ return a && b; }

//    @FnName("!") public static boolean not( boolean a ){ return !a; }
//    @FnName("!") public static Boolean not( Boolean a ){ return !a; }
}
