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

package com.goldeneyes.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.TreeMap;

import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
public class HttpUtil {
	public static void main(String[] args) {
		//JSONObject object = new JSONObject();
		/*object.put("uuid", CommonUtil.getPhysicalAddress());
		object.put("appid", "JAVAPC");
		object.put("token", "C72521");
		object.put("pageIndex", 1);
		object.put("pageSize", 0);*/
		JSONObject requestJSONObject = new JSONObject();
		requestJSONObject.put("uuid", CommonUtil.getPhysicalAddress());
		requestJSONObject.put("shaketype", "login");
		requestJSONObject.put("appid", "JAVAPC");
		String postURL = "http://192.168.0.44:8511/api/CloudApi/PostShakeHand";
	//	String urls = "appid=" + "JAVAPC" + "&shaketype=" + "login" + "&uuid=" + CommonUtil.getPhysicalAddress();
		try {
			requestJSONObject.put("sign", EncryptUtil.hmacSHA1Encrypt(CommonUtil.getEncryptText(requestJSONObject), "jsy309"));
			//requestJSONObject.put("sign", EncryptUtil.hmacSHA1Encrypt(urls, "jsy309"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String outJson = HttpUtil.postHttpJson(postURL, requestJSONObject.toString());
		System.out.println(outJson);
	}

	/**
	 * 提交Http请求，post json数据
	 * 
	 * @param serviceUrl
	 *            接口地址
	 * @param inJson
	 *            提交的json
	 * @return 返回的json
	 */
	public static String postHttpJson(String serviceUrl, String inJson) {
		String outJson = "";
		try {
			// 创建url资源
			URL url = new URL(serviceUrl);
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置允许输出
			conn.setDoOutput(true);

			conn.setDoInput(true);

			// 设置不用缓存
			conn.setUseCaches(false);
			// 设置传递方式
			conn.setRequestMethod("POST");
			// 设置维持长连接
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 设置文件字符集:
			conn.setRequestProperty("Charset", "UTF-8");
			// 转换为字节数组
			byte[] data = (inJson).getBytes("UTF-8");
			// 设置文件长度
			conn.setRequestProperty("Content-Length", String.valueOf(data.length));

			// 设置文件类型:
			conn.setRequestProperty("Content-type", "application/json");

			// 开始连接请求
			conn.connect();
			OutputStream out = conn.getOutputStream();
			// 写入请求的字符串
			out.write(inJson.getBytes("UTF-8"));
			out.flush();
			out.close();

			System.out.println(conn.getResponseCode());

			// 请求返回的状态
			if (conn.getResponseCode() == 200) {
				System.out.println("连接成功");
				// 请求返回的数据
				InputStream in = conn.getInputStream();
				try {
					byte[] data1 = new byte[in.available()];
					in.read(data1);
					// 转成字符串
					outJson = new String(data1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				System.out.println("no++");
			}

		} catch (Exception e) {

		}
		return outJson;
	}

}
