package com.letv.cdn.monitor.api;

import java.util.Iterator;

import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.common.FixedFIFOList;
import com.letv.cdn.monitor.pojo.OffSetInfo;
import com.letv.cdn.monitor.task.OffSetCheckTask;
import com.letv.cdn.monitor.utils.Constants;

/**
 * 偏移量检测项
 * 
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Repository("offSetCheckItem")
public class OffSetCheckItem extends AbstractCheckItem{
    
    private static final long VALUE = 1800000000L;
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("KafkaOffSetDiff");
        FixedFIFOList<OffSetInfo> list = OffSetCheckTask.getvodOffSetInfos();
        if (!checkList(VALUE, list)) {
            itemState.changeSate(Constants.SEND_SMS);
        }else {
            itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        }
        
        return itemState;
        
    }
    
    /**
     * 检测列表 最近5项 是否都超过value 如果超过 返回false 没超过返回true
     * 
     * @method: OffSetCheckItem checkList
     * @param value
     * @param list
     * @return boolean
     * @createDate： 2015年3月17日
     * @2015, by lvzhouyang.
     */
    private static boolean checkList(long value, FixedFIFOList<OffSetInfo> list) {
    
        Iterator<OffSetInfo> iterable = list.iterator();
        while (iterable.hasNext()) {
            OffSetInfo offSetInfo = (OffSetInfo) iterable.next();
            if (offSetInfo.getOffSetDif() - value > 0l) {
                return false;
            }
        }
        
        return true;
    }
}
