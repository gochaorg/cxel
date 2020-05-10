package xyz.cofe.cxel;

import xyz.cofe.cxel.ast.KeywordAST;
import xyz.cofe.cxel.tok.KeywordTok;
import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.GR;
import xyz.cofe.text.tparse.TPointer;

import java.util.Arrays;
import java.util.Optional;

/**
 * Список ключевых слов
 */
public enum Keyword {
    /** Нулевая ссылка */ Null("null"),
    /** Булево - true */ True("true"),
    /** Булево - false */ False("false"),

    /**
     * Открытая круглая скобка
     */
    OpenParenthes("("),

    /**
     * Закрытая круглая скобка
     */
    CloseParenthes(")"),

    /** Открытая квадратная скобка */ OpenBracket("["),
    /** Закрытая квадратная скобка */ CloseBracket("]"),

    /** Открытая фигурная скобка */ OpenBrace("{"),
    /** Закрытая фигурная скобка */ CloseBrace("}"),

    /** Равенство */ Equals("=="),
    /** Равенство */ StrongEquals("==="),
    /** Не равенство */ NotEquals("!="),
    /** Не равенство */ StrongNotEquals("!=="),
    /** Меньше */ Less("<"),
    /** Больше */ More(">"),
    /** Меньше или равно */ LessOrEquals("<="),
    /** Больше или равно */ MoreOrEquals(">="),

    /** Вхождение */ In("in"),
    /** соответсвие типу */ InstanceOf("instanceof"),
    /** Получение типа */ TypeOf("typeof"),
    /** Без возврата значения */ Void("void"),
    /** Удаление ключа */ Delete("delete"),
    /** Создание объекта */ New("new"),

    /** Вопрос */ Question("?"),

    /** Битовое и */ BitAnd("&"),
    /** Битовое или */ BitOr("|"),
    /** Битовое или */ BitXor("^"),
    /** Битовый сдвиг */ BitLeftShift("<<"),
    /** Битовый сдвиг */ BitRightShift(">>"),
    /** Битовый сдвиг */ BitRRightShift(">>>"),

    /** Тильда */ Tilde("~"),

    /** Логическое Или */ Or("||"),
    /** Логическое Не */ Not("!"),
    /** Логическое И */ And("&&"),

    /** Запятая */ Comma(","),
    /** Двоеточие */ Colon(":"),

    /** Плюс */ Plus("+"),
    /** Минус */ Minus("-"),
    /** Делить */ Divide("/"),
    /** Множить */ Multiple("*"),
    /** Остаток от деления */ Modulo("%"),
    /** Возведение в степень */ Power("**"),
    /** Точка */ Dot(".")
    ;

    /**
     * Ключевое слово
     */
    public final String text;

    /**
     * Конструктор
     * @param text ключевое слово
     */
    Keyword( String text ){
        if( text==null )throw new IllegalArgumentException( "text==null" );
        if( text.length()<1 )throw new IllegalArgumentException( "text.length()<1" );
        if( KeywordImpl.predefined.contains(text) )throw new IllegalArgumentException(
            "text=\""+text+"\" already defined"
        );
        KeywordImpl.predefined.add(text);
        this.text = text;
    }

    //region lex(), lexer(), parser(), parsreOf() - различные грамматические парсеры
    private static Keyword[] ordDescByLen;

    /**
     * Ключевые слова отсортированные в порядке убывания длины
     * @return ключевые слова
     */
    public static Keyword[] keywordsByReverseLen(){
        if( ordDescByLen!=null )return ordDescByLen;
        Keyword[] kws = Arrays.copyOf(values(), values().length);
        Arrays.sort( kws, (a,b)-> -Integer.compare(a.text.length(), b.text.length() ) );
        ordDescByLen = kws;
        return ordDescByLen;
    }

    /**
     * Возвращает правило для лексичекого анализа данной лексемы
     * @return правило / парсер
     */
    public GR<CharPointer, KeywordTok> lex(){
        return ptr -> {
            for( int i=0; i<text.length(); i++ ){
                Optional<Character> chr = ptr.lookup(i);
                if( !chr.isPresent() )return Optional.empty();
                if( text.charAt(i)!=chr.get() )return Optional.empty();
            }
            return Optional.of( new KeywordTok(this,ptr,ptr.move(text.length())) );
        };
    }

    /**
     * Возвращает грамматическое правило лексичекого парсера
     * @return парсер
     */
    public static GR<CharPointer,KeywordTok> lexer(){
        return ptr -> {
            if( ptr.eof() )return Optional.empty();
            for( Keyword kw : keywordsByReverseLen() ){
                boolean matched = true;
                for( int i=0; i<kw.text.length(); i++ ){
                    Optional<Character> c = ptr.lookup(i);
                    if( !c.isPresent() ){
                        matched = false;
                        break;
                    }
                    if( c.get() != kw.text.charAt(i) ){
                        matched = false;
                        break;
                    }
                }
                if( matched ){
                    return Optional.of( new KeywordTok(kw, ptr, ptr.move(kw.text.length())) );
                }
            }
            return Optional.empty();
        };
    }

    public static class KeywordsGRParser implements GR<TPointer,KeywordAST> {
        private final Keyword[] keywords;

        public KeywordsGRParser(Keyword[] keywords){
            if( keywords==null )throw new IllegalArgumentException("keywords==null");
            if( Arrays.asList(keywords).contains(null) )throw new IllegalArgumentException("Arrays.asList(keywords).contains(null)");
            this.keywords = keywords;
        }

        @Override
        public Optional<KeywordAST> apply( TPointer ptr ){
            Optional<KeywordAST> res = null;
            for( Keyword k : keywords ){
                res = k.parser().apply(ptr);
                if( res!=null && res.isPresent() )return res;
            }
            return Optional.empty();
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("keywords: ");
            int c = -1;
            for( Keyword kw : keywords ){
                c++;
                if( c>0 )sb.append(", ");
                sb.append(kw.text);
            }
            return sb.toString();
        }
    }

    public static class KeywordGRParser implements GR<TPointer, KeywordAST> {
        private final Keyword keyword;
        public KeywordGRParser( Keyword keyword ){
            if( keyword==null )throw new IllegalArgumentException("keyword==null");
            this.keyword = keyword;
        }

        @Override
        public Optional<KeywordAST> apply( TPointer ptr ){
            Optional<CToken> tok = ptr.lookup(0);
            if( !tok.isPresent() )return Optional.empty();

            CToken t = tok.get();
            if( t instanceof KeywordTok ){
                if( ((KeywordTok) t).keyword().text.equals( keyword.text ) ){
                    return Optional.of( new KeywordAST(ptr, (KeywordTok)t) );
                }
            }

            return Optional.empty();
        }

        @Override
        public String toString(){
            return "keyword "+keyword.text;
        }
    }

    /**
     * Парсинг входящей цепочки на свопадение с указанной лексеммой
     * @return парсер
     */
    public GR<TPointer, KeywordAST> parser(){
        return new KeywordGRParser(this);
    }

    /**
     * Создание парсера для нескольких ключевых слов объеденных "альтернативой"
     * @param keywords ключевые слова
     * @return парсер
     */
    public static GR<TPointer,KeywordAST> parserOf( Keyword ... keywords ){
        if( keywords==null )throw new IllegalArgumentException( "keywords==null" );
        if( keywords.length<1 )throw new IllegalArgumentException( "keywords.length<1" );
        if( keywords.length==1 )return keywords[0].parser();
        return new KeywordsGRParser(keywords);
    }
    //endregion
    //region match() - Проверка на совпадение
    /**
     * Проверка - совпадает текущее ключевое слово с указанным токеном/лексеммой
     * @param cToken лексема
     * @return true - совпадает / false - не совпадает
     */
    public boolean match( CToken cToken ){
        if( cToken==null )return false;
        if( !(cToken instanceof KeywordTok) )return false;

        KeywordTok kwt = (KeywordTok)cToken;
        return this.equals(kwt.keyword());
    }

    /**
     * Проверка - совпадает текущее ключевое слово с указанным токеном/лексеммой
     * @param cToken лексема
     * @return true - совпадает / false - не совпадает
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public boolean match( Optional<? extends CToken> cToken ){
        if( cToken==null || !cToken.isPresent() )return false;
        if( !(cToken.get() instanceof KeywordTok) )return false;

        KeywordTok kwt = (KeywordTok)cToken.get();
        return this.equals(kwt.keyword());
    }
    //endregion
}
