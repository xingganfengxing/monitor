package com.letv.cdn.monitor.task;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 每天定时 查询点播 summary表的数据量
 * 
 * @author lvzhouyang
 * @createDate 2015年3月24日
 */
@Repository("mqSqlCheckTimerTask")
public class MySqlCheckTimerTask{
    private static final Logger LOG = LoggerFactory.getLogger("MqSqlCheckTimerTask");
    @Autowired
    private JdbcTemplate jdbcTemplateVod;
    
    private static final String PTIME_FORMAT = "yyyyMMdd";
    private static final String TABLE_PREFIX = "cdn_summary_";
    private static final String CHECK_SQL = "select count(*) from {0};";
    private static long yesterdayCount = 0L;
    
    private static String getTableName() {
    
        DateTime nowTime = new DateTime();
        // 每天监测昨天的表 是否导入成功
        DateTime now = nowTime.withTimeAtStartOfDay().minusDays(1);
        return StringUtils.join(new String[] { TABLE_PREFIX, now.toString(PTIME_FORMAT) });
        
    }
    
    @PostConstruct
    public void getCount() {
    
        try {
            yesterdayCount = jdbcTemplateVod.queryForLong(MessageFormat.format(CHECK_SQL, getTableName()));
            LOG.info("定时检测点播 MySql数据 : " +yesterdayCount);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
    
    public static long getYesterdayCount() {
        return yesterdayCount;
    }
    
}
