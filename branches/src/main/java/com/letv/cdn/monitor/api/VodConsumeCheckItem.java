package com.letv.cdn.monitor.api;

import java.util.Iterator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.common.FixedFIFOList;
import com.letv.cdn.monitor.pojo.MinuteDetail;
import com.letv.cdn.monitor.task.RedisCacheTask;
import com.letv.cdn.monitor.utils.Constants;


/**
 * 接收机消费队列长度检测项
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Repository("vodConsumeCheckItem")
public class VodConsumeCheckItem extends AbstractCheckItem{

    private static final long VALUE = 10000L;
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("RedisFileQueue");
        FixedFIFOList<MinuteDetail> list =  RedisCacheTask.getCachedList();
        if (!checkList(VALUE, list)) {
            itemState.changeSate(Constants.SEND_SMS);
        }else {
            itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        }
        
        return itemState;
        	
    }
    
    private static boolean checkList(long value, FixedFIFOList<MinuteDetail> list) {
        
        Iterator<MinuteDetail> iterable = list.iterator();
        while (iterable.hasNext()) {
            MinuteDetail tmp = (MinuteDetail) iterable.next();
            if (NumberUtils.toLong(tmp.getRedisQueueLen()) > value) {
                return false;
            }
        }
        
        return true;
    }
    
}
