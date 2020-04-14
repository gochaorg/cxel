package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.iter.Eterable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Optional;

public class ReflectPreparingCalls implements PreparingCalls {
    public ReflectPreparingCalls(EvalContext context){
        if( context==null )throw new IllegalArgumentException("context==null");
        this.context = context;
    }

    private final EvalContext context;

    @Override
    public List<? extends PreparedCall> prepare(Object inst, String method, List<Object> args) {
        Eterable<Method> methods = null;

        if( inst!=null ){
            methods = Eterable.of(inst.getClass().getMethods()).filter( m->m.getName().equals(method) );
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

        if( methods.count()<1 ){
            throw new EvalError("method/function '"+method+"' not found");
        }

        return methods.map( m->{
            ReflectCall rcall = null;
            Parameter[] params = m.getParameters();
            if( params.length==0 ){
                rcall = new ReflectCall(inst,m);
            }else{
                rcall = new ReflectCall(inst,m);
                for( int pi=0; pi<params.length; pi++ ){
                    Parameter p = params[pi];
                    if( pi>=args.size() ){
                        // Значение параметра не определено
                        rcall.getArgs().add( ArgPass.unpassable(pi, p.getType(), null));
                    }else{
                        Class<?> pt = p.getType();

                        Object arg = args.get(pi);
                        Class<?> at = arg!=null ? arg.getClass() : Object.class;
                        if( arg==null && pt.isPrimitive() ){
                            // Значение параметра не может быть null
                            rcall.getArgs().add( ArgPass.unpassable(pi,pt,null) );
                        }else {
                            if( pt.equals(at) ){
                                // Полное совпадение типа
                                rcall.getArgs().add( ArgPass.invariant(pi,pt,arg) );
                            }else if( pt.isAssignableFrom(at) ){
                                // Коваринтное совпадение типов
                                rcall.getArgs().add( ArgPass.covar(pi,pt,arg) );
                            }else{
                                if( NumCast.isPrimitiveNumber(pt) && arg instanceof Number ){
                                    // Парамтер представлен примитивным числом и передано число
                                    NumCast ncast = NumCast.tryCast(pt,(Number)arg);
                                    if( ncast!=null ){
                                        if( ncast.sameType ){
                                            rcall.getArgs().add(
                                                    ArgPass.invariant(pi, pt, ncast.targetValue)
                                                            .primitiveCast(true)
                                                            .castLooseData(false)
                                            );
                                        }else {
                                            rcall.getArgs().add(
                                                    ArgPass.covar(pi, pt, ncast.targetValue)
                                                            .primitiveCast(true)
                                                            .castLooseData(ncast.looseData)
                                            );
                                        }
                                    }else {
                                        Object v = pt.cast(arg);
                                        rcall.getArgs().add(ArgPass.covar(pi, pt, v));
                                    }
                                }else{
                                    // Нет совместимых вариантов
                                    rcall.getArgs().add(ArgPass.unpassable(pi, pt, arg));
                                }
                            }
                        }
                    }
                }
            }
            return rcall;
        }).filter(ReflectCall::callable)
        .toList();
    }
}
