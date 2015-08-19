package com.letv.cdn.monitor.api;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.letv.cdn.monitor.utils.Constants;

/**
 * 通过数据库检测Hadoop状态
 * @author lvzhouyang
 * @createDate 2015年4月17日
 */
@Repository("hadoopStatusCheckItem")
public class HadoopStatusCheckItem extends AbstractCheckItem{
    
    @Autowired
    JdbcTemplate jdbcTemplateMonitor;
    private final String SQL = "select * from hadoop_status;";
    
    @Override
    public ItemState operateCheck() {
    
        initItemState("HadoopStatus");
        List<Map<String, Object>> result =  jdbcTemplateMonitor.queryForList(SQL);
        for (Iterator<Map<String, Object>> iterator = result.iterator(); iterator.hasNext();) {
            Map<String, Object> map = iterator.next();
            if (1 ==NumberUtils.toInt(map.get("status").toString())) {
                itemState.changeSate(Constants.SEND_SMS);
                return itemState;
            }
        }
        
        itemState.changeSate(Constants.SYSTEM_STATUS_OK);
        
        return itemState;
        
    }
}
