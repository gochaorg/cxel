package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

/**
 * Унарные операции
 */
public class UnaryOperations {
    @FnName("!") public static boolean not( boolean a ){ return !a; }
    @FnName("!") public static Boolean not( Boolean a ){ return !a; }

    @FnName("-") public static byte minus( byte v ){ return (byte)(0-v); }
    @FnName("-") public static Byte minus( Byte v ){ return (byte)(0-v); }
    @FnName("-") public static short minus( short v ){ return (short) (0-v); }
    @FnName("-") public static Short minus( Short v ){ return (short) (0-v); }
    @FnName("-") public static int minus( int v ){ return -v; }
    @FnName("-") public static Integer minus( Integer v ){ return -v; }
    @FnName("-") public static long minus( long v ){ return -v; }
    @FnName("-") public static Long minus( Long v ){ return -v; }
    @FnName("-") public static float minus( float v ){ return -v; }
    @FnName("-") public static Float minus( Float v ){ return -v; }
    @FnName("-") public static double minus( double v ){ return -v; }
    @FnName("-") public static Double minus( Double v ){ return -v; }
}
