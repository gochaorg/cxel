package xyz.cofe.cxel.js;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.ASTDump;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.js.op.*;
import xyz.cofe.text.tparse.TPointer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.LongStream;

@SuppressWarnings({ "rawtypes", "SimplifiableJUnitAssertion", "ConstantConditions" })
public class Parser2Test {
    //region eval
    protected EvalContext context;

    /**
     * Возвращает контекст интерпретации
     * @return контекст
     */
    public EvalContext context(){
        if( context!=null )return context;
        context = new EvalContext();
        context.bind("undefined", Undef.instance);
        context.bindStaticMethods(UnaryMinusOperator.class);
        context.bindStaticMethods(UnaryPlusOperator.class);
        context.bindStaticMethods(NotOperator.class);
        context.bindStaticMethods(OrOperator.class);
        context.bindStaticMethods(AndOperator.class);
        context.bindStaticMethods(BitOrOperator.class);
        context.bindStaticMethods(BitAndOperator.class);
        context.bindStaticMethods(BitXorOperator.class);
        context.bindStaticMethods(StrongEqualsOperator.class);
        context.bindStaticMethods(NotEqualsOperator.class);
        context.bindStaticMethods(MoreOrEqualsOperator.class);
        context.bindStaticMethods(MoreOperator.class);
        context.bindStaticMethods(LessOrEqualsOperator.class);
        context.bindStaticMethods(LessOperator.class);
        context.bindStaticMethods(EqualsOperator.class);
        context.bindStaticMethods(LShiftOpeartor.class);
        context.bindStaticMethods(RShiftOpeator.class);
        context.bindStaticMethods(RRShiftOperator.class);
        context.bindStaticMethods(AddOperator.class);
        context.bindStaticMethods(SubOperator.class);
        context.bindStaticMethods(MulOperator.class);
        context.bindStaticMethods(DivOperator.class);
        context.bindStaticMethods(ModuloOperator.class);
        context.bindStaticMethods(PowerOperator.class);
        return context;
    }

    /**
     * Интерпретация AST дерева
     * @param ast дерево
     * @return результат интерпретации
     */
    public Object eval( AST ast ){
        if( ast==null )throw new IllegalArgumentException("ast==null");
        return eval(ast, null);
    }
    public Object eval( AST ast, Consumer<EvalContext> ctx ){
        if( ast==null )throw new IllegalArgumentException("ast==null");
        EvalContext context = context();
        if( ctx!=null ){
            ctx.accept(context);
        }
        Eval eval = new Eval(context);
        return eval.eval(ast);
    }
    //endregion

    //region lexer
    private final JsLexer lexer = new JsLexer();

    public TPointer tpointer( String source ){
        if( source==null )throw new IllegalArgumentException("source==null");
        return new TPointer(lexer.tokens(source));
    }
    //endregion

    @Test
    public void numLiteral(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.literal.apply(tpointer("1.2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res);
        assertTrue(res!=null);
        assertTrue(res.equals(1.2));
    }

    @Test
    public void unaryMinus1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("-1"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(-1.0));
    }

    @Test
    public void unaryPlus1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("+1"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(1.0));
    }

    @Test
    public void varRef(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("a + b"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get(), ctx->ctx.bind("a",1.0).bind("b", 2.0));
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(3.0));
    }

    @Test
    public void list1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("[ a, b ]"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get(), ctx->ctx.bind("a",1.0).bind("b", 2.0));
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res instanceof List);

        List lst = (List)res;
        assertTrue(lst.size()==2);
        assertTrue(lst.get(0)!=null);
        assertTrue(lst.get(0).equals(1.0));
        assertTrue(lst.get(1)!=null);
        assertTrue(lst.get(1).equals(2.0));
    }

    @Test
    public void map1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("{ a: a, b: b }"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get(), ctx->ctx.bind("a",1.0).bind("b", 2.0));
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res instanceof Map);

        Map m = (Map)res;
        assertTrue(m.size()==2);

        assertTrue(m.containsKey("a"));
        assertTrue(m.get("a")!=null);
        assertTrue(m.get("a").equals(1.0));

        assertTrue(m.containsKey("b"));
        assertTrue(m.get("b")!=null);
        assertTrue(m.get("b").equals(2.0));
    }

    @Test
    public void parenthes3(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("(1)"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(1.0));
    }

    @Test
    public void multiply(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("1 * 2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(2.0));
    }

    @Test
    public void power1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("2 ** 3"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(8.0));
    }

    @Test
    public void power2(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("3 * 2 ** 3"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(24.0));
    }

    @Test
    public void bitShift1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("1 << 2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(4.0));
    }

    @Test
    public void bitShift2(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("20 >> 2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(5.0));
    }

    @Test
    public void bitShift3(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("20 >>> 2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(5.0));
    }

    @Test
    public void bitShift4(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("-1 >>> 0"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
//        assertTrue(res.equals(5.0));
        // TODO здесь херня со стороны js { -1 >>> 0 = 4294967295 } ~ groovy (0xffffffff = 4294967295) ~ (( ((long)(Integer.MAX_VALUE)) << 1  ) + 1 = 4294967295)
    }

    @Test
    public void addSubOrder(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("1 + 2 - 3 + 4 - 5"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(-1.0));
    }

    @Test
    public void addMulDivSubOrder1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("1 + 2 * 3 / 4 - 5"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,NumberAST,BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(-2.5));
    }

    @Test
    public void addMulDivSubOrder2(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("1 * 2 + 3 / 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(2.75));
    }

    @Test
    public void mulDivOrder1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("2 / 3 * 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);

        double nres = ((Double)res);
        assertTrue(nres > 2.66);
        assertTrue(nres < 2.67);
    }

    @Test
    public void addSubOrder1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("2 + 3 - 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(1.0));
    }

    @Test
    public void addMulOrder(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("2 + 3 * 4"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(14.0));
    }

    @Test
    public void parenthes1(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("2 + (3 + 4)"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str1 = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str1);
        assertTrue( str1.startsWith("BinaryOpAST,NumberAST,ParenthesAST,BinaryOpAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(9.0));
    }

    @Test
    public void parenthes2(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("(2 + 3) + 4"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,ParenthesAST,BinaryOpAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(9.0));
    }

    @Test
    public void parenthes3addSubMulDivOrder(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("(1 + 2) * 3 / 4 - 5"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,BinaryOpAST,ParenthesAST,BinaryOpAST,NumberAST,NumberAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(-2.75));
    }

    @Test
    public void mulDivSubOrder(){
        JsParser parser = new JsParser();
        Optional<AST> oast = parser.expression.apply(tpointer("3 * 3 / 4 - 5"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST,NumberAST") );

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(-2.75));
    }

    @Test
    public void speedtst1(){
        JsParser parser = new JsParser();

        EvalContext context = context();
        Eval eval = new Eval(context);

        List<String> operands = new ArrayList<>();
        for( int i=1; i<50; i++ )operands.add( Integer.toString(i) );

        List<String> operators = new ArrayList<>(
            Arrays.asList("+","-","*","/","%","**","<<",">>",">>>","|","||")
        );

        int len = 10;
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder();

        int cycles = 100;
        long[] genTime = new long[cycles];
        long[] lexerTime = new long[cycles];
        long[] parseTime = new long[cycles];
        long[] evalTime = new long[cycles];
        long[] summaryTime = new long[cycles];

        long echo = System.currentTimeMillis();
        for( int cnt=0;cnt<cycles;cnt++ ){
            if( (System.currentTimeMillis()-echo)>500 ){
                System.out.println("cycle "+(cnt+1)+" of "+cycles);
                echo = System.currentTimeMillis();
            }

            long t0 = System.nanoTime();
            sb.setLength(0);
            for( int i=0;i<len;i++ ){
                sb.append(operands.get(rnd.nextInt(operands.size())));
                sb.append(" ");
                sb.append(operators.get(rnd.nextInt(operators.size())));
                sb.append(" ");
            }
            sb.append(operands.get(rnd.nextInt(operands.size())));
            long t1 = System.nanoTime();

            TPointer tptr = tpointer(sb.toString());
            long t2 = System.nanoTime();

            Optional<AST> oast = parser.expression.apply(tptr);
            long t3 = System.nanoTime();

            if( oast.isPresent() ){
                eval.eval(oast.get());
            }
            long t4 = System.nanoTime();

            genTime[cnt] = t1-t0;
            lexerTime[cnt] = t2-t1;
            parseTime[cnt] = t3-t2;
            evalTime[cnt] = t4-t3;
            summaryTime[cnt] = t4-t0;

            //System.out.println(sb);
        }

        System.out.println("cycles="+cycles);

        Consumer<long[]> showTimes = (times)->{
            System.out.println("sum="+ LongStream.of(times).sum()/1000000L+" ms");
            System.out.println("max="+LongStream.of(times).max().getAsLong()/1000000L+" ms");
            System.out.println("avg="+LongStream.of(times).sum()/1000000L/cycles+" ms");
            System.out.println("min="+LongStream.of(times).min().getAsLong()/1000000L+" ms");
            System.out.println("99%="+LongStream.of(times).sorted().limit((long)(cycles*0.99)).max().getAsLong()*0.000001+" ms" );
            System.out.println("95%="+LongStream.of(times).sorted().limit((long)(cycles*0.95)).max().getAsLong()*0.000001+" ms" );
            System.out.println("90%="+LongStream.of(times).sorted().limit((long)(cycles*0.90)).max().getAsLong()*0.000001+" ms" );
            System.out.println("50%="+LongStream.of(times).sorted().limit((long)(cycles*0.50)).max().getAsLong()*0.000001+" ms" );
        };

        System.out.println("summary times:");
        showTimes.accept(summaryTime);

        System.out.println("gen times:");
        showTimes.accept(genTime);

        System.out.println("lexer times:");
        showTimes.accept(lexerTime);

        System.out.println("parse times:");
        showTimes.accept(parseTime);

        System.out.println("eval times:");
        showTimes.accept(evalTime);
    }
}
