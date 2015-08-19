package com.letv.cdn.manager.controller.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.letv.cdn.manager.exception.NoLoginException;
import com.letv.cdn.manager.pojo.User;

/**
 * <br>
 * 
 * <b>Project</b> : uts-report<br>
 * <b>Create Date</b> : 2013-7-11<br>
 * <b>Company</b> : letv<br>
 * <b>Copyright @ 2013 letv – Confidential and Proprietary</b><br>
 * 
 * @author Chen Hao
 */
public abstract class BaseController {
    
    // private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    /**
     * 异常处理方法
     * @param e
     * @param request
     * @param response
     * @return
     */
    @ExceptionHandler({Exception.class})
    public abstract String exceptionHandler(Exception e, HttpServletRequest request, HttpServletResponse response) throws Exception;
    
    /**
     * 获取当前请求的request
     * @return
     */
    protected final HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
    
    /**
     * 获取当前登录用户
     * @return
     */
    protected final User getCurrentUser() {
        HttpSession session = this.getCurrentRequest().getSession();
        Object o = session.getAttribute("user");
        if (o == null) {
            throw new NoLoginException();
        } else {
            return (User) o;
        }
    }
    
    /**
     * 获取当前登录用户id
     * @return
     */
    protected final Long getCurrentUserId() {
        return this.getCurrentUser().getId();
    }
    
    /**
     * 获取当前登录用户名
     * @return
     */
    protected final String getCurrentUsername() {
        return this.getCurrentUser().getUsername();
    }
    
    /**
     * 得到请求url
     * @param request
     * @return
     */
    protected final String getCurrentRequestUrl() {
        HttpServletRequest request = this.getCurrentRequest();
        String queryP = request.getQueryString();
        queryP = queryP == null ? "" : queryP;
        return request.getRequestURL().append(queryP).toString();
    }
    
    /**
     * 得到请求 contextPath
     * @param request
     * @return
     */
    protected final String getCurrentRequestContextPath(){
        return this.getCurrentRequest().getContextPath();
    }
    

    /**
     * redirect到指定url
     * @param paths
     * @return
     */
    protected final String redirectToUrl(String ... paths) {
        StringBuffer s = new StringBuffer("");
        s = s.append(InternalResourceViewResolver.REDIRECT_URL_PREFIX);
        for (String p : paths) {
            s = s.append(p);
        }
        return s.toString();
    }
    
    /**
     * forward到指定url
     * @param paths
     * @return
     */
    protected final String forwardToUrl(String ... paths) {
        StringBuffer s = new StringBuffer("");
        s = s.append(InternalResourceViewResolver.FORWARD_URL_PREFIX);
        for (String p : paths) {
            s = s.append(p);
        }
        return s.toString();
    }
    
    /**
     * 根据参数返回viewName
     * @param paths
     * @return 
     */
    protected final String buildViewName(String ... paths) {
        StringBuffer s = new StringBuffer("");
        for (String p : paths) {
            s = s.append(p);
        }
        return s.toString(); 
    }
    
    
    /**
     * 判断是否是ajax请求
     * @author Chen Hao
     * @param request
     * @return
     */
    protected boolean isAjax(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return header != null && "XMLHttpRequest".equals(header);
    }
    
    /**
     * 根据e获取抛出此异常的方法名
     * @param e
     * @return
     */
    protected final String getThrowMethodName(Exception e) {
        StackTraceElement[] stes = e.getStackTrace();
        String thisClassName = this.getClass().getName();
        for (StackTraceElement ste : stes) {
            String throwClassName = ste.getClassName();
            if (throwClassName.startsWith(thisClassName)) {
                return ste.getMethodName();
            }
        }
        return "";
    }

    /**
     * 文件下载
     * 若文件名为null，将按"\"分割文件路径，取数组的最后索引的值作为文件名。
     * @param response 
     * @param filePath 文件路径
     * @param fileName 文件名
     * @throws IOException
     */
    public void download(HttpServletResponse response, String filePath, String fileName) throws IOException{
    	
    	if(fileName == null){
	    	String[] fileArray = filePath.split("\\\\");
	    	fileName = fileArray[fileArray.length-1];
    	}
		response.setHeader("Content-Disposition", "attachment; filename="+ new String(fileName.getBytes("gb2312"),"iso8859-1"));
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(response.getOutputStream());

			byte[] buff = new byte[2048];
			int bytesRead;

			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}

		} catch (IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
    }
}
