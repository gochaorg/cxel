package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

public class MapPropEntreyAST extends MapEntryAST<VarRefAST,MapPropEntreyAST> {
    protected MapPropEntreyAST() {
        super();
    }

    protected MapPropEntreyAST(MapPropEntreyAST sample) {
        super(sample);
    }

    public MapPropEntreyAST(TPointer begin, TPointer end, VarRefAST propertyId, AST value) {
        super(begin, end, propertyId, value);
    }

    @Override
    public MapPropEntreyAST clone() {
        return new MapPropEntreyAST(this);
    }
}
