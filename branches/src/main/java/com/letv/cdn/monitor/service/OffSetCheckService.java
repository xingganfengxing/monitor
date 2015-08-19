package com.letv.cdn.monitor.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.letv.cdn.monitor.common.FixedFIFOList;
import com.letv.cdn.monitor.pojo.OffSetInfo;
import com.letv.cdn.monitor.task.OffSetCheckTask;

@Service
public class OffSetCheckService{
    
    /**
     * 点播
     * @method: OffSetCheckService  getvodOffSetInfo
     * @return  JSONObject
     * @createDate： 2015年4月17日
     * @2015, by lvzhouyang.
     */
    public JSONObject getVodOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getvodOffSetInfos());
    }
    
    public JSONObject getTopOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.gettopOffSetInfos());
    }
    
    public JSONObject getClientOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getclientOffSetInfos());
    }

    public JSONObject getP2pOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getP2pOffSetInfos());
    }

    public JSONObject getRtmpOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getRtmpOffSetInfos());
    }

    public JSONObject getFlvOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getFlvOffSetInfos());
    }

    public JSONObject getHlsOffSetInfo() {
        return getOffSetInfo(OffSetCheckTask.getHlsOffSetInfos());
    }
    
    /**
     * 获取storm 消费kafka速度 及偏移差距
     * 
     * @method: OffSetCheckService getOffSetInfo
     * @return JSONObject
     * @createDate： 2015年3月11日
     * @2015, by lvzhouyang.
     */
    public JSONObject getOffSetInfo(FixedFIFOList<OffSetInfo> offSetInfos ) {
    
        JSONObject json = new JSONObject();
        // x 轴时间列表
        List<String> dateList = Lists.newArrayList();
        List<Long> timeList = Lists.newArrayList();
        // 差值列表
        List<Long> offSetDifList = Lists.newArrayList();
        // storm 记录的 偏移列表
        List<Long> stormOffsetList = Lists.newArrayList();
        // storm读取kafka速度
        List<Long> stormConsumeRate = Lists.newArrayList();
        if (offSetInfos != null) {
            Iterator<OffSetInfo> iterable = offSetInfos.iterator();
            while (iterable.hasNext()) {
                OffSetInfo offSetInfo = (OffSetInfo) iterable.next();
                timeList.add(offSetInfo.getTimeStamp());
                dateList.add(timeMillis2Date(offSetInfo.getTimeStamp()));
                offSetDifList.add(offSetInfo.getOffSetDif());
                stormOffsetList.add(offSetInfo.getOffSetInStorm());
            }
            
            for (int i = 0; i < (timeList.size() - 1); i++) {
                stormConsumeRate.add(calConsumeRate((stormOffsetList.get(i + 1) - stormOffsetList.get(i)),
                        (timeList.get(i + 1) - timeList.get(i))));
            }
            
            json.put("offsettime", StringUtils.join(dateList.toArray(), ","));
            json.put("offsetdif", StringUtils.join(offSetDifList.toArray(), ","));
            // 速度数据比偏移数据少一位
            dateList.remove(0);
            json.put("ratetime", StringUtils.join(dateList.toArray(), ","));
            json.put("ratelist", StringUtils.join(stormConsumeRate.toArray(), ","));
        }
        
        return json;
    }
    
    /**
     * 将毫秒时间戳 转为相应日期格式
     * 
     * @method: OffSetCheckService timeMillis2Date
     * @param time
     * @return String
     * @createDate： 2015年3月11日
     * @2015, by lvzhouyang.
     */
    private static String timeMillis2Date(long time) {
    
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(d).toString();
    }
    
    /**
     * 粗略计算storm消费速度
     * 
     * @method: OffSetCheckService calConsumeRate
     * @param offSet
     * @param time
     * @return long
     * @createDate： 2015年3月11日
     * @2015, by lvzhouyang.
     */
    private static long calConsumeRate(long offSet, long time) {
    
        return (offSet / time) * 1000;
    }
}
