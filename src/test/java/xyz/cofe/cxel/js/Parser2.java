package xyz.cofe.cxel.js;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.ParseError;
import xyz.cofe.cxel.ast.*;
import xyz.cofe.cxel.parse.*;
import xyz.cofe.cxel.tok.*;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.ProxyGR;
import xyz.cofe.text.tparse.TPointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xyz.cofe.cxel.Parser.atomic;
import static xyz.cofe.cxel.Parser.dummy;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Parser2 extends BaseParser {
    //region Литералы
    /** Литерал - целое число */
    public final GR<TPointer, AST> intNumber = (GR)atomic( IntegerNumberTok.class, NumberAST::new ).name("intNumber");
    /** Литерал - дробное число */
    public final GR<TPointer, AST> floatNumber = (GR)atomic( FloatNumberTok.class, NumberAST::new ).name("floatNumber");
    /** Литерал - число */
    public final GR<TPointer, AST> number = floatNumber.another(intNumber).map( t->(AST)t ).name("number");
    /** Литерал - булево */
    public static class GRBool implements GR<TPointer, AST> {
        @Override
        public Optional<AST> apply( TPointer ptr ){
            Optional<CToken> tok = ptr.lookup(0);
            if (tok.isPresent() && tok.get() instanceof KeywordTok ) {
                Keyword k = ((KeywordTok) tok.get()).keyword();
                if (k != null) {
                    if (k == Keyword.True || k == Keyword.False) {
                        return Optional.of(new BooleanAST(ptr, (KeywordTok) tok.get()));
                    }
                }
            }
            return Optional.empty();
        }
    }
    /** Литерал - булево */
    public final GR<TPointer, AST> bool = new GRBool();
    /** Литерал - null */
    public static class GRNull implements GR<TPointer, AST> {
        @Override
        public Optional<AST> apply( TPointer ptr ){
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
        }
    }
    /** Литерал - null */
    public final GR<TPointer, AST> nullz = new GRNull();
    /** Литерал - строка */
    public final GR<TPointer,AST> string = (GR)atomic(StringTok.class, StringAST::new).name("string");
    /** Литерал - литерал */
    public final GR<TPointer,AST> literal = number.another(bool).another(nullz).another(string).map().name("literal");
    //endregion

    /** Выражение */
    public final ProxyGR<TPointer,AST> expression = new ProxyGR<>(dummy()).name("expression");

    //region  Правая рекурсия: значение 2 / 3 * 4 (=2.666) будет интерпретированно как 2 / ( 3 * 4 ) (=0.166)
    //
    //    public final ProxyGR<TPointer,AST> mulDiv1 = new ProxyGR<>(dummy());
    //    public final GR<TPointer,AST> mulDiv =
    //        literal.next(Keyword.parserOf(Keyword.Multiple,Keyword.Divide)).next(mulDiv1)
    //            .map(BinaryOpAST::new)
    //            .another(literal).map(t->(AST)t);
    //    { mulDiv1.setTarget(mulDiv); }
    //endregion
    //region Нормальная левая "рекурсия" 2 / 3 * 4 будет ( 2 / 3 ) * 4
    // Первый вариант - Нормальная левая "рекурсия" 2 / 3 * 4 будет ( 2 / 3 ) * 4
    /*
    public GR<TPointer,AST> mulDiv2 = new GR<TPointer, AST>() {
        private GR<TPointer,KeywordAST> gKW = Keyword.parserOf(Keyword.Multiple,Keyword.Divide);

        @Override
        public Optional<AST> apply( TPointer ptr ){
            GR<TPointer,AST> gLeft = literal;
            GR<TPointer,AST> gRight = literal;

            Optional<AST> left = gLeft.apply(ptr);
            if( !left.isPresent() )return Optional.empty();

            BinaryOpAST op = null;

            while( true ){
                Optional<KeywordAST> kw = gKW.apply(op!=null ? op.end() : left.get().end());
                if( !kw.isPresent() ) return op != null ? Optional.of(op) : left;

                Optional<AST> right = gRight.apply(kw.get().end());
                if( !right.isPresent() ) return left;

                if( op == null ){
                    op = new BinaryOpAST(left.get(), kw.get(), right.get());
                }else{
                    op = new BinaryOpAST(op, kw.get(), right.get());
                }
            }
        }
    };
    */
    //endregion
    //region Нормальня левая "рекурсия" через грамматику
    /*
    public final GR<TPointer,AST> mulDiv =
        literal.next(
            Keyword.parserOf(Keyword.Multiple,Keyword.Divide).next( literal )
                .map(BinaryRight::new).repeat().map(TailRights::new)
        ).map( (head,tail) -> {
            BinaryOpAST op = null;
            for( BinaryRight p : tail.tail ){
                if( op==null ){
                    op = new BinaryOpAST( head, p.op, p.right );
                }else{
                    op = new BinaryOpAST( op, p.op, p.right );
                }
            }
            return op;
        }).another( literal ).map( t->(AST)t );
    */

    /**
     * Оператор и правая часть, т.е. для выражения a + b соответствует + b
     */
    public static class BinaryRight extends ASTBase<BinaryRight> {
        public final KeywordAST op;
        public final AST right;
        public BinaryRight( KeywordAST op, AST right ){
            this.op = op;
            this.right = right;
            begin = op.begin();
            end = right.end();
        }

        @Override public BinaryRight clone(){ return new BinaryRight(op, right); }
        @Override public AST[] children(){ return new AST[0]; }
    }

    /**
     * Последовательность равноценных операторов,
     * т.е. для a + b - c + d соответствует + b - c + d
     */
    public static class TailRights extends ASTBase<TailRights> {
        final List<BinaryRight> tail;
        public TailRights( List<BinaryRight> tail ){
            this.tail = tail;
            TPointer b = null;
            TPointer e = null;
            for( BinaryRight i : tail ){
                b = b==null ? i.begin() : ( i.begin().compareTo(b)<0 ? i.begin() : b );
                e = e==null ? i.end() : ( i.end().compareTo(e)>0 ? i.end() : e );
            }
            begin = b;
            end = e;
        }
        @Override public TailRights clone(){ return new TailRights(tail); }
        @Override public AST[] children(){ return new AST[0]; }
    }

    /**
     * Консутруирование бинарного лево-рекурсивного оператора
     * @param left левый операнд
     * @param right правый операнд
     * @param alt алтернатива
     * @param operators оператор(ы)
     * @return бинарный оператор
     */
    public static GR<TPointer,AST> binaryOp( GR<TPointer,AST> left, GR<TPointer,AST> right, GR<TPointer,AST> alt, Keyword ... operators ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");
        if( alt==null )throw new IllegalArgumentException("alt==null");

        if( operators==null )throw new IllegalArgumentException("operators==null");
        if( operators.length<1 )throw new IllegalArgumentException("operators.length<1");
        return
            left.next( Keyword.parserOf(operators).next( right )
                .map(BinaryRight::new).repeat().map(TailRights::new)
            ).map( (head,tail) -> {
                BinaryOpAST op = null;
                for( BinaryRight p : tail.tail ){
                    if( op==null ){
                        op = new BinaryOpAST( head, p.op, p.right );
                    }else{
                        op = new BinaryOpAST( op, p.op, p.right );
                    }
                }
                return op;
            }).another( alt ).map( t->(AST)t );
    }

    /**
     * Консутруирование бинарного лево-рекурсивного оператора
     * @param left левый операнд
     * @param right правый операнд
     * @param operators оператор(ы)
     * @return бинарный оператор
     */
    public static GR<TPointer,AST> binaryOp( GR<TPointer,AST> left, GR<TPointer,AST> right, Keyword ... operators ){
        if( left==null )throw new IllegalArgumentException("left==null");
        if( right==null )throw new IllegalArgumentException("right==null");

        if( operators==null )throw new IllegalArgumentException("operators==null");
        if( operators.length<1 )throw new IllegalArgumentException("operators.length<1");

        return binaryOp( left, right, left, operators );
    }

    /**
     * Консутруирование бинарного лево-рекурсивного оператора
     * @param left операнд
     * @param operators оператор(ы)
     * @return бинарный оператор
     */
    public static GR<TPointer,AST> binaryOp( GR<TPointer,AST> left, Keyword ... operators ){
        if( left==null )throw new IllegalArgumentException("left==null");

        if( operators==null )throw new IllegalArgumentException("operators==null");
        if( operators.length<1 )throw new IllegalArgumentException("operators.length<1");

        return binaryOp( left, left, left, operators );
    }
    //endregion

    //region Атомарные конструкции
    //region parenthes - скобки
    /** Круглые скобки */
    public final GR<TPointer,AST> parenthes = (GR)(Keyword.OpenParenthes.parser().next( expression ).next( Keyword.CloseParenthes.parser() ).map(
        ParenthesAST::new
    ).name("parenthes"));
    //endregion
    //region Унарные опреаторы
    /** Унарный оператор */
    public final GR<TPointer,AST> unaryExression = Keyword.parserOf(
        Keyword.Minus,
        Keyword.Plus,
        Keyword.Not,
        Keyword.Tilde,
        Keyword.Delete,
        Keyword.Void,
        Keyword.TypeOf
    ).next(expression).map((op, vl) -> new UnaryOpAST(op.begin(), vl.end(), op, vl));
    //endregion
    //region varRef - Указатель на переменную
    public final GR<TPointer,AST> varRef = atomic( IdTok .class, VarRefAST::new);
    //endregion
    //region map - Карта значений
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
    //endregion
    //region list - Список значений
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
    //endregion
    //region atom - первичная "атомарная" конструкция
    /** первичная "атомарная" конструкция */
    public final GR<TPointer,AST> atom =
        parenthes
            //.another( unaryExression )
            //.another( varRef )
            //.another( list(expression) )
            //.another( map(literal, expression) )
            .another( literal )
            .map( t->(AST)t ).name("atom");
    //endregion
    //endregion

    //region Проходноой вариант expression v1
    //    public final GR<TPointer,AST> expression1 =
    //        atom.next( Keyword.parserOf( Keyword.Multiple, Keyword.Divide) ).next( expression ).map( BinaryOpAST::new ).name("*/")
    //        .another( atom.next(Keyword.parserOf( Keyword.Plus, Keyword.Minus)).next(expression).map(BinaryOpAST::new).name("+/") )
    //        .another( atom ).map( t->(AST)t );
    //
    //    { expression.setTarget(expression1); }
    //endregion

    //region postfix
    /**
     * Раширение {@link #atom} до операций:
     * <ul>
     *  <li> доступа к полю объекта </li>
     *  <li> вызов метода </li>
     *  <li> доступ к элементу массива </li>
     * </ul>
     * postfix ::= {@link #atom} { <br>
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
     * @param grBase ссылка на {@link #atom}
     * @return Расширение {@link #atom}
     */
    @SuppressWarnings({ "UnnecessaryLocalVariable", "UnnecessaryContinue" })
    public static GR<TPointer, AST> postfix( GR<TPointer, AST> grBase, GR<TPointer, AST> expr ){
        if( grBase==null )throw new IllegalArgumentException( "grBase==null" );
        if( expr==null )throw new IllegalArgumentException("expr==null");

        GR<TPointer, AST> reslt = ptrStart -> {
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

                        Optional<AST> arg = expr.apply(p);
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
                    Optional<AST> idxOffAst = expr.apply(ptr.move(1));
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
    //endregion
    public final GR<TPointer,AST> primary = postfix( atom, expression );
    //public final GR<TPointer,AST> power = binaryOp( primary, Keyword.Power );
    //public final GR<TPointer,AST> mulDiv = binaryOp( power, Keyword.Multiple, Keyword.Divide, Keyword.Modulo );
    public final GR<TPointer,AST> mulDiv = binaryOp( primary, Keyword.Multiple, Keyword.Divide, Keyword.Modulo );
    public final GR<TPointer,AST> addSub = binaryOp( mulDiv, Keyword.Plus, Keyword.Minus );
    { expression.setTarget(addSub); }
//    public final GR<TPointer,AST> bitShift = binaryOp( addSub, Keyword.BitLeftShift, Keyword.BitRightShift, Keyword.BitRRightShift );
//    public final GR<TPointer,AST> compare = bitShift.next( Keyword.parserOf(
//        Keyword.Less,
//        Keyword.LessOrEquals,
//        Keyword.More,
//        Keyword.MoreOrEquals,
//        Keyword.In,
//        Keyword.InstanceOf
//    ) ).next( bitShift ).map( BinaryOpAST::new );
//    public final GR<TPointer,AST> equals = compare.next( Keyword.parserOf(
//        Keyword.Equals,
//        Keyword.NotEquals,
//        Keyword.StrongEquals,
//        Keyword.StrongNotEquals
//    ) ).next( compare ).map( BinaryOpAST::new );
//    public final GR<TPointer,AST> bitAnd = binaryOp( equals, Keyword.BitAnd );
//    public final GR<TPointer,AST> bitXor = binaryOp( bitAnd, Keyword.BitXor );
//    public final GR<TPointer,AST> bitOr = binaryOp( bitXor, Keyword.BitOr );
//    public final GR<TPointer,AST> and = binaryOp( bitOr, Keyword.And );
//    public final GR<TPointer,AST> or = binaryOp( and, Keyword.Or );
//    public final GR<TPointer,AST> ifOp = or.next(Keyword.Question.parser())
//        .next(or)
//        .next(Keyword.Colon.parser())
//        .next(or)
//        .map((cond, k_qst, succ, k_cln, fail) -> new IfAST(
//            cond.begin(), fail.end(), cond, succ, fail)
//        );
//
//    { expression.setTarget(ifOp); }
}
