package com.letv.cdn.report.service;

import com.letv.cdn.report.dao.VodPlatStatMapper;
import com.letv.cdn.report.pojo.*;
import com.letv.cdn.report.utils.ApiUtils;
import com.letv.cdn.report.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liufeng1 on 2015/3/16.
 */
@Service(value = "reportService")
public class ReportService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private String pattern = "yyyy-MM-dd HH:mm";

    @Resource
    private VodPlatStatMapper vodPlatStatMapper;

    public List<BusinessNode> getBusiness() {
        return ApiUtils.getBusiness();
    }

    public List<BusinessNode> getSubBusiness(String platid) {
        return ApiUtils.getSubBusiness(platid);
    }

    public Map<String, String> getBwMap(String platId, String splatId, String startTime, String endTime) {
        Map<String, String> bwMap = new HashMap<>();

        StringBuilder xBuilder = new StringBuilder("");
        StringBuilder yBuilder = new StringBuilder("");

        BwQuery bwQuery = new BwQuery();
        bwQuery.setPlatid(platId);
        bwQuery.setSplatid(splatId);
        bwQuery.setStartTime(startTime);
        bwQuery.setEndTime(endTime);
        bwQuery.setTableName(getTableName(startTime));

        List<XyNode> list = vodPlatStatMapper.getLineNodeBwByQuery(bwQuery);

        for (XyNode lineNode : list) {
            xBuilder.append(lineNode.getxKey() + ",");
            yBuilder.append(ConvertUtils.convertToMbs(lineNode.getyValue()) + ",");
        }

        bwMap.put("x", xBuilder.toString());
        bwMap.put("y", yBuilder.toString());
        return bwMap;
    }

    public List<PieNode> getBwDistribute(String startTime) {
        BwQuery bwQuery = new BwQuery();
        String toPattern = "yyyyMMddHHmm";
        bwQuery.setStartTime(getFiveIntervalStartTime(startTime, toPattern));
        bwQuery.setTableName(getTableName(startTime));
        return vodPlatStatMapper.getBwByQuery(bwQuery);
    }

    public Map<String, String> getBwSubDistribute(String platId, String startTime) {
        Map<String, String> bwMap = new HashMap<>();

        StringBuilder xBuilder = new StringBuilder("");
        StringBuilder yBuilder = new StringBuilder("");

        BwQuery bwQuery = new BwQuery();
        bwQuery.setPlatid(platId);
        bwQuery.setTableName(getTableName(startTime));
        String toPattern = "yyyyMMddHHmm";
        bwQuery.setStartTime(getFiveIntervalStartTime(startTime, toPattern));

        List<XyNode> list = vodPlatStatMapper.getBwSubByQuery(bwQuery);

        for (XyNode xyNode : list) {
            xBuilder.append(xyNode.getxKey() + ",");
            yBuilder.append(ConvertUtils.convertToMbs(xyNode.getyValue()) + ",");
        }

        String xStr = xBuilder.toString();
        String yStr = yBuilder.toString();

        if (xStr.contains(",")) {
            xStr = xStr.substring(0, xStr.lastIndexOf(","));
            yStr = yStr.substring(0, yStr.lastIndexOf(","));
        }

        bwMap.put("x", xStr);
        bwMap.put("y", yStr);
        return bwMap;
    }

    public List<VodPlatStat> getVodPlatStat(String platId, String splatId, String startTime) {
        BwQuery bwQuery = new BwQuery();
        bwQuery.setPlatid(platId);
        bwQuery.setSplatid(splatId);
        String toPattern = "yyyyMMddHHmm";
        bwQuery.setStartTime(getFiveIntervalStartTime(startTime, toPattern));
        bwQuery.setTableName(getTableName(startTime));

        return vodPlatStatMapper.getByStartTime(bwQuery);
    }


    //根据startTime获取查询表名
    private String getTableName(String startTime) {
        if (!StringUtils.isEmpty(startTime)) {
            return "vod_plat_statistic_" + startTime.substring(0, 7).replace("-", "");
        } else {
            DateTime dateTime = new DateTime();
            if(dateTime.getDayOfMonth() == 1){
                return "vod_plat_statistic_" + dateTime.minusDays(1).toString("yyyyMM");
            }
            return "vod_plat_statistic_" + dateTime.toString("yyyyMM");
        }
    }

    //根据获取startTime 5分钟粒度
    private String getFiveIntervalStartTime(String startTime, String toPattern) {

        DateTime dateTime = null;
        if (StringUtils.isEmpty(startTime)) {
            dateTime = getDateTimeByTopPtime("end");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                Date date = sdf.parse(startTime);
                dateTime = new DateTime(date);
                int minute = dateTime.getMinuteOfHour();
                if (minute % 5 != 0) {
                    int fiveMinute = minute / 5 * 5 + 5;
                    if (60 != fiveMinute) {
                        dateTime = dateTime.withMinuteOfHour(fiveMinute);
                    } else {
                        dateTime = dateTime.plusHours(1).withMinuteOfHour(0);
                    }
                }
            } catch (ParseException e) {
                LOGGER.error(e.getMessage());
            }
        }

        return dateTime.toString(toPattern);
    }

    public DateTime getDateTimeByTopPtime(String flag) {
        DateTime dateTime = new DateTime();
        BwQuery bwQuery = new BwQuery();
        bwQuery.setTableName("vod_plat_statistic_" + dateTime.toString("yyyyMM"));

        //跨月分
        if(dateTime.getDayOfMonth() == 1){
            bwQuery.setTableName("vod_plat_statistic_" + dateTime.minusDays(1).toString("yyyyMM"));
        }

        String ptime = "";
        if("end".equals(flag)){
            ptime = vodPlatStatMapper.getEndPtime(bwQuery);
        }
        if("start".equals(flag)){
            ptime = vodPlatStatMapper.getStartPtime(bwQuery);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date date = sdf.parse(ptime);
            dateTime = new DateTime(date);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage());
        }
        return dateTime;
    }
}
