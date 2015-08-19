package com.letv.cdn.monitor.service;

import java.util.*;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.letv.cdn.manager.utils.Env;
import com.letv.cdn.monitor.pojo.MinuteDetail;
import com.letv.cdn.monitor.utils.JedisUtil;

/**
 * 接收模块redis监控服务
 * Created by liufeng1 on 2015/3/10.
 * modify:吕周洋 2015/3/16
 */
@Service(value = "vodCollectRedisService")
public class VodCollectRedisService{
    
    private String cdnLogKey = Env.get("rediskey_req");
    private String cdnErrorKey = Env.get("rediskey_req_err");
    
    private List<MinuteDetail> getMinuteDetail(String key) {
    
        List<String> redisList = JedisUtil.getList(key);
        return pasListToDetails(redisList);
    }
    
    /**
     * 返回最新的N条记录
     * @method: VodCollectRedisService getLastNRecord
     * @param n
     * @return List<MinuteDetail>
     * @createDate： 2015年3月16日
     * @2015, by lvzhouyang.
     */
    public static List<MinuteDetail> getLastNRecord(long n) {
    
        String dateFormat = "yyyy-MM-dd";
        DateTime dateTime = new DateTime();
        String key = dateTime.toString(dateFormat) + "_minute_detail";
        
        List<String> redisList = JedisUtil.getListLastN(key, n);
        return pasListToDetails(redisList);
    }
    
    private static List<MinuteDetail> pasListToDetails(List<String> redisList) {
    
        List<MinuteDetail> list = Lists.newArrayList();
        for (Iterator<String> iterator = redisList.iterator(); iterator.hasNext();) {
            list.add(parseLine(iterator.next()));
        }
        
        return list;
    }
    
    private static MinuteDetail parseLine(String line) {
    
        MinuteDetail minuteDetail = new MinuteDetail();
        String minute = line.substring(0, line.lastIndexOf(":"));
        String noneMinute = line.substring(line.lastIndexOf(":") + 1).replace(" ", "");
        
        String[] dotLines = noneMinute.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String dotLine : dotLines) {
            String[] keyValues = dotLine.split("->");
            map.put(keyValues[0], keyValues[1]);
        }
        
        minuteDetail.setMinute(minute);
        minuteDetail.setMinuteFileCount(map.get("minuteFileCount"));
        minuteDetail.setSecondLogCount(map.get("secondLogCount"));
        minuteDetail.setSecondLogFailCount(map.get("secondLogFailCount"));
        minuteDetail.setSecondLogSuccRate(map.get("secondLogSuccRate"));
        minuteDetail.setSecondLogSize(map.get("secondLogSize"));
        minuteDetail.setMinuteFileSize(map.get("minuteFileSize"));
        minuteDetail.setRedisQueueLen(map.get("redisQueueLen"));
        
        return minuteDetail;
    }
    
    /**
     * 今日每分钟接收速度
     *
     * @return
     */
    public List<MinuteDetail> getTodayMinuteDetail() {
    
        String dateFormat = "yyyy-MM-dd";
        DateTime dateTime = new DateTime();
        String key = dateTime.toString(dateFormat) + "_minute_detail";
        return getMinuteDetail(key);
    }
    
    /**
     * 昨日日每分钟接收速度
     *
     * @return
     */
    public List<MinuteDetail> getYesterDayMinuteDetail() {
    
        String dateFormat = "yyyy-MM-dd";
        DateTime dateTime = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        String key = dateTime.minusDays(1).toString(dateFormat) + "_minute_detail";
        return getMinuteDetail(key);
    }
    
    /**
     * 未接收的日志队列长度
     *
     * @return
     */
    public long getQueueLen() {
    
        return JedisUtil.getRedisKeyLen(cdnLogKey);
    }
    
    /**
     * 接收错误的日志队列长度
     *
     * @return
     */
    public long getErrorQueueLen() {
    
        return JedisUtil.getRedisKeyLen(cdnErrorKey);
    }
    
    public JSONObject getRdsJson(String dayMinuteDetail) {
    
        JSONObject rdsJson = new JSONObject();
        List<MinuteDetail> list = null;
        if ("todayMinuteDetail".equals(dayMinuteDetail)) {
            list = getTodayMinuteDetail();
        } else {
            list = getYesterDayMinuteDetail();
        }

        Collections.reverse(list);
        rdsJson.put("total", list.size());
        rdsJson.put("rows", list);
        return rdsJson;
    }
}
