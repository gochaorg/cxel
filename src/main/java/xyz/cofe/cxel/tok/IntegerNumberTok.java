package xyz.cofe.cxel.tok;

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
     * Конструктор коптрования
     * @param sample образец для копирования
     */
    protected IntegerNumberTok( IntegerNumberTok sample ){
        super(sample.begin(), sample.end());
        this.radix = sample.radix;
        this.digits = sample.digits;
    }

    @Override
    public IntegerNumberTok clone(){
        return new IntegerNumberTok(this);
    }

    protected DigitsTok digits;

    /**
     * Указывает на лексему последовательности цифр
     * @return последовательность цифр
     */
    public DigitsTok digits(){
        return digits;
    }

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

    private Long longValue;
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

    protected BigInteger bigInt;
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

    @Override
    public Number value(){
        return longValue();
    }
}
