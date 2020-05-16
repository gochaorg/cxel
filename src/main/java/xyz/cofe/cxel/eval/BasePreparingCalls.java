package xyz.cofe.cxel.eval;

import xyz.cofe.iter.Eterable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * Абстрактный класс, который из предложенных функций {@link #lookup( String, List)}
 * составляет возможные варианты вызовов, используя различные формы преобразования типов аргументов
 * см реализацию {@link #prepare( String, List)}
 */
public abstract class BasePreparingCalls implements PreparingCalls {
    /**
     * Имя функции в контексте
     * которая реализует имплицитное преобразование
     * типов аргументов
     */
    public static final String IMPLICIT="implicit";

    /**
     * Контекст исполнения
     */
    protected final EvalContext context;

    /**
     * Конструктор
     * @param context контекст исполнения
     */
    public BasePreparingCalls(EvalContext context) {
        this.context = context;
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public BasePreparingCalls(BasePreparingCalls sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        this.context = sample.context;
    }

    /**
     * Поиск методов/функций которые возможно пододут для вызова
     * @param method метод
     * @param args арменты
     * @return Вызываемые методы
     */
    protected abstract Eterable<TypedFn> lookup(String method, List<Object> args);

    @Override
    public List<? extends PreparedCall> prepare(String method, List<Object> args) {
        Eterable<TypedFn> targetFunctions = lookup(method, args);

        return targetFunctions
                .map( m->{
            Call rcall = null;
            Type[] params = m.getParameterTypes();
            if( params.length==0 ){
                rcall = new Call(m);
                rcall.setInputArgs(args);
            }else{
                rcall = new Call(m);
                rcall.setInputArgs(args);
                for( int pi=0; pi<params.length; pi++ ){
                    Class<?> p = (Class<?>) params[pi];
                    if( pi>=args.size() ){
                        // Значение параметра не определено
                        rcall = rcall.addArg( ArgPass.unpassable(pi, p, null).cacheable(true) );
                    }else{
                        Object arg = args.get(pi);
                        Class<?> at = arg!=null ? arg.getClass() : Object.class;
                        if( arg==null && p.isPrimitive() ){
                            // Значение параметра не может быть null
                            rcall = rcall.addArg( ArgPass.unpassable(pi,p,null).cacheable(true) );
                        }else {
                            if( p.equals(at) ){
                                // Полное совпадение типа
                                rcall = rcall.addArg( ArgPass.invariant(pi,p,ArgPass::inputValue).cacheable(true) );
                            }else if( p.isAssignableFrom(at) ){
                                // Коваринтное совпадение типов
                                rcall = rcall.addArg( ArgPass.covar(pi,p,ArgPass::inputValue).cacheable(true) );
                            }else{
                                if( NumCast.isPrimitiveNumber(p) && arg instanceof Number ){
                                    // Парамтер представлен примитивным числом и передано число
                                    NumCast ncast = NumCast.tryCast(p,(Number)arg);
                                    if( ncast!=null ){
                                        //ArgPass argPass = ArgPass.invariant(pi, p,ncast.targetValue );
                                        ArgPass argPass = ArgPass.invariant(pi,p,apass->
                                            NumCast.tryCast(p,(Number)apass.inputValue()).targetValue
                                        ).cacheable(true);

                                        if( ncast.sameType ){
                                            rcall = rcall.addArg(
                                                argPass
                                                    .primitiveCast(true)
                                                    .castLooseData(false)
                                            );
                                        }else {
                                            rcall = rcall.addArg(
                                                argPass
                                                    .primitiveCast(true)
                                                    .castLooseData(ncast.looseData)
                                            );
                                        }
                                    }else {
                                        //Object v = p.cast(arg);
                                        //rcall = rcall.addArg(ArgPass.covar(pi, p, v));
                                        rcall = rcall.addArg( ArgPass.covar(
                                            pi, p, apass -> p.cast(apass.inputValue())
                                        ).cacheable(true) );
                                    }
                                }else{
                                    // Нет совместимых вариантов
                                    // Поиск имплицитного преобразования типа
                                    StaticMethods implicitCasts = null;

                                    if( context!=null ){
                                        Optional<Object> oImplicitCasts = context.tryRead(IMPLICIT);
                                        if( oImplicitCasts!=null && oImplicitCasts.isPresent() && oImplicitCasts.get() instanceof StaticMethods ){
                                            StaticMethods sm = (StaticMethods)oImplicitCasts.get();
                                            implicitCasts = sm.sameRetAndArgs(p, at);
                                        }
                                    }

                                    if( implicitCasts!=null && implicitCasts.size()==1 ) {
                                        if( implicitCasts.size()==1 ) {
                                            // Найдено одно и единственное возможное имплицитное преобразование
                                            TypedFn implctCaster = implicitCasts.first().get();

                                            //Object castedValue = implctCaster.call(new Object[]{arg});
                                            //rcall = rcall.addArg(ArgPass.implicit(pi, p, castedValue));

                                            Object[] implArgs = new Object[1];
                                            rcall = rcall.addArg(ArgPass.implicit(pi, p, apass->{
                                                implArgs[0] = apass.inputValue();
                                                return implctCaster.call(implArgs);
                                            }).cacheable(true)
                                            );
                                        }else{
                                            if( implicitCasts.size()==0 ) {
                                                // Подходящих имплицитных преобразований не найдено
                                                // Нет совместимых вариантов
                                                rcall = rcall.addArg(
                                                    ArgPass.unpassable(pi, p, arg).cacheable(true)
                                                );
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
                                        rcall = rcall.addArg(
                                            ArgPass.unpassable(pi, p, arg)
                                            .cacheable(true)
                                        );
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
