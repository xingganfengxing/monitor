package com.letv.cdn.monitor.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.task.MySqlCheckTimerTask;
import com.letv.cdn.monitor.utils.Constants;

/**
 * TODO:每日检测一次Hadoop导入mysql结果
 * 
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Repository("mrCheckItem")
public class MRCheckItem extends AbstractCheckItem{
    
    private static final Logger LOG = LoggerFactory.getLogger("MRCheckItem");
    private static final long LEAST_COUNT = 2000000L;
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("HadoopToMysql");
        if (MySqlCheckTimerTask.getYesterdayCount() < LEAST_COUNT) {
            itemState.changeSate(Constants.SEND_SMS);
            LOG.error("点播summary 表有异常");
        } else {
            itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        }
        return this.itemState;
        
    }
    
}
