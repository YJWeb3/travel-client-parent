package com.zheng.travel.client.localcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EhCacheService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 设置缓存对象
     *
     * @param key
     * @param object
     */
    public void setCache(String key, Object object) {
        Cache cache = cacheManager.getCache("usercache");
        Element element = new Element(key, object);
        cache.put(element);
    }

    /**
     * 从缓存中取出对象
     *
     * @param key
     * @return
     */
    public Object getCache(String key) {
        Object object = null;
        Cache cache = cacheManager.getCache("usercache");
        if (cache!=null && cache.get(key) != null && !cache.get(key).equals("")) {
            object = cache.get(key).getObjectValue();
        }
        return object;
    }

    public boolean removeCache(String key) {
        Object object = null;
        Cache cache = cacheManager.getCache("usercache");
        if (cache!=null && cache.get(key) != null && !cache.get(key).equals("")) {
            return cache.remove(key);
        }
        return false;
    }
}