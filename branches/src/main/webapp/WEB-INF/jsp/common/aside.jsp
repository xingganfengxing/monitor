<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">

</script>

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
 