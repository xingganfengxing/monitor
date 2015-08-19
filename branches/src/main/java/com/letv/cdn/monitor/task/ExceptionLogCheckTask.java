package com.letv.cdn.monitor.task;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

/**
 * check database to confirm if exits new exceptions
 * 
 * @author lvzhouyang
 * @createDate 2015年3月26日
 */
@Repository("exceptionLogCheckTask")
public class ExceptionLogCheckTask{
    
    @Autowired
    private JdbcTemplate jdbcTemplateLog;
    
    private static final String CHECK_LOG_COUNT = "SELECT COUNT(event_id) FROM logging_event_property;";
    private static final String CHECK_EXP_MESSAGE = "SELECT logger_name FROM logging_event ORDER BY event_id DESC LIMIT 0,{0}";
    private long lastCount = NumberUtils.LONG_ZERO;
    
    // 发生即报警
    private static List<String> alermLogger = Lists.newArrayList();
    // 发生多次再报警
    private static List<String> timesThenAlerm = Lists.newArrayList();
    
    static {
        alermLogger.add("storm.kafka.KafkaUtils");
        alermLogger.add("backtype.storm.util");
        alermLogger.add("backtype.storm.daemon.worker");
        alermLogger.add("storm.kafka.DynamicBrokersReader");
        alermLogger.add("backtype.storm.daemon.worker");
        
        timesThenAlerm.add("com.letvcloud.cnd.util.MemcacheUtils");
        timesThenAlerm.add("com.letvcloud.cnd.util.ApiUtils");
        timesThenAlerm.add("com.letvcloud.cnd.util.HdfsUtils");
        timesThenAlerm.add("com.letvcloud.cnd.util.SQLUtils");
        
    }
    
    /**
     * query the database, get the log count
     * 
     * @method: ExceptionLogCheckTask getLogCount
     * @return long
     * @createDate： 2015年3月26日
     * @2015, by lvzhouyang.
     */
    private long getLogCount() {
    
        return jdbcTemplateLog.queryForLong(CHECK_LOG_COUNT);
        
    }
    
    @PostConstruct
    private void setLastCount() {
    
        lastCount = getLogCount();
    }
    
    /**
     * if new count > last count, mean that new excepiton happens
     * 
     * @method: ExceptionLogCheckTask checkIfOccurExp
     * @return boolean
     * @createDate： 2015年3月26日
     * @2015, by lvzhouyang.
     */
    public long checkIfOccuredExp() {
    
        long tmp = getLogCount();
        
        if (tmp - lastCount > 0) {
            lastCount = tmp;
        }
        return tmp - lastCount;
        
    }
    
    private boolean checkIfNeedToAlarm(long tmpcount) {
    
        List<Map<String, Object>> resultList = jdbcTemplateLog.queryForList(MessageFormat.format(CHECK_EXP_MESSAGE,
                tmpcount));
        
        for (Iterator<Map<String, Object>> iterator = resultList.iterator(); iterator.hasNext();) {
            Map<String, Object> map = iterator.next();
            if (alermLogger.contains(map.get("logger_name").toString())) {
                return true;
            }
        }
        return false;
        
    }
    
    public boolean checkExp() {
    
        // count 差
        long tmp = checkIfOccuredExp();
        if (tmp > 0) {
            
            return checkIfNeedToAlarm(tmp);
        }
        
        return false;
        
    }
}
