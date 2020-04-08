package xyz.cofe.cxel.eval;

import java.lang.annotation.*;

/**
 * Указывает имя функции
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FnName {
    String[] value();
}
