package xyz.cofe.cxel.tok;

/**
 * Точность (размерность) дробных чисел
 */
public enum FloatPrecision {
    /**
     * 4 байта на число
     */
    FLOAT,

    /**
     * 8 байт на число
     */
    DOUBLE,

    /**
     * Большые числа
     */
    BIGDECIMAL
}
