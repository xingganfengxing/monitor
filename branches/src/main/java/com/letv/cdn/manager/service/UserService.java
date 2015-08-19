package com.letv.cdn.manager.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.dao.UserMapper;
import com.letv.cdn.manager.exception.ServiceException;
import com.letv.cdn.manager.pojo.User;
import com.letv.cdn.manager.pojo.UserExample;
import com.letv.cdn.manager.pojo.UserExample.Criteria;
import com.letv.cdn.manager.utils.Env;
import com.letv.cdn.manager.utils.MD5Util;
import com.letv.cdn.manager.utils.StringUtil;


@Service(value = "cdnUserService")
public class UserService extends BaseUserService {

    @Resource
    private UserMapper um;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * 用户登录验证
     * 
     * @param user
     * @return 若用户存在，返回用户信息；若不存在，返回null。
     * @throws Exception
     */
    public User loginCheck(User loginuser) throws Exception {
	List<User> userList = null;
	UserExample ue = new UserExample();
	Criteria c = ue.createCriteria();
	c.andUsernameEqualTo(loginuser.getUsername());
	userList = this.um.selectByExample(ue);
	if (userList.size() == 0) {
	    return null;
	} else {
	    User dbUser = userList.get(0);
	    if (!MD5Util.getStringMD5String(loginuser.getPassword())
		    .equalsIgnoreCase(dbUser.getPassword())) {
		throw new ServiceException("用户名或密码错误");
	    }
	    return dbUser;
	}
    }

    /**
     * 用户登录验证
     * 
     * @param user
     * @return 若用户存在，返回用户信息；若不存在，返回null。
     * @throws Exception
     */
    public User loginCheckTmp(User loginuser) throws Exception {
	User dbUser = new User(Env.get("manager_account"),
		Env.get("admin_password"));
	User readOnlyUser = new User(Env.get("cdn_manager_account"),
		Env.get("cdn_admin_password"));
	List<String> usernameList = new ArrayList<String>();
	usernameList.add(dbUser.getUsername());
	usernameList.add(readOnlyUser.getUsername());
	if (!usernameList.contains(loginuser.getUsername())) {
	    return null;
	}
	if (dbUser.getUsername().equals(loginuser.getUsername())) {
	    if (!dbUser.getPassword().equalsIgnoreCase(
		    MD5Util.getStringMD5String(loginuser.getPassword()))) {
		throw new ServiceException("用户名或密码错误");
	    }
	    return dbUser;
	} else {
	    if (!readOnlyUser.getPassword().equalsIgnoreCase(
		    MD5Util.getStringMD5String(loginuser.getPassword()))) {
		throw new ServiceException("用户名或密码错误");
	    }
	    return readOnlyUser;
	}

    }

    /**
     * 根据用户名查询用户对象
     * 
     * @param username
     * @return
     */
    public User selectByUsername(String username) {
	UserExample ue = new UserExample();
	ue.createCriteria().andUsernameEqualTo(username);
	List<User> userList = this.um.selectByExample(ue);
	if (userList.size() > 0) {
	    return userList.get(0);
	} else {
	    return null;
	}
    }

    /**
     * 修改用户密码
     * 
     * @param user
     * @return
     */
    public boolean updatePassword(User user) {
	if (user != null) {
	    user.setDefaultpwd("");
	    if (this.um.updateByPrimaryKey(user) > 0) {
		return true;
	    }
	}
	return false;
    }

    private List<User> userList;

    /**
     * 增加userlist 的设置方法 方便有新用户注册时user更新缓存
     * 
     * @return
     */

    public void setUserList(List<User> userList) {
	this.userList = userList;
    }

    @PostConstruct
    public void reload() {
	log.info("user - 初始化 用户信息 存入缓存");
	this.userList = this.um.selectByExample(new UserExample());
    }

    public List<User> selectAllUser() {
	return this.userList;
    }

    public User getUserById(Long userId) {
	for (User user : this.userList) {
	    if (userId.longValue() == user.getId().longValue()) {
		return user;
	    }
	}
	return null;
    }

    public JSONObject getDataGridJson(int page, int rows, String email) {
	JSONObject dataGridJson = new JSONObject();
	JSONArray data = new JSONArray();
	List<User> userList = new ArrayList<User>();
	if ("".equals(email)) {
	    userList = this.selectAllUser();
	} else {
	    List<User> list = this.selectByEmail("%" + email + "%");
	    if (list.size() > 0) {
		userList = list;
	    } else {
		User u = new User();
		u.setDefaultpwd("----");
		u.setId(null);
		u.setUsername("用户不存在");
		u.setType("-----");
		u.setEmail("-----");
		userList.add(u);
	    }

	}
	int begin = (page - 1) * rows;
	int end = userList.size() < page * rows ? userList.size() : page * rows;
	for (int i = begin; i < end; i++) {
	    JSONObject jo = new JSONObject();
	    jo.put("id", userList.get(i).getId());
	    jo.put("username", userList.get(i).getUsername());
	    jo.put("type", userList.get(i).getType());
	    jo.put("defaultpwd", userList.get(i).getDefaultpwd());
	    jo.put("email", userList.get(i).getEmail());
	    if (userList.get(i).getId() == null) {// 用户不存在时设值
		jo.put("opt", "");
	    }
	    data.add(jo);
	}
	dataGridJson.put("rows", data);
	dataGridJson.put("total", userList.size());
	return dataGridJson;
    }

    /**
     * @param user
     * @return 更新或者插入user数据
     * @throws Exception
     */
    public boolean updateOrSaveMenu(List<User> userLst) throws Exception {
	if (userLst != null && userLst.size() > 0) {
	    for (User user : userLst) {
		if (user.getId() != null) {
		    User tmp = this.getUserById(user.getId());
		    user.setPassword(tmp.getPassword());
		    if (this.um.updateByPrimaryKey(user) > 0) {
		    } else {
			this.reload();
			return false;
		    }
		} else {
		    String pwd = StringUtil.genRandom(8);
		    user.setPassword(MD5Util.getStringMD5String(pwd));
		    user.setDefaultpwd(pwd);
		    if (this.um.insert(user) > 0) {
		    } else {
			this.reload();
			return false;
		    }
		}
	    }
	    this.reload();
	    return true;
	}

	return false;
    }

    /**
     * @param userId
     * @return 删除用户
     */
    public boolean deleteUser(Long userId) {
	if (this.um.deleteByPrimaryKey(userId) > 0) {
	    return true;
	}
	return false;
    }

    /**
     * @Title: setDefaultPass
     * @Description: TODO 重置密码为默认密码
     * @param @param userId
     * @param @throws Exception 设定文件
     * @return void 返回类型
     * @throws
     */
    public boolean setDefaultPass(Long userId) throws Exception {
	User user = this.getUserById(userId);
	String pwd = StringUtil.genRandom(8);
	user.setPassword(MD5Util.getStringMD5String(pwd));
	user.setDefaultpwd(pwd);
	if (this.um.updateByPrimaryKey(user) > 0) {
	    this.reload();
	    return true;
	}
	return false;
    }

    /**
     * @Title: formateComboBoxData
     * @Description: TODO 返回下拉列表的数据源
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    public String formateComboBoxData() {
	List<User> userList = this.selectAllUser();
	JSONArray jsonArray = new JSONArray();
	for (User user : userList) {
	    JSONObject jo = new JSONObject();
	    jo.put("id", user.getId());
	    jo.put("name", user.getUsername());
	    jsonArray.add(jo);
	}
	return jsonArray.toString();
    }

    /**
     * 根据userid查询user对象
     * 
     * @method: UserService selectByUserid
     * @param userid
     * @return User
     * @2014, by liuchangfu.
     */
    public User selectByUserid(Integer userid) {
	UserExample ue = new UserExample();
	ue.createCriteria().andUseridEqualTo(userid);
	List<User> userList = this.um.selectByExample(ue);
	if (userList.size() > 0) {
	    return userList.get(0);
	} else {
	    return null;
	}
    }

    /**
     * 根据邮箱查找用户 集合
     * 
     * @param email
     * @return
     */
    public List<User> selectByEmail(String email) {
	UserExample userm = new UserExample();
	Criteria criteria = userm.createCriteria();
	criteria.andEmailLike("%" + email + "%");
	List<User> list = um.selectByExample(userm);
	return list;
    }
    /**
     * 根据userid查询用户
     * 
     * @method: UserService  getUserByUserid
     * @param userid
     * @return  User
     * @create date： 2015年2月2日
     * @2015, by liuchangfu.
     */
    public User getUserByUserid(String userid) {
	UserExample ue = new UserExample();
	ue.createCriteria().andUseridEqualTo(Integer.parseInt(userid));
	List<User> userList = this.um.selectByExample(ue);
	if (userList.size() > 0) {
	    return userList.get(0);
	} else {
	    return null;
	}
    }
    
    /**
     * 
     * 根据配置文件allowUser获取user 集合
     * @method: UserService  getUserList
     * @param users
     * @return  List<String>
     * @create date： 2015年2月12日
     * @2015, by liuchangfu.
     */
    public List<String> getUserList(String users) {
	List<String> userList = new ArrayList<String>();
	if (users.contains(",")) {
	    String[] srcs = users.split(",");
	    userList = Arrays.asList(srcs);
	}
	return userList;
    }
}
