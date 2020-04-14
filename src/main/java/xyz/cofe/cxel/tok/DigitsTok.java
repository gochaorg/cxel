package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

import java.util.List;

/**
 * Лексема - Последовательность цифр. <br>
 *
 * Цифры могут быть представлены знаками:
 * <code>0123456789abcdefghijklmnopqrstuvwxyz</code>
 * Регистр символов не учитывается.
 */
public class DigitsTok extends CToken {
    public DigitsTok( CharPointer begin, CharPointer end ){ super(begin, end); }
    public DigitsTok( CToken begin, CToken end ){ super(begin, end); }
    public DigitsTok( List<CToken> tokens ){ super(tokens); }
    public DigitsTok( CToken sample ){
        super(sample);
    }

    @Override
    public DigitsTok clone() {
        return new DigitsTok(this);
    }

    private static final String digits = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final char[] charsOfDigits = digits.toCharArray();

    /**
     * Вохвращает максимаьлноу систему счисления
     * @return макс. система счисления
     */
    public static int maxRadix(){ return charsOfDigits.length; }

    /**
     * Возвращает кол-во цифр - совпадает с текстом
     * @return кол-во цифр
     */
    public int length(){ return text().length(); }

    /**
     * Возвращает цифру для указанного символа.
     * значение цифры определеляется позицией (от 0) символа (без учета регистра)
     * в наборе возможных цифр: <code>0123456789abcdefghijklmnopqrstuvwxyz</code>
     * @param di индекс символа
     * @return цифра
     * @throws IllegalArgumentException если:
     * di < 0 или символ на который ссылается di (text().charAt(di)) не входит в набор допустимых символов
     */
    public int digit( int di ){
        if( di<0 )throw new IllegalArgumentException("di<0");
        if( di>=length() )throw new IllegalArgumentException("di>=length()");
        char c = Character.toLowerCase(text().charAt(di));
        for( int i=0; i<charsOfDigits.length; i++ ){
            if( charsOfDigits[i]==c ){
                return i;
            }
        }
        throw new IllegalArgumentException("text().charAt(di="+di+")="+c+" not in valid digit");
    }
}
