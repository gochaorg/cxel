package xyz.cofe.cxel;

import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.cofe.text.tparse.Chars.*;

/**
 * Лексемы
 */
public class Lexer {
    /**
     * Целое число
     */
    public static final GR<CharPointer, IntegerNumberTok> integerNumber
        = digit.repeat().map( digits -> new IntegerNumberTok( new DigitsTok(digits) ) );

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
            add(floatNumber);
            add(integerNumber);
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
