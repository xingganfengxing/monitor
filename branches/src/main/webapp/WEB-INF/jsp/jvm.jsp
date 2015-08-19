<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jvm监控</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp"%>
<script type="text/javascript" src="${ctx}/scripts/echarts/echarts-plain.js"></script>

</head>
<body>
	<h1>JVM监控——进程：${proc} <a href="${ctx}/jvmmenu" >返回</a></h1>

	<c:forEach var="ip" items="${ips}">
		<div id="jvm${ip }"
			style="width: 30%; border: 0.2em #94DBD8 solid; float: left; margin-right: 0.5%; margin-bottom: 2%; margin-left: 0.5%;padding: 0.5%;font-family: 微软雅黑">

			<div style="font-size: 0.9em; font-weight: bold;">
				<b style="font-size: 1.2em;">机器IP：${ip }</b><br>
				启动时间：<span id="uptime${ip }" ></span> <br> 加载类数量：<span id="loadclass${ip }" ></span> 
				系统负载：<span id="systemload${ip }" ></span> <br> 新生代垃圾回收次数：<span id="youngcount${ip }" ></span>
				时间：<span id="youngtime${ip }" ></span> <br> 老年代垃圾回收次数：<span id="oldcount${ip }" ></span> 时间：<span id="oldtime${ip }" ></span> <br>

				线程：<span id="thread${ip }" ></span> 守护线程：<span id="daemonthread${ip }" ></span> <br>
				线程平均block次数：<span id="avgcount${ip }" ></span> 时间：<span id="avgtime${ip }" ></span> <br>
				死锁线程：<span id="deadlock${ip }" ></span>
			</div>

			<div id="heap${ip }" style="height: 200px;"></div>
			<div id="oldgen${ip }" style="height: 200px;"></div>
		</div>

	</c:forEach>

</body>

<script>
    //循环 ajax获取jvm信息
	function getJvmInfo() {
		<c:forEach var="ip" items="${ips} ">
		$.ajax({
			url : "${ctx}/getjvm",
			type : "get",
			data : {
				ip : "${ip}",
				procName : "${proc}"
			},
			success : function(data) {
				tmp = "${ip}";
				makeChart(tmp.replace(/[^0-9.""]/ig, ""), data);
			},
			error : function(data) {
			}
		});
		</c:forEach>
	}

    //自动加载jvm信息 定时任务 1分钟
	$(document).ready(function() {
		getJvmInfo();
		timename = setInterval("getJvmInfo();", 1000 * 30);
	});
</script>

<script type="text/javascript">
	function makeChart(ip, data) {
		// 基于准备好的dom，初始化echarts图表
		var heapchart = echarts.init(document.getElementById('heap' + ip));
		var oldgenchart = echarts.init(document.getElementById('oldgen' + ip));
		// 为echarts对象加载数据 
		heapchart.setOption(initOption("HeapUsed", data.x.split(","),data.heapValueList.split(","),data.heapmax));
		oldgenchart.setOption(initOption("OldGenUsed", data.x.split(","),data.oldgenValueList.split(","),data.oldgenmax));
		changeValue(ip,data);
	}
	
	//给数值型数据赋值
	function changeValue(ip,data){
		document.getElementById("uptime" +ip).innerHTML=data.uptime || 0;
		document.getElementById("loadclass" +ip).innerHTML=data.loadClass || 0;
		document.getElementById("systemload" +ip).innerHTML=data.systemLoad || 0;
		document.getElementById("youngcount" +ip).innerHTML=data.youngCount || 0;
		document.getElementById("youngtime" +ip).innerHTML=data.youngTime || 0;
		document.getElementById("oldcount" +ip).innerHTML=data.oldTime || 0;
		document.getElementById("oldtime" +ip).innerHTML=data.systemLoad || 0;
		document.getElementById("thread" +ip).innerHTML=data.thread || 0;
		document.getElementById("daemonthread" +ip).innerHTML=data.daemonThreads || 0;
		document.getElementById("avgcount" +ip).innerHTML=data.avgThreadBCount || 0;
		document.getElementById("avgtime" +ip).innerHTML=data.avgThreadBTime || 0;
		document.getElementById("deadlock" +ip).innerHTML=data.deadlockedThreads || 0;
	}

	//根据标题，x轴 y轴数据初始化echarts option
	function initOption(title, xdata, seriesdata,ymax) {
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
				name : 'Memory(KB)',
				type : 'value',
				scale : true,
			    max: ymax,
			    min: 0,
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