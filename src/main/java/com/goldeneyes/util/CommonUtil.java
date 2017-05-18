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

import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.gson.Gson;

import net.sf.json.JSONObject;

/**
 * @author konglm
 *
 */
public class CommonUtil {
	public static void main(String[] args) {
		System.out.println(getArea("370000 370100|山东省 济南市"));
	}
	
	/**
	 * 从区域串中获取区域
	 * @param areaStr
	 * @return
	 */
	public static String getArea(String areaStr){
		return areaStr.substring(7, 13);
	}

	/**
	 * 读取配置文件中的url
	 * 
	 * @param urlType
	 * @return
	 */
	public static String getUrl(String urlType) {
		Properties properties = new Properties();
		Resource resource = new ClassPathResource("/common.properties");
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String httpType = properties.getProperty("HttpType");
		String ip = "";
		String port = "";
		if (urlType.equals("jbc")) {
			ip = properties.getProperty("JiaoBaoCloudIP");
			port = properties.getProperty("JiaoBaoCloudPort");
		} else if (urlType.equals("cai")) {
			ip = properties.getProperty("CloudApiIP");
			port = properties.getProperty("CloudApiPort");
		}
		return httpType + "://" + ip + ":" + port;
	}
	
	/**
	 * 获取配置参数
	 * @param propName
	 * @return
	 */
	public static String getProp(String propName){
		Properties properties = new Properties();
		Resource resource = new ClassPathResource("/common.properties");
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties.getProperty(propName);
	}
	
	/**
	 * 获取不带扩展名的文件名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    }

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * 判断Token是否超期 超期：Token续订 未超期：返回原Token
	 * 
	 * @param inToken
	 *            原Token
	 * @return
	 */
	public static String checkToken(String inToken) {
		return null;
	}

	/**
	 * 析出RspCode
	 * 
	 * @param inJson
	 * @return
	 */
	public static String getRspCodeFromJson(JSONObject inJson) {
		return inJson.getString("RspCode");
	}

	/**
	 * 析出RspData
	 * 
	 * @param inJson
	 * @return
	 */
	public static JSONObject getRspDataFromJson(JSONObject inJson) {
		JSONObject object = new JSONObject();
		object = (JSONObject) inJson.get("RspData");
		return object;
	}

	/**
	 * 添加接口的公用参数
	 * 
	 * @param inObj
	 *            其他参数
	 * @param token
	 *            传入的令牌
	 * @return
	 */
	public static JSONObject addCommonServiceParam(JSONObject inObj, String token) {
		inObj.put("uuid", CommonUtil.getPhysicalAddress());
		inObj.put("appid", "JAVAPC");
		inObj.put("token", token);
		try {
			inObj.put("sign", EncryptUtil.hmacSHA1Encrypt(CommonUtil.getEncryptText(inObj), "jsy309"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inObj;

	}

	/**
	 * 获取电脑网卡物理地址类
	 * 
	 * @author xubs
	 * @return
	 */
	public static String getPhysicalAddress() {
		Enumeration<NetworkInterface> ni;
		try {
			ni = NetworkInterface.getNetworkInterfaces();

			while (ni.hasMoreElements()) {
				NetworkInterface netI = ni.nextElement();

				byte[] bytes = netI.getHardwareAddress();
				if (netI.isUp() && netI != null && bytes != null && bytes.length == 6) {
					StringBuffer sb = new StringBuffer();
					for (byte b : bytes) {
						// 与11110000作按位与运算以便读取当前字节高4位
						sb.append(Integer.toHexString((b & 240) >> 4));
						// 与00001111作按位与运算以便读取当前字节低4位
						sb.append(Integer.toHexString(b & 15));
						sb.append("-");
					}
					sb.deleteCharAt(sb.length() - 1);
					return sb.toString().toUpperCase();
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Json排序
	 * 
	 * @param obj
	 * @return
	 */
	public static TreeMap sortJsonObject(JSONObject obj) {
		TreeMap map = new TreeMap();
		Iterator<String> it = obj.keys();
		while (it.hasNext()) {
			String key = it.next();
			Object value = obj.get(key);
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 获得签名
	 * 
	 * @param signJson
	 * @return
	 */
	public static String getEncryptText(JSONObject signJson) {
		// 排序参数
		String encryptText = "";
		try {
			TreeMap treeMap = sortJsonObject(signJson);

			Iterator it = treeMap.keySet().iterator();
			while (it.hasNext()) {
				String keyt = it.next().toString();
				String valuet = treeMap.get(keyt).toString();
				if (!keyt.equals("sign")) {
					// 特殊处理含有vvl和vvl1的排序问题
					if(!(keyt.equals("vvl") && treeMap.containsKey("vvl1"))){
						encryptText = encryptText + keyt + "=" + valuet + "&";
					}
				}
			}
			encryptText = encryptText.substring(0, encryptText.length() - 1);
			if(treeMap.containsKey("vvl") && treeMap.containsKey("vvl1")){
				String valuet = treeMap.get("vvl").toString();
				encryptText = encryptText + "&vvl" + "=" + valuet;
			}
		} catch (Exception e) {

		}
		return encryptText;
	}

}
