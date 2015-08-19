/* 
 * Initialization.java
 * 
 * Created on 2015年3月9日
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
package com.letv.cdn.monitor.servlet;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.monitor.task.ClusterSettingsLoaderTask;
import com.letv.cdn.monitor.task.OffSetCheckTask;
import com.letv.cdn.monitor.task.ProcessCheckTask;
import com.letv.cdn.monitor.task.RedisCacheTask;

/**
 * 
 * spring配置加载后启动周期任务
 *
 * @author yangxuan
 *
 * @version 1.0
 */
public class Initialization extends HttpServlet implements Servlet{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory
            .getLogger(Initialization.class);
    
    public void init() {
    
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(
                10);
        scheduler.scheduleWithFixedDelay(new ClusterSettingsLoaderTask(), 0,
                60, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(new ProcessCheckTask(), 10,
                60 * 2 , TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(new OffSetCheckTask(20), 0,
                60, TimeUnit.SECONDS);
        scheduler.scheduleWithFixedDelay(new RedisCacheTask(), 0,
                60, TimeUnit.SECONDS);
    }
}
