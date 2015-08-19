package com.letv.cdn.monitor.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.monitor.api.AbstractCheckItem;
import com.letv.cdn.monitor.api.CheckItemContext;
import com.letv.cdn.monitor.api.ItemState;

/**
 * API接口
 * 
 * @author lvzhouyang
 * @createDate 2015年3月17日
 */
@Controller
public class ApiController{
    
    @Autowired
    private AbstractCheckItem mrCheckItem;
    @Autowired
    private AbstractCheckItem vodErrorCheckItem;
    @Autowired
    private AbstractCheckItem vodConsumeCheckItem;
    @Autowired
    private AbstractCheckItem offSetCheckItem;
    @Autowired
    private AbstractCheckItem exceptionCheckItem;
    @Autowired
    private AbstractCheckItem hadoopStatusCheckItem;
    private static final String ITEM_SEP ="\r\n";
    
    @RequestMapping("/status")
    public void statusInfo(HttpServletResponse rp) throws IOException {
    
        List<String> resulteList = Lists.newArrayList();
        CheckItemContext checkItemContext = new CheckItemContext(null);
        
        // Kafka偏移检查项
        resulteList.add(getStatus(checkItemContext, offSetCheckItem));
        
        // 接收机消费队列检测
        resulteList.add(getStatus(checkItemContext, vodConsumeCheckItem));
        
        // 接收机错误队列检测
        resulteList.add(getStatus(checkItemContext, vodErrorCheckItem));
        
        
        // 异常日志检测
        resulteList.add(getStatus(checkItemContext, exceptionCheckItem));
        
        // Hadoop定时导入mysql检测
        resulteList.add(getStatus(checkItemContext, mrCheckItem));
        
        // Hadoop 进程状态检测
        resulteList.add(getStatus(checkItemContext, hadoopStatusCheckItem));
        
        ResponseUtil.sendJsonNoCache(rp, StringUtils.join(resulteList.toArray(),ITEM_SEP));
    }
    
    private static String getStatus(CheckItemContext checkItemContext, AbstractCheckItem checkItem) {
    
        checkItemContext.setCheckItem(checkItem);
        ItemState itemState = checkItemContext.check();
       
        return itemState.toString();
        
    }
}
