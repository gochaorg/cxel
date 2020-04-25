package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.js.Undef;

import java.util.List;

public class BitOperator extends BaseOperator {
    @SuppressWarnings("rawtypes")
    protected static long toBit( List lst ){
        if( lst == null ) return 0;
        if( lst.isEmpty() ) return 0;
        if( lst.size() > 1 ) return 0;

        Object e = lst.get(0);
        if( e == null ) return 0;
        if( e instanceof Number ) return ((Number) e).longValue();

        return 0;
    }

    protected static long toBit( Undef undef ){
        if( undef == null ) return 0;
        return 0;
    }

    protected static long toBit( Boolean b ){
        if( b == null ) return 0;
        return b ? 1 : 0;
    }

    protected static long toBit( Number n ){
        if( Double.isNaN(n.doubleValue()) )return 0;
        return n.longValue();
    }

    protected static long toBit( String n ){
        return 0;
    }

    @SuppressWarnings("rawtypes")
    protected static long toBit( Object obj ){
        if( obj==null )return 0;
        if( obj instanceof List )return toBit((List)obj);
        if( obj instanceof Number )return toBit((Number)obj);
        if( obj instanceof String )return toBit((String)obj);
        if( obj instanceof Undef )return toBit((Undef)obj);
        if( obj instanceof Boolean )return toBit((Boolean)obj);
        return 0;
    }
}
