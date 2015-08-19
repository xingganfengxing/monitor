/* 
 * ClusterSettingsLoaderTask.java
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
package com.letv.cdn.monitor.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.letv.cdn.monitor.common.GlobalCacheInfo;
import com.letv.cdn.monitor.pojo.ClusterSettings;
import com.letv.cdn.monitor.utils.WebApplicationUtils;

public class ClusterSettingsLoaderTask implements Runnable{
    
    @Override
    public void run() {
    
        List<ClusterSettings> clusterSet = WebApplicationUtils
                .getAllValidCluster();
        if (CollectionUtils.isNotEmpty(clusterSet)) {
            Map<String, ClusterSettings> clusterMap = new HashMap<String, ClusterSettings>();
            for (ClusterSettings clusterSettings : clusterSet) {
                clusterMap
                        .put(clusterSettings.getProcessKey(), clusterSettings);
            }
            GlobalCacheInfo.putClusterSettingsMap(clusterMap);
        }
    }
    
}
