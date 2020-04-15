package xyz.cofe.cxel;

import org.junit.Test;
import xyz.cofe.cxel.eval.Eval;

public class EvalParseTest {
    @Test
    public void parse1(){
        System.out.println(
            Eval.parse("1+2").eval()
        );
    }
}
