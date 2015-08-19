/* 
 * GlobalInfo.java
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
package com.letv.cdn.monitor.common;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.letv.cdn.monitor.pojo.ClusterSettings;

/**
 * 
 * 全局配置、结果缓存
 *
 * @author yangxuan
 *
 * @version 1.0
 */
public class GlobalCacheInfo{
    // 缓存
    private static Map<String, ClusterSettings> clusterSettingsMap = new ConcurrentHashMap<String, ClusterSettings>();
    
    // 进程是否存在
    private static Map<ProgressStatus,ProgressStatus> progressStatusMap = new ConcurrentHashMap<ProgressStatus,ProgressStatus>();
    
    // 执行扫描进程间隔 单位：分
    private static volatile int fixedDelay = 2;
    
    private GlobalCacheInfo() {
    
    }
    
    public static int getFixedDelay() {
    
        return fixedDelay;
    }
    
    public static void setFixedDelay(int fixedDelay) {
    
        GlobalCacheInfo.fixedDelay = fixedDelay;
    }
    
    public synchronized  static Map<String, ClusterSettings> getClusterSettingsMap() {
    
        return clusterSettingsMap;
    }
    
    public synchronized static void putClusterSettingsMap(
            Map<String, ClusterSettings> clusterSettingsMap) {
        GlobalCacheInfo.clusterSettingsMap.clear();
        GlobalCacheInfo.clusterSettingsMap.putAll(clusterSettingsMap);;
    }
    
    
    public static Collection<ProgressStatus> getProgressStatusSet() {
    
        return progressStatusMap.values();
    }

    public static void addAll(Collection<ProgressStatus> progressStatusCol) {
    
        for (ProgressStatus progressStatus : progressStatusCol) {
            GlobalCacheInfo.progressStatusMap.put(progressStatus,
                    progressStatus);
        }
    }
    
}
