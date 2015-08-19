package com.letv.cdn.monitor.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import scala.reflect.generic.Trees.This;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.monitor.pojo.ClusterSettings;
import com.letv.cdn.monitor.service.JvmMonitorService;

@Controller
public class JvmController{
    
    private static final Logger LOGGER = LoggerFactory.getLogger("JvmController");
    @Resource
    private JvmMonitorService jvmMonitorService;
    
    @RequestMapping("/getjvm")
    public void jvmInfo(HttpServletResponse rp, @RequestParam String ip, @RequestParam String procName)
            throws IOException {
    
        JSONObject jvmInfoJsonObject = jvmMonitorService.getJVMJson(ip.trim().replaceAll("[^0-9.“”]+", ""), procName);
        ResponseUtil.sendJsonNoCache(rp, jvmInfoJsonObject.toString());
        
    }
    
    @RequestMapping("/jvm")
    public String jvm(Model m, HttpServletRequest rq, @RequestParam String procname) {
    
        m.addAttribute("proc", procname);
        m.addAttribute("ips", this.jvmMonitorService.getClusterIps(procname));
        return "/jvm";
        
    }
    
    @RequestMapping("/jvmmenu")
    public String jvmMenu(Model m) {
    
        List<ClusterSettings> clusterSettings = this.jvmMonitorService.getCluster();
        m.addAttribute("cluster", clusterSettings);
        return "/jvmmenu";
        
    }
    
}
