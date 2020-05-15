package xyz.cofe.cxel.eval.score;

/**
 * Возвращает кол-во аргументов которые передаются с преобразованием примитивных типов данных
 */
public interface PrimitiveCastArgs {
    /**
     * Возвращает кол-во аргументов которые передаются с преобразованием примитивных типов данных
     * @return
     *     0 - нет аргументов которые передаются с преобразованием примитивных типов данных <br>
     *     1 - один аргумент
     */
    int primitiveCastArgs();
}
