/* 
 * ProgressStatus.java
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

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ProgressStatus implements Serializable{
    private static final long serialVersionUID = 1L;
    // 进程ip
    private String ip;
    // 进程名
    private String progress;
    // 是否存在
    boolean isAlive;
    // 最近检查时间
    private String datetime;
    
    //出错信息
    private String description ;
    
    private int reboot ;
    
    private String cmd ;
    
    public String getDescription() {
    
        return description;
    }

    public void setDescription(String description) {
    
        this.description = description;
    }

    public String getIp() {
    
        return ip;
    }
    
    public void setIp(String ip) {
    
        this.ip = ip;
    }
    
    public String getProgress() {
    
        return progress;
    }
    
    public void setProgress(String progress) {
    
        this.progress = progress;
    }
    
    public boolean isAlive() {
    
        return isAlive;
    }
    
    public void setAlive(boolean isAlive) {
    
        this.isAlive = isAlive;
    }
    
    public String getDatetime() {
    
        return datetime;
    }
    
    public void setDatetime(String datetime) {
    
        this.datetime = datetime;
    }
    
    
    public boolean needReboot() {
    
        return reboot == 1;
    }

    public void setReboot(int reboot) {
    
        this.reboot = reboot;
    }

    public String getCmd() {
    
        return cmd;
    }

    public void setCmd(String cmd) {
    
        this.cmd = cmd;
    }

    @Override
    public String toString() {
    
        return ReflectionToStringBuilder.toString(this);
    }
    
    @Override
    public boolean equals(Object obj) {
    
        return new EqualsBuilder()
                .append(this.getIp(), ((ProgressStatus) obj).getIp())
                .append(this.getProgress(),
                        ((ProgressStatus) obj).getProgress()).build();
    }
    
    @Override
    public int hashCode() {
    
        return new HashCodeBuilder().append(this.getIp())
                .append(this.getProgress()).toHashCode();
    }
}
