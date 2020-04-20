package xyz.cofe.cxel.js;

import xyz.cofe.cxel.Lexer;
import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.Parser;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.tok.WhiteSpaceTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.TPointer;
import xyz.cofe.text.tparse.Tokenizer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JsEval {
    public JsEval configure(Consumer<JsEval> conf){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        conf.accept(this);
        return this;
    }

    protected Lexer lexer;
    public Lexer lexer(){
        if( lexer!=null )return lexer;
        lexer = new Lexer();
        return lexer;
    }

    protected Parser parser;
    public Parser parser(){
        if( parser!=null )return parser;
        parser = new Parser();
        return parser;
    }

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
    public JsEval context( Consumer<EvalContext> conf ){
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
