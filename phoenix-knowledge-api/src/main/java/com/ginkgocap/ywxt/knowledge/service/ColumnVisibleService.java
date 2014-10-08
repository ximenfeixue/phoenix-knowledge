package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.ColumnVisible;

/** 
 * <p>知识栏目可见性操作接口</p>  
 * <p>于2014-10-09 由 bianzhiwei 创建 </p>   
 * @since <p>1.2.1-SNAPSHOT</p> 
 */
public interface ColumnVisibleService {

    public ColumnVisible queryListByCidAndUserId(long userid, long cid);
    
    public List<ColumnVisible> queryListByPidAndUserId(long userid, long pid);

    public void saveCids(long userid, String cids, long pcid);

    public void del(long id);

    public void delByPcid(long pcid, long userid);

}
