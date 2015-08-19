package com.letv.cdn.report.pojo;

import com.letv.cdn.report.utils.ConvertUtils;

public class VodPlatStat {

    private Integer id;
    private String ptime;
    private Integer platid;
    private String platname;
    private Integer splatid;
    private String splatname;
    private Double bw;
    private Integer request;
    private String bwStr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime == null ? null : ptime.trim();
    }

    public Integer getPlatid() {
        return platid;
    }

    public void setPlatid(Integer platid) {
        this.platid = platid;
    }

    public String getPlatname() {
        return platname;
    }

    public void setPlatname(String platname) {
        this.platname = platname == null ? null : platname.trim();
    }

    public Integer getSplatid() {
        return splatid;
    }

    public void setSplatid(Integer splatid) {
        this.splatid = splatid;
    }

    public String getSplatname() {
        return splatname;
    }

    public void setSplatname(String splatname) {
        this.splatname = splatname == null ? null : splatname.trim();
    }

    public Double getBw() {
        return bw;
    }

    public void setBw(Double bw) {
        this.bw = bw;
        this.bwStr = ConvertUtils.convertToMbsWithDot(bw);
    }

    public Integer getRequest() {
        return request;
    }

    public void setRequest(Integer request) {
        this.request = request;
    }

    public String getBwStr() {
        return bwStr;
    }

    public void setBwStr(String bwStr) {
        this.bwStr = bwStr;
    }
}