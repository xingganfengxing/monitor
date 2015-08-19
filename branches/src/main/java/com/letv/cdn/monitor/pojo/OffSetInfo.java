package com.letv.cdn.monitor.pojo;

/**
 * 偏移监控信息 bean
 * 
 * @author lvzhouyang
 * @createDate 2015年3月10日
 */
public class OffSetInfo{
    
    private long timeStamp;
    private long offSetInKafka;
    private long offSetInStorm;
    private long offSetDif;
    
    public OffSetInfo() {
    
        timeStamp = System.currentTimeMillis();
    }
    
    @Override
    public String toString() {
    
        return "OffSetInfo [timeStamp=" + timeStamp + ", offSetInKafka=" + offSetInKafka + ", offSetInStorm="
                + offSetInStorm + ", offSetDif=" + offSetDif + "]";
    }
    
    public long getTimeStamp() {
    
        return timeStamp;
    }
    
    public void setTimeStamp(long timeStamp) {
    
        this.timeStamp = timeStamp;
    }
    
    public long getOffSetInKafka() {
    
        return offSetInKafka;
    }
    
    public void setOffSetInKafka(long offSetInKafka) {
    
        this.offSetInKafka = offSetInKafka;
    }
    
    public long getOffSetInStorm() {
    
        return offSetInStorm;
    }
    
    public void setOffSetInStorm(long offSetInStorm) {
    
        this.offSetInStorm = offSetInStorm;
    }
    
    public long getOffSetDif() {
    
        return this.offSetInKafka - this.offSetInStorm;
    }
    
}
