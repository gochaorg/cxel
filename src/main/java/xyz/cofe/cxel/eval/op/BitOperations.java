package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

/**
 * Операции над битами
 */
public class BitOperations {
    @FnName("|") public static long or(long a, long b){ return a | b; }
    @FnName("&") public static long and(long a, long b){ return a & b; }
    @FnName("^") public static long xor(long a, long b){ return a ^ b; }
    @FnName("<<") public static long leftShift(long a, int b){ return a << b; }
    @FnName(">>") public static long rightShift(long a, int b){ return a >> b; }
    @FnName(">>>") public static long rrightShift(long a, int b){ return a >>> b; }
    @FnName("~") public static long bitNot(long a){ return ~a; }
}
