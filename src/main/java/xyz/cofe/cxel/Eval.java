package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.ast.BinaryOpAST;
import xyz.cofe.cxel.ast.NumberAST;
import xyz.cofe.cxel.ast.UnaryOpAST;
import xyz.cofe.num.BaseNumbers;
import xyz.cofe.num.BitCount;
import xyz.cofe.num.CommonBase;

/**
 * Непосредственная интерпретация AST
 */
public class Eval {
    /**
     * Интерпретация ast дерева
     * @param ast дерево
     * @return результат
     */
    public Object eval( AST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        if( ast instanceof UnaryOpAST ){
            return unary( (UnaryOpAST)ast );
        }else if( ast instanceof BinaryOpAST ){
            return binary( (BinaryOpAST) ast );
        }else if( ast instanceof NumberAST ){
            return number( (NumberAST)ast );
        }
        throw new RuntimeException("can't evaluate undefined ast: "+ast.getClass());
    }

    //region eval number
    protected Object number( NumberAST nast ){
        return nast.value();
    }
    //endregion

    //region unary()
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    protected Object unary( UnaryOpAST op ){
        switch( op.opKeyword() ){
            case Minus:
                return unaryMinus(op);
            default:
                throw new RuntimeException(
                    "can't eval unary for keyword "+op.opKeyword()+" ("+op.opText()+")"
                );
        }
    }

    protected Object unaryMinus( UnaryOpAST op ){
        Object val = eval(op.operand());
        if( !(val instanceof Number) )
            throw new RuntimeException(
                "can't eval unary minus for not Number ("+
                    (val!=null ? val.getClass() : "null")+
                    ")");

        Number n = (Number)val;
        return BaseNumbers.commonBase(0,n).sub();
    }
    //endregion

    //region binary()
    protected Object binary( BinaryOpAST op ){
        switch( op.opKeyword() ){
            case Plus: return plus(op);
            case Minus: return minus(op);
            case Multiple: return multiple(op);
            case Divide: return divide(op);
            default:
                throw new RuntimeException(
                    "can't eval binary for keyword "+op.opKeyword()+" ("+op.opText()+")"
                );
        }
    }

    protected Object plus( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        if( !(vLeft instanceof Number) )
            throw new RuntimeException(
                "can't eval plus for not Number - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");

        Object vRight = eval(op.right());
        if( !(vRight instanceof Number) )
            throw new RuntimeException(
                "can't eval plus for not Number - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");

        return plus( (Number)vLeft, (Number)vRight );
    }

    protected Object minus( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        if( !(vLeft instanceof Number) )
            throw new RuntimeException(
                "can't eval minus for not Number - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");

        Object vRight = eval(op.right());
        if( !(vRight instanceof Number) )
            throw new RuntimeException(
                "can't eval minus for not Number - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");

        return minus( (Number)vLeft, (Number)vRight );
    }

    protected Object multiple( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        if( !(vLeft instanceof Number) )
            throw new RuntimeException(
                "can't eval multiple for not Number - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");

        Object vRight = eval(op.right());
        if( !(vRight instanceof Number) )
            throw new RuntimeException(
                "can't eval multiple for not Number - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");

        return multiple( (Number)vLeft, (Number)vRight );
    }

    protected Object divide( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        if( !(vLeft instanceof Number) )
            throw new RuntimeException(
                "can't eval divide for not Number - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");

        Object vRight = eval(op.right());
        if( !(vRight instanceof Number) )
            throw new RuntimeException(
                "can't eval divide for not Number - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");

        return divide( (Number)vLeft, (Number)vRight );
    }
    //endregion

    //region math operations
    /**
     * Сложение чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Сумма
     */
    private static Number plus( Number n1, Number n2 ){
        if( n1==null )throw new IllegalArgumentException( "n1==null" );
        if( n2==null )throw new IllegalArgumentException( "n2==null" );
        return BaseNumbers.commonBase(n1,n2).add();
    }

    /**
     * Вычитание чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Разница
     */
    private static Number minus( Number n1, Number n2 ){
        if( n1==null )throw new IllegalArgumentException( "n1==null" );
        if( n2==null )throw new IllegalArgumentException( "n2==null" );
        return BaseNumbers.commonBase(n1,n2).sub();
    }

    /**
     * Умножение чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Произведение
     */
    private static Number multiple( Number n1, Number n2 ){
        if( n1==null )throw new IllegalArgumentException( "n1==null" );
        if( n2==null )throw new IllegalArgumentException( "n2==null" );
        return BaseNumbers.commonBase(n1,n2).mul();
    }

    /**
     * Деление чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Произведение
     */
    private static Number divide( Number n1, Number n2 ){
        if( n1==null )throw new IllegalArgumentException( "n1==null" );
        if( n2==null )throw new IllegalArgumentException( "n2==null" );
        return BaseNumbers.commonBase(n1,n2).div();
    }

    /**
     * Деление чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Произведение
     */
    private static Number cmp( Number n1, Number n2 ){
        if( n1==null )throw new IllegalArgumentException( "n1==null" );
        if( n2==null )throw new IllegalArgumentException( "n2==null" );

        CommonBase cbase = BaseNumbers.commonBase(n1,n2);

        boolean less = cbase.less();
        if( less )return -1;

        boolean eq = cbase.equals();
        if( eq )return 0;

        boolean more = cbase.more();
        if( more )return 1;

        throw new RuntimeException("bug at compare numbers");
    }
    //endregion
}
