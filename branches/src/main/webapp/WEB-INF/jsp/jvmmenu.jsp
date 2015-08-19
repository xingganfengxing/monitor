<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JVM进程</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/fcf/FusionCharts.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="aside">
    <ol>
      <c:forEach items="${cluster}" var="item">
        <li class="${item.class}">
            <a id=""  href="${ctx}/jvm?procname=${item.processKey}" >
                <span class="name" id="aside">${item.processKey}</span>
            </a>
        </li>
      </c:forEach>
    </ol>
</div>
</body>
</html>