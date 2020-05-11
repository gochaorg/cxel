package xyz.cofe.cxel.js;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ASTDump;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.Text;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Примеры для демострации
 */
public class JsEvalSampleTest {
    public static class Interop1 {
        private String str;
        public String getStr(){ return str; }
        public void setStr( String value ){
            this.str = value;
        }

        public void print( String str ){
            System.out.println("call print: "+str);
        }
    }

    @Test
    public void interop1(){
        Interop1 interop = new Interop1();
        interop.setStr("xtr");

        JsEvaluator evaluator = new JsEvaluator();
        evaluator.context()
            .bind("i",interop)
            .bind("a","str")
            .bind("b",10)
        ;

        Object res = evaluator.eval("i.print( a + b + i.str )");
        System.out.println("result "+res);
    }

    //region тестирование и вывод в формате markdown
    public static class Testing {
        private final Queue<Runnable> postRunQueue = new LinkedList<>();

        private JsEvaluator evaluator;
        public JsEvaluator evaluator(){ return evaluator; }
        public Testing evaluator(JsEvaluator ev){ evaluator=ev; return this; }

        private String header;
        private String header(){ return header; }
        private Testing header(String title){ header = title; return this; }
        private Testing header(String title, boolean dropHeaderAfter){
            header = title;
            if( dropHeaderAfter ){
                postRunQueue.add(() -> {
                    header = null;
                });
            }
            return this;
        }

        private String source = null;
        public String source(){ return source; }
        public Testing source(String v){ source=v; return this; }

        private boolean sourceShow = false;
        public boolean sourceShow(){ return sourceShow; }
        public Testing sourceShow( boolean src){ sourceShow =src; return this; }

        private String sourceTitle;
        private String sourceTitle(){ return sourceTitle; }
        private Testing sourceTitle(String title){ sourceTitle = title; return this; }

        private boolean resultShow = false;
        public boolean resultShow(){ return resultShow; }
        public Testing resultShow( boolean v){ resultShow =v; return this; }

        private String resultTitle;
        private String resultTitle(){ return resultTitle; }
        private Testing resultTitle(String title){ resultTitle = title; return this; }

        private boolean tokensShow = false;
        public boolean tokensShow(){ return tokensShow; }
        public Testing tokensShow( boolean v){ tokensShow =v; return this; }

        private String tokensTitle;
        private String tokensTitle(){ return tokensTitle; }
        private Testing tokensTitle(String title){ tokensTitle = title; return this; }

        private boolean astShow = false;
        public boolean astShow(){ return astShow; }
        public Testing astShow( boolean v){ astShow =v; return this; }

        private String astTitle;
        private String astTitle(){ return astTitle; }
        private Testing astTitle(String title){ astTitle = title; return this; }

        private boolean eval = false;
        public boolean eval(){ return eval; }
        public Testing eval(boolean v){ eval=v; return this; }

        private String evalTitle;
        private String evalTitle(){ return evalTitle; }
        private Testing evalTitle(String title){ evalTitle = title; return this; }

        private String footprint;
        private String footprint(){ return footprint; }
        private Testing footprint(String title){ footprint = title; return this; }

        private Consumer<Object> test = null;
        public Consumer<Object> test(){ return test; }
        public Testing test(Consumer<Object> v){ test=v; return this; }
        public Testing expected( Object expectedValue ){
            return test( evaluatedValue -> { Assert.assertTrue(Objects.equals(expectedValue,evaluatedValue)); } );
        }

        public Testing run(){
            if( source==null )throw new IllegalStateException("source not defined");

            if( header!=null ){
                System.out.println(header);
            }

            if( sourceShow ){
                if( sourceTitle!=null ) System.out.println(sourceTitle);
                System.out.println(Text.indent("    ",source));
            }

            JsEvaluator evaltr = evaluator==null ? new JsEvaluator() : evaluator;

            List<? extends CToken> tokens = evaltr.tokens(source,0);
            if( tokens==null )throw new IllegalStateException("return null tokens");
            if( tokensShow ){
                if( tokensTitle!=null ) System.out.println(tokensTitle);
                int idx=-1;
                for(CToken tok : tokens){
                    idx++;
                    System.out.println( " "+(idx+1)+". "+tok);
                }
            }

            AST ast = evaltr.parse(tokens);
            if( astShow ){
                if( astTitle!=null ) System.out.println(astTitle);
                ASTDump.build().configure(c->{
                    c.setIndentPrefix("");
                    c.setIndent("  ");
                    c.setIndentSuffix("* ");
                }).dump(ast);
            }

            Object res = evaltr.eval(ast);
            if( resultShow ){
                if( resultTitle!=null ){
                    System.out.println(resultTitle);
                }
                System.out.println(
                    Text.indent( "    ",
                    ""+res+(res!=null ? " : "+res.getClass():"")
                    )
                );
            }

            if( test!=null ){
                test.accept(res);
            }

            if( footprint!=null ){
                System.out.println(footprint);
            }

            while( true ){
                Runnable r = postRunQueue.poll();
                if( r!=null ){
                    r.run();
                    continue;
                }
                break;
            }

            return this;
        }
    }

    public Testing source( String source ){
        return new Testing()
            .source(source)
            .sourceTitle("### input source\n")
            .sourceShow(true)
            .tokensTitle("\n### tokens\n")
            .tokensShow(true)
            .astTitle("\n### ast tree")
            .astShow(true)
            .evalTitle("\n### eval result\n")
            .eval(true)
            .resultShow(true)
            .resultTitle("\n### eval result\n")
            .footprint("\n\n")
            ;
    }

    public Testing header( String header ){
        return source(null).header(header);
    }
    public Testing header( String header,boolean dropHeaderAfterRun ){
        return source(null).header(header,dropHeaderAfterRun);
    }
    //endregion

    @Test
    public void literals(){
        header("## Число",true)
            .source("1").expected(1.0).run()
            .source("1.0").expected(1.0).run()
            .source("NaN").test(null).run();

        header("## Булево",true)
            .source("true").expected(true).run()
            .source("false").expected(false).run();

        header("## Строка",true)
            .source("'single'").expected("single").run()
            .source("'double'").expected("double").run();

        header("## Прочее",true)
            .source("null").expected(null).run()
            .source("undefined").expected(Undef.instance).run();
    }
}
