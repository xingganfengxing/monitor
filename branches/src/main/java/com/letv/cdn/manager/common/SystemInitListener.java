package com.letv.cdn.manager.common;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letvcloud.saas.util.XmlFileConfigFactory;

/**
 * 系统初始化
 * TODO:add description of class here
 * Company : 乐视云计算<br>
 * Copyright @ 2015 letv – Confidential and Proprietary
 * @author liuchangfu
 * Create Date 2015年1月29日
 *
 */
public class SystemInitListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(SystemInitListener.class);
    /**
     * 初始上下文
     */
    public void contextInitialized(ServletContextEvent sce) {
	log.info("加载不需要登录就可访问的url配置参数文件-----");
	XmlFileConfigFactory.init("config.xml");
    }

    /**
     * 销毁上下文
     */
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
