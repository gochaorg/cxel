package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.Objects;

/**
 * Операции над целыми числами
 */
public class LongOperators {
    @FnName("+") public static long add( long a, byte b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, Byte b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, short b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, Short b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, int b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, Integer b ){ return (long)(a + b); }
    @FnName("+") public static long add( long a, long b ){ return (long)(a + b); }
    @FnName("+") public static Long add( long a, Long b ){ return (Long)(a + b); }
    @FnName("+") public static float add( long a, float b ){ return (float)(a + b); }
    @FnName("+") public static Float add( long a, Float b ){ return (Float)(a + b); }
    @FnName("+") public static double add( long a, double b ){ return (double)(a + b); }
    @FnName("+") public static Double add( long a, Double b ){ return (Double)(a + b); }
    @FnName("+") public static Long add( Long a, byte b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, Byte b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, short b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, Short b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, int b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, Integer b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, long b ){ return (Long)(a + b); }
    @FnName("+") public static Long add( Long a, Long b ){ return (Long)(a + b); }
    @FnName("+") public static float add( Long a, float b ){ return (float)(a + b); }
    @FnName("+") public static Float add( Long a, Float b ){ return (Float)(a + b); }
    @FnName("+") public static double add( Long a, double b ){ return (double)(a + b); }
    @FnName("+") public static Double add( Long a, Double b ){ return (Double)(a + b); }
    @FnName("-") public static long sub( long a, byte b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, Byte b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, short b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, Short b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, int b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, Integer b ){ return (long)(a - b); }
    @FnName("-") public static long sub( long a, long b ){ return (long)(a - b); }
    @FnName("-") public static Long sub( long a, Long b ){ return (Long)(a - b); }
    @FnName("-") public static float sub( long a, float b ){ return (float)(a - b); }
    @FnName("-") public static Float sub( long a, Float b ){ return (Float)(a - b); }
    @FnName("-") public static double sub( long a, double b ){ return (double)(a - b); }
    @FnName("-") public static Double sub( long a, Double b ){ return (Double)(a - b); }
    @FnName("-") public static Long sub( Long a, byte b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, Byte b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, short b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, Short b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, int b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, Integer b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, long b ){ return (Long)(a - b); }
    @FnName("-") public static Long sub( Long a, Long b ){ return (Long)(a - b); }
    @FnName("-") public static float sub( Long a, float b ){ return (float)(a - b); }
    @FnName("-") public static Float sub( Long a, Float b ){ return (Float)(a - b); }
    @FnName("-") public static double sub( Long a, double b ){ return (double)(a - b); }
    @FnName("-") public static Double sub( Long a, Double b ){ return (Double)(a - b); }
    @FnName("*") public static long mul( long a, byte b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, Byte b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, short b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, Short b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, int b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, Integer b ){ return (long)(a * b); }
    @FnName("*") public static long mul( long a, long b ){ return (long)(a * b); }
    @FnName("*") public static Long mul( long a, Long b ){ return (Long)(a * b); }
    @FnName("*") public static float mul( long a, float b ){ return (float)(a * b); }
    @FnName("*") public static Float mul( long a, Float b ){ return (Float)(a * b); }
    @FnName("*") public static double mul( long a, double b ){ return (double)(a * b); }
    @FnName("*") public static Double mul( long a, Double b ){ return (Double)(a * b); }
    @FnName("*") public static Long mul( Long a, byte b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, Byte b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, short b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, Short b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, int b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, Integer b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, long b ){ return (Long)(a * b); }
    @FnName("*") public static Long mul( Long a, Long b ){ return (Long)(a * b); }
    @FnName("*") public static float mul( Long a, float b ){ return (float)(a * b); }
    @FnName("*") public static Float mul( Long a, Float b ){ return (Float)(a * b); }
    @FnName("*") public static double mul( Long a, double b ){ return (double)(a * b); }
    @FnName("*") public static Double mul( Long a, Double b ){ return (Double)(a * b); }
    @FnName("/") public static long div( long a, byte b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, Byte b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, short b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, Short b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, int b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, Integer b ){ return (long)(a / b); }
    @FnName("/") public static long div( long a, long b ){ return (long)(a / b); }
    @FnName("/") public static Long div( long a, Long b ){ return (Long)(a / b); }
    @FnName("/") public static float div( long a, float b ){ return (float)(a / b); }
    @FnName("/") public static Float div( long a, Float b ){ return (Float)(a / b); }
    @FnName("/") public static double div( long a, double b ){ return (double)(a / b); }
    @FnName("/") public static Double div( long a, Double b ){ return (Double)(a / b); }
    @FnName("/") public static Long div( Long a, byte b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, Byte b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, short b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, Short b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, int b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, Integer b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, long b ){ return (Long)(a / b); }
    @FnName("/") public static Long div( Long a, Long b ){ return (Long)(a / b); }
    @FnName("/") public static float div( Long a, float b ){ return (float)(a / b); }
    @FnName("/") public static Float div( Long a, Float b ){ return (Float)(a / b); }
    @FnName("/") public static double div( Long a, double b ){ return (double)(a / b); }
    @FnName("/") public static Double div( Long a, Double b ){ return (Double)(a / b); }

    @FnName("<") public static Boolean less( Long a, byte b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Byte b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, short b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Short b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, int b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Integer b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, long b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Long b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, float b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Float b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, double b ){ return a < b; }
    @FnName("<") public static Boolean less( Long a, Double b ){ return a < b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, byte b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Byte b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, short b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Short b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, int b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Integer b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, long b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Long b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, float b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Float b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, double b ){ return a <= b; }
    @FnName("<=") public static Boolean eqOrLess( Long a, Double b ){ return a <= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, byte b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Byte b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, short b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Short b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, int b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Integer b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, long b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Long b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, float b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Float b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, double b ){ return a >= b; }
    @FnName(">=") public static Boolean eqOrMore( Long a, Double b ){ return a >= b; }
    @FnName(">") public static Boolean more( Long a, byte b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Byte b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, short b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Short b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, int b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Integer b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, long b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Long b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, float b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Float b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, double b ){ return a > b; }
    @FnName(">") public static Boolean more( Long a, Double b ){ return a > b; }
}
