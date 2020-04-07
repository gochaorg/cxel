package xyz.cofe.cxel.eval;

import java.util.Objects;
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
        castLooseData = sample.castLooseData;
        primitiveCast = sample.primitiveCast;
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

    /**
     * Создает клон и указывает индекс аргумента
     * @param idx индекс
     * @return клон с новыми параметрами
     */
    public ArgPass index( int idx ){
        return cloneAndConf(c->c.index = idx);
    }
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

    /**
     * Создает клон и указывает принимаемый тип
     * @param cls Принимаемый тип
     * @return клон с новыми параметрами
     */
    public ArgPass inputType( Class<?> cls ){
        return cloneAndConf( c->c.inputType=cls );
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

    /**
     * Создает клон и указывает аргумент
     * @param arg аргумент
     * @return клон с новыми параметрами
     */
    public ArgPass arg( Object arg  ){
        return cloneAndConf( c->c.arg=arg );
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

    /**
     * Создает клон и указывает аргумент может быть передан или нет, функция с данным аргументом может быть вызвана
     * @param p передаваемый аргумент
     * @return клон с новыми параметрами
     */
    public ArgPass passable( boolean p ){
        return cloneAndConf( c->c.passable = p );
    }

    /**
     * Создает клон и указывает аргумент может быть передан, функция с данным аргументом может быть вызвана
     * @return клон с новыми параметрами
     */
    public ArgPass passable(){
        return cloneAndConf( c->c.passable = true );
    }

    /**
     * Создает клон и указывает аргумент не может быть передан, функция с данным аргументом не может быть вызвана
     * @return клон с новыми параметрами
     */
    public ArgPass unpassable(){
        return cloneAndConf( c->c.passable = false );
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

    /**
     * Создает клон и указывает аргумент передается без изменений типа
     * @param inv передаваемый аргумент передается без изменений
     * @return клон с новыми параметрами
     */
    public ArgPass invariant( boolean inv ){
        return cloneAndConf( c->c.invarant=inv );
    }

    /**
     * Создает клон и указывает аргумент передается без изменений типа
     * @return клон с новыми параметрами
     */
    public ArgPass invariant(){
        return cloneAndConf( c->{
            c.invarant=true;
            c.covariant=false;
            c.implicit=false;
            c.passable=true;
        });
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

    /**
     * Создает клон и указывает значение не сопадает по типу полностью, но может быть передано, т.к. ковариантно
     * @param covar значение ковариантно
     * @return клон с новыми параметрами
     */
    public ArgPass covariant( boolean covar ){
        return cloneAndConf( c->c.covariant=covar );
    }

    /**
     * Создает клон и указывает значение не сопадает по типу полностью, но может быть передано, т.к. ковариантно
     * @return клон с новыми параметрами
     */
    public ArgPass covariant(){
        return cloneAndConf( c->{
            c.invarant=false;
            c.covariant=true;
            c.implicit=false;
            c.passable=true;
        });
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

    /**
     * Значение не совпадет по типу, но может быть передано, т.к. пременено неявное преобразование типа
     * @param implicit true - значение неявно преобразовано, false - неявного преобразования небыло
     * @return клон с настройками
     */
    public ArgPass implicit( boolean implicit ){
        return cloneAndConf( c->c.implicit = implicit );
    }

    /**
     * Значение не совпадет по типу, но может быть передано, т.к. пременено неявное преобразование типа
     * @return клон с настройками
     */
    public ArgPass implicit(){
        return cloneAndConf( c->{
            c.invarant=false;
            c.covariant=false;
            c.implicit=true;
            c.passable=true;
        });
    }
    //endregion
    //region primitiveCast : boolean - Преобразование примитивного типа данных
    /**
     * Преобразование примитивного типа данных
     */
    protected boolean primitiveCast = false;

    /**
     * Возвращает факт преобразование примитивного типа данных
     * @return Преобразование примитивного типа данных
     */
    public boolean isPrimitiveCast(){
        return primitiveCast;
    }

    /**
     * Преобразование примитивного типа данных
     * @param pcast true - Преобразование примитивного типа данных, false - небыло преобразование примитивного типа
     * @return клон
     */
    public ArgPass primitiveCast(boolean pcast){
        return cloneAndConf( c->c.primitiveCast = pcast );
    }
    //endregion
    //region castLooseData : boolean - При преобразовании типа данных, была ли потеря точности исчислений
    /**
     * При преобразовании типа данных, была ли потеря точности исчислений
     */
    protected boolean castLooseData = false;

    /**
     * При преобразовании типа данных, была ли потеря точности исчислений
     * @return была ли потеря точности исчислений
     */
    public boolean isCastLooseData(){
        return castLooseData;
    }

    /**
     * При преобразовании типа данных, была ли потеря точности исчислений
     * @param looseData true - была потеря, false - не было потери
     * @return клон с настройками
     */
    public ArgPass castLooseData( boolean looseData ){
        return cloneAndConf( c->c.castLooseData = looseData );
    }
    //endregion
}
