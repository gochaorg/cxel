package xyz.cofe.cxel;

import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static xyz.cofe.text.tparse.Chars.*;

/**
 * Лексемы
 */
public class Lexer {
    /**
     * 16тиричные цифры
     */
    public static final GR<CharPointer,DigitsTok> hexDigits
        = test( c->"0123456789abcdefABCDEF".contains(""+c)).repeat().map(DigitsTok::new);

    /**
     * Целое 16тиричное число
     */
    public static final GR<CharPointer, IntegerNumberTok> hexIntegerNumber
        = test(c -> c == '0').next(test(c -> c == 'x')).next(
        hexDigits
    ).map((c0, c1, ntok) -> new IntegerNumberTok(c0.begin(), ntok.end(), ntok).radix(16));

    /**
     * 2чные цифры
     */
    public static final GR<CharPointer,DigitsTok> binDigits
        = test( c->"01".contains(""+c)).repeat().map(DigitsTok::new);

    /**
     * Целое 16тиричное число
     */
    public static final GR<CharPointer, IntegerNumberTok> binIntegerNumber
        = test(c -> c == '0').next(test(c -> c == 'b')).next(
        binDigits
    ).map((c0, c1, ntok) -> new IntegerNumberTok(c0.begin(), ntok.end(), ntok).radix(2));

    /**
     * 8чные цифры
     */
    public static final GR<CharPointer,DigitsTok> octDigits
        = test( c->"01234567".contains(""+c)).repeat().map(DigitsTok::new);

    /**
     * Целое 16тиричное число
     */
    public static final GR<CharPointer, IntegerNumberTok> octIntegerNumber
        = test(c -> c == '0').next(
        octDigits
    ).map((c0, dits) -> new IntegerNumberTok(c0.begin(), dits.end(), dits).radix(8));

    /**
     * Целое число
     */
    public static final GR<CharPointer, IntegerNumberTok> integerNumber
        = digit.repeat().map( digits -> new IntegerNumberTok( new DigitsTok(digits) ) );

    public static GR<CharPointer, IntegerNumberTok> integerPrecisionSuffix(
        GR<CharPointer, IntegerNumberTok> intParseRule
    ){
        if( intParseRule==null )throw new IllegalArgumentException("intParseRule==null");
        return ptr -> {
            Optional<IntegerNumberTok> tNum = intParseRule.apply(ptr);
            if( tNum==null || !tNum.isPresent() )return Optional.empty();

            Optional<Character> suff = tNum.get().end().lookup(0);
            if( suff!=null && suff.isPresent() ){
                switch (suff.get()){
                    case 'l': case 'L': {
                        IntegerNumberTok ntok = tNum.get();
                        ntok = ntok.precision(IntegerPrecision.LONG).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 'i': case 'I': {
                        IntegerNumberTok ntok = tNum.get();
                        ntok = ntok.precision(IntegerPrecision.INTEGER).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 's': case 'S': {
                        IntegerNumberTok ntok = tNum.get();
                        ntok = ntok.precision(IntegerPrecision.SHORT).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 'b': case 'B': {
                        IntegerNumberTok ntok = tNum.get();
                        ntok = ntok.precision(IntegerPrecision.BYTE).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 'n': case 'N': case 'w': case 'W': {
                        IntegerNumberTok ntok = tNum.get();
                        ntok = ntok.precision(IntegerPrecision.BIGINT).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                }
            }

            return tNum;
        };
    }

    /**
     * Последовательность цифр
     */
    public static final GR<CharPointer, DigitsTok> digits
        = digit.repeat().map(DigitsTok::new);

    /**
     * Дробное число
     */
    public static final GR<CharPointer, FloatNumberTok> floatNumber
        = digits.next(Keyword.Dot.lex()).next(digits)
              .map( (intPart,dot,floatPart)->new FloatNumberTok(intPart.begin(), floatPart.end(), intPart, floatPart) );

    /**
     * Дробное число предворенное точкой
     */
    public static final GR<CharPointer, FloatNumberTok> dotPrefFloatNumber
        = Keyword.Dot.lex().next(digits).map( (dot,dgts)->{ return new FloatNumberTok(dot.begin(), dgts.end(), null, dgts);} );

    /**
     * Дробное число с точкой в конце
     */
    public static final GR<CharPointer, FloatNumberTok> dotSuffFloatNumber
        = digits.next(Keyword.Dot.lex()).map( (dgts,dot)->{ return new FloatNumberTok(dgts.begin(), dot.end(), dgts, null);} );

    public static GR<CharPointer, FloatNumberTok> floatPrecisionSuffix(
        GR<CharPointer, FloatNumberTok> floatParseRule
    ){
        if( floatParseRule==null )throw new IllegalArgumentException("floatParseRule==null");
        return ptr -> {
            Optional<FloatNumberTok> tNum = floatParseRule.apply(ptr);
            if( tNum==null || !tNum.isPresent() )return Optional.empty();

            Optional<Character> suff = tNum.get().end().lookup(0);
            if( suff!=null && suff.isPresent() ){
                switch (suff.get()){
                    case 'f': case 'F': {
                        FloatNumberTok ntok = tNum.get();
                        ntok = ntok.precision(FloatPrecision.FLOAT).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 'd': case 'D': {
                        FloatNumberTok ntok = tNum.get();
                        ntok = ntok.precision(FloatPrecision.DOUBLE).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                    case 'n': case 'N': case 'w': case 'W': {
                        FloatNumberTok ntok = tNum.get();
                        ntok = ntok.precision(FloatPrecision.BIGDECIMAL).location(ntok.begin(),ntok.end().move(1));
                        tNum = Optional.of(ntok);
                    }
                    break;
                }
            }

            return tNum;
        };
    }

    public static final GR<CharPointer, FloatNumberTok> intAsDouble
        = digits.next(test(c->c=='d' || c=='D' ))
        .map( (dgts,sf)->new FloatNumberTok(dgts.begin(), sf.end(), dgts, null).precision(FloatPrecision.DOUBLE) );

    public static final GR<CharPointer, FloatNumberTok> intAsFloat
        = digits.next(test(c->c=='f' || c=='F' ))
        .map( (dgts,sf)->new FloatNumberTok(dgts.begin(), sf.end(), dgts, null).precision(FloatPrecision.FLOAT) );

    public static final GR<CharPointer, FloatNumberTok> intAsBigDecimal
        = digits.next(test(c->c=='w' || c=='W')).next(test( c->c=='d' || c=='D' || c=='f' || c=='F' ))
        .map( (dgts,sf1,sf2)->new FloatNumberTok(dgts.begin(), sf2.end(), dgts, null).precision(FloatPrecision.BIGDECIMAL) );

    /**
     * пробельные символы
     */
    public static final GR<CharPointer, WhiteSpaceTok> ws
        = whitespace.repeat().map(WhiteSpaceTok::new);

    /**
     * Ключевые слова, операторы и прочие однозначные конструкуии
     */
    public static final GR<CharPointer, KeywordTok> keyword
        = Keyword.lexer();

    /**
     * Идентификатор
     */
    public static final GR<CharPointer, IdTok> id
        = test( c->Character.isLetter(c) || c=='_' )
          .next( test(c->Character.isLetter(c) || c=='_' || Character.isDigit(c)).repeat().map(CToken::new) )
          .map( (a,b) -> new IdTok(a.begin(),b.end()) )
        .<IdTok>another(
            test(c->Character.isLetter(c) || c=='_', p -> new IdTok(p,p.move(1)) )
        ).map()
        ;

    /**
     * Список парсеров лексем
     */
    private static final List<GR<CharPointer, ? extends CToken>> tokenParsers =
        new ArrayList(){{
            add(floatPrecisionSuffix(floatNumber));
            add(floatPrecisionSuffix(dotSuffFloatNumber));
            add(floatPrecisionSuffix(dotPrefFloatNumber));
            add(integerPrecisionSuffix(hexIntegerNumber));
            add(integerPrecisionSuffix(binIntegerNumber));
            add(integerPrecisionSuffix(octIntegerNumber));
            add(intAsDouble);
            add(intAsFloat);
            add(intAsBigDecimal);
            add(integerPrecisionSuffix(integerNumber));
            add(StringTok.javascript);
            add(keyword);
            add(id);
            add(ws);
        }};

    /**
     * Лексический анализатор
     * @param source исходный текст
     * @return анализатор
     */
    public static Tokenizer<CharPointer, ? extends CToken> tokenizer( String source ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        //noinspection unchecked
        return Tokenizer.lexer(source, tokenParsers);
    }


    /**
     * Лексический анализатор
     * @param source исходный текст
     * @param from с какого символа (0 и больше) начинать парсинг
     * @return анализатор
     */
    public static Tokenizer<CharPointer, ? extends CToken> tokenizer( String source, int from ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        if( from<0 )throw new IllegalArgumentException( "from<0" );
        //noinspection unchecked
        return Tokenizer.lexer(source, from, tokenParsers);
    }

    /**
     * Парсинг текста и получение набора лексем
     * @param source исходный текст
     * @return список лексем без пробелов
     */
    public static List<? extends CToken> tokens( String source ){
        if( source==null )throw new IllegalArgumentException( "source==null" );

        Tokenizer<CharPointer, ? extends CToken> tknz = tokenizer(source);
        List<? extends CToken> toks = tknz.toList();

        if( source.length()>0 ){
            if( toks.isEmpty() )throw new ParseError("lexer return 0 tokens for non empty source");
            CToken lastTok = toks.get(toks.size()-1);
            if( lastTok.end().position() < source.length() ){
                throw new ParseError("lexer not parse all source");
            }
        }

        return toks.stream().filter( t-> !(t instanceof WhiteSpaceTok) ).collect(Collectors.toList());
    }

    /**
     * Парсинг текста и получение набора лексем
     * @param source исходный текст
     * @param from с какого символа (0 и больше) начинать парсинг
     * @return список лексем без пробелов
     */
    public static List<? extends CToken> tokens( String source, int from ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        if( from<0 )throw new IllegalArgumentException( "from<0" );

        Tokenizer<CharPointer, ? extends CToken> tknz = tokenizer(source);
        List<? extends CToken> toks = tknz.toList();
        int parseSize = source.length() - from;
        if( parseSize>0 ){
            if( toks.isEmpty() )throw new ParseError("lexer return 0 tokens for non empty source");
            CToken lastTok = toks.get(toks.size()-1);
            if( lastTok.end().position() < source.length() ){
                throw new ParseError("lexer not parse all source");
            }
        }

        return toks.stream().filter( t-> !(t instanceof WhiteSpaceTok) ).collect(Collectors.toList());
    }
}
