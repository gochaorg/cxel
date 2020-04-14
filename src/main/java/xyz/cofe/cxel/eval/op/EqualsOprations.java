package xyz.cofe.cxel.eval.op;

import xyz.cofe.cxel.eval.FnName;

import java.util.Objects;

public class EqualsOprations {
    @FnName("==") public static Boolean eq( Object a, Object b ){ return Objects.equals(a,b); }
    @FnName("!=") public static Boolean notEq( Object a, Object b ){ return !Objects.equals(a,b); }

    @FnName("==") public static Boolean eq( byte a, byte b ){ return (byte)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( byte a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Byte b ){ return (byte)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( byte a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, short b ){ return (byte)a == (short)b; }
    @FnName("!=") public static Boolean notEq( byte a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Short b ){ return (byte)a == (short)b; }
    @FnName("!=") public static Boolean notEq( byte a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, int b ){ return (byte)a == (int)b; }
    @FnName("!=") public static Boolean notEq( byte a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Integer b ){ return (byte)a == (int)b; }
    @FnName("!=") public static Boolean notEq( byte a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, long b ){ return (byte)a == (long)b; }
    @FnName("!=") public static Boolean notEq( byte a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Long b ){ return (byte)a == (long)b; }
    @FnName("!=") public static Boolean notEq( byte a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, float b ){ return (byte)a == (float)b; }
    @FnName("!=") public static Boolean notEq( byte a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Float b ){ return (byte)a == (float)b; }
    @FnName("!=") public static Boolean notEq( byte a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, double b ){ return (byte)a == (double)b; }
    @FnName("!=") public static Boolean notEq( byte a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( byte a, Double b ){ return (byte)a == (double)b; }
    @FnName("!=") public static Boolean notEq( byte a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, byte b ){ return (byte)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Byte a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Byte b ){ return (byte)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, short b ){ return (byte)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Byte a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Short b ){ return (byte)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, int b ){ return (byte)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Byte a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Integer b ){ return (byte)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, long b ){ return (byte)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Byte a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Long b ){ return (byte)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, float b ){ return (byte)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Byte a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Float b ){ return (byte)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, double b ){ return (byte)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Byte a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Byte a, Double b ){ return (byte)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Byte a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, byte b ){ return (short)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( short a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Byte b ){ return (short)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( short a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, short b ){ return (short)a == (short)b; }
    @FnName("!=") public static Boolean notEq( short a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Short b ){ return (short)a == (short)b; }
    @FnName("!=") public static Boolean notEq( short a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, int b ){ return (short)a == (int)b; }
    @FnName("!=") public static Boolean notEq( short a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Integer b ){ return (short)a == (int)b; }
    @FnName("!=") public static Boolean notEq( short a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, long b ){ return (short)a == (long)b; }
    @FnName("!=") public static Boolean notEq( short a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Long b ){ return (short)a == (long)b; }
    @FnName("!=") public static Boolean notEq( short a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, float b ){ return (short)a == (float)b; }
    @FnName("!=") public static Boolean notEq( short a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Float b ){ return (short)a == (float)b; }
    @FnName("!=") public static Boolean notEq( short a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, double b ){ return (short)a == (double)b; }
    @FnName("!=") public static Boolean notEq( short a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( short a, Double b ){ return (short)a == (double)b; }
    @FnName("!=") public static Boolean notEq( short a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, byte b ){ return (short)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Short a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Byte b ){ return (short)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Short a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, short b ){ return (short)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Short a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Short b ){ return (short)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Short a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, int b ){ return (short)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Short a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Integer b ){ return (short)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Short a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, long b ){ return (short)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Short a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Long b ){ return (short)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Short a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, float b ){ return (short)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Short a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Float b ){ return (short)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Short a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, double b ){ return (short)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Short a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Short a, Double b ){ return (short)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Short a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, byte b ){ return (int)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( int a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Byte b ){ return (int)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( int a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, short b ){ return (int)a == (short)b; }
    @FnName("!=") public static Boolean notEq( int a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Short b ){ return (int)a == (short)b; }
    @FnName("!=") public static Boolean notEq( int a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, int b ){ return (int)a == (int)b; }
    @FnName("!=") public static Boolean notEq( int a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Integer b ){ return (int)a == (int)b; }
    @FnName("!=") public static Boolean notEq( int a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, long b ){ return (int)a == (long)b; }
    @FnName("!=") public static Boolean notEq( int a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Long b ){ return (int)a == (long)b; }
    @FnName("!=") public static Boolean notEq( int a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, float b ){ return (int)a == (float)b; }
    @FnName("!=") public static Boolean notEq( int a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Float b ){ return (int)a == (float)b; }
    @FnName("!=") public static Boolean notEq( int a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, double b ){ return (int)a == (double)b; }
    @FnName("!=") public static Boolean notEq( int a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( int a, Double b ){ return (int)a == (double)b; }
    @FnName("!=") public static Boolean notEq( int a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, byte b ){ return (int)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Integer a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Byte b ){ return (int)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, short b ){ return (int)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Integer a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Short b ){ return (int)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, int b ){ return (int)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Integer a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Integer b ){ return (int)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, long b ){ return (int)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Integer a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Long b ){ return (int)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, float b ){ return (int)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Integer a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Float b ){ return (int)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, double b ){ return (int)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Integer a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Integer a, Double b ){ return (int)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Integer a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, byte b ){ return (long)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( long a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Byte b ){ return (long)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( long a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, short b ){ return (long)a == (short)b; }
    @FnName("!=") public static Boolean notEq( long a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Short b ){ return (long)a == (short)b; }
    @FnName("!=") public static Boolean notEq( long a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, int b ){ return (long)a == (int)b; }
    @FnName("!=") public static Boolean notEq( long a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Integer b ){ return (long)a == (int)b; }
    @FnName("!=") public static Boolean notEq( long a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, long b ){ return (long)a == (long)b; }
    @FnName("!=") public static Boolean notEq( long a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Long b ){ return (long)a == (long)b; }
    @FnName("!=") public static Boolean notEq( long a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, float b ){ return (long)a == (float)b; }
    @FnName("!=") public static Boolean notEq( long a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Float b ){ return (long)a == (float)b; }
    @FnName("!=") public static Boolean notEq( long a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, double b ){ return (long)a == (double)b; }
    @FnName("!=") public static Boolean notEq( long a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( long a, Double b ){ return (long)a == (double)b; }
    @FnName("!=") public static Boolean notEq( long a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, byte b ){ return (long)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Long a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Byte b ){ return (long)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Long a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, short b ){ return (long)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Long a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Short b ){ return (long)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Long a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, int b ){ return (long)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Long a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Integer b ){ return (long)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Long a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, long b ){ return (long)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Long a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Long b ){ return (long)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Long a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, float b ){ return (long)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Long a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Float b ){ return (long)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Long a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, double b ){ return (long)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Long a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Long a, Double b ){ return (long)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Long a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, byte b ){ return (float)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( float a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Byte b ){ return (float)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( float a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, short b ){ return (float)a == (short)b; }
    @FnName("!=") public static Boolean notEq( float a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Short b ){ return (float)a == (short)b; }
    @FnName("!=") public static Boolean notEq( float a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, int b ){ return (float)a == (int)b; }
    @FnName("!=") public static Boolean notEq( float a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Integer b ){ return (float)a == (int)b; }
    @FnName("!=") public static Boolean notEq( float a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, long b ){ return (float)a == (long)b; }
    @FnName("!=") public static Boolean notEq( float a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Long b ){ return (float)a == (long)b; }
    @FnName("!=") public static Boolean notEq( float a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, float b ){ return (float)a == (float)b; }
    @FnName("!=") public static Boolean notEq( float a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Float b ){ return (float)a == (float)b; }
    @FnName("!=") public static Boolean notEq( float a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, double b ){ return (float)a == (double)b; }
    @FnName("!=") public static Boolean notEq( float a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( float a, Double b ){ return (float)a == (double)b; }
    @FnName("!=") public static Boolean notEq( float a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, byte b ){ return (float)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Float a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Byte b ){ return (float)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Float a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, short b ){ return (float)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Float a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Short b ){ return (float)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Float a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, int b ){ return (float)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Float a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Integer b ){ return (float)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Float a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, long b ){ return (float)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Float a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Long b ){ return (float)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Float a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, float b ){ return (float)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Float a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Float b ){ return (float)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Float a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, double b ){ return (float)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Float a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Float a, Double b ){ return (float)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Float a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, byte b ){ return (double)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( double a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Byte b ){ return (double)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( double a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, short b ){ return (double)a == (short)b; }
    @FnName("!=") public static Boolean notEq( double a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Short b ){ return (double)a == (short)b; }
    @FnName("!=") public static Boolean notEq( double a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, int b ){ return (double)a == (int)b; }
    @FnName("!=") public static Boolean notEq( double a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Integer b ){ return (double)a == (int)b; }
    @FnName("!=") public static Boolean notEq( double a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, long b ){ return (double)a == (long)b; }
    @FnName("!=") public static Boolean notEq( double a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Long b ){ return (double)a == (long)b; }
    @FnName("!=") public static Boolean notEq( double a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, float b ){ return (double)a == (float)b; }
    @FnName("!=") public static Boolean notEq( double a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Float b ){ return (double)a == (float)b; }
    @FnName("!=") public static Boolean notEq( double a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, double b ){ return (double)a == (double)b; }
    @FnName("!=") public static Boolean notEq( double a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( double a, Double b ){ return (double)a == (double)b; }
    @FnName("!=") public static Boolean notEq( double a, Double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, byte b ){ return (double)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Double a, byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Byte b ){ return (double)a == (byte)b; }
    @FnName("!=") public static Boolean notEq( Double a, Byte b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, short b ){ return (double)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Double a, short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Short b ){ return (double)a == (short)b; }
    @FnName("!=") public static Boolean notEq( Double a, Short b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, int b ){ return (double)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Double a, int b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Integer b ){ return (double)a == (int)b; }
    @FnName("!=") public static Boolean notEq( Double a, Integer b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, long b ){ return (double)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Double a, long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Long b ){ return (double)a == (long)b; }
    @FnName("!=") public static Boolean notEq( Double a, Long b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, float b ){ return (double)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Double a, float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Float b ){ return (double)a == (float)b; }
    @FnName("!=") public static Boolean notEq( Double a, Float b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, double b ){ return (double)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Double a, double b ){ return !eq( a, b ); }
    @FnName("==") public static Boolean eq( Double a, Double b ){ return (double)a == (double)b; }
    @FnName("!=") public static Boolean notEq( Double a, Double b ){ return !eq( a, b ); }
}
