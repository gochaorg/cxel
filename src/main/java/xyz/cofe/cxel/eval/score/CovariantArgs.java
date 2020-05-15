package xyz.cofe.cxel.eval.score;

/**
 * Возвращает кол-во аргументов которые передаются с ковариантным преобразованием типов данных
 */
public interface CovariantArgs {
    /**
     * Возвращает кол-во аргументов которые передаются с ковариантным преобразованием типов данных
     * @return
     *     0 - нет аргументов которые передаются с ковариантным преобразованием типов данных <br>
     *     1 - один аргумент
     */
    int covariantArgs();
}
