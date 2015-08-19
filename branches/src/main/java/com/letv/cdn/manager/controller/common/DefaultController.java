package com.letv.cdn.manager.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.exception.NoLoginException;
import com.letv.cdn.manager.exception.NoWriteAuthorityException;
import com.letv.cdn.manager.utils.ResponseUtil;

public class DefaultController extends BaseController {

    @Override
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//用户未登陆或session超时的异常处理
    	if(e instanceof NoLoginException){
    		if("ajaxRequest".equals(e.getMessage())){
    			response.setHeader("sessionstatus", "timeout");
				response.setStatus(911);
				ResponseUtil.sendJsonNoCache(response, new JSONObject().toString());
    		}else{
    			response.sendRedirect(((NoLoginException)e).getUrl());
    		}
    		return null;
    	}
		// 判断用户有没有读写权限时的异常处理
		if (e instanceof NoWriteAuthorityException) {
			if ("isReadonly".equals(e.getMessage())) {
				response.setStatus(405);
				JSONObject jObject  = new JSONObject();
				ResponseUtil.sendJsonNoCache(response,jObject.toString());
			}else{	
			    	String path = request.getContextPath();
				response.sendRedirect(path+((NoWriteAuthorityException)e).getUrl());
			}
			return null;
		}
        throw new RuntimeException(e);
    }

}
