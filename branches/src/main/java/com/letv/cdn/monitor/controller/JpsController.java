/* 
 * JpsController.java
 * 
 * Created on 2015年3月12日
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
package com.letv.cdn.monitor.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.monitor.common.GlobalCacheInfo;
import com.letv.cdn.monitor.common.ProgressStatus;

@Controller
@RequestMapping("/jps")
public class JpsController{
    private static final Logger LOGGER = LoggerFactory.getLogger("JpsController");
    
    @RequestMapping("/index")
    public String index(HttpServletRequest rq) {
        return "/jps";
    }
    @RequestMapping("/showlist")
    public void showlist(HttpServletResponse rp,HttpServletRequest rq) throws IOException{
        Collection<ProgressStatus>  coll = GlobalCacheInfo.getProgressStatusSet();
        if(CollectionUtils.isNotEmpty(coll)){
            List<ProgressStatus> list = Lists.newArrayList(coll) ;
            Collections.sort(list,new Comparator<ProgressStatus>(){

                @Override
                public int compare(ProgressStatus o1, ProgressStatus o2) {
                    
                    return o1.getProgress().compareTo(o2.getProgress());
                }
            }) ;
            JSONObject rdsJson = new JSONObject();
            rdsJson.put("total", list.size());
            rdsJson.put("rows", list);
            
            ResponseUtil.sendJsonNoCache(rp, rdsJson.toString());
        }
        
    }
    
}
