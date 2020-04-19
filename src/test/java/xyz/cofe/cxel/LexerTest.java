package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.cxel.tok.NumberTok;
import xyz.cofe.cxel.tok.StringTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LexerTest {
    @Test
    public void test01(){
        new Lexer().tokens("1 + 2 - 3 * 4 / ( 5 + 6 ) ").forEach( System.out::println );
    }

    @Test
    public void testId(){
        List<? extends CToken> toks = new Lexer().tokens("1 a abc a12 a.b");
        toks.forEach(System.out::println);
    }

    @Test
    public void javascriptString01(){
        String jsCode = "\"hello \\\"js\\\" litteral\"";
        System.out.println(jsCode);

        CharPointer cptr = new CharPointer(jsCode);
        Optional<StringTok> tok = StringTok.javascript.apply(cptr);

        assertTrue(tok!=null);
        assertTrue(tok.isPresent());

        System.out.println("tok val="+tok.get().value());
    }

    @Test
    public void hexBinOctInt(){
        List<? extends CToken> toks = new Lexer().tokens("1 0x0A 0xbc 0b1011 377 0377 3777 03777");

        toks.forEach( t -> {
            if( t instanceof IntegerNumberTok ){
                IntegerNumberTok n = ((IntegerNumberTok) t);
                System.out.println(t+" radix="+n.radix()+" value="+n.value()+" bigint="+n.bigIntegerValue());
            }else{
                System.out.println(t);
            }
        });

        List<IntegerNumberTok> numToks = toks.stream()
            .map( t-> t instanceof IntegerNumberTok ? (IntegerNumberTok)t : null )
            .filter( Objects::nonNull )
            .collect(Collectors.toList());

        assertTrue( numToks.stream().anyMatch(n->n.longValue()==1) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==10) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==188) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==11) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==377) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==255) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==3777) );
        assertTrue( numToks.stream().anyMatch(n->n.longValue()==2047) );
    }

    @Test
    public void numSuffixes(){
        List<? extends CToken> toks = new Lexer().tokens("1 2b 3s 4i 5l 6n 7w 8.0f 9.1d 10.0w 11.0n");

        toks.forEach( t -> {
            if( t instanceof NumberTok ){
                NumberTok<?> n = ((NumberTok<?>) t);
                System.out.println(t+" value="+n.value()+(n.value()!=null ? " : "+n.value().getClass().getSimpleName() : ""));
            }else{
                System.out.println(t);
            }
        });

        List<NumberTok<?>> numToks = toks.stream()
            .map( t-> t instanceof NumberTok ? (NumberTok<?>)t : null )
            .filter( Objects::nonNull )
            .collect(Collectors.toList());

//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==1) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==10) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==188) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==11) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==377) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==255) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==3777) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==2047) );
    }

    @Test
    public void floatNums(){
        List<? extends CToken> toks = new Lexer().tokens(
            "1.2 .34 .45f .46d .56w .67n 12. 13.f 14.d 15.w 16.n "+
                "17f 18d 19wf 20wd"
        );

        toks.forEach( t -> {
            if( t instanceof NumberTok ){
                NumberTok<?> n = ((NumberTok<?>) t);
                System.out.println(t+" value="+n.value()+(n.value()!=null ? " : "+n.value().getClass().getSimpleName() : ""));
            }else{
                System.out.println(t);
            }
        });

        List<NumberTok<?>> numToks = toks.stream()
            .map( t-> t instanceof NumberTok ? (NumberTok<?>)t : null )
            .filter( Objects::nonNull )
            .collect(Collectors.toList());

//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==1) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==10) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==188) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==11) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==377) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==255) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==3777) );
//        assertTrue( numToks.stream().anyMatch(n->n.longValue()==2047) );
    }
}
