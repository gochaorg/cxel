package xyz.cofe.cxel.ast;

import xyz.cofe.text.tparse.TPointer;

import java.util.function.Consumer;

/**
 * Базовый класс для AST выражений
 * @param <SELF> Производный тип
 */
public abstract class ASTBase<SELF extends ASTBase<SELF>> implements AST {
    /**
     * Конструктор по умолчанию
     */
    protected ASTBase(){}

    /**
     * Уонструктор копирования
     * @param sample образец для копирования
     */
    protected ASTBase(SELF sample){
        if( sample!=null ){
            this.begin = sample.begin;
            this.end = sample.end;
        }
    }

    /**
     * Конструктор
     * @param begin начало AST узла
     * @param end конец AST узла
     */
    public ASTBase( TPointer begin, TPointer end ){
        if( begin==null )throw new IllegalArgumentException("begin==null");
        if( end==null )throw new IllegalArgumentException("end==null");
        this.begin = begin;
        this.end = end;
    }

    /**
     * Клонирование AST узла
     * @return клон
     */
    public abstract SELF clone();

    /**
     * Клонирование с настройкой клона
     * @param conf конфигурация
     * @return клон
     */
    protected SELF cloneAndConf( Consumer<SELF> conf){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        SELF c = clone();
        conf.accept(c);
        return c;
    }

    protected TPointer begin;

    /**
     * Возвращает начало токена
     * @return указатель на начало (включительно)
     */
    @Override public TPointer begin() {
        return begin;
    }

    protected TPointer end;

    /**
     * Возвращает указатель на конец токена
     * @return Указатель на конец токена (включительно)
     */
    @Override public TPointer end() {
        return end;
    }

    private static final AST[] emptyChildren = new AST[0];

    /**
     * Возвращает пустой массив
     * @return пустой массив
     */
    protected AST[] emptyChildren(){ return  emptyChildren; };

    /**
     * Возвращает указанный массив
     * @param omitNull false - возвращать массив как есть, true - возвращать массив без null ссылок
     * @param children массив
     * @return переданный массив
     */
    protected AST[] children( boolean omitNull, AST ... children ){
        if( !omitNull ) return children;
        int nonNullCnt = 0;
        for( AST a : children ){
            if( a!=null )nonNullCnt++;
        }
        AST[] arr = new AST[nonNullCnt];
        int idx = -1;
        for( AST a : children ){
            if( a!=null ){
                idx++;
                arr[idx] = a;
            }
        }
        return arr;
    }

    /**
     * Возвращает указанный массив
     * @param children массив
     * @return переданный массив
     */
    protected AST[] children(AST ... children){
        return children(false, children);
    }
}