package xyz.cofe.cxel.js;

import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ASTDump;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.tok.NumberTok;
import xyz.cofe.text.tparse.CToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("rawtypes")
public class JsEvalTest {
    //region EvalTester
    @SuppressWarnings("UnusedReturnValue")
    public static class EvalTester {
        //region construct
        public EvalTester(){
        }
        @SuppressWarnings("CopyConstructorMissesField")
        public EvalTester( EvalTester sample){
            if( sample==null )throw new IllegalArgumentException("sample==null");
            source = sample.source;
            tokens = sample.tokens!=null ? new ArrayList<>(sample.tokens) : null;
            ast = sample.ast;
            logTokens = sample.logTokens;
            logAst = sample.logAst;
            logEval = sample.logEval;
            logPostRun = sample.logPostRun;
            expected.addAll( sample.expected );
        }
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public EvalTester clone(){
            return new EvalTester(this);
        }
        public EvalTester cloneAndConf( Consumer<EvalTester> conf ){
            EvalTester t = clone();
            if( conf!=null ){
                conf.accept(t);
            }
            return t;
        }
        //endregion

        //region source : String
        protected String source;
        public String source(){
            return source;
        }
        public EvalTester source( String src ){
            return cloneAndConf( c->c.source = src );
        }
        //endregion

        //region logTokens - логирование лексем
        protected BiConsumer<String, List<? extends CToken>> logTokens;
        public BiConsumer<String, List<? extends CToken>> logTokens(){
            return logTokens;
        }
        public EvalTester logTokens(BiConsumer<String, List<? extends CToken>> logger){
            return cloneAndConf( c->c.logTokens = logger );
        }
        public void logTokens( String src, List<? extends CToken> toks ){
            if( logTokens!=null ){
                logTokens.accept(src,toks);
            }
        }
        //endregion
        //region logAst - логирование ast
        protected BiConsumer<List<? extends CToken>,AST> logAst;
        public BiConsumer<List<? extends CToken>,AST> logAst(){
            return logAst;
        }
        public EvalTester logAst(BiConsumer<List<? extends CToken>,AST> logger){
            return cloneAndConf( c->c.logAst = logger );
        }
        public void logAst( List<? extends CToken> toks, AST ast ){
            if( logAst!=null ){
                logAst.accept(toks,ast);
            }
        }
        //endregion
        //region logEval - логирование результата интерпретации
        protected BiConsumer<AST, Object> logEval;
        public BiConsumer<AST, Object> logEval(){
            return logEval;
        }
        public EvalTester logEval(BiConsumer<AST, Object> logger){
            return cloneAndConf( c->c.logEval = logger );
        }
        public void logEval( AST ast, Object res ){
            if( logEval!=null ){
                logEval.accept(ast, res);
            }
        }
        //endregion
        //region logPostRun - логирование по завершению run
        protected Runnable logPostRun;
        public Runnable logPostRun(){
            return logPostRun;
        }
        public EvalTester logPostRun(Runnable logger){
            return cloneAndConf( c->c.logPostRun = logger );
        }
        protected void logPostRun0(){
            if( logPostRun!=null ){
                logPostRun.run();
            }
        }
        //endregion

        //region evaluator
        protected JsEvaluator evaluator;
        public JsEvaluator evaluator(){
            if( evaluator!=null )return evaluator;
            evaluator = new JsEvaluator();
            return evaluator;
        }
        //endregion
        //region tokens
        protected List<? extends CToken> tokens;
        public List<? extends CToken> tokens(){
            if( tokens!=null )return tokens;
            if( source==null )throw new IllegalArgumentException("source not defined");
            tokens = evaluator().tokens(source,0);
            logTokens(source, tokens);
            return tokens;
        }
        //endregion
        //region ast
        protected AST ast;
        public AST ast(){
            if( ast!=null )return ast;
            List<? extends CToken> toks = tokens();
            ast = evaluator().parse(toks);
            logAst(toks, ast);
            return ast;
        }
        //endregion

        //region context
        public EvalContext context(){
            return evaluator().context();
        }
        public EvalTester context( Consumer<EvalContext> conf ){
            if( conf!=null )throw new IllegalArgumentException("conf!=null");
            return cloneAndConf( c->c.context(conf) );
        }
        //endregion

        public Object eval(){
            AST ast = ast();
            Object res = evaluator().eval(ast);
            logEval(ast, res);
            return res;
        }

        protected final List<Consumer<?>> expected = new ArrayList<>();
        @SuppressWarnings("SimplifiableJUnitAssertion")
        public EvalTester expected( Object expectedValue ){
            return cloneAndConf( c->{
                c.expected.add( val->assertTrue(Objects.equals(val, expectedValue)) );
            });
        }
        public EvalTester expected( Consumer<Object> testFun ){
            if( testFun==null )throw new IllegalArgumentException("testFun==null");
            return cloneAndConf( c->c.expected.add(testFun) );
        }
        public EvalTester expected( Function<Object,Boolean> testFun ){
            if( testFun==null )throw new IllegalArgumentException("testFun==null");
            return cloneAndConf( c->c.expected.add(
                v->{
                    assertTrue( testFun.apply(v) );
                }
            ));
        }

        @SuppressWarnings("unchecked")
        public Object run(){
            if( expected.isEmpty() ){
                System.out.println("expected nothing");
                return null;
            }

            Object result = eval();
            for( Consumer exp : expected ){
                exp.accept(result);
            }

            logPostRun0();
            return result;
        }
    }

    public static EvalTester parse(String source){
        if( source==null )throw new IllegalArgumentException("source==null");
        return
            new EvalTester()
            .source(source)
            .logTokens( (src,toks)->{
                System.out.println("== :source: ==");
                System.out.println(source);
                System.out.println("-- tokens ----------");
                toks.forEach(System.out::println);
            })
            .logAst((toks,ast)->{
                System.out.println("== :parsed ast: ==");
                ASTDump.build().dump(ast);
            })
            .logEval((ast,res)->{
                System.out.println("== :eval result: ==");
                System.out.println("type="+(res!=null ? res.getClass() : "null"));
                System.out.println("value:");
                System.out.println(res);
            })
            .logPostRun(()->{
                System.out.println();
                System.out.println();
            });
    }
    //endregion

    @Test
    public void allNumLitteralIsDouble(){
        System.out.println("=== test01() ===");

        EvalTester tester = parse("10 + 1");
        List<Class<?>> numLiteralTypes = new ArrayList<>();
        tester.tokens().forEach(t->{
            if( t instanceof NumberTok ){
                NumberTok n = (NumberTok)t;
                System.out.println(t+" type="+(n.value()!=null ? n.value().getClass() : "null"));
                if( n.value()!=null )numLiteralTypes.add(n.value().getClass());
            }else{
                System.out.println(t);
            }
        });

        long nonDoubleCount = numLiteralTypes.stream().filter( c -> !(c==Double.class || c==double.class) ).count();
        assertTrue(nonDoubleCount<1);

        tester.ast();
    }

    /**
     * Литералы в js
     * <br>
     *
     * <a href="https://developer.mozilla.org/ru/docs/Web/JavaScript/Guide/Grammar_and_types">Согласно mozilla есть</a>
     *
     * <ul>
     *     <li>Шесть типов данных,  которые являются примитивами:
     *     <ul>
     *         <li>
     *             Boolean. true и false.
     *         </li><li>
     *             null. Специальное ключевое слово, обозначающее нулевое или «пустое» значение. Поскольку JavaScript чувствителен к регистру, null не то же самое, что Null, NULL или любой другой вариант.
     *         </li><li>
     *             undefined. Свойство глобального объекта; переменная, не имеющая присвоенного значения, обладает типом undefined.
     *         </li><li>
     *             Number. 42 или 3.14159.
     *         </li><li>
     *             String. "Howdy".
     *         </li><li>
     *             Symbol (ECMAScript 6)
     *         </li>
     *     </ul>
     *     </li>
     *     <li>
     *         и Object
     *     </li>
     * </ul>
     */
    @Test
    public void literals(){
        System.out.println("==== literals() ====");
        parse("10").expected(10.0d).run();
        parse("10.0").expected(10.0d).run();
        parse("0.10").expected(0.1d).run();
        parse("true").expected(true).run();
        parse("false").expected(false).run();
        parse("null").expected(Objects::isNull).run();
        parse("undefined").expected(Undef.instance).run();
        parse("'single quoted string'").expected("single quoted string").run();
        parse("\"double quoted string\"").expected("double quoted string").run();

        parse("'escape 1\\nline 2'").expected("escape 1\nline 2").run();
        parse("'escape 2\\rline 2'").expected("escape 2\rline 2").run();
        parse("'escape 2\\bline 2'").expected("escape 2\bline 2").run();
        parse("'escape 2\\fline 2'").expected("escape 2\fline 2").run();
        parse("'escape 2\\tline 2'").expected("escape 2\tline 2").run();
        parse("'escape 2\\vline 2'").expected("escape 2\u000bline 2").run();
        parse("'escape 2\\\\x\\'\\\"'").expected("escape 2\\x'\"").run();
        parse("'oct \\043'").expected("oct #").run();
        parse("'hex \\x23'").expected("hex #").run();
        parse("'unicode \\u0023'").expected("unicode #").run();
    }

    @Test
    public void booleanOp(){
        System.out.println("==== booleanOp() ====");
        parse("true || true").eval();
        parse("true || 1").eval();
        parse("1 || true").eval();
        parse("{} || true").eval();
    }

    @Test
    public void mathOp(){
        System.out.println("==== mathOp() ====");
        parse("1 + 2 * 3 / 4 - 5").eval();
        parse("(1 + 2) * 3 / 4 - 5").eval();
    }
}
