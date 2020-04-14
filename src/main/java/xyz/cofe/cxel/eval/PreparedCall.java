package xyz.cofe.cxel.eval;

/**
 * Подготовленный вызов метода/функции
 */
public interface PreparedCall {
    /**
     * Вызов метода/функции
     * @return результат вызова
     */
    Object call();
}
