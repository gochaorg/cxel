package xyz.cofe.cxel;

import java.util.function.Supplier;

/**
 * Кэширование функций
 */
public interface GRCache {
    /**
     * Кэширование созданных функций
     * @param key ключ
     * @param fn функция
     * @param <U> вычисляемое значеие
     * @return функция
     */
    <U> U cache(String key, Supplier<U> fn);
}
