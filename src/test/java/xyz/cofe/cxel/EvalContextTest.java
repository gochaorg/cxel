package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.eval.op.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EvalContextTest {
    public static int sum( int a, int b ){
        System.out.println("sum(int,int)");
        return a + b;
    }

    public static long sum( long a, long b ){
        System.out.println("sum(long,long)");
        return a + b;
    }

    @Test
    public void stat01(){
        EvalContext ctx = new EvalContext();
        try{
            Method m = EvalContextTest.class.getMethod("sum", int.class, int.class);
            ctx.bindStaticMethod(m);

            List<Object> args = new ArrayList<>();
            args.add( 10 );
            args.add( 12 );
            Object res = ctx.call(null,"sum", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void stat02(){
        EvalContext ctx = new EvalContext();
        try{
            ctx.bindStaticMethod(
                EvalContextTest.class.getMethod("sum", int.class, int.class)
            );

            ctx.bindStaticMethod(
                EvalContextTest.class.getMethod("sum", long.class, long.class)
            );

            List<Object> args = new ArrayList<>();
            args.add( 10 );
            args.add( 12 );
            Object res = ctx.call(null,"sum", args );
            System.out.println("res = "+res);

            args.clear();
            args.add( 23L );
            args.add( 24L );
            res = ctx.call(null,"sum", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void stat03(){
        EvalContext ctx = new EvalContext();
        try{
            ctx.bindStaticMethod( "+",
                EvalContextTest.class.getMethod("sum", int.class, int.class)
            );

            ctx.bindStaticMethod( "+",
                EvalContextTest.class.getMethod("sum", long.class, long.class)
            );

            List<Object> args = new ArrayList<>();
            args.add( 10 );
            args.add( 12 );
            Object res = ctx.call(null,"+", args );
            System.out.println("res = "+res);

            args.clear();
            args.add( 23L );
            args.add( 24L );
            res = ctx.call(null,"+", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void numTest(){
        EvalContext ctx = new EvalContext();
        ctx.bindStaticMethods(ByteOperators.class);
        ctx.bindStaticMethods(ShortOperators.class);
        ctx.bindStaticMethods(IntegerOperators.class);
        ctx.bindStaticMethods(LongOperators.class);
        ctx.bindStaticMethods(FloatOperators.class);
        ctx.bindStaticMethods(DoubleOperators.class);

        List<Object> args = new ArrayList<>();
        args.add( 10 );
        args.add( 12 );
        Object res = ctx.call(null,"+", args );
        System.out.println("res = "+res+(res!=null ? " : "+res.getClass() : ""));

        args.clear();
        args.add( 23L );
        args.add( 24L );
        res = ctx.call(null,"+", args );
        System.out.println("res = "+res+(res!=null ? " : "+res.getClass() : ""));
    }

    @FnName({"|","||"})
    public static boolean or( boolean a, boolean b ){ return a || b; }


}
