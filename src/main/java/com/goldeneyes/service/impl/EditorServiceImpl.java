/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：EditorServiceImpl
 * 文件功能描述：
 *
 * 
 * 创建标识：konglm20170511
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.goldeneyes.object.AskAnswerChannel;
import com.goldeneyes.service.EditorService;
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
@Service("editorService")
public class EditorServiceImpl implements EditorService {

	/**
	 * @author konglm
	 */
	@Override
	public List<AskAnswerChannel> getEditorInitInfo(String token) throws Exception {
		// TODO Auto-generated method stub
		List<AskAnswerChannel> channels = new ArrayList<AskAnswerChannel>();
		JSONObject channelObject = new JSONObject();
		// 构造json，post接口
		JSONObject object = new JSONObject();
		object.put("pageIndex", 1);
		object.put("pageSize", 0);
		object = CommonUtil.addCommonServiceParam(object, token);
		System.out.println(CommonUtil.getUrl("jbc") + "/JiaoBaoCloudService/askAnswer/getAllChannels");
		String outJson = HttpUtil.postHttpJson(
				CommonUtil.getUrl("jbc") + "/JiaoBaoCloudService/askAnswer/getAllChannels", object.toString());
		System.out.println(outJson);
		// 获得接口返回的json
		channelObject = JSONObject.fromObject(outJson);
		// 返回正常，才进行解析
		if (CommonUtil.getRspCodeFromJson(channelObject).equals("0000")) {
			// 析出RspData
			channelObject = CommonUtil.getRspDataFromJson(channelObject);
			// 析出内容，形成channel
			JSONArray channelArray = new JSONArray();
			channelArray = channelObject.getJSONArray("Data");
			if (!channelArray.isEmpty()) {
				for (int i = 0; i < channelArray.size(); i++) {
					JSONObject jsonChannel = new JSONObject();
					jsonChannel = channelArray.getJSONObject(i);
					AskAnswerChannel channel = new AskAnswerChannel();
					channel.setTabid(jsonChannel.getInt("TabId"));
					channel.setChannelcode(jsonChannel.getString("ChannelCode"));
					channel.setChannelname(jsonChannel.getString("ChannelName"));
					channels.add(channel);
				}

			}
		}

		return channels;
	}

	/**
	 *  @author konglm
	 */
	@Override
	public int addAsk(String token, int userId, int channelId, String title, String content) throws Exception {
		// TODO Auto-generated method stub
		JSONObject resultObject = new JSONObject();
		//构造json，post接口
		JSONObject object = new JSONObject();		
		object.put("askTitle", title);
		object.put("askNote", content);
		object.put("encType", 5);
		object.put("encLen", 0);
		object.put("encAddr", "");
		object.put("thumbnail", "");
		object.put("cutImg", "");
		object.put("askChannel", channelId);
		object.put("askMan", userId);
		object.put("isAnonym", 0);
		int[] inviteExperts = new int[0];
		object.put("inviteExperts", inviteExperts);
		object = CommonUtil.addCommonServiceParam(object,token);
		String outJson = HttpUtil.postHttpJson(CommonUtil.getUrl("jbc") + "/JiaoBaoCloudService/askAnswer/addAsk", object.toString());
		System.out.println(outJson);
		//获得接口返回的json
		resultObject = JSONObject.fromObject(outJson);
		//返回正常，才进行解析
		int addResult = 0;
		if(CommonUtil.getRspCodeFromJson(resultObject).equals("0000")){
			//析出RspData
			resultObject = CommonUtil.getRspDataFromJson(resultObject);
			//析出内容
			addResult = (int) resultObject.getLong("Result");
		} else if (CommonUtil.getRspCodeFromJson(resultObject).equals("0006")){
			VerifyService vsi = new VerifyServiceImpl();
			String newToken = vsi.renewToken(userId, token);
			//如果没有获得新令牌，抛出异常
			if((newToken.equals("")) || (newToken == null)){
				throw new Exception();
			}
			this.addAsk(newToken, userId, channelId, title, content);
			return 0;
		} else {
			throw new Exception();
		}
		return addResult;
	}

	/**
	 *  @author konglm
	 */
	@Override
	public int addUserSpace(String token, int userId, String pubArea, String content, int channelId) throws Exception {
		// TODO Auto-generated method stub
		/*
		 * 获取所有的群用户
		 */
		//获取所有的群
		JSONObject jsonGroup = new JSONObject();
		jsonGroup.put("utid", userId);
		jsonGroup.put("vtp", "ag");
		jsonGroup.put("vvl", userId);
		jsonGroup = CommonUtil.addCommonServiceParam(jsonGroup,token);
		System.out.println(jsonGroup.toString());
		String outJsonG = HttpUtil.postHttpJson(CommonUtil.getUrl("cai") + "/api/CloudApi/PostGList", jsonGroup.toString());
		System.out.println(outJsonG);
		JSONObject resultObjectG = new JSONObject();
		resultObjectG = JSONObject.fromObject(outJsonG);
		List<Integer> groupIds = new ArrayList<Integer>();
		if(CommonUtil.getRspCodeFromJson(resultObjectG).equals("0000")){			
			//析出内容
			JSONArray groupArray = resultObjectG.getJSONArray("RspData");
			if(!groupArray.isEmpty()){
				for(int i = 0; i < groupArray.size(); i++){
					JSONObject jsonG = new JSONObject();
					jsonG = groupArray.getJSONObject(i);
					groupIds.add(jsonG.getInt("gid"));
				}
			}
		}  else if (CommonUtil.getRspCodeFromJson(resultObjectG).equals("0006")){
			VerifyService vsi = new VerifyServiceImpl();
			String newToken = vsi.renewToken(userId, token);
			//如果没有获得新令牌，抛出异常
			if((newToken.equals("")) || (newToken == null)){
				throw new Exception();
			}
			this.addUserSpace(newToken, userId, pubArea, content, channelId);
			return 0;
		} else {
			throw new Exception();
		}
		//去重
		List<Integer> groupId4Qrys = new ArrayList<Integer>(new HashSet<Integer>(groupIds));
		//获取群中所有的用户
		String groupIdStr = "";
		for(int groupId : groupId4Qrys){
			groupIdStr = groupIdStr + groupId + ",";
		}
		if(!groupIdStr.equals("")){
			groupIdStr = groupIdStr.substring(0, groupIdStr.length() - 1);
		}
		JSONObject jsonUser = new JSONObject();
		jsonUser.put("utid", userId);
		jsonUser.put("top", -1);
		jsonUser.put("vvl", groupIdStr);
		jsonUser.put("vvl1",-1);
		jsonUser = CommonUtil.addCommonServiceParam(jsonUser,token);
		System.out.println(jsonUser.toString());
		String outJsonU = HttpUtil.postHttpJson(CommonUtil.getUrl("cai") + "/api/CloudApi/PostGusers", jsonUser.toString());
		System.out.println(outJsonU);
		JSONObject resultObjectU = new JSONObject();
		resultObjectU = JSONObject.fromObject(outJsonU);
		List<Integer> userIds = new ArrayList<Integer>();
		if(CommonUtil.getRspCodeFromJson(resultObjectU).equals("0000")){			
			//析出内容
			JSONArray userArray = resultObjectU.getJSONArray("RspData");
			if(!userArray.isEmpty()){
				for(int i = 0; i < userArray.size(); i++){
					JSONObject jsonU = new JSONObject();
					jsonU = userArray.getJSONObject(i);
					userIds.add(jsonU.getInt("utid"));
				}
			}
		} else {
			throw new Exception();
		}		
		//去重
		List<Integer> userId4Qrys = new ArrayList<Integer>(new HashSet<Integer>(userIds));
		JSONObject resultObject = new JSONObject();
		//构造json，post接口
		JSONObject object = new JSONObject();		
		object.put("userId", userId);
		object.put("msgContent", content);
		object.put("encType", 5);
		object.put("encLen", 0);
		object.put("encAddr", "");
		object.put("encImg", "");
		object.put("encIntro", "");
		object.put("noteType", 2);
		object.put("userIds", userId4Qrys);
		List<Integer> pubScopes = new ArrayList<Integer>();
		if(channelId == 1){
			pubScopes.add(1);
		} else {
			pubScopes.add(1);
			pubScopes.add(2);
		}
		object.put("pubScopes", pubScopes);	
		object.put("pubArea", CommonUtil.getArea(pubArea));
		object = CommonUtil.addCommonServiceParam(object,token);
		System.out.println(CommonUtil.getUrl("jbc") + "/JiaoBaoCloudService/userSpace/addUserSpace");
		System.out.println(object.toString());
		String outJson = HttpUtil.postHttpJson(CommonUtil.getUrl("jbc") + "/JiaoBaoCloudService/userSpace/addUserSpace", object.toString());
		System.out.println(outJson);
		//获得接口返回的json
		resultObject = JSONObject.fromObject(outJson);
		//返回正常，才进行解析
		int addResult = 0;
		if(CommonUtil.getRspCodeFromJson(resultObject).equals("0000")){
			//析出RspData
			resultObject = CommonUtil.getRspDataFromJson(resultObject);
			//析出内容
			addResult = (int) resultObject.getLong("Result");
		} else {
			throw new Exception();
		}
		return addResult;
	}

}
	
