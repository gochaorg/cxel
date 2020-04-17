package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

public class MapLiteralEntreyAST extends MapEntryAST<LiteralAST, MapLiteralEntreyAST> {
    protected MapLiteralEntreyAST() {
        super();
    }

    protected MapLiteralEntreyAST(MapLiteralEntreyAST sample) {
        super(sample);
    }

    public MapLiteralEntreyAST(TPointer begin, TPointer end, LiteralAST key, AST value) {
        super(begin, end, key, value);
    }

    @Override
    public MapLiteralEntreyAST clone() {
        return new MapLiteralEntreyAST(this);
    }
}
