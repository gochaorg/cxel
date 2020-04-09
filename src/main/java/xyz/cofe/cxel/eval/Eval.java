package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
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
        }else if( ast instanceof IndexAST ){
            return index( (IndexAST) ast );
        }
        throw new EvalError("can't evaluate undefined ast: "+ast.getClass());
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

    protected Object callOperator( UnaryOpAST op ){
        Object v = eval(op.operand());
        return context.call(null,op.opText(), Collections.singletonList(v));
    }

    private Object callOperator( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());
        return context.call(null,op.opText(), Arrays.asList(vLeft,vRight));
    }

    protected Object variable( VarRefAST ast ){
        return context.read(ast.variable());
    }
    protected Object property( PropertyAST ast ){
        Object obj = eval(ast.base());
        return context.get(obj,ast.property());
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
    protected Object index( IndexAST ast ){
        Object obj = eval(ast.base());
        Object idx = eval(ast.index());
        return context.getAt(obj,idx);
    }
}
