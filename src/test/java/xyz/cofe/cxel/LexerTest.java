package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
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
}
