package com.goldeneyes.qiniu;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import com.baidu.ueditor.define.QiNiuCom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldeneyes.util.CommonUtil;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.UrlSafeBase64;

import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * 七牛上传帮助类
 */
public class QiniuHelper {
	private static ObjectMapper objectMapper = new ObjectMapper();
	private final static int maxSize = 200;
	private final static String str_BucketName = "pb";// 根据项目阶段（开发或测试）及具体需求（需要存储私有或公开）进行填写

	public static void main(String[] args) {
		// 获取token
		// String tokenResult = getToken("pc/", "test2.jpg");
		//
		// JSONObject jsonToken = JSONObject.fromObject(tokenResult);
		// JSONObject jsonData = jsonToken.getJSONObject("Data");
		// String token = jsonData.getString("Token");
		// String key = jsonData.getString("Key");
		//
		// System.out.println("token == " + token);
		// System.out.println("key == " + key);
		String filePath = "E:\\jinan.jpg";
		String fileToPath = "E:\\jinan_s.jpg";
		File file = new File(filePath);
		try {
			UploadFile(file,"jinan.jpg");
		} catch (QiniuException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 上传附件
	 * 
	 * @throws QiniuException
	 */
	public static String UploadFile(File file, String fileName) throws QiniuException {

		System.out.println("FileName === " + fileName);
		// 生成文件名
		fileName = CommonUtil.getRandomFileName() + "." + CommonUtil.getExtensionName(fileName);
		// 先获取token
		String tokenResult = getToken("pc/", fileName, "8");
		JSONObject jsonToken = JSONObject.fromObject(tokenResult);
		JSONObject jsonData = jsonToken.getJSONObject("Data");
		String token = jsonData.getString("Token");
		String key = jsonData.getString("Key");
		int width = 0;
		int height = 0;
		if (!CommonUtil.getExtensionName(fileName).equals("mp4")) {
			// 压缩文件			
			try {
				BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
				width = sourceImg.getWidth();
				height = sourceImg.getHeight();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			String path = QiniuHelper.class.getResource("").getPath();
//			System.out.println(path);
			File directory = new File("");//设定为当前文件夹   
			try {
				if ((width > 1024) || (height > 1024)) {
					Thumbnails.of(file).size(1024, 1024).toFile(directory.getAbsolutePath() + "/" + fileName);
//					// 用压缩文件上传
					File fileNew = new File(directory.getAbsolutePath() + "/" + fileName);
					file = fileNew;
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// 创建上传对象
		Configuration config = new Configuration(Zone.zone0());
		UploadManager uploadManager = new UploadManager(config);

		// 上传文件
		Response response = uploadManager.put(file, key, token);
		// 解析上传成功的结果
		DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
		System.out.println("上传完毕！");
		System.out.println(putRet.key);
		System.out.println(putRet.hash);
		//如果压缩过，删除压缩文件
		if ((width > 1024) || (height > 1024)) {
			file.delete();
		}
		return key;
	}

	/**
	 * 获得url地址
	 */
	public static String GetUrl(String key) {
		return String.format("http://%s/%s", CommonUtil.getProp("QiNiuSpace"), key);
	}

	// 获取Token
	public static String getToken(String uploadSpace, String fileName, String appId) {
		// 设置参数
		String str_Key = uploadSpace + fileName;// 此处不需要添加第一级前缀名，一级前缀名由授权接口控制，之后的前缀名及命名规范由各APP自行控制
		String mainSpace = str_BucketName;
		String saveSpace = uploadSpace;
		String thumbSpace = saveSpace + "thumb/";
		String thumbName = fileName;
		if (CommonUtil.getExtensionName(fileName).equals("mp4")) {
			thumbName = CommonUtil.getFileNameNoEx(fileName) + ".jpg";
		}
		String thumbKey = UrlSafeBase64.encodeToString((mainSpace + ":" + thumbSpace + thumbName).getBytes());
		System.out.println(thumbKey);
		String str_Pops = "";
		if (CommonUtil.getExtensionName(fileName).equals("mp4")) {
			str_Pops = "vframe/jpg/offset/1/w/" + maxSize + "/h/" + maxSize + "|saveas/" + thumbKey;// 此处可追加预处理命令，视频上传的时候，一起上传缩略图
		} else if  (CommonUtil.getExtensionName(fileName).equals("mp3")) {
			str_Pops = "";
		} else {
			str_Pops = "imageView2/1/w/" + maxSize + "/h/" + maxSize + "/format/png|saveas/" + thumbKey;// 此处可追加预处理命令，图片在上传原图的时候，一起上传缩略图
		}
		// String str_Pops = ""; // 需要保存的指令，指定文件key时
		// 一样不需要添加第一级前缀名，接口程序会处理
		String str_NotifyUrl = "";// 通知页面地址
		// 形成参数对象
		QiNiuCom com = new QiNiuCom();
		com.setBucket(mainSpace);
		com.setKey(saveSpace + fileName);
		com.setPops(str_Pops);
		com.setNotifyUrl("");

		// com.setBucket(mainSpace);
		// com.setKey("Knowledge/1494043559671.jpg");
		// com.setPops("imageView2/1/w/200/h/200/format/png|saveas/cGI6S25vd2xlZGdlL3RodW1iLzE0OTQwNDM1NTk2NzEuanBn;imageMogr2/gravity/Center/crop/!576x259.2/format/png|saveas/cGI6S25vd2xlZGdlL2NsaXAvMTQ5NDA0MzU1OTY3MS5qcGc=");
		// com.setNotifyUrl("");
		// post http请求
		String result = "";
		System.out.println(CommonUtil.getProp("GetUpLoadToKenUrl"));
		try {
			result = httpPost(CommonUtil.getProp("GetUpLoadToKenUrl"), appId,
					desEncrypt(getAppPwd(appId), objectMapper.writeValueAsString(com)));
			System.out.println("result ====== " + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * form post到url
	 * 
	 * @param url
	 * @param appId
	 * @param param
	 * @return
	 */
	public static String httpPost(String urlStr, String appId, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(urlStr);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print("AppID=" + appId + "&Param=" + param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// url安全的64位编码
	private static String safeUrlBase64Encode(byte[] data) {
		String encodeBase64 = new BASE64Encoder().encode(data);
		String safeBase64Str = encodeBase64.replace('+', '-');
		safeBase64Str = safeBase64Str.replace('/', '_');
		safeBase64Str = safeBase64Str.replaceAll("=", "");
		return safeBase64Str;
	}

	// DES加密函数
	private static String desEncrypt(String key, String message) {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		System.out.println(1);
		String path = QiniuHelper.class.getResource("").getPath();
		System.out.println(2);
		try {
			engine.eval(new FileReader(path + "/cryption.js"));
		} catch (FileNotFoundException | ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String result = "";
		if (engine instanceof Invocable) {
			Invocable in = (Invocable) engine;
			try {
				result = (String) in.invokeFunction("encryptByDES", key, message);
			} catch (NoSuchMethodException | ScriptException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("加密结果为：" + result);
		}
		return result;
	}

	/**
	 * 根据APPID返回密码
	 * 
	 * @param appId
	 * @return
	 */
	private static String getAppPwd(String appId) {
		switch (appId) {
		case "1": {
			return "";
		}
		case "2": {// 资源平台
			return "jsy8004";
		}
		case "3": {// 教宝云作业
			return "zy309309!";
		}
		case "4": {// 教宝云盘
			return "jbyp@2017";
		}
		case "5": {// 教宝云用户管理
			return "jbman456";
		}
		case "6": {// 家校圈
			return "jxq789!@";
		}
		case "7": {// 求知
			return "qz123qwe";
		}
		case "8": {// pc
			return "ypc890$";
		}
		default: {
			return "";
		}
		}

	}
}
