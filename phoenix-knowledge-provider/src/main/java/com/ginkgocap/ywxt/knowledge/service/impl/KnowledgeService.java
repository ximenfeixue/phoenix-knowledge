package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ginkgocap.ywxt.asso.service.annotation.AssoDeleteAnnotation;
import com.ginkgocap.ywxt.asso.service.annotation.AssoGetAnnotation;
import com.ginkgocap.ywxt.asso.service.annotation.AssoSaveAnnotation;
import com.ginkgocap.ywxt.asso.service.annotation.AssoUpdateAnnotation;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeBaseDao;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMongo;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.IKnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.IBigDataService;
import com.ginkgocap.ywxt.knowledge.service.common.IKnowledgeCommonService;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.DiaryService;
import com.ginkgocap.ywxt.user.service.UserFeedService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;

@Service("knowledgeService")
public class KnowledgeService implements IKnowledgeService {
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeService.class);
	
	/**知识简表*/
	@Autowired
	private IKnowledgeBaseDao knowledgeBaseDao;
	/**知识详细表*/
	@Autowired
	private IKnowledgeMongoDao knowledgeMongoDao;
	/**知识来源表*/
	@Autowired
	private IKnowledgeReferenceDao knowledgeReferenceDao;
	/**知识公共服务*/
	@Autowired
	private IKnowledgeCommonService knowledgeCommonService;
	/**MQ大数据服务*/
	@Autowired
	private IBigDataService bigDataService;
	/**动态推送服务*/
	@Autowired
	private UserFeedService userFeedService;
	/**心情日记*/
	@Autowired
	private DiaryService diaryService;
	
	@AssoSaveAnnotation
	@Override
	public InterfaceResult<DataCollection> insert(DataCollection dataCollection, User user) throws Exception {
		
		KnowledgeMongo knowledgeMongo = (KnowledgeMongo) dataCollection.getKnowledge();
		KnowledgeReference knowledgeReference = dataCollection.getReference();
		
		long knowledgeId = this.knowledgeCommonService.getKnowledgeSeqenceId();
		
		long columnId = knowledgeMongo.getColumnId();
		
		knowledgeMongo.setId(knowledgeId);
		
		knowledgeMongo.createContendDesc();
		
		//知识详细信息插入
		KnowledgeMongo afterSaveKnowledgeMongo = this.knowledgeMongoDao.insert(knowledgeMongo, user);
		
		//知识基础表插入
		try {
			this.knowledgeBaseDao.insert(afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, user, true, false, false, false, false);
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表插入
		KnowledgeReference afterSaveKnowledgeReference = null;
		try {
			this.knowledgeReferenceDao.insert(knowledgeReference, knowledgeId, user);
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, user, true, true, false, false, false);
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, user, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送（仅推送观点）
		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, user, diaryService));
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, user, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(afterSaveKnowledgeMongo,afterSaveKnowledgeReference));
	}

	@AssoUpdateAnnotation
	@Override
	public InterfaceResult<DataCollection> update(DataCollection dataCollection, User user) throws Exception {

		KnowledgeMongo knowledgeMongo = (KnowledgeMongo) dataCollection.getKnowledge();
		KnowledgeReference knowledgeReference = dataCollection.getReference();
		
		long knowledgeId = knowledgeMongo.getId();
		
		long columnId = knowledgeMongo.getColumnId();
		
		knowledgeMongo.createContendDesc();
		
		KnowledgeMongo oldKnowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		//知识详细表更新
		KnowledgeMongo afterSaveKnowledgeMongo = this.knowledgeMongoDao.update(knowledgeMongo, user);
		
		//知识简表更新
		KnowledgeBase oldKnowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		try {
			this.knowledgeBaseDao.update(afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,null,null, user, true, false, false, false, false);
			logger.error("知识基础表更新失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表更新
		KnowledgeReference afterSaveKnowledgeReference = null;
		KnowledgeReference oldKnowledgeReference = this.knowledgeReferenceDao.getByKnowledgeId(knowledgeId);
		try {
			this.knowledgeReferenceDao.update(knowledgeReference, user);
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,null, user, true, true, false, false, false);
			logger.error("知识来源表更新失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送更新
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_UPDATE, afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,oldKnowledgeReference, user, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送更新（仅推送观点）
		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, user, diaryService));
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,oldKnowledgeReference, user, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(afterSaveKnowledgeMongo,afterSaveKnowledgeReference));
	}

	@AssoDeleteAnnotation
	@Override
	public InterfaceResult<DataCollection> deleteByKnowledgeId(long knowledgeId, long columnId, User user) throws Exception {
		
		KnowledgeMongo oldKnowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		
		//知识简表删除
		KnowledgeBase oldKnowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		try {
			this.knowledgeBaseDao.deleteById(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,null,null, user, true, false, false, false, false);
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		KnowledgeReference oldKnowledgeReference = this.knowledgeReferenceDao.getByKnowledgeId(knowledgeId);
		try {
			this.knowledgeReferenceDao.deleteById(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,null, user, true, true, false, false, false);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_DELETE, oldKnowledgeMongo, user);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,oldKnowledgeReference, user, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送删除（仅推送观点）
		try {
			userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeBase,oldKnowledgeReference, user, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
	}

	@AssoDeleteAnnotation
	@Override
	public InterfaceResult<DataCollection> deleteByKnowledgeIds(List<Long> knowledgeIds, long columnId, User user) throws Exception {
		
		List<KnowledgeMongo> oldKnowledgeMongoList = this.knowledgeMongoDao.getByIdsAndColumnId(knowledgeIds, columnId);
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdsAndColumnId(knowledgeIds, columnId);
		
		//知识简表删除
		List<KnowledgeBase> oldKnowledgeBaseList = this.knowledgeBaseDao.getByIds(knowledgeIds);
		try {
			this.knowledgeBaseDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,null,null, user, true, false, false, false, false);
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		List<KnowledgeReference> oldKnowledgeReferenceList = this.knowledgeReferenceDao.getByKnowledgeIds(knowledgeIds);
		try {
			this.knowledgeReferenceDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeBaseList,null, user, true, true, false, false, false);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_DELETE, oldKnowledgeMongoList, user);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeBaseList,oldKnowledgeReferenceList, user, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送删除（仅推送观点）
		try {
			for(long knowledgeId : knowledgeIds) 
				userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeBaseList,oldKnowledgeReferenceList, user, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
	}

	@AssoGetAnnotation
	@Override
	public InterfaceResult<DataCollection> getDetailById(long knowledgeId,long columnId,User user) throws Exception {
		
		KnowledgeMongo knowledgeMongo = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(knowledgeMongo,knowledgeReference));
		
	}

	@Override
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId,User user) throws Exception {
		
		KnowledgeBase knowledgeBase = this.knowledgeBaseDao.getById(knowledgeId);
		
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(knowledgeBase,knowledgeReference));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByIds(List<Long> knowledgeIds,User user) throws Exception {
		
		List<KnowledgeBase> knowledgeBaseList = this.knowledgeBaseDao.getByIds(knowledgeIds);
		
		List<KnowledgeReference> knowledgeReferenceList = this.knowledgeReferenceDao.getByIds(knowledgeIds);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(KnowledgeReference.putReferenceList2BaseList(knowledgeBaseList,knowledgeReferenceList));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseAll(int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getAll(start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserId(User user,int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByCreateUserId(user.getId(), start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnId(User user,long columnId,int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByCreateUserIdAndColumnId(user.getId(), columnId, start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndType(User user,String type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByCreateUserIdAndType(user.getId(), type, start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnIdAndType(User user,long columnId,String type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByCreateUserIdAndTypeAndColumnId(user.getId(), type, columnId, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByType(String type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByType(type, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByColumnId(long columnId,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByColumnId(columnId, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndType(long columnId,String type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeBaseDao.getByTypeAndColumnId(type, columnId, start, size)));
	}
	
	/**
	 * 插入时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:30:18
	 * @throws Exception
	 */
	private void insertRollBack(long knowledgeId, long columnId, User user,boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		if(isBase) this.knowledgeBaseDao.deleteById(knowledgeId);
		if(isReference) this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
		if(isBigData) this.bigDataService.deleteMessage(knowledgeId, columnId, user);
		if(isUserFeed) this.userFeedService.deleteDynamicKnowledge(knowledgeId);
	}
	
	/**
	 * 更新时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:30:54
	 * @throws Exception
	 */
	private void updateRollBack(long knowledgeId, long columnId,
			KnowledgeMongo oldKnowledgeMongo,KnowledgeBase oldKnowledgeBase,KnowledgeReference oldKnowledgeReference, User user,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeMongo, knowledgeId, user);
		if(isBase) this.knowledgeBaseDao.insertAfterDelete(oldKnowledgeBase, user);
		if(isReference) this.knowledgeReferenceDao.insertAfterDelete(oldKnowledgeReference, knowledgeId, user);
		if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_UPDATE, oldKnowledgeMongo, user);
		if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, user, diaryService));
	}
	
	/**
	 * 单条删除时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:31:29
	 * @throws Exception
	 */
	private void deleteRollBack(long knowledgeId, long columnId,
			KnowledgeMongo oldKnowledgeMongo,KnowledgeBase oldKnowledgeBase,KnowledgeReference oldKnowledgeReference, User user,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insert(oldKnowledgeMongo, user);
		if(isBase) this.knowledgeBaseDao.insert(oldKnowledgeBase, user);
		if(isReference) this.knowledgeReferenceDao.insert(oldKnowledgeReference, knowledgeId, user);
		if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, oldKnowledgeMongo, user);
		if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, user, diaryService));
	}
	
	/**
	 * 批量删除时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:32:13
	 * @throws Exception
	 */
	private void deleteListRollBack(List<KnowledgeMongo> oldKnowledgeMongoList,List<KnowledgeBase> oldKnowledgeBaseList,List<KnowledgeReference> oldKnowledgeReferenceList, User user,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insertList(oldKnowledgeMongoList, user);
		if(isBase) this.knowledgeBaseDao.insertList(oldKnowledgeBaseList, user);
		if(isReference) this.knowledgeReferenceDao.insertList(oldKnowledgeReferenceList, user);
		if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, oldKnowledgeMongoList, user);
		if(isUserFeed) {
			for (KnowledgeMongo oldKnowledgeMongo: oldKnowledgeMongoList)
			this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, user, diaryService));
		}
		
	}
	
	/**
	 * 返回数据包装方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:32:58
	 * @param knowledgeBaseList
	 * @return
	 */
	private List<DataCollection> getReturn(List<KnowledgeBase> knowledgeBaseList) {

		List<DataCollection> returnList = new ArrayList<DataCollection>();
		if(knowledgeBaseList != null && !knowledgeBaseList.isEmpty())
			for (KnowledgeBase data : knowledgeBaseList)
				returnList.add(getReturn(data,null));
		
		return returnList;
	}
	
	/**
	 * 返回数据包装方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:33:16
	 * @param knowledgeBase
	 * @param knowledgeReference
	 * @return
	 */
	private DataCollection getReturn(KnowledgeBase knowledgeBase, KnowledgeReference knowledgeReference) {

		DataCollection dataCollection = new DataCollection();
		
		dataCollection.setKnowledge(knowledgeBase);
		
		dataCollection.setReference(knowledgeReference);
		
		return dataCollection;
	}
	
}