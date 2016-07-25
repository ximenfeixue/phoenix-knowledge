package com.ginkgocap.ywxt.knowledge.service.v1;

import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * @Title: 知识管理服务
 * @Description: 知识管理服务
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface KnowledgeServiceV1 {

	/**
	 * 插入，承担以下任务：
	 * <p>1、知识详细表插入</P>
	 * <p>2、知识基础表插入</P>
	 * <p>3、知识来源表插入</P>
	 * <p>4、大数据MQ推送</P>
	 * <p>5、动态推送</P>
	 * @date 2016年1月15日 上午9:41:03
	 * @param dataCollection
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult insert(DataCollection dataCollection);
	
	/**
	 * 更新，承担以下任务：
	 * <p>1、知识详细表更新</P>
	 * <p>2、知识基础表更新</P>
	 * <p>3、知识来源表更新</P>
	 * <p>4、大数据MQ推送更新</P>
	 * <p>5、动态推送更新</P>
	 * @date 2016年1月15日 上午9:41:16
	 * @param dataCollection
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult update(DataCollection dataCollection) throws Exception;

    /**
     * 更新，承担以下任务：
     * <p>1、知识详细表更新</P>
     * <p>2、知识基础表更新</P>
     * @date 2016年1月15日 上午9:41:16
     * @param dataCollection
     * @return
     * @throws Exception
     */
    public boolean updateKnowledge(DataCollection dataCollection) throws Exception;
	/**
	 * 删除，承担以下任务：
	 * <p>1、知识详细表删除</P>
	 * <p>2、知识基础表删除</P>
	 * <p>3、知识来源表删除</P>
	 * <p>4、大数据MQ推送删除</P>
	 * <p>5、动态推送删除</P>
	 * @date 2016年1月15日 上午9:41:20
	 * @param knowledgeId
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult deleteByKnowledgeId(long knowledgeId, int columnId) throws Exception;
	
	/**
	 * 批量删除，承担以下任务：
	 * <p>1、知识详细表批量删除</P>
	 * <p>2、知识基础表批量删除</P>
	 * <p>3、知识来源表批量删除</P>
	 * <p>4、大数据MQ推送批量删除</P>
	 * <p>5、动态推送批量删除</P>
	 * @date 2016年1月15日 上午9:41:23
	 * @param knowledgeIds
	 * @param columnId
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult batchDeleteByKnowledgeIds(List<Long> knowledgeIds, int columnId) throws Exception;
	
	/**
	 * 提取详细信息（一般用在知识详细信息查看界面、知识编辑界面的数据提取中），具体提取以下信息：
	 * <p>1、知识详细表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @date 2016年1月15日 上午9:41:26
	 * @param knowledgeId
	 * @param columnId 由于知识详细表信息为分库存储，则columnId为必须字段
	 * @return
	 * @throws Exception
	 */
	public KnowledgeDetail getDetailById(long knowledgeId,int columnId) throws Exception;

    /**
     * 更新，承担以下任务：
     * <p>1、获取知识详细</P>
     * <p>2、获取知识基础</P>
     * @return
     * @throws Exception
     */
    public DataCollection getKnowledge(long knowledgeId,int columnId) throws Exception;
	
	/**
	 * 提取简要信息（一般用在知识简要信息界面的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @date 2016年1月15日 上午9:41:39
	 * @param knowledgeId
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId) throws Exception;
	
	/**
	 * 提取简要信息列表（一般用在知识简要信息界面的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * <p>2、知识来源表信息</P>
	 * @date 2016年1月15日 上午9:41:37
	 * @param knowledgeIds
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByIds(List<Long> knowledgeIds) throws Exception;
	
	/**
	 * 提取所有数据（一般用在首页数据展示中）
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 下午5:43:26
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseAll(int start,int size) throws Exception;

	/**
	 * 提取所有数据（一般用在首页数据展示中）
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 下午5:43:26
	 * @return
	 * @throws Exception
	 */
	public long getBaseAllPublicCount(short permission) throws Exception;

	/**
	 * 提取所有数据（一般用在首页数据展示中）
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 下午5:43:26
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseAllPublic(int start,int size,short permission) throws Exception;
	
	/**
	 * 根据用户ID提取简要信息列表（一般用在用户个人中心知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午9:41:33
	 * @param userId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByCreateUserId(long userId,int start,int size) throws Exception;

    /**
     * 根据用户ID与栏目ID提取简要信息列表（一般用在用户个人中心知识信息列表查询的数据提取中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 上午9:41:32
     * @param knowledgeIds
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getMyCollected(List<Long> knowledgeIds,String keyword) throws Exception;

	/**
	 * 根据用户ID与栏目ID提取简要信息列表（一般用在用户个人中心知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午9:41:32
	 * @param columnId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByCreateUserIdAndColumnId(long userId,int columnId,int start,int size) throws Exception;
	
	/**
	 * 根据用户ID与类型提取简要信息列表（一般用个人中心在根据类型区分的知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午9:41:30
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByCreateUserIdAndType(long userId,short type,int start,int size) throws Exception;
	
	/**
	 * 根据用户ID与类型提取简要信息列表（一般用在个人中心根据类型、栏目区分的知识信息列表查询的数据提取中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午9:41:29
	 * @param userId
	 * @param columnId
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByCreateUserIdAndColumnIdAndType(long userId,int columnId,short type,int start,int size) throws Exception;
	
	/**
	 * 根据类型提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:51
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByType(short type,int start,int size) throws Exception;
	
	/**
	 * 根据栏目提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:55
	 * @param columnId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBaseByColumnId(int columnId,int start,int size) throws Exception;


	/**
	 * 根据栏目提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:55
	 * @param columnId
	 * @param permission
	 * @return
	 * @throws Exception
	 */
	public long getBasePublicCountByColumnId(int columnId,short permission) throws Exception;

	/**
	 * 根据栏目提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:55
	 * @param columnId
	 * @param start
	 * @param size
	 * @param permission
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getBasePublicByColumnId(int columnId,short permission,int start,int size) throws Exception;

    /**
     * 根据关键字提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * @param keyWord
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getBaseByKeyWord(long userId,int start,int size,String keyWord) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @param directoryId
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    //public List<KnowledgeBase> getBaseByDirectoryId(long userId,long directoryId,int start,int size) throws Exception;
    /**
     * 根据栏目提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * @param columnId
     * @param keyWord
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getBaseByColumnIdAndKeyWord(String keyWord,int columnId,int start,int size) throws Exception;

	/**
	 * 根据栏目、类型提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:59
	 * @param columnId
	 * @param type
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndType(int columnId,short type,int start,int size) throws Exception;

	/**
	 * 获取知识数量：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:59
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public int getKnowledgeCount(long userId) throws Exception;

	/**
	 * 获取未分组知识数量：
	 * <p>1、知识基础表信息</P>
	 * @date 2016年1月15日 上午10:14:59
	 * @param userId
	 * @param start
	 * @param size
	 * @return
	 * @throws Exception
	 */
	public List<KnowledgeBase> getKnowledgeNoDirectory(long userId,int start,int size) throws Exception;
}