package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Литерал - булево
 */
public class BooleanAST extends ASTBase<BooleanAST> {
    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected BooleanAST( BooleanAST sample ) {
        super(sample);
        if( sample!=null ){
            this.keywordTok = sample.keywordTok;
        }
    }

    /**
     * Конструктор
     * @param begin указатель на расположение
     * @param tok токен
     */
    public BooleanAST( TPointer begin, KeywordTok tok) {
        if(begin==null)throw new IllegalArgumentException("begin==null");
        if( tok==null )throw new IllegalArgumentException("tok==null");
        this.begin = begin;
        this.end = begin.move(1);
        this.keywordTok = tok;
    }

    /**
     * Клонирование
     * @return клон
     */
    public BooleanAST clone(){ return new BooleanAST(this); }

    /**
     * токен соответствующий числу
     */
    protected KeywordTok keywordTok;

    /**
     * Возвращает токен соответствующий числу
     * @return токен
     */
    public KeywordTok keywordTok(){ return keywordTok; }

    /**
     * Указывает токен соответствующий числу
     * @param t токен
     * @return клон с новым токеном
     */
    public BooleanAST keywordTok( KeywordTok t ){
        if( t==null )throw new IllegalArgumentException("t==null");
        BooleanAST c = clone();
        c.keywordTok = t;
        return c;
    }

    /**
     * Возвращает булево
     * @return булево
     */
    public Boolean value(){
        KeywordTok tok = keywordTok;
        if( tok!=null && tok.keyword()!=null ){
            switch( tok.keyword() ){
                case True: return true;
                case False: return false;
                default:
                    throw new EvalError("can't eval token "+tok.keyword()+" as boolean");
            }
        }
        throw new EvalError("can't eval token");
    }

    @Override
    public String toString() {
        return BooleanAST.class.getSimpleName()+" "+(keywordTok.text());
    }

    @Override
    public AST[] children(){ return emptyChildren(); }
}