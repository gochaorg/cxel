package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

import java.math.BigDecimal;
import java.util.List;

public class FloatNumberTok extends NumberTok<FloatNumberTok> {
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

    public FloatNumberTok( CharPointer begin, CharPointer end, DigitsTok intPart, DigitsTok floatPart ){
        this(begin,end,intPart,10,floatPart,10);
    }

    protected FloatNumberTok( FloatNumberTok sample ){
        super(sample);
        this.floatRadix = sample.floatRadix;
        this.intRadix = sample.intRadix;
        this.intPart = sample.intPart;
        this.floatPart = sample.floatPart;
    }

    @Override
    public FloatNumberTok clone(){
        return new FloatNumberTok(this);
    }

    private DigitsTok intPart;
    public DigitsTok intPart(){ return intPart; }

    private int intRadix;
    public int intRadix(){ return intRadix; }

    private DigitsTok floatPart;
    public DigitsTok floatPart(){ return floatPart; }

    private int floatRadix;
    public int floatRadix(){ return floatRadix; }

    private BigDecimal bigdec = null;
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

    private Double doubleValue;
    public double doubleValue(){
        if( doubleValue!=null )return doubleValue;
        doubleValue = bigDecimalValue().doubleValue();
        return doubleValue;
    }

    @Override
    public Number value(){
        return doubleValue();
    }
}
