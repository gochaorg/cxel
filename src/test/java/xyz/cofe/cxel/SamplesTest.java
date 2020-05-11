package xyz.cofe.cxel;

import org.junit.Test;
import xyz.cofe.cxel.eval.FnName;

import java.util.LinkedHashMap;
import java.util.Map;

public class SamplesTest {
//    @Test
//    public void parse1(){
//        System.out.println(
//            Eval.parse("1+2").eval()
//        );
//    }
//
//    public static class Bean1 {
//        private int v1;
//        public int getV1() { return v1; }
//        public void setV1(int v1) { this.v1 = v1; }
//
//        private long v2;
//        public long getV2() { return v2; }
//        public void setV2(long v2) { this.v2 = v2; }
//
//        public Bean1(){}
//        public Bean1(int v1,long v2){
//            this.v1 = v1;
//            this.v2 = v2;
//        }
//    }
//
//    @Test
//    public void bindVariables(){
//        System.out.println(
//            Eval
//                .parse("a+b")
//                .context( ctx->ctx
//                    .bind("a",1)
//                    .bind("b",2)
//                )
//            .eval()
//        );
//
//        Map<String,Object> m = new LinkedHashMap<>();
//        m.put("f1","str");
//        m.put("f2","ing");
//        m.put("f3",3);
//        m.put("f4",4);
//
//        System.out.println(
//            Eval.parse( "o.f1 + o.f2 ").context(c->c.bind("o",m)).eval()
//        );
//
//        System.out.println(
//            Eval.parse( "o['f3'] + o['f4'] ").context(c->c.bind("o",m)).eval()
//        );
//
//        Bean1 b1 = new Bean1();
//        System.out.println(
//            Eval.parse("b.v1 + b.v2 + 10.0d").context(c->c.bind("b",b1)).eval()
//        );
//    }
//
//    @Test
//    public void bindFun(){
//        System.out.println(
//            Eval
//                .parse("d2(a+b) * d2(a-b)")
//                .context( ctx->ctx
//                    .bind("a",2)
//                    .bind("b",4)
//                    .bindFn("d2",
//                        Integer.class, // тип аргумента
//                        Integer.class, // тип результата
//                        a -> a+a
//                    )
//                )
//            .eval()
//        );
//    }
//
//    public static class StatFun {
//        @FnName({"sum","summa"})
//        public static Integer sum( Integer a, Integer b ){
//            return a+b;
//        }
//    }
//
//    @Test
//    public void bindStatFun(){
//        Eval.Builder bldr = Eval
//            .parse("sum(a,b)")
//            .context( ctx->ctx
//                .bind("a",2)
//                .bind("b",4)
//                .bind("c",8)
//                .bindStaticMethods(StatFun.class)
//            );
//
//        System.out.println( bldr.eval() );
//        System.out.println( bldr.eval("summa(b,c)") );
//    }
//
//    @Test
//    public void overrideOperators(){
//        Eval.Builder bldr = Eval
//            .parse("a*b")
//            .context( ctx->ctx
//                .bind("a","a1")
//                .bind("b",4)
//                .bindFn("*", String.class, Integer.class, String.class, (str,cnt)->{
//                    StringBuilder sb = new StringBuilder();
//                    for( int i=0;i<(cnt!=null ? cnt : 0);i++ ){
//                        sb.append(str);
//                    }
//                    return sb.toString();
//                })
//            );
//
//        System.out.println( bldr.eval() );
//    }
}
