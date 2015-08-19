/*
 * Created on Dec 8, 2007
 */
package com.letv.cdn.manager.utils;

import static java.net.URLEncoder.encode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lichao
 */
public class ResponseUtil {

    public static String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    public static String CONTENT_TYPE_XML = "text/xml;charset=UTF-8";

    public static String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

    public static void sendJsonNoCache(HttpServletResponse response, String message) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        sendMessage(response, message);
    }



    public static void sendJsonpNoCache(HttpServletResponse response, String callback, String message) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        setNoCacheHeader(response);
        sendMessage(response, callback + "(" + message + ")");
    }



    public static void sendMessageNoCache(HttpServletResponse response, String message, String contentType) throws IOException {
        response.setContentType(contentType);
        setNoCacheHeader(response);
        sendMessage(response, message);
    }
    public static void sendMessageWithCache(HttpServletResponse response, String message, String contentType, int cacheSec) throws IOException {
        response.setContentType(contentType);
        setCacheHeader(response, cacheSec);
        sendMessage(response, message);
    }



    private static void sendMessage(HttpServletResponse response, String message) throws IOException {
        StringBuilder sb = new StringBuilder();
        //response.setContentLength(message.getBytes("utf-8").length);
        java.io.PrintWriter writer = response.getWriter();
        sb.append(message);
        writer.write(sb.toString());
        response.flushBuffer();
    }



    /**
     * 设置cache header
     *
     * @param response
     * @param cacheSec
     */
    public static void setCacheHeader(HttpServletResponse response, int cacheSec) {
        // HTTP 1.0
        response.setHeader("Pragma", "Public");
        // HTTP 1.1
        response.setHeader("Cache-Control", "max-age=" + cacheSec);
        response.setDateHeader("Expires", System.currentTimeMillis() + cacheSec * 1000L);
    }



    /**
     * 设置no-cache header
     *
     * @param response
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        // HTTP 1.0
        response.setHeader("Pragma", "No-cache");
        // HTTP 1.1
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /**
     * 设置 允许跨域
     * @param response
     */
    public static void setCrossDomain(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Headers","Origin, No-Cache, X-Requested-With,Content-Range, X_FILENAME, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
    }



    public static String addParam(String url, String paramValuePair) {
        return url + (url.contains("?") ? "&" : "?") + paramValuePair;
    }



    public static String addParam(String url, JSONObject paramValuePair) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramValuePair.entrySet()) {
            if (sb.length() > 0) sb.append('&');
            try {
                sb.append(encode(entry.getKey(), "UTF-8")).append('=').append(encode(entry.getValue().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url + (url.contains("?") ? "&" : "?") + sb;
    }
}
