package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

public class KeywordAST extends ASTBase<KeywordAST> {
    protected KeywordAST(){
        super();
    }

    protected KeywordAST( KeywordAST sample ){
        super(sample);
        this.keywordTok = sample!=null ? sample.keywordTok : null;
    }

    public KeywordAST( TPointer begin, KeywordTok tok ){
        super(begin, begin.move(1));
        if( tok==null )throw new IllegalArgumentException( "tok==null" );
        this.keywordTok = tok;
    }

    protected KeywordTok keywordTok;
    public KeywordTok getKeywordTok(){ return keywordTok; }

    @Override
    public KeywordAST clone(){
        return new KeywordAST(this);
    }

    @Override
    public AST[] children(){
        return emptyChildren();
    }
}
