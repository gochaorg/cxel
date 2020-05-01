package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

import static xyz.cofe.cxel.js.op.LessOperator.*;

@SuppressWarnings({ "rawtypes", "SimplifiableConditionalExpression", "ConstantConditions" })
public class MoreOrEqualsOperator {
    @FnName(">=") public static Boolean moreOrEq(Object left, Object right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(List left, Double right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(List left, List right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Double left, List right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(List left, Boolean right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Boolean left, List right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(String left, String right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Object left, Boolean right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Undef left, Boolean right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Boolean left, Undef right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Boolean left, Boolean right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Boolean left, Double right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Double left, Boolean right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Double left, Double right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Object left, Undef right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Undef left, Object right){ return !less(left, right); }
    @FnName(">=") public static Boolean moreOrEq(Undef left, Undef right){ return !less(left, right); }
}
