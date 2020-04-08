package xyz.cofe.cxel.eval;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Описывает сигнатуру вызова
 */
public interface CallSign {
    /**
     * Типы аргментов вызова
     * @return аргументы вызова
     */
    List<Type> arguments();

    /**
     * Возвращаемые типы данных
     * @return возвращаемые типы данных
     */
    Type returns();

    /**
     * Создание сигнатуры вызова и метода рефлексии
     * @param method метод рефлексии
     * @return сигнатура
     */
    public static CallSign of( Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        return new CallSign() {
            @Override
            public List<Type> arguments(){
                return Arrays.asList(method.getGenericParameterTypes());
            }

            @Override
            public Type returns(){
                return method.getGenericReturnType();
            }
        };
    }
}
