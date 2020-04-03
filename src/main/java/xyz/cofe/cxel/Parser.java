package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.tok.FloatNumberTok;
import xyz.cofe.cxel.tok.IntegerNumberTok;
import xyz.cofe.text.tparse.*;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Парсинг AST
 */
public class Parser {
    //region atmoic( tokenClass, map2AST ) : GR

    /**
     * Создает правило сопоставления для единичной лексемы вывод AST узла
     * @param target тип лексемы
     * @param map вывод AST из лексемы
     * @param <T> тип лексемы
     * @param <A> тип AST
     * @return правило
     */
    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    public static <T extends CToken, A extends AST>
    GR<TPointer,A> atomic( Class<T> target, BiFunction<TPointer,T,A> map ){
        if( target==null )throw new IllegalArgumentException("target == null");
        return ptr -> {
            CToken t = ptr.lookup(0).orElseGet( null );
            if( t!=null && target.isAssignableFrom(t.getClass()) ){
                return Optional.of( map.apply(ptr,(T)t) );
            }
            return Optional.empty();
        };
    }
    //endregion

    //region dummy : GR
    /**
     * Заглушка
     */
    public static final GR<TPointer,AST> dummy = ptr -> Optional.empty();
    //endregion

    //region binaryOp( grLeft, operator, grRight ) : GR - Бинарный оператор
    /**
     * Бинарный оператор, соответствует BNF: <br>
     * <code>
     * grLeft { operator grRight }
     * </code>
     * @param grLeft Левая часть от оператора
     * @param operator Оператор
     * @param grRight Правая часть
     * @return Правило
     */
    public static GR<TPointer, ? extends AST> binaryOp(
        GR<TPointer,? extends AST> grLeft,
        GR<TPointer,? extends KeywordAST> operator,
        GR<TPointer,? extends AST> grRight
    ) {
        if( grLeft==null )throw new IllegalArgumentException( "grLeft==null" );
        if( operator==null )throw new IllegalArgumentException( "operator==null" );
        if( grRight==null )throw new IllegalArgumentException( "grRight==null" );

        return ptr -> {
            if( ptr==null || ptr.eof() )return Optional.empty();

            //TPointer beginPtr = ptr;

            Optional<? extends AST> left = grLeft.apply(ptr);
            if( !left.isPresent() )return Optional.empty();

            BinaryOpAST binOp = null;

            while ( true ) {
                Optional<? extends KeywordAST> op = operator.apply(
                    binOp==null ?
                        left.get().end() :
                        binOp.right().end()
                );
                if (!op.isPresent()) break;

                Optional<? extends AST> right = grRight.apply(op.get().end());
                if( !right.isPresent() )break;

                if( binOp==null ){
                    binOp = new BinaryOpAST(left.get(), op.get(), right.get());
                }else{
                    binOp = new BinaryOpAST(binOp, op.get(), right.get());
                }
            }

            if( binOp==null )return Optional.of(left.get());

            return Optional.of( binOp );
        };
    }
    //endregion

    //region number : GR
    /**
     * Парсинг целого числа
     */
    public static final GR<TPointer, AST> intNumber = atomic(
        IntegerNumberTok.class, NumberAST::new
    );

    /**
     * Парсинг плавующего числа
     */
    public static final GR<TPointer, AST> floatNumber = atomic(
        FloatNumberTok.class, NumberAST::new
    );

    /**
     * Парсинг числа
     */
    public static final GR<TPointer, AST> number = floatNumber.another(intNumber).map( t->(AST)t );
    //endregion

    /**
     * Выражение <br>
     * expression ::= {@link #plusMinusOperator}
     */
    public static final ProxyGR<TPointer,AST> expression = new ProxyGR<>(dummy);

    /**
     * Скобочное выражение <br>
     * bracketExpression ::=
     *   {@link Keyword#OpenBracket '('}
     *   {@link #expression}
     *   {@link Keyword#CloseBracket ')'}
     */
    public static final GR<TPointer, ? extends AST> bracketExpression
        = Keyword.OpenBracket.parser().next( expression ).next( Keyword.CloseBracket.parser() )
        .map( (l,e,r)->e );

    /**
     * Унарная операция <br>
     * unaryExression ::= {@link Keyword#Minus minus} {@link #expression}
     */
    public static final GR<TPointer, ? extends AST> unaryExression
        = Keyword.Minus.parser().next(expression).map( (op,vl)->new UnaryOpAST(op.begin(),vl.end(),op,vl));

    /**
     * Атомарное значение <br>
     * atomValue ::=
     *     {@link #bracketExpression}
     *   | {@link #number}
     *   | {@link #unaryExression}
     */
    public static final GR<TPointer,? extends AST> atomValue
        = bracketExpression
              .another(number)
              .another(unaryExression)
              .map( t -> (AST)t );

    /**
     * Оператор умножения/деления <br>
     * multipleDevideOperator ::= {@link #atomValue}
     * {
     *   ( {@link Keyword#Multiple mul} | {@link Keyword#Divide div} )
     *   {@link #atomValue}
     * }
     */
    public static final GR<TPointer, ? extends AST> multipleDevideOperator
        = binaryOp(
        atomValue,
        Keyword.parserOf( Keyword.Multiple, Keyword.Divide ),
        atomValue
    );

    /**
     * Оператор сложения/вычитания <br>
     * plusMinusOperator ::= {@link #multipleDevideOperator mulDiv}
     * {
     *   ( {@link Keyword#Plus plus} | {@link Keyword#Minus minus} )
     *   {@link #multipleDevideOperator mulDiv}
     * }
     */
    public static final GR<TPointer, ? extends AST> plusMinusOperator
        = binaryOp(
        multipleDevideOperator,
        Keyword.parserOf( Keyword.Plus, Keyword.Minus ),
        multipleDevideOperator
    );

    //region init expression
    static {
        expression.setTarget(plusMinusOperator);
    }
    //endregion

    //region source( src [, from ] ) : TPointer - лексический анализатор / указатель
    /**
     * Создание указателя по лексемам
     * @param source исходный текст
     * @return указатель по лексемам
     */
    public static TPointer source( String source ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        return new TPointer( Lexer.tokens(source) );
    }

    /**
     * Создание указателя по лексемам
     * @param source исходный текст
     * @param from с какого символа (0 и больше) начинать парсинг
     * @return указатель по лексемам
     */
    public static TPointer source( String source, int from ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        return new TPointer( Lexer.tokens(source, from) );
    }
    //endregion
}
