package xyz.cofe.cxel;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.fn.Pair;
import xyz.cofe.iter.Eterable;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.TPointer;
import xyz.cofe.text.tparse.Tokenizer;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ParserTest {
    public static class Parsing {
        protected String source;
        public Parsing( String source ){
            this.source = source;
        }

        protected boolean eval=false;
        public Parsing eval( boolean eval ){
            this.eval = eval;
            return this;
        }
        public Parsing eval(){
            return eval( true );
        }

        protected boolean checkExpected=false;
        protected Object expected = null;
        public Parsing expected( Object val ){
            this.eval = true;
            this.expected = val;
            this.checkExpected = true;
            return this;
        }

        public Parsing expected( boolean checkExpected, Object val ){
            this.expected = val;
            this.checkExpected = checkExpected;
            return this;
        }

        protected Consumer<EvalContext> contextConfigure;
        public Parsing context( Consumer<EvalContext> conf ){
            contextConfigure = conf;
            return this;
        }

        public Object run(){
            TPointer psource = Parser.source(source);

            System.out.println("--- tokens ---");
            psource.tokens().forEach(System.out::println);

            Optional<? extends AST> astRoot
                = Parser.expression.apply( psource );

            Assertions.assertTrue(astRoot!=null);
            Assertions.assertTrue(astRoot.isPresent());

            System.out.println("--- ast ---");
            ASTDump.build().dump( astRoot.get() );

            if( eval ){
                Eval ev = new Eval();
                if( contextConfigure!=null ){
                    contextConfigure.accept(ev.context());
                }

                Object evRes = ev.eval(astRoot.get());
                System.out.println("eval result: " + evRes);

                if( checkExpected ){
                    System.out.println("expected: "+expected);
                    assertTrue(Objects.equals(expected, evRes));
                }

                return evRes;
            }

            return null;
        }
    }
    public static Parsing parse( String source ){
        return new Parsing(source);
    }

    public void tryParse( String source, boolean eval ){
        tryParse(source,eval,false,null);
    }
    public void tryParse( String source, boolean eval, boolean checkExpected, Object expected ){
        parse(source).eval(eval).expected(checkExpected,expected).run();
    }

    @Test
    public void test01(){
        tryParse("1 + 2 + 3",true, true, 6);
        tryParse( "1 * 2 + 3 - 4 / 5", false);
    }

    @Test
    public void test02(){
        TPointer psource = Parser.source("-1");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();
        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
        assertTrue(evRes!=null);
        assertTrue(evRes.equals(-1));

        System.out.println("...................");
        psource = Parser.source("-1*(2-3)");
        astRoot = Parser.expression.apply( psource );
        Assertions.assertTrue(astRoot.isPresent());
        ASTDump.build().dump( astRoot.get() );

        ev = new Eval();
        evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
        assertTrue(evRes!=null);
        assertTrue(evRes.equals(1));
    }

    @Test
    public void test02_2(){
        System.out.println("...................");
        TPointer psource = Parser.source("-1*(2-3)");
        Optional<AST> astRoot = Parser.expression.apply( psource );
        Assertions.assertTrue(astRoot.isPresent());
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();
        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
        assertTrue(evRes!=null);
        assertTrue(evRes.equals(1));
    }

    @Test
    public void test04(){
        TPointer psource = Parser.source("1 < 2");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();
        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
    }

    @Test
    public void test05(){
        AtomicInteger sampleNum = new AtomicInteger(0);
        Eterable.of(
            Pair.of("true",true),
            Pair.of("true && false",false),
            Pair.of("!true",false),
            Pair.of("!(true)",false),
            Pair.of("null == null",true),
            Pair.of("1 < 2",true),
            Pair.of("2 < 2",false),
            Pair.of("1 <= 2",true),
            Pair.of("1 == 1",true),
            Pair.of("1 == 2",false),
            Pair.of("1 != 2",true),
            Pair.of("1 > 2",false),
            Pair.of("10 > 2",true),
            Pair.of("1.1 + 2.3 > 2",true),
            Pair.of("true || false",true)
        ).forEach( sample -> {
            if( sampleNum.incrementAndGet()>1 ){
                System.out.println("..................................");
                System.out.println();
            }
            System.out.println("--- sample "+sampleNum.get()+" ----");
            System.out.println(sample.a());
            TPointer psource = Parser.source(sample.a());

            System.out.println("--- tokens ---");
            psource.tokens().forEach(System.out::println);

            Optional<? extends AST> astRoot
                = Parser.expression.apply( psource );

            Assertions.assertTrue(astRoot!=null);
            Assertions.assertTrue(astRoot.isPresent());

            System.out.println("--- ast ---");
            ASTDump.build().dump( astRoot.get() );

            Eval ev = new Eval();
            Object evRes = ev.eval(astRoot.get());
            System.out.println("--- eval result ---");
            System.out.println(evRes);
            assertTrue(evRes!=null);
            assertTrue(evRes.equals(sample.b()));
        });
    }

    @Test
    public void varRef(){
        TPointer psource = Parser.source("a + b");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();
        ev.context().bind("a", 10 );
        ev.context().bind("b", 12 );

        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
    }

    @Test
    public void prop01(){
        TPointer psource = Parser.source("v.a + v.b");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();

        LinkedHashMap<String,Object> m = new LinkedHashMap<>();
        m.put("a",23);
        m.put("b",34);
        ev.context().bind("v", m );

        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
    }

    @Test
    public void prop02(){
        TPointer psource = Parser.source("v.a.c + v.b.d");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();

        LinkedHashMap<String,Object> l = new LinkedHashMap<>();
        l.put("c",13);
        l.put("d",24);

        LinkedHashMap<String,Object> m = new LinkedHashMap<>();
        m.put("a",l);
        m.put("b",l);
        ev.context().bind("v", m );

        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
    }

    @Test
    public void call01(){
        TPointer psource = Parser.source("fn2( 1, 2 ) + fn1( 1 ) - fn0()");

        System.out.println("--- tokens ---");
        psource.tokens().forEach(System.out::println);

        Optional<? extends AST> astRoot
            = Parser.expression.apply( psource );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        System.out.println("--- ast ---");
        ASTDump.build().dump( astRoot.get() );
    }

    @Test
    public void str01(){
        tryParse("\"abc\"", false);

        System.out.println("...............");
        tryParse("\"eval str\"", true);

        System.out.println("...............");
        tryParse("\"left\" + 'right'", true);
    }

    @Test
    public void call02(){
        tryParse("'abcdef'.substring( 1,3 )",true);
    }

    @Test
    public void compare(){
        tryParse("'af' == 'af'",true);
    }

    @Test
    public void index01(){
        parse("a[10]").run();

        ArrayList<Object> lst = new ArrayList<>();
        lst.add("a");
        lst.add("b");
        lst.add(12);

        parse("a[1]")
            .context(c->c.bind("a",lst))
            .eval()
            .run();
    }

    @Test
    public void partialParse01(){
        String src = "a[10] \u2022 888";
        System.out.println("source: "+src);

        Tokenizer<CharPointer, ? extends CToken> ptr = Lexer.tokenizer(src);
        List<CToken> tokens = new ArrayList<>();
        for( CToken tok : ptr ){
            tokens.add(tok);
        }

        System.out.println("tokens:");
        tokens.forEach(System.out::println);

        System.out.println("src len: "+src.length());
        int minPos = tokens.stream().mapToInt( t->Math.min(t.begin().position(),t.end().position()) ).min().getAsInt();
        int maxPos = tokens.stream().mapToInt( t->Math.max(t.begin().position(),t.end().position()) ).max().getAsInt();
        System.out.println(" min: "+minPos);
        System.out.println(" max: "+maxPos);
    }

    @Test
    public void partialParse02(){
        String src = "a[10] 888";
        System.out.println("source: "+src);

        Tokenizer<CharPointer, ? extends CToken> ptr = Lexer.tokenizer(src);
        List<CToken> tokens = new ArrayList<>();
        for( CToken tok : ptr ){
            tokens.add(tok);
        }

        System.out.println("tokens:");
        tokens.forEach(System.out::println);

        System.out.println("src len: "+src.length());
        int minPos = tokens.stream().mapToInt( t->Math.min(t.begin().position(),t.end().position()) ).min().getAsInt();
        int maxPos = tokens.stream().mapToInt( t->Math.max(t.begin().position(),t.end().position()) ).max().getAsInt();
        System.out.println(" min: "+minPos);
        System.out.println(" max: "+maxPos);
    }

    @Test
    public void listExpr(){
        parse("[ true, 'abc', 10, null ]").run();
    }

    @Test
    public void mapExpr(){
        parse("{}").run();
        parse("{ key: true }").run();
        parse("{ 'key name': 'value' }").run();
        parse("{ true: true }").run();
        parse("{ (1+2): true }").run();
        parse("{ key1: true, key2: 'val2' }").run();
    }
}
