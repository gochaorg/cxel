package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

/**
 * Условный оператор (тренарный)
 */
public class IfAST extends ASTBase<IfAST> {
    protected IfAST() {
        super();
    }

    protected IfAST(IfAST sample) {
        super(sample);
        condition = sample.condition;
        success = sample.success;
        failure = sample.failure;
    }

    public IfAST(TPointer begin, TPointer end, AST condition, AST success, AST failure) {
        super(begin, end);
        if( condition==null )throw new IllegalArgumentException("condition==null");
        if( success==null )throw new IllegalArgumentException("success==null");
        if( failure==null )throw new IllegalArgumentException("failure==null");
        this.condition = condition;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public IfAST clone() {
        return new IfAST(this);
    }

    @Override
    public AST[] children() {
        return children(condition,success,failure);
    }

    protected AST condition;
    public AST condition(){ return condition; }
    public IfAST condition( AST newCondition ){
        if( newCondition==null )throw new IllegalArgumentException("newCondition==null");
        return cloneAndConf( c->c.condition = newCondition );
    }

    protected AST success;
    public AST success(){ return success; }
    public IfAST success( AST newSuccess ){
        if( newSuccess==null )throw new IllegalArgumentException("newSuccess==null");
        return cloneAndConf( c->c.success = newSuccess );
    }

    protected AST failure;
    public AST failure(){ return failure; }
    public IfAST failure( AST newFailure ){
        if( newFailure==null )throw new IllegalArgumentException("newFailure==null");
        return cloneAndConf( c->c.failure = newFailure );
    }
}
