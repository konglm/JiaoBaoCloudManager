<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<c:set value="${pageContext.request.contextPath}" var="path"
	scope="page" />
<script type="text/javascript">
	window.UEDITOR_HOME_URL = '${path}/UEditor/';
</script>
<script src="${path}/UEditor/ueditor.config.js" type="text/javascript"></script>
<script src="${path}/UEditor/ueditor.all.js" type="text/javascript"></script>

<script>
 	function selChannel(channelId,channelname){
 		$("#menu1").text(channelname).append('<span class="caret"></span>');
 		$("#channelId").text(channelId);
 	}

	function postService2() {
		if(confirm("发布后将不可修改，是否确认发布（建议预览后再发布）？")){
			var ue = UE.getEditor('editor');
			var channelId = $('#channelId').text();
			var title = $('#inputtitle').val();
			var content = encodeURIComponent(ue.getContent());
			if(channelId == 0){
				alert("请选择话题！");
			} else if (title == ''){
				alert("标题不可为空！");
			} else if (ue.getContent() == ''){
				alert("内容不可为空！");
			} else if (getWordCnts(title) > 30){
				alert("输入标题过长，发布失败！");
			} else if (getWordCnts(ue.getContent()) > 2000){
				alert("输入内容过长，发布失败！");
			} else {
				$.ajax({
					type : "get",
					url : "chkContent.do",
					async : false,
					dataType : "json",
					data : "content=" + content,
					success : function(data) {
						if (data.msg == 'havetwo') {
							alert("不可同时上传图片和视频！");
						} else if (data.msg == 'bignine') {
							alert("图片最多上传9张！");
						} else if (data.msg == 'bigone') {
							alert("视频最多上传1条！");
						} else {
							$.ajax({
								type : "get",
								url : "postQiuZhi.do",
								async : false,
								data : "channelId=" + channelId + "&title=" + title + "&content=" + content,
								success : function(data) {
									$("#editorContent").html(data);
								},
								error : function(data) {
									alert("发布失败！");
								},
								complete:function(XMLHttpRequest,textStatus){
				                     var result=XMLHttpRequest.responseText;
				                     if(result == 'timeout'){
				                    	 alert("超时请重新登录！");
				                         window.location='login.do';
				                     } else if (textStatus == 'success'){
				                     	alert("发布成功！");
				                     }
				                }
							});
						}
					}
				});
			}
		}
	}
	
	//计算正文字数
	function getWordCnts(fData) {
		var intLength=0 
	    for (var i=0;i<fData.length;i++) 
	    { 
	        if ((fData.charCodeAt(i) < 0) || (fData.charCodeAt(i) > 255)) 
	            intLength=intLength+1 
	        else 
	            intLength=intLength+1    
	    } 
	    return intLength
	}
</script>
</head>
<body>

	<div class="clearfix"></div>
	<hr style="background: #00a5e0; height: 2px; width: 6%; float: left;">
	<hr style="background: #e4e4e4; height: 2px; width: 94%; float: left;">
	<div class="clearfix"></div>


	<div class="form-group qiuzhi">
		<div class="dropdown dropdown-bg">
			<label
				style="margin-right: 30px; font-size: 18px; color: #323232; font-weight: normal; font-family: '微软雅黑';">话题选择</label>
			<h3 id="channelId" style="visibility: hidden; display: none;">0</h3>
			<button class="btn btn-default dropdown-toggle" type="button"
				id="menu1" data-toggle="dropdown"
				style="padding-right: 30px; padding-left: 30px; color: #323232; font-size: 16px;">
				请选择 <span class="caret"></span>
			</button>
			<ul class="dropdown-menu allmenu" role="menu" aria-labelledby="menu1">
				<li role="presentation" class="text-center"><a role="menuitem"
					tabindex="00" href="#" onclick="selChannel('0','请选择')">请选择</a></li>

				<c:forEach var="channel" items="${channels}">
					<li role="presentation" class="divider"></li>
					<li role="presentation" class="text-center"><a role="menuitem"
						tabindex="${channel.tabid}" href="#" onclick="selChannel('${channel.tabid}','${channel.channelname}')">${channel.channelname}</a></li>
				</c:forEach>
			</ul>
		</div>

	</div>
	<div class="form-group qusetion qiuzhi">
		<input type="text" class="form-control" id="inputtitle"
			placeholder="请输入问题的标题" style="height: 50px;">
	</div>
	<!--		  面板-->
	<div class="panel panel-default panel-bg">
		<script type="text/plain" id="editor" name="editContent"
			style="width:100%; height:500px;"></script>
		<script type="text/javascript">
			var ue = UE.getEditor('editor', {
				toolbars : [ [ "bold", "italic", "underline",
						"insertorderedlist", "insertunorderedlist",
						"simpleupload", "insertvideo", "|", "preview" ] ]
			});
		</script>
	</div>
	<button class="btn btn-default pull-right btn-bg"
		onclick="postService2()">发布</button>
	<div class="clearfix">提示：请上传".png", ".jpg", ".jpeg", ".gif"格式的图片，单张图片大小不得超过2M，不得多于9张。
	<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp请上传".mp4", ".mp3"格式的音视频文件，视频大小不得超过10M，只可上传1条视频。
	<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp音视频与图片不可同时上传。</div>
</body>
</html>