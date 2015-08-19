<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>带宽查询</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/echarts/echarts-plain.js"></script>
</head>
<body>
	<h1>带宽查询</h1>
    <form id="queryForm" name="queryForm">
     <div id="business" style="color:black;font-size:12px">
        <span>业 务 线：</span>
        <input class="easyui-combobox" id="businessInput"
            data-options="url:'/getBusiness',valueField:'id',textField:'text',panelHeight:'auto'"/>
         <span>二级业务线：</span>
         <input class="easyui-combobox" id="subBusinessInput"
            data-options="url:'/getSubBusiness',valueField:'id',textField:'text',panelHeight:'400'"/>
    </div>
    <div id="time" style="padding-top:10px;color:black;font-size:12px">
        <span>日期范围：</span>
        <input id="startTime" type="text" name="startTime" class="easyui-datetimebox"/>
        <span class="txt mr15">-</span>
        <input id="endTime" type="text" name="endTime" class="easyui-datetimebox" />
        <a href="#" id="query" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>
        <a href="#" id="undo" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">重置</a>
    </div>
    </form>
    <div id="bwDiv" style="height:600px"></div>
</body>
<script type="text/javascript">
    $(window).load(function() {
        $('#undo').bind('click', function(){  
            $("input").val("");
        });

        $('#query').bind('click', function(){ 
            getBwInfo();
        });

        $('#businessInput').combobox({
            onSelect:function(obj){ 
                $('#subBusinessInput').combobox('reload',"/getSubBusWithPlatId?platid="+obj.id);
            }
        });

        $('#businessInput').combobox('setValue','-1');
        $('#subBusinessInput').combobox('setValue','-1');
        setDefaultStartTime();
        setDefaultEndTime();
        getBwInfo();
    });

     function setDefaultStartTime(){
        $.ajax({
            url : "${ctx}/getTopPtime?flag=start",
            type : "get",
            success : function(data) {
               $('#startTime').datetimebox('setValue',data)
            }
        });
    }

    function setDefaultEndTime(){
        $.ajax({
            url : "${ctx}/getTopPtime?flag=end",
            type : "get",
            success : function(data) {
               $('#endTime').datetimebox('setValue',data)
            }
        });
    }

    function getBwInfo() {
        var platid = $('#businessInput').combobox('getValue');
        var splatid = $('#subBusinessInput').combobox('getValue');
        var startTime = $('#startTime').datebox('getValue');
        var endTime = $('#endTime').datebox('getValue');
        var params = "?platId=" + platid + "&splatId="
                   + splatid + "&startTime=" + startTime + "&endTime=" + endTime;
        $.ajax({
            url : "${ctx}/getBwInfo" + params,
            type : "get",
            success : function(data) {
                makeChart(data);
            },
            error : function(data) {
            }
        });
    }

    function makeChart(data) {
        // 基于准备好的dom，初始化echarts图表
        var difchart = echarts.init(document.getElementById('bwDiv'));
        // 为echarts对象加载数据
        difchart.setOption(initOption("业务线带宽", data.x.split(","),data.y.split(","),'带宽使用量Mb/s'));
    }

    //根据标题，x轴 y轴数据初始化echarts option
    function initOption(title, xdata, seriesdata,yname) {
        var option = {
            title : {
                text : title,
                x : 'center'
            },
            tooltip : {
                trigger : 'axis',
                showDelay : 0,
                axisPointer : {
                    type : 'line',
                    lineStyle : {
                        color : '#324234',
                        width : 2,
                        type : 'solid'
                    },
                    crossStyle : {
                        color : '#1e90ff',
                        width : 1,
                        type : 'dashed'
                    },
                    shadowStyle : {
                        size : 'auto',
                        color : 'rgba(150,150,150,0.3)'
                    }
                }
            },
            dataZoom : {
                show : true,
                realtime: true,
                start : 0,
                end : 100,
                height : 35,
                y : 565,
            },
            xAxis : [ {
                type : 'category',
                boundaryGap : false,
                data : function() {
                    var list = xdata;
                    return list;
                }()
            } ],
            yAxis : [ {
                name : yname,
                type : 'value',
                scale : true,
                splitNumber : 3,
            } ],
            animation : false,
            series : [ {
                name : '带宽',
                type : 'line',
                smooth : false,
                itemStyle : {
                    normal : {
                        color : '#BF4A40',
                        lineStyle : { // 系列级个性化折线样式
                            width : 2,
                            type : 'solid'
                        }
                    }
                },
                data : function() {
                    var list = seriesdata;
                    return list;
                }()
            }
            ]
        };
        return option;
    }
</script>
</html>