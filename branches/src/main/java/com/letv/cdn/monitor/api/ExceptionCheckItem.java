package com.letv.cdn.monitor.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.task.ExceptionLogCheckTask;
import com.letv.cdn.monitor.utils.Constants;

/**
 * TODO:每日检测一次Hadoop导入mysql结果
 * 
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Repository("exceptionCheckItem")
public class ExceptionCheckItem extends AbstractCheckItem{
    
    private static final Logger LOG = LoggerFactory.getLogger("MRCheckItem");
    
    @Autowired
    private ExceptionLogCheckTask exceptionLogCheckTask;
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("ExceptionLog");
        if (exceptionLogCheckTask.checkExp()) {
            itemState.changeSate(Constants.SEND_SMS);
        }else {
            itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        }
        return this.itemState;
        
    }
    
}
