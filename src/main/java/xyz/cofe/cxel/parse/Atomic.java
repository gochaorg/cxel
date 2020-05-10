package xyz.cofe.cxel.parse;

import xyz.cofe.cxel.ast.AST;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

public interface Atomic extends GRCache {
    GR<TPointer, AST> atmoic();
}
