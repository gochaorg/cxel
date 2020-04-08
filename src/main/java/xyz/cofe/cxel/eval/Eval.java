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
            return callOperator( (UnaryOpAST)ast );
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
    protected Object callOperator( UnaryOpAST op ){
        Object v = eval(op.operand());
        return context.call(null,op.opText(), Collections.singletonList(v));
    }
    //endregion

    //region binary()
    private Object callOperator( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());
        return context.call(null,op.opText(), Arrays.asList(vLeft,vRight));
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
