package com.letv.cdn.manager.exception;

/**
 * 登录失败的不同提示
 * 日期：2014年7月10日10:10:00
 * @author chenyuxin
 *
 */
public class ServiceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6617611651084440676L;

	public ServiceException(String message){
		super(message);
	}
}
