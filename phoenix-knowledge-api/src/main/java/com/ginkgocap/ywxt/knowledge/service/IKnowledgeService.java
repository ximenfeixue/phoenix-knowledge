package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;

/**
 * @Title: 知识管理服务
 * @Description: 知识管理服务
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IKnowledgeService {

	/**
	 * 插入，承担以下任务：
	 * <p>1、知识详细表插入</P>
	 * <p>2、知识基础表插入</P>
	 * <p>3、知识来源表插入</P>
	 * <p>4、大数据MQ推送</P>
	 * <p>5、动态推送</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:03
	 * @param dataCollection
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> insert(DataCollection dataCollection, User user) throws Exception;
	
	/**
	 * 更新，承担以下任务：
	 * <p>1、知识详细表更新</P>
	 * <p>2、知识基础表更新</P>
	 * <p>3、知识来源表更新</P>
	 * <p>4、大数据MQ推送更新</P>
	 * <p>5、动态推送更新</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:16
	 * @param dataCollection
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> update(DataCollection dataCollection, User user) throws Exception;
	
	/**
	 * 删除，承担以下任务：
	 * <p>1、知识详细表删除</P>
	 * <p>2、知识基础表删除</P>
	 * <p>3、知识来源表删除</P>
	 * <p>4、大数据MQ推送删除</P>
	 * <p>5、动态推送删除</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:20
	 * @param knowledgeId
	 * @param columnId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> deleteByKnowledgeId(long knowledgeId, long columnId, User user) throws Exception;
	
	/**
	 * 批量删除，承担以下任务：
	 * <p>1、知识详细表批量删除</P>
	 * <p>2、知识基础表批量删除</P>
	 * <p>3、知识来源表批量删除</P>
	 * <p>4、大数据MQ推送批量删除</P>
	 * <p>5、动态推送批量删除</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:23
	 * @param knowledgeIds
	 * @param columnId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> deleteByKnowledgeIds(List<Long> knowledgeIds, long columnId, User user) throws Exception;
	
	/**
	 * 提取详细信息（一般用在知识详细信息查看界面、知识编辑界面的数据提取中），具体提取以下信息：
	 * <p>1、知识详细表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:26
	 * @param knowledgeId
	 * @param columnId 由于知识详细表信息为分库存储，则columnId为必须字段
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> getDetailById(long knowledgeId,long columnId,User user) throws Exception;
	
	/**
	 * 提取简要信息（一般用在知识简要信息界面的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:39
	 * @param knowledgeId
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId,User user) throws Exception;
	
	/**
	 * 提取简要信息列表（一般用在知识简要信息界面的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:37
	 * @param knowledgeIds
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByIds(List<Long> knowledgeIds,User user) throws Exception;
	
	/**
	 * 提取所有数据（一般用在首页数据展示中）
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 下午5:43:26
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseAll(int start,int size) throws Exception;
	
	/**
	 * 根据用户ID提取简要信息列表（一般用在用户个人中心知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:33
	 * @param user
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserId(User user,int start,int size) throws Exception;
	
	/**
	 * 根据用户ID与栏目ID提取简要信息列表（一般用在用户个人中心知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:32
	 * @param user
	 * @param columnId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnId(User user,long columnId,int start,int size) throws Exception;
	
	/**
	 * 根据用户ID与类型提取简要信息列表（一般用个人中心在根据类型区分的知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:30
	 * @param user
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndType(User user,String type,int start,int size) throws Exception;
	
	/**
	 * 根据用户ID与类型提取简要信息列表（一般用在个人中心根据类型、栏目区分的知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午9:41:29
	 * @param user
	 * @param columnId
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnIdAndType(User user,long columnId,String type,int start,int size) throws Exception;
	
	/**
	 * 根据类型提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午10:14:51
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByType(String type,int start,int size) throws Exception;
	
	/**
	 * 根据栏目提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午10:14:55
	 * @param columnId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByColumnId(long columnId,int start,int size) throws Exception;
	
	/**
	 * 根据栏目、类型提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @author 周仕奇
	 * @date 2016年1月15日 上午10:14:59
	 * @param columnId
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndType(long columnId,String type,int start,int size) throws Exception;
}