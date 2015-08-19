<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" ></c:set>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort())
            + path + "/";
    int wrapThreshold = 100;
%>
<c:set var="basePath" value="<%=basePath%>"/>
<base href="${basePath}"/>
<!-- jquery -->
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/jquery/jquery.form.js"></script>
<!-- date component -->
<script type="text/javascript"	src="<%=basePath%>scripts/My97DatePicker/WdatePicker.js"></script>
<!-- easyui -->
<script type="text/javascript" src="<%=basePath%>scripts/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>scripts/easyui/locale/easyui-lang-zh_CN.js"></script>   
<script type="text/javascript" src="<%=basePath%>scripts/layer/layer.min.js"></script>  
<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/easyui/themes/default/easyui.css">  
<link rel="stylesheet" type="text/css" href="<%=basePath%>scripts/easyui/themes/icon.css">  
<!-- common -->
<script type="text/javascript" src="<%=basePath%>scripts/common/base.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/subform.css">  