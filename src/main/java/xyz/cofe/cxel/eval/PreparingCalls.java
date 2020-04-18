package xyz.cofe.cxel.eval;

import java.util.List;

/**
 * Подготовка вызовов
 */
public interface PreparingCalls {
    /**
     * Подготовка вызовов
     * @param method имя метода
     * @param args аргументы
     * @return Вариант вызова
     */
    List<? extends PreparedCall> prepare( String method, List<Object> args );
}
