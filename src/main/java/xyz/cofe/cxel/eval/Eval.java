package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.eval.op.*;
import xyz.cofe.text.tparse.GR;
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
        }else if( ast instanceof ListAST ){
            return list( (ListAST)ast );
        }else if( ast instanceof MapAST ){
            return map( (MapAST)ast );
        }else if( ast instanceof IfAST ){
            return If( (IfAST)ast );
        }else if( ast instanceof ParenthesAST ){
            return parenthes( (ParenthesAST)ast );
        }
        throw new EvalError("can't evaluate undefined ast: "+ast.getClass());
    }

    protected Object parenthes( ParenthesAST ast ){
        return eval( ast.expression() );
    }

    /**
     * Интерпретация унарного оператора
     * @param op AST узел
     * @return результат интерпретации, см {@link EvalContext#call}
     */
    protected Object operator( UnaryOpAST op ){
        Object v = eval(op.operand());
        return context.call( op.opText(), Collections.singletonList(v));
    }

    /**
     * Интерпретация бинарного оператора
     * @param op AST узел,
     * @return результат интерпретации, см {@link EvalContext#call}
     */
    protected Object operator( BinaryOpAST op ){
        Object vLeft = eval(op.left());
        Object vRight = eval(op.right());
        return context.call(op.opText(), Arrays.asList(vLeft,vRight));
    }

    /**
     * Интерпретация чтения переменной
     * @param ast AST узел
     * @return значение переменной, см {@link EvalContext#read}
     */
    protected Object variable( VarRefAST ast ){
        return context.read(ast.variable());
    }

    /**
     * Интерпретация чтения свойства объекта
     * @param ast AST узел
     * @return значение свойства, см {@link EvalContext#get}
     */
    protected Object property( PropertyAST ast ){
        Object obj = eval(ast.base());
        return context.get(obj,ast.property());
    }

    /**
     * Интерпретация вызова метода
     * @param ast AST узел
     * @return результат вызова, см {@link EvalContext#call}
     */
    protected Object call( CallAST ast ){
        AST base = ast.base();
        if( base instanceof VarRefAST ){
            List<Object> args = new ArrayList<>();
            for( AST arg : ast.args() ){
                args.add( eval(arg) );
            }

            return context.call( ((VarRefAST) base).variable(), args );
        }else if( base instanceof PropertyAST ){
            PropertyAST past = (PropertyAST)base;
            String propName = past.property();

            Object obj = eval(past.base());

            List<Object> args = new ArrayList<>();
            args.add(obj);
            for( AST arg : ast.args() ){
                args.add( eval(arg) );
            }

            return context.call(propName,args);
        }
        return null;
    }

    /**
     * Интерпретация доступа к элементу массива / списка.
     * @param ast AST узел доступа к элементу
     * @return значение см {@link EvalContext#getAt}
     */
    protected Object index( IndexAST ast ){
        Object obj = eval(ast.base());
        Object idx = eval(ast.index());
        return context.getAt(obj,idx);
    }

    /**
     * Интерпретация создания списка
     * @param listAst констукция списка
     * @return список
     */
    protected Object list( ListAST listAst ){
        AST[] avalues = listAst.expressions();
        Object[] values = null;
        if( avalues!=null ){
            values = new Object[avalues.length];
            for( int ai=0; ai<avalues.length; ai++ ){
                values[ai] = eval(avalues[ai]);
            }
            return context.list(values);
        }else{
            values = new Object[0];
            return context.list(values);
        }
    }

    /**
     * Интерпретация создания карты
     * @param mast конструкция карты
     * @return карта
     */
    @SuppressWarnings("rawtypes")
    protected Object map(MapAST mast){
        EvalContext.MapBuilder bld = context.map();
        for( MapEntryAST e : mast.entries() ){
            Object key = null;
            Object val = null;
            if( e instanceof MapExprEntreyAST ){
                MapExprEntreyAST me = (MapExprEntreyAST)e;
                key = eval(me.key());
                val = eval(me.value());
            }else if( e instanceof MapLiteralEntreyAST ){
                MapLiteralEntreyAST me = (MapLiteralEntreyAST)e;
                key = me.key().value();
                val = eval(me.value());
            }else if( e instanceof MapPropEntreyAST ){
                MapPropEntreyAST me = (MapPropEntreyAST)e;
                key = me.key().variable();
                val = eval(me.value());
            }else{
                throw new EvalError("can't evaluate map entry of "+e);
            }
            bld = bld.put(key,val);
        }
        return bld.build();
    }

    private Object If(IfAST ifAST){
        boolean b = context.condition(eval(ifAST.condition()));
        if( b ){
            return eval(ifAST.success());
        }else{
            return eval(ifAST.failure());
        }
    }
    //endregion
}
