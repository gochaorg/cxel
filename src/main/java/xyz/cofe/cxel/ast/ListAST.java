package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

import java.util.ArrayList;
import java.util.List;

/**
 * Создание списка
 */
public class ListAST extends ASTBase<ListAST> {
    protected ListAST() {
        super();
        expressions = new AST[0];
    }

    protected ListAST(ListAST sample) {
        super(sample);
        if( sample!=null ){
            expressions = sample.expressions;
        }
    }

    public ListAST(TPointer begin, TPointer end) {
        super(begin, end);
        expressions = new AST[0];
    }

    public ListAST(TPointer begin, TPointer end, AST[] expressions) {
        super(begin, end);
        if( expressions==null )throw new IllegalArgumentException("expressions==null");
        for( int i=0;i<expressions.length;i++ ){
            if( expressions[i]==null )throw new IllegalArgumentException("expressions["+i+"]==null");
        }
        this.expressions = expressions;
    }

    public ListAST(TPointer begin, TPointer end, Iterable<AST> expressions) {
        super(begin, end);
        if( expressions==null )throw new IllegalArgumentException("expressions==null");

        List<AST> lst = new ArrayList<>();
        int idx = -1;
        for (AST ast : expressions){
            idx++;
            if( ast==null )throw new IllegalArgumentException("expressions["+idx+"]==null");
            lst.add(ast);
        }
        this.expressions = lst.toArray(new AST[0]);
    }

    @Override
    public ListAST clone() {
        return new ListAST(this);
    }

    @Override
    public AST[] children() {
        return expressions;
    }

    protected AST[] expressions;
    public AST[] expressions() { return expressions; }
    public ListAST expressions(AST[] exprss) {
        if( exprss==null )throw new IllegalArgumentException("exprss==null");
        return cloneAndConf(c->c.expressions = exprss);
    }
}
