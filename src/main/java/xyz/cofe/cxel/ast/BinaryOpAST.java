package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.Pointer;

/**
 * Бинарный оператор
 */
public class BinaryOpAST extends OpAST<BinaryOpAST> {
    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected BinaryOpAST(BinaryOpAST sample) {
        super(sample);
        if( sample!=null ){
            operator = sample.operator;
            left = sample.left;
            right = sample.right;
        }
    }

    /**
     * Конструктор
     * @param left левый операнд
     * @param op оператор
     * @param right правй операнд
     */
    public BinaryOpAST( AST left, KeywordAST op, AST right) {
        super( left.begin(), right.end(), op );
        this.begin = Pointer.min( left.begin(), left.end(), right.begin(), right.end() );
        this.end = Pointer.max(left.begin(), left.end(), right.begin(), right.end());
        this.operator = op;
        this.left = left;
        this.right = right;
    }

    /**
     * Клонирование
     * @return клон
     */
    public BinaryOpAST clone(){ return new BinaryOpAST(this); }

    //region left : AST - левый операнд
    /**
     * левый операнд
     */
    protected AST left;

    /**
     * Возвращает левый операнд
     * @return левый операнд
     */
    public AST left(){ return left; }

    /**
     * Указывает левый операнд
     * @param left левый операнд
     * @return клон с новым операндом
     */
    public BinaryOpAST left( AST left ){
        if( left==null )throw new IllegalArgumentException( "left==null" );
        return cloneAndConf( c->c.left = left );
    }
    //endregion
    //region right : AST - правый операнд
    /**
     * правый операнд
     */
    protected AST right;

    /**
     * Возвращает правй операнд
     * @return правый операнд
     */
    public AST right(){ return right; }

    /**
     * Указывает правый операнд
     * @param right операнд
     * @return клон с новым операндом
     */
    public BinaryOpAST right(AST right){
        if( right==null )throw new IllegalArgumentException( "right==null" );
        return cloneAndConf( c->c.right=right);
    }
    //endregion

    @Override
    public String toString() {
        return
            "left("+left+")"+
                " "+BinaryOpAST.class.getSimpleName()+" "+operator.keywordTok.text()+
                " right("+right+")";
    }

    @Override
    public AST[] children(){
        return children(left, right);
    }
}
