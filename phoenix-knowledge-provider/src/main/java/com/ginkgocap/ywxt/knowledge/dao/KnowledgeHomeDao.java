package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

public interface KnowledgeHomeDao {
    /**       
     * getRankList 获取排行
     * @param type 0：热门排行 1：热门标签排行  2：评论排行 3.最新排行 .. 
     * @param column 默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规
     * @return 知识列表
     */
    public List<Knowledge> getRankList(int type,int column);
   
    /**      
     * getHomeList 获取首页列表      
     * @param column 默认0：其他,1：资讯，2：投融工具，3：行业，4：经典案例，5：图书报告，6：资产管理，7：宏观，8：观点，9：判例，10，法律法规
     * @param userId 用户id
     * @param pageNo 页码
     * @param pageSize 页尺寸
     * @param sortBy 0:时间倒序 1计算后排序
     * @return 知识列表和分页信息
     */
    public List<Knowledge> getHomeList(int column,long userId,int pageStart,int pageSize,int sortBy);
    
    /**
     * countHomeList 获取总数 
     * @param column 栏目id
     * @param userId 用户id
     * @return 总数
     */
    public int countHomeList(int column,long userId);
    
    /**
     * 获取分类列表
     * @param userId 用户id
     * @param column 分类id
     * @return 分类列表
     */
    public List<KnowledgeColumn> getTypeList(long userId,int column);

}
