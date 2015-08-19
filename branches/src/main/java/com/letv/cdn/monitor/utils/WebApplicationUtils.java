/* 
 * WebApplicationUtils.java
 * 
 * Created on 2015年3月11日
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
package com.letv.cdn.monitor.utils;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.letv.cdn.monitor.dao.ClusterSettingsMapper;
import com.letv.cdn.monitor.pojo.ClusterSettings;
import com.letv.cdn.monitor.pojo.ClusterSettingsExample;
import com.letv.cdn.monitor.pojo.ClusterSettingsExample.Criteria;

public class WebApplicationUtils{
    
    private final static WebApplicationContext wac = ContextLoader
            .getCurrentWebApplicationContext();
    
    public static List<ClusterSettings> getAllValidCluster() {
    
        List<ClusterSettings> clusterSet;
       
        ClusterSettingsMapper clusterSettingsMapper = (ClusterSettingsMapper) wac
                .getBean("clusterSettingsMapper");
        ClusterSettingsExample condition = new ClusterSettingsExample();
        Criteria c = condition.createCriteria();
        c.andValidEqualTo((byte) 1);
        clusterSet = clusterSettingsMapper.selectByExample(condition);
        return clusterSet;
    }
    public static DataSource getCdnDataSource() {
       return (DataSource) wac.getBean("cdnDataSource");
    }
    
    
    
}
