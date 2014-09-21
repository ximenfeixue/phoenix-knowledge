package com.ginkgocap.ywxt.knowledge.service;


import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;

/** 
 * <p>知识首页操作接口</p>  
 * <p>于2014-8-19 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>1.2.1-SNAPSHO</p> 
 */
public interface KnowledgeHomeService {
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
    public Map<String ,Object> getHomeList(int column,long userId,int pageNo,int pageSize,int sortBy);
    
    /**
     * 获取分类列表
     * @param userId 用户id
     * @param column 分类id
     * @return 分类列表
     */
    public List<KnowledgeColumn>getTypeList(long userId,int column);

}
