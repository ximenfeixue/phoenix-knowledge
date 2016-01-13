package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeBaseDao;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.IKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.IKnowledgeCommonService;
import com.ginkgocap.ywxt.knowledge.utils.JsonKeyConstant;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;

@Service("knowledgeService")
public class KnowledgeService implements IKnowledgeService {
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeService.class);
	
	@Autowired
	private IKnowledgeBaseDao knowledgeBaseDao;
	@Autowired
	private IKnowledgeMongoDao knowledgeMongoDao;
	@Autowired
	private IKnowledgeReferenceDao knowledgeReferenceDao;
	@Autowired
	private IKnowledgeCommonService knowledgeCommonService;
	
	public InterfaceResult<JSONObject> insert(JSONObject knowledgeJson, User user) throws Exception {
		
		KnowledgeMongo knowledgeMongo = (KnowledgeMongo) getBeans(knowledgeJson,JsonKeyConstant.JSONKEY_KNOWLEDGE,KnowledgeMongo.class);
		KnowledgeReference knowledgeReference = (KnowledgeReference) getBeans(knowledgeJson,JsonKeyConstant.JSONKEY_KNOWLEDGE_REFERENCE,KnowledgeReference.class);
		
		long knowledgeId = this.knowledgeCommonService.getKnowledgeSeqenceId();
		
		long columnId = knowledgeMongo.getColumnId();
		
		knowledgeMongo.setId(knowledgeId);
		
		knowledgeMongo.createContendDesc();
		
		KnowledgeMongo afterSaveKnowledgeMongo = this.knowledgeMongoDao.insert(knowledgeMongo, user);
		
		try {
			this.knowledgeBaseDao.insert(afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		KnowledgeReference afterSaveKnowledgeReference = null;
		try {
			this.knowledgeReferenceDao.insert(knowledgeReference, knowledgeId, user);
		} catch (Exception e) {
			this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
			this.knowledgeBaseDao.deleteById(knowledgeId);
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturnJson(afterSaveKnowledgeMongo,afterSaveKnowledgeReference));
	}
	
	public InterfaceResult<JSONObject> update(JSONObject knowledgeJson, User user) throws Exception {
		
		KnowledgeMongo knowledgeMongo = (KnowledgeMongo) getBeans(knowledgeJson,JsonKeyConstant.JSONKEY_KNOWLEDGE,KnowledgeMongo.class);
		KnowledgeReference knowledgeReference = (KnowledgeReference) getBeans(knowledgeJson,JsonKeyConstant.JSONKEY_KNOWLEDGE_REFERENCE,KnowledgeReference.class);
		
		long knowledgeId = knowledgeMongo.getId();
		
		long columnId = knowledgeMongo.getColumnId();
		
		knowledgeMongo.createContendDesc();
		
		KnowledgeMongo oldKnowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		KnowledgeMongo afterSaveKnowledgeMongo = this.knowledgeMongoDao.update(knowledgeMongo, user);
		
		KnowledgeBase oldKnowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		try {
			this.knowledgeBaseDao.update(afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeMongo, knowledgeId, user);
			logger.error("知识基础表更新失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}

		KnowledgeReference afterSaveKnowledgeReference = null;
		try {
			this.knowledgeReferenceDao.insert(knowledgeReference, knowledgeId, user);
		} catch (Exception e) {
			this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeMongo, knowledgeId, user);
			this.knowledgeBaseDao.insertAfterDelete(oldKnowledgeBase, user);
			logger.error("知识来源表更新失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturnJson(afterSaveKnowledgeMongo,afterSaveKnowledgeReference));
	}
	
	public InterfaceResult<JSONObject> deleteByKnowledgeId(long knowledgeId, long columnId, User user) throws Exception {
		
		KnowledgeMongo oldKnowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		
		KnowledgeBase oldKnowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		try {
			this.knowledgeBaseDao.deleteById(knowledgeId);
		} catch (Exception e) {
			this.knowledgeMongoDao.insert(oldKnowledgeMongo, user);
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		try {
			this.knowledgeReferenceDao.deleteById(knowledgeId);
		} catch (Exception e) {
			this.knowledgeMongoDao.insert(oldKnowledgeMongo, user);
			this.knowledgeBaseDao.insert(oldKnowledgeBase, user);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
	}
	
	public InterfaceResult<JSONObject> deleteByKnowledgeIds(List<Long> knowledgeIds, long columnId, User user) throws Exception {
		
		List<KnowledgeMongo> oldKnowledgeMongoList = this.knowledgeMongoDao.getByIdsAndColumnId(knowledgeIds, columnId);
		
		this.knowledgeMongoDao.deleteByIdsAndColumnId(knowledgeIds, columnId);
		
		List<KnowledgeBase> oldKnowledgeBaseList = this.knowledgeBaseDao.getByIds(knowledgeIds);
		try {
			this.knowledgeBaseDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			this.knowledgeMongoDao.insertList(oldKnowledgeMongoList, user);
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		try {
			this.knowledgeReferenceDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			this.knowledgeMongoDao.insertList(oldKnowledgeMongoList, user);
			this.knowledgeBaseDao.insertList(oldKnowledgeBaseList, user);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
	}
	
	public InterfaceResult<JSONObject> getDetailById(long knowledgeId,long columnId,User user) throws Exception {
		
		KnowledgeMongo knowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturnJson(knowledgeMongo,knowledgeReference));
		
	}
	
	public InterfaceResult<JSONObject> getBaseById(long knowledgeId,User user) throws Exception {
		
		KnowledgeBase knowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturnJson(knowledgeBase,knowledgeReference));
	}
	
	public InterfaceResult<List<JSONObject>> getBaseByIds(List<Long> knowledgeIds,User user) throws Exception {
		
		List<KnowledgeBase> knowledgeBaseList = this.knowledgeBaseDao.getByIds(knowledgeIds);
		
		List<KnowledgeReference> knowledgeReferenceList = this.knowledgeReferenceDao.getByIds(knowledgeIds);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(KnowledgeReference.putReferenceList2BaseList(knowledgeBaseList,knowledgeReferenceList));
	}
	
	private JSONObject getReturnJson(KnowledgeBase knowledgeBase, KnowledgeReference knowledgeReference) {

		JSONObject returnJson = new JSONObject();
		
		returnJson.put(JsonKeyConstant.JSONKEY_KNOWLEDGE, knowledgeBase);
		
		returnJson.put(JsonKeyConstant.JSONKEY_KNOWLEDGE_REFERENCE, knowledgeReference);
		
		return returnJson;
	}
	
	private Object getBeans(JSONObject json,String key,Class<?> beanClass) {

		JSONObject jsonObject = json.getJSONObject(key);
		
		return JSONObject.toBean(jsonObject, beanClass);
		
	}
	
}