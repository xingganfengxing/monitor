package com.letv.cdn.monitor.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.letv.cdn.manager.utils.Env;
import com.letv.cdn.monitor.common.FixedFIFOLinkedList;
import com.letv.cdn.monitor.common.FixedFIFOList;
import com.letv.cdn.monitor.pojo.MinuteDetail;
import com.letv.cdn.monitor.service.VodCollectRedisService;
import com.letv.cdn.monitor.utils.JedisUtil;

/**
 * 定时缓存最近10分钟的接收机 redis记录
 * 
 * @author lvzhouyang
 * @createDate 2015年3月16日
 */
@Component
public class RedisCacheTask implements Runnable{
    
    private static final Logger LOGGER = LoggerFactory.getLogger("RedisCacheTack");
    private static final int DEFAULT_SIZE = 10;
    
    private static final String CDN_ERROR_KEY = Env.get("rediskey_req_err");
    
    private static FixedFIFOList<MinuteDetail> cachedDetaiList;
    private static long errorListSize;
    
    public RedisCacheTask() {
    
        super();
        cachedDetaiList = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
    }
    
    public RedisCacheTask(int listSize) {
    
        super();
        cachedDetaiList = new FixedFIFOLinkedList<>(listSize);
        
    }
    
    @Override
    public void run() {
    
        try {
            LOGGER.info("redis cache start");
            cachedDetaiList.addAllLatSafe(VodCollectRedisService.getLastNRecord(cachedDetaiList.getMaxSize()));
            errorListSize = JedisUtil.getRedisKeyLen(CDN_ERROR_KEY);
            LOGGER.info("redis cache finish");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
    
    /**
     * geter below
     */
    
    public static FixedFIFOList<MinuteDetail> getCachedList() {
    
        return cachedDetaiList;
    }
    
    public static long getErrorListSize() {
    
        return errorListSize;
    }
    
}
