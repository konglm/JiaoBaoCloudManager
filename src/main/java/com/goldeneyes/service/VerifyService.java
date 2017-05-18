/*----------------------------------------------------------------
 *  Copyright (C) 2016山东金视野教育科技股份有限公司
 * 版权所有。 
 *
 * 文件名：VerifyService
 * 文件功能描述：验证用service
 *
 * 
 * 创建标识：konglm20170516
 *
 * 修改标识：
 * 修改描述：
 *----------------------------------------------------------------*/

package com.goldeneyes.service;

/**
 * @author konglm
 *
 */
public interface VerifyService {

	/**
	 * 过期更新Token
	 * @param oldToken
	 * @return
	 * @throws Exception
	 */
	public String renewToken(int utid, String oldToken) throws Exception;

}
