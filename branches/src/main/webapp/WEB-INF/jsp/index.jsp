<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/fcf/FusionCharts.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<div class="section">
<jsp:include page="/layout/aside?func=index" />
    <div class="main">
    </div>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
</body>
</html>