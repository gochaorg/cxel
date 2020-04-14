package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.cxel.tok.StringTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

public class LexerTest {
    @Test
    public void test01(){
        Lexer.tokens("1 + 2 - 3 * 4 / ( 5 + 6 ) ").forEach( System.out::println );
    }

    @Test
    public void testId(){
        List<? extends CToken> toks = Lexer.tokens("1 a abc a12 a.b");
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
        Lexer.tokens("1 0x0A 0xbc 0b1010 377 0377 3777 03777").forEach( t -> {
            if( t instanceof IntegerNumberTok ){
                IntegerNumberTok n = ((IntegerNumberTok) t);
                System.out.println(t+" radix="+n.radix()+" value="+n.value()+" bigint="+n.bigIntegerValue());
            }else{
                System.out.println(t);
            }
        });
    }
}
