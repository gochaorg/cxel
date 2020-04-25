package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

/**
 * Логическая операция НЕ - !
 */
public class NotOperator extends BaseOperator {
    @FnName("!")
    public static boolean leftNull( Object value ){
        if( value==null )return true;
        return false;
    }

    @FnName("!")
    public static boolean leftUndef( Undef undef ){
        return true;
    }

    @FnName("!")
    public static boolean leftBoolean( Boolean value ){
        if( value==null )return true;
        return !value;
    }

    @FnName("!")
    public static boolean leftNumber( Double num ){
        if( num==null )return true;
        if( Double.isNaN(num) )return true;

        return num==0.0;
    }

    @FnName("!")
    public static boolean leftString( String str ){
        if( str==null )return true;
        return str.length()==0;
    }
}
