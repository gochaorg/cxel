package xyz.cofe.cxel.tok;

import xyz.cofe.text.tparse.CToken;
import xyz.cofe.text.tparse.CharPointer;
import xyz.cofe.text.tparse.GR;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * Строковой литерал
 */
public class StringTok extends CToken {
    /**
     * Конструктор
     * @param begin начало литерала
     * @param end конец литерала
     * @param value значение
     */
    public StringTok( CharPointer begin, CharPointer end, String value ){
        super(begin, end);
        if( value==null )throw new IllegalArgumentException( "value==null" );
        this.value = value;
    }

    public StringTok( StringTok sample ){
        super(sample);
        this.value = value();
    }

    public StringTok clone(){ return new StringTok(this); }

    //region value
    protected String value;

    /**
     * Возвращает значение
     * @return значение
     */
    public String value(){ return value; }
    //endregion

    //region js string litteral parse
    private static char[] octDigits = "01234567".toCharArray();
    private static int index( char[] chars, char c ){
        for( int i=0;i<chars.length;i++ ){
            if( c==chars[i] )return i;
        }
        return -1;
    }
    private static int hexDigit( char c ){
        switch( c ){
            case '0': return 0;
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'a': case 'A': return 10;
            case 'b': case 'B': return 11;
            case 'c': case 'C': return 12;
            case 'd': case 'D': return 13;
            case 'e': case 'E': return 14;
            case 'f': case 'F': return 15;
            default: return -1;
        }
    }
    private static Optional<StringTok> js( CharPointer ptr ){
        if( ptr==null )throw new IllegalArgumentException( "ptr==null" );
        if( ptr.eof() )return Optional.empty();

        Optional<Character> started = ptr.lookup(0);
        if( !started.isPresent() )return Optional.empty();
        if( !(started.get() == '"' || started.get() == '\'') )return Optional.empty();

        StringBuilder sb = new StringBuilder();
        CharPointer finished = ptr;

        char chStarted = started.get();

        int idx = 0;
        while( true ){
            idx++;

            Optional<Character> chrOpt = ptr.lookup(idx);
            if( !chrOpt.isPresent() )return Optional.empty();

            char chr = chrOpt.get();
            if( chr==chStarted ){
                finished = ptr.move(idx+1);
                break;
            }

            if( chr=='\\' ){
                Optional<Character> c1opt = ptr.lookup(idx+1);
                if( !c1opt.isPresent() )return Optional.empty();
                char c1 = c1opt.get();

                Optional<Character> c2opt = ptr.lookup(idx+2);
                char c2 = c2opt.isPresent() ? c2opt.get() : 0;
                Optional<Character> c3opt = ptr.lookup(idx+3);
                char c3 = c3opt.isPresent() ? c3opt.get() : 0;
                Optional<Character> c4opt = ptr.lookup(idx+4);
                char c4 = c4opt.isPresent() ? c4opt.get() : 0;
                Optional<Character> c5opt = ptr.lookup(idx+5);
                char c5 = c5opt.isPresent() ? c5opt.get() : 0;
                Optional<Character> c6opt = ptr.lookup(idx+6);
                char c6 = c6opt.isPresent() ? c6opt.get() : 0;
                Optional<Character> c7opt = ptr.lookup(idx+7);
                char c7 = c7opt.isPresent() ? c7opt.get() : 0;
                Optional<Character> c8opt = ptr.lookup(idx+8);
                char c8 = c8opt.isPresent() ? c8opt.get() : 0;

                if( c1=='"' ){
                    sb.append("\"");
                    idx++;
                }else if( c1=='\'' ){
                    sb.append("'");
                    idx++;
                }else if( c1=='\\' ){
                    sb.append("\\");
                    idx++;
                }else if( c1=='n' ){
                    sb.append("\n");
                    idx++;
                }else if( c1=='r' ){
                    sb.append("\r");
                    idx++;
                }else if( c1=='b' ){
                    sb.append("\b");
                    idx++;
                }else if( c1=='f' ){
                    sb.append("\f");
                    idx++;
                }else if( c1=='t' ){
                    sb.append("\t");
                    idx++;
                }else if( c1=='v' ){
                    sb.append("\u000b");
                    idx++;
                }else if( index(octDigits,c1)>=0 && index(octDigits,c2)>=0 && index(octDigits,c3)>=0 ){
                    int k = 1;

                    int v = index(octDigits,c3) * k;
                    k = k * 8;

                    v += index(octDigits,c2) * k;
                    k = k * 8;

                    v += index(octDigits,c1) * k;
                    k = k * 8;

                    char c = (char)v;
                    sb.append(c);
                    idx += 3;
                }else if( c1=='x' && hexDigit(c2)>=0 && hexDigit(c3)>=0 ){
                    char c = (char)( hexDigit(c2)*16 + hexDigit(c3));
                    sb.append(c);
                    idx += 3;
                }else if( c1=='u' && hexDigit(c2)>=0 && hexDigit(c3)>=0 && hexDigit(c4)>=0 && hexDigit(c5)>=0 ){
                    char c = (char)(
                        hexDigit(c2)*16*16*16 +
                        hexDigit(c3)*16*16 +
                        hexDigit(c4)*16 +
                        hexDigit(c5)
                    );
                    sb.append(c);
                    idx += 5;
                }else if( c1=='u' && c2=='{'
                    && hexDigit(c3)>=0
                    && hexDigit(c4)>=0
                    && hexDigit(c5)>=0
                    && hexDigit(c6)>=0
                    && hexDigit(c7)>=0
                    && c8=='}'
                ){
                    Charset cs = Charset.forName("utf-32be");
                    CharBuffer cbuf = cs.decode(ByteBuffer.wrap(new byte[]{
                        (byte) 0,
                        (byte) (hexDigit(c3)),
                        (byte) (hexDigit(c4)*16 + hexDigit(c5)),
                        (byte) (hexDigit(c6)*16 + hexDigit(c7))
                    }));
                    idx += 8;
                    sb.append(cbuf.toString());
                }else if( c1=='\n' && c2=='\r' ){
                    idx += 2;
                }else if( c1=='\r' && c2=='\n' ){
                    idx += 2;
                }else if( c1=='\n' ){
                    idx += 1;
                }else if( c1=='\r' ){
                    idx += 1;
                }else{
                    return Optional.empty();
                }
            }else if( ((int)chr)>=32 ){
                sb.append(chr);
            }
        }

        return Optional.of( new StringTok(ptr,finished,sb.toString()) );
    }

    /**
     * Литерал соответ Javascript
     */
    public final static GR<CharPointer,StringTok> javascript = StringTok::js;
    //endregion

    /**
     * Преобзразоваание строки в литерал
     * @param text строка
     * @return литерал
     */
    public static String toJavaLitteral( String text ){
        if( text==null )return "null";

        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for( int i=0; i<text.length(); i++ ){
            char chr = text.charAt(i);
            if( chr=='\n' ){ sb.append("\\n");
            }else if( chr=='\r' ){ sb.append("\\r");
            }else if( chr=='\t' ){ sb.append("\\t");
            }else if( chr=='\b' ){ sb.append("\\b");
            }else if( chr=='\f' ){ sb.append("\\f");
            }else if( chr=='"' ){ sb.append("\\\"");
            }else if( chr=='\\' ){ sb.append("\\\\");
            }else if( ((int)chr)>=32 ){ sb.append(chr);
            }else{
                int ch = (int)chr;
                int b0 = ch & 0xff;
                int b1 = (ch>>16) & 0xff;

                String s1 = Integer.toString(b1,16);
                if( s1.length()==1 )s1 = "0"+s1;

                String s0 = Integer.toString(b0,16);
                if( s0.length()==1 )s0 = "0"+s0;

                sb.append("\\u");
                sb.append(s1);
                sb.append(s0);
            }
        }
        sb.append("\"");

        return sb.toString();
    }
}
