/* 
 * ProcessCheckTask.java
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
package com.letv.cdn.monitor.task;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.letv.cdn.monitor.common.GlobalCacheInfo;
import com.letv.cdn.monitor.common.ProgressStatus;
import com.letv.cdn.monitor.pojo.ClusterSettings;
import com.letv.cdn.monitor.utils.SshCommandUtils;
import com.letv.cdn.monitor.utils.WebApplicationUtils;

/**
 * 
 * 检查进程是否存在周期性任务
 *
 * @author yangxuan
 *
 * @version 1.0
 */
public class ProcessCheckTask implements Runnable{
    private final static Logger logger = LoggerFactory
            .getLogger("ProcessCheckTask");
    /*
     * private final static String USER = "root"; private final static String
     * PASSWD = "CDN800G";
     */
    private final static String USER = "root";
    private final static String PASSWD = "CDN800G";
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String CMD = "source /etc/profile; source ~/.bash_profile;jps |grep {0}";
    private final static String CMD_V = "source /etc/profile; source ~/.bash_profile;jps -v|grep ";
    private final static String CMD_SP = "#";
    @Override
    public void run() {
    
        try {
            logger.info("check process tack start.");
            Map<String, ClusterSettings> clusterSettingsMap = GlobalCacheInfo
                    .getClusterSettingsMap();
            List<ClusterSettings> clusterSet = null;
            if (MapUtils.isNotEmpty(clusterSettingsMap)) {
                clusterSet = new ArrayList<ClusterSettings>();
                clusterSet.addAll(clusterSettingsMap.values());
            } else {
                clusterSet = WebApplicationUtils.getAllValidCluster();
            }
            List<ProgressStatus> progressStatusList = this
                    .getProgressStatus(clusterSet);
            this.sanNoAliveAndWarn(progressStatusList);
            
            // 全局保存,提供页面显示
            GlobalCacheInfo.addAll(progressStatusList);
            
            logger.info("check process tack finish.");
        } catch (Exception e) {
            logger.error("ProcessCheckTask run.", e);
        }
    }
    
    // todo：扫描报警
    private void sanNoAliveAndWarn(List<ProgressStatus> list) {
        Set<ProgressStatus> set = new HashSet<ProgressStatus>(list);
        for (ProgressStatus progressStatus : set) {
            // 进程挂掉，或者不能访问
            if (!progressStatus.isAlive()) {
                // 是否需要重启，重启成功，不需要报警
                if (progressStatus.needReboot()) {
                    String cmd = progressStatus.getCmd();
                    try {
                        if (StringUtils.isNotEmpty(cmd)) {
                            String[] cmdArr = cmd.split(CMD_SP);
                            if (cmdArr != null) {
                                for (String execmd : cmdArr) {
                                    // only 重启
                                    logger.info("reboot start ,execmd:{},ip:{}",
                                            execmd,
                                            progressStatus.getIp());
                                    SshCommandUtils.sshCmd(
                                            progressStatus.getIp(), USER,
                                            PASSWD, execmd);
                                }
                            }
                        }
                        
                    } catch (Exception e) {
                        logger.error("cmd:{},ip:{},error:{}",
                                progressStatus.getCmd(),
                                progressStatus.getIp(), e);
                        // todo，报警
                    }
                } 
            }
        }
    }
    
    private List<ProgressStatus> getProgressStatus(
            List<ClusterSettings> clusterSet) {
    
        List<ProgressStatus> progressStatusList = new ArrayList<ProgressStatus>();
        if (CollectionUtils.isNotEmpty(clusterSet)) {
            for (ClusterSettings clusterSettings : clusterSet) {
                String ips = clusterSettings.getIps();
                String[] ipArr = ips.split(",");
                for (String ip : ipArr) {
                    int reboot = clusterSettings.getReboot() == null ? 0
                            : clusterSettings.getReboot();
                    progressStatusList.add(this.scanIp(
                            clusterSettings.getProcessKey(), reboot, clusterSettings.getCmd(), ip));
                }
            }
        }
        return progressStatusList;
    }
    
    private ProgressStatus scanIp(String key, int reboot, String cmd, String ip) {
    
        ProgressStatus progressStatus = new ProgressStatus();
        progressStatus.setIp(ip);
        progressStatus.setProgress(key);
        progressStatus.setDatetime(new DateTime().toString(DATETIME_FORMAT));
        progressStatus.setReboot(reboot);
        progressStatus.setCmd(cmd);
        progressStatus.setAlive(false);
        try {
            String grepCmd ="calcbandwidth".contains(key)? (CMD_V
                    +key + "|awk '{print $1}'"): MessageFormat.format(CMD,
                    key);
            String progressInfo = SshCommandUtils.sshCmd(ip, USER, PASSWD, grepCmd);
            if (StringUtils.isNotEmpty(progressInfo)) {
                progressStatus.setAlive(true);
            }
        } catch (Exception e) {
            logger.error("ssh error,ip:" + ip + "", e);
            progressStatus.setDescription("ip:" + ip + "访问出错");
            progressStatus.setReboot(0);
        }
        return progressStatus;
    }
    
}
