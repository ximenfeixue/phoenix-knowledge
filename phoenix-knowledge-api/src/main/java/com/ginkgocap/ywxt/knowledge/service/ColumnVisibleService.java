package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;
import com.ginkgocap.ywxt.user.model.User;

/** 
 * <p>知识栏目可见性操作接口</p>  
 * <p>于2014-10-09 由 bianzhiwei 创建 </p>   
 * @since <p>1.2.1-SNAPSHOT</p> 
 */
public interface ColumnVisibleService {

    public void init(long userid, long gtnid);

    public ColumnVisible queryListByCidAndUserId(long userid, long cid);

    public List<ColumnVisible> queryListByPidAndUserId(long userid, long pid);

    public long countListByPidAndUserId(long userid, Long pid);

    public void updateCids(long userid, String cids,long pcid);

    public void saveCid(long userid, long cid);
    
    public void saveOrUpdate(Column c);
    
    public void delByUserIdAndColumnId(long userid, long cid);

    public void del(long id);

    /**
     * 查询首页栏目
     * @param user 登陆用户
     * @return
     * @author haiyan
     */
	public Map<String,Object> queryHomeColumn(User user);

	public List<ColumnVisible> queryListByPidAndUserIdAndState(long userid, long cid, short state);

    long countListByCidAndUserId(long userid, Long pid);

    void initOne(long userid, long gid, Long cid);

    void initDiff(long userId); 

}
