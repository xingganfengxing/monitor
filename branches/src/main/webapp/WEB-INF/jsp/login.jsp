<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>乐视云客户服务系统</title>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/jquery-1.8.0.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
body {
    background-color: #0491DB;
}

.loginHeader {
    background-color: #FFFFFF;
    text-align: center;
    width: 100%;
    height: 60px;
}
.loginHeader div {
    height: 60px;
    min-width: 900px;
    max-width: 1300px;
    margin: auto;
    background-image: url("../../../resources/images/logo.png");
    background-position: 0px 5px;
    background-repeat: no-repeat;
}
.loginHeader div span {
    position: relative;
    color: #005D91;
    top: 20px;
    font-size: 25px;
    margin-left: 200px;
}

.loginSection > div {
    height: 566px;
    width: 1082px;
    margin: 0 auto;
    background-image: url("../../../resources/images/login.png");
    background-repeat: no-repeat;
}
.loginMain {
    line-height: 40px;
    position: relative;
    top: 190px;
    left: 530px;
    font-size: 20px;
    font-weight: bolder;
    color: #005D91;
    width: 400px;
}
#IbtnEnter {
    color: #FFFFFF;
    font-family: 微软雅黑;
    font-size: 30px;
    font-weight: bolder;
    background-color: rgba(0, 0, 0, 0);
    margin-left: 56px;
    margin-top: 3px;
    width: 175px;
    height: 55px;
    border: none;
}

.loginInput {
    background-color: #005D91;
    color: #FFFFFF;
    border: none;
    line-height: 30px;
    width: 280px;
}
#username {
    font-size: 25px;
}
#password {
    font-size: 16px;
}

#tipMsg {
    font-size: 16px;
    font-weight: bolder;
    color: #FF0000;
}
</style>

<script type="text/javascript">
 	function doLogin() {
 		$.ajax({
 			type : "post",
 			url  : "${pageContext.servletContext.contextPath}/login/check",
 			data : {
 				"username" : $("#username").val(),
 				"password" : $("#password").val(),
 			},
 			beforeSend : function(){
 				$("#tipMsg").text("用户登录...");
 			},
 			success : function( msg ){
 				if (msg.result == 0) {
                    if (msg.errorMessage) {
                    	$("#tipMsg").text("");
                    	alert("用户名或密码错误")
                        return ;
                    } else {
                    	$("#tipMsg").text("");
                    	alert(msg.errorMessage)
                        return ;
                    }
                } else {
                	window.location.href = "${pageContext.servletContext.contextPath}/index";
                }
 			},
 			error: function () {
 				$("#tipMsg").text("");
 				alert("系统异常");
            }
 		});
 	}
 	$(function() {
 		$("#IbtnEnter").click(doLogin);
 		$(window).keydown(function(event) {
 			if( event.keyCode == 13 ){
 				doLogin();
 			}
 	    });
 		$("#username").bind("input propertychange", function(){
 			$("#password").val("");
 		});
 	});
</script>
</head>
<body>
    <div class="loginHeader"><div><span>乐视云客户服务系统</span></div></div>
    <div class="loginSection">
        <div>
            <div class="loginMain">
            	<span>用户名:</span><br/>
            	<input type="text"  class="loginInput" id="username" name="username" value="${username}"/><br/>
            	<span>密&nbsp;&nbsp;码:</span><br/>
            	<input type="password"  class="loginInput" id="password" name="password" value="${password}" /><br/>
                <input type="checkbox" id="autologin" checked="${autologin}" name="autologin"><span>记住我&nbsp;&nbsp;</span><span id="tipMsg"></span><br/>
            	<input id="IbtnEnter" type="button" value="登&nbsp;录" /><br/>
            </div>
        </div>
    </div>
</body>
</html>