/* 
 * SshCommandUtils.java
 * 
 * Created on 2015年3月6日
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
package com.letv.cdn.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class SshCommandUtils{
    private static Logger logger = LoggerFactory.getLogger("SshCommandUtils");
    
    public static String sshCmd(String ip, String user, String passwd,
            String cmd) throws IOException {
    
        Connection con = new Connection(ip);
        con.connect();
        boolean isAuth = con.authenticateWithPassword(user, passwd);
        if (isAuth) {
            try {
                return SshCommandUtils.remoteCmd(ip, cmd,con);
            } finally {
                con.close();
            }
        } else {
            logger.error("log-merge,no auth. ip:{},user:{},passwd:{}", ip,
                    user, passwd);
        }
        return null;
        
    }
    
    public static String remoteCmd(String ip, String cmd,Connection con) throws IOException {
    
        InputStream stdout = null;
        BufferedReader br = null;
        Session session = null;
        String msg = "";
        try {
            session = con.openSession();
            // 远程压缩、删除原文件
            session.execCommand(cmd);
            stdout = new StreamGobbler(session.getStdout());
            br = new BufferedReader(new InputStreamReader(stdout));
            
            String line = null;
            while ((line = br.readLine()) != null) {
                msg += line;
            }
            logger.info("output:{},ip:{},cmd:{}",msg,ip,cmd);
            
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(stdout);
            if (session != null) {
                session.close();
            }
        }
        return msg;
    }
}
