package com.letv.cdn.manager.exception;
/**
 * 没有写的操作权限异常
 * @author liuchangfu
 *
 */
public class NoWriteAuthorityException extends RuntimeException {
	
	private String url = "/norole";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2358079656297510445L;
	
	public NoWriteAuthorityException(String message){
		super(message);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
