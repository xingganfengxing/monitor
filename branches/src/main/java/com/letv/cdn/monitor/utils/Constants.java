package com.letv.cdn.monitor.utils;

import com.letv.cdn.manager.utils.Env;

public class Constants{
    public static final String SEND_SMS = "send&message";
    public static final String MAKE_PHONE_CALL = "make&phone&call";
    
    public static final String SYSTEM_STATUS_OK = "OK";
    public static final String SYSTEM_STATUS_ERROR = "ERROR";
    
    public static final String CDN_ERROR_KEY = Env.get("rediskey_req_err");

    public static final String COMMA = ",";

    public static final String DAYTIME_FORMAT="yyyyMMdd";
    
}
