/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：EditorController
 * 文件功能描述：文章编辑Cotroller层
 *
 * 
 * 创建标识：konglm20170511
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.goldeneyes.object.AskAnswerChannel;
import com.goldeneyes.service.EditorService;
import com.goldeneyes.util.CommonUtil;

/**
 * @author konglm
 *
 */
@Controller
@SessionAttributes({"loginOK","token","utid","uimg","unick","utxt","uarea","cityName","upower"})
@RequestMapping("/")
public class EditorController {
	@Resource
	EditorService editorService;

	@RequestMapping("/editor.do")
	public String editor(HttpServletRequest request, HttpSession session, Model model) {
		String token = session.getAttribute("token").toString();
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String cityName = session.getAttribute("cityName").toString();
		String upower = session.getAttribute("upower").toString();
		//String upower = "111";
		model.addAttribute("token", token);
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("upower", upower);
		String canZhanXian = upower.substring(1, 2);
		String canQiuZhi = upower.substring(2,3);
		model.addAttribute("canZhanXian", canZhanXian);
		model.addAttribute("canQiuZhi", canQiuZhi);
		if(canZhanXian.equals("0") && canQiuZhi.equals("1")){
			List<AskAnswerChannel> channels = new ArrayList<AskAnswerChannel>();
			try {
				channels = editorService.getEditorInitInfo(token);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			model.addAttribute("channels", channels);
		}
		return "editor";
	}

	@RequestMapping("/qiuZhiEditor.do")
	public String qiuZhiEditor(HttpServletRequest request, HttpSession session, Model model) {
		//重新写session
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String uarea = session.getAttribute("uarea").toString();
		String cityName = session.getAttribute("cityName").toString();
		String loginOK = session.getAttribute("loginOK").toString();
		String token = session.getAttribute("token").toString();
		String utid = session.getAttribute("utid").toString();
		String upower = session.getAttribute("upower").toString();
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("uarea", uarea);
		model.addAttribute("loginOK", loginOK);
		model.addAttribute("token", token);
		model.addAttribute("utid", utid);
		model.addAttribute("upower", upower);
		List<AskAnswerChannel> channels = new ArrayList<AskAnswerChannel>();
		try {
			channels = editorService.getEditorInitInfo(token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("channels", channels);
		return "qiuzhieditor";
	}

	@RequestMapping("/zhanXianEditor.do")
	public String zhanXianEditor(HttpServletRequest request, HttpSession session, Model model) {
		//重新写session
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String uarea = session.getAttribute("uarea").toString();
		String cityName = session.getAttribute("cityName").toString();
		String loginOK = session.getAttribute("loginOK").toString();
		String token = session.getAttribute("token").toString();
		String utid = session.getAttribute("utid").toString();
		String upower = session.getAttribute("upower").toString();
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("uarea", uarea);
		model.addAttribute("loginOK", loginOK);
		model.addAttribute("token", token);
		model.addAttribute("utid", utid);
		model.addAttribute("upower", upower);
		return "zhanxianeditor";
	}

	@RequestMapping("/postZhanXian.do")
	public String postZhanXian(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		//重新写session
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String uarea = session.getAttribute("uarea").toString();
		String cityName = session.getAttribute("cityName").toString();
		String loginOK = session.getAttribute("loginOK").toString();
		String token = session.getAttribute("token").toString();
		String utid = session.getAttribute("utid").toString();
		String upower = session.getAttribute("upower").toString();
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("uarea", uarea);
		model.addAttribute("loginOK", loginOK);
		model.addAttribute("token", token);
		model.addAttribute("utid", utid);
		model.addAttribute("upower", upower);
		//发布展现
		String userIdStr = session.getAttribute("utid").toString();
		int userId = 0;
		if(!userIdStr.equals("")){
			userId = Integer.valueOf(userIdStr);
		} else {
			throw new Exception();
		}
		String pubArea = session.getAttribute("uarea").toString();
		String content = request.getParameter("content");
		List<String> imgs = CommonUtil.getPicFromHtml(content);
		List<String> videos = CommonUtil.getVideoFromHtml(content);
		if((imgs.size() != 0) && (videos.size() != 0)){
			return "havetwo";
		} else if (imgs.size() > 9){
			return "bignine";
		} else if (videos.size() > 1){
			return "bigone";
		}
		int channelId = Integer.valueOf(request.getParameter("channelId"));
		editorService.addUserSpace(token, userId, pubArea, content, channelId);
		return "zhanxianeditor";
	}

	@RequestMapping("/postQiuZhi.do")
	public String postQiuZhi(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		//重新写session
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String uarea = session.getAttribute("uarea").toString();
		String cityName = session.getAttribute("cityName").toString();
		String loginOK = session.getAttribute("loginOK").toString();
		String token = session.getAttribute("token").toString();
		String utid = session.getAttribute("utid").toString();
		String upower = session.getAttribute("upower").toString();
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("uarea", uarea);
		model.addAttribute("loginOK", loginOK);
		model.addAttribute("token", token);
		model.addAttribute("utid", utid);
		model.addAttribute("upower", upower);
		// 发布求知提问
		String userIdStr = session.getAttribute("utid").toString();
		int userId = 0;
		if(!userIdStr.equals("")){
			userId = Integer.valueOf(userIdStr);
		} else {
			throw new Exception();
		}
		int channelId = Integer.valueOf(request.getParameter("channelId"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		editorService.addAsk(token, userId, channelId, title, content);
		// 重新获取数据，显示清空后的求知编辑页面
		List<AskAnswerChannel> channels = new ArrayList<AskAnswerChannel>();
		channels = editorService.getEditorInitInfo(token);
		model.addAttribute("channels", channels);
		return "qiuzhieditor";
	}
	
	@RequestMapping("/chkContent.do")
	public @ResponseBody Map<String,Object> chkContent(HttpServletRequest request, HttpSession session, Model model) throws Exception {
		//重新写session
		String uimg = session.getAttribute("uimg").toString();
		String unick = session.getAttribute("unick").toString();
		String utxt = session.getAttribute("utxt").toString();
		String uarea = session.getAttribute("uarea").toString();
		String cityName = session.getAttribute("cityName").toString();
		String loginOK = session.getAttribute("loginOK").toString();
		String token = session.getAttribute("token").toString();
		String utid = session.getAttribute("utid").toString();
		String upower = session.getAttribute("upower").toString();
		model.addAttribute("uimg", uimg);
		model.addAttribute("unick", unick);
		model.addAttribute("utxt", utxt);
		model.addAttribute("cityName", cityName);
		model.addAttribute("uarea", uarea);
		model.addAttribute("loginOK", loginOK);
		model.addAttribute("token", token);
		model.addAttribute("utid", utid);
		model.addAttribute("upower", upower);
		//检查展现
		Map<String,Object> map = new HashMap<String,Object>(); 
		String content = request.getParameter("content");
		List<String> imgs = CommonUtil.getPicFromHtml(content);
		List<String> videos = CommonUtil.getVideoFromHtml(content);
		if((imgs.size() != 0) && (videos.size() != 0)){
			map.put("msg","havetwo");
		} else if (imgs.size() > 9){
			map.put("msg","bignine");
		} else if (videos.size() > 1){
			map.put("msg","bigone");
		} else {
			map.put("msg", "ok");
		}
		return map; 
	}

}
