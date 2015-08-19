package com.letv.cdn.monitor.task;

import com.letv.cdn.manager.utils.Env;
import com.letv.cdn.monitor.common.FixedFIFOLinkedList;
import com.letv.cdn.monitor.common.FixedFIFOList;
import com.letv.cdn.monitor.pojo.OffSetInfo;
import com.letv.log.monitor.message.OffsetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时检测 Storm offset kafka offset 以及差值
 *
 * @author lvzhouyang
 * @createDate 2015年3月10日
 */
public class OffSetCheckTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger("OffSetCheckTask");
    private static final int DEFAULT_SIZE = 10;

    private static String jarpath = Env.get("kafka_jar_path");
    //点播 cdn信息
    private static String vodtopic = "cdn-log-beta";
    private static String vodinzkpath = "/cdn-calc-beta/cdn-calc-beta";

    //topurl
    private static String topUrlTopic = "cdn-log-beta";
    private static String topUrlZkPath = "/top-url-beta/top-url-beta";

    //playerclient
    private static String playerClientTopic = "client-all-log";
    private static String playerClientZkPath = "/playerclient-beta/playerclient-beta";

    //p2p
    private static String p2pTopic = "p2p-log-test";
    private static String p2pZkPath = "/kafka-conssumer-p2p/kafka-conssumer-p2p";

    //rtmp
    private static String rtmpTopic = "live-server-report";
    private static String rtmpZkPath = "/live-calc-beta/live-calc-beta";

    //flv
    private static String flvTopic = "live-server-flv";
    private static String flvZkPath = "/flv-server/flv-server";

    //hls
    private static String hlsTopic = "live-server-hls";
    private static String hlsZkPath = "/hls-server/hls-server";

    private static String zkhosts = "10.182.200.37,10.182.200.21,10.182.200.39";

    private static FixedFIFOList<OffSetInfo> vodcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> topcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> clientcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> p2pcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> rtmpcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> flvcachedOffSetInfos;
    private static FixedFIFOList<OffSetInfo> hlscachedOffSetInfos;

    public OffSetCheckTask() {

        super();
        vodcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        topcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        clientcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        p2pcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        rtmpcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        flvcachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
        hlscachedOffSetInfos = new FixedFIFOLinkedList<>(DEFAULT_SIZE);
    }

    public OffSetCheckTask(int listSize) {

        super();
        vodcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        topcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        clientcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        p2pcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        rtmpcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        flvcachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
        hlscachedOffSetInfos = new FixedFIFOLinkedList<>(listSize);
    }

    @Override
    public void run() {

        try {
            LOGGER.info("check offset start");

            getLastInfo(vodtopic, vodinzkpath, jarpath, zkhosts, vodcachedOffSetInfos);
            getLastInfo(topUrlTopic, topUrlZkPath, jarpath, zkhosts, topcachedOffSetInfos);
            getLastInfo(playerClientTopic, playerClientZkPath, jarpath, zkhosts, clientcachedOffSetInfos);
            getLastInfo(p2pTopic, p2pZkPath, jarpath, zkhosts, p2pcachedOffSetInfos);
            getLastInfo(rtmpTopic, rtmpZkPath, jarpath, zkhosts, rtmpcachedOffSetInfos);
            getLastInfo(flvTopic, flvZkPath, jarpath, zkhosts, flvcachedOffSetInfos);
            getLastInfo(hlsTopic, hlsZkPath, jarpath, zkhosts, hlscachedOffSetInfos);

            LOGGER.info("check offset finish");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static void getLastInfo(String topic, String zkpath, String jarpath, String zkhosts, FixedFIFOList<OffSetInfo> cacheList) throws Exception {

        OffSetInfo tmpInfo = new OffSetInfo();
        long kfkOffset = OffsetUtils.getOffSetInKafka(jarpath, topic);
        long zkOffset = OffsetUtils.getOffSetInZk(zkhosts, zkpath);
        tmpInfo.setOffSetInKafka(kfkOffset);
        tmpInfo.setOffSetInStorm(zkOffset);
        cacheList.addLastSafe(tmpInfo);
    }

    /**
     * @return FIFO<OffSetInfo>
     * @method: OffSetCheckTask getrOffSetInfos
     * @createDate： 2015年3月11日
     * @2015, by lvzhouyang.
     */
    public static FixedFIFOList<OffSetInfo> getvodOffSetInfos() {

        return vodcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> gettopOffSetInfos() {

        return topcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> getclientOffSetInfos() {

        return clientcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> getP2pOffSetInfos() {

        return p2pcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> getRtmpOffSetInfos() {

        return rtmpcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> getFlvOffSetInfos() {

        return flvcachedOffSetInfos;
    }

    public static FixedFIFOList<OffSetInfo> getHlsOffSetInfos() {

        return hlscachedOffSetInfos;
    }

}
