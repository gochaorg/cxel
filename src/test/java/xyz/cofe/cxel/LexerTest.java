package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;

public class LexerTest {
    @Test
    public void test01(){
        Lexer.tokens("1 + 2 - 3 * 4 / ( 5 + 6 ) ").forEach( System.out::println );
    }
}
