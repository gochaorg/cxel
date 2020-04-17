package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Создание карты
 */
public class MapAST extends ASTBase<MapAST> {
    protected MapAST() {
        super();
        entries = new MapEntryAST<?,?>[0];
    }

    protected MapAST(MapAST sample) {
        super(sample);
        if( sample!=null && sample.entries!=null ) {
            entries = Arrays.copyOf(sample.entries, sample.entries.length);
        }else {
            entries = new MapEntryAST<?,?>[0];
        }
    }

    public MapAST(TPointer begin, TPointer end, MapEntryAST<?,?>[] entries) {
        super(begin, end);
        if( entries!=null ){
            for( int i=0; i<entries.length; i++ ){
                if( entries[i]==null )throw new IllegalArgumentException("entries["+i+"]==null");
            }
            this.entries = entries;
        }else{
            this.entries = new MapEntryAST<?,?>[0];
        }
    }

    public MapAST(TPointer begin, TPointer end, Iterable<MapEntryAST<?,?>> entries) {
        super(begin, end);
        if( entries!=null ){
            List<MapEntryAST<?,?>> lst = new ArrayList<>();
            int i=-1;
            for( MapEntryAST<?,?> e : entries ){
                i++;
                if( e==null )throw new IllegalArgumentException("entries["+i+"]==null");
                lst.add(e);
            }
            this.entries = lst.toArray(new MapEntryAST[0]);
        }else{
            this.entries = new MapEntryAST<?,?>[0];
        }
    }

    @Override
    public MapAST clone() {
        return new MapAST(this);
    }

    protected MapEntryAST<?,?>[] entries;

    @Override
    public AST[] children() {
        return entries;
    }

    public MapEntryAST<?,?>[] entries(){ return entries; }
    public MapAST entries( MapEntryAST<?,?> ... newEntries ){
        if( newEntries==null )throw new IllegalArgumentException("newEntries==null");
        for( int i=0; i<newEntries.length; i++ ){
            if( newEntries[i]==null )throw new IllegalArgumentException("newEntries["+i+"]==null");
        }
        return cloneAndConf( c->c.entries = newEntries );
    }
    public MapAST entries( Iterable<MapEntryAST<?,?>> newEntries ){
        if( newEntries==null )throw new IllegalArgumentException("newEntries==null");
        List<MapEntryAST<?,?>> lst = new ArrayList<>();
        int i=-1;
        for( MapEntryAST<?,?> e : newEntries ){
            i++;
            if( e==null )throw new IllegalArgumentException("newEntries["+i+"]==null");
            lst.add(e);
        }
        return cloneAndConf( c->c.entries = lst.toArray(new MapEntryAST[0]) );
    }
}
