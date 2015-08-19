/* 
 * ShardJedisTest.java
 * 
 * Created on 2015年3月30日
 * 
 * Copyright(C) 2015, by letv.com.
 * 
 * Original Author: yangxuan
 * Contributor(s):
 * 
 * Changes 
 * -------
 * $Log$
 */
package com.letv.cdn.monitor.utils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ShardJedis {
    private final static org.slf4j.Logger LOGGER = LoggerFactory
            .getLogger("ShardJedis");
    private ShardedJedisPool pool;

    public ShardJedis(String ips) {
        this(ips, 0);
    }

    public ShardJedis(String ips, int db) {
        JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置
        String[] iparr = ips.split(Constants.COMMA);
        List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>();
        for (String ip : iparr) {
            URI uri = null;
            try {
                uri = new URI("http", null, ip, 6379, "/" + db, null, null);
            } catch (URISyntaxException e) {
                LOGGER.error(e.getMessage());
            }
            jdsInfoList.add(new JedisShardInfo(uri));
        }
        pool = new ShardedJedisPool(config, jdsInfoList);
    }

    /**
     * 批量删除key
     * @param keyList
     */
    public void delKey(List<String> keyList){
        ShardedJedis jds = null;
        try {
            jds = pool.getResource();
            ShardedJedisPipeline p = jds.pipelined();
            for(String key : keyList){
                p.del(key);
            }
            p.sync();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            pool.returnResource(jds);
        }
    }
}
