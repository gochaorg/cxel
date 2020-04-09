package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.*;

import java.util.ArrayList;
import java.util.List;
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

    //region Литеральные значения
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
    //region bool : GR
    /**
     * Парсинг булево
     */
    public static final GR<TPointer, AST> bool = ptr -> {
        Optional<CToken> tok = ptr.lookup(0);
        if( tok.isPresent() && tok.get() instanceof KeywordTok ){
            Keyword k = ((KeywordTok)tok.get()).keyword;
            if( k!=null ){
                if( k==Keyword.True || k==Keyword.False ){
                    return Optional.of( new BooleanAST(ptr, (KeywordTok)tok.get()) );
                }
            }
        }
        return Optional.empty();
    };
    //endregion
    //region null : GR
    /**
     * Парсинг null значение
     */
    public static final GR<TPointer, AST> nullConst = ptr -> {
        Optional<CToken> tok = ptr.lookup(0);
        if( tok.isPresent() && tok.get() instanceof KeywordTok ){
            Keyword k = ((KeywordTok)tok.get()).keyword;
            if( k!=null ){
                if( k==Keyword.Null ){
                    return Optional.of( new NullAST(ptr, (KeywordTok)tok.get()) );
                }
            }
        }
        return Optional.empty();
    };
    //endregion
    //endregion

    /**
     * Выражение <br>
     * expression ::= {@link #or}
     */
    public static final ProxyGR<TPointer,AST> expression = new ProxyGR<>(dummy);

    /**
     * Скобочное выражение <br>
     * bracketExpression ::=
     *   {@link Keyword#OpenParenthes '('}
     *   {@link #expression}
     *   {@link Keyword#CloseParenthes ')'}
     */
    public static final GR<TPointer, ? extends AST> bracketExpression
        = Keyword.OpenParenthes.parser().next( expression ).next( Keyword.CloseParenthes.parser() )
        .map( (l,e,r)->e );

    /**
     * Унарная операция <br>
     * unaryExression ::=
     * ( {@link Keyword#Minus '-'}
     * | {@link Keyword#Not '!'}
     * )
     * {@link #expression}
     */
    public static final GR<TPointer, ? extends AST> unaryExression
        = Keyword.parserOf( Keyword.Minus, Keyword.Not )
          .next(expression).map( (op,vl)->new UnaryOpAST(op.begin(),vl.end(),op,vl));

    /**
     * Указатель на переменную <br>
     * varRef ::= {@link IdTok}
     */
    public static final GR<TPointer, ? extends AST> varRef
        = atomic(IdTok.class, VarRefAST::new);

    /**
     * Атомарное значение <br>
     * atomValue ::=
     *     {@link #bracketExpression}
     *   | {@link #number}
     *   | {@link #bool}
     *   | {@link #nullConst}
     *   | {@link #varRef}
     *   | {@link #unaryExression}
     */
    public static final GR<TPointer,? extends AST> atomValue
        = bracketExpression
              .another(number)
              .another(bool)
              .another(nullConst)
              .another(atomic(StringTok.class,StringAST::new))
              .another(varRef)
              .another(unaryExression)
              .map( t -> (AST)t );

    public static GR<TPointer, ? extends AST> postfix(GR<TPointer, ? extends AST> grBase){
        if( grBase==null )throw new IllegalArgumentException( "grBase==null" );

        GR<TPointer, ? extends AST> reslt = ptrStart -> {
            Optional<? extends AST> obase = grBase.apply(ptrStart);
            if( obase==null || !obase.isPresent() )return Optional.empty();

            AST base = obase.get();
            TPointer ptr = base.end();

            while( true ){
                Optional<CToken> t0 = ptr.lookup(0);

                if( Keyword.Dot.match(t0) ){
                    Optional<CToken> t1 = ptr.lookup(1);
                    if( t1.isPresent() && t1.get() instanceof IdTok ){
                        PropertyAST prop = new PropertyAST(ptrStart, ptr.move(2), base, (IdTok) t1.get());
                        base = prop;
                        ptr = base.end();
                        continue;
                    }
                } else if( Keyword.OpenParenthes.match(t0) ){
                    boolean succ = true;
                    List<AST> args = new ArrayList<>();
                    TPointer p = ptr.move(1);
                    while( true ){
                        if( Keyword.CloseParenthes.match(p.lookup(0)) ){
                            p = p.move(1);
                            succ = true;
                            break;
                        }

                        Optional<AST> arg = expression.apply(p);
                        if( arg==null || !arg.isPresent() ){
                            succ = false;
                            break;
                        }

                        p = arg.get().end();
                        args.add( arg.get() );
                        if( Keyword.Comma.match(p.lookup(0)) ){
                            p = p.move(1);
                            continue;
                        }else if( Keyword.CloseParenthes.match(p.lookup(0))){
                            p = p.move(1);
                            succ = true;
                            break;
                        }
                    }
                    if( succ ){
                        CallAST call = new CallAST(base.end(), p, base, args);
                        base = call;
                        ptr = base.end();
                        continue;
                    }
                } else if( Keyword.OpenBracket.match(t0) ){
                    Optional<AST> idxOffAst = expression.apply(ptr.move(1));
                    if( idxOffAst.isPresent() && Keyword.CloseBracket.match(idxOffAst.get().end().lookup(0)) ){
                        IndexAST idxAst = new IndexAST(ptr,idxOffAst.get().end().move(1),base,idxOffAst.get());
                        base = idxAst;
                        ptr = base.end();
                        continue;
                    }
                }

                break;
            }

            return Optional.of(base);
        };
        return reslt;
    }

    //region Бинарные операторы, в порядке уменьшения приоритета
    /**
     * Оператор умножения/деления <br>
     * multipleDevideOperator ::= {@link #atomValue}
     * {
     *   ( {@link Keyword#Multiple mul} | {@link Keyword#Divide div} )
     *   {@link #atomValue}
     * }
     */
    public static final GR<TPointer, ? extends AST> mulDiv
        = binaryOp(
        postfix(atomValue),
        Keyword.parserOf( Keyword.Multiple, Keyword.Divide ),
        postfix(atomValue)
    );

    /**
     * Оператор сложения/вычитания <br>
     * plusMinusOperator ::= {@link #mulDiv mulDiv}
     * {
     *   ( {@link Keyword#Plus plus} | {@link Keyword#Minus minus} )
     *   {@link #mulDiv mulDiv}
     * }
     */
    public static final GR<TPointer, ? extends AST> plusMinus
        = binaryOp(
        mulDiv,
        Keyword.parserOf( Keyword.Plus, Keyword.Minus ),
        mulDiv
    );

    /**
     * Оператор сравнения <br>
     * compare ::= {@link #plusMinus}
     * {
     *   ( {@link Keyword#Less &lt;} |
     *     {@link Keyword#LessOrEquals &lt;=} |
     *     {@link Keyword#More &gt;} |
     *     {@link Keyword#MoreOrEquals &gt;=} |
     *     {@link Keyword#Equals ==} |
     *     {@link Keyword#NotEquals !=}
     *   )
     *   {@link #plusMinus}
     * }
     */
    public static final GR<TPointer, ? extends AST> compare
        = binaryOp(
        plusMinus,
        Keyword.parserOf(
            Keyword.Less,
            Keyword.LessOrEquals,
            Keyword.More,
            Keyword.MoreOrEquals,
            Keyword.Equals,
            Keyword.NotEquals ),
        plusMinus
    );

    /**
     * Оператор И <br>
     * and ::= {@link #compare}
     * {
     *   {@link Keyword#And}
     *   {@link #compare}
     * }
     */
    public static final GR<TPointer, ? extends AST> and
        = binaryOp(
        compare,
        Keyword.And.parser(),
        compare
    );

    /**
     * Оператор ИЛИ <br>
     * or ::= {@link #compare}
     * {
     *   {@link Keyword#Or}
     *   {@link #compare}
     * }
     */
    public static final GR<TPointer, ? extends AST> or
        = binaryOp(
        and,
        Keyword.Or.parser(),
        and
    );
    //endregion

    //region Инициализация рекурсии expression
    static {
        expression.setTarget(or);
        //expression.setTarget(postfix());
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
