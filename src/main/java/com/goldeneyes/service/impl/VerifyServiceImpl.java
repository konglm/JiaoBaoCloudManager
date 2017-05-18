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

import com.goldeneyes.object.AskAnswerChannel;
import com.goldeneyes.service.VerifyService;
import com.goldeneyes.util.CommonUtil;
import com.goldeneyes.util.EncryptUtil;
import com.goldeneyes.util.HttpUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
public class VerifyServiceImpl implements VerifyService {
	
	public static void main(String[] args){
		VerifyServiceImpl test = new VerifyServiceImpl();
		try {
			test.renewToken(1,"VI24sGNt8bi0MtU8WBX20o85Isw=");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  @author konglm
	 */
	@Override
	public String renewToken(int utid, String oldToken) throws Exception {
		// TODO Auto-generated method stub
		JSONObject tokenObject=new JSONObject();
		String token = "";
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
		jsonObj.put("appid", "JAVAPC");
		jsonObj.put("utid", utid);
		jsonObj.put("token", oldToken);
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
		jsonObj.put("sign", sign);
		System.out.println(jsonObj.toString());
		String outJson = HttpUtil.postHttpJson(CommonUtil.getUrl("cai") + "/api/CloudApi/PostTokenRenew", jsonObj.toString());
		System.out.println(outJson);
		//获得接口返回的json
		tokenObject = JSONObject.fromObject(outJson);
		//返回正常，才进行解析
		if(CommonUtil.getRspCodeFromJson(tokenObject).equals("0000")){
			//析出内容
			token = tokenObject.getString("RspData");
		}
		System.out.println(token);
		return token;
	}

}
