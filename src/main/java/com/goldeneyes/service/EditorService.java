/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：EditorService
 * 文件功能描述：
 *
 * 
 * 创建标识：konglm20170511
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.service;

import java.util.List;

import com.goldeneyes.object.AskAnswerChannel;

/**
 * @author konglm
 *
 */
public interface EditorService {
	/**
	 * 获取问题话题列表
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public List<AskAnswerChannel> getEditorInitInfo(String token) throws Exception;
	/**
	 * 新增发布提问
	 * @param userId
	 * @param channelId
	 * @param title
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public int addAsk(String token, int userId, int channelId, String title, String content) throws Exception;
	/**
	 * 新增展现
	 * @param token
	 * @param userId
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public int addUserSpace(String token, int userId, String pubArea, String content, int channelId) throws Exception;

}
