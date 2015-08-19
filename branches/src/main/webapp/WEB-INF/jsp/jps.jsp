<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>集群进程监控</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript">

function loadlist(){
    $("#dg").datagrid({
        url:"/jps/showlist",
        columns:[[
                  {field:'progress',width:60,title:'进程名'},
                  {field:'ip',width:60,title:'ip'},
                  {field:'datetime',width:60,title:'扫描时间'},
                  {field:'alive',width:60,title:'状态',
                	  formatter:function(value){
                	        if(value==true)
                	                return '正常';
                	        else
                	                return '不正常';
                	        }},
                  {field:'description',width:60,title:'状态描述'},
                 
        ]]
    });
}

$(document).ready(function() {
	loadlist();
    timename = setInterval("loadlist();", 1000 * 120);
});

</script>

</head>
<body>
    <div id="tt" class="easyui-tabs" style="width:auto;">
        <div title="集群进程监控" style="width:100%">
            <table id="dg" class="easyui-datagrid" style="width:auto;height:600px;"
            rownumbers="true" fitColumns="true" singleSelect="true">
            </table>
        </div>
    </div>
</body>
</html>