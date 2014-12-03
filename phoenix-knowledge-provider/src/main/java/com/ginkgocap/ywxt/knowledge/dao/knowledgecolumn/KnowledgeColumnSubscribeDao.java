package com.ginkgocap.ywxt.knowledge.dao.knowledgecolumn;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.form.KnowledgeSimpleMerge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumnSubscribe;

/** 
 * <p>订阅栏目dao</p>  
 * @author   guangyuan   
 * @since 1.2.1-SNAPSHOT
 * @createdtime 2014-09-09
 */
public interface KnowledgeColumnSubscribeDao {

    /**
     * 增加用户订阅栏目的记录
     * @param kcs 要增加的记录
     * @return 增加的记录
     */
    public KnowledgeColumnSubscribe insert(KnowledgeColumnSubscribe kcs);
    /**
     * 根据用户id和栏目id查询用户的订阅条数，可能存在重复
     * @param userId 用户id
     * @param columnId 栏目id
     */
    public Long countSubNumber(long userId,long columnId);
    /**
     * 修改用户订阅栏目的记录
     * @param kcs
     * @return 修改的记录
     */
    public int update(KnowledgeColumnSubscribe kcs);
    /**
     * 根据用户id和栏目id删除用户订阅栏目的记录
     * @param userId 用户id
     * @param columnId 栏目id
     * @return none
     */
    public void deleteByUIdAndKCId(long userId,long columnId);
    /**
     * 根据id删除用户订阅栏目的记录
     * @param id 要删除的的订阅id
     * @return none
     */
    public void deleteByPK(long id);
    /**
     * 查询用户所订阅栏目的记录列表
     * @param userId 用户id
     * @return 订阅记录列表
     */
    public List<KnowledgeColumnSubscribe> selectByUserId(long userId);
    /**
     * 查询用户所订阅的栏目列表
     * @param userId 用户id
     * @return 栏目列表
     */
    public List<KnowledgeColumn> selectKCListByUserId(long userId);
    /**
     * 查询栏目的所有订阅人的id
     * @param columnId 栏目id
     * @return 用户id列表
     */
    public List<Long> selectUserIdListByKc(long columnId);
    /**
     * 查询栏目的所有订阅人的记录列表
     * @param columnId 栏目id
     * @return 订阅记录列表
     */
    public List<KnowledgeColumnSubscribe> selectByKCId(long columnId);
    
    /**
     * 统计某栏目的被订阅总数
     * @param columnId 栏目id
     * @return 统计数字
     */
    public int countByKC(long columnId);
    /**
     * 统计所有栏目的被订阅总数
     * @param userId 用户id
     * @return 统计数字
     */
    public List<Integer> countAllByKC(long userId);
    /**
     * 统计某人订阅栏目的总数
     * @param columnId 栏目id
     * @return 统计数字
     */
    public int countByUserId(long userId);
    /**
     * 查询某人所订阅的知识
     * @param userId 用户id
     * @return 知识列表
     */
    public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId);
    /**
     * 查询某人所订阅的知识
     * @param userId 用户id
     * @param type 知识类型
     * @return 知识列表
     */
    public List<KnowledgeSimpleMerge> selectSubKnowByUserId(long userId,int type);
    /**
     * 根据订阅栏目查询知识
     * @param list 栏目列表
     * @return 知识列表
     */
    public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<KnowledgeColumn> list);
    /**
     * 根据订阅栏目查询知识
     * @param list 栏目列表
     * @param type 知识类型
     * @return 知识列表
     */
    public List<KnowledgeSimpleMerge> selectSubKnowByKCList(List<KnowledgeColumn> list,int type);
}