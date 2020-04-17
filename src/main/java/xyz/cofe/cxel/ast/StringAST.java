package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.StringTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Строковой литерал
 */
public class StringAST extends ASTBase<StringAST> implements LiteralAST {
    /**
     * Конструктор по умолчанию
     */
    protected StringAST(){
        super();
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    protected StringAST( StringAST sample ){
        super(sample);
        stringTok = sample.stringTok;
    }

    /**
     * Конструктор
     * @param ptr Указатель
     * @param tok Лексема
     */
    public StringAST( TPointer ptr, StringTok tok ){
        super(ptr, ptr.move(1));
        if( tok==null )throw new IllegalArgumentException( "tok==null" );
        this.stringTok = tok;
    }

    /**
     * Клонирование
     * @return клон
     */
    @Override
    public StringAST clone(){
        return new StringAST(this);
    }

    @Override
    public AST[] children(){
        return emptyChildren();
    }

    protected StringTok stringTok;

    /**
     * Возвращает лексему
     * @return лексема
     */
    public StringTok stringTok(){ return stringTok; }

    /**
     * Возвращает декодированное значение
     * @return значение
     */
    public String value(){
        StringTok t = stringTok;
        return t!=null ? t.value() : null;
    }
}
