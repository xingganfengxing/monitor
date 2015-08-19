package com.letv.cdn.manager.utils;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by liujs on 14-5-27.
 */
public class MemcacheManager {
    private static final Logger log = LoggerFactory.getLogger(MemcacheManager.class);
    static MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(Env.get("memcached.address")));

    private static MemcachedClient memcachedClient = null;

    public static MemcachedClient getMemcachedClient() {
        try {
            if (memcachedClient == null) {
                builder.setConnectionPoolSize(10);
                memcachedClient = builder.build();
            }
            return memcachedClient;
        } catch (IOException e) {
            log.error("连接memcatch服务器异常！", e);
            return null;
        }
    }
    
    public static void saveToCache(String key, Object value) {
    	log.debug("saveToCache(key:{},value{})", key, value);
        try {
        	 getMemcachedClient().set(key, 0, value);
        } catch (Exception e) {
            log.error("缓存数据错误", e);
        } 
    }
    
    public static void saveToCache(String key, int exp, Object value) {
    	log.debug("saveToCache(key:{},value{})", key, value);
        try {
        	getMemcachedClient().set(key, exp, value);
        } catch (Exception e) {
        	log.error("缓存数据错误",e);
        } 
    }
    
    public static <T> T getFromCache(String key) {
    	log.debug("getFromCache(key:{})", key);
        try {
        	return getMemcachedClient().get(key);
        } catch (Exception e) {
        	log.error("获取缓存错误", e);
        } 
		return null;
    }
    
}
