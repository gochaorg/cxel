package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Литерал - null
 */
public class NullAST extends ASTBase<NullAST> implements LiteralAST {
    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected NullAST( NullAST sample ) {
        super(sample);
        if( sample!=null ){
            this.token = sample.token;
        }
    }

    /**
     * Конструктор
     * @param begin указатель на расположение
     * @param tok токен
     */
    public NullAST( TPointer begin, KeywordTok tok) {
        if(begin==null)throw new IllegalArgumentException("begin==null");
        if( tok==null )throw new IllegalArgumentException("tok==null");
        this.begin = begin;
        this.end = begin.move(1);
        this.token = tok;
    }

    /**
     * Клонирование
     * @return клон
     */
    public NullAST clone(){ return new NullAST(this); }

    /**
     * токен соответствующий числу
     */
    protected KeywordTok token;

    /**
     * Возвращает токен соответствующий числу
     * @return токен
     */
    public KeywordTok token(){ return token; }

    /**
     * Указывает токен соответствующий числу
     * @param t токен
     * @return клон с новым токеном
     */
    public NullAST token( KeywordTok t ){
        if( t==null )throw new IllegalArgumentException("t==null");
        NullAST c = clone();
        c.token = t;
        return c;
    }

    /**
     * Возвращает null ссылку
     * @return null ссылка
     */
    public Object value(){
        return null;
    }

    @Override
    public String toString() {
        return NullAST.class.getSimpleName()+" "+(token.text());
    }

    @Override
    public AST[] children(){ return emptyChildren(); }
}