package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

public class MapExprEntreyAST extends MapEntryAST<AST, MapExprEntreyAST> {
    protected MapExprEntreyAST() {
        super();
    }

    protected MapExprEntreyAST(MapExprEntreyAST sample) {
        super(sample);
    }

    public MapExprEntreyAST(TPointer begin, TPointer end, AST key, AST value) {
        super(begin, end, key, value);
    }

    @Override
    public MapExprEntreyAST clone() {
        return new MapExprEntreyAST(this);
    }
}
