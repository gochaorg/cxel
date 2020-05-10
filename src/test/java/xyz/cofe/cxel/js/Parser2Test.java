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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
    public void multiply(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("1 * 2"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        Object res = eval(oast.get());
        System.out.println("eval res = "+res+" : "+(res!=null ? res.getClass().getName() : "null"));
        assertTrue(res!=null);
        assertTrue(res.equals(2.0));
    }

    @Test
    public void addSubOrder(){
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
        Parser2 parser = new Parser2();
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
}
