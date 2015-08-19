package com.letv.cdn.report.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.letv.cdn.manager.utils.StringUtil;
import com.letv.cdn.report.pojo.BusinessNode;
import com.letv.cdn.report.pojo.SNMappingModel;
import com.letv.cdn.report.pojo.SplatNode;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ApiUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUtils.class);

    /**
     * 获取serverip 与 cdnid对应关系
     * 
     * @return
     */
    public static final Map<String, String> getServerIpMapFromApi() {
        String url = "http://g3.letv.com/stat/hosts?cdnid=999999";
        Map<String, String> spMap = Maps.newHashMap();
        Gson gson = new Gson();
        String body = sendRequest(url);
        if (StringUtils.isNotEmpty(body)) {
            List<SNMappingModel> snmapList = gson.fromJson(body, new TypeToken<List<SNMappingModel>>(){
            }.getType());
            for (SNMappingModel model : snmapList) {
                if (model.getHost0() != null && StringUtils.isNotBlank(model.getHost0())) {
                    spMap.put(model.getHost0(), model.getCdnid());
                }
                if (model.getHost1() != null && StringUtils.isNotBlank(model.getHost1())) {
                    spMap.put(model.getHost1(), model.getCdnid());
                }
                if (model.getHost2() != null && StringUtils.isNotBlank(model.getHost2())) {
                    spMap.put(model.getHost2(), model.getCdnid());
                }
            }
        }
        LOGGER.info(url + "  被调用！！");
        return spMap;
    }
    /**
     * 获取serverip 与 cdnid对应关系 字符串
     * 
     * @return
     */
    public static final String getServerIpMapFromApiStr() {
        String url = "http://g3.letv.com/stat/hosts?cdnid=999999";
        Gson gson = new Gson();
        String body = sendRequest(url);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(body)) {
            List<SNMappingModel> snmapList = gson.fromJson(body, new TypeToken<List<SNMappingModel>>(){
            }.getType());
            for (SNMappingModel model : snmapList) {
                if (model.getHost0() != null && StringUtils.isNotBlank(model.getHost0())) {
                    sb.append(model.getHost0() + ":" + model.getCdnid()).append(",");
                }
                if (model.getHost1() != null && StringUtils.isNotBlank(model.getHost1())) {
                    sb.append(model.getHost1() + ":" + model.getCdnid()).append(",");
                }
                if (model.getHost2() != null && StringUtils.isNotBlank(model.getHost2())) {
                    sb.append(model.getHost2() + ":" + model.getCdnid()).append(",");
                }
            }
        }
        LOGGER.info(url + "  被调用！！");
        return sb.toString();
    }
    /**
     * 获取业务线
     * 
     * @return
     */
    public static final Map<String, SplatNode> getSplatidFromApi() {
        String url = "http://fid.oss.letv.com/gslb/splatid?format=1";
        Map<String, SplatNode> maps = new HashMap<String, SplatNode>();
        Gson gson = new Gson();
        String body = sendRequest(url);
        if (StringUtils.isNotEmpty(body)) {
            List<SplatNode> nodeList = gson.fromJson(body, new TypeToken<List<SplatNode>>(){
            }.getType());
            for (SplatNode node : nodeList) {
                String key = String.valueOf(node.getPlatid()) + "." + String.valueOf(node.getSplatid());
                maps.put(key, node);
            }
        }
        LOGGER.info(url + "  被调用！！");
        return maps;
    }
    /**
     * 获取业务线字符串拼接
     * 
     * @return
     */
    public static final String getSplatidFromApiStr() {
        String url = "http://fid.oss.letv.com/gslb/splatid?format=1";
        Gson gson = new Gson();
        String body = sendRequest(url);
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(body)) {
            List<SplatNode> nodeList = gson.fromJson(body, new TypeToken<List<SplatNode>>(){
            }.getType());
            for (SplatNode node : nodeList) {
                String key = String.valueOf(node.getPlatid()) + "." + String.valueOf(node.getSplatid());
                String value = node.getPlatname().trim() + "#@#" + node.getSplatname().trim();
                sb.append(key + ":" + value).append(",");
            }
        }
        LOGGER.info(url + "  被调用！！");
        return sb.toString();
    }
    /**
     * 请求接口 获取返回值
     *
     * @method: ApiUtils sendRequest
     * @param url
     * @return String
     * @createDate： 2015年1月28日
     * @2015, by lvzhouyang.
     */
    private static final String sendRequest(String url) {
        String body = "";
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        // 设置timeout
        method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        int statusCode;
        try {
            if (null != method && null != method.getParams()) {
                statusCode = client.executeMethod(method);
                // 判断访问状态码
                if (statusCode != 200) {
                    LOGGER.error("获取映射关系失败");
                    return body;
                }
                body = method.getResponseBodyAsString();
            }
        } catch (HttpException e) {
            LOGGER.error("请求映射关系失败", e);
        } catch (IOException e) {
            LOGGER.error("请求映射关系失败", e);
        }
        return body;
    }

    /**
     * 获取所有一级业务线
     * @return
     */
    public static List<BusinessNode> getBusiness(){
        List<BusinessNode> list = new ArrayList<>();
        Map<String, SplatNode> map = ApiUtils.getSplatidFromApi();
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, SplatNode> entry : map.entrySet()) {
            if(!set.contains(entry.getValue().getPlatid())){
                BusinessNode businessNode = new BusinessNode();
                businessNode.setId(entry.getValue().getPlatid());
                businessNode.setText(entry.getValue().getPlatname());
                list.add(businessNode);
                set.add(entry.getValue().getPlatid());
            }
        }
        return list;
    }

    /**
     * 根据一级业务线platName获取platId
     * @param platName
     * @return
     */
    public static String getPlatIdByPlatName(String platName){
        if("CDN".equals(platName)){
            return "102";
        }

        List<BusinessNode> list = getBusiness();
        for(BusinessNode businessNode : list){
            if(platName.equals(businessNode.getText())){
                return businessNode.getId();
            }
        }
        return "";
    }

    /**
     * 获取所有二级业务线
     * @return
     */
    public static List<BusinessNode> getSubBusiness(String platid){
        List<BusinessNode> list = new ArrayList<>();
        Map<String, SplatNode> map = ApiUtils.getSplatidFromApi();
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, SplatNode> entry : map.entrySet()) {
            if(!set.contains(entry.getValue().getSplatid())){
                if(StringUtils.isEmpty(platid) || platid.equals(entry.getValue().getPlatid())){
                    BusinessNode businessNode = new BusinessNode();
                    businessNode.setId(entry.getValue().getSplatid());
                    businessNode.setText(entry.getValue().getSplatname());
                    list.add(businessNode);
                    set.add(entry.getValue().getSplatid());
                }
            }
        }
        return list;
    }
}
