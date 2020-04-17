package xyz.cofe.cxel.eval;

import xyz.cofe.iter.Eterable;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Статичные методы / глбоальные функции
 */
public class StaticMethods implements Eterable<TypedFn> {
    /**
     * Конструктор по умолчанию
     */
    public StaticMethods(){
    }

    /**
     * Конструктор копирования
     * @param sample образец для копирования
     */
    public StaticMethods( StaticMethods sample ){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        sample.forEach(methods::add);
        methods.forEach( m -> methodsMap.put(keyOf(m),m) );
    }

    /**
     * Конструктор
     * @param initial начальное значение
     */
    private StaticMethods( Iterable<TypedFn> initial ){
        if( initial!=null ){
            initial.forEach( m->{if(m!=null)methods.add(m);} );
            methods.forEach( m -> methodsMap.put(keyOf(m),m) );
        }
    }

    /**
     * Список методов уникальных по ключу, где ключ определяется функцией {@link #keyOf}
     */
    private final HashMap<String,TypedFn> methodsMap = new HashMap<>();

    /**
     * Функция генерирующая уникальный ключ для типов аргментов метода
     * @param method метод
     * @return уникальный ключ
     */
    private static String keyOf( TypedFn method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        Type[] params = method.getParameterTypes();
        StringBuilder sb = new StringBuilder();
        for( Type p : params ){
            if( sb.length()>0 )sb.append(",");
            sb.append(p.toString());
        }
        return sb.toString();
    }

    /**
     * Список методов
     */
    private final Set<TypedFn> methods = new HashSet<>();

    @Override
    public Iterator<TypedFn> iterator(){
        return methods.iterator();
    }

    /**
     * Поиск методов которые совпадают по типам аргументов
     * @param method метод
     * @return методы совпадающие по типу аргументов
     */
    public StaticMethods sameArgs( Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        return new StaticMethods(filter( m ->{
            Type[] mParams = m.getParameterTypes();
            Type[] sParams = method.getParameterTypes();
            if( mParams.length != sParams.length )return false;
            for( int i=0; i<mParams.length; i++ ){
                Type mt = mParams[i];
                Type st = mParams[i];
                if( !mt.equals(st) )return false;
            }
            return true;
        }));
    }

    /**
     * Поиск методов которые совпадают по типам аргументов
     * @param paramTypes Типы аргментов
     * @return методы совпадающие по типу аргументов
     */
    public StaticMethods sameArgs( Type ... paramTypes ){
        if( paramTypes==null )throw new IllegalArgumentException( "paramTypes==null" );
        return new StaticMethods(filter( m ->{
            Type[] existsParams = m.getParameterTypes();
            if( existsParams.length != paramTypes.length )return false;
            for( int i=0; i<existsParams.length; i++ ){
                Type hasType = existsParams[i];
                Type expectedType = paramTypes[i];
                if( !hasType.equals(expectedType) )return false;
            }
            return true;
        }));
    }

    public StaticMethods sameRetAndArgs( Type returnType, Type ... paramTypes ){
        if( returnType==null )throw new IllegalArgumentException("returnType==null");
        if( paramTypes==null )throw new IllegalArgumentException("paramTypes==null");
        return new StaticMethods(filter( m ->{
            Type[] existsParams = m.getParameterTypes();
            if( existsParams.length != paramTypes.length )return false;
            for( int i=0; i<existsParams.length; i++ ){
                Type hasType = existsParams[i];
                Type expectedType = paramTypes[i];
                if( !hasType.equals(expectedType) )return false;
            }

            Type hasRetType = m.getReturnType();
            if( !hasRetType.equals(returnType) )return false;

            return true;
        }));
    }

    /**
     * Возвращает кол-во методов
     * @return кол-во методов
     */
    public int size(){ return methods.size(); }

    /**
     * Клонирование
     * @return клон
     */
    public synchronized StaticMethods clone(){
        return new StaticMethods(this);
    }

    /**
     * Добавляет метод, если метод с такой же сигнатурой определен,
     * то данный метод будет переопределен
     * @param method метод
     */
    public synchronized void add( Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        TypedFn tFn = TypedFn.of(method);
        String k = keyOf( tFn );

        TypedFn old = methodsMap.put(k,tFn);
        if( old!=null ){
            methods.remove(old);
        }
        methods.add(tFn);
    }

    /**
     * Добавляет метод, если метод с такой же сигнатурой определен,
     * то данный метод будет переопределен
     * @param fn метод
     */
    public synchronized void add( TypedFn fn ){
        if( fn==null )throw new IllegalArgumentException( "fn==null" );
        String k = keyOf( fn );

        TypedFn old = methodsMap.put(k,fn);
        if( old!=null ){
            methods.remove(old);
        }
        methods.add(fn);
    }

    /**
     * Добавляет методы, если метод с такой же сигнатурой определен,
     * то данный метод будет переопределен
     * @param methods методы
     */
    public synchronized void addAll( Iterable<Method> methods ){
        if( methods==null )throw new IllegalArgumentException( "methods==null" );
        methods.forEach( m->{if(m!=null)add(m);} );
    }

    /**
     * Удаляет указанный метод из набора
     * @param method метод
     */
    public synchronized void remove( Method method ){
        if( method==null )throw new IllegalArgumentException( "method==null" );
        TypedFn tFn = TypedFn.of(method);
        String key = keyOf(tFn);
        methods.remove(method);
        methodsMap.remove(key);
    }

    /**
     * Удаляет указанный метод из набора
     * @param fn метод
     */
    public synchronized void remove( TypedFn fn ){
        if( fn==null )throw new IllegalArgumentException( "fn==null" );
        String key = keyOf( fn );

        methods.remove(fn);
        methodsMap.remove(key);
    }

    /**
     * Удаляет указанные методы из набора
     * @param methods методы
     */
    public synchronized void removeAll( Iterable<Method> methods ){
        if( methods==null )throw new IllegalArgumentException( "methods==null" );
        methods.forEach( m->{ if(m!=null){ remove(m); } } );
    }
}
