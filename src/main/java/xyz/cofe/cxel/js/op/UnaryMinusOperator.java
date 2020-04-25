package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.eval.FnName;
import xyz.cofe.cxel.js.Undef;

import java.util.ArrayList;
import java.util.List;

public class UnaryMinusOperator extends BaseOperator {
/*
- null                        -  null                      : object                => 0                     : number
- undefined                   -  undefined                 : undefined             => NaN                   : number
- true                        -  true                      : boolean               => -1                    : number
- false                       -  false                     : boolean               => 0                     : number
- 1                           -  1                         : number                => -1                    : number
- 0                           -  0                         : number                => 0                     : number
- 0.1                         -  0.1                       : number                => -0.1                  : number
-                             -                            : string                => 0                     : number
- xyz                         -  xyz                       : string                => NaN                   : number
-                             -                            : object of Array       => 0                     : number
- 1                           -  1                         : object of Array       => -1                    : number
- [object Object]             -  [object Object]           : object of Object      => NaN                   : number
- [object Object]             -  [object Object]           : object of Object      => NaN                   : number

> - ([ 1, 2 ])
NaN
> - ([ 1 ])
-1
> - ([ 1, 2 ])
NaN
> - ([ 1 ])
-1
> - ([ 2 ])
-2
> - ([ -3 ])
3
> - ([ null ])
-0
> - ([ null, null ])
NaN

> - ([ '' ])
-0

> - ([ 'a' ])
NaN

> - ([  ])
-0
> - ([ []  ])
-0
> - ([ [ '' ]  ])
-0
> - ([ [ 'aa' ]  ])
NaN

> let lst = []
undefined
> lst.push( lst )
1
> lst
[ [Circular] ]
> - lst
-0

 */
    @SuppressWarnings("rawtypes")
    @FnName("-")
    public static Double uminusNull( Object v ){
        if( v==null )return -0.0;

        if( v instanceof List )return uminusList((List)v, null);
        if( v instanceof String )return uminus((String)v);

        return Double.NaN;
    }

    @FnName("-")
    public static Double uminus( Undef undef ){
        if( undef==null )return -0.0;
        return Double.NaN;
    }

    @FnName("-")
    @SuppressWarnings("rawtypes")
    public static Double uminus( List lst ){
        return uminusList( lst, null );
    }

    @SuppressWarnings("rawtypes")
    public static Double uminusList( List lst, List visited ){
        if( visited==null )visited = new ArrayList();
        if( visited.contains(lst) ){
            return -0.0;
        }
        visited.add(lst);

        if( lst==null )return -0.0;
        if( lst.isEmpty() )return -0.0;
        if( lst.size()>1 )return Double.NaN;

        Object itm = lst.get(0);
        if( itm==null )return -0.0;

        if( itm instanceof String ) return uminus((String)itm);
        if( itm instanceof Number ) return uminus( ((Number)itm).doubleValue() );
        if( itm instanceof Boolean ) return uminus((Boolean)itm);
        if( itm instanceof Undef ) return uminus((Undef)itm);
        if( itm instanceof List ) return uminusList((List)itm, visited);

        return Double.NaN;
    }

    @FnName("-")
    public static Double uminus( Double num ){
        if( num==null )return -0.0;
        return -num;
    }

    @FnName("-")
    public static Double uminus( String str ){
        if( str==null )return -0.0;
        if( str.length()==0 )return -0.0;
        return Double.NaN;
    }

    @FnName("-")
    public static Double uminus( Boolean v ){
        if( v==null )return -0.0;
        return v ? -1.0 : 0.0;
    }
}
