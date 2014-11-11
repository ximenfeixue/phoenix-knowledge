package com.ginkgocap.ywxt.knowledge.service;


import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeStatics;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.tree.Node;

/** 
 * <p>知识首页操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */
public interface KnowledgeHomeService {

    /**
     * 查询分类
     * @param userId 用户id
     * @param column 栏目
     * @return
     */
    public List<Column>getTypeList(Long userId,Long column);

    /**
     * 查询知识
     * @param t类型实例 
     * @param state 0 栏目 1 目录 2 收藏
     * @param columnid 栏目id
     * @param userid 用户id
     * @param page 当前页码
     * @param size 每页大小
     * @return
     */
    public <T> Map<String, Object> selectAllByParam(T t,int state, String columnid, Long userid, int page, int size);
    
    /**
     * 查询目录知识     
     * @param tid 11种类型
     * @param lid 6种来源
     * @param state 状态 0：云知识目录 1：收藏夹目录
     * @param sortid 排序id
     * @param userid 用户id
     * @param keyword 关键词
     * @param page 当前页
     * @param size 每页大小
     * @return map<pre>example：
     * selectAllKnowledgeCategoryByParam("1","1",0,"000000001", 1l,"关键词", 1, 20);</pre>
     * @see {@link KnowledgeHomeServiceImpl#selectAllByParam}
     * @author bianzhiwei
     * @since 2014.11.11
     */
    public Map<String,Object>  selectAllKnowledgeCategoryByParam(String tid,String lid,int state, String sortid, Long userid,String keyword, int page, int size);

    /**
     * 查询排行
     * @param column 栏目id
     * @return
     */
    public List<KnowledgeStatics> getRankList(Long  colunm);
    /**
     * 查询热点排行
     * @param column 栏目id
     * @return
     */
    public List<KnowledgeStatics> getRankHotList(Long  colunm);
    
    /**
     * 根据知识id获取评论等个数
     * @param id
     * @return
     */
    public KnowledgeStatics getPl(long id);

    /**
     * 首页查询
     * @param ty 类型
     * @param page 当前页
     * @param size 每页大小
     * @return
     */
    public <T> List<T> selectIndexByParam(Constants.Type ty, int page, int size);
    
    /**
     * 出现栏目
     * @param cid 栏目id
     * @param userId 用户id
     * @return
     */
    public List<Node> queryColumns(long cid, long userId) ;
    
    /**
     * 查询知识和用户的关系
     * @param id 
     * @param kid 知识id
     * @param userId userid
     * @return
     */
    public int beRelation(long id, int t,long kid, long userId) ;
}
