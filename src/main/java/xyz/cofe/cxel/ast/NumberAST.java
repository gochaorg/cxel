package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.NumberTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Литерал - число
 */
public class NumberAST extends ASTBase<NumberAST> {
    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected NumberAST(NumberAST sample) {
        super(sample);
        if( sample!=null ){
            this.numberToken = sample.numberToken;
        }
    }

    /**
     * Конструктор
     * @param begin указатель на расположение
     * @param numberToken токен
     */
    public NumberAST( TPointer begin, NumberTok numberToken) {
        if(begin==null)throw new IllegalArgumentException("begin==null");
        if( numberToken==null )throw new IllegalArgumentException("numberToken==null");
        this.begin = begin;
        this.end = begin.move(1);
        this.numberToken = numberToken;
    }

    /**
     * Клонирование
     * @return клон
     */
    public NumberAST clone(){ return new NumberAST(this); }

    /**
     * токен соответствующий числу
     */
    protected NumberTok numberToken;

    /**
     * Возвращает токен соответствующий числу
     * @return токен
     */
    public NumberTok numberToken(){ return numberToken; }

    /**
     * Указывает токен соответствующий числу
     * @param t токен
     * @return клон с новым токеном
     */
    public NumberAST numberToken(NumberTok t){
        if( t==null )throw new IllegalArgumentException("t==null");
        NumberAST c = clone();
        c.numberToken = t;
        return c;
    }

    /**
     * Возвращает число
     * @return число
     */
    public Number value(){
        NumberTok tok = numberToken;
        return tok!=null ? tok.value() : null;
    }

    @Override
    public String toString() {
        return NumberAST.class.getSimpleName()+" "+(numberToken.text());
    }

    @Override
    public AST[] children(){ return emptyChildren(); }
}