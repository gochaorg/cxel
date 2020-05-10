package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.Keyword;

public class ParenthesAST extends ASTBase<ParenthesAST> {
    public ParenthesAST( KeywordAST left, AST expression, KeywordAST right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( expression==null )throw new IllegalArgumentException("expression==null");
        begin = left.begin();
        end = right.end();

        this.expression = expression;
        this.left = left;
        this.right = right;
    }

    protected KeywordAST left;
    public KeywordAST left(){ return left; }
    public ParenthesAST left(KeywordAST kw){
        if( kw==null )throw new IllegalArgumentException("kw==null");
        return cloneAndConf(c->c.left = kw);
    }

    protected KeywordAST right;
    public KeywordAST right(){ return right; }
    public ParenthesAST right(KeywordAST kw){
        if( kw==null )throw new IllegalArgumentException("kw==null");
        return cloneAndConf(c->c.right = kw);
    }

    protected AST expression;
    public AST expression(){ return expression; }
    public ParenthesAST expression(AST expr){
        if( expr==null )throw new IllegalArgumentException("expr==null");
        return cloneAndConf(c->c.expression = expr);
    }

    @Override
    public ParenthesAST clone(){
        return new ParenthesAST( left, expression, right );
    }

    @Override
    public AST[] children(){
        return children(expression);
    }
}
