package xyz.cofe.cxel.eval;

import xyz.cofe.iter.Eterable;

import java.util.List;
import java.util.Optional;

/**
 * Реализует один метод - lookup,
 * Который из контекста находит подходящие функции для вызова
 */
public class ContextPrepatingCalls extends BasePreparingCalls {
    public ContextPrepatingCalls(EvalContext context){
        super(context);
        if( context==null )throw new IllegalArgumentException("context==null");
    }

    @Override
    protected Eterable<TypedFn> lookup(String method, List<Object> args) {
        Optional<Object> namedFunctions = context.tryRead(method);
        if( namedFunctions!=null && namedFunctions.isPresent() && namedFunctions.get() instanceof StaticMethods ){
            return Eterable.of( (StaticMethods)namedFunctions.get() );
        }

        return Eterable.empty();
    }
}
