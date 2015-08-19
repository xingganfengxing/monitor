<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>日志数据接收Redis监控</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/echarts/echarts-plain.js"></script>
</head>
<body>
    <h1>日志数据接收Redis监控</h1>
    未接收日志文件：${queueLen}个，接收异常日志文件：${errorQueueLen}个<br><br>
    <div id="tt" class="easyui-tabs" style="width:auto;">
         <div title="今日" style="width:100%">
            <table id="dg1" class="easyui-datagrid" style="width:auto;height:600px;"
            rownumbers="true" fitColumns="true" singleSelect="true">
            </table>
        </div>
        <div title="昨日" style="width:100%">
            <table id="dg" class="easyui-datagrid" style="width:auto;height:600px;"
            rownumbers="true" fitColumns="true" singleSelect="true">
            </table>
        </div>
    </div>
</body>
<script type="text/javascript">
    function loadMinuteDetail(dg,dayMinuteDetail){
        $('#'+dg).datagrid({
            url:"/getrds?dayMinuteDetail="+dayMinuteDetail,
            columns:[[
                {field:'minute',width:60,title:'时间'},
                {field:'minuteFileCount',width:60,title:'文件数(分钟)'},
                {field:'secondLogCount',width:60,title:'接收条数(秒)'},
                {field:'secondLogFailCount',width:60,title:'过滤条数(秒)'},
                {field:'secondLogSuccRate',width:60,title:'接收比例'},
                {field:'secondLogSize',width:60,title:'日志大小(秒)'},
                {field:'minuteFileSize',width:60,title:'接收日志(Mb/分钟)'},
                {field:'redisQueueLen',width:60,title:'积压文件数'}
            ]]
        });
    }

    function loadTodayMinuteDetail(){
       loadMinuteDetail("dg1","todayMinuteDetail");
    }

    $(window).load(function() {
        loadMinuteDetail("dg","yesterDayMinuteDetail");
        loadMinuteDetail("dg1","todayMinuteDetail");
        timename = setInterval("loadTodayMinuteDetail();", 1000 * 30);
    });
</script>
</html>