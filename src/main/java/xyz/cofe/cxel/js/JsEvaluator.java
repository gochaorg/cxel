package xyz.cofe.cxel.js;

import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.eval.BasePreparingCalls;
import xyz.cofe.cxel.eval.Eval;
import xyz.cofe.cxel.eval.EvalContext;
import xyz.cofe.cxel.eval.score.DefaultScrolling;
import xyz.cofe.cxel.js.op.*;
import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.TPointer;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Парсинг js выражений.
 * <br>
 *
 * <h1>Лексический анализатор</h1>
 * <h2>Литералы</h2>
 * <ul>
 *     <li>
 *         Все литералы относящиеся к числам (потомки {@link NumberTok})
 *         всегда возвращают значение типа Double
 *     </li>
 * </ul>
 *
 * <h2>Литералы и типы данных</h2>
 *
 * <a href="https://developer.mozilla.org/ru/docs/Web/JavaScript/Guide/Grammar_and_types">Согласно mozilla есть</a>
 *
 * <ul>
 *     <li>Шесть типов данных,  которые являются примитивами:
 *     <ul>
 *         <li>
 *             <b>Boolean</b>. true и false.
 *         </li><li>
 *             <b>null</b>. Специальное ключевое слово, обозначающее нулевое или «пустое» значение. Поскольку JavaScript чувствителен к регистру, null не то же самое, что Null, NULL или любой другой вариант.
 *         </li><li>
 *             <b>undefined</b>. Свойство глобального объекта; переменная, не имеющая присвоенного значения, обладает типом undefined.
 *         </li><li>
 *             <b>Number</b>. 42 или 3.14159.
 *         </li><li>
 *             <b>String</b>. "Howdy".
 *         </li><li>
 *             <b>Symbol</b> (ECMAScript 6)
 *         </li>
 *     </ul>
 *     </li>
 *     <li>
 *         и <b>Object</b>
 *     </li>
 * </ul>
 *
 * <h1>Синтаксический анализатор</h1>
 * <h2>Приоритет бинарных операций</h2>
 *
 * <a href="https://developer.mozilla.org/ru/docs/Web/JavaScript/Guide/Expressions_and_Operators">
 *     Приоритет операций согласно Mozilla
 * </a>
 *
 * <table border="0">
 *     <tr>
 *         <td>Тип оператора</td>
 *         <td>Операторы</td>
 *     </tr>
 *     <tr>
 *         <td>свойство объекта</td>
 *         <td>. []</td>
 *     </tr>
 *     <tr>
 *         <td>вызов, создание экземпляра объекта</td>
 *         <td>() new</td>
 *     </tr>
 *     <tr>
 *         <td>отрицание, инкремент</td>
 *         <td>! ~ - + ++ -- typeof void delete</td>
 *     </tr>
 *     <tr>
 *         <td>умножение, деление</td>
 *         <td>* / %</td>
 *     </tr>
 *     <tr>
 *         <td>сложение, вычитание</td>
 *         <td>+ -</td>
 *     </tr>
 *     <tr>
 *         <td>побитовый сдвиг</td>
 *         <td>
 * 	           &lt;&lt; &gt;&gt; &gt;&gt;&gt;
 * 	       </td>
 *     </tr>
 *     <tr>
 *         <td>сравнение, вхождение</td>
 *         <td>&lt; &lt;= &gt; &gt;= in instanceof</td>
 *     </tr>
 *     <tr>
 *         <td>равенство</td>
 *         <td>== != === !==</td>
 *     </tr>
 *     <tr>
 *         <td>битовое-и</td>
 *         <td>&amp;</td>
 *     </tr>
 *     <tr>
 *         <td>битовое-исключающее-или</td>
 *         <td>^</td>
 *     </tr>
 *     <tr>
 *         <td>битовое-или	вертикальная черта</td>
 *         <td>׀</td>
 *     </tr>
 *     <tr>
 *         <td>логическое-и	</td>
 *         <td>&&</td>
 *     </tr>
 *     <tr>
 *         <td>логическое-или две вертикальная черты</td>
 *         <td>׀׀</td>
 *     </tr>
 *     <tr>
 *         <td>условный (тернарный) оператор</td>
 *         <td>?:</td>
 *     </tr>
 *     <tr>
 *         <td>присваивание</td>
 *         <td>= += -= *= /= %= &lt;&lt;= &gt;&gt;= &gt;&gt;&gt;= &amp;= ^= ׀=</td>
 *     </tr>
 *     <tr>
 *         <td>запятая</td>
 *         <td>,</td>
 *     </tr>
 * </table>
 */
public class JsEvaluator {
    /**
     * Конфигурация
     * @param conf конфигурация
     * @return SELF ссылка
     */
    public JsEvaluator configure( Consumer<JsEvaluator> conf){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        conf.accept(this);
        return this;
    }

    //region лексический анализатор
    /** лексический анализатор */
    protected JsLexer lexer;

    /**
     * Лексер / Лексический анализ
     * @return лексический анализатор
     */
    public JsLexer lexer(){
        if( lexer!=null )return lexer;
        lexer = new JsLexer();
        return lexer;
    }
    //endregion

    /** Парсер / синтаксичесий анализ */
    protected JsParser parser;

    /**
     * Парсер на лексемы
     * @return Парсер / синтаксичесий анализатор
     */
    public JsParser parser(){
        if( parser!=null )return parser;
        parser = new JsParser();
        return parser;
    }

    /**
     * Получение списка лексем
     * @param source исходный код
     * @param from с какой позиции (индекса) начать анализ
     * @return лексемы
     */
    public List<? extends CToken> tokens(String source,int from){
        if( source==null )throw new IllegalArgumentException("source==null");
        if( from<0 )throw new IllegalArgumentException("from<0");
        return lexer().tokens(source,from);
    }

    /**
     * Строит указатель по лексемам
     * @param source исходный код
     * @param from с какой позиции (индекса) начать анализ
     * @return указатель на лексемы
     */
    public TPointer tpointer(String source, int from){
        if( source==null )throw new IllegalArgumentException("source==null");
        if( from<0 )throw new IllegalArgumentException("from<0");
        return new TPointer( tokens(source,from) );
    }

    /**
     * Парсинг исходников
     * @param source исходники
     * @param from позиция в исходном тексте
     * @return AST дерево
     */
    public AST parse(String source, int from){
        Optional<AST> ast = parser().expression.apply(tpointer(source,from));
        if( ast==null || !ast.isPresent() ){
            throw new ParseError("can't parse source, offset="+from+" source:\n"+source);
        }
        return ast.get();
    }

    /**
     * Парсинг исходников
     * @param tokens Лексемы
     * @return AST дерево
     */
    public AST parse(List<? extends CToken> tokens){
        if( tokens==null )throw new IllegalArgumentException("tokens==null");
        Optional<AST> ast = parser().expression.apply(new TPointer(tokens));
        if( ast==null || !ast.isPresent() ){
            throw new ParseError("can't parse source, tokens:\n"+tokens);
        }
        return ast.get();
    }

    /**
     * Парсинг исходников
     * @param source исходники
     * @return AST дерево
     */
    public AST parse(String source){
        if( source==null )throw new IllegalArgumentException("source==null");
        return parse(source,0);
    }

    protected EvalContext context;

    /**
     * Возвращает контекст интерпретации
     * @return контекст
     */
    public EvalContext context(){
        if( context!=null )return context;
        context = new EvalContext();

        context.bind("undefined", Undef.instance);
        context.bind("NaN", Double.NaN);

        context.bindStaticMethods(UnaryMinusOperator.class);
        context.bindStaticMethods(NotOperator.class);
        context.bindStaticMethods(OrOperator.class);
        context.bindStaticMethods(AndOperator.class);
        context.bindStaticMethods(BitOrOperator.class);
        context.bindStaticMethods(BitAndOperator.class);
        context.bindStaticMethods(BitXorOperator.class);
        context.bindStaticMethods(StrongEqualsOperator.class);
        context.bindStaticMethods(NotEqualsOperator.class);
        context.bindStaticMethods(MoreOrEqualsOperator.class);
        context.bindStaticMethods(MoreOperator.class);
        context.bindStaticMethods(LessOrEqualsOperator.class);
        context.bindStaticMethods(LessOperator.class);
        context.bindStaticMethods(EqualsOperator.class);
        context.bindStaticMethods(LShiftOpeartor.class);
        context.bindStaticMethods(RShiftOpeator.class);
        context.bindStaticMethods(RRShiftOperator.class);
        context.bindStaticMethods(AddOperator.class);
        context.bindStaticMethods(SubOperator.class);
        context.bindStaticMethods(MulOperator.class);
        context.bindStaticMethods(DivOperator.class);
        context.bindStaticMethods(ModuloOperator.class);
        context.bindStaticMethods(PowerOperator.class);

        context.bindFn( BasePreparingCalls.IMPLICIT, Float.class, Double.class, Float::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, float.class, Double.class, Float::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, Long.class, Double.class, Long::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, long.class, Double.class, Long::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, Integer.class, Double.class, Integer::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, int.class, Double.class, Integer::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, Short.class, Double.class, Short::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, short.class, Double.class, Short::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, Byte.class, Double.class, Byte::doubleValue);
        context.bindFn( BasePreparingCalls.IMPLICIT, byte.class, Double.class, Byte::doubleValue);

        context.scoring(
            new DefaultScrolling().
                impicitPower(2).
                covariantPower(3)
        );

        return context;
    }

    /**
     * Указывает контекст
     * @param conf конфигурация контекст
     * @return SELF ссылка
     */
    public JsEvaluator context( Consumer<EvalContext> conf ){
        if( conf==null )throw new IllegalArgumentException("conf==null");
        conf.accept(context());
        return this;
    }

    /**
     * Интерпретация AST дерева
     * @param ast дерево
     * @return результат интерпретации
     */
    public Object eval( AST ast ){
        if( ast==null )throw new IllegalArgumentException("ast==null");
        Eval eval = new Eval(context());
        return eval.eval(ast);
    }

    /**
     * Интерпретация
     * @param source исходный текст
     * @return результат интерпретации
     */
    public Object eval( String source ){
        if( source==null )throw new IllegalArgumentException("source==null");
        return eval( parse(source,0) );
    }
}
