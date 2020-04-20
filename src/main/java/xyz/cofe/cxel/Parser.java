package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Парсинг AST
 */
public class Parser {
    //region cache
    protected Map<String,Object> cache = new LinkedHashMap<>();
    protected synchronized <U> U cache(String key, Supplier<U> fn){
        if( key==null )throw new IllegalArgumentException("key==null");
        if( fn==null )throw new IllegalArgumentException("fn==null");
        if( cache.containsKey(key) ){
            return (U)cache.get(key);
        }
        U u = fn.get();
        cache.put(key,u);
        return u;
    }
    //endregion

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
    protected static GR<TPointer,AST> dummyInst = null;
    /**
     * Заглушка
     */
    public static GR<TPointer,AST> dummy() {
        if( dummyInst!=null )return dummyInst;
        dummyInst = ptr -> Optional.empty();
        return dummyInst;
    }
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
    public GR<TPointer, AST> intNumber(){
        return cache( "intNumber", ()-> atomic( IntegerNumberTok.class, NumberAST::new ) );
    }

    /**
     * Парсинг плавующего числа
     */
    public GR<TPointer, AST> floatNumber(){
        return cache( "floatNumber", ()-> atomic( FloatNumberTok.class, NumberAST::new ) );
    }

    /**
     * Парсинг числа
     */
    public GR<TPointer, AST> number(){
        return cache( "number", ()-> floatNumber().another(intNumber()).map( t->(AST)t ) );
    }
    //endregion
    //region bool : GR
    /**
     * Парсинг булево
     */
    public GR<TPointer, AST> bool() {
        return cache( "bool", ()-> ptr -> {
            Optional<CToken> tok = ptr.lookup(0);
            if (tok.isPresent() && tok.get() instanceof KeywordTok) {
                Keyword k = ((KeywordTok) tok.get()).keyword();
                if (k != null) {
                    if (k == Keyword.True || k == Keyword.False) {
                        return Optional.of(new BooleanAST(ptr, (KeywordTok) tok.get()));
                    }
                }
            }
            return Optional.empty();
        } );
    }
    //endregion
    //region null : GR
    /**
     * Парсинг null значение
     */
    public GR<TPointer, AST> nullConst(){
        return cache( "nullConst", ()-> ptr -> {
            Optional<CToken> tok = ptr.lookup(0);
            if( tok.isPresent() && tok.get() instanceof KeywordTok ){
                Keyword k = ((KeywordTok)tok.get()).keyword();
                if( k!=null ){
                    if( k==Keyword.Null ){
                        return Optional.of( new NullAST(ptr, (KeywordTok)tok.get()) );
                    }
                }
            }
            return Optional.empty();
        } );
    }
    //endregion
    //endregion

    //region expression() - Выражение
    /**
     * Выражение <br>
     * expression ::= {@link #binaryOps}
     */
    protected final ProxyGR<TPointer,AST> expression = new ProxyGR<>(dummy());
    public ProxyGR<TPointer,AST> expression(){
        return expression;
    }
    //endregion

    //region Унарные операции
    /**
     * Унарная операция <br>
     * unaryExression ::=
     * ( {@link Keyword#Minus '-'}
     * | {@link Keyword#Not '!'}
     * )
     * {@link #expression}
     */
    public GR<TPointer, ? extends AST> unaryExression() {
        return cache( "unaryExression", ()-> Keyword.parserOf(
            Keyword.Minus,
            Keyword.Plus,
            Keyword.Not,
            Keyword.Tilde,
            Keyword.Delete,
            Keyword.Void,
            Keyword.TypeOf
        ).next(expression).map((op, vl) -> new UnaryOpAST(op.begin(), vl.end(), op, vl)) );
    }
    //endregion

    //region Атомарные значения
    /**
     * Скобочное выражение <br>
     * bracketExpression ::=
     *   {@link Keyword#OpenParenthes '('}
     *   {@link #expression}
     *   {@link Keyword#CloseParenthes ')'}
     */
    public GR<TPointer, ? extends AST> bracketExpression() {
        return cache( "bracketExpression", ()-> Keyword.OpenParenthes.parser().next(expression()).next(Keyword.CloseParenthes.parser())
            .map((l, e, r) -> e) );
    }

    /**
     * Указатель на переменную <br>
     * varRef ::= {@link IdTok} <br>
     * Входит в {@link #atomValue}
     */
    public GR<TPointer, ? extends AST> varRef() {
        return cache( "varRef", ()-> atomic(IdTok.class, VarRefAST::new) );
    }

    /**
     * Список значений <br>
     * list ::=
     *   {@link Keyword#OpenBracket '['}
     *   [ {@link #expression}
     *     {
     *         {@link Keyword#Comma ','}
     *         {@link #expression}
     *     }
     *   ]
     *   {@link Keyword#CloseBracket ']'}
     * @param expr ссылка на правило {@link #expression}
     * @return Правило создания списка
     */
    public static GR<TPointer, ? extends AST> list( GR<TPointer, ? extends AST> expr ){
        if( expr==null )throw new IllegalArgumentException("expr==null");
        return ptr -> {
            if( !Keyword.OpenBracket.match( ptr.lookup(0) ) )return Optional.empty();

            TPointer begin = ptr;
            TPointer end = null;
            List<AST> expLst = new ArrayList<>();

            ptr = ptr.move(1);
            if( Keyword.CloseBracket.match(ptr.lookup(0)) ){
                end = ptr.move(1);
            }else {
                while (true) {
                    if (ptr.eof()) return Optional.empty();

                    Optional<? extends AST> exp = expr.apply(ptr);
                    if (exp == null || !exp.isPresent()) {
                        break;
                    }

                    if (Keyword.Comma.match(exp.get().end().lookup(0))) {
                        expLst.add(exp.get());
                        ptr = exp.get().end().move(1);
                        continue;
                    }

                    if (Keyword.CloseBracket.match(exp.get().end().lookup(0))) {
                        expLst.add(exp.get());
                        end = exp.get().end().move(1);
                        break;
                    }

                    throw new ParseError(
                        "await Keyword.Comma or Keyword.CloseBracket at "+
                            exp.get().end()
                    );
                }
            }

            if( end!=null ){
                return Optional.of( new ListAST(begin,end,expLst) );
            }

            return Optional.empty();
        };
    }

    /**
     * Карта значений
     * @param literal литеральное значение
     * @param expr выражение
     * @return парсер правила
     */
    public static GR<TPointer, ? extends AST> map(
        GR<TPointer, ? extends AST> literal,
        GR<TPointer, ? extends AST> expr
    ){
        if( literal==null )throw new IllegalArgumentException("literal==null");
        if( expr==null )throw new IllegalArgumentException("expr==null");
        return startPtr -> {
            if( !Keyword.OpenBrace.match(startPtr.lookup(0)) )return Optional.empty();
            if( Keyword.CloseBrace.match(startPtr.lookup(1)) ){
                return Optional.of(
                    new MapAST( startPtr, startPtr.move(2), new MapEntryAST[0] )
                );
            }

            TPointer end = null;
            TPointer ptr = startPtr.move(1);
            List<MapEntryAST<?,?>> entries = new ArrayList<>();

            while (true){
                Optional<CToken> key = ptr.lookup(0);
                if( key==null || !key.isPresent() )break;

                CToken tkey = key.get();
                if( tkey instanceof IdTok ) {
                    if (Keyword.Colon.match(ptr.lookup(1))) {
                        Optional<? extends AST> exp = expr.apply(ptr.move(2));
                        if( exp!=null && exp.isPresent() ){
                            VarRefAST propId = new VarRefAST(ptr, (IdTok) tkey);
                            AST aexp = exp.get();

                            MapEntryAST<?,?> entry = new MapPropEntreyAST(ptr,aexp.end(),propId,aexp);
                            entries.add(entry);

                            if( Keyword.Comma.match(entry.end().lookup(0)) ){
                                ptr = entry.end().move(1);
                                continue;
                            }
                            if( Keyword.CloseBrace.match(entry.end().lookup(0)) ){
                                end = entry.end().move(1);
                                break;
                            }
                            throw new ParseError(
                                "await Keyword.Comma or Keyword.CloseBracket at "+
                                    entry.end()
                            );
                        }
                    }
                }

                if( Keyword.OpenParenthes.match(tkey) ){
                    Optional<? extends AST> keyExp = expr.apply(ptr.move(1));
                    if( keyExp!=null && keyExp.isPresent() ){
                        if( !Keyword.CloseParenthes.match(keyExp.get().end().lookup(0)) ){
                            throw new ParseError(
                                "await Keyword.CloseParenthes at "+
                                    keyExp.get().end()
                            );
                        }

                        if( !Keyword.Colon.match(keyExp.get().end().lookup(1)) ){
                            throw new ParseError(
                                "await Keyword.Colon at "+
                                    keyExp.get().end().move(1)
                            );
                        }

                        Optional<? extends AST> exp = expr.apply(keyExp.get().end().move(2));
                        if( exp!=null && exp.isPresent() ){
                            AST aexp = exp.get();

                            MapEntryAST<?,?> entry = new MapExprEntreyAST(
                                ptr,
                                aexp.end(),
                                keyExp.get(),
                                aexp
                            );
                            entries.add(entry);

                            if( Keyword.Comma.match(entry.end().lookup(0)) ){
                                ptr = entry.end().move(1);
                                continue;
                            }
                            if( Keyword.CloseBrace.match(entry.end().lookup(0)) ){
                                end = entry.end().move(1);
                                break;
                            }

                            throw new ParseError(
                                "await Keyword.Comma or Keyword.CloseBracket at "+
                                    entry.end()
                            );
                        }
                    }
                }

                Optional<? extends AST> oliteral = literal.apply(ptr);
                if( oliteral!=null && oliteral.isPresent() ){
                    AST keyExp = oliteral.get();
                    if (Keyword.Colon.match(keyExp.end().lookup(0))) {
                        Optional<? extends AST> exp = expr.apply(keyExp.end().move(1));
                        if( exp!=null && exp.isPresent() ){
                            LiteralAST lAst = (LiteralAST) keyExp;
                            AST aexp = exp.get();
                            MapEntryAST<?,?> entry = new MapLiteralEntreyAST(
                                ptr,
                                aexp.end(),
                                lAst,
                                aexp
                            );
                            entries.add(entry);

                            if( Keyword.Comma.match(entry.end().lookup(0)) ){
                                ptr = entry.end().move(1);
                                continue;
                            }
                            if( Keyword.CloseBrace.match(entry.end().lookup(0)) ){
                                end = entry.end().move(1);
                                break;
                            }

                            throw new ParseError(
                                "await Keyword.Comma or Keyword.CloseBracket at "+
                                    entry.end()
                            );
                        }
                    }
                }

                break;
            }

            if( end!=null ){
                MapAST mapAST = new MapAST(startPtr, end, entries);
                return Optional.of(mapAST);
            }

            return Optional.empty();
        };
    }

    /**
     * Литеральные значения
     */
    public GR<TPointer,? extends AST> literal() {
        return cache( "literal", ()-> nullConst().<AST>another(
            number()
        ).<TPointer, AST>another(
            bool()
        ).<TPointer, AST>another(
            atomic(StringTok.class, StringAST::new)
        ).map(t -> (AST) t) );
    }

    /**
     * Атомарное значение <br>
     * atomValue ::=
     *     {@link #bracketExpression} <br>
     *   | {@link #number} <br>
     *   | {@link #bool} <br>
     *   | {@link #nullConst} <br>
     *   | {@link StringTok} <br>
     *   | {@link #varRef} <br>
     *   | {@link #unaryExression}
     */
    public GR<TPointer,? extends AST> atomValue() {
        return cache( "atomValue", ()-> bracketExpression()
            .another(list(expression))
            .another(map(
                literal(),
                expression
            ))
            .another(literal())
            .another(varRef())
            .another(unaryExression())
            .map(t -> (AST) t));
    }
    //endregion

    //region Бинарные операторы, в порядке уменьшения приоритета
    //region postfix
    /**
     * Раширение {@link #atomValue} до операций:
     * <ul>
     *  <li> доступа к полю объекта </li>
     *  <li> вызов метода </li>
     *  <li> доступ к элементу массива </li>
     * </ul>
     * postfix ::= {@link #atomValue} { <br>
     * {@link Keyword#Dot '.'} {@link IdTok} <br>
     *
     * | {@link Keyword#OpenParenthes '('}
     *   [
     *       {@link #expression}
     *       {
     *         {@link Keyword#Comma ','}
     *         {@link #expression}
     *       }
     *   ]
     *   {@link Keyword#CloseParenthes ')'} <br>
     *
     * | {@link Keyword#OpenBracket '['}
     *   {@link #expression}
     *   {@link Keyword#CloseBracket ']'}
     * <br>
     *
     * }
     * @param grBase ссылка на {@link #atomValue}
     * @return Расширение {@link #atomValue}
     */
    @SuppressWarnings({ "UnnecessaryLocalVariable", "UnnecessaryContinue" })
    public GR<TPointer, ? extends AST> postfix( GR<TPointer, ? extends AST> grBase){
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

                        Optional<AST> arg = expression().apply(p);
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
        return cache( "postfix", ()-> reslt );
    }
    //endregion

    /**
     * Создает последовательность бинарных операторов.
     * <p>
     *     Чтоб не писать последовательность однотипных операторов
     *     показаных ниже
     * </p>
     * <pre>
     * public static final GR<TPointer, ? extends AST> mulDiv
     *   = binaryOp(
     *        postfix(atomValue),
     *        Keyword.parserOf( Keyword.Multiple, Keyword.Divide ),
     *        postfix(atomValue));
     *
     * public static final GR<TPointer, ? extends AST> plusMinus
     *   = binaryOp(
     *       mulDiv,
     *       Keyword.parserOf( Keyword.Plus, Keyword.Minus ),
     *       mulDiv);
     *
     * public static final GR<TPointer, ? extends AST> shift
     *   = binaryOp(
     *       plusMinus,
     *       Keyword.parserOf(
     *         Keyword.BitLeftShift,
     *         Keyword.BitRightShift,
     *         Keyword.BitRRightShift ),
     *       plusMinus);
     * </pre>
     *
     * Всю эту последовательность можно свернуть,
     * где начальное правило (аргмент start) в данном примере это - postfix(atomValue).
     * <p>
     *
     * А последовательность/приоритет операторов в данном случаи это:
     *
     * <pre>
     * [ Keyword.parserOf( Keyword.Multiple, Keyword.Divide )
     * , Keyword.parserOf( Keyword.Plus, Keyword.Minus )
     * , Keyword.parserOf(
     *      Keyword.BitLeftShift,
     *      Keyword.BitRightShift,
     *      Keyword.BitRRightShift )
     * ]
     * </pre>
     *
     * @param start Начальное правило
     * @param orderKeywords Имена операторов в последовательности уменьшения приоритета
     * @return Бинарные операторы
     */
    @SafeVarargs
    public static GR<TPointer, ? extends AST> binaryOps(
        GR<TPointer, ? extends AST> start,
        GR<TPointer,KeywordAST> ... orderKeywords
    ) {
        if( start==null )throw new IllegalArgumentException("start==null");
        if( orderKeywords==null )throw new IllegalArgumentException("orderKeywords==null");

        GR<TPointer, ? extends AST> gr = start;
        for( GR<TPointer,KeywordAST> kw : orderKeywords ){
            gr = binaryOp( gr, kw, gr );
        }

        return gr;
    }

    /**
     * Бинарные операции
     */
    public GR<TPointer, ? extends AST> binaryOps() {
        return cache( "binaryOps", ()-> binaryOps(
            postfix(atomValue())
            , Keyword.Power.parser()
            , Keyword.parserOf(Keyword.Multiple, Keyword.Divide, Keyword.Modulo)
            , Keyword.parserOf(Keyword.Plus, Keyword.Minus)
            , Keyword.parserOf(Keyword.BitLeftShift, Keyword.BitRightShift, Keyword.BitRRightShift)
            , Keyword.parserOf(
                Keyword.Less,
                Keyword.LessOrEquals,
                Keyword.More,
                Keyword.MoreOrEquals,
                Keyword.In,
                Keyword.InstanceOf
            )
            , Keyword.parserOf(
                Keyword.Equals,
                Keyword.NotEquals,
                Keyword.StrongEquals,
                Keyword.StrongNotEquals
            )
            , Keyword.BitAnd.parser()
            , Keyword.BitXor.parser()
            , Keyword.BitOr.parser()
            , Keyword.And.parser()
            , Keyword.Or.parser()
        ));
    }
    //endregion

    //region if - тернарный оператор
    public GR<TPointer, ? extends AST> ifOpMatch() {
        return cache( "ifOpMatch", ()-> binaryOps()
            .next(Keyword.Question.parser())
            .next(binaryOps())
            .next(Keyword.Colon.parser())
            .next(binaryOps())
            .map((cond, k_qst, succ, k_cln, fail) -> new IfAST(
                cond.begin(), fail.end(), cond, succ, fail)
            ));
    }

    public GR<TPointer, ? extends AST> ifOp() {
        return cache( "ifOp", ()-> ifOpMatch().<AST>another(binaryOps()).map());
    }
    //endregion

    //region Инициализация рекурсии expression
    {
        // expression.setTarget(binaryOps);
        expression().setTarget(ifOp());
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
        return new TPointer( new Lexer().tokens(source) );
    }

    /**
     * Создание указателя по лексемам
     * @param source исходный текст
     * @param from с какого символа (0 и больше) начинать парсинг
     * @return указатель по лексемам
     */
    public static TPointer source( String source, int from ){
        if( source==null )throw new IllegalArgumentException( "source==null" );
        return new TPointer( new Lexer().tokens(source, from) );
    }
    //endregion
}
