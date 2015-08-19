/* 
 * TimerSchedule.java
 * 
 * Created on 2015年4月30日
 * 
 * Copyright(C) 2015, by 360buy.com.
 * 
 * Original Author: yangxuan
 * Contributor(s):
 * 
 * Changes 
 * -------
 * $Log$
 */
package com.letv.cdn.monitor.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.letv.cdn.monitor.utils.WebApplicationUtils;

@Component
public class TimerSchedule {

    private String ips = "10.182.200.30,10.182.200.31,10.182.200.32,10.182.200.33,10.182.200.34";

    @Scheduled(cron = "0 2 0 * * ?")
    public void dailyTopUrl() {

        ExportRedisTask task = new ExportRedisTask(
                ips,
                WebApplicationUtils.getCdnDataSource());
        task.run();

    }

    @Scheduled(cron = "0 1 0 * * ?")
    public void delKeys() {
        ClearRedisKeyTask task = new ClearRedisKeyTask(ips, 1);
        task.run();
    }
}
