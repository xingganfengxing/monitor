/* 
 * DeleteRedisTask.java
 * 
 * Created on 2015年4月21日
 * 
 * Copyright(C) 2015, by 360buy.com.
 * 
 * Original Author: yangxuan
 * Contributor(s):
 * 
 * Changes 
 * -------
 * $Log$
 */
package com.letv.cdn.monitor.task;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import com.letv.cdn.monitor.pojo.TopurlTrafficDetail;
import com.letv.cdn.monitor.utils.WebApplicationUtils;

/**
 * 
 * 删除top 1000以外的数据
 *
 * @author yangxuan
 *
 * @version 1.0
 */
public class ExportRedisTask implements Runnable{
    private static final Logger LOG = LoggerFactory
            .getLogger("ExportRedisTask");
    private String[] ipArr;
    private ShardedJedisPool pool;
    private final int TOP = 199;
    private final int MINUS_DAYS = 2 ;
    private DataSource dataSource;
    private final static String INSERT_SQL = "insert into topurl_traffic_detail(log_date,business,url,traffic,hit,request,total_traffic) values(?,?,?,?,?,?,?)";
    
    public ExportRedisTask(String ips, DataSource dataSource) {
    
        ipArr = ips.split(",");
        this.dataSource = dataSource;
        JedisPoolConfig config = new JedisPoolConfig();// Jedis池配置
        List<JedisShardInfo> jdsInfoList = new ArrayList<JedisShardInfo>();
        for (String ip : ipArr) {
            jdsInfoList.add(new JedisShardInfo(ip));
        }
        pool = new ShardedJedisPool(config, jdsInfoList);
    }
    
    @Override
    public void run() {
    
        try {
            LOG.info("ExportRedisTask start.");
            // 获取前两天的所有TBW的key
            List<String> keys = this.getAllKeys();
            // 获取top的所有指标
            for (String key : keys) {
                // 根据keys获取urls
                this.mvDataFromRedisToDb(key);
            }
            
            LOG.info("ExportRedisTask finish.");
        } catch (Exception e) {
            LOG.error("", e);
        }
    }
    
    private void mvDataFromRedisToDb(String key) {
        LOG.info("start ExportRedisTask mvDataFromRedisToDb,key:{}.",key);
        List<TopurlTrafficDetail> details = this.getDetails(key);
        this.insert2Db(details);
        LOG.info("finish ExportRedisTask mvDataFromRedisToDb,key:{},fetchsize:{}.",key,details.size());
    }
    
    private void insert2Db(List<TopurlTrafficDetail> details) {
    
        try {
            QueryRunner runner = new QueryRunner(
                    dataSource);
            if (CollectionUtils.isNotEmpty(details)) {
                Object[][] params = new Object[details.size()][7];
                int i = 0;
                for (TopurlTrafficDetail detail : details) {
                    
                    params[i][0] = detail.getLogDate();
                    params[i][1] = detail.getBusiness();
                    params[i][2] = detail.getUrl();
                    params[i][3] = detail.getTraffic();
                    params[i][4] = detail.getHit();
                    params[i][5] = detail.getRequest();
                    params[i][6] = detail.getBwAll();
                    i++;
                }
                // 执行语句
                runner.batch(ExportRedisTask.INSERT_SQL, params);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        } 
        
    }
    
    private List<TopurlTrafficDetail> getDetails(String key) {
    
        List<TopurlTrafficDetail> details = new ArrayList<TopurlTrafficDetail>();
        ShardedJedis shardedJedis = pool.getResource();
        try {
            Set<Tuple> tuples = shardedJedis.zrevrangeWithScores(key, 0, TOP);
            TopurlTrafficDetail detail = null;
            if (CollectionUtils.isNotEmpty(tuples)) {
                String[] tmp = key.split("\\|");
                String tal = "TAL|" + tmp[1] + "|" + tmp[2];
                String tht = "THT|" + tmp[1] + "|" + tmp[2];
                String tto = "TTO|" + tmp[1] + "|" + tmp[2];
                String all = shardedJedis.hget(tal, "ALL");
                long bwall = Long.valueOf(all);
                for (Tuple tuple : tuples) {
                    detail = new TopurlTrafficDetail();
                    detail.setBwAll(bwall);
                    try {
                        String field = tuple.getElement();
                        long score = (long) tuple.getScore();
                        detail.setLogDate(StringUtils.replace(tmp[1], "-", ""));
                        detail.setBusiness(tmp[2]);
                        detail.setUrl(field);
                        detail.setTraffic(score);
                        // 取命中数
                        String hitS = shardedJedis.hget(tht, field);
                        if (StringUtils.isNotEmpty(hitS)) {
                            detail.setHit(Integer.valueOf(hitS));
                        }
                        // 取访问总数
                        String total = shardedJedis.hget(tto, field);
                        if (StringUtils.isNotEmpty(total)) {
                            detail.setRequest(Integer.valueOf(total));
                        }
                        details.add(detail);
                    } catch (Exception e) {
                        //
                        LOG.error("ExportRedisTask start. key:" + key, e);
                    }
                }
                shardedJedis.del(key);
                shardedJedis.del(tal);
                shardedJedis.del(tht);
                shardedJedis.del(tto);
            }
            return details;
        } finally {
            pool.returnResource(shardedJedis);
        }
    }
    
    private List<String> getAllKeys() {
    
        String date = new DateTime().minusDays(MINUS_DAYS).toString("yyyyMMdd");
        List<String> keys = new ArrayList<String>();
        for (String ip : ipArr) {
            Jedis jedis = new Jedis(ip);
            Set<String> set = jedis.keys("TBW|" + date + "|*");
            if(CollectionUtils.isNotEmpty(set)){
                keys.addAll(set);
            }
            jedis.close();
        }
        return keys;
    }
   
    public static void main(String[] args) {
    
        org.apache.commons.dbcp.BasicDataSource dataSource = new org.apache.commons.dbcp.BasicDataSource();
        dataSource.setUrl("jdbc:mysql://10.150.148.103:3306/cdn_report");
        dataSource.setUsername("cdn_test");
        dataSource.setPassword("cdn0Cdn");
        
        ExportRedisTask task = new ExportRedisTask(
                "10.182.200.30,10.182.200.31,10.182.200.32,10.182.200.33,10.182.200.34",
                dataSource);
        task.run();
        
    }
}
