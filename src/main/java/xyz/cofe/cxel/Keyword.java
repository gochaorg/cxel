package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.KeywordAST;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

import java.util.Arrays;
import java.util.Optional;

/**
 * Список ключевых слов
 */
public enum Keyword {
    /**
     * Открытая круглая скобка
     */
    OpenBracket("("),

    /**
     * Закрытая круглая скобка
     */
    CloseBracket(")"),

    /** Плюс */ Plus("+"),
    /** Минус */ Minus("-"),
    /** Делить */ Divide("/"),
    /** Множить */ Multiple("*"),
    /** Точка */ Dot(".")
    ;

    /**
     * Ключевое слово
     */
    public final String text;

    /**
     * Конструктор
     * @param text ключевое слово
     */
    Keyword( String text ){
        if( text==null )throw new IllegalArgumentException( "text==null" );
        if( text.length()<1 )throw new IllegalArgumentException( "text.length()<1" );
        if( KeywordImpl.predefined.contains(text) )throw new IllegalArgumentException(
            "text=\""+text+"\" already defined"
        );
        KeywordImpl.predefined.add(text);
        this.text = text;
    }

    private static Keyword[] ordDescByLen;

    /**
     * Ключевые слова отсортированные в порядке убывания длины
     * @return ключевые слова
     */
    public static Keyword[] keywordsByReverseLen(){
        if( ordDescByLen!=null )return ordDescByLen;
        Keyword[] kws = Arrays.copyOf(values(), values().length);
        Arrays.sort( kws, (a,b)-> -Integer.compare(a.text.length(), b.text.length() ) );
        ordDescByLen = kws;
        return ordDescByLen;
    }

    public GR<CharPointer, KeywordTok> lex(){
        return ptr -> {
            for( int i=0; i<text.length(); i++ ){
                Optional<Character> chr = ptr.lookup(i);
                if( !chr.isPresent() )return Optional.empty();
                if( text.charAt(i)!=chr.get() )return Optional.empty();
            }
            return Optional.of( new KeywordTok(this,ptr,ptr.move(text.length())) );
        };
    }

    /**
     * Возвращает грамматическое правило лексичекого парсера
     * @return парсер
     */
    public static GR<CharPointer,KeywordTok> lexer(){
        return ptr -> {
            if( ptr.eof() )return Optional.empty();
            for( Keyword kw : keywordsByReverseLen() ){
                boolean matched = true;
                for( int i=0; i<kw.text.length(); i++ ){
                    Optional<Character> c = ptr.lookup(i);
                    if( !c.isPresent() ){
                        matched = false;
                        break;
                    }
                    if( c.get() != kw.text.charAt(i) ){
                        matched = false;
                        break;
                    }
                }
                if( matched ){
                    return Optional.of( new KeywordTok(kw, ptr, ptr.move(kw.text.length())) );
                }
            }
            return Optional.empty();
        };
    }

    /**
     * Парсинг входящей цепочки на свопадение с указанной лексеммой
     * @return парсер
     */
    public GR<TPointer, KeywordAST> parser(){
        return ptr -> {
            Optional<CToken> tok = ptr.lookup(0);
            if( !tok.isPresent() )return Optional.empty();

            CToken t = tok.get();
            if( t instanceof KeywordTok ){
                if( ((KeywordTok) t).keyword.text.equals( this.text ) ){
                    return Optional.of( new KeywordAST(ptr, (KeywordTok)t) );
                }
            }

            return Optional.empty();
        };
    }

    /**
     * Создание парсера для нескольких ключевых слов объеденных "альтернативой"
     * @param keywords ключевые слова
     * @return парсер
     */
    public static GR<TPointer,KeywordAST> parserOf( Keyword ... keywords ){
        if( keywords==null )throw new IllegalArgumentException( "keywords==null" );
        if( keywords.length<1 )throw new IllegalArgumentException( "keywords.length<1" );
        if( keywords.length==1 )return keywords[0].parser();
        return ptr -> {
            Optional<KeywordAST> res = null;
            for( Keyword k : keywords ){
                res = k.parser().apply(ptr);
                if( res!=null && res.isPresent() )return res;
            }
            return Optional.empty();
        };
    }
}
