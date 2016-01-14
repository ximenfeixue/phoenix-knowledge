package com.ginkgocap.ywxt.knowledge.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.InterfaceResult;

/**
 * @Title: 知识管理服务
 * @Description: 知识管理服务
 * @author 周仕奇
 * @date 2016年1月11日 下午2:31:19
 * @version V1.0.0
 */
public interface IKnowledgeService {

	public InterfaceResult<JSONObject> insert(JSONObject knowledgeJson, User user) throws Exception;
	
	public InterfaceResult<JSONObject> update(JSONObject knowledgeJson, User user) throws Exception;
	
	public InterfaceResult<JSONObject> deleteByKnowledgeId(long knowledgeId, long columnId, User user) throws Exception;
	
	public InterfaceResult<JSONObject> deleteByKnowledgeIds(List<Long> knowledgeIds, long columnId, User user) throws Exception;
	
	public InterfaceResult<JSONObject> getDetailById(long knowledgeId,long columnId,User user) throws Exception;
	
	public InterfaceResult<JSONObject> getBaseById(long knowledgeId,User user) throws Exception;
	
	public InterfaceResult<List<JSONObject>> getBaseByIds(List<Long> knowledgeIds,User user) throws Exception;
	
	public InterfaceResult<JSONObject> getBaseByCreateUserId(User user,int start,int size) throws Exception;
	
	public InterfaceResult<JSONObject> getBaseByCreateUserIdAndColumnId(User user,long columnId,int start,int size) throws Exception;
	
	public InterfaceResult<JSONObject> getBaseByCreateUserIdAndType(User user,String type,int start,int size) throws Exception;
	
	public InterfaceResult<JSONObject> getBaseByCreateUserIdAndColumnIdAndType(User user,long columnId,String type,int start,int size) throws Exception;
}