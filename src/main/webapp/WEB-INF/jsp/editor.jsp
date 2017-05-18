<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>教宝云管理后台</title>
<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<script type="text/javascript">
	window.UEDITOR_HOME_URL = '${path}/UEditor/';
</script>
<script src="${path}/UEditor/ueditor.config.js" type="text/javascript"></script>
<script src="${path}/UEditor/ueditor.all.js" type="text/javascript"></script>
<link href="${path}/css/bootstrap.min.css" rel="stylesheet">
<link href="${path}/css/bootstrap.css" rel="stylesheet">
<link href="${path}/css/bootstrap-theme.min.css" rel="stylesheet">
<link href="${path}/css/bootstrap-theme.css" rel="stylesheet">
<link href="${path}/css/build.css" rel="stylesheet">
<link href="${path}/css/awesome-bootstrap-checkbox.css" rel="stylesheet">
<script src="${path}/js/jquery-1.11.2.min.js"></script>
<script src="${path}/js/bootstrap.min.js"></script>

<style>
body {
	background: #f2f2f2;;
}

.topnav {
	border: none;
	padding: 0;
}

.a {
	padding: 10px 15px;
	line-height: 30px;
}

.touxiang {
	margin-right: 5px;
}

.Inuser {
	margin-bottom: 0px;
	background: #f2f2f2;
}

.picture {
	width: 80px;
}

.user {
	font-size: 20px;
	color: #000000;
	font-family: "微软雅黑";
}

.user span {
	margin-left: 10px;
	color: #00a5e0;
	line-height: 20px;
	font-size: 18px;
}

.user span  img {
	margin-top: -4px;
	width: 24px;
	margin-right: 3px;
}

p {
	font-size: 16px;
	color: #808080;
}

.choose {
	font-size: 18px;
	color: #323232;
	line-height: 20px;
	margin-right: 30px;
}

.allmenu {
	margin-left: 100px;
}

.allmenu li a {
	font-size: 16px;
	color: #323232;
	line-height: 20px;
	letter-spacing: 10px;
}

.form-bg {
	background: #FFFFFF;
	padding: 30px 20px;
	margin-top: 0px;
}

.form-group-bg {
	line-height: 20px;
}

.btn-bg {
	background: #1db8f1;
	color: white;
	border: none;
	font-size: 20px;
	line-height: 34px;
	letter-spacing: 2px;
	padding-left: 30px;
	padding-right: 30px;
}

.textarea {
	border: none;
	box-shadow: none;
	padding: 0px;
}

hr {
	margin-top: 10px;
}

.dropdown-bg {
	margin-top: 10px;
}

.qusetion {
	margin-top: 30px;
}

.panel-bg {
	margin-top: 30px;
}

#textarea::-webkit-input-placeholder {
	font-size: 16px;
	color: #808080;
} /* 使用webkit内核的浏览器 */
#inputtitle::-webkit-input-placeholder {
	font-size: 16px;
	color: #808080;
}

#textarea {
	box-shadow: none;
}

input {
	font-size: 18px;
	color: #323232;
}
</style>

<script>
	function showQiuZhi(opt) {
		if (opt == 1) {
			if (confirm("此操作将清空编辑器，是否确认切换到展现？")) {
				var ue = UE.getEditor('editor');
				ue.setContent('');
				$("#qiuZhiRadio").attr("checked", false);
				$.ajax({
					type : "get",
					url : "zhanXianEditor.do",
					async : false,
					success : function(data) {
						$("#editorContent").html(data);
					},
					error : function(data) {
						alert("获取展现数据失败！");
					},
					complete : function(XMLHttpRequest, textStatus) {
						var result = XMLHttpRequest.responseText;
						if (result == 'timeout') {
							alert("超时请重新登录！");
							window.location = 'login.do';
						}
					}
				});
			} else {
				$("input:radio[name='qiuZhiRadio']").eq(0)
						.attr("checked", true);
				$("#zhanXianRadio").attr("checked", false);
			}
		} else {
			if (confirm("此操作将清空编辑器，是否确认切换到求知？")) {
				var ue = UE.getEditor('editor');
				ue.setContent('');
				$("#zhanXianRadio").attr("checked", false);
				$.ajax({
					type : "get",
					url : "qiuZhiEditor.do",
					async : false,
					success : function(data) {
						$("#editorContent").html(data);
					},
					error : function() {
						alert("获取求知数据失败！");
					},
					complete : function(XMLHttpRequest, textStatus) {
						var result = XMLHttpRequest.responseText;
						if (result == 'timeout') {
							alert("超时请重新登录！");
							window.location = 'login.do';
						}
					}
				});
			} else {
				$("input:radio[name='zhanXianRadio']").eq(0).attr("checked",
						true);
				$("#qiuZhiRadio").attr("checked", false);
			}
		}
	}

	function selChannel(channelId, channelname) {
		$("#menu1").text(channelname).append('<span class="caret"></span>');
		$("#channelId").text(channelId);
		var channelId = $('#channelId').text();
	}

	function postService() {
		if (confirm("发布后将不可修改，是否确认发布（建议预览后再发布）？")) {
			var ue = UE.getEditor('editor');
			var channelId = $('#channelId').text();
			$
					.ajax({
						type : "get",
						url : "postZhanXian.do",
						async : false,
						data : "channelId=" + channelId + "&content="
								+ ue.getContent(),
						success : function(data) {
							$("#editorContent").html(data);
						},
						error : function(data) {
							alert("发布失败！");
						},
						complete : function(XMLHttpRequest, textStatus) {
							var result = XMLHttpRequest.responseText;
							if (result == 'timeout') {
								alert("超时请重新登录！");
								window.location = 'login.do';
							} else if (textStatus == 'success') {
								alert("发布成功！");
							}
						}
					});
		}
	}
</script>
</head>
<body>
	<nav class="navbar navbar-inverse tapnav"
		style="border:none; box-shadow: none;">
	<div class="container">
		<div class="navbar-header" style="border: none; box-shadow: none;">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#navbar" aria-expanded="false"
				aria-controls="navbar">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand " href="#"><img src="${path}/img/logo.png"
				class="picture"></a> <a class="navbar-brand "
				style="color: #FFFFFF; font-size: 20px; padding-left: 30px; padding-right: 30px; background: #1DB8F1; margin-left: 20px;">发布</a>
		</div>
		<div id="navbar" class="navbar-collapse collapse "
			style="border: none; box-shadow: none;">
			<ul class="nav navbar-nav navbar-right n">
				<li><a href="#" style="padding: 10px 15px; line-height: 30px;"><img
						class="img-circle" src="${uimg}" accesskey="我是头像" width="24px"
						height="24px" style="margin-right: 10px; margin-top: -3px;"><span>${unick}</span></a></li>
				<li><a href="login.do"
					style="padding: 10px 15px; line-height: 30px;">退出</a></li>

			</ul>
		</div>
		<!--/.nav-collapse -->
	</div>
	</nav>
	<div class="container">

		<!-- Main component for a primary marketing message or call to action -->
		<div class="jumbotron Inuser"
			style="padding-left: 20px; padding-right: 20px; padding-top: 20px;">
			<div class="row">
				<div class="col-md-1 " style="padding-left: 0px;">
					<img class="img-circle" src="${uimg}" accesskey="我是头像" width="90px"
						height="90px">
				</div>
				<div class="col-md-11">
					<h2 class="user">
						${unick}<span><img src="${path}/img/region.png">${cityName}</span>
					</h2>
					<p style="font-size: 16px;">${utxt}</p>
				</div>
			</div>
		</div>
		<div class="form-bg" id="form1" runat="server">
			<div class="form-group form-group-bg">
				<label
					style="margin-right: 40px; font-size: 18px; color: #323232; font-weight: normal; font-family: '微软雅黑';">发布到</label>
				<div class="radio radio-info radio-inline choose"
					style="margin-top: -4px;">
					<input type="radio" id="zhanXianRadio" onchange="showQiuZhi(1)"
						value="option1" name="zhanXianRadio" checked> <label
						for="inlineRadio1 "> 展现</label>
				</div>
				<div class="radio radio-info radio-inline choose"
					style="margin-top: -4px;">
					<input type="radio" id="qiuZhiRadio" onchange="showQiuZhi(2)"
						value="option1" name="qiuZhiRadio"> <label
						for="inlineRadio1"> 求知</label>
				</div>
				<div id="editorContent">
					<div class="clearfix"></div>
					<hr
						style="background: #00a5e0; height: 2px; width: 6%; float: left;">
					<hr
						style="background: #e4e4e4; height: 2px; width: 94%; float: left;">
					<div class="clearfix"></div>

					<div class="form-group qiuzhi">
						<div class="dropdown dropdown-bg">
							<label
								style="margin-right: 30px; font-size: 18px; color: #323232; font-weight: normal; font-family: '微软雅黑';">发布范围选择：</label>
							<h3 id="channelId" style="visibility: hidden; display: none;">0</h3>
							<button class="btn btn-default dropdown-toggle" type="button"
								id="menu1" data-toggle="dropdown"
								style="padding-right: 30px; padding-left: 30px; color: #323232; font-size: 16px;">
								空间和展现<span class="caret"></span>
							</button>
							<ul class="dropdown-menu allmenu" role="menu"
								aria-labelledby="menu1">
								<li role="presentation" class="text-center"><a
									role="menuitem" tabindex="01" href="#"
									onclick="selChannel('0','展现和个人')">空间和展现</a></li>
								<li role="presentation" class="text-center"><a
									role="menuitem" tabindex="02" href="#"
									onclick="selChannel('1','仅个人动态')">仅个人空间</a></li>
							</ul>
						</div>

					</div>

					<!--		  面板-->
					<div class="panel panel-default panel-bg">
						<script type="text/plain" id="editor" name="editContent"
							style="width:100%; height:500px;"></script>
						<script type="text/javascript">
							var ue = UE.getEditor('editor', {
								toolbars : [ [ "simpleupload", "insertvideo",
										"|", "preview" ] ]
							});
						</script>
					</div>
					<button class="btn btn-default pull-right btn-bg"
						onclick="postService()">发布</button>
					<div class="clearfix"></div>
				</div>
			</div>
		</div>


	</div>
</body>
</html>