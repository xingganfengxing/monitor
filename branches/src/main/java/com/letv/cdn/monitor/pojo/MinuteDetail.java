package com.letv.cdn.monitor.pojo;

/**
 * Redis每分钟详情
 * Created by liufeng1 on 2015/3/10.
 */
public class MinuteDetail {
    private String minute;
    private String minuteFileCount;
    private String secondLogCount;
    private String secondLogFailCount;
    private String secondLogSuccRate;
    private String secondLogSize;
    private String minuteFileSize;
    private String redisQueueLen;

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMinuteFileCount() {
        return minuteFileCount;
    }

    public void setMinuteFileCount(String minuteFileCount) {
        this.minuteFileCount = minuteFileCount;
    }

    public String getSecondLogCount() {
        return secondLogCount;
    }

    public void setSecondLogCount(String secondLogCount) {
        this.secondLogCount = secondLogCount;
    }

    public String getSecondLogFailCount() {
        return secondLogFailCount;
    }

    public void setSecondLogFailCount(String secondLogFailCount) {
        this.secondLogFailCount = secondLogFailCount;
    }

    public String getSecondLogSuccRate() {
        return secondLogSuccRate;
    }

    public void setSecondLogSuccRate(String secondLogSuccRate) {
        this.secondLogSuccRate = secondLogSuccRate;
    }

    public String getSecondLogSize() {
        return secondLogSize;
    }

    public void setSecondLogSize(String secondLogSize) {
        this.secondLogSize = secondLogSize;
    }

    public String getMinuteFileSize() {
        return minuteFileSize;
    }

    public void setMinuteFileSize(String minuteFileSize) {
        this.minuteFileSize = minuteFileSize;
    }

    public String getRedisQueueLen() {
        return redisQueueLen;
    }

    public void setRedisQueueLen(String redisQueueLen) {
        this.redisQueueLen = redisQueueLen;
    }
}
