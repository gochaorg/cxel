package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.Objects;

/**
 * Операции над целыми числами
 */
public class ByteOperators {
//    @FnName("+") public static byte add( byte a, byte b ){ return (byte)(a+b); }
//    @FnName("+") public static Byte add( Byte a, Byte b ){ return (byte)(a+b); }
//    @FnName("+") public static Short add( Byte a, Short b ){ return (short)(a+b); }
//    @FnName("+") public static Integer add( Byte a, Integer b ){ return (int)(a+b); }
//    @FnName("+") public static Long add( Byte a, Long b ){ return (long)(a+b); }
//    @FnName("+") public static Float add( Byte a, Float b ){ return (float)(a+b); }
//    @FnName("+") public static Double add( Byte a, Double b ){ return (double)(a+b); }

    @FnName("+") public static byte add( byte a, byte b ){ return (byte)(a + b); }
    @FnName("+") public static Byte add( byte a, Byte b ){ return (Byte)(byte)(a + b); }
    @FnName("+") public static short add( byte a, short b ){ return (short)(a + b); }
    @FnName("+") public static Short add( byte a, Short b ){ return (Short)(short)(a + b); }
    @FnName("+") public static int add( byte a, int b ){ return (int)(a + b); }
    @FnName("+") public static Integer add( byte a, Integer b ){ return (Integer)(a + b); }
    @FnName("+") public static long add( byte a, long b ){ return (long)(a + b); }
    @FnName("+") public static Long add( byte a, Long b ){ return (Long)(a + b); }
    @FnName("+") public static float add( byte a, float b ){ return (float)(a + b); }
    @FnName("+") public static Float add( byte a, Float b ){ return (Float)(a + b); }
    @FnName("+") public static double add( byte a, double b ){ return (double)(a + b); }
    @FnName("+") public static Double add( byte a, Double b ){ return (Double)(a + b); }
    @FnName("+") public static Byte add( Byte a, byte b ){ return (Byte)(byte)(a + b); }
    @FnName("+") public static Byte add( Byte a, Byte b ){ return (Byte)(byte)(a + b); }
    @FnName("+") public static short add( Byte a, short b ){ return (short)(a + b); }
    @FnName("+") public static Short add( Byte a, Short b ){ return (Short)(short)(a + b); }
    @FnName("+") public static int add( Byte a, int b ){ return (int)(a + b); }
    @FnName("+") public static Integer add( Byte a, Integer b ){ return (Integer)(a + b); }
    @FnName("+") public static long add( Byte a, long b ){ return (long)(a + b); }
    @FnName("+") public static Long add( Byte a, Long b ){ return (Long)(a + b); }
    @FnName("+") public static float add( Byte a, float b ){ return (float)(a + b); }
    @FnName("+") public static Float add( Byte a, Float b ){ return (Float)(a + b); }
    @FnName("+") public static double add( Byte a, double b ){ return (double)(a + b); }
    @FnName("+") public static Double add( Byte a, Double b ){ return (Double)(a + b); }
    @FnName("-") public static byte sub( byte a, byte b ){ return (byte)(a - b); }
    @FnName("-") public static Byte sub( byte a, Byte b ){ return (Byte)(byte)(a - b); }
    @FnName("-") public static short sub( byte a, short b ){ return (short)(a - b); }
    @FnName("-") public static Short sub( byte a, Short b ){ return (Short)(short)(a - b); }
    @FnName("-") public static int sub( byte a, int b ){ return (int)(a - b); }
    @FnName("-") public static Integer sub( byte a, Integer b ){ return (Integer)(a - b); }
    @FnName("-") public static long sub( byte a, long b ){ return (long)(a - b); }
    @FnName("-") public static Long sub( byte a, Long b ){ return (Long)(a - b); }
    @FnName("-") public static float sub( byte a, float b ){ return (float)(a - b); }
    @FnName("-") public static Float sub( byte a, Float b ){ return (Float)(a - b); }
    @FnName("-") public static double sub( byte a, double b ){ return (double)(a - b); }
    @FnName("-") public static Double sub( byte a, Double b ){ return (Double)(a - b); }
    @FnName("-") public static Byte sub( Byte a, byte b ){ return (Byte)(byte)(a - b); }
    @FnName("-") public static Byte sub( Byte a, Byte b ){ return (Byte)(byte)(a - b); }
    @FnName("-") public static short sub( Byte a, short b ){ return (short)(a - b); }
    @FnName("-") public static Short sub( Byte a, Short b ){ return (Short)(short)(a - b); }
    @FnName("-") public static int sub( Byte a, int b ){ return (int)(a - b); }
    @FnName("-") public static Integer sub( Byte a, Integer b ){ return (Integer)(a - b); }
    @FnName("-") public static long sub( Byte a, long b ){ return (long)(a - b); }
    @FnName("-") public static Long sub( Byte a, Long b ){ return (Long)(a - b); }
    @FnName("-") public static float sub( Byte a, float b ){ return (float)(a - b); }
    @FnName("-") public static Float sub( Byte a, Float b ){ return (Float)(a - b); }
    @FnName("-") public static double sub( Byte a, double b ){ return (double)(a - b); }
    @FnName("-") public static Double sub( Byte a, Double b ){ return (Double)(a - b); }
    @FnName("*") public static byte mul( byte a, byte b ){ return (byte)(a * b); }
    @FnName("*") public static Byte mul( byte a, Byte b ){ return (Byte)(byte)(a * b); }
    @FnName("*") public static short mul( byte a, short b ){ return (short)(a * b); }
    @FnName("*") public static Short mul( byte a, Short b ){ return (Short)(short)(a * b); }
    @FnName("*") public static int mul( byte a, int b ){ return (int)(a * b); }
    @FnName("*") public static Integer mul( byte a, Integer b ){ return (Integer)(a * b); }
    @FnName("*") public static long mul( byte a, long b ){ return (long)(a * b); }
    @FnName("*") public static Long mul( byte a, Long b ){ return (Long)(a * b); }
    @FnName("*") public static float mul( byte a, float b ){ return (float)(a * b); }
    @FnName("*") public static Float mul( byte a, Float b ){ return (Float)(a * b); }
    @FnName("*") public static double mul( byte a, double b ){ return (double)(a * b); }
    @FnName("*") public static Double mul( byte a, Double b ){ return (Double)(a * b); }
    @FnName("*") public static Byte mul( Byte a, byte b ){ return (Byte)(byte)(a * b); }
    @FnName("*") public static Byte mul( Byte a, Byte b ){ return (Byte)(byte)(a * b); }
    @FnName("*") public static short mul( Byte a, short b ){ return (short)(a * b); }
    @FnName("*") public static Short mul( Byte a, Short b ){ return (Short)(short)(a * b); }
    @FnName("*") public static int mul( Byte a, int b ){ return (int)(a * b); }
    @FnName("*") public static Integer mul( Byte a, Integer b ){ return (Integer)(a * b); }
    @FnName("*") public static long mul( Byte a, long b ){ return (long)(a * b); }
    @FnName("*") public static Long mul( Byte a, Long b ){ return (Long)(a * b); }
    @FnName("*") public static float mul( Byte a, float b ){ return (float)(a * b); }
    @FnName("*") public static Float mul( Byte a, Float b ){ return (Float)(a * b); }
    @FnName("*") public static double mul( Byte a, double b ){ return (double)(a * b); }
    @FnName("*") public static Double mul( Byte a, Double b ){ return (Double)(a * b); }
    @FnName("/") public static byte div( byte a, byte b ){ return (byte)(a / b); }
    @FnName("/") public static Byte div( byte a, Byte b ){ return (Byte)(byte)(a / b); }
    @FnName("/") public static short div( byte a, short b ){ return (short)(a / b); }
    @FnName("/") public static Short div( byte a, Short b ){ return (Short)(short)(a / b); }
    @FnName("/") public static int div( byte a, int b ){ return (int)(a / b); }
    @FnName("/") public static Integer div( byte a, Integer b ){ return (Integer)(a / b); }
    @FnName("/") public static long div( byte a, long b ){ return (long)(a / b); }
    @FnName("/") public static Long div( byte a, Long b ){ return (Long)(a / b); }
    @FnName("/") public static float div( byte a, float b ){ return (float)(a / b); }
    @FnName("/") public static Float div( byte a, Float b ){ return (Float)(a / b); }
    @FnName("/") public static double div( byte a, double b ){ return (double)(a / b); }
    @FnName("/") public static Double div( byte a, Double b ){ return (Double)(a / b); }
    @FnName("/") public static Byte div( Byte a, byte b ){ return (Byte)(byte)(a / b); }
    @FnName("/") public static Byte div( Byte a, Byte b ){ return (Byte)(byte)(a / b); }
    @FnName("/") public static short div( Byte a, short b ){ return (short)(a / b); }
    @FnName("/") public static Short div( Byte a, Short b ){ return (Short)(short)(a / b); }
    @FnName("/") public static int div( Byte a, int b ){ return (int)(a / b); }
    @FnName("/") public static Integer div( Byte a, Integer b ){ return (Integer)(a / b); }
    @FnName("/") public static long div( Byte a, long b ){ return (long)(a / b); }
    @FnName("/") public static Long div( Byte a, Long b ){ return (Long)(a / b); }
    @FnName("/") public static float div( Byte a, float b ){ return (float)(a / b); }
    @FnName("/") public static Float div( Byte a, Float b ){ return (Float)(a / b); }
    @FnName("/") public static double div( Byte a, double b ){ return (double)(a / b); }
    @FnName("/") public static Double div( Byte a, Double b ){ return (Double)(a / b); }

//    @FnName("-") public static byte sub( byte a, byte b ){ return (byte)(a-b); }
//    @FnName("-") public static Byte sub( Byte a, Byte b ){ return (byte)(a-b); }
//    @FnName("-") public static Short sub( Byte a, Short b ){ return (short)(a-b); }
//    @FnName("-") public static Integer sub( Byte a, Integer b ){ return (int)(a-b); }
//    @FnName("-") public static Long sub( Byte a, Long b ){ return (long)(a-b); }
//    @FnName("-") public static Float sub( Byte a, Float b ){ return (float)(a-b); }
//    @FnName("-") public static Double sub( Byte a, Double b ){ return (double)(a-b); }
//
//    @FnName("*") public static byte mul( byte a, byte b ){ return (byte)(a*b); }
//    @FnName("*") public static Byte mul( Byte a, Byte b ){ return (byte)(a*b); }
//    @FnName("*") public static Short mul( Byte a, Short b ){ return (short)(a*b); }
//    @FnName("*") public static Integer mul( Byte a, Integer b ){ return (int)(a*b); }
//    @FnName("*") public static Long mul( Byte a, Long b ){ return (long)(a*b); }
//    @FnName("*") public static Float mul( Byte a, Float b ){ return (float)(a*b); }
//    @FnName("*") public static Double mul( Byte a, Double b ){ return (double)(a*b); }
//
//    @FnName("/") public static byte div( byte a, byte b ){ return (byte)(a/b); }
//    @FnName("/") public static Byte div( Byte a, Byte b ){ return (byte)(a/b); }
//    @FnName("/") public static Short div( Byte a, Short b ){ return (short)(a/b); }
//    @FnName("/") public static Integer div( Byte a, Integer b ){ return (int)(a/b); }
//    @FnName("/") public static Long div( Byte a, Long b ){ return (long)(a/b); }
//    @FnName("/") public static Float div( Byte a, Float b ){ return (float)(a/b); }
//    @FnName("/") public static Double div( Byte a, Double b ){ return (double)(a/b); }

    @FnName("<") public static Boolean less( Byte a, byte b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Byte b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, short b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Short b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, int b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Integer b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, long b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Long b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, float b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Float b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, double b ){ return a < b; }
    @FnName("<") public static Boolean less( Byte a, Double b ){ return a < b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, byte b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Byte b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, short b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Short b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, int b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Integer b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, long b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Long b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, float b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Float b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, double b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Byte a, Double b ){ return a <= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, byte b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Byte b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, short b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Short b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, int b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Integer b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, long b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Long b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, float b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Float b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, double b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Byte a, Double b ){ return a >= b; }
    @FnName(">") public static Boolean more( Byte a, byte b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Byte b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, short b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Short b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, int b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Integer b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, long b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Long b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, float b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Float b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, double b ){ return a > b; }
    @FnName(">") public static Boolean more( Byte a, Double b ){ return a > b; }
}
