package xyz.cofe.cxel.eval;

import java.math.BigInteger;

/**
 * Преобразование Number object в Number primitive
 */
public class NumCast {
    /**
     * Проверка что указанный тип является number primitive
     * @param t тип
     * @return true - примитив (byte|short|int|long|float|double)
     */
    public static boolean isPrimitiveNumber( Class<?> t ){
        if( t==byte.class )return true;
        if( t==short.class )return true;
        if( t==int.class )return true;
        if( t==long.class )return true;
        if( t==float.class )return true;
        return t == double.class;
    }

    /**
     * Целевой тип данных (принимаемый)
     */
    public final Class<?> targetType;

    /**
     * Целевое значение (принимаемое)
     */
    public final Object targetValue;

    /**
     * Исходное значение
     */
    public final Number sourceNumber;

    /**
     * true - есть потеря данных; false - нет потери данных
     */
    public final boolean looseData;

    /**
     * Преобразован тот же самый тип (Unboxing)
     */
    public final boolean sameType;

    /**
     * Конструктор
     * @param targetType Целевой тип данных (принимаемый)
     * @param targetValue Целевое значение (принимаемое)
     * @param sourceNumber Исходное значение
     * @param looseData true - есть потеря данных; false - нет потери данных
     */
    public NumCast(Class<?> targetType, Object targetValue, Number sourceNumber, boolean looseData){
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.sourceNumber = sourceNumber;
        this.looseData = looseData;
        this.sameType = false;
    }

    /**
     * Конструктор
     * @param targetType Целевой тип данных (принимаемый)
     * @param targetValue Целевое значение (принимаемое)
     * @param sourceNumber Исходное значение
     * @param looseData true - есть потеря данных; false - нет потери данных
     */
    public NumCast(Class<?> targetType, Object targetValue, Number sourceNumber, boolean looseData, boolean sameType){
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.sourceNumber = sourceNumber;
        this.looseData = looseData;
        this.sameType = sameType;
    }

    /**
     * Преобразоваание (cast) числа к примитиву
     * @param primitiveNumber число примитив (тип)
     * @param someNum исходное число
     * @return Преобразование или null
     */
    public static NumCast tryCast( Class<?> primitiveNumber, Number someNum ){
        if( someNum==null )throw new IllegalArgumentException( "someNum==null" );
        if( primitiveNumber==null )throw new IllegalArgumentException( "primitiveNumber==null" );
        if( !primitiveNumber.isPrimitive() )throw new IllegalArgumentException( "!primitiveNumber.isPrimitive()" );
        if( !NumCast.isPrimitiveNumber(primitiveNumber) )throw new IllegalArgumentException( "!isPrimitiveNumber(primitiveNumber)" );

        if( primitiveNumber==byte.class ){
            if( someNum instanceof Byte ){
                NumCast ncast =new NumCast(primitiveNumber, (byte)(Byte)someNum, someNum, false, true );
                return ncast;
            }else if( someNum instanceof Integer ){
                int v = (int)(Integer)someNum;
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else if( someNum instanceof Short ){
                short v = (short)(Short)someNum;
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else if( someNum instanceof Long ){
                long v = someNum.longValue();
                if( v>255 || v<Byte.MIN_VALUE ){
                    return new NumCast(primitiveNumber, (byte)v, someNum, true);
                }else{
                    return new NumCast(primitiveNumber, (byte)v, someNum, false);
                }
            }else{
                return new NumCast(primitiveNumber, (byte)someNum.byteValue(), someNum, true);
            }
        }else if( primitiveNumber==int.class ){
            if( someNum instanceof Byte || someNum instanceof Short || someNum instanceof Integer ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.intValue(), someNum, false, someNum instanceof Integer);
                return ncast;
            }else{
                if( someNum instanceof Long || someNum instanceof BigInteger){
                    long v = someNum.longValue();
                    boolean loose = v > Integer.MAX_VALUE || v < Integer.MIN_VALUE;
                    return new NumCast(primitiveNumber, someNum.intValue(), someNum, loose);
                }else{
                    return new NumCast(primitiveNumber, someNum.intValue(), someNum, true);
                }
            }
        }else if( primitiveNumber==short.class ){
            if( someNum instanceof Byte || someNum instanceof Short ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.shortValue(), someNum, false, someNum instanceof Short);
                return ncast;
            }else{
                if( someNum instanceof Integer || someNum instanceof Long || someNum instanceof BigInteger ){
                    long v = someNum.longValue();
                    boolean loose = v > Short.MAX_VALUE || v < Short.MIN_VALUE;
                    return new NumCast(primitiveNumber, someNum.shortValue(), someNum, loose);
                }else{
                    return new NumCast(primitiveNumber, someNum.shortValue(), someNum, true);
                }
            }
        }else if( primitiveNumber==long.class ){
            if( someNum instanceof Long || someNum instanceof Integer || someNum instanceof Short || someNum instanceof Byte ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.longValue(), someNum, false, someNum instanceof Long);
                return ncast;
            }else if( someNum instanceof BigInteger ){
                BigInteger v = (BigInteger)someNum;
                boolean loose = v.compareTo(BigInteger.valueOf(Long.MAX_VALUE))>0 ||
                        v.compareTo(BigInteger.valueOf(Long.MIN_VALUE))<0;
                return new NumCast(primitiveNumber, someNum.longValue(), someNum, loose);
            }else{
                return new NumCast(primitiveNumber, someNum.longValue(), someNum, true);
            }
        }else if( primitiveNumber==float.class ){
            if( someNum instanceof Float || someNum instanceof Double ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.floatValue(), someNum, false, someNum instanceof Float);
                return ncast;
            }else{
                return new NumCast(primitiveNumber, someNum.floatValue(), someNum, true);
            }
        }else if( primitiveNumber==double.class ){
            if( someNum instanceof Double ){
                NumCast ncast = new NumCast(primitiveNumber, someNum.doubleValue(), someNum, false, true);
                return ncast;
            }else{
                return new NumCast(primitiveNumber, someNum.doubleValue(), someNum, true);
            }
        }

        return null;
    }
}
