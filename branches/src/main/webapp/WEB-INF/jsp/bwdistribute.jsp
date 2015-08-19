<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>带宽分布</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/echarts/echarts-plain.js"></script>
</head>
<body style="color:black;font-size:12px">
	<h1>带宽分布</h1>
    <div id="time" style="padding-top:10px;color:black;font-size:12px">
        <span>业 务 线：</span>
        <input class="easyui-combobox" id="businessInput"
            data-options="url:'/getBusiness',valueField:'id',textField:'text',panelHeight:'auto'"/>
         <span>二级业务线：</span>
         <input class="easyui-combobox" id="subBusinessInput"
            data-options="url:'/getSubBusiness',valueField:'id',textField:'text',panelHeight:'400'"/>
       <span>时间点：</span>
       <input id="startTime" type="text" name="startTime" class="easyui-datetimebox"/>
       <a href="#" id="query" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>
       <a href="#" id="undo" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
    </div>
    <div style="padding-top:10px;"></div>
    <div id="bwDiv" style="height:400px;width:68%;float: left;border: 0.2em #94DBD8 solid;"></div>
    <div id="bwDetail" style="height:800px;width:30%;float: right;align:center;">
         <table id="dg1" class="easyui-datagrid" style="width:auto;height:800px;"
            rownumbers="true" fitColumns="true" singleSelect="true">
         </table>
    </div>
    <div id="subBwDiv" style="height:400px;width:68%;float: left;border: 0.2em #94DBD8 solid;border-top:0px"></div>
</body>
<script type="text/javascript">
    $(window).load(function() {
        $('#undo').bind('click', function(){  
            $("input").val("");
        });

        $('#query').bind('click', function(){  
            getBwDistribute();
            getBwSubDistribute('');
            loadVodPlatStat('');
        });

        $('#businessInput').combobox({
            onSelect:function(obj){ 
                $('#subBusinessInput').combobox('reload',"/getSubBusWithPlatId?platid="+obj.id);
                $('#subBusinessInput').combobox('setValue','-1');
            }
        });

        $('#dg1').datagrid({
            columns:[[
                {field:'platname',width:100,title:'一级业务线'},
                {field:'splatname',width:200,title:'二级业务线'},
                {field:'bwStr',width:200,title:'带宽'}
            ]]
        });

        $('#businessInput').combobox('setValue','-1');
        $('#subBusinessInput').combobox('setValue','-1');
        setDefaultStartTime();
        getBwDistribute();
        getBwSubDistribute('');
        loadVodPlatStat('');
    });

    function setDefaultStartTime(){
        $.ajax({
            url : "${ctx}/getTopPtime?flag=end",
            type : "get",
            success : function(data) {
               $('#startTime').datetimebox('setValue',data)
            }
        });
    }

    function loadVodPlatStat(platid){
        if(platid ==''){
            platid = $('#businessInput').combobox('getValue');
        }
        var splatid = $('#subBusinessInput').combobox('getValue');
        var startTime = $('#startTime').datetimebox('getValue');
        var params = "?platId=" + platid + "&splatId="
                  + splatid + "&startTime=" + startTime;
        $.ajax({
            url : "${ctx}/getVodPlatStats" + params,
            type : "get",
            success : function(data) {
                $('#dg1').datagrid({
                    data:data
                });
            }
        });
    }

    function getBwDistribute() {
        var startTime = $('#startTime').datetimebox('getValue');
        var params = "?startTime=" + startTime;

        $.ajax({
            url : "${ctx}/getBwDistribute" + params,
            type : "get",
            success : function(data) {
                makeChart(data);
            }
        });
    }

    function getBwSubDistribute(platid) {
        if(platid ==''){
            platid = $('#businessInput').combobox('getValue');
        }
        var startTime = $('#startTime').datetimebox('getValue');
        var params = "?platId=" + platid + "&startTime=" + startTime;
        $.ajax({
            url : "${ctx}/getBwSubDistribute" + params,
            type : "get",
            success : function(data) {
                makeBarChart(data);
            }
        });
    }

    function makeChart(data) {
        var piechart = echarts.init(document.getElementById('bwDiv'));
        var businessName = new Array();
        //遍历
        for (i in data) {
            businessName[i] = data[i].name;
        }
        var option = initPieOption("业务线带宽", businessName,data,'带宽使用量');
        piechart.setOption(option, true);
        piechart.on('click', function (param) {
            $.ajax({
                url : "${ctx}/getPlatIdByPlatName?platName=" + param.name,
                type : "get",
                success : function(platid) {
                    getBwSubDistribute(platid);
                    loadVodPlatStat(platid);
                }
            });
        });
    }

    function makeBarChart(data) {
        var barchart = echarts.init(document.getElementById('subBwDiv'));
        barchart.setOption(initBarOption("二级业务线带宽", data.x.split(","),data.y.split(","),'带宽使用量'));
    }

    //根据标题，x轴 y轴数据初始化echarts option
    function initPieOption(title, xdata, data,yname) {
        var option = {
            title : {
                text: '一级业务线带宽分布',
                x:'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient : 'vertical',
                x : 'left',
                data:xdata
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            series : [
                {
                    name:'一级业务线',
                    type:'pie',
                    radius : '55%',
                    center: ['50%', '60%'],
                    data:data
                }
            ]
        };
        return option;
    }

    //根据标题，x轴 y轴数据初始化echarts option
    function initBarOption(title, xdata, ydata,yname) {
        var option = {
            title : {
                text: '二级业务线带宽分布',
            },
            tooltip : {
                trigger: 'axis'
            },
            toolbox: {
                show : true,
                feature : {
                    mark : {show: true},
                    dataView : {show: true, readOnly: false},
                    restore : {show: true},
                    saveAsImage : {show: true}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    data : xdata
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'带宽',
                    type:'bar',
                    data:ydata,
                    markPoint : {
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine : {
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    }
                }
            ]
        };
        return option;
    }
</script>
</html>