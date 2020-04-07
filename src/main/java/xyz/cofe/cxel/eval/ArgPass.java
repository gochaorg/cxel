package xyz.cofe.cxel.eval;

import java.util.function.Consumer;

/**
 * Передача аргумента в функцию
 */
public class ArgPass {
    /**
     * Конструктор по умолчанию
     */
    public ArgPass(){
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public ArgPass( ArgPass sample ){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        passable = sample.passable;
        index = sample.index;
        inputType = sample.inputType;
        arg = sample.arg;
        invarant = sample.invarant;
        covariant = sample.covariant;
        implicit = sample.implicit;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ArgPass clone(){
        return new ArgPass(this);
    }

    protected ArgPass configure( Consumer<ArgPass> conf ){
        if( conf!=null ){
            conf.accept(this);
        }
        return this;
    }

    protected ArgPass cloneAndConf( Consumer<ArgPass> conf ){
        return clone().configure(conf);
    }

    /**
     * Аргумент не может быть передан
     * @param idx индекс аргумента
     * @param type входящий тип
     * @param value значение аргумента
     * @return Форма передачи аргмента
     */
    public static ArgPass unpassable( int idx, Class<?> type, Object value ){
        return new ArgPass().configure( c -> {
            c.passable = false;
            c.implicit = false;
            c.covariant = false;
            c.invarant = false;
            c.inputType = type;
            c.arg = value;
            c.index = idx;
        });
    }

    /**
     * Аргумент может быть передан без какиз либо преобрахований
     * @param idx индекс аргумента
     * @param type входящий тип
     * @param value значение аргумента
     * @return Форма передачи аргмента
     */
    public static ArgPass invariant( int idx, Class<?> type, Object value ){
        return new ArgPass().configure( c -> {
            c.passable = true;
            c.implicit = false;
            c.covariant = false;
            c.invarant = true;
            c.inputType = type;
            c.arg = value;
            c.index = idx;
        });
    }

    /**
     * Аргумент может быть передан с использованием коваринтного приведения типа
     * @param idx индекс аргумента
     * @param type входящий тип
     * @param value значение аргумента
     * @return Форма передачи аргмента
     */
    public static ArgPass covar( int idx, Class<?> type, Object value ){
        return new ArgPass().configure( c -> {
            c.passable = true;
            c.implicit = false;
            c.covariant = true;
            c.invarant = false;
            c.inputType = type;
            c.arg = value;
            c.index = idx;
        });
    }

    /**
     * Аргумент может быть передан с использованием неявного преобразования
     * @param idx индекс аргумента
     * @param type входящий тип
     * @param value значение аргумента
     * @return Форма передачи аргмента
     */
    public static ArgPass implicit( int idx, Class<?> type, Object value ){
        return new ArgPass().configure( c -> {
            c.passable = true;
            c.implicit = true;
            c.covariant = false;
            c.invarant = false;
            c.inputType = type;
            c.arg = value;
            c.index = idx;
        });
    }

    //region index - Индекс аргумента
    protected int index;

    /**
     * Индекс аргумента
     * @return индекс
     */
    public int getIndex(){ return index; }
    //endregion
    //region inputType - Принимаемый тип
    /**
     * Принимаемый тип
     */
    protected Class<?> inputType;

    /**
     * Возвращает Принимаемый тип
     * @return Принимаемый тип
     */
    public Class<?> getInputType(){
        return inputType;
    }
    //endregion
    //region arg : Object - аргумент
    protected Object arg;

    /**
     * Входной аргумент
     * @return аргумент
     */
    public Object getArg(){
        return arg;
    }
    //endregion
    //region passable : boolean - Аргумент может быть передан, функция с данным аргументом может быть вызвана
    /**
     * Аргумент может быть передан, функция с данным аргументом может быть вызвана
     */
    protected boolean passable;

    /**
     * Аргумент может быть передан, функция с данным аргументом может быть вызвана
     * @return true - передаваемый аргумент
     */
    public boolean isPassable(){
        return passable;
    }
    //endregion
    //region invarant : boolean - Аргумент передается без изменений
    /**
     * Аргумент передается без изменений
     */
    protected boolean invarant;

    /**
     * Аргумент передается без изменений типа
     * @return Аргумент передается без изменений
     */
    public boolean isInvarant(){
        return invarant;
    }
    //endregion
    //region covariant : boolean - значение ковариантно
    protected boolean covariant;

    /**
     * Значение не сопадает по типу полностью, но может быть передано, т.к. ковариантно
     * @return значение ковариантно
     */
    public boolean isCovariant(){
        return covariant;
    }
    //endregion
    //region implicit : boolean - значение не явно преобразовано
    protected boolean implicit;

    /**
     * Значение не совпадет по типу, но может быть передано, т.к. пременено неявное преобразование типа
     * @return значение неявно преобразовано
     */
    public boolean isImplicit(){
        return implicit;
    }
    //endregion
}
