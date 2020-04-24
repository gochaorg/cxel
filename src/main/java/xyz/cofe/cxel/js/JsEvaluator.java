package xyz.cofe.cxel.js;

import xyz.cofe.cxel.Lexer;
import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.Parser;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.TPointer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Парсинг js выражений
 */
public class Evaluator {
    /**
     * Конфигурация
     * @param conf конфигурация
     * @return SELF ссылка
     */
    public Evaluator configure( Consumer<Evaluator> conf){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        conf.accept(this);
        return this;
    }

    //region лексический анализатор
    /**
     * Замена всех {@link xyz.cofe.cxel.tok.IntegerNumberTok} на {@link ForcedFloatNumberTok}
     * @param toks токены/лексемы
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void hookTokens( List toks ){
        if( toks==null )return;
        for( int i=0; i<toks.size(); i++ ){
            Object tok = toks.get(i);
            if( tok!=null && tok.getClass()==IntegerNumberTok.class ){
                IntegerNumberTok itok = (IntegerNumberTok)tok;
                toks.set(i, new ForcedFloatNumberTok(itok));
            }
        }
    }

    protected Lexer lexer;

    /**
     * Лексер / Лексический анализ
     * @return лексический анализатор
     */
    public Lexer lexer(){
        if( lexer!=null )return lexer;
        lexer = new Lexer(){
            @Override
            public List<? extends CToken> tokens( String source ){
                List<? extends CToken> toks = super.tokens(source);
                hookTokens(toks);
                return toks;
            }

            @Override
            public List<? extends CToken> tokens( String source, int from ){
                List<? extends CToken> toks = super.tokens(source, from);
                hookTokens(toks);
                return toks;
            }

            @Override
            public List<? extends CToken> tokens( String source, int from, Predicate<CToken> filter ){
                List<? extends CToken> toks = super.tokens(source, from, filter);
                hookTokens(toks);
                return toks;
            }
        };
        return lexer;
    }
    //endregion

    protected Parser parser;

    /**
     * Парсер на лексемы
     * @return
     */
    public Parser parser(){
        if( parser!=null )return parser;
        parser = new Parser();
        return parser;
    }

    /**
     * Строит указатель по лексемам
     * @param source исходный код
     * @param from с какой позиции (индекса) начать анализ
     * @return указатель на лексемы
     */
    public TPointer tpointer(String source, int from){
        if( source==null )throw new IllegalArgumentException("source==null");
        if( from<0 )throw new IllegalArgumentException("from<0");
        return new TPointer( lexer().tokens(source,from) );
    }

    public AST parse(String source, int from){
        Optional<AST> ast = parser().expression().apply(tpointer(source,from));
        if( ast==null || !ast.isPresent() ){
            throw new ParseError("can't parse source, offset="+from+" source:\n"+source);
        }
        return ast.get();
    }
    public AST parse(String source){
        if( source==null )throw new IllegalArgumentException("source==null");
        return parse(source,0);
    }

    protected EvalContext context;
    public EvalContext context(){
        if( context!=null )return context;
        context = new EvalContext();
        return context;
    }
    public Evaluator context( Consumer<EvalContext> conf ){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        conf.accept(context());
        return this;
    }

    public Object eval( AST ast ){
        if( ast==null )throw new IllegalArgumentException("ast==null");
        Eval eval = new Eval(context());
        return eval.eval(ast);
    }

    public Object eval( String source ){
        if( source==null )throw new IllegalArgumentException("source==null");
        return eval( parse(source,0) );
    }
}
