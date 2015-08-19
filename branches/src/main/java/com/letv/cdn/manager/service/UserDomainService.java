package com.letv.cdn.manager.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.letv.cdn.manager.dao.UserDomainMapper;
import com.letv.cdn.manager.pojo.UserDomain;
import com.letv.cdn.manager.pojo.UserDomainExample;
import com.letv.cdn.manager.pojo.UserDomainExample.Criteria;


@Service
public class UserDomainService{
    
    @Resource
    private UserDomainMapper userDomainMapper;
    
    private List<UserDomain> userDomainList;
    private static final Logger log = LoggerFactory.getLogger(UserDomainService.class);
    
    protected void reloadUserDomain(){
        log.info("UserDomainService : 缓存Userdomain信息");
        this.userDomainList = this.userDomainMapper.selectByExample(new UserDomainExample());
    }
    
    protected List<UserDomain> selectAllUserDomain(){
        if(this.userDomainList == null){
            this.reloadUserDomain();
        }
        return this.userDomainList;
    }
    
    /**
     * 如果存在该域名的信息则返回数据 否则返回null
     * @method: UserDomainService  selectByTag
     * @param domainTag
     * @return  UserDomain
     * @createDate： 2014年9月15日
     * @2014, by lvzhouyang.
     */
    protected List<UserDomain> selectByTag(String domainTag){
        List<UserDomain> tmpList = this.selectAllUserDomain();
        List<UserDomain> domainList = new ArrayList<UserDomain>();
        for(UserDomain tmpDomain : tmpList){
            if(tmpDomain.getDomainTag().equals(domainTag)){
                domainList.add(tmpDomain);
            }
        }
        return domainList;
    }
    
    /**
     * 
     * @method: UserDomainService  saveOrUpdate
     * @param userDomain
     * @return  boolean
     * @createDate： 2014年9月15日
     * @2014, by lvzhouyang.
     */
    protected boolean saveOrUpdate(UserDomain userDomain){
        if(userDomain != null){
            if(userDomain.getId() > 0){
                if(this.userDomainMapper.updateByPrimaryKey(userDomain) > 0){
                    return true;
                }
            }else{
                if(this.userDomainMapper.insert(userDomain) > 0){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 更加域名tag 删除所有该tag的对应关系
     * @method: UserDomainService  deleteByTag
     * @param domainTag
     * @return  boolean
     * @createDate： 2014年9月15日
     * @2014, by lvzhouyang.
     */
    protected boolean deleteByTag(String domainTag){
        log.info("UserDomainService : 根据Domain Tag 删除Userdomain信息");
        UserDomainExample udExample = new UserDomainExample();
        Criteria c = udExample.createCriteria();
        c.andDomainTagEqualTo(domainTag);
        
        if(this.userDomainMapper.deleteByExample(udExample) > 0){
            return true;
        }
        return false;
    }
    
    /**
     * 根据用户id 删除该用户的所有 域名用户对应关系
     * @method: UserDomainService  deleteByUserId
     * @param userId
     * @return  boolean
     * @createDate： 2014年9月15日
     * @2014, by lvzhouyang.
     */
    protected boolean deleteByUserId(long userId){
        log.info("UserDomainService : 根据UserId删除Userdomain信息");
        UserDomainExample udExample = new UserDomainExample();
        Criteria c = udExample.createCriteria();
        c.andUserIdEqualTo(userId);
        
        if(this.userDomainMapper.deleteByExample(udExample) > 0){
            return true;
        }
        return false;
    }
    
    protected boolean deleteyById(long userDomainId){
        if(this.userDomainMapper.deleteByPrimaryKey(userDomainId) > 0){
         return true;   
        }
        return false;
    }
    
    /**
     * 删除domaitag对应域名信息<br>
     * 2014-12-19
     * @author gao.jun
     * @param domaintag 
     * @return 如果没有删除域名信息，返回false，反之返回true
     */
	protected boolean deleteyByDomaintag(String domaintag) {
		UserDomainExample example = new UserDomainExample();
		UserDomainExample.Criteria c = example.createCriteria();
		c.andDomainTagEqualTo(domaintag);
		if (this.userDomainMapper.deleteByExample(example) > 0) {
			return true;
		}
		return false;
	}
    
    /**
     * 根据userId检索用户 --- 域名对应
     * @method: UserDomainService  selectByUserId
     * @param userId
     * @return  List<UserDomain>
     * @createDate： 2014年10月13日
     * @2014, by lvzhouyang.
     */
    protected List<UserDomain> selectByUserId(long userId) {
    
        List<UserDomain> tmpList = this.selectAllUserDomain();
        List<UserDomain> domainList = new ArrayList<UserDomain>();
        for (UserDomain tmpDomain : tmpList) {
            if (userId == tmpDomain.getUserId()) {
                domainList.add(tmpDomain);
            }
        }
        return domainList;
    }
    
    
}
