package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Вызов метода/функции/лямбды
 */
public class CallAST extends ASTBase<CallAST> {
    protected CallAST(){
        super();
    }

    protected CallAST( CallAST sample ){
        super(sample);

        List<AST> lst = new ArrayList<>(sample.args);
        this.args = Collections.unmodifiableList(lst);

        this.base = sample.base;
    }

    public CallAST( TPointer begin, TPointer end, AST base, List<AST> args ){
        super(begin, end);
        if( base==null )throw new IllegalArgumentException( "base==null" );
        if( args==null )throw new IllegalArgumentException( "args==null" );
        for( AST a : args ){
            if( a==null )throw new IllegalArgumentException("args contains null");
        }

        List<AST> lst = new ArrayList<>(args.size());
        lst.addAll(args);
        lst = Collections.unmodifiableList(lst);
        this.args = lst;

        this.base = base;
    }

    @Override
    public CallAST clone(){
        return new CallAST(this);
    }

    protected List<AST> args;
    public List<AST> args(){ return args; }

    protected AST base;
    public AST base(){ return base; }

    @Override
    public AST[] children(){
        List<AST> lst = new ArrayList<>();
        lst.add(base);
        lst.addAll(args);
        return lst.toArray(new AST[0]);
    }
}
