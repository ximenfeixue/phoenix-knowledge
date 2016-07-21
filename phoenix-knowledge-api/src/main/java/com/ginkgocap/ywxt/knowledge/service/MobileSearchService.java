package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;

/**
 * @Description: 移动端官方搜索接口
 * @Author: zhangzhen
 * @CreateDate: 2014-10-20
 * @Version: [v1.0]
 */

/**
 * Created by gintong on 2016/7/21.
 */
public interface MobileSearchService
{
    /** 关键词搜索 */
    public Map<String, Object> searchByKeywords(Long userid, String keywords,
                                                String scope, String pno, String pszie);

    /** 根据标签与关键字搜索 */
    public Map<String, Object> selectKnowledgeByTagsAndkeywords(Long userid,
                                                                String keywords, String scope, String tag, int page, int size);

    /** 根据我的收藏与关键字搜索 */
    public Map<String, Object> selectKnowledgeByMyCollectionAndkeywords(
            Long userid, String keywords, String scope, int page, int size);

    /**
     * 查询我的所有好友的指定栏目下，所有知识 根据栏目和来源获取知识列表 ( 3-好友 )
     * */
    public Map<String, Object> selectMyFriendknowledgeByColumnId(long columnId,
                                                                 long userId, String scope, int page, int size);

    /** 整合查询 zz join*/
    public JSONObject searchKnowledge(long userid, String keyword,
                                      String tag, int scope, int pno, int psize, String qf, int type,
                                      String sort) throws Exception;

    /** 查询全平台的知识 */
    public Map<String, Object> selectPermissionByAllPermission(long userId,long columnId,int start,int size);

    /** 查询好友的知识 */
    public Map<String, Object> selectPermissionByMyFriends(long userId,long columnId,int start,int size);

    /** 根据栏目ID和用户ID获取知识 */
    public List<Knowledge> getKnowledge(String[] columnID,long user_id,String type,int offset,int limit);

    /** 查询全平台与好友的知识 */
    public Map<Long,Integer> selectKnowledgeByPermission(long userId,long columnId,int start,int size);

    /** 查询全平台与好友的知识个数 */
    public int selectKnowledgeCountByPermission(long userId,long columnId);

    /**
     * 获取 金桐和自己  混合
     */
    List<Knowledge> getMixKnowledge(String columnID,long user_id,String type,int offset,int limit);

    long getMixKnowledgeCount(String columnID,long user_id,String type);


    /**
     * 从MySQL中查询出的knowledge_id和type  填充相应的knowledge 形成List
     */
    List<Knowledge>  fileKnowledge(Map<Long,Integer> map);

    long getKnowledgeCountByUserIdAndColumnID(String[] columnID,long user_id,String type);

    /** 查询好友的知识 */
    public <T> Map<String,Object> selectAllByParam(int classType, int state, String columnid, Long userid, int page, int size);

    /** 查询首页知识 */
    public <T> Map<String, Object> selectAll(T t, int state, String columnid, Long userid, int page, int size);

    /** 查询我的标签被知识所使用个数 */
    public int getCountForMyKnowledgeTag(long userId,String keyword);

    /**
     * 2014-12-24
     * 查询好友的知识id
     * **/
    public List<Map<String,Object>> getKnowledgeIdByPermission(Long userId,int columnType);



    List<Knowledge> fetchFriendKw(long[] kid,int type,int offset,int limit);

    long fetchFriendKwCount(long[] kid,int type) ;

    /** 查询我创建的和我收藏的知识列表 */
    public Map<String, Object> selectAllKnowledge(Long userid,String keywords,int page, int size);

    public int selectCountKnowledge(long userid,String keywords);

    public int countPush(Long userid,String tag,String keywords);
}
