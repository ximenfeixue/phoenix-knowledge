package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseSync;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.InterfaceResult;

import java.util.List;

/**
 * Created by gintong on 2016/7/19.
 */
public interface KnowledgeService
{
    /**
     * 插入，承担以下任务：
     * <p>1、知识详细表插入</P>
     * <p>2、知识基础表插入</P>
     * <p>3、知识来源表插入</P>
     * @date 2016年1月15日 上午9:41:03
     * @param DataCollect
     * @return
     * @throws Exception
     */
    public InterfaceResult<Long> insert(DataCollect DataCollect);


    /**
     * 插入，承担以下任务：
     * <p>1、知识详细表插入</P>
     * @return
     * @throws Exception
     */
    public Knowledge insert(Knowledge detail);

    /**
     * 插入，承担以下任务：
     * <p>1、知识详细表插入</P>
     * @return
     * @throws Exception
     */
    //public InterfaceResult insertKnowledgeList(List<Knowledge> knowledgeList,final int type);

    /**
     * 更新，承担以下任务：
     * <p>1、知识详细表更新</P>
     * @param detail
     * @return
     * @throws Exception
     */
    public Knowledge updateKnowledgeDetail(Knowledge detail);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param DataCollect
     * @return
     * @throws Exception
     */
    public InterfaceResult<Knowledge> update(DataCollect DataCollect);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param perm
     * @return
     * @throws Exception
     */
    public boolean updatePermission(Permission perm);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean addTag(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean addDirectory(final long userId, final long knowledgeId, final int type, final List<Long> directoryIdList);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean updateTag(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList);

    /**
     * @date 2016年1月15日 上午9:41:16
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean updateDirectory(final long userId, final long knowledgeId, final int type, final List<Long> directoryIdList);
    /**
     * @date 2016年1月15日 上午9:41:20
     * @param knowledgeId
     * @param columnId
     * @return
     * @throws Exception
     */
    public InterfaceResult deleteByKnowledgeId(long knowledgeId, int columnId) throws Exception;

    /**
     * @date 2016年1月15日 上午9:41:20
     * @param userId
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean deleteTag(final long userId, final long knowledgeId, final int type, final List<Long> idList) throws Exception;

    /**
     * @date 2016年1月15日 上午9:41:20
     * @param userId
     * @param userId
     * @return
     * @throws Exception
     */
    public boolean deleteDirectory(final long userId, final long knowledgeId, final int type, final List<Long> idList) throws Exception;

    /**
     * <p>5、动态推送批量删除</P>
     * @date 2016年1月15日 上午9:41:23
     * @param knowledgeIds
     * @param columnId
     * @return
     * @throws Exception
     */
    public InterfaceResult batchDeleteByKnowledgeIds(List<Long> knowledgeIds, int columnId) throws Exception;

    /**
     * @param knowledgeIds
     * @param type
     * @return
     * @throws Exception
     * */
    public InterfaceResult logicDelete(List<Long> knowledgeIds, final int type);

    /**
     * @param knowledgeIds
     * @param type
     * @return
     * @throws Exception
     * */
    public InterfaceResult logicRecovery(List<Long> knowledgeIds, final int type);

    /**
     * @param knowledgeId
     * @param columnType 由于知识详细表信息为分库存储，则columnId为必须字段
     * @return
     * @throws Exception
     */
    public Knowledge getDetailById(long knowledgeId,int columnType) throws Exception;

    /**
     * 更新，承担以下任务：
     * <p>1、获取知识详细</P>
     * <p>2、获取知识基础</P>
     * @return
     * @throws Exception
     */
    public DataCollect getKnowledge(long knowledgeId,int columnType) throws Exception;

    /**
     * 提取简要信息（一般用在知识简要信息界面的数据提取中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * <p>2、知识来源表信息</P>
     * @date 2016年1月15日 上午9:41:39
     * @param knowledgeId
     * @return
     * @throws Exception
     */
    public KnowledgeBase getBaseById(long knowledgeId) throws Exception;

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
    public List<KnowledgeBase> getAll(int start,int size) throws Exception;

    /**
     * 提取所有数据（一般用在首页数据展示中）
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 下午5:43:26
     * @return
     * @throws Exception
     */
    public long getAllPublicCount(short permission) throws Exception;

    /**
     * 提取所有数据（一般用在首页数据展示中）
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 下午5:43:26
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getAllPublic(int start,int size,short permission) throws Exception;

    /**
     * 获取知识数量：
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 上午10:14:59
     * @param userId
     * @return
     * @throws Exception
     */
    public int countByUserId(long userId) throws Exception;

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
    public List<KnowledgeBase> getByUserId(long userId,int start,int size) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 上午9:41:33
     * @param userName
     * @return
     * @throws Exception
     */
    public int countByUserName(String userName) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @param userName
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getByUserName(String userName,int start,int size) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 上午9:41:33
     * @param title
     * @return
     * @throws Exception
     */
    public int countByTitle(String title) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @param title
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getByTitle(String title,int start,int size) throws Exception;

    /**
     * <p>1、知识基础表信息</P>
     * @param userId
     * @return
     * @throws Exception
     */
    public long countAllCreateAndCollected(long userId, String keyWord);

    /**
     * @param userId
     * @param keyWord;
     * @return
     * @throws Exception
     */
    public int countCreated(long userId, String keyWord);

    /**
     * @param userId
     * @param keyWord
     * @return
     * @throws Exception
     */
    public long countCollected(long userId, String keyWord);

    /**
     * <p>1、知识基础表信息</P>
     * @param userId
     * @param keyWord
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getAllCreateAndCollected(long userId, long total, String keyWord, int start, int size);

    /**
     * <p>1、知识基础表信息</P>
     * @param userId
     * @param keyWord
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getCreatedKnowledge(long userId, int start, int size, String keyWord);

    /**
     * <p>1、知识基础表信息</P>
     * @param userId
     * @param keyWord
     * @param index
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getCollectedKnowledgeByIndex(long userId, int index, int size, String keyWord);

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
    public List<KnowledgeBase> getByUserIdAndColumnId(long userId,int columnId,int start,int size) throws Exception;

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
    public List<KnowledgeBase> getByUserIdAndType(long userId,short type,int start,int size) throws Exception;

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
     * 根据关键字提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * @param userId
     * @param keyWord
     * @return
     * @throws Exception
     */
    public int countByUserIdKeyWord(long userId,String keyWord) throws Exception;

    /**
     * 根据关键字提取简要信息列表（一般用在游客、或者首页等不区分用户的界面中），具体提取以下信息：
     * <p>1、知识基础表信息</P>
     * @param keyWord
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBase> getByUserIdKeyWord(long userId,String keyWord,int start,int size) throws Exception;

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
    public InterfaceResult<List<DataCollect>> getBaseByColumnIdAndType(int columnId,short type,int start,int size) throws Exception;

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

    /**
     * 获取未分组知识数量：
     * <p>1、知识基础表信息</P>
     * @date 2016年1月15日 上午10:14:59
     * @param start
     * @param size
     * @return
     * @throws Exception
     */
    public List<KnowledgeBaseSync> getBackupKnowledgeBase(int start, int size) throws Exception;

    public boolean syncKnowledgeBase(KnowledgeBaseSync knowledgeSync) throws Exception;

    public void addTopKnowledge(List<Long> ids, short type);

    public void deleteTopKnowledge(List<Long> ids, short type);

    public List<KnowledgeBase> getTopKnowledgeByPage(short type, int page, int size);

    public List getCreatedKnowledgeCountGroupByDay(long userId, long startDate, long endDate);
}
