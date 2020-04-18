package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.iter.Eterable;

import java.util.List;
import java.util.Optional;

/**
 * Реализует один метод - lookup,
 * Который из методов объекта находит подходящие функции для вызова
 */
public class ReflectPreparingCalls extends BasePreparingCalls {
    public ReflectPreparingCalls(EvalContext context){
        super(context);
        if( context==null )throw new IllegalArgumentException("context==null");
    }

    @Override
    protected Eterable<TypedFn> lookup(String method, List<Object> args) {
        Object inst = args.size()>0 ? args.get(0) : null;
        if( inst!=null ){
            return Eterable.of(
                    inst.getClass().getMethods()).filter( m->m.getName().equals(method)
            ).map( TypedFn::of );
        }

        return Eterable.empty();
    }
}
