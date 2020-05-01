package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.List;

import static xyz.cofe.cxel.js.op.LessOrEqualsOperator.*;

@SuppressWarnings({ "rawtypes", "unused" })
public class MoreOperator {
    @FnName(">") public static Boolean more(Object left, Object right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Double left, Double right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(List left, List right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(List left, Double right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Double left, List right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(List left, Boolean right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Boolean left, List right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(String left, String right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Object left, Boolean right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Undef left, Boolean right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Boolean left, Undef right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Boolean left, Boolean right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Boolean left, Double right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Double left, Boolean right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Object left, Undef right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Undef left, Object right){ return !lessOrEq(left, right); }
    @FnName(">") public static Boolean more(Undef left, Undef right){ return !lessOrEq(left, right); }
}
