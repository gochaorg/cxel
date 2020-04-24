package xyz.cofe.cxel.js;

import xyz.cofe.cxel.tok.DigitsTok;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.cxel.tok.IntegerPrecision;
import xyz.cofe.text.tparse.CharPointer;

/**
 * Формированное целое число как double, по скольку js не поддерживает целых чисел из коробки
 */
public class ForcedFloatNumberTok extends IntegerNumberTok {
    public ForcedFloatNumberTok( DigitsTok tok ){
        super(tok);
    }

    public ForcedFloatNumberTok( CharPointer begin, CharPointer end, DigitsTok tok ){
        super(begin, end, tok);
    }

    public ForcedFloatNumberTok( IntegerNumberTok sample ){
        super(sample);
    }

    @Override
    public ForcedFloatNumberTok clone(){
        return new ForcedFloatNumberTok(this);
    }

    @Override
    public IntegerNumberTok precision( IntegerPrecision precision ){
        System.err.println("ignore set precision to precision");
        return super.precision(precision);
    }

    @Override
    public Number value(){
        Long val = (Long)longValue();
        return val.doubleValue();
    }
}