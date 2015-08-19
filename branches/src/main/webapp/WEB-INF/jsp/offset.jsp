<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Storm消费偏移监控</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/echarts/echarts-plain.js"></script>

</head>
<body>
	<h1>Storm消费偏移监控</h1>


	<div id="Voddif" style="height: 400px; width: 49%; float: left;"></div>
	<div id="Vodrate" style="height: 400px; width: 49%; float: left;"></div>

	<div id="TopUrldif" style="height: 400px; width: 49%; float: left;"></div>
	<div id="TopUrlrate" style="height: 400px; width: 49%; float: left;"></div>

	<div id="Clientdif" style="height: 400px; width: 49%; float: left;"></div>
	<div id="Clientrate" style="height: 400px; width: 49%; float: left;"></div>

	<div id="P2pdif" style="height: 400px; width: 49%; float: left;"></div>
    <div id="P2prate" style="height: 400px; width: 49%; float: left;"></div>

    <div id="Rtmpdif" style="height: 400px; width: 49%; float: left;"></div>
    <div id="Rtmprate" style="height: 400px; width: 49%; float: left;"></div>

    <div id="Flvdif" style="height: 400px; width: 49%; float: left;"></div>
    <div id="Flvrate" style="height: 400px; width: 49%; float: left;"></div>

    <div id="Hlsdif" style="height: 400px; width: 49%; float: left;"></div>
    <div id="Hlsrate" style="height: 400px; width: 49%; float: left;"></div>
</body>

<script>
    //循环 ajax获取jvm信息
	function getVodOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getvodoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"Vod");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}
    
	function getTopOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/gettopoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"TopUrl");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}
	
	function getClientOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getclientoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"Client");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

	function getP2pOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getp2poffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"P2p");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

	function getRtmpOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getrtmpoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"Rtmp");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

	function getFlvOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getflvoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"Flv");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

	function getHlsOffSetInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/gethlsoffsetinfo",
			type : "get",
			success : function(data) {
				makeChart(data,"Hls");
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

    //自动加载jvm信息 定时任务 1分钟
	$(document).ready(function() {
		getVodOffSetInfo();
		timename = setInterval("getVodOffSetInfo();", 1000 * 60);
		
		getTopOffSetInfo();
		timename = setInterval("getTopOffSetInfo();", 1000 * 70);
		
		getClientOffSetInfo();
		timename = setInterval("getClientOffSetInfo();", 1000 * 80);

		getP2pOffSetInfo();
		timename = setInterval("getP2pOffSetInfo();", 1000 * 90);

		getRtmpOffSetInfo();
        timename = setInterval("getRtmpOffSetInfo();", 1000 * 90);

        getFlvOffSetInfo();
        timename = setInterval("getFlvOffSetInfo();", 1000 * 90);

        getHlsOffSetInfo();
        timename = setInterval("getHlsOffSetInfo();", 1000 * 90);
	});
</script>

<script type="text/javascript">
	function makeChart(data,type) {
		// 基于准备好的dom，初始化echarts图表
		var difchart = echarts.init(document.getElementById(type + 'dif'));
		var ratechart = echarts.init(document.getElementById(type + 'rate'));
		// 为echarts对象加载数据 
		difchart.setOption(initOption(type + " Kafka And Storm Offset Diff", data.offsettime.split(","),data.offsetdif.split(","),'偏移量'));
		ratechart.setOption(initOption(type + " Storm Consume Rate", data.ratetime.split(","),data.ratelist.split(","),'速度(offset/s)'));
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
				name : 'Memory',
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