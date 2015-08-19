package com.letv.cdn.report.dao;

import com.letv.cdn.report.pojo.BwQuery;
import com.letv.cdn.report.pojo.PieNode;
import com.letv.cdn.report.pojo.XyNode;
import com.letv.cdn.report.pojo.VodPlatStat;

import java.util.List;

public interface VodPlatStatMapper {

    List<VodPlatStat> selectBwByQuery(BwQuery bwQuery);

    List<VodPlatStat> getByStartTime(BwQuery bwQuery);

    List<PieNode> getBwByQuery(BwQuery bwQuery);

    List<XyNode> getLineNodeBwByQuery(BwQuery bwQuery);

    List<XyNode> getBwSubByQuery(BwQuery bwQuery);

    String getEndPtime(BwQuery bwQuery);

    String getStartPtime(BwQuery bwQuery);
}