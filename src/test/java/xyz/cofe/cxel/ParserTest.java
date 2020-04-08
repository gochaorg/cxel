package xyz.cofe.cxel;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.fn.Pair;
import xyz.cofe.iter.Eterable;
import xyz.cofe.text.tparse.TPointer;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ParserTest {
    public void tryParse( String source, boolean eval ){
        tryParse(source,eval,false,null);
    }
    public void tryParse( String source, boolean eval, boolean checkExpected, Object expected ){
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
            Object evRes = ev.eval(astRoot.get());
            System.out.println("eval result: " + evRes);

            if( checkExpected ){
                System.out.println("expected: "+expected);
                assertTrue(Objects.equals(expected, evRes));
            }
        }
    }

    @Test
    public void test01(){
        tryParse("1 + 2 + 3",true, true, 6L);
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
        assertTrue(evRes.equals(-1L));

        System.out.println("...................");
        psource = Parser.source("-1*(2-3)");
        astRoot = Parser.expression.apply( psource );
        Assertions.assertTrue(astRoot.isPresent());
        ASTDump.build().dump( astRoot.get() );

        ev = new Eval();
        evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);
        assertTrue(evRes!=null);
        assertTrue(evRes.equals(1L));
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
            Pair.of("true & false",false),
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
            Pair.of("true | false",true)
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
}
