package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

/**
 * Доступ к элементу массива
 */
public class IndexAST extends ASTBase<IndexAST> {
    protected IndexAST(){
        super();
    }

    protected IndexAST( IndexAST sample ){
        super(sample);
        base = sample.index;
        index = sample.index;
    }

    public IndexAST( TPointer begin, TPointer end, AST base, AST index ){
        super(begin, end);
        if( base==null )throw new IllegalArgumentException( "base==null" );
        if( index==null )throw new IllegalArgumentException( "index==null" );
        this.base = base;
        this.index = index;
    }

    @Override
    public IndexAST clone(){
        return new IndexAST(this);
    }

    protected AST base;
    public AST base(){ return base; }

    protected AST index;
    public AST index(){ return index; }

    @Override
    public AST[] children(){
        return children(base, index);
    }
}
