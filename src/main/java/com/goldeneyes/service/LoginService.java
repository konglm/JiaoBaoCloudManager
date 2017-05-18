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

package com.goldeneyes.service;

import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
public interface LoginService {
	/**
	 * 握手方法
	 * @author xubs
	 * 
	 * @param shaketype
	 * @param appid
	 * 
	 * @return String
	 */
	public String PostShakeHand (String shaketype,String appid)throws Exception;

	/**
	 * @author xubs postLogin()方法
	 * 
	 * @param shakeType
	 * @param uid
	 * @param pw
	 * 
	 * @return String
	 */
	public String postLogin( String shaketype,String appid, String uid,String encryptUID, String pw,String encryptPW,String vtp)throws Exception;

}
