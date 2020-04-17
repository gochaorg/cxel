package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.NumberTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Литерал - число
 */
public class NumberAST extends ASTBase<NumberAST> implements LiteralAST {
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
     * @param ptr указатель на расположение
     * @param numberToken токен
     */
    public NumberAST( TPointer ptr, NumberTok numberToken) {
        if(ptr==null)throw new IllegalArgumentException("ptr==null");
        if( numberToken==null )throw new IllegalArgumentException("numberToken==null");
        this.begin = ptr;
        this.end = ptr.move(1);
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
    public NumberTok numberTok(){ return numberToken; }

    /**
     * Указывает токен соответствующий числу
     * @param t токен
     * @return клон с новым токеном
     */
    public NumberAST numberTok( NumberTok t){
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