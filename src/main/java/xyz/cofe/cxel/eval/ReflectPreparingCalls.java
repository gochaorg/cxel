package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.iter.Eterable;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Реализует один метод - lookup,
 * Который из методов объекта находит подходящие функции для вызова.
 *
 * <p>
 * Данный объект может содержать фильтр безопастности {@link #securityFilter(Predicate)},
 * который для сформерованной выборки удаляет те методы, которые не должны быть вызваны
 */
public class ReflectPreparingCalls extends BasePreparingCalls {
    public ReflectPreparingCalls(EvalContext context){
        super(context);
        if( context==null )throw new IllegalArgumentException("context==null");
    }

    public ReflectPreparingCalls(ReflectPreparingCalls sample){
        super(sample);
        this.securityFilter = sample.securityFilter;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public ReflectPreparingCalls clone(){
        return new ReflectPreparingCalls(this);
    }

    /**
     * фильтр безопастности
     */
    protected Predicate<Method> securityFilter;

    /**
     * Возвращает фильтр безопастности
     * @return фильтр
     */
    public Predicate<Method> securityFilter(){ return securityFilter; }

    /**
     * Клонирует объект и указывает для клона новый фильтр безопастности
     * @param filter фильтр
     * @return клон с новым фильтром
     */
    public ReflectPreparingCalls securityFilter( Predicate<Method> filter ){
        ReflectPreparingCalls clone = clone();
        clone.securityFilter = filter;
        return clone;
    }

    @Override
    protected Eterable<TypedFn> lookup(String method, List<Object> args) {
        Object inst = args.size()>0 ? args.get(0) : null;
        if( inst!=null ){
            Eterable<Method> methds = Eterable.of(
                inst.getClass().getMethods()).filter( m->m.getName().equals(method)
            );

            Predicate<Method> sfilter = securityFilter;
            if( sfilter!=null ){
                methds = methds.filter(securityFilter);
            }

            return methds.map( TypedFn::of );
        }

        return Eterable.empty();
    }
}
