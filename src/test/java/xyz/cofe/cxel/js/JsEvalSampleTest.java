package xyz.cofe.cxel.js;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ASTDump;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.Text;

import java.util.*;
import java.util.function.Consumer;

/**
 * Примеры для демострации
 */
@SuppressWarnings({ "SimplifiableJUnitAssertion", "rawtypes", "unchecked" })
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
        public String repeat( String str, int count ){
            if( str==null )return null;
            if( count<=0 )return "";
            if( count==1 )return str;
            if( str.length()==0 )return str;
            StringBuilder sb = new StringBuilder();
            for( int i=0; i<count; i++ ){
                sb.append(str);
            }
            return sb.toString();
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

        System.out.println(
            new JsEvaluator().configure(
                ev->ev.context().bind("o",interop)
            ).eval("o.repeat('aB',2)")
        );

        evaluator.context().bindFn( "concat", String.class, String.class, String.class, (a,b)->a+b );
        System.out.println(
            evaluator.eval("concat( 'ab', 'cd' )")
        );
    }

    //region тестирование и вывод в формате markdown
    public static class Testing {
        private final Queue<Runnable> postRunQueue = new LinkedList<>();

        private JsEvaluator evaluator;
        public JsEvaluator evaluator(){
            if( evaluator==null ){
                evaluator = new JsEvaluator();
            }
            return evaluator;
        }
        public Testing evaluator(JsEvaluator ev){ evaluator=ev; return this; }

        public Testing bind(String varName, Object varValue){
            if( varName==null )throw new IllegalArgumentException("varName==null");
            evaluator().context().bind(varName, varValue);
            return this;
        }

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
            return test( evaluatedValue -> { assertTrue(Objects.equals(expectedValue,evaluatedValue)); } );
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

            JsEvaluator evaltr = evaluator();
            evaltr = evaltr==null ? new JsEvaluator() : evaltr;

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
            //.sourceTitle("### input source\n")
            .sourceShow(true)
            .tokensTitle("\n**Лексемы**\n")
            .tokensShow(true)
            .astTitle("\n**AST дерево**\n")
            .astShow(true)
            .evalTitle("\n**интерпретация**\n")
            .eval(true)
            .resultShow(true)
            .resultTitle("\n**интерпретация**\n")
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
            .source("\"double\"").expected("double").run()
            .source("\"encode\\\"en\\\\code\\'abc\"").expected("encode\"en\\code'abc").run()
            .source("'a\\nb\\rc\\bd\\fe\\tf\\vg'").expected("a\nb\rc\bd\fe\tf\u000bg").run()
            .source("'\\152\\x73'").expected("js").run()
            .source("'\\u0041'").expected("A").run()
            .source("'\\u{00041}'").expected("A").run()
            .source("'ab\\\nef'").expected("abef").run()
            ;

        header("## Прочее",true)
            .source("null").expected(null).run()
            .source("undefined").expected(Undef.instance).run();
    }

    @Test
    public void atom(){
        header("## Унарные операции", true)
            .source("-1").expected(-1.0).run()
            .source("+(1+2)").expected(3.0).run()
            .source("! true").expected(false).run()
        .header("## Ссылки на переменные",true)
            .bind("a",10).bind("b",12)
            .source("a+b").expected(22.0).run()
        .header("## Списки")
            .source("[ a, b, a+b ]").test( ev -> {
                assertTrue( ev!=null );
                assertTrue( ev instanceof List );

                List lst = (List)ev;
                assertTrue( lst.size()==3 );
                assertTrue( lst.get(0)!=null );
                assertTrue( lst.get(0).equals(10) );
                assertTrue( lst.get(1)!=null );
                assertTrue( lst.get(1).equals(12) );
                assertTrue( lst.get(2)!=null );
                assertTrue( lst.get(2).equals(22.0) );
        }).run()
        .header("## Карты")
            .source("{ a: a, b: 12, 'c': 23, true: 34, (undefined): 45+5, undefined:333, (1+2): 56, l:[ 0, 1 ], m: {x:7} }")
            .test( ev->{
                assertTrue(ev!=null);
                assertTrue(ev instanceof Map);

                Map m = (Map)ev;
                assertTrue(m.size()==9);
                assertTrue(m.containsKey("a"));
                assertTrue(m.get("a")!=null);
                assertTrue(m.get("a").equals(10));

                assertTrue(m.containsKey("b"));
                assertTrue(m.get("b")!=null);
                assertTrue(m.get("b").equals(12.0));

                assertTrue(m.containsKey("c"));
                assertTrue(m.get("c")!=null);
                assertTrue(m.get("c").equals(23.0));

                assertTrue(m.containsKey(true));
                assertTrue(m.get(true)!=null);
                assertTrue(m.get(true).equals(34.0));

                assertTrue(m.containsKey(Undef.instance));
                assertTrue(m.get(Undef.instance)!=null);
                assertTrue(m.get(Undef.instance).equals(50.0));

                assertTrue(m.containsKey("undefined"));
                assertTrue(m.get("undefined")!=null);
                assertTrue(m.get("undefined").equals(333.0));

                assertTrue(m.containsKey(3.0));
                assertTrue(m.get(3.0)!=null);
                assertTrue(m.get(3.0).equals(56.0));

                assertTrue(m.containsKey("l"));
                assertTrue(m.get("l")!=null);
                assertTrue(m.get("l") instanceof List);
                assertTrue(((List)m.get("l")).size()==2);
                assertTrue(((List)m.get("l")).get(0)!=null);
                assertTrue(((List)m.get("l")).get(0).equals(0.0));
                assertTrue(((List)m.get("l")).get(1)!=null);
                assertTrue(((List)m.get("l")).get(1).equals(1.0));

                assertTrue(m.containsKey("m"));
                assertTrue(m.get("m")!=null);
                assertTrue(m.get("m")instanceof Map);
                assertTrue(((Map)m.get("m")).size()==1);
                assertTrue(((Map)m.get("m")).containsKey("x"));
                assertTrue(((Map)m.get("m")).get("x")!=null);
                assertTrue(((Map)m.get("m")).get("x").equals(7.0));
            }).run();
    }

    @Test
    public void primary(){
        Interop1 interop1 = new Interop1();
        interop1.setStr("propvalue");

        Map map = new LinkedHashMap();
        map.put("key1","mapvalue");

        Map map2 = new LinkedHashMap();
        map2.put("a","a");
        map.put("m",map2);

        List list = new ArrayList();
        list.add(1.0);
        list.add("str");

        Object[] arr = list.toArray();

        header("## Свойство",true)
            .bind("obj",interop1).bind("map",map)
            .source("obj.str").expected(interop1.getStr()).run()
            .source("map.key1").expected(map.get("key1")).run()
            .source("map.m.a").expected(map2.get("a")).run();

        header("## Индекс",true)
            .bind("obj",interop1).bind("map",map)
            .bind("list",list).bind("arr",arr)
            .source("obj['str']").expected(interop1.getStr()).run()
            .source("map['key1']").expected(map.get("key1")).run()
            .source("map['m']['a']").expected(map2.get("a")).run()
            .source("list[0]").expected(list.get(0)).run()
            .source("list[1]").expected(list.get(1)).run()
            .source("arr[0]").expected(list.get(0)).run()
            .source("arr[1]").expected(list.get(1)).run()
            .source("obj[arr[1]]").expected(interop1.getStr()).run()
        ;

        header("## Вызов метода",true)
            .bind("obj",interop1)
            .source("obj.repeat( 'a', 3 )").expected("aaa").run();
    }

    @Test
    public void mathOp(){
        header("## Математические операции\n",true)
            .source("1 + 1").expected(2.0).run()
            .source("1 - 1").expected(0.0).run()
            .source("2 * 3").expected(6.0).run()
            .source("12 / 3").expected(4.0).run()
            .source("10 % 3").expected(1.0).run()
            .source("3 ** 3").expected(27.0).run()
            .source("1 + 2 * 4").expected(9.0).run()
            .source("(1 + 2) * 4").expected(12.0).run()
            .source("12 / 3 * 4").expected(16.0).run()
            .source("12 / (3 * 4)").expected(1.0).run()
            .source("12 / (3 * 4)").expected(1.0).run()
            .source("2 * 3 ** 3").expected(54.0).run()
            .source("(2 * 3) ** 3").expected(216.0).run();

        header("## Мат операции над не числовыми данными\n",true)
            .source("'abc' + 'def'").expected("abcdef").run()
            .source("true + false").expected(1.0).run()
            .source("true + null").expected(1.0).run()
            .source("true + NaN").test(v-> assertTrue(v instanceof Double && Double.isNaN((Double)v)) ).run()
            .source("10 + '2'").expected("102").run()
            .source("'2'+4").expected("24").run()
            .source("null + null").expected(0.0).run()
        ;
    }

    @Test
    public void overrideOp(){
        Interop1 interop1 = new Interop1();

        JsEvaluator jsEvaluator = new JsEvaluator();
        jsEvaluator.context().bindFn("*",
            String.class, Double.class, String.class,
            (str,cnt)->interop1.repeat(str,cnt.intValue())
        );

        jsEvaluator.context().bindFn("*",
            Double.class, Double.class, Double.class,
            (a,b)-> a==1.0 && b==1.0 ? 123.0 : a*b
        );

        header("## Переопределение поведения операторов",true).
            evaluator(jsEvaluator).
            source("1 * 1").expected(123.0).run().
            source("'str' * 3").expected("strstrstr").run();
    }
}
