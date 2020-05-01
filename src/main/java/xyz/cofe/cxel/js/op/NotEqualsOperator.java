package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;
import static xyz.cofe.cxel.js.op.EqualsOperator.*;

import java.util.List;

public class NotEqualsOperator {
    @FnName("!=") public static Boolean neq( Boolean left, Boolean right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( List left, List right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Object left, Object right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( String left, Boolean right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( Boolean left, String right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( String left, String right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( String left, Object right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( Object left, String right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Boolean left, Double right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Double left, Boolean right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Object left, Double right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Double left, Object right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Double left,String right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(String left,Double right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( Double left, List right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( List left, Double right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( Double left, Double right ){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq( Undef left, Undef right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Object left, Undef right){ return !eq(left, right); }
    @FnName("!=") public static Boolean neq(Undef left, Object right){ return !eq(left, right); }
}
