package com.letv.cdn.manager.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.letv.cdn.manager.dao.UserMapper;
import com.letv.cdn.manager.exception.ServiceException;
import com.letv.cdn.manager.pojo.User;
import com.letv.cdn.manager.pojo.UserExample;
import com.letv.cdn.manager.pojo.UserExample.Criteria;


@Service(value="cdnBaseUserService")
public class BaseUserService {

    @Resource
    private UserMapper um;

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
	c.andEmailEqualTo(loginuser.getEmail());
	userList = this.um.selectByExample(ue);
	if (userList.size() > 0) {
	    User dbUser = userList.get(0);
	    if (!loginuser.getPassword().equalsIgnoreCase(dbUser.getEmail())) {
		throw new ServiceException("用户名或密码错误");
	    }
	    return dbUser;
	}
	return null;
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
     * 根据用户的userid查询用户信息
     * 
     * @method: UserService getUserByUserid
     * @param userid
     * @return User
     * @createDate： 2014年10月16日
     * @2014, by chenyuxin.
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
}
