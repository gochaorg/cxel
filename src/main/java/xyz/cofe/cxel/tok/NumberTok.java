package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

import java.util.function.Consumer;

/**
 * Лексема - Число
 */
public abstract class NumberTok<SELF extends NumberTok<SELF>> extends CToken {
    /**
     * Конструктор
     * @param begin начало лексемы
     * @param end конец лексемы
     */
    public NumberTok( CharPointer begin, CharPointer end ){
        super(begin, end);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    protected NumberTok( NumberTok<SELF> sample ){
        super(sample.begin(), sample.end());
    }

    /**
     * Клонирование
     * @return клон
     */
    public abstract SELF clone();

    /**
     * Клонирование и настройка
     * @param conf настройка
     * @return клон с конфигурацией
     */
    protected SELF cloneAndConf( Consumer<SELF> conf ){
        SELF cl = clone();
        if( conf!=null )conf.accept(cl);
        return cl;
    }

    /**
     * Возвращает число
     * @return число
     */
    public abstract Number value();

    /**
     * Клонирование с указанием другого расположения лексемы
     * @param begin начало лексемы
     * @param end конец лексемы
     * @return клон-лексема
     */
    public SELF location(CharPointer begin,CharPointer end){
        SELF cloned = (SELF) super.location(begin,end);
        return cloned;
    }
}
