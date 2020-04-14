package xyz.cofe.cxel.eval;

import xyz.cofe.cxel.ast.BinaryOpAST;

import java.util.List;

/**
 * <p>Подсчет кол-ва очков для конкретного вызова.</p>
 *
 * <h1>Дилемма</h1>
 * Допустим у нас есть два метода/функции с одинаковыми названифми, но разными аргументами:
 * <pre>
 * // Сложение чисел
 * int add( int a, int b )
 *
 * // Конкатенация строк
 * String add( String a, string b )
 * </pre>
 *
 * AST дерево:
 * <ul>
 *     <li>{@link xyz.cofe.cxel.ast.BinaryOpAST} (operator={@link xyz.cofe.cxel.Keyword#Plus})</li>
 *     <li>
 *         <ul>
 *             <li>AST {@link BinaryOpAST#left()} = {@link xyz.cofe.cxel.ast.VarRefAST}</li>
 *             <li>AST {@link BinaryOpAST#right()} = {@link xyz.cofe.cxel.ast.VarRefAST}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * В данном AST дереве отсуствуют типы данных, есть только ссылка на операцию add
 * (задаеться через <code>{@link Eval#context()}.bind( "+", <i>ссылка на методы add</i> )</code>).
 * <br><br>
 *
 * Соответственно, без типов данных нельзя вывести из AST какой метод необходимо вызвать.
 * <br><br>
 *
 * <b>В runtime</b> происходит вызов {@link EvalContext#call(Object, String, List)},
 * где передаются след аргументы
 *
 * <ul>
 *     <li>inst = null</li>
 *     <li>method = "+"</li>
 *     <li>args =
 *         <ol>
 *           <li>"abc"</li>
 *           <li>"def"</li>
 *         </ol>
 *     </li>
 * </ul>
 *
 * В контексте {@link EvalContext#bind(String, Object)}
 * задано соответствие + и методов add <br>
 *
 * Необходимо выбрать подходящий метод из возможных <br>
 *
 * <p>
 * Основная идея в том, чтоб для нескольких вариантов вызовов выбрать наиболее подходящий,
 * путем подсчет кол-ва очков.
 * </p>
 *
 * Каждое очко - это дополнительное действие на вызов (конвертация аргментов или еще какая либо метрика)
 * <br><br>
 *
 * Тот вариант, который набрал наименьшее кол-во очков будет вызван.
 * @param <CALL> тип вызова
 */
public interface CallScoring<CALL extends PreparedCall> {
    /**
     * Подсчет кол-ва баллов для конкретного варианта вызова
     * @param preparedCall вариант вызова
     * @return кол-во баллов
     */
    public int calculate( CALL preparedCall );
}
