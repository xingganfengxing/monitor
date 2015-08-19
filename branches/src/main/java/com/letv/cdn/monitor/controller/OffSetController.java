package com.letv.cdn.monitor.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.monitor.service.OffSetCheckService;

@Controller
public class OffSetController{
    private static final Logger LOGGER = LoggerFactory.getLogger("OffSetController");
    @Resource
    private OffSetCheckService  offSetCheckService;
    
    @RequestMapping("/offset")
    public String offSet() {
    
        return "offset";
    }
    
    @RequestMapping("getvodoffsetinfo")
    public void getVodOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getVodOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }
    
    @RequestMapping("gettopoffsetinfo")
    public void getTopOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getTopOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }
    
    @RequestMapping("getclientoffsetinfo")
    public void getClientOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getClientOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }

    @RequestMapping("getp2poffsetinfo")
    public void getP2pOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getP2pOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }

    @RequestMapping("getrtmpoffsetinfo")
    public void getRtmpOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getRtmpOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }

    @RequestMapping("getflvoffsetinfo")
    public void getFlvOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getFlvOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }

    @RequestMapping("gethlsoffsetinfo")
    public void getHlsOffSetInfo(HttpServletResponse rp) throws IOException{
        JSONObject offSetInfoObject = offSetCheckService.getHlsOffSetInfo();
        ResponseUtil.sendJsonNoCache(rp, offSetInfoObject.toString());
    }
}
