package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeStatics;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by gintong on 2016/7/13.
 */
public interface KnowledgeHomeService
{
    /**
     * 查询分类
     * @param userId 用户id
     * @param column 栏目id
     * @return
     */
    //public List<Column>getTypeList(Long userId,Long columnId);

    /**
     * 查询知识
     * @param columnId 栏目id
     * @param userId 用户id
     * @param start 起始实记录
     * @param size 每页大小
     * @return
     */
    public List<Knowledge> getAllByParam(short type,String columnPath,int columnId, long userId, int start, int size);

    public long getKnowledgeCountByUserIdAndColumnID(String[] columnID, long userId, short type);

    public List<Knowledge> getKnowledge(String[] columnID, long user_id, short type, int start, int size);

    /**
     * 查询目录知识
     * @param tid 11种类型
     * @param lid 6种来源
     * @param state 状态 0：云知识目录 1：收藏夹目录
     * @param sortId 排序id
     * @param userId 用户id
     * @param keyword 关键词
     * @param page 当前页
     * @param size 每页大小
     * @return map<pre>example：
     * selectAllKnowledgeCategoryByParam("1","1",0,"000000001", 1l,"关键词", 1, 20);</pre>
     * @author bianzhiwei
     * @since 2014.11.11
     */
    public Map<String,Object>  selectAllKnowledgeCategoryByParam(String tid,String lid,int state, String sortId, long userId,String keyword, int page, int size);

    /**
     * 查询排行
     * @param columnId 栏目id
     * @return
     */
    public List<KnowledgeStatics> getRankList(int columnId);
    /**
     * 查询热点排行
     * @param columnId 栏目id
     * @return
     */
    public List<KnowledgeStatics> getRankHotList(int columnId);

    /**
     * 根据知识id获取评论等个数
     * @param id
     * @return
     */
    public KnowledgeStatics getPl(long id);

    /**
     * 首页查询
     * @param columnId 类型
     * @param page 当前页
     * @param size 每页大小
     * @return
     */
    public <T> List<T> selectIndexByParam(int columnId, int page, int size);

    /**
     * 出现栏目
     * @param cid 栏目id
     * @param userId 用户id
     * @return
     */
    //public List<Node> queryColumns(long cid, long userId) ;

    /**
     * 查询知识和用户的关系
     * @param id
     * @param kid 知识id
     * @param userId userid
     * @return
     */
    public int beRelation(long id, int t,long kid, Long userId) ;

    /**
     * 导入导出查询目录知识
     * @param userId 用户id
     * @param groupId 目录id
     * @param page 当前页
     * @param size 页面大小
     * @return map
     * @author bianzhiwei
     * @since 2014-11-20
     */
    public Map<String, Object> selectKnowledgeCategoryForImport(long userId,List<Long> groupId, int page, int size);

    /**
     *  查询目录知识记录数
     * @param tid 11种类型
     * @param lId 6种来源
     * @param state 状态 0：云知识目录 1：收藏夹目录
     * @param sortId 排序id
     * @param userId 用户id
     * @param keyword 关键词
     */
    public int countKnowledgeIds(String tid,String lId, int state, String sortId, long userId, String keyword);

    /**
     * 登录用户查询全平台的知识
     * @param columnId 类型
     * @param columnId 栏目id
     * @param userId 用户id
     * @param page
     * @param size
     * @return
     */
    public List<KnowledgeBase> selectPlatform(int columnId, long userId, int page, int size);

    /**
     * 根据定制的栏目读取聚合信息
     * @param userId
     * @param columnIds
     * @param page
     * @param size
     * @return
     */
    public Map<String, Object> getAggregationRead(long userId,String[] columnIds, int page, int size);

    /**
     * 查询发现推荐中的人脉知识
     * @param userId 用户id
     * @param page 当前页
     * @param size 每页大小
     * @author caihe
     */
    public Map<String,Object>  selectRecommendedKnowledge(long userId,int page, int size);

    /**
     * 知识添加星标签
     *
     * @param userId
     * @return
     */
    Map<String, Object> addUserStar(List<Long> knowledgeIds, long userId,int star);
}
