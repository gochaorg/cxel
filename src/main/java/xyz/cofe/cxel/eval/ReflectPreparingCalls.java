package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.iter.Eterable;

import java.util.List;
import java.util.Optional;

/**
 * Реализует один метод - lookup,
 * Который из контекста и/или методов объекта находит подходящие функции для вызова
 */
public class ReflectPreparingCalls extends BasePreparingCalls {
    public ReflectPreparingCalls(EvalContext context){
        super(context);
        if( context==null )throw new IllegalArgumentException("context==null");
    }

    @Override
    protected Eterable<TypedFn> lookup(Object inst, String method, List<Object> args) {
        Eterable<TypedFn> methods = null;

        if( inst!=null ){
            methods = Eterable.of(
                    inst.getClass().getMethods()).filter( m->m.getName().equals(method)
            ).map( TypedFn::of );
            if( methods.count()<1 ){
                throw new EvalError("method '"+method+"' not found in "+inst.getClass());
            }
        }else{
            Optional<Object> m = context.tryRead(method);
            if( !m.isPresent() || !(m.get() instanceof StaticMethods) ){
                throw new EvalError("function '"+method+"' not found");
            }
            methods = Eterable.of((StaticMethods)m.get());
        }

        return methods;
    }
}
