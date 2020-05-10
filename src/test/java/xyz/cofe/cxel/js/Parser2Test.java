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
import java.util.Objects;
import java.util.Optional;

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
        Eval eval = new Eval(context());
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
    public void test01(){
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
    public void test02_1(){
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
    public void test02(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("1 + 2 - 3 + 4 - 5"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());
    }

    @Test
    public void test03(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("1 + 2 * 3 / 4 - 5"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());
    }

    @Test
    public void test04(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("1 * 2 + 3 / 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());
    }

    @Test
    public void test05(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("2 / 3 * 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());
    }

    @Test
    public void test06(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("2 + 3 - 4"));

        Assert.assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,BinaryOpAST,NumberAST,NumberAST,NumberAST") );
    }

    @Test
    public void test07(){
        Parser2 parser = new Parser2();
        Optional<AST> oast = parser.expression.apply(tpointer("2 + 3 * 4"));

        assertTrue(oast.isPresent());
        ASTDump.build().dump(oast.get());

        String str = oast.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str);
        assertTrue( str.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );
    }

    @Test
    public void test08(){
        Parser2 parser = new Parser2();
        Optional<AST> oast1 = parser.expression.apply(tpointer("2 + (3 + 4)"));

        assertTrue(oast1.isPresent());
        ASTDump.build().dump(oast1.get());

        String str1 = oast1.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str1);
//        assertTrue( str1.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );
    }

    @Test
    public void test09(){
        Parser2 parser = new Parser2();
        Optional<AST> oast2 = parser.expression.apply(tpointer("(2 + 3) + 4"));

        assertTrue(oast2.isPresent());
        ASTDump.build().dump(oast2.get());

        String str2 = oast2.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str2);
//        assertTrue( str2.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );
    }

    @Test
    public void test10(){
        Parser2 parser = new Parser2();
        Optional<AST> oast2 = parser.expression.apply(tpointer("(1 + 2) * 3 / 4 - 5"));

        assertTrue(oast2.isPresent());
        ASTDump.build().dump(oast2.get());

        String str2 = oast2.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str2);
//        assertTrue( str2.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );
    }

    @Test
    public void test10_2(){
        Parser2 parser = new Parser2();
        Optional<AST> oast2 = parser.expression.apply(tpointer("3 * 3 / 4 - 5"));

        assertTrue(oast2.isPresent());
        ASTDump.build().dump(oast2.get());

        String str2 = oast2.get().walk().go().map(a->a.getClass().getSimpleName() ).reduce("",(s,a)->s.length()<1 ? a : s+","+a);
        System.out.println(str2);
//        assertTrue( str2.startsWith("BinaryOpAST,NumberAST,BinaryOpAST,NumberAST,NumberAST") );
    }
}
