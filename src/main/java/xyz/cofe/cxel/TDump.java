package xyz.cofe.cxel;

import xyz.cofe.iter.Eterable;
import xyz.cofe.iter.TreeStep;

import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Дамп/снимок дерева
 */
public class TDump<A> {
    public TDump(){
        out = System.out;
        endl = System.lineSeparator();
        indent = "-|";
    }

    public TDump<A> configure( Consumer<TDump<A>> conf ){
        if(conf!=null) conf.accept(this);
        return this;
    }

    private Function<A,Iterable<? extends A>> follow;
    public synchronized Function<A, Iterable<? extends A>> getFollow(){ return follow; }
    public synchronized void setFollow( Function<A, Iterable<? extends A>> follow ){ this.follow = follow; }

    private Appendable out;
    public synchronized Appendable getOut(){ return out; }
    public synchronized void setOut( Appendable out ){ this.out = out; }

    private String endl;
    public synchronized String getEndl(){ return endl; }
    public synchronized void setEndl( String endl ){ this.endl = endl; }

    private String indent;
    public synchronized String getIndent(){ return indent; }
    public synchronized void setIndent( String indent ){ this.indent = indent; }

    public synchronized void dump( A root ){
        if( out==null )return;
        try {
            go( root );
        } catch( IOException ex ){
            throw new IOError(ex);
        } finally {
            if( out instanceof Writer ){
                try {
                    ((Writer) out).flush();
                } catch( IOException ex ){
                    System.err.println("can't flush "+ex);
                }
            }else if( out instanceof OutputStream ){
                try {
                    ((OutputStream) out).flush();
                } catch( IOException ex ){
                    System.err.println("can't flush "+ex);
                }
            }
        }
    }

    protected void println() throws IOException {
        if( endl!=null ){
            out.append(endl);
        }
    }
    protected void println(Object ... args) throws IOException {
        if( args!=null ){
            for( Object arg : args ){
                out.append(arg!=null ? arg.toString() : "null");
            }
        }
        if( endl!=null ){
            out.append(endl);
        }
    }

    protected void go( A root ) throws IOException {
        if( out==null )return;
        if( root==null ){
            println("null");
            return;
        }

        if( follow!=null ){
            Eterable<TreeStep<A>> treeSteps = Eterable.tree(root, follow).go();
            go( treeSteps );
        }
    }
    protected void go( Eterable<TreeStep<A>> treeSteps ) throws IOException {
        StringBuilder indnt = new StringBuilder();
        for( TreeStep<A> ts : treeSteps ){
            indnt.setLength(0);
            if( indent!=null && indent.length()>0 ){
                for( int i = 0; i < ts.getLevel(); i++ ){
                    indnt.append(indent);
                }
            }
            println(indnt,decode(ts.getNode()));
        }
    }

    protected String decode( A a ){
        if( a==null )return "null";
        String cname = a.getClass().getSimpleName();
        Function<Object,String> deco = decoders.get(a.getClass());
        if( deco!=null ){
            return cname+" "+deco.apply(a);
        }
        return cname;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Map<Class,Function<Object,String>> decoders
        = new LinkedHashMap<>();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <N extends A> TDump<A> decode( Class<N> cls, Function<N,String> decoder ){
        if( cls==null )throw new IllegalArgumentException( "cls==null" );
        if( decoder==null )throw new IllegalArgumentException( "decoder==null" );
        decoders.put(cls,(Function)decoder);
        return this;
    }
}
