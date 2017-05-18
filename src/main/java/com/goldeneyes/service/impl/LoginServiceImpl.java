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

package com.goldeneyes.service.impl;

import java.security.interfaces.RSAPublicKey;

import org.springframework.stereotype.Service;

import com.goldeneyes.service.LoginService;
import com.goldeneyes.util.CommonUtil;
import com.goldeneyes.util.EncryptUtil;
import com.goldeneyes.util.HttpUtil;

import net.sf.json.JSONObject;

/**
 * @author konglm 登录服务实现类
 */
@Service
public class LoginServiceImpl implements LoginService {
	/**
	 * 定义常量（constant）
	 * 
	 */
	private static final String API="/api/CloudApi/";
	/**
	 * @author xubs
	 * 
	 */
	@Override
	public String postLogin( String shaketype,String appid,String uid,String encryptUID, String pw,String encryptPW,String vtp)throws Exception {
		
		
		/**
		 * 用户电脑网卡的物理地址
		 */
		String uuid= CommonUtil.getPhysicalAddress();
		/**
		 * 得到sign签名的值
		 * 将sign之外的其他参数按字母顺序排列，键=值组成对后，组成的字符串为
		 */
		/**
		 * 将sign之外的其他参数及值put进去JSONObject
		 */
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("uuid", uuid);
		jsonObj.put("shaketype", shaketype);
		jsonObj.put("appid", appid);
		jsonObj.put("uid", encryptUID);
		jsonObj.put("pw", encryptPW);
		jsonObj.put("vtp", vtp);
		String urls=CommonUtil.getEncryptText(jsonObj);
		System.out.println("postLogin-urls====="+urls);
		/**
		 * 根据urls生成sign
		 */
		String sign="";
		try {
			 sign=EncryptUtil.hmacSHA1Encrypt(urls, "jsy309");
			 System.out.println("POSTLOGIN接口生成的sign："+sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 将request请求的全部数据put进去JSONObject
		 * 将加密后的uid和pw ，put进去
		 */
		jsonObj.put("uuid", uuid);
		
		jsonObj.put("uid", encryptUID);
		jsonObj.put("shaketype", shaketype);
		jsonObj.put("appid", appid);
		jsonObj.put("vtp", vtp);
		jsonObj.put("pw", encryptPW);
		jsonObj.put("sign", sign);
		
//		String postURL="http://192.168.0.44:8511/api/CloudApi/PostLogin";
		String postURL=CommonUtil.getUrl("cai");
		System.out.println("postURL+API+PostLogin======="+postURL+API+"PostLogin");
		String responseData = HttpUtil.postHttpJson(postURL+API+"PostLogin", jsonObj.toString());
//		String responseData = HttpUtil.postHttpJson(postURL, jsonObj.toString());
		System.out.println("jsonObj--postLogin====="+jsonObj+"\n向postLogin接口请求的数据："+jsonObj.toString());
		System.out.println("POSTLOGIN---responseData===="+responseData);
		return responseData;
	}

	/**
	 * 握手方法
	 */
	@Override
	public String PostShakeHand(String shaketype, String appid) throws Exception{
		String sign = "";

		// TODO Auto-generated method stub
		/**
		 * uuid的获取，该值是电脑网卡的物理地址
		 */
		String uuid = CommonUtil.getPhysicalAddress();
		/**
		 * sign 签名
		 * 将所传的参数键=值组成对后，按字母顺序排列，并按“&”连接，组成的字符串通过HMACSHA1及相应密钥生成BASE64位摘要
		 */
		String urls = "appid=" + appid + "&shaketype=" + shaketype + "&uuid=" + uuid;
		System.out.println("urls===" + urls);
		try {
			/**
			 * 调用url签名算法将参数字符串签名得到BASE64位摘要
			 */

			sign = EncryptUtil.hmacSHA1Encrypt(urls, "jsy309");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**
		 * 将post请求的参数及值json序列化后post给接口程序
		 */

		JSONObject requestJSONObject = new JSONObject();
		requestJSONObject.put("uuid", uuid);
		requestJSONObject.put("shaketype", shaketype);
		requestJSONObject.put("appid", appid);
		requestJSONObject.put("sign", sign);
		String postURL = "http://192.168.0.44:8511/api/CloudApi/PostShakeHand";
		/**
		 * http post 请求该接口，并用String接收其返回来的数据
		 */
	//	System.out.println("requestJSONObject.toString()====" + requestJSONObject.toString());
		String responseData = HttpUtil.postHttpJson(postURL, requestJSONObject.toString());
		//System.out.println("responseString=====" + responseData);
		
		return responseData;
	}

}
