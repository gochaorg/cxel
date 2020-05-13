package xyz.cofe.cxel.parse;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.ast.BinaryOpAST;
import xyz.cofe.cxel.ast.StringAST;
import xyz.cofe.cxel.tok.StringTok;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

import static xyz.cofe.cxel.parse.BaseParser.atomic;

public interface AddSub extends Atomic, Expression {
    public default GR<TPointer, AST> addSub(){
        GR<TPointer, AST> rule =
            atmoic().next(
                Keyword.parserOf(Keyword.Plus, Keyword.Minus)
            ).next(expression())
                .map(BinaryOpAST::new);

        return cache( "addSub", ()-> {
            GR<TPointer, AST> r1 =
                atmoic().next(
                    Keyword.parserOf(Keyword.Plus, Keyword.Minus)
                ).next(expression())
                    .map(BinaryOpAST::new);

            //GR<TPointer, AST> r2 = r1.another(atmoic()).map( t->(AST)t );
            //GR<TPointer, AST> rule = r2;

            return r1;
        });
    }
}
