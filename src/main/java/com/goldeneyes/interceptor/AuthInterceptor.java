/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：AuthInterceptor
 * 文件功能描述：验证拦截器
 *
 * 
 * 创建标识：konglm20161108
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.goldeneyes.util.EncryptUtil;

import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
@SessionAttributes({"loginOK"})
public class AuthInterceptor implements HandlerInterceptor {
	private final String ADMINSESSION = "loginOK";

	/**
	 * @author konglm
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//先判断是否已登录或登录是否超时，未登录或超时转到登录窗口
		Object sessionObj = request.getSession().getAttribute(ADMINSESSION);  
	    if(sessionObj!=null) {  
	      return true;  
	    }   
	    PrintWriter wirter =  response.getWriter();
	    wirter.write("timeout");
	    wirter.flush(); 
	    return false;  
	}

	/**
	 * @author konglm
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @author konglm
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
