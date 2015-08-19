package com.letv.cdn.monitor.controller;

import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.monitor.service.VodCollectRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RdsController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("RdsController");
    @Resource
    private VodCollectRedisService vodCollectRedisService;

    @RequestMapping("/getrds")
    public void getRds(HttpServletResponse rp,@RequestParam String dayMinuteDetail)
            throws IOException {

        JSONObject rdsInfoJsonObject = vodCollectRedisService.getRdsJson(dayMinuteDetail);
        ResponseUtil.sendJsonNoCache(rp, rdsInfoJsonObject.toJSONString());
    }
    
    @RequestMapping("/rds")
    public String rds(Model m, HttpServletRequest rq) {
        m.addAttribute("queueLen",vodCollectRedisService.getQueueLen());
        m.addAttribute("errorQueueLen",vodCollectRedisService.getErrorQueueLen());
        return "/rds";
    }
}
