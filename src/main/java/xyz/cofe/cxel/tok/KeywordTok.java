package xyz.cofe.cxel.tok;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

/**
 * Ключевое слово
 */
public class KeywordTok extends CToken {
    public KeywordTok( Keyword k, CharPointer begin, CharPointer end ){
        super(begin, end);
        this.keyword = k;
        if( k==null )throw new IllegalArgumentException( "k==null" );
    }

    /**
     * Ключевое слово
     */
    private Keyword keyword;

    /**
     * Возвращает ключевое слово
     * @return ключевое слово
     */
    public Keyword keyword(){ return keyword; }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public KeywordTok( KeywordTok sample ){
        super(sample);
        this.keyword = sample.keyword;
    }

    public KeywordTok clone(){ return new KeywordTok(this); }
}
