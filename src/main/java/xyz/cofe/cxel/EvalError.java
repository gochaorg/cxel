package xyz.cofe.cxel;

/**
 * Ошибка периода исполнения
 */
public class EvalError extends RuntimeException {
    public EvalError(){
        super();
    }

    public EvalError( String message ){
        super(message);
    }

    public EvalError( String message, Throwable cause ){
        super(message, cause);
    }

    public EvalError( Throwable cause ){
        super(cause);
    }
}
