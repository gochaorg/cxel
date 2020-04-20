package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CharPointer;

import java.math.BigInteger;

/**
 * Целое число
 */
public class IntegerNumberTok extends NumberTok<IntegerNumberTok> {
    /**
     * Конструктор
     * @param tok последовательность цифр
     */
    public IntegerNumberTok( DigitsTok tok ){
        super(tok.begin(), tok.end());
        this.digits = tok;
    }

    /**
     * Конструктор
     * @param begin начало
     * @param end конец
     * @param tok последовательность цифр
     */
    public IntegerNumberTok(CharPointer begin, CharPointer end,DigitsTok tok ){
        super(begin, end);
        this.digits = tok;
    }

    //region clone()
    /**
     * Конструктор коптрования
     * @param sample образец для копирования
     */
    protected IntegerNumberTok( IntegerNumberTok sample ){
        super(sample.begin(), sample.end());
        this.radix = sample.radix;
        this.digits = sample.digits;
        this.longValue = sample.longValue;
        this.bigInt = sample.bigInt;
        this.precision = sample.precision;
    }

    @Override
    public IntegerNumberTok clone(){
        return new IntegerNumberTok(this);
    }
    //endregion
    //region digits - последовательность цифр
    protected DigitsTok digits;

    /**
     * Указывает на лексему последовательности цифр
     * @return последовательность цифр
     */
    public DigitsTok digits(){
        return digits;
    }

    /**
     * Указывает на лексему последовательности цифр
     * @param dgts последовательность цифр
     * @return Клон с новой последовательностью цифр
     */
    public IntegerNumberTok digits( DigitsTok dgts ){
        if( dgts==null )throw new IllegalArgumentException("dgts==null");
        return cloneAndConf( c->{
            c.digits = dgts;
            c.longValue = null;
            c.bigInt = null;
        });
    }
    //endregion
    //region radix - система счиссления
    /**
     * Возвращает систему счисления
     */
    protected int radix = 10;

    /**
     * Указывает систему счисления
     * @return система счисления
     */
    public int radix(){ return radix; }

    /**
     * Указывает новую систему счисления
     * @param newRadix система счисления (основание)
     * @return новая лексема с указанной системой счисления
     */
    public IntegerNumberTok radix( int newRadix ){
        if( newRadix<2 )throw new IllegalArgumentException( "newRadix<2" );
        if( newRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "newRadix>DigitsTok.maxRadix()" );
        return cloneAndConf( c -> c.radix = newRadix );
    }
    //endregion
    //region long значение
    /**
     * long значение
     */
    protected Long longValue;

    /**
     * Возвращает long значение соответствующее лексеме
     * @return long значение
     */
    public long longValue(){
        if( longValue!=null )return longValue;
        long r = 0;
        if( digits!=null ){
            if( digits.length()>0 ){
                long k = 1;
                for( int di=digits.length()-1; di>=0; di-- ){
                    int d = digits.digit(di);
                    r = r + d*k;
                    k = k * radix;
                }
            }
        }
        longValue = r;
        return longValue;
    }
    //endregion
    //region bigIntegerValue значение
    /**
     * Big Integer значение
     */
    protected BigInteger bigInt;

    /**
     * Возвращает Big Integer значение
     * @return значение соовет токену
     */
    public BigInteger bigIntegerValue(){
        if( bigInt!=null )return bigInt;
        BigInteger r = BigInteger.ZERO;
        if( digits!=null ){
            if( digits.length()>0 ){
                BigInteger k = BigInteger.ONE;
                for( int di=digits.length()-1; di>=0; di-- ){
                    int d = digits.digit(di);
                    r = r.add( BigInteger.valueOf(d).multiply( k ) );
                    k = k.multiply( BigInteger.valueOf(radix) );
                }
            }
        }
        bigInt = r;
        return bigInt;
    }
    //endregion
    //region precision - точность чисел (разраядность)
    /**
     * Указывает точность чисел
     */
    protected IntegerPrecision precision = IntegerPrecision.INTEGER;

    /**
     * Возвращает точность чисел
     * @return точность чисел
     */
    public IntegerPrecision precision(){ return precision; }

    /**
     * Указывает точность чисел
     * @param precision точность чисел
     * @return клон
     */
    public IntegerNumberTok precision( IntegerPrecision precision ){
        if( precision==null )throw new IllegalArgumentException("precision==null");
        return cloneAndConf( c->{
            c.precision = precision;
        });
    }

    /**
     * Клонирует и явно (precisionPredefined = false) указывает точность числа
     * @param newPrecision точность числа
     * @return клон
     */
    public IntegerNumberTok defprecision( IntegerPrecision newPrecision ){
        if( precision!=null )throw new IllegalArgumentException("precision!=null");
        return cloneAndConf( c->{c.precisionPredefined = false; c.precision=newPrecision;} );
    }
    //endregion
    /**
     * Точность (разрядность) чисел предопределена
     */
    protected boolean precisionPredefined = true;

    /**
     * Возвращает точность (разрядность) чисел предопределена
     * @return true - предопределена, false - задана явно
     */
    public boolean precisionPredefined(){ return precisionPredefined; }

    /**
     * Клонирует и указывает точность (разрядность) чисел предопределена
     * @param predef true - точность чисел предопределена, false - задана явно
     * @return клон
     */
    public IntegerNumberTok precisionPredefined( boolean predef ){
        return cloneAndConf( c->c.precisionPredefined = predef );
    }
    @SuppressWarnings("DuplicateBranchesInSwitch")
    @Override
    public Number value(){
        switch (precision) {
            case BYTE: return ((Long)longValue()).byteValue();
            case SHORT: return ((Long)longValue()).shortValue();
            case INTEGER: return ((Long)longValue()).intValue();
            case LONG: return longValue();
            case BIGINT: return bigIntegerValue();
            default:
                return longValue();
        }
    }
}
