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
    public <T> List<T> selectAllByParam(T t,int state, String columnid, Long userid, int page, int size);
    
    /**
     * 查询目录
     * @param state 0默认 1收藏
     * @param sortid 排序id
     * @param userid 用户id
     * @param page页
     * @param size 页大小
     * @return
     */
    public  List<KnowledgeCategory> selectAllKnowledgeCategoryByParam(int state, String sortid, Long userid, int page, int size);

    /**
     * 查询排行
     * @param column 栏目id
     * @return
     */
    public List<KnowledgeStatics> getRankList(Long  colunm);
    
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
    
    public List<Node> queryColumns(long cid, long userId) ;
}
