package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

public abstract class MapEntryAST<KEY extends AST,SELF extends MapEntryAST<KEY,SELF>>
    extends ASTBase<SELF>
{
    protected MapEntryAST() {
        super();
    }

    protected MapEntryAST(SELF sample) {
        super(sample);
        if( sample!=null ){
            key = sample.key;
            value = sample.value;
        }
    }

    public MapEntryAST(TPointer begin, TPointer end, KEY key, AST value) {
        super(begin, end);
        if( key==null )throw new IllegalArgumentException("key==null");
        if( value==null )throw new IllegalArgumentException("value==null");
        this.key = key;
        this.value = value;
    }

    protected KEY key;
    public KEY key(){ return key; }
    public SELF key( KEY newKey ){
        if( newKey==null )throw new IllegalArgumentException("newKey==null");
        return cloneAndConf(c->c.key = newKey);
    }

    protected AST value;
    public AST value(){ return value; }
    public SELF value( AST newValue ){
        if( newValue==null )throw new IllegalArgumentException("newValue==null");
        return cloneAndConf(c->c.value = newValue);
    }

    @Override
    public AST[] children() {
        return children(key, value);
    }
}
