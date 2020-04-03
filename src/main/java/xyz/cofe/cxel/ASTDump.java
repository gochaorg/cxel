package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.*;

import java.util.function.Consumer;

/**
 * Дамп AST
 */
public class ASTDump {
    /**
     * Создание дампа
     * @return создание дампа
     */
    public static TDump<AST> build(){
        return new TDump<AST>().configure( c -> {
            c.setFollow(AST::nodes);
            c.decode(BinaryOpAST.class, BinaryOpAST::opText);
            c.decode(UnaryOpAST.class, UnaryOpAST::opText);
            c.decode(NumberAST.class, n -> n.value().toString());
            c.decode(NullAST.class, n -> "null");
            c.decode(BooleanAST.class, n -> n.value().toString());
        } );
    }

    /**
     * Создание дампа
     * @param conf конфигурация
     * @return создание дампа
     */
    public static TDump<AST> build( Consumer<TDump<AST>> conf ){
        TDump<AST> d = build();
        if( conf!=null ) d = d.configure(conf);
        return d;
    }
}
