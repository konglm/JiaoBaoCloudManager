<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">

<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<link href="${path}/css/bootstrap.min.css" rel="stylesheet">
<link href="${path}/css/bootstrap.css" rel="stylesheet">
<link href="${path}/css/bootstrap-theme.min.css" rel="stylesheet">
<link href="${path}/css/bootstrap-theme.css" rel="stylesheet">
<link href="${path}/css/build.css" rel="stylesheet">
<link href="${path}/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="${path}/js/jquery-1.11.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>
<style>
* {
	font-family: "微软雅黑";
}

body {
	background: #F2F2F2;
}

.container-bg {
	width: 500px;
	margin: 0 auto;
	border: 1px solid #e4e4e4;
	padding: 50px;
	margin-top: 300px;
	border-radius: 4px;
	background: white;
}

.container-bg h2 {
	font-size: 26px;
	color: #323232;
	margin-bottom: 30px;
	margin-top: 0px;
}

.container-bg .input-groupbg {
	margin-bottom: 15px;
}

.container-bg .iconR {
	background: none;
	border-top: 1px solid #E4E4E4;
	border-left: 1px solid #E4E4E4;
	border-bottom: 1px solid #E4E4E4;
}

.container-bg input {
	height: 50px;
	border: 1px solid #E4E4E4;
	box-shadow: none;
	font-size: 18px;
	color: #323232;
}

input::-webkit-input-placeholder {
	font-size: 18px;
	color: #808080;
	line-height: 20px;
}

.login {
	border: none;
	box-shadow: none;
	background: #1DB8F1;
	font-size: 22px;
	letter-spacing: 5px;
}
</style>

<title>教宝云管理后台</title>

</head>
<body>

	<div class="container container-bg">
		<form class="form-signin" id="myForm">
			<h2 class="form-signin-heading text-center">请输入教宝云账号和密码</h2>
			<div class="input-group input-groupbg">
				<div class="input-group-addon iconR">
					<img src="${path}/img/user.png" alt="我是用户名">
				</div>
				<input type="text" class="form-control" id="exampleInputAmount"
					placeholder="请输入用户名" name="username">
			</div>
			<div class="input-group input-groupbg">
				<div class="input-group-addon iconR">
					<img src="${path}/img/password.png" alt="我是密码">
				</div>
				<input type="password" class="form-control" id="inputPassword"
					placeholder="请输入密码" required name="password">
			</div>
			<button class="btn btn-lg btn-primary btn-block login" type="button"
				id="loginButton">登录</button>
		</form>
	</div>
	<script type="text/javascript">
	
		$("#loginButton").click(function (){
			var username=$("#exampleInputAmount").val();
			var password=$("#inputPassword").val();
			$.ajax({
				type:"get",
				url:"loginOK.do",
				data:"username="+username+"&password="+password,
				success:function(data){
				
				},
				complete:function(XmlHttpRequest){
					var result=XmlHttpRequest.responseText;
		
					if(result =='error'){
						var errorInfo="用户名或密码错误";
						alert(errorInfo);
						window.location="login.do" 
					}else{
						window.location="editor.do"
					}
				}
				
			});
		});
			
		 
			
	
	
	
	</script>
</body>
</html>
