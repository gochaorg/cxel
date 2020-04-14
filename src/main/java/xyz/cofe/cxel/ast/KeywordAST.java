package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Ключевое слово, зрезервировано для описания AST узлов как сложение, вычитание, скобок и прочего,
 * где операторы являются ключевыми словами
 */
public class KeywordAST extends ASTBase<KeywordAST> {
    /**
     * Конструктор по умолчанию
     */
    protected KeywordAST(){
        super();
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected KeywordAST( KeywordAST sample ){
        super(sample);
        this.keywordTok = sample!=null ? sample.keywordTok : null;
    }

    /**
     * Конструктор
     * @param begin указатель на ключевое слово
     * @param tok само ключевое слово
     */
    public KeywordAST( TPointer begin, KeywordTok tok ){
        super(begin, begin.move(1));
        if( tok==null )throw new IllegalArgumentException( "tok==null" );
        this.keywordTok = tok;
    }

    /**
     * ключевое слово
     */
    protected KeywordTok keywordTok;

    /**
     * Вохвразает ключевое слово
     * @return ключевое слово
     */
    public KeywordTok getKeywordTok(){ return keywordTok; }

    /**
     * Клонирование
     * @return клон
     */
    @Override
    public KeywordAST clone(){
        return new KeywordAST(this);
    }

    @Override
    public AST[] children(){
        return emptyChildren();
    }
}
