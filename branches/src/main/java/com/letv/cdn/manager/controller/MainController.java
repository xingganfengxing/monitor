package com.letv.cdn.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.letv.cdn.manager.controller.common.DefaultController;

/**
 * 页面框架
 * <p>
 * <b>Project</b> : cdn-report<br>
 * <b>Create Date</b> : 2014年8月20日<br>
 * <b>Company</b> : letv<br>
 * <b>Copyright @ 2014 letv – Confidential and Proprietary</b>
 * </p>
 * </p>
 *
 * @author Chen Hao
 */
@Controller
public class MainController extends DefaultController{
    
    @RequestMapping("/index")
    public String index() {
    
        return "/index";
    }
    
    @RequestMapping("/layout/header")
    public String header(HttpServletRequest rq, Long domainId, Model m) {
    
        return "/common/header";
    }
    
    // 没权限页面跳转
    @RequestMapping("/norole")
    public String toPage() {
    
        return "/norole";
    }
    
    @RequestMapping("/layout/aside")
    public String aside(HttpServletRequest rq, @RequestParam String func, Model m) {
    
        List<Map<String, String>> menu = new ArrayList<Map<String, String>>();
        
        Map<String, String> m2 = new HashMap<String, String>();
        m2.put("class", "unselected");
        m2.put("id", "jvmmenu");
        m2.put("url", rq.getContextPath() + "/jvmmenu");
        m2.put("name", "JVM监控");
        menu.add(m2);

        Map<String, String> m3 = new HashMap<String, String>();
        m3.put("class", "unselected");
        m3.put("id", "rds");
        m3.put("url", rq.getContextPath() + "/rds");
        m3.put("name", "Rds监控");
        menu.add(m3);
        
        Map<String, String> m4 = new HashMap<String, String>();
        m4.put("class", "unselected");
        m4.put("id", "stormkafka");
        m4.put("url", rq.getContextPath() + "/offset");
        m4.put("name", "Storm消费监控");
        menu.add(m4);
        
        
        Map<String, String> m5 = new HashMap<String, String>();
        m5.put("class", "unselected");
        m5.put("id", "progress");
        m5.put("url", rq.getContextPath() + "/jps/index");
        m5.put("name", "java进程监控");
        menu.add(m5);

        Map<String, String> m6 = new HashMap<String, String>();
        m6.put("class", "unselected");
        m6.put("id", "progress");
        m6.put("url", rq.getContextPath() + "/reportmenu");
        m6.put("name", "统计报表");
        menu.add(m6);
        
        Map<String, String> m7 = new HashMap<String, String>();
        m7.put("class", "unselected");
        m7.put("id", "progress");
        m7.put("url", rq.getContextPath() + "/status");
        m7.put("name", "监控状态接口");
        menu.add(m7);
        
        m.addAttribute("menu", menu);
        return "/common/aside";
    }
    
}
