package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;

import java.util.List;

/**
 * Идентификатор
 */
public class IdTok extends CToken {
    /**
     * Конструктор
     * @param begin начало идентификатора
     * @param end конец идентификатора
     */
    public IdTok( CharPointer begin, CharPointer end ){
        super(begin, end);
    }

    /**
     * Конструктор
     * @param begin начало идентификатора
     * @param end конец идентификатора
     */
    public IdTok( CToken begin, CToken end ){
        super(begin, end);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public IdTok( IdTok sample ){
        super(sample);
    }

    public IdTok clone(){ return new IdTok(this); }

    /**
     * Конструктор
     * @param tokens токены входящие в идентификатор
     */
    public IdTok( List<CToken> tokens ){
        super(tokens);
    }
}
