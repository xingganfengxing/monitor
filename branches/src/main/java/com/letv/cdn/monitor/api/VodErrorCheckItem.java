package com.letv.cdn.monitor.api;

import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.task.RedisCacheTask;
import com.letv.cdn.monitor.utils.Constants;
import com.letv.cdn.monitor.utils.JedisUtil;

/**
 * 接收机错误队列检测项
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Repository("vodErrorCheckItem")
public class VodErrorCheckItem extends AbstractCheckItem{
  
    private static long defaultValue = JedisUtil.getRedisKeyLen(Constants.CDN_ERROR_KEY);
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("VodRedisErrorQueue");
        if(RedisCacheTask.getErrorListSize() > defaultValue){
            defaultValue = RedisCacheTask.getErrorListSize();
            itemState.changeSate(Constants.MAKE_PHONE_CALL);
        }else {
            itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        }
        
        return itemState;
        
    }
    
}
