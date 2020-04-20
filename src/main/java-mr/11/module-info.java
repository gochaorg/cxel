open module xyz.cofe.cxel {
    requires java.base;
    requires transitive java.logging;
    requires transitive xyz.cofe.ecolls;
    requires transitive xyz.cofe.xparse;
    requires transitive java.desktop;
    exports xyz.cofe.cxel;
    exports xyz.cofe.cxel.ast;
    exports xyz.cofe.cxel.eval;
    exports xyz.cofe.cxel.eval.op;
    exports xyz.cofe.cxel.eval.score;
    exports xyz.cofe.cxel.tok;
}