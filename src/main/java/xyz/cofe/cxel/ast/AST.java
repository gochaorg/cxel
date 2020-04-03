package xyz.cofe.cxel.ast;

import xyz.cofe.collection.ImTree;
import xyz.cofe.collection.ImTreeWalk;
import xyz.cofe.iter.Eterable;
import xyz.cofe.text.tparse.TPointer;
import xyz.cofe.text.tparse.Tok;

/**
 * Описывает узел AST
 */
public interface AST extends Tok<TPointer>, ImTree<AST>, ImTreeWalk<AST> {
    /**
     * Возвращает дочерние узлы
     * @return дочерние узлы
     */
    AST[] children();

    @Override
    default Eterable<AST> nodes(){ return Eterable.of(children()); }
}
