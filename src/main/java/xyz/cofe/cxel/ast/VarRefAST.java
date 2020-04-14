package xyz.cofe.cxel.ast;

import xyz.cofe.cxel.tok.IdTok;
import xyz.cofe.text.tparse.TPointer;

/**
 * Ссылка на переменную
 */
public class VarRefAST extends ASTBase<VarRefAST> {
    /**
     * Конструктор по умолчанию
     */
    protected VarRefAST(){
        super();
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    protected VarRefAST( VarRefAST sample ){
        super(sample);
        this.idTok = sample.idTok;
    }

    /**
     * Конструктор конструктор
     * @param ptr Указатель
     * @param idTok Имя переменной
     */
    public VarRefAST( TPointer ptr, IdTok idTok ){
        super(ptr, ptr.move(1));
        if( idTok==null )throw new IllegalArgumentException( "idTok==null" );
        this.idTok = idTok;
    }

    /**
     * Клонирование
     * @return клон
     */
    @Override
    public VarRefAST clone(){
        return new VarRefAST(this);
    }

    @Override
    public AST[] children(){
        return emptyChildren();
    }

    /**
     * Идентификатор переменной
     */
    protected IdTok idTok;

    /**
     * Возвращает идентификатор переменной (имя переменной)
     * @return идентификатор переменной
     */
    public IdTok idTok(){ return idTok; }

    /**
     * Возвращает имя переменной на которую производиться ссылка
     * @return имя переменной
     */
    public String variable(){
        IdTok idTok = this.idTok;
        return idTok!=null ? idTok.text() : null;
    }
}
