package xyz.cofe.cxel;

/**
 * Ошибка при парсинге
 */
public class ParseError extends Error {
    /**
     * Конструктор по умолчанию
     */
    public ParseError(){
        super();
    }

    /**
     * Конструктор
     * @param message сообщение о ошибке
     */
    public ParseError( String message ){
        super(message);
    }

    /**
     * Конструктор
     * @param message сообщение о ошибке
     * @param cause причина
     */
    public ParseError( String message, Throwable cause ){
        super(message, cause);
    }

    public ParseError( Throwable cause ){
        super(cause);
    }
}
