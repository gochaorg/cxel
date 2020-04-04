package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
import xyz.cofe.text.tparse.CToken;

import java.util.List;

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
}
