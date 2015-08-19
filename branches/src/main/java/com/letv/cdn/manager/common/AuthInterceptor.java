package com.letv.cdn.manager.common;

import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.letv.cdn.manager.exception.NoWriteAuthorityException;
import com.letv.live.framework.Constants;
import com.letv.live.framework.controller.annotation.UserRightAnnotation;
import com.letv.live.framework.service.UserRightService;
import com.letvcloud.saas.util.session.SessionProvider;
/**
 * 权限鉴权类
 * TODO:add description of class here
 * Company : 乐视云计算<br>
 * Copyright @ 2015 letv – Confidential and Proprietary
 * @author liuchangfu
 * Create Date 2015年2月2日
 *
 */
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private UserRightService userRightService;
    @Resource
    private SessionProvider sessionProvider;

    @Override
    public void afterCompletion(HttpServletRequest request,
	    HttpServletResponse response, Object handler, Exception ex)
	    throws Exception {
    }

    @Override
    public void postHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler, ModelAndView model)
	    throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler) throws Exception {
	// 如果请求的内容不在spring mvc
	// controller中,则不需要验证DefaultServletHttpRequestHandler
	if (!(handler instanceof HandlerMethod)) {
	    return true;
	}
	HandlerMethod handlerMethod = (HandlerMethod) handler;
	UserRightAnnotation userRightAnnotation = handlerMethod.getMethodAnnotation(UserRightAnnotation.class);
	// UserRightAnnotation classUserRightAnnotation =
	// handlerMethod.getBean().getClass().getAnnotation(UserRightAnnotation.class);
	UserRightAnnotation classUserRightAnnotation = handlerMethod.getBeanType().getAnnotation(UserRightAnnotation.class);

	// 如果没有鉴权要求，则跳过鉴权
	if ((userRightAnnotation == null || StringUtils.isEmpty(userRightAnnotation.menuId()))&& (classUserRightAnnotation == null || StringUtils.isEmpty(classUserRightAnnotation.menuId()))) {
	    return true;
	}
	// 判断是否是超级用户
	// if ((Boolean)
	// request.getSession().getAttribute(Constants.SESSION_IS_SUPER_USER_PARAM))
	// {
	// add by wangpeng 2014-12-12
	if ((Boolean) sessionProvider.getAttribute(request,Constants.SESSION_IS_SUPER_USER_PARAM)) {
	    return true;
	}

	// 鉴权
	// String userId = (String)
	// request.getSession().getAttribute(Constants.SESSION_USER_ID_PARAM);
	// add by wangpeng 2014-12-12
	String userId = (String) sessionProvider.getAttribute(request,Constants.SESSION_USER_ID_PARAM);
	String menuFunc1 = null;
	if (!(userRightAnnotation == null || StringUtils.isEmpty(userRightAnnotation.menuId()))) {
	    if (StringUtils.isEmpty(userRightAnnotation.functionId())) {
		menuFunc1 = userRightAnnotation.menuId();
	    } else {
		menuFunc1 = userRightAnnotation.menuId() + "."+ userRightAnnotation.functionId();
	    }
	}
	String menuFunc2 = null;
	if (!(classUserRightAnnotation == null || StringUtils
		.isEmpty(classUserRightAnnotation.menuId()))) {
	    if (StringUtils.isEmpty(classUserRightAnnotation.functionId())) {
		menuFunc2 = classUserRightAnnotation.menuId();
	    } else {
		menuFunc2 = classUserRightAnnotation.menuId() + "."+ classUserRightAnnotation.functionId();
	    }
	}

	Set<String> userMenuFuncs = userRightService.getAllUserMenus(userId);
	boolean isOk = true;
	if (menuFunc1 != null) {
	    if (!userMenuFuncs.contains(menuFunc1)) {
		isOk = false;
	    }
	}
	if (menuFunc2 != null) {
	    if (!userMenuFuncs.contains(menuFunc2)) {
		isOk = false;
	    }
	}
	if (isOk) {
	    return true;
	} else {
	    String ajaxHeader = request.getHeader("X-Requested-With");// 获取是否为ajax请求
	    if (ajaxHeader != null) {
		throw new NoWriteAuthorityException("isReadonly");
	    } else {
		throw new NoWriteAuthorityException("ieReadonly");
	    }
	    // throw new InputCheckException("用户没有权限。");
	}
    }

}
