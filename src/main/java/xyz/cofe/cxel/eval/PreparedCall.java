package xyz.cofe.cxel.eval;

import java.util.List;

/**
 * Подготовленный вызов метода/функции
 */
public interface PreparedCall {
    /**
     * Вызов метода/функции
     * @return результат вызова
     */
    Object call( List<Object> args );

    /**
     * Возвращает факт возможности повторного вызова
     * с новыми аргментами без перестройки нового {@link Call}
     * @return true - можно повторно вызывать
     */
    boolean cacheable();
}
