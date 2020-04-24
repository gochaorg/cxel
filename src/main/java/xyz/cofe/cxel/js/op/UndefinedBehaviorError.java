package xyz.cofe.cxel.js.op;

import xyz.cofe.cxel.EvalError;

/**
 * Не определенное поведение.
 * При возникновении данной ошибки,
 * необходимо вмешательтсво в интерпретатор.
 *
 * Данная ошибка является заглушкой в коде, для последующей реализацтт данного случая
 */
public class UndefinedBehaviorError extends EvalError {
    public UndefinedBehaviorError(){
        this("Undefined behavior");
    }

    public UndefinedBehaviorError( String message ){
        super(message);
    }

    public UndefinedBehaviorError( String message, Throwable cause ){
        super(message, cause);
    }

    public UndefinedBehaviorError( Throwable cause ){
        this("Undefined behavior",cause);
    }
}
