package com.letv.cdn.manager.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.controller.common.DefaultController;
import com.letv.cdn.manager.pojo.User;
import com.letv.cdn.manager.service.UserService;
import com.letv.cdn.manager.utils.MD5Util;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.live.framework.controller.annotation.UserRightAnnotation;

/**
 * @author lvzhouyang
 *
 */
@Controller(value = "cdnUserController")
public class UserController extends DefaultController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Resource
    private UserService cdnUserService;

    /**
     * 修改密码
     * 
     * @param request
     * @param response
     * @param oldpwd
     * @param newpwd
     * @throws Exception
     */
    @UserRightAnnotation(menuId="60601000",functionId="01")
    @RequestMapping(value = "/changePassword")
    public void updateUserPassword(HttpServletRequest request,
	    HttpServletResponse response, String oldpwd, String newpwd,
	    String username) throws Exception {
	User user = cdnUserService.selectByUsername(username);
	JSONObject jsonResult = new JSONObject();

	if (!MD5Util.getStringMD5String(oldpwd).equalsIgnoreCase(
		user.getPassword())) {
	    jsonResult.put("success", false);
	    jsonResult.put("message", "原密码错误！");
	} else {
	    try {
		user.setPassword(MD5Util.getStringMD5String(newpwd));
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		log.error("error:" + e.toString(), e);
		jsonResult.put("success", false);
		jsonResult.put("message", e.getMessage());
	    }
	    if (cdnUserService.updatePassword(user)) {
		jsonResult.put("success", true);
	    } else {
		jsonResult.put("success", false);
		jsonResult.put("message", "修改失败，请重试");
	    }
	}
	ResponseUtil.sendJsonNoCache(response, jsonResult.toString());

    }

    
    @UserRightAnnotation(menuId="60601000")
    @RequestMapping("/userEditData")
    public void showUserDataGrid(HttpServletResponse rp,
	    @RequestParam int page, @RequestParam int rows,
	    @RequestParam String email) throws Exception {
	JSONObject dataGridJson = this.cdnUserService.getDataGridJson(page,
		rows, email);
	ResponseUtil.sendJsonNoCache(rp, dataGridJson.toString());

    }

    
    @UserRightAnnotation(menuId="60601000")
    @RequestMapping("/user")
    public String userIndex() {
	return "/user";
    }
    
    @UserRightAnnotation(menuId="60601000",functionId="01")
    @RequestMapping("/savaUser")
    public void savaUserChange(HttpServletResponse rp, HttpServletRequest rq,
	    @RequestParam String rowstr) throws Exception {
	List<User> userList = JSON.parseArray(rowstr, User.class);
	JSONObject jsonResult = new JSONObject();
	if (this.cdnUserService.updateOrSaveMenu(userList)) {
	    jsonResult.put("success", true);
	} else {
	    jsonResult.put("success", false);
	}
	ResponseUtil.sendJsonNoCache(rp, jsonResult.toString());
    }
    
    @UserRightAnnotation(menuId="60601000",functionId="01")
    @RequestMapping("/delUser")
    public void deleteUser(HttpServletResponse rp, HttpServletRequest rq,
	    @RequestParam long userId) throws IOException {
	JSONObject jsonResult = new JSONObject();

	if (this.cdnUserService.deleteUser(userId)) {
	    jsonResult.put("success", true);
	} else {
	    jsonResult.put("success", false);
	}
	ResponseUtil.sendJsonNoCache(rp, jsonResult.toString());
    }

    
    @UserRightAnnotation(menuId="60601000",functionId="01")
    @RequestMapping("/setDefaultPwd")
    public void setDefaultPassword(HttpServletResponse rp,
	    HttpServletRequest rq, @RequestParam long userId) throws Exception {
	JSONObject jsonResult = new JSONObject();
	if (this.cdnUserService.setDefaultPass(userId)) {
	    jsonResult.put("success", true);
	} else {
	    jsonResult.put("success", false);
	}
	ResponseUtil.sendJsonNoCache(rp, jsonResult.toString());
    }

    /**
     * @param rp
     * @throws IOException
     *             提供下拉列表的数据源
     */
   
    @UserRightAnnotation(menuId="60601000")
    @RequestMapping("/domainDataSource")
    public void comboboxDataSource(HttpServletResponse rp) throws IOException {
	ResponseUtil.sendJsonNoCache(rp,
		this.cdnUserService.formateComboBoxData());
    }
    
    /**
     * @Title: action
     * @Description: TODO 跳转到用户域名页面
     * @param @param rp
     * @param @param rq
     * @param @param userId
     * @param @param m
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @UserRightAnnotation(menuId="60601000",functionId="01")
    @RequestMapping("/userdomain")
    public String userDomain(HttpServletResponse rp, HttpServletRequest rq, @RequestParam Long userId, Model m) {
    
        Map<String, String> user = new HashMap<String, String>();
        User tmp = this.cdnUserService.getUserById(userId);
        user.put("id", tmp.getId().toString());
        user.put("username", tmp.getUsername());
        if (tmp.getPeak() != null) {
            user.put("peak", tmp.getPeak().toString());
        }
        if (tmp.getDownload() != null) {
            user.put("download", tmp.getDownload().toString());
        }
        user.put("type", tmp.getType());
        m.addAttribute("user", user);
        return "/userdomain";
    }
    
    
    @RequestMapping("/reloadPrivilege")
    public void reloadPrivilege(HttpServletResponse response) throws IOException {
        JSONObject jsonResult = new JSONObject();
        log.info("重载权限相关数据");
        this.cdnUserService.reload();
        jsonResult.put("success", "重新加载域名信息成功。");
        ResponseUtil.sendJsonNoCache(response, jsonResult.toString());
    }
}
