package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseSync;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.common.EModuleType;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/19.
 */
public interface KnowledgeMongoDao
{
    /**
     * 插入
     * @date 2016年1月13日 上午10:54:20
     * @param Knowledge
     * @return
     * @throws Exception
     */
    Knowledge insert(Knowledge Knowledge) throws Exception;

    /**
     * 批量插入
     * @date 2016年1月13日 下午4:24:56
     * @param KnowledgeList
     * @return
     * @throws Exception
     */
    List<Knowledge> insertList(List<Knowledge> KnowledgeList, final int type) throws Exception;

    /**
     * 更新
     * @date 2016年1月13日 上午10:54:29
     * @param Knowledge
     * @return
     * @throws Exception
     */
    Knowledge update(Knowledge Knowledge,int oldType);

    /**
     * 更新
     * @date 2016年1月13日 上午10:54:29
     * @param id
     * @return
     * @throws Exception
     */
    boolean updatePrivated(long id, short type, short privated);

    /**
     * 更新
     * @date 2017年1月13日 上午10:54:29
     * @param knowledgeId
     * @param type
     * @param tagIdList
     * @return
     * @throws Exception
     */
    boolean addTag(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList);

    /**
     * 更新
     * @date 2017年1月13日 上午10:54:29
     * @param knowledgeId
     * @param type
     * @param directoryIdList
     * @return
     * @throws Exception
     */
    boolean addDirectory(final long userId, final long knowledgeId, final int type, final List<Long> directoryIdList);

    /**
     * 先删除后插入
     * @date 2016年1月13日 上午10:54:44
     * @param Knowledge
     * @return
     * @throws Exception
     */
    Knowledge insertAfterDelete(Knowledge Knowledge) throws Exception;

    /**
     * 根据主键及栏目删除数据
     * @date 2016年1月13日 上午10:54:47
     * @param id
     * @param columnId
     * @return
     * @throws Exception
     */
    boolean deleteByIdAndColumnId(long id,int columnId);

    /**
     * 根据主键list以及栏目批量删除数据
     * @date 2016年1月13日 上午10:54:50
     * @param ids
     * @param columnId
     * @return
     * @throws Exception
     */
    boolean deleteByIdsAndColumnType(List<Long> ids,int columnId) throws Exception;

    /**
     * @param ids
     * @param columnType
     * @return
     * @throws Exception
     */
    boolean logicDeleteByIdsType(List<Long> ids,int columnType);

    /**
     * @param ids
     * @param columnType
     * @return
     * @throws Exception
     */
    boolean logicRecoveryByIdsType(List<Long> ids,int columnType);

    /**
     * 根据用户Id以及栏目删除数据
     * @date 2016年1月13日 上午10:54:53
     * @param createUserId
     * @param columnType
     * @return
     * @throws Exception
     */
    boolean deleteByUserIdAndColumnType(long createUserId,int columnType) throws Exception;

    /**
     * @date 2016年1月13日 上午10:54:53
     * @param knowledgeId
     * @param directoryId
     * @return
     * @throws Exception
     */
    boolean deleteKnowledgeDirectory(long knowledgeId,int columnId,long directoryId);

    /**
     * @date 2016年1月13日 上午10:54:53
     * @param userId
     * @param knowledgeId
     * @return
     * @throws Exception
     */
    boolean deleteTag(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList);

    /**
     * @date 2016年1月15日 上午9:41:20
     * @param userId
     * @param knowledgeId
     * @return
     * @throws Exception
     */
    boolean deleteDirectory(final long userId, final long knowledgeId, final int type, final List<Long> tagIdList) throws Exception;

    /**
     * 根据主键以及栏目提取数据
     * @date 2016年1月13日 上午10:54:56
     * @param id
     * @param columnType
     * @return
     * @throws Exception
     */
    Knowledge getByIdAndColumnId(long id,int columnType);

    /**
     * 根据主键list以及栏目提取数据
     * @date 2016年1月13日 上午10:54:58
     * @param ids
     * @param columnType
     * @return
     * @throws Exception
     */
    List<Knowledge> getByIdsAndColumnId(List<Long> ids,int columnType) throws Exception;

    /**
     * @date 2016年1月13日 上午10:54:58
     * @param userId
     * @param start
     * @param size
     * @throws Exception
     */
    List<Knowledge> getNoDirectory(long userId,int start,int size);

    List<Knowledge> selectIndexByParam(short type,int page, int size,List<Long> ids);

    void backupKnowledgeBase(KnowledgeBaseSync knowledge);

    void deleteBackupKnowledgeBase(long knowledgeId);

    List<KnowledgeBaseSync> getBackupKnowledgeBase(int start, int size);

    boolean updateIds(final long userId, final long knowledgeId, final int type, final List<Long> idList, final EModuleType moduleType);
}
