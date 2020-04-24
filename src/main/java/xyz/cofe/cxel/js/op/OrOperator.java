package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

/**
 * Оператор {@link xyz.cofe.cxel.Keyword#Or}
 * <br>
 *
 * Согласно логам поведение оператора || определяется левым операндом.
 */
public class OrOperator {
    //region null or obj || null
    /**
     * При передачи left=null всегда возвращается правый (right) операнд
     * @param left левый операнд, ожидается null
     * @param right правый операнд
     * @return правый операнд
     */
    @FnName("||")
    public static Object or( Object left, Object right ){
        if( left==null )return right;
        if( Undef.instance.equals(left))return right;
        if( left instanceof Boolean ){
            Boolean b = (Boolean)left;
            return or(b,right);
        }
        if( left instanceof String ){
            String s = (String)left;
            return or(s,right);
        }
        if( left instanceof Number ){
            Number n = (Number)left;
            return or(n,right);
        }
        return left;
    }
    //endregion

    //region x || undefined
    /**
     * При передачи left=undefined всегда возвращается правый (right) операнд
     * @param left левый операнд, ожидается undefined
     * @param right правый операнд
     * @return правый операнд
     */
    @FnName("||")
    public static Object or( Undef left, Object right ){
        // В случает left = null, результат аналогичный
        return right;
    }

    @FnName("||")
    public static Object or( Object left, Undef right ){
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left==null )return right;
        return left;
    }

    @FnName("||")
    public static Object or( Boolean left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return left ? left : right;
    }

    @FnName("||")
    public static Object or( Undef left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        return right;
    }

    @FnName("||")
    public static Object or( String left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( left.length()==0 )return right;
        return left;
    }

    @FnName("||")
    public static Object or( Double left, Undef right ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( Double.isNaN(left) )return right;
        if( left==0 )return right;
        return left;
    }
    //endregion

    //region boolean || x
    /**
     * Если left = true, то true, иначе right
     * @param left Левый операнд
     * @param right Правый операнд
     * @return Если left = true, то true, иначе right
     */
    @FnName("||")
    public static Object or( Boolean left, Object right ){
        if( left==null )return right;
        return left ? true : right;
    }
    //endregion

    //region double || x
    /**
     * Если left!=0.0 тогда =&gt; left иначе right
     * @param left Левый операнд
     * @param right Правый операнд
     * @return Если left!=0.0 тогда =&gt; left иначе right
     */
    @FnName("||")
    public static Object or( Double left, Object right ){
        if( left==null )return right;
        if( left==0 )return right;
        if( Double.isNaN(left) )return right;
        return right;
    }
    //endregion

    //region string || x
    /**
     * Если left.length()==0 тогда =&gt; left иначе right
     * @param left Левый операнд
     * @param right Правый операнд
     * @return Если left.length()==0 тогда =&gt; left иначе right
     */
    @FnName("||")
    public static Object or( String left, Object right ){
        if( left==null )return right;
        if( left.length()==0 )return right;
        return left;
    }
    //endregion
}
