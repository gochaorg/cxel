package xyz.cofe.cxel;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.ast.BinaryOpAST;
import xyz.cofe.cxel.ast.NumberAST;
import xyz.cofe.fn.Pair;
import xyz.cofe.iter.Eterable;
import xyz.cofe.text.tparse.TPointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ParserTest {
    @Test
    public void test01(){
        Optional<? extends AST> astRoot
            = Parser.plusMinus.apply( Parser.source( "1 + 2 + 3" ) );

        System.out.println(astRoot);

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        ASTDump.build().dump( astRoot.get() );

        Eval ev = new Eval();
        Object evRes = ev.eval(astRoot.get());
        System.out.println("eval result: "+evRes);

        assertTrue( evRes!=null && evRes.equals(6L));
    }

    @Test
    public void test02(){
        Optional<? extends AST> astRoot
            = Parser.plusMinus.apply( Parser.source( "1 * 2 + 3 - 4 / 5" ) );

        Assertions.assertTrue(astRoot!=null);
        Assertions.assertTrue(astRoot.isPresent());

        new TDump<AST>().configure( c -> {
            c.setFollow(AST::nodes);
            c.decode(BinaryOpAST.class, BinaryOpAST::opText);
            c.decode(NumberAST.class, n -> n.value().toString());
        } ).dump( astRoot.get() );
    }

    @Test
    public void test03(){
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
}
