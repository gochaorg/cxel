package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.js.JsLexer;
import xyz.cofe.cxel.js.Undef;
import xyz.cofe.cxel.tok.NumberTok;
import xyz.cofe.text.tparse.CToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseOperator {
    protected static final JsLexer lexer = new JsLexer();

    @SuppressWarnings("rawtypes")
    public static double toNumber( String str ){
        if( str==null || str.length()==0 )return 0.0;

        List<? extends CToken> toks = lexer.tokens(str);
        if( toks==null || toks.isEmpty() )return Double.NaN;

        CToken tok = toks.get(0);
        if( !(tok instanceof NumberTok) )return Double.NaN;

        Number num = ((NumberTok)tok).value();
        if( num==null )return Double.NaN;

        return num.doubleValue();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static double toNumber( List lst ){
        return toNumber(lst,null);
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "SameParameterValue" })
    private static double toNumber( List<Object> lst, List visited ){
        if( lst==null )return Double.NaN;

        while( true ){
            if( lst.isEmpty() ) return 0.0;
            if( lst.size() > 1 ) return Double.NaN;

            Object v = lst.get(0);
            if( v == null ) return 0.0;
            if( v instanceof Number ) return ((Number) v).doubleValue();
            if( v instanceof String ){
                return toNumber((String) v);
            }

            if( Undef.instance.equals(v) ) return 0.0;
            if( v instanceof List ){
                if( visited==null )visited = new ArrayList<>();
                if( visited.contains(v) )return 0.0;
                visited.add( lst );
                lst = (List<Object>)v;
                continue;
            }
            break;
        }

        return Double.NaN;
    }

    public static double toNumber( Boolean bool ){
        if( bool==null )return 0;
        return bool ? 1 : 0;
    }

    @SuppressWarnings("rawtypes")
    public static String toString( List lst ){
        StringBuilder sb = new StringBuilder();
        throw new UndefinedBehaviorError();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static String toString_itm( Object itm, List visited ){
        if( itm==null )return "";
        if( Undef.instance.equals(itm) )return "";

        if( visited==null )visited = new ArrayList();
        if( visited.contains(itm) )return "";
        visited.add(itm);

        if( itm instanceof List )return toString_itm( (List)itm, visited );
        if( itm instanceof Boolean )return ((Boolean)itm) ? "true" : "false";
        if( itm instanceof Number )return itm.toString();
        if( itm instanceof String )return itm.toString();

        return "[object Object]";
    }

    @SuppressWarnings("rawtypes")
    private static String toString_itm( List lst, List visited ){
        if( lst==null )throw new IllegalArgumentException("lst==null");
        if( lst.isEmpty() )return "";

        StringBuilder sb = new StringBuilder();
        int i=-1;
        for( Object itm : lst ){
            i++;
            if( i>0 )sb.append(",");
            sb.append( toString_itm(itm, visited) );
        }

        return sb.toString();
    }

    public static Optional<Object> first( List lst ){
        if( lst==null )return Optional.empty();

        while( true ){
            if( lst.isEmpty() ) return Optional.empty();
            if( lst.size() > 1 ) return Optional.empty();
            Object u = lst.get(0);
            if( u instanceof List ){
                lst = (List)u;
                continue;
            }
            return Optional.ofNullable(u);
        }
    }
}
