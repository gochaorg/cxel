package xyz.cofe.cxel.parse;

import xyz.cofe.cxel.Keyword;
import xyz.cofe.cxel.Lexer;
import xyz.cofe.cxel.ast.AST;
import xyz.cofe.cxel.ast.BinaryOpAST;
import xyz.cofe.cxel.ast.KeywordAST;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Базовый класс для парсеров
 */
@SuppressWarnings("FieldMayBeFinal")
public class BaseParser implements GRCache {
    //region GRCache
    protected Map<String,Object> cache = new LinkedHashMap<>();
    /**
     * Кэширование созданных функций
     * @param key ключ
     * @param fn функция
     * @param <U> вычисляемое значеие
     * @return функция
     */
    @SuppressWarnings("unchecked")
    public synchronized <U> U cache( String key, Supplier<U> fn){
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
     * Правило сопоставления лексем
     * @param <T> Тип лексемы лексера
     * @param <A> AST Тип лексемы парсера
     */
    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    public static class Atomic<T extends CToken, A extends AST> implements GR<TPointer,A>
    {
        private final Class<T> target;
        private final BiFunction<TPointer,T,A> map;

        public Atomic(Class<T> target, BiFunction<TPointer,T,A> map){
            if( target==null )throw new IllegalArgumentException("target==null");
            if( map==null )throw new IllegalArgumentException("map==null");
            this.target = target;
            this.map = map;
        }

        private String name;

        @Override
        public GR<TPointer, A> name( String name ){
            this.name = name;
            return this;
        }
        @Override public String name(){ return name; }

        @Override
        public String toString(){
            if( name!=null )return name;
            return super.toString();
        }

        @Override
        public Optional<A> apply( TPointer ptr ){
            CToken t = ptr.lookup(0).orElseGet( null );
            if( t!=null && target.isAssignableFrom(t.getClass()) ){
                return Optional.of( map.apply(ptr,(T)t) );
            }
            return Optional.empty();
        }
    }

    /**
     * Создает правило сопоставления для единичной лексемы вывод AST узла
     * @param target тип лексемы
     * @param map вывод AST из лексемы
     * @param <T> тип лексемы
     * @param <A> тип AST
     * @return правило
     */
    public static <T extends CToken, A extends AST>
    GR<TPointer,A> atomic( Class<T> target, BiFunction<TPointer,T,A> map ){
        if( target==null )throw new IllegalArgumentException("target == null");
        return new Atomic<>(target,map);
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

    //region binaryOp( operand, operators ) - Правило бинарного выражения
    /**
     * Правило бинарного выражения
     * @param operand операнд
     * @param operators операторы
     * @return правило
     */
    public GR<TPointer,AST> binaryOp( GR<TPointer,AST> operand, Keyword... operators ){
        return new GR<TPointer, AST>() {
            private String name;

            @Override
            public GR<TPointer, AST> name( String name ){
                this.name = name;
                return this;
            }

            @Override
            public String name(){
                return name;
            }

            @Override
            public String toString(){
                if( name!=null )return name;
                return super.toString();
            }

            private GR<TPointer, KeywordAST> gKW = Keyword.parserOf(operators);

            @SuppressWarnings("UnnecessaryLocalVariable")
            @Override
            public Optional<AST> apply( TPointer ptr ){
                GR<TPointer, AST> gLeft = operand;
                GR<TPointer, AST> gRight = operand;

                Optional<AST> left = gLeft.apply(ptr);
                if( !left.isPresent() ) return Optional.empty();

                BinaryOpAST op = null;

                while( true ){
                    Optional<KeywordAST> kw = gKW.apply(op != null ? op.end() : left.get().end());
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
    }
    //endregion
}
