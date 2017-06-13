package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeShare;

/**
 * Created by gintong on 2016/8/6.
 */
public interface KnowledgeShareDao {
    /**
     * 添加分享信息
     * @param knowledgeShare
     * @return KnowledgeShare
     */
    KnowledgeShare save(KnowledgeShare knowledgeShare);
    /**
     * 查询我分享的
     * @param userId 当前用户
     * @param start 偏移量
     * @param end 量大小
     * @return List<KnowledgeShare>
     */
    List<KnowledgeShare> findMyShare(long userId, int start, int end, String title);
    /**
     * 查询我分享的行数
     * @param userId 当前用户
     * @return int
     */
    int findMyShareCount(long userId, String title);
    /**
     * 查询分享给我的
     * @param userId 当前用户
     * @param start 偏移量
     * @param end 量大小
     * @return List<KnowledgeShare>
     */
    List<KnowledgeShare> findShareMe(long userId, int start, int end, String title);
    /**
     * 查询分享给我的行数
     * @param userId 当前用户
     * @return int
     */
    int findShareMeCount(long userId, String title);
    /**
     * 删除分享
     * @param knowledgeId
     */
    void deleteShareInfoByKnowledgeId(long knowledgeId);
    /**
     * 查询我分享的一条数据
     * @param userId 当前用户
     * @param knowledgeId 知识id
     * @return KnowledgeShare
     */
    KnowledgeShare findMyShareOne(long userId , long knowledgeId);
    /**
     * 查询分享给我的一条数据
     * @param userId 接收人
     * @param knowledgeId 知识id
     * @return KnowledgeShare
     */
    KnowledgeShare findShareMeOne(long userId , long knowledgeId);
    /**
     * 更新我分享的标题
     * @param title
     */
    void updateMyShareTitle(long userId , long knowledgeId, String title);
    /**
     * 更新分享给我的标题
     * @param userId
     * @param knowledgeId
     * @param title
     */
    void updateShareMeTitle(long userId , long knowledgeId, String title);
    /**
     * 删除我分享的
     * @param userId 当前用户Id
     * @param knowledgeId 知识id
     */
    void deleteMyShare(long userId , long knowledgeId);
    /**
     * 删除分享给我的
     * @param userId 接收人id
     * @param knowledgeId 知识id
     */
    void deleteShareMe(long userId , long knowledgeId);
}
