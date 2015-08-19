package com.letv.cdn.monitor.utils;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.google.common.collect.Lists;
import com.letv.cdn.manager.utils.Env;

/**
 * 初始化JedisKey Created by liufeng1 on 2014/12/24.
 */
public class JedisUtil{
    
    private static JedisPoolConfig CONFIG = initConfig();
    private static String REDIS_SERVER_IP = Env.get("redisServerIp");
    private static int REDIS_SERVER_PORT = Integer.parseInt(Env.get("redisServerPort"));
    
    public static final JedisPool JEDIS_POOL = new JedisPool(CONFIG, REDIS_SERVER_IP, REDIS_SERVER_PORT);
    
    private static JedisPoolConfig initConfig() {
    
        JedisPoolConfig config = new JedisPoolConfig();
        
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);
        
        // 设置的逐出策略类名, 默认DefaultEvictionPolicy
        // (当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setJmxEnabled(true);
        
        // MBean ObjectName = new ObjectName("org.apache.commons.pool2:
        // type=GenericObjectPool,name=" + "pool" + i);
        config.setJmxNamePrefix("pool");
        
        // 是否启用后进先出, 默认true
        config.setLifo(true);
        
        // 最大空闲连接数, 默认8个
        config.setMaxIdle(8);
        
        // 最大连接数, 默认8个
        config.setMaxTotal(30);
        
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),
        // 如果超时就抛异常, 小于零:阻塞不确定的时间, 默认-1
        config.setMaxWaitMillis(-1);
        
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(1800000);
        
        // 最小空闲连接数, 默认0
        config.setMinIdle(0);
        
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);
        
        // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,
        // 不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        
        // 在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(false);
        
        // 在空闲时检查有效性, 默认false
        config.setTestWhileIdle(false);
        
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(-1);
        return config;
    }
    
    /**
     * 返回 list的长度
     * 
     * @method: JedisUtil getRedisKeyLen
     * @param key
     * @return long
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public static long getRedisKeyLen(String key) {
    
        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        long queueLen = jedis.llen(key);
        JedisUtil.JEDIS_POOL.returnResourceObject(jedis);
        return queueLen;
    }
    
    /**
     * 从list缓存中 获取指定索引的数据
     * 
     * @method: JedisUtil getListIndex
     * @param key
     * @param beginIndex
     * @param endIndex
     * @return List<String>
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public static List<String> getListIndex(String key, long beginIndex, long endIndex) {
    
        if (endIndex > getRedisKeyLen(key)) {
            throw new IndexOutOfBoundsException(String.valueOf(endIndex));
        }
        long subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new IndexOutOfBoundsException(String.valueOf(subLen));
        }
        
        List<String> list = Lists.newArrayList();
        Jedis jedis = JedisUtil.JEDIS_POOL.getResource();
        for (long i = beginIndex; i < endIndex; i++) {
            String line = jedis.lindex(key, i);
            list.add(line);
        }
        JedisUtil.JEDIS_POOL.returnResourceObject(jedis);
        return list;
        
    }
    
    /**
     * 获取某个key 的全部list
     * 
     * @method: JedisUtil getList
     * @param key
     * @return List<String>
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public static List<String> getList(String key) {
    
        return getListIndex(key, 0, getRedisKeyLen(key));
    }
    
    /**
     * 获取最后N条记录
     * @method: JedisUtil  getListLastN
     * @param key
     * @param n
     * @return  List<String>
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public static List<String> getListLastN(String key, long n) {
    
        long length = getRedisKeyLen(key);
        return getListIndex(key, (length - 1 - n), (length - 1));
    }
    
}
