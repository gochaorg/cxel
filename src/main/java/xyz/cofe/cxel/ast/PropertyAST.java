package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.IdTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Чтение свойства объекта
 */
public class PropertyAST extends ASTBase<PropertyAST> {
    /**
     * Конструктор по умолчанию
     */
    protected PropertyAST(){
        super();
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    protected PropertyAST( PropertyAST sample ){
        super(sample);
        this.base = sample.base;
        this.idTok = sample.idTok;
    }

    /**
     * Конструктор
     * @param begin начало
     * @param end конец
     * @param base объект чье свойство интересует
     * @param idTok имя свойства
     */
    public PropertyAST( TPointer begin, TPointer end, AST base, IdTok idTok ){
        super(begin, end);
        if( base==null )throw new IllegalArgumentException( "base==null" );
        if( idTok==null )throw new IllegalArgumentException( "idTok==null" );
        this.base = base;
        this.idTok = idTok;
    }

    @Override
    public PropertyAST clone(){
        return new PropertyAST(this);
    }

    protected AST base;

    /**
     * объект чье свойство интересует
     * @return объект
     */
    public AST base(){ return base; }

    /**
     * имя свойства (идентификатор)
     */
    protected IdTok idTok;

    /**
     * Возвращает имя свойства (идентификатор)
     * @return имя свойства (идентификатор)
     */
    public IdTok idTok(){ return idTok; }

    /**
     * Возвращает имя свойства
     * @return имя свойства
     */
    public String property(){
        IdTok t = idTok;
        return t!=null ? t.text() : null;
    }

    @Override
    public AST[] children(){
        return children(base);
    }
}
