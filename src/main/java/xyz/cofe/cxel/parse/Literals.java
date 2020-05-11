package xyz.cofe.cxel.parse;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.tok.FloatNumberTok;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.cxel.tok.StringTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

import java.util.Optional;

import static xyz.cofe.cxel.parse.BaseParser.atomic;

/**
 * Литеральные конструкции
 */
public interface Literals extends GRCache {
    //region number : GR
    /**
     * Парсинг целого числа
     */
    public default GR<TPointer, AST> intNumber(){
        return cache( "intNumber", ()-> atomic( IntegerNumberTok.class, NumberAST::new ) );
    }

    /**
     * Парсинг плавующего числа
     */
    public default GR<TPointer, AST> floatNumber(){
        return cache( "floatNumber", ()-> atomic( FloatNumberTok.class, NumberAST::new ) );
    }

    /**
     * Парсинг числа
     */
    public default GR<TPointer, AST> number(){
        return cache( "number", ()-> floatNumber().another(intNumber()).map( t->(AST)t ) );
    }
    //endregion

    //region bool : GR
    /**
     * Парсинг булево
     */
    public default GR<TPointer, AST> bool() {
        return cache( "bool", ()-> ptr -> {
            Optional<CToken> tok = ptr.lookup(0);
            if (tok.isPresent() && tok.get() instanceof KeywordTok ) {
                Keyword k = ((KeywordTok) tok.get()).keyword();
                if (k != null) {
                    if (k == Keyword.True || k == Keyword.False) {
                        return Optional.of(new BooleanAST(ptr, (KeywordTok) tok.get()));
                    }
                }
            }
            return Optional.empty();
        } );
    }
    //endregion

    //region null : GR
    /**
     * Парсинг null значение
     */
    public default GR<TPointer, AST> nullConst(){
        return cache( "nullConst", ()-> ptr -> {
            Optional<CToken> tok = ptr.lookup(0);
            if( tok.isPresent() && tok.get() instanceof KeywordTok ){
                Keyword k = ((KeywordTok)tok.get()).keyword();
                if( k!=null ){
                    if( k==Keyword.Null ){
                        return Optional.of( new NullAST(ptr, (KeywordTok)tok.get()) );
                    }
                }
            }
            return Optional.empty();
        } );
    }
    //endregion

    public default GR<TPointer,AST> string(){
        return cache( "string", ()->atomic(StringTok.class, StringAST::new) );
    }

    public default GR<TPointer, AST> literal(){
        return cache("literal", ()->
            number()
                .another(bool())
                .another(nullConst())
                .another(string())
                .map()
        );
    }
}
