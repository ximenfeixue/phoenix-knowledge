package com.ginkgocap.ywxt.knowledge.dao;

import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.user.model.User;

/**
 * @Title: 用户定制栏目表
 * @Description: 存储用户定制栏目，用户定制栏目栏目为用户选择哪些栏目显示以及栏目排序
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IColumnCustomDao {

	/**
	 * 根据用户id和栏目id查询
	 * @param userid
	 * @param cid
	 * @return
	 */
    public List<ColumnCustom> queryListByCidAndUserId(long userid, long cid) throws Exception;
    
    /**
     * 查询用户所有栏目
     * @param userid
     * @return
     * @throws Exception
     */
    public List<ColumnCustom> queryListByUserId(long userid) throws Exception;

    /**
     * 根据用户id和上级栏目id查询
     * @param userid
     * @param pid
     * @return
     */
    public List<ColumnCustom> queryListByPidAndUserId(long userid, long pid) throws Exception;


    /**
     * 根据栏目主键删除
     * @param id
     */
    public Boolean del(long id) throws Exception;
    
	public Map<String,Object> queryHomeColumn(User user);

	/**
	 * 根据用户id和上级栏目id及可见状态查询
	 * @param userid
	 * @param cid
	 * @param state
	 * @return
	 */
	public List<ColumnCustom> queryListByPidAndUserIdAndState(long userid, long cid, short state);
	
	public ColumnCustom queryMaxCCByUid(long userid) throws Exception;
	
	ColumnCustom update(ColumnCustom columnCustom) throws Exception;
	
	ColumnCustom insert(ColumnCustom columnCustom) throws Exception;
	
	ColumnCustom queryById(Long id) throws Exception;
	
}