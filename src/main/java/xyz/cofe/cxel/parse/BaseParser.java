package xyz.cofe.cxel.parse;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class BaseParser implements GRCache {
    //region cache
    protected Map<String,Object> cache = new LinkedHashMap<>();
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
}
