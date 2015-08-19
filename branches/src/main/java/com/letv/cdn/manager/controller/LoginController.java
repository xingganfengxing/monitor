package com.letv.cdn.manager.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.controller.common.DefaultController;
import com.letv.cdn.manager.pojo.User;
import com.letv.cdn.manager.service.UserService;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.manager.utils.StringUtil;
import com.letvcloud.saas.util.session.SessionProvider;

/**
 * 
 * @author liuchangfu
 *
 */
@Controller
public class LoginController extends DefaultController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Resource
    private UserService cdnUserService;
    @Resource
    private SessionProvider sessionProvider;
    
    
    @RequestMapping(value = "/")
    public String index(HttpServletRequest request, Model m) {
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
	    String username = null;
	    String password = null;
	    for (Cookie cookie : cookies) {
		if ("autoLoginManagerUsername".equals(cookie.getName())) {
		    username = cookie.getValue();
		}
		if ("autoLoginManagerPassword".equals(cookie.getName())) {
		    password = cookie.getValue();
		}
	    }
	    m.addAttribute("username", username);
	    m.addAttribute("password", password);
	    m.addAttribute("autologin", "checked");
	}
	return "/login";
    }
    /**
    // 用户登录验证
    @RequestMapping(value = "/loginSystem")
    public void loginSystem_test(HttpServletRequest request,HttpServletResponse response,
	    @RequestParam(required = false) String autologin,
	    @RequestParam(required = false) String username,
	    @RequestParam(required = false) String password) throws IOException {
	log.info("username : " + username);
	JSONObject jsonResult = new JSONObject();
	try {
	    // 判断用户和密码是否为空字符串
	    if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
		jsonResult.put("success", false);
		jsonResult.put("message", "用户名或密码不能为空");
		ResponseUtil.sendJsonNoCache(response, jsonResult.toString());
		return;
	    }
	    User user = new User();
	    user.setUsername(username);
	    user.setPassword(password);
        user = cdnUserService.loginCheckTmp(user);
	    // 判断用户是否存在
	    if (user == null) {
		jsonResult.put("success", false);
		jsonResult.put("message", "用户名不存在");
		ResponseUtil.sendJsonNoCache(response, jsonResult.toString());
		return;
	    }
	    HttpSession session = request.getSession();
	    session.setAttribute("user", user);
	    jsonResult.put("success", true);
	    // 当用户勾选记住我时，将用户名和密码存储到cookie中
	    if ("checked".equals(autologin)) {
		Cookie ckUsername = new Cookie("autoLoginManagerUsername",user.getUsername());
		ckUsername.setPath("/");
		ckUsername.setMaxAge(99999999);
		response.addCookie(ckUsername);
		Cookie ckPassword = new Cookie("autoLoginManagerPassword",password);
		ckPassword.setPath("/");
		ckPassword.setMaxAge(99999999);
		response.addCookie(ckPassword);
	    }
	} catch (Exception e) {
	    log.info("error:" + e.toString(), e);
	    jsonResult.put("success", false);
	    jsonResult.put("message", e.getMessage());
	}
	ResponseUtil.sendJsonNoCache(response, jsonResult.toString());
    }
	**/
    /**
     * 用户注销
     * 
     * @param session
     * @throws IOException
     */
    
    @RequestMapping(value = "/logoutSystem")
    public void logoutSystem(HttpServletRequest request,HttpServletResponse response) throws IOException {
	sessionProvider.logout(request, response);
	response.sendRedirect("/");
    }
}
