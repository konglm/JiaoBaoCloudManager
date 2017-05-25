/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：
 * 文件功能描述：
 *
 * 
 * 创建标识：
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyStore.Entry;
import java.security.interfaces.RSAPublicKey;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goldeneyes.service.LoginService;

import com.goldeneyes.util.EncryptUtil;
import com.goldeneyes.util.PhoneFormatCheckUtils;

import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
@Controller
@SessionAttributes({ "loginOK", "token", "utid", "uimg", "unick", "utxt", "uarea", "cityName" })
@RequestMapping("/")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model, HttpServletResponse response) {

		return "login";
	}

	/**
	 * @author xubs
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/loginOK")
	public String loginOK(HttpServletRequest request, HttpSession session, HttpServletResponse response, Model model)
			throws IOException {

		System.out.println("前端的ajax请求过来了=====");

		/**
		 * 先调握手接口，获取指数及模生成公钥以用于用户名和密码的RSA加密
		 */
		/**
		 * 传给握手接口的参数
		 */
		// 握手类型：登录
		String shaketype = "login";
		// 应用ID
		String appid = "JAVAPC";
		/**
		 * 握手接口程序返回过来模和指数
		 */
		String shakeHandRspData = "";
		try {
			shakeHandRspData = loginService.PostShakeHand(shaketype, appid);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("shakeHandRspData=====" + shakeHandRspData);
		/**
		 * 获取指数和模的值
		 */
		JSONObject responseData = JSONObject.fromObject(shakeHandRspData);
		System.out.println("responseData----------" + responseData);
		String rspData = responseData.getString("RspData");

		System.out.println("rspData=======" + rspData);
		/**
		 * 再将rspData转换成JSONObject
		 */
		JSONObject rspDataObj = JSONObject.fromObject(rspData);
		String modulus = rspDataObj.getString("Modulus");
		String exponent = rspDataObj.getString("Exponent");
		System.out.println("exponent=======" + exponent + "\n modulus====" + modulus);
		/**
		 * 获取前端传过来的用户名和密码
		 */
		String uid = request.getParameter("username");
		String pw = request.getParameter("password");
		System.out.println("-----userLogin-----");
		System.out.println("username:" + uid + "--password:" + pw);
		/**
		 * 根据用户账号判断登录类型 mb(手机号),nm(账号或邮箱) 调用判断手机号工具类及方法,如果返回true则为大陆的手机号
		 */
		String vtp = "";
		Boolean flag = PhoneFormatCheckUtils.isChinaPhoneLegal(uid);
		if (flag) {
			vtp = "mb";
		} else {
			vtp = "nm";
		}
		/**
		 * 组装postLogin（）方法所需的各个参数值
		 * 
		 */
		/**
		 * RSA加密用户名和密码 先生成公钥
		 */

		RSAPublicKey rsaPublicKey = EncryptUtil.getPublicKey(modulus, exponent);
		System.out.println("rsaPublicKey========" + rsaPublicKey);

		/**
		 * 用户名和密码需要RSA加密后再传给接口程序 加密用户名和密码
		 */
		String encryptUID = "";
		String encryptPW = "";

		try {

			encryptUID = EncryptUtil.encryptByPublicKey(uid, rsaPublicKey);
			encryptPW = EncryptUtil.encryptByPublicKey(pw, rsaPublicKey);

			System.out.println("encryptUID===" + encryptUID + "\nencryptPW====" + encryptPW);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * loginService多态指向其实现类创建的对象调用该接口的方法标准postLogin（）
		 */
		String postLoginRspData = "";
		try {
			postLoginRspData = loginService.postLogin(shaketype, appid, uid, encryptUID, pw, encryptPW, vtp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 根据responseData中的responseCode来判断是否登录成功
		 */
		JSONObject obj = JSONObject.fromObject(postLoginRspData);
		/**
		 * get responseCode 得到返回的状态码
		 */
		String responseCode = obj.getString("RspCode");
		String returnURL = "";
		String rspData2 = obj.getString("RspData");
		JSONObject obj2 = JSONObject.fromObject(rspData2);
		switch (responseCode) {
		case "0000":// 调用正常，能返回用户数据
			/**
			 * 取用户权限串
			 */
			String upower = obj2.getString("upower");
			/**
			 * 如果upower为空或首位为0则登不进去
			 */
			/**
			 * 截取权限串的首位
			 */
			String firstChar = upower.substring(0, 1);
			/**
			 * 看是否是0,
			 */
			Boolean flag2 = firstChar.equals("0");

			if (upower != null &&upower!=""&& flag2 == false) {
				/**
				 * 将用户的令牌保存进session中
				 */

				String token = obj2.getString("token");
				System.out.println("token=====" + token);

				HttpSession httpSession = request.getSession();
				/**
				 * 用户的信息保存进session中
				 */
				httpSession.setAttribute("token", token);

				String uimg = obj2.getString("uimg");
				httpSession.setAttribute("uimg", uimg);
				String unick = obj2.getString("unick");
				httpSession.setAttribute("unick", unick);
				String utxt = obj2.getString("utxt");
				httpSession.setAttribute("utxt", utxt);
				String uarea = obj2.getString("uarea");
				httpSession.setAttribute("uarea", uarea);
				String utid = obj2.getString("utid");
				httpSession.setAttribute("utid", utid);
				System.out.println("uarea============" + uarea);
				/**
				 * 将权限串补到200位,后面补0
				 */
				int n = 200;

				System.out.println(upower + String.format("%1$0" + (n - upower.length()) + "d", 0));
				/**
				 * 补位后的upower权限串
				 */
				String newUpower = upower + String.format("%1$0" + (n - upower.length()) + "d", 0);
				httpSession.setAttribute("upower", newUpower);
				/**
				 * 拿到“省”的索引
				 */
				System.out.println("省的index====" + uarea.indexOf("省"));
				/**
				 * 拿到“市”的下标（index，索引）
				 */
				System.out.println("市的index====" + uarea.indexOf("市"));

				/**
				 * 如果地域不为空则 截取uarea字符串以获得城市名
				 */
				String cityName = "";
				if (!uarea.equals("")) {

					cityName = uarea.substring(uarea.indexOf("省") + 1 + 1, uarea.indexOf("市"));
					System.out.println("cityName========" + cityName);

				} else {
					/**
					 * 没有城市名
					 */
					cityName = "";

				}
				httpSession.setAttribute("cityName", cityName);
				model.addAttribute("loginOK", "loginOK");
				returnURL = "redirect:/editor.do";

			}else{
				/**
				 * 进不去，回到登录页面
				 */
				PrintWriter wirter = response.getWriter();
				wirter.write("noAdmin");
				wirter.flush();

				returnURL =null;
			}

			break;
		case "0005":
			/**
			 * 用户名或密码不正确及用户有关方面
			 */

			PrintWriter wirter = response.getWriter();
			wirter.write("error");
			wirter.flush();
			// returnURL = "redirect:/login.do";

			returnURL = null;
			break;

		default:
			break;
		}
		return returnURL;

	}

}
