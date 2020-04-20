package xyz.cofe.cxel;

import org.junit.jupiter.api.Test;
import xyz.cofe.cxel.eval.*;
import xyz.cofe.cxel.eval.op.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
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

    private static EvalContext createCtx(){
        EvalContext cx = new EvalContext();
        cx.bindStaticMethods(EqualsOprations.class);
        cx.bindStaticMethods(BoolOperations.class);
        cx.bindStaticMethods(ByteOperators.class);
        cx.bindStaticMethods(ShortOperators.class);
        cx.bindStaticMethods(IntegerOperators.class);
        cx.bindStaticMethods(LongOperators.class);
        cx.bindStaticMethods(FloatOperators.class);
        cx.bindStaticMethods(DoubleOperators.class);
        cx.bindStaticMethods(UnaryOperations.class);

        cx.bindStaticMethods(BitOperations.class);

        //noinspection ConstantConditions
        cx.bindFn("+",
            CharSequence.class,CharSequence.class,
            String.class,
            (a,b)-> a==null && b==null ? "nullnull" :
                a!=null && b==null ? a+"null" :
                    a==null && b!=null ? "null"+b :
                        a.toString()+b.toString()
        );
        return cx;
    }

    @Test
    public void stat01(){
        EvalContext ctx = createCtx();
        try{
            Method m = EvalContextTest.class.getMethod("sum", int.class, int.class);
            ctx.bindStaticMethod(m);

            List<Object> args = new ArrayList<>();
            args.add( 10 );
            args.add( 12 );
            Object res = ctx.call("sum", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void stat02(){
        EvalContext ctx = createCtx();
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
            Object res = ctx.call("sum", args );
            System.out.println("res = "+res);

            args.clear();
            args.add( 23L );
            args.add( 24L );
            res = ctx.call("sum", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }

    @Test
    public void stat03(){
        EvalContext ctx = createCtx();
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
            Object res = ctx.call("+", args );
            System.out.println("res = "+res);
        }catch( NoSuchMethodException e ){
            e.printStackTrace();
        }
    }
}
