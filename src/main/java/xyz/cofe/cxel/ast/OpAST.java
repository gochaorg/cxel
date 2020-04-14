package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Базовый класс для оператора
 * @param <SELF> собственный (производный) тип
 */
public abstract class OpAST<SELF extends OpAST<SELF>> extends ASTBase<SELF> {
    /**
     * Конструктор
     * @param begin начало
     * @param end конец
     * @param op оператор
     */
    public OpAST( TPointer begin, TPointer end, KeywordAST op){
        super(begin, end);
        if( op==null )throw new IllegalArgumentException( "op==null" );
        this.operator = op;
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected OpAST( OpAST<SELF> sample ){
        super(sample);
        this.operator = sample.operator;
    }

    /**
     * Возвращает ключевое слово
     * @return ключевое слово
     */
    public Keyword opKeyword(){
        KeywordAST kw = operator;
        KeywordTok tok = kw!=null ? kw.getKeywordTok() : null;
        return tok!=null ? tok.keyword() : null;
    }

    /**
     * Возвращает ключевое слово (текст)
     * @return ключевое слово
     */
    public String opText(){
        Keyword kw = opKeyword();
        return kw!=null ? kw.text : null;
    }

    //region operator : KeywordAST - оператор
    /**
     * оператор
     */
    protected KeywordAST operator;

    /**
     * Указывает оператор
     * @return оператор
     */
    public KeywordAST operator(){ return operator; }

    /**
     * Указывает оператор
     * @param op оператор
     * @return клон с новым оператором
     */
    public SELF operator(KeywordAST op ){
        if( op==null )throw new IllegalArgumentException("op==null");
        SELF c = clone();
        c.operator = op;
        return c;
    }
    //endregion
}
