package com.letv.cdn.report.controller;

import com.alibaba.fastjson.JSONObject;
import com.letv.cdn.manager.controller.common.DefaultController;
import com.letv.cdn.manager.utils.ResponseUtil;
import com.letv.cdn.report.pojo.BusinessNode;
import com.letv.cdn.report.pojo.PieNode;
import com.letv.cdn.report.pojo.VodPlatStat;
import com.letv.cdn.report.service.ReportService;
import com.letv.cdn.report.utils.ApiUtils;
import com.letv.cdn.report.utils.ConvertUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 统计报表
 * Created by liufeng1 on 2015/3/11.
 */
@Controller
public class ReportController extends DefaultController {

    @Resource
    private ReportService reportService;

    @RequestMapping("/reportmenu")
    public String reportMenue(Model m, HttpServletRequest rq) {

        List<Map<String, String>> menu = new ArrayList<Map<String, String>>();

        Map<String, String> m1 = new HashMap<String, String>();
        m1.put("class", "unselected");
        m1.put("id", "user");
        m1.put("url", rq.getContextPath() + "/bwquery");
        m1.put("name", "带宽查询");
        menu.add(m1);

        Map<String, String> m2 = new HashMap<String, String>();
        m2.put("class", "unselected");
        m2.put("id", "user");
        m2.put("url", rq.getContextPath() + "/bwdistribute");
        m2.put("name", "带宽分布");
        menu.add(m2);

        Map<String, String> m3 = new HashMap<String, String>();
        m3.put("class", "unselected");
        m3.put("id", "user");
        m3.put("url", "http://10.150.140.68:8000/data/weekreport/");
        m3.put("name", "带宽周报");
        menu.add(m3);

        m.addAttribute("menu", menu);
        return "/reportmenu";
    }

    @RequestMapping("/bwquery")
    public String bwQuery(Model m, HttpServletRequest rq) {
        return "/bwquery";
    }

    @RequestMapping("/bwdistribute")
    public String bwDistribute(Model m, HttpServletRequest rq) {
        return "/bwdistribute";
    }

    @RequestMapping("/getBusiness")
    public void getBusiness(HttpServletResponse rp) throws IOException {
        List<BusinessNode> list = reportService.getBusiness();
        BusinessNode businessNode = new BusinessNode();
        businessNode.setId("-1");
        businessNode.setText("所有业务线");
        list.add(businessNode);
        Collections.sort(list);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(list));
    }

    @RequestMapping("/getSubBusiness")
    public void getSubBusiness(HttpServletResponse rp) throws IOException {
        List<BusinessNode> list = getSubBusinessComm(null);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(list));
    }

    @RequestMapping("/getSubBusWithPlatId")
    public void getSubBusWithPlatId(HttpServletResponse rp, @RequestParam String platid) throws IOException {
        List<BusinessNode> list = getSubBusinessComm(platid);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(list));
    }

    @RequestMapping("getBwInfo")
    public void getBwInfo(HttpServletResponse rp,
                          @RequestParam String platId, @RequestParam String splatId,
                          @RequestParam String startTime,
                          @RequestParam String endTime) throws IOException {

        platId = ConvertUtils.convertPlatSplatId(platId);
        splatId = ConvertUtils.convertPlatSplatId(splatId);
        Map<String, String> bwMap = reportService.getBwMap(platId, splatId, startTime, endTime);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(bwMap));
    }

    @RequestMapping("getBwDistribute")
    public void getBwDistribute(HttpServletResponse rp,
                                @RequestParam String startTime) throws IOException {

        List<PieNode> list = reportService.getBwDistribute(startTime);
        for(int i=0; i<list.size(); i++){
            PieNode pieNode = list.get(i);
            list.get(i).setValue(ConvertUtils.convertBigDeciToMbs(pieNode.getValue()));
        }
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(list));
    }

    @RequestMapping("getBwSubDistribute")
    public void getBwSubDistribute(HttpServletResponse rp,@RequestParam String platId,
                                @RequestParam String startTime) throws IOException {

        Map<String,String> bwMap = reportService.getBwSubDistribute(ConvertUtils.convertPlatSplatId(platId),startTime);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(bwMap));
    }


    @RequestMapping("getPlatIdByPlatName")
    public void getPlatIdByPlatName(HttpServletResponse rp,@RequestParam String platName) throws IOException {
        ResponseUtil.sendJsonNoCache(rp,JSONObject.toJSONString(ApiUtils.getPlatIdByPlatName(platName)));
    }

    @RequestMapping("getVodPlatStats")
    public void getVodPlatStats(HttpServletResponse rp,
                                @RequestParam String platId, @RequestParam String splatId,
                                @RequestParam String startTime) throws IOException {

        platId = ConvertUtils.convertPlatSplatId(platId);
        splatId = ConvertUtils.convertPlatSplatId(splatId);
        List<VodPlatStat> list = reportService.getVodPlatStat(platId, splatId, startTime);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(list));
    }

    private List<BusinessNode> getSubBusinessComm(String platid) {
        List<BusinessNode> list = reportService.getSubBusiness(platid);
        BusinessNode businessNode = new BusinessNode();
        businessNode.setId("-1");
        businessNode.setText("所有子业务线");
        list.add(businessNode);
        Collections.sort(list);
        return list;
    }

    @RequestMapping("getTopPtime")
    public void getTopPtime(HttpServletResponse rp,@RequestParam String flag) throws IOException {
        DateTime dateTime = reportService.getDateTimeByTopPtime(flag);
        ResponseUtil.sendJsonNoCache(rp, JSONObject.toJSONString(dateTime.toString("yyyy-MM-dd HH:mm:ss")));
    }
}
