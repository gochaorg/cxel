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
    public final Keyword keyword;
}
