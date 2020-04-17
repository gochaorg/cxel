package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;
import xyz.cofe.iter.Eterable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * Абстрактный класс, который из предложенных функций {@link #lookup(Object, String, List)}
 * составляет возможные варианты вызовов, используя различные формы преобразования типов аргументов
 * см реализацию {@link #prepare(Object, String, List)}
 */
public abstract class BasePreparingCalls implements PreparingCalls {
    /**
     * Имя функции в контексте
     * которая реализует имплицитное преобразование
     * типов аргументов
     */
    public static final String IMPLICIT="implicit";

    protected final EvalContext context;

    public BasePreparingCalls(EvalContext context) {
        this.context = context;
    }

    /**
     * Поиск методов/функций которые возможно пододут для вызова
     * @param inst Объект
     * @param method метод
     * @param args арменты
     * @return Вызываемые методы
     */
    protected abstract Eterable<TypedFn> lookup(Object inst, String method, List<Object> args);

    @Override
    public List<? extends PreparedCall> prepare(Object inst, String method, List<Object> args) {
        Eterable<TypedFn> targetFunctions = lookup(inst, method, args);

        if( targetFunctions==null || targetFunctions.count()<1 ){
            throw new EvalError("method/function '"+method+"' not found");
        }

        return targetFunctions
                .map( m->{
            Call rcall = null;
            Type[] params = m.getParameterTypes();
            if( params.length==0 ){
                rcall = new Call(inst,m);
                rcall.setInputArgs(args);
            }else{
                rcall = new Call(inst,m);
                rcall.setInputArgs(args);
                for( int pi=0; pi<params.length; pi++ ){
                    Class<?> p = (Class<?>) params[pi];
                    if( pi>=args.size() ){
                        // Значение параметра не определено
                        rcall.getArgs().add( ArgPass.unpassable(pi, p, null));
                    }else{
                        Class<?> pt = (Class<?>) p;

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
                                    // Поиск имплицитного преобразования типа
                                    StaticMethods implicitCasts = null;

                                    if( context!=null ){
                                        Optional<Object> oImplicitCasts = context.tryRead(IMPLICIT);
                                        if( oImplicitCasts!=null && oImplicitCasts.isPresent() && oImplicitCasts.get() instanceof StaticMethods ){
                                            StaticMethods sm = (StaticMethods)oImplicitCasts.get();
                                            implicitCasts = sm.sameRetAndArgs(pt, at);
                                        }
                                    }

                                    if( implicitCasts!=null && implicitCasts.size()==1 ) {
                                        if( implicitCasts.size()==1 ) {
                                            // Найдено одно и единственное возможное имплицитное преобразование
                                            TypedFn implctCaster = implicitCasts.first().get();
                                            Object castedValue = implctCaster.call(null, new Object[]{arg});
                                            rcall.getArgs().add(ArgPass.implicit(pi, pt, castedValue));
                                        }else{
                                            if( implicitCasts.size()==0 ) {
                                                // Подходящих имплицитных преобразований не найдено
                                                // Нет совместимых вариантов
                                                rcall.getArgs().add(ArgPass.unpassable(pi, pt, arg));
                                            }else{
                                                System.err.println("found multiple implicit argument cast");
                                                int idx=-1;
                                                for( TypedFn tFn : implicitCasts ){
                                                    idx++;
                                                    System.out.println("  implicit cast #"+idx+":"+tFn);
                                                }
                                            }
                                        }
                                    }else {
                                        // Подходящих имплицитных преобразований не найдено
                                        // Нет совместимых вариантов
                                        rcall.getArgs().add(ArgPass.unpassable(pi, pt, arg));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return rcall;
        }).filter(Call::callable)
        .toList();
    }
}
