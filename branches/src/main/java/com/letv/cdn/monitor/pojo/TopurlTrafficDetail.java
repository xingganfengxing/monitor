/* 
 * TopurlTrafficDetail.java
 * 
 * Created on 2015年4月21日
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
package com.letv.cdn.monitor.pojo;

public class TopurlTrafficDetail{
    private String logDate;
    private String business;
    private String url;
    private long traffic;
    private int hit;
    private int request;
    private long bwAll;
    
    public long getBwAll() {
    
        return bwAll;
    }

    public void setBwAll(long bwAll) {
    
        this.bwAll = bwAll;
    }

    public String getLogDate() {
    
        return logDate;
    }
    
    public void setLogDate(String logDate) {
    
        this.logDate = logDate;
    }
    
    public String getBusiness() {
    
        return business;
    }
    
    public void setBusiness(String business) {
    
        this.business = business;
    }
    
    public String getUrl() {
    
        return url;
    }
    
    public void setUrl(String url) {
    
        this.url = url;
    }
    
    public long getTraffic() {
    
        return traffic;
    }
    
    public void setTraffic(long traffic) {
    
        this.traffic = traffic;
    }
    
    public int getHit() {
    
        return hit;
    }
    
    public void setHit(int hit) {
    
        this.hit = hit;
    }
    
    public int getRequest() {
    
        return request;
    }
    
    public void setRequest(int request) {
    
        this.request = request;
    }
    
    
}
