package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.ast.*;
import xyz.cofe.num.BaseNumbers;
import xyz.cofe.num.BitCount;
import xyz.cofe.num.CommonBase;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Непосредственная интерпретация AST
 */
public class Eval {
    //region create and configure
    public Eval(){
        context = new EvalContext();
    }
    public Eval(EvalContext ctx){
        if( ctx==null )throw new IllegalArgumentException( "ctx==null" );
        context = ctx;
    }
    public Eval configure( Consumer<Eval> conf ){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }
    //endregion

    //region context
    protected final EvalContext context;
    public EvalContext context(){ return context; }
    //endregion

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
            return callOperator( (BinaryOpAST) ast );
        }else if( ast instanceof NumberAST ){
            return number( (NumberAST)ast );
        }else if( ast instanceof BooleanAST ){
            return bool( (BooleanAST)ast );
        }else if( ast instanceof StringAST ){
            return string( (StringAST)ast );
        }else if( ast instanceof NullAST ){
            return nul( (NullAST) ast );
        }else if( ast instanceof VarRefAST ){
            return variable( (VarRefAST) ast );
        }else if( ast instanceof PropertyAST ){
            return property( (PropertyAST) ast );
        }else if( ast instanceof CallAST ){
            return call( (CallAST) ast );
        }
        throw new RuntimeException("can't evaluate undefined ast: "+ast.getClass());
    }

    //region eval literal : number, bool, nul, str
    protected Object string( StringAST nast ){
        return nast.value();
    }
    protected Object number( NumberAST nast ){
        return nast.value();
    }
    protected Object bool( BooleanAST ast ){
        return ast.value();
    }
    protected Object nul( NullAST ast ){
        return ast.value();
    }
    //endregion

    //region unary()
    protected Object unary( UnaryOpAST op ){
        switch( op.opKeyword() ){
            case Minus: return unaryMinus(op);
            case Not: return unaryNot(op);
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
    protected Object unaryNot( UnaryOpAST op ){
        Object val = eval(op.operand());
        if( !(val instanceof Boolean) )
            throw new RuntimeException(
                "can't eval unary not for not Boolean ("+
                    (val!=null ? val.getClass() : "null")+
                    ")");

        Boolean n = (Boolean) val;
        return !n;
    }
    //endregion

    //region binary()
    protected Object binary( BinaryOpAST op ){
        switch( op.opKeyword() ){
            case Plus: return plus(op);
            case Minus: return minus(op);
            case Multiple: return multiple(op);
            case Divide: return divide(op);
            case Less: return less(op);
            case LessOrEquals: return lessOrEquals(op);
            case Equals: return equals(op);
            case NotEquals: return notEquals(op);
            case More: return more(op);
            case MoreOrEquals: return moreOrEquals(op);
            case And: return and(op);
            case Or: return or(op);
            default:
                throw new RuntimeException(
                    "can't eval binary for keyword "+op.opKeyword()+" ("+op.opText()+")"
                );
        }
    }

    //region binary: + - * /
    private Object callOperator( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());
        return context.call(null,op.opText(), Arrays.asList(vLeft,vRight));
    }

    protected Object plus( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft instanceof String ){
            String str = vLeft.toString();
            str = str + vRight;
            return str;
        }

        if( vLeft instanceof Number && vRight instanceof Number ){
            return plus( (Number)vLeft, (Number)vRight );
        }

        if( vRight instanceof String ){
            String str = vRight.toString();
            str = "" + vLeft + str;
            return str;
        }

        throw new RuntimeException("can't eval plus for "+
           (vLeft==null ? "null" : vLeft.getClass())+" and "+
           (vRight==null ? "null" : vRight.getClass())
        );
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
    //region binary: < <= == != >= >
    @SuppressWarnings({ "ConstantConditions", "unchecked", "rawtypes" })
    protected Object less( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return false;

        if( vLeft==null && vRight!=null )return true;
        if( vLeft!=null && vRight==null )return false;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) < 0;
        }

        if( vLeft instanceof Comparable ){
            return ((Comparable) vLeft).compareTo(vRight) < 0;
        }

        throw new RuntimeException("can't compare for "+vLeft.getClass()+" < "+vRight.getClass());
    }
    @SuppressWarnings({ "ConstantConditions", "unchecked", "rawtypes" })
    protected Object lessOrEquals( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return true;

        if( vLeft==null && vRight!=null )return true;
        if( vLeft!=null && vRight==null )return false;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) <= 0;
        }

        if( vLeft instanceof Comparable ){
            return ((Comparable) vLeft).compareTo(vRight) <= 0;
        }

        throw new RuntimeException("can't compare for "+vLeft.getClass()+" <= "+vRight.getClass());
    }
    @SuppressWarnings({ "ConstantConditions", "unchecked", "rawtypes" })
    protected Object more( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return false;

        if( vLeft==null && vRight!=null )return false;
        if( vLeft!=null && vRight==null )return true;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) > 0;
        }

        if( vLeft instanceof Comparable ){
            return ((Comparable) vLeft).compareTo(vRight) > 0;
        }

        throw new RuntimeException("can't compare for "+vLeft.getClass()+" > "+vRight.getClass());
    }
    @SuppressWarnings({ "ConstantConditions", "unchecked", "rawtypes" })
    protected Object moreOrEquals( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return true;

        if( vLeft==null && vRight!=null )return false;
        if( vLeft!=null && vRight==null )return true;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) >= 0;
        }

        if( vLeft instanceof Comparable ){
            return ((Comparable) vLeft).compareTo(vRight) >= 0;
        }

        throw new RuntimeException("can't compare for "+vLeft.getClass()+" >= "+vRight.getClass());
    }
    @SuppressWarnings({ "ConstantConditions" })
    protected Object equals( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return true;

        if( vLeft==null && vRight!=null )return false;
        if( vLeft!=null && vRight==null )return false;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) == 0;
        }

        return vLeft.equals(vRight);
    }
    @SuppressWarnings({ "ConstantConditions" })
    protected Object notEquals( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());

        if( vLeft==null && vRight==null )return false;

        if( vLeft==null && vRight!=null )return true;
        if( vLeft!=null && vRight==null )return true;

        if( vLeft instanceof Number && vRight instanceof Number ){
            return cmp( (Number)vLeft, (Number)vRight ) != 0;
        }

        return !vLeft.equals(vRight);
    }
    //endregion
    //region binary: & |
    protected Object and( BinaryOpAST op ){
        Object vLeft = eval(op.left());

        if( !(vLeft instanceof Boolean) ){
            throw new RuntimeException(
                "can't eval and for not Boolean - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");
        }

        Object vRight = eval(op.right());
        if( !(vRight instanceof Boolean) ){
            throw new RuntimeException(
                "can't eval and for not Boolean - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");
        }

        return ((Boolean)vLeft) && ((Boolean)vRight);
    }
    protected Object or( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        if( !(vLeft instanceof Boolean) ){
            throw new RuntimeException(
                "can't eval or for not Boolean - left operand ("+
                    (vLeft!=null ? vLeft.getClass() : "null")+
                    ")");
        }

        Object vRight = eval(op.right());
        if( !(vRight instanceof Boolean) ){
            throw new RuntimeException(
                "can't eval or for not Boolean - right operand ("+
                    (vRight!=null ? vRight.getClass() : "null")+
                    ")");
        }

        return ((Boolean)vLeft) || ((Boolean)vRight);
    }
    //endregion

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
     * Сравнение чисел
     * @param n1 первое число
     * @param n2 второе число
     * @return Произведение
     */
    private static int cmp( Number n1, Number n2 ){
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

    protected Object variable( VarRefAST ast ){
        return context.read(ast.variable());
    }
    protected Object property( PropertyAST ast ){
        Object obj = eval(ast.base());
        if( obj==null )throw new IllegalArgumentException("can't read property '"+ast.property()+"' of null");

        if( obj instanceof Map ){
            return ((Map)obj).get(ast.property());
        }

        throw new RuntimeException("can't resolve property '"+ast.property()+"' for obj of type "+obj.getClass());
    }
    protected Object call( CallAST ast ){
        AST base = ast.base();
        if( base instanceof VarRefAST ){
            List<Object> args = new ArrayList<>();
            for( AST arg : ast.args() ){
                args.add( eval(arg) );
            }

            return context.call(null, ((VarRefAST) base).variable(), args );
        }else if( base instanceof PropertyAST ){
            PropertyAST past = (PropertyAST)base;
            String propName = past.property();

            Object obj = eval(past.base());

            List<Object> args = new ArrayList<>();
            for( AST arg : ast.args() ){
                args.add( eval(arg) );
            }

            return context.call(obj,propName,args);
        }
        return null;
    }
}
