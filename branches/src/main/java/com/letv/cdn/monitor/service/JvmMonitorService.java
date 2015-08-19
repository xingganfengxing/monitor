package com.letv.cdn.monitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.letv.cdn.monitor.common.GlobalCacheInfo;
import com.letv.cdn.monitor.dao.ClusterSettingsMapper;
import com.letv.cdn.monitor.pojo.ClusterSettings;
import com.letv.cdn.monitor.utils.WebApplicationUtils;
import com.letv.log.jvm.JvmMonitor.MetricType;
import com.letv.log.util.XMemcacheUtil;

/**
 * 获取JVM监控信息
 * @author lvzhouyang
 * @createDate 2015年3月10日
 */
@Service
public class JvmMonitorService{
    private static final Logger LOGGER = LoggerFactory.getLogger("JvmService");

    private static final String TIME_FORMAT = "yyyyMMddHHmm";
    private static final String CACHE_SEP = "&";
    
    @Resource
    private ClusterSettingsMapper clusterSettingsMapper;
    
    public JSONObject getJVMJson(String ip, String procName) {
        JSONObject totalJson = new JSONObject();
        
        SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT);// 设置日期格式
        String nowtime = df.format(new Date());
        DateTime dateTime = DateTime.parse(nowtime, DateTimeFormat.forPattern(TIME_FORMAT));
        
        List<String> timeList = Lists.newArrayList();
        timeList.add(dateTime.toString(TIME_FORMAT));
        
        for (int i = 0; i < 9; i++) {
            dateTime = dateTime.minusMinutes(1);
            timeList.add(dateTime.toString(TIME_FORMAT));
        }
        timeList = Lists.reverse(timeList);
        
        //基本信息
        Object uptime = getCacheValue(getKey(ip,procName,MetricType.UPTIME));
        Object loadClass = getCacheValue(getKey(ip,procName,MetricType.LOADED_CLASSES));
        Object youngCount = getCacheValue(getKey(ip,procName,MetricType.YOUNG_GEN_GARBAGE_COLLECTION_COUNT));
        Object youngTime = getCacheValue(getKey(ip,procName,MetricType.YOUNG_GEN_GARBAGE_COLLECTION_TIME));
        Object oldCount = getCacheValue(getKey(ip,procName,MetricType.OLD_GEN_GARBAGE_COLLECTION_COUNT));
        Object oldTime = getCacheValue(getKey(ip,procName,MetricType.OLD_GEN_GARBAGE_COLLECTION_TIME));
        Object systemLoad = getCacheValue(getKey(ip,procName,MetricType.SYSTEM_LOAD_AVAREGE));
        // 线程相关
        Object thread = getCacheValue(getKey(ip,procName,MetricType.THREADS));
        Object daemonThreads = getCacheValue(getKey(ip,procName,MetricType.DAEMON_THREADS));
        Object avgThreadBCount = getCacheValue(getKey(ip,procName,MetricType.AVAREGE_THREAD_BLOCK_COUNT));
        Object avgThreadBTime = getCacheValue(getKey(ip,procName,MetricType.AVERAGE_THREAD_BLCOK_TIME));
        Object deadlockedThreads = getCacheValue(getKey(ip,procName,MetricType.DEADLOCKED_THREADS));
        
        //堆空间 老年代
        List<Object> heapValueList = Lists.newArrayList();
        List<Object> oldgenValueList = Lists.newArrayList();
        for (String object : timeList) {
            Object tmp = getCacheValue(getKeyTimely(ip,procName,MetricType.HEAP_MEMORY_USED, object));
            if (tmp != null) {
                heapValueList.add(tmp);
            } else {
                heapValueList.add(0);
            }
            tmp = getCacheValue(getKeyTimely(ip,procName,MetricType.OLD_GEN_USED, object));
            if (tmp != null) {
                oldgenValueList.add(tmp);
            } else {
                oldgenValueList.add(0);
            }
        }
        
        totalJson.put("x", StringUtils.join(timeList.toArray(), ","));
        totalJson.put("heapValueList", StringUtils.join(heapValueList.toArray(), ","));
        totalJson.put("oldgenValueList", StringUtils.join(oldgenValueList.toArray(), ","));
        totalJson.put("heapmax", getCacheValue(getKey(ip,procName,MetricType.HEAP_MEMORY_MAX)));
        totalJson.put("oldgenmax", getCacheValue(getKey(ip,procName,MetricType.OLD_GEN_MAX)));
        
        
        totalJson.put("uptime", uptime);
        totalJson.put("loadClass", loadClass);
        totalJson.put("youngCount", youngCount);
        totalJson.put("youngTime", youngTime);
        totalJson.put("oldCount", oldCount);
        totalJson.put("oldTime", oldTime);
        totalJson.put("systemLoad", systemLoad);
        
        totalJson.put("thread", thread);
        totalJson.put("daemonThreads", daemonThreads);
        totalJson.put("avgThreadBCount", avgThreadBCount);
        totalJson.put("avgThreadBTime", avgThreadBTime);
        totalJson.put("deadlockedThreads", deadlockedThreads);
        
        return totalJson;
    }
    
    private Object getCacheValue(String key) {
    
        try {
            return XMemcacheUtil.getFromCache(key);
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
    
    private String getKeyTimely(String ip, String procName, MetricType type, String nowtime) {
    
        return StringUtils.join(new String[] { ip, procName, type.toString().toLowerCase(), nowtime }, CACHE_SEP);
        
    }
    
    private String getKey(String ip, String procName, MetricType type) {
    
        return StringUtils.join(new String[] { ip, procName, type.toString().toLowerCase() }, CACHE_SEP);
        
    }
    
    public List<ClusterSettings> getCluster() {
        Map<String, ClusterSettings> clusterSettingsMap = GlobalCacheInfo
                .getClusterSettingsMap();
        List<ClusterSettings> clusterSet = null;
        if (MapUtils.isNotEmpty(clusterSettingsMap)) {
            clusterSet = new ArrayList<ClusterSettings>();
            clusterSet.addAll(clusterSettingsMap.values());
        } else {
            clusterSet = WebApplicationUtils.getAllValidCluster();
        }
        return clusterSet;
        
    }
    
    public List<String> getClusterIps(String procKey){
        
        return Lists.newArrayList(GlobalCacheInfo.getClusterSettingsMap().get(procKey).getIps().split(","));
        
    }
    
}
