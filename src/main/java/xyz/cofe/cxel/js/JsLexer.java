package xyz.cofe.cxel.js;

import xyz.cofe.cxel.Lexer;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.text.tparse.CToken;

import java.util.List;
import java.util.function.Predicate;

public class JsLexer extends Lexer {
    /**
     * Замена всех {@link xyz.cofe.cxel.tok.IntegerNumberTok} на {@link ForcedFloatNumberTok}
     * @param toks токены/лексемы
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void hookTokens( List toks ){
        if( toks==null )return;
        for( int i=0; i<toks.size(); i++ ){
            Object tok = toks.get(i);
            if( tok!=null && tok.getClass()== IntegerNumberTok.class ){
                IntegerNumberTok itok = (IntegerNumberTok)tok;
                toks.set(i, new ForcedFloatNumberTok(itok));
            }
        }
    }

    @Override
    public List<? extends CToken> tokens( String source ){
        List<? extends CToken> toks = super.tokens(source);
        hookTokens(toks);
        return toks;
    }

    @Override
    public List<? extends CToken> tokens( String source, int from ){
        List<? extends CToken> toks = super.tokens(source, from);
        hookTokens(toks);
        return toks;
    }

    @Override
    public List<? extends CToken> tokens( String source, int from, Predicate<CToken> filter ){
        List<? extends CToken> toks = super.tokens(source, from, filter);
        hookTokens(toks);
        return toks;
    }
}
