package com.letv.cdn.report.pojo;

/**
 * Created by liufeng1 on 2015/3/17.
 */
public class BwQuery {

    private String platid;
    private String splatid;
    private String startTime;
    private String endTime;
    private String tableName;

    public String getPlatid() {
        return platid;
    }

    public void setPlatid(String platid) {
        this.platid = platid;
    }

    public String getSplatid() {
        return splatid;
    }

    public void setSplatid(String splatid) {
        this.splatid = splatid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
