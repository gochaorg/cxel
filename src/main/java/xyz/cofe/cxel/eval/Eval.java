package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.Parser;
import xyz.cofe.cxel.ast.*;
import xyz.cofe.text.tparse.TPointer;

import java.util.*;
import java.util.function.Consumer;

/**
 * Непосредственная интерпретация AST.
 *
 * <p>
 * Интерпретация AST узлов напряму зависит от контекста ({@link EvalContext}).
 * </p>
 */
public class Eval {
    //region create and configure

    /**
     * Конструктор по умолчанию
     */
    public Eval(){
        context = new EvalContext();
    }

    /**
     * Конструктор
     * @param ctx начальный контекст или null
     */
    public Eval(EvalContext ctx){
        if( ctx==null )ctx = new EvalContext();
        context = ctx;
    }

    /**
     * Конфигурация экземпляра
     * @param conf конфигурация
     * @return self ссылка
     */
    public Eval configure( Consumer<Eval> conf ){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }
    //endregion

    //region context
    /**
     * контекст интерпретации
     */
    protected final EvalContext context;

    /**
     * Возвращает контекс интерпретации
     * @return контекст интерпретации
     */
    public EvalContext context(){ return context; }
    //endregion

    //region Парсинг и интерпретация
    public static class Builder {
        protected String source;
        protected int from;
        protected EvalContext initialContext;
        protected Consumer<EvalContext> contextConfigure;

        public Builder( String source, int from ){
            if( source==null )throw new IllegalArgumentException( "source==null" );
            if( from<0 )throw new IllegalArgumentException( "from<0" );
            this.source = source;
            this.from = from;
        }

        public Builder( String source ){
            if( source==null )throw new IllegalArgumentException( "source==null" );
            this.source = source;
            this.from = 0;
        }

        public Builder context( EvalContext initialContext ){
            this.initialContext = initialContext;
            return this;
        }

        public Builder context( Consumer<EvalContext> conf ){
            this.contextConfigure = conf;
            return this;
        }

        public AST parse(){
            TPointer ptr = Parser.source(source,from);

            Optional<AST> astRoot = Parser.expression.apply( ptr );
            if( astRoot==null || !astRoot.isPresent() ){
                throw new ParseError("can't parse source, offset="+from+" source:\n"+source);
            }

            return astRoot.get();
        }

        protected Eval createEval(){
            return new Eval(initialContext);
        };

        public Object eval(){
            AST astRoot = parse();

            Eval eval = createEval();
            if( contextConfigure!=null ){
                contextConfigure.accept(eval.context());
            }

            return eval.eval(astRoot);
        }
    }

    public static Builder parse( String source, int from ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        if( from<0 )throw new IllegalArgumentException( "from<0" );
        return new Builder(source,from);
    }

    public static Builder parse( String source ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        return new Builder(source,0);
    }
    //endregion

    //region Интерпретация AST узлов
    /**
     * Интерпретация ast дерева
     * @param ast дерево
     * @return результат
     */
    public Object eval( AST ast ){
        if( ast==null )throw new IllegalArgumentException( "ast==null" );
        if( ast instanceof UnaryOpAST ){
            return operator( (UnaryOpAST)ast );
        }else if( ast instanceof BinaryOpAST ){
            return operator( (BinaryOpAST) ast );
        }else if( ast instanceof NumberAST ){
            return context.number( (NumberAST)ast );
        }else if( ast instanceof BooleanAST ){
            return context.bool( (BooleanAST)ast );
        }else if( ast instanceof StringAST ){
            return context.string( (StringAST)ast );
        }else if( ast instanceof NullAST ){
            return context.nullLiteral( (NullAST) ast );
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

    /**
     * Интерпретация унарного оператора
     * @param op AST узел,
     *           см {@link xyz.cofe.cxel.Parser#unaryExression},
     * @return результат интерпретации, см {@link EvalContext#call}
     */
    protected Object operator( UnaryOpAST op ){
        Object v = eval(op.operand());
        return context.call(null,op.opText(), Collections.singletonList(v));
    }

    /**
     * Интерпретация бинарного оператора
     * @param op AST узел,
     *           см {@link xyz.cofe.cxel.Parser#mulDiv},
     *           {@link xyz.cofe.cxel.Parser#plusMinus},
     *           {@link xyz.cofe.cxel.Parser#and},
     *           {@link xyz.cofe.cxel.Parser#or},
     *           {@link xyz.cofe.cxel.Parser#compare},
     * @return результат интерпретации, см {@link EvalContext#call}
     */
    protected Object operator( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());
        return context.call(null,op.opText(), Arrays.asList(vLeft,vRight));
    }

    /**
     * Интерпретация чтения переменной
     * @param ast AST узел, см {@link xyz.cofe.cxel.Parser#varRef}
     * @return значение переменной, см {@link EvalContext#read}
     */
    protected Object variable( VarRefAST ast ){
        return context.read(ast.variable());
    }

    /**
     * Интерпретация чтения свойства объекта
     * @param ast AST узел, см {@link xyz.cofe.cxel.Parser#postfix}
     * @return значение свойства, см {@link EvalContext#get}
     */
    protected Object property( PropertyAST ast ){
        Object obj = eval(ast.base());
        return context.get(obj,ast.property());
    }

    /**
     * Интерпретация вызова метода
     * @param ast AST узел, см {@link xyz.cofe.cxel.Parser#postfix}
     * @return результат вызова, см {@link EvalContext#call}
     */
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

    /**
     * Интерпретация доступа к элементу массива / списка.
     * @param ast AST узел доступа к элементу, см {@link xyz.cofe.cxel.Parser#postfix}
     * @return значение см {@link EvalContext#getAt}
     */
    protected Object index( IndexAST ast ){
        Object obj = eval(ast.base());
        Object idx = eval(ast.index());
        return context.getAt(obj,idx);
    }
    //endregion
}
