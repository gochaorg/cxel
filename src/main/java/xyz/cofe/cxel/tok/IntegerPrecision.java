package xyz.cofe.cxel.tok;

/**
 * Точность (размерность) целых чисел
 */
public enum IntegerPrecision {
    /** 1 байт */ BYTE,
    /** 2 байта */ SHORT,
    /** 4 байта */ INTEGER,
    /** 8 байтов */ LONG,
    /** Переменное кол-во байт */ BIGINT
}
