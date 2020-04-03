package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Унарный оператор
 */
public class UnaryOpAST extends OpAST<UnaryOpAST> {
    /**
     * Конструктор
     * @param begin начало оператора
     * @param end конец оператора
     * @param op оператор
     * @param operand операнд
     */
    public UnaryOpAST( TPointer begin, TPointer end, KeywordAST op, AST operand ){
        super(begin, end, op);
        this.operand = operand;
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected UnaryOpAST( UnaryOpAST sample ){
        super(sample);
        this.operand = sample.operand;
        this.operator = sample.operator;
    }

    /**
     * Клонирование
     * @return клон
     */
    @Override
    public UnaryOpAST clone(){
        return new UnaryOpAST(this);
    }

    //region operand : AST - Операнд
    /**
     * Операнд
     */
    protected AST operand;

    /**
     * Возвращает операнд
     * @return операнд
     */
    public AST operand(){ return operand; }

    /**
     * Указывает операнд
     * @param o операнд
     * @return клон с новым операндом
     */
    public UnaryOpAST operand( AST o ){
        if( o==null )throw new IllegalArgumentException( "o==null" );
        return cloneAndConf( c->c.operand=o );
    }
    //endregion

    @Override
    public AST[] children(){
        return children(operand);
    }
}
