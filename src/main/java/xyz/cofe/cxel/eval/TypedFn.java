package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.EvalError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Функция с типами
 */
public interface TypedFn {
    /**
     * Возвращает тип аргументов
     * @return тип аргументов
     */
    Type[] getParametersType();

    /**
     * Возврщает тип реузльтата
     * @return тип результата
     */
    Type getReturnType();

    /**
     * Вызов функции
     * @param args аргументы
     * @return результат вызова
     */
    Object call( Object[] args );

    /**
     * Создание экземпляра для существующего статического метода
     * @param method метод
     * @return Экземпляр
     */
    static TypedFn of(Method method){
        if( method==null )throw new IllegalArgumentException("method==null");

        boolean methIsStatic = (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
        if( !methIsStatic ){
            throw new IllegalArgumentException("method not static");
        }

        return new TypedFn() {
            @Override
            public Type[] getParametersType() {
                return method.getGenericParameterTypes();
            }

            @Override
            public Type getReturnType() {
                return method.getGenericReturnType();
            }

            @Override
            public Object call(Object[] args) {
                try {
                    return method.invoke(null, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new EvalError(e);
                }
            }
        };
    }
}
