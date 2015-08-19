package com.letv.cdn.monitor.task;

import com.google.common.collect.Lists;
import com.letv.cdn.monitor.utils.Constants;
import com.letv.cdn.monitor.utils.ShardJedis;
import org.joda.time.DateTime;

import java.util.List;

/**
 * 删除过期的redis key的定时任务
 *
 * @author liufeng1
 * @date 28/5/2015
 */
public class ClearRedisKeyTask implements Runnable {


    private ShardJedis shardJedis;

    public ClearRedisKeyTask(String ips,int db){
        shardJedis = new ShardJedis(ips,db);
    }

    @Override
    public void run() {
        DateTime dateTime = DateTime.now();
        List<String> keyList = Lists.newArrayList();
        for (int i = 2; i <= 7; i++) {
            String day = dateTime.minusDays(i).toString(Constants.DAYTIME_FORMAT);
            keyList.add("RTMP|" + day);
            keyList.add("HLS|" + day);
            keyList.add("FLV|" + day);
        }
        shardJedis.delKey(keyList);
    }
}
