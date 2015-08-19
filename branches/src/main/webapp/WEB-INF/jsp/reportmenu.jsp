<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报表</title>
<%@ include file="/WEB-INF/jsp/common/base.jsp" %>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/fcf/FusionCharts.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="aside">
     <ol>
         <c:forEach items="${menu}" var="item">
           <li class="${item.class}">
               <a id="${item.id}" target="_blank" href="${item.url}" >
                   <span class="name" id="aside">${item.name}</span>
               </a>
           </li>
         </c:forEach>
       </ol>
</div>
</body>
</html>