package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

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
    public Knowledge insert(Knowledge Knowledge) throws Exception;

    /**
     * 批量插入
     * @date 2016年1月13日 下午4:24:56
     * @param KnowledgeList
     * @return
     * @throws Exception
     */
    public List<Knowledge> insertList(List<Knowledge> KnowledgeList) throws Exception;

    /**
     * 更新
     * @date 2016年1月13日 上午10:54:29
     * @param Knowledge
     * @return
     * @throws Exception
     */
    public Knowledge update(Knowledge Knowledge);

    /**
     * 先删除后插入
     * @date 2016年1月13日 上午10:54:44
     * @param Knowledge
     * @return
     * @throws Exception
     */
    public Knowledge insertAfterDelete(Knowledge Knowledge) throws Exception;

    /**
     * 根据主键及栏目删除数据
     * @date 2016年1月13日 上午10:54:47
     * @param id
     * @param columnId
     * @return
     * @throws Exception
     */
    public int deleteByIdAndColumnId(long id,int columnId);

    /**
     * 根据主键list以及栏目批量删除数据
     * @date 2016年1月13日 上午10:54:50
     * @param ids
     * @param columnId
     * @return
     * @throws Exception
     */
    public int deleteByIdsAndColumnId(List<Long> ids,int columnId) throws Exception;

    /**
     * 根据用户Id以及栏目删除数据
     * @date 2016年1月13日 上午10:54:53
     * @param createUserId
     * @param columnId
     * @return
     * @throws Exception
     */
    public int deleteByCreateUserIdAndColumnId(long createUserId,int columnId) throws Exception;

    /**
     *
     * @date 2016年1月13日 上午10:54:53
     * @param knowledgeId
     * @param directoryId
     * @return
     * @throws Exception
     */
    public boolean deleteKnowledgeDirectory(long knowledgeId,int columnId,long directoryId);

    /**
     * 根据主键以及栏目提取数据
     * @date 2016年1月13日 上午10:54:56
     * @param id
     * @param columnId
     * @return
     * @throws Exception
     */
    public Knowledge getByIdAndColumnId(long id,int columnId);

    /**
     * 根据主键list以及栏目提取数据
     * @date 2016年1月13日 上午10:54:58
     * @param ids
     * @param columnId
     * @return
     * @throws Exception
     */
    public List<Knowledge> getByIdsAndColumnId(List<Long> ids,int columnId) throws Exception;

    /**
     * @date 2016年1月13日 上午10:54:58
     * @param userId
     * @param start
     * @param size
     * @throws Exception
     */
    public List<Knowledge> getNoDirectory(long userId,int start,int size);

    /**
     * @date 2016年1月13日 上午10:54:58
     * @param userId
     * @param start
     * @param size
     * @throws Exception
     */
    public List<Knowledge> getKnowledgeByUserIdAndColumnIds(int[] columnIds,long userId, short type,int start,int size);

    /**
     * @date 2016年1月13日 上午10:54:58
     * @param userId
     * @throws Exception
     */
    public Map<String, Object> getAllByParam(short type,String columnPath,int columnId, Long userId, int page, int size);
}
