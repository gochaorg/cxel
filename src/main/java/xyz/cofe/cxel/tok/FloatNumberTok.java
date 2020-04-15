package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Дробное число с дисятичной точкой
 */
public class FloatNumberTok extends NumberTok<FloatNumberTok> {
    /**
     * Конструктор
     * @param begin Начало токена
     * @param end Конец токена
     * @param intPart Целая часть или null
     * @param intRadix Системя счисления целой части
     * @param floatPart Дробная часть или null
     * @param floatRadix Системя счисления дробной части
     */
    public FloatNumberTok( CharPointer begin, CharPointer end, List<CToken> intPart, int intRadix, List<CToken> floatPart, int floatRadix ){
        super(begin, end);
        this.intPart = intPart!=null ? new DigitsTok(intPart) : null;
        this.intRadix = intRadix;

        this.floatPart = floatPart!=null ? new DigitsTok(floatPart) : null;
        this.floatRadix = floatRadix;

        if( intRadix<2 )throw new IllegalArgumentException( "intRadix<2" );
        if( intRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "intRadix>DigitsTok.maxRadix()" );

        if( floatRadix<2 )throw new IllegalArgumentException( "floatRadix<2" );
        if( floatRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "floatRadix>DigitsTok.maxRadix()" );
    }

    /**
     * Конструктор
     * @param begin Начало токена
     * @param end Конец токена
     * @param intPart Целая часть или null
     * @param intRadix Системя счисления целой части
     * @param floatPart Дробная часть или null
     * @param floatRadix Системя счисления дробной части
     */
    public FloatNumberTok( CharPointer begin, CharPointer end, DigitsTok intPart, int  intRadix, DigitsTok floatPart, int floatRadix ){
        super(begin, end);

        this.intPart = intPart;
        this.intRadix = intRadix;

        this.floatPart = floatPart;
        this.floatRadix = floatRadix;

        if( intRadix<2 )throw new IllegalArgumentException( "intRadix<2" );
        if( intRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "intRadix>DigitsTok.maxRadix()" );

        if( floatRadix<2 )throw new IllegalArgumentException( "floatRadix<2" );
        if( floatRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "floatRadix>DigitsTok.maxRadix()" );
    }

    /**
     * Конструктор
     * @param begin Начало токена
     * @param end Конец токена
     * @param intPart Целая часть или null
     * @param floatPart Дробная часть или null
     */
    public FloatNumberTok( CharPointer begin, CharPointer end, DigitsTok intPart, DigitsTok floatPart ){
        this(begin,end,intPart,10,floatPart,10);
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected FloatNumberTok( FloatNumberTok sample ){
        super(sample);
        this.floatRadix = sample.floatRadix;
        this.intRadix = sample.intRadix;
        this.intPart = sample.intPart;
        this.floatPart = sample.floatPart;
        this.precision = sample.precision;
    }

    /**
     * Клонироние
     * @return клон
     */
    @Override
    public FloatNumberTok clone(){
        return new FloatNumberTok(this);
    }

    private void dropValues(){
        doubleValue = null;
        bigdec = null;
    }

    //region intPart : DigitsTok - цифры целой части
    /**
     * цифры целой части
     */
    protected DigitsTok intPart;

    /**
     * Возвращает цифры целой части
     * @return цифры целой части
     */
    public DigitsTok intPart(){ return intPart; }

    /**
     * Клонирует и указывает цифры целой части
     * @param digits цифры
     * @return клон
     */
    public FloatNumberTok intPart( DigitsTok digits ){
        return cloneAndConf( c->{
            c.dropValues();
            c.intPart=digits;
        });
    }
    //endregion
    //region intRadix : int - Система счисления для целой части
    /** Система счисления для целой части */
    protected int intRadix;

    /**
     * Система счисления для целой части
     * @return Система счисления
     */
    public int intRadix(){ return intRadix; }

    /**
     * Указывает систему счисления для целой части
     * @param newRadix система счисления
     * @return клон
     */
    public FloatNumberTok intRadix( int newRadix ){
        if( newRadix<2 )throw new IllegalArgumentException( "newRadix<2" );
        if( newRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "newRadix>DigitsTok.maxRadix()" );
        return cloneAndConf(c->{
        c.dropValues();
        c.intRadix = newRadix;
    });}
    //endregion
    //region floatPart : DigitsTok - цифры дробной части
    /**
     * цифры дробной части
     */
    protected DigitsTok floatPart;

    /**
     * Возвращает цифры дробной части
     * @return цифры дробной части
     */
    public DigitsTok floatPart(){ return floatPart; }

    /**
     * Клонирует и указывает цифры дробной части
     * @param digits цифры
     * @return клон
     */
    public FloatNumberTok floatPart(DigitsTok digits){ return cloneAndConf(c->{
        c.dropValues();
        c.floatPart=digits;
    });}
    //endregion
    //region floatRadix : int - Система счисления для дробной части
    /** Система счисления для дробной части */
    protected int floatRadix;

    /**
     * Система счисления для дробной части
     * @return система счисления
     */
    public int floatRadix(){ return floatRadix; }

    /**
     * Указывает систему счисления для дробной части
     * @param newRadix система счисления
     * @return клон
     */
    public FloatNumberTok floatRadix( int newRadix ){
        if( newRadix<2 )throw new IllegalArgumentException( "newRadix<2" );
        if( newRadix>DigitsTok.maxRadix() )throw new IllegalArgumentException( "newRadix>DigitsTok.maxRadix()" );
        return cloneAndConf(c->{
        c.dropValues();
        c.floatRadix=newRadix;
    });}
    //endregion
    //region bigDecimalValue() : BigDecimal
    /** значение соответствующее лексеме */
    protected BigDecimal bigdec = null;

    /**
     * Вычисляет и возвращает BigDecimal значение
     * @return значение соответствующее лексеме
     */
    @SuppressWarnings("BigDecimalMethodWithoutRoundingCalled")
    public BigDecimal bigDecimalValue(){
        if( bigdec!=null )return bigdec;

        BigDecimal intv = BigDecimal.ZERO;
        if( intPart!=null ){
            BigDecimal k = BigDecimal.ONE;
            for( int di=intPart.length()-1; di>=0; di-- ){
                BigDecimal digit = BigDecimal.valueOf(intPart.digit(di));
                intv = intv.add( digit.multiply( k ) );
                k = k.multiply( BigDecimal.valueOf(intRadix) );
            }
        }

        BigDecimal floatv = BigDecimal.ZERO;
        if( floatPart!=null ){
            BigDecimal k = BigDecimal.ONE.divide( BigDecimal.valueOf(floatRadix) );
            for( int di=0; di<floatPart.length(); di++ ){
                BigDecimal digit = BigDecimal.valueOf(floatPart.digit(di));
                BigDecimal pv = k.multiply(digit);
                floatv = floatv.add(pv);
                k = k.divide(BigDecimal.valueOf(floatRadix));
            }
        }

        bigdec = intv.add(floatv);
        return bigdec;
    }
    //endregion
    //region doubleValue() : double
    /** значение соответствующее лексеме */
    protected Double doubleValue;

    /**
     * Вычисляет и возвращает double значение
     * @return значение соответствующее лексеме
     */
    public double doubleValue(){
        if( doubleValue!=null )return doubleValue;
        doubleValue = bigDecimalValue().doubleValue();
        return doubleValue;
    }
    //endregion
    //region precision - Указывает точность чисел
    /**
     * Указывает точность чисел
     */
    protected FloatPrecision precision = FloatPrecision.DOUBLE;

    /**
     * Возвращает точность чисел
     * @return точность чисел
     */
    public FloatPrecision precision(){ return precision; }

    /**
     * Указывает точность чисел
     * @param precision точность чисел
     * @return клон
     */
    public FloatNumberTok precision( FloatPrecision precision ){
        if( precision==null )throw new IllegalArgumentException("precision==null");
        return cloneAndConf( c->{
            c.precision = precision;
        });
    }
    //endregion
    @Override
    public Number value(){
        switch (precision){
            case FLOAT:
                return (float)doubleValue();
            case DOUBLE:
                return doubleValue();
            case BIGDECIMAL:
                return bigDecimalValue();
        }
        return doubleValue();
    }
}
