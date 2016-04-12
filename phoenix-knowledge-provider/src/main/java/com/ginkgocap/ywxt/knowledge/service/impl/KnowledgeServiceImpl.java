package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.BigDataService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.ginkgocap.ywxt.user.service.DiaryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("knowledgeService")
public class KnowledgeServiceImpl implements KnowledgeService {
	
	private Logger logger = LoggerFactory.getLogger(KnowledgeServiceImpl.class);
	
	/**知识简表*/
	@Autowired
	private KnowledgeMysqlDao knowledgeMysqlDao;
	/**知识详细表*/
	@Autowired
	private KnowledgeMongoDao knowledgeMongoDao;
	/**知识来源表*/
	@Autowired
	private KnowledgeReferenceDao knowledgeReferenceDao;
	/**知识公共服务*/
	@Autowired
	private KnowledgeCommonService knowledgeCommonService;
	/**MQ大数据服务*/
	@Autowired
	private BigDataService bigDataService;
	/**动态推送服务*/
	//@Autowired
	//private UserFeedService userFeedService;
	/**心情日记*/
	@Autowired
	private DiaryService diaryService;

    boolean isBigData = false;
    boolean isUserFeed = false;

	@Override
	public InterfaceResult insert(DataCollection dataCollection) throws Exception {
		
        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();
		KnowledgeReference knowledgeReference = dataCollection.getReference();
		
		long knowledgeId = this.knowledgeCommonService.getKnowledgeSeqenceId();
		short columnId = knowledgeDetail.getColumnId();
        knowledgeDetail.setId(knowledgeId);

        KnowledgeBase knowledge = dataCollection.generateKnowledge();
		
        //knowledgeDetail.createContendDesc();
		
		//知识详细信息插入
        KnowledgeDetail afterSaveKnowledgeDetail = this.knowledgeMongoDao.insert(knowledgeDetail);
		
		//知识基础表插入
		try {
			this.knowledgeMysqlDao.insert(knowledge);
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, true, false, false, false, false);
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表插入
        KnowledgeReference savedKnowledgeReference = null;
        if (knowledgeReference != null) {
            try {
                savedKnowledgeReference = this.knowledgeReferenceDao.insert(knowledgeReference, knowledgeId);
            } catch (Exception e) {
                this.insertRollBack(knowledgeId, columnId, true, true, false, false, false);
                logger.error("知识基础表插入失败！失败原因：\n" + e.getCause().toString());
                return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
            }
        }
		
		//大数据MQ推送
        /*
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		//动态推送（仅推送观点）
        /*
		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, diaryService));
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
	}

	@Override
	public InterfaceResult update(DataCollection dataCollection) throws Exception {

        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();
		KnowledgeReference knowledgeReference = dataCollection.getReference();
		
		Long knowledgeId = knowledgeDetail.getId();
		short columnId = knowledgeDetail.getColumnId();

		//knowledgeMongo.createContendDesc();

		//知识详细表更新
        KnowledgeDetail ret = this.knowledgeMongoDao.update(knowledgeDetail);

        KnowledgeBase knowledge = dataCollection.generateKnowledge();
		
		//知识简表更新
		//KnowledgeDetail retKnowledgeDetail = this.knowledgeMysqlDao.getById(knowledgeId);
		try {
			this.knowledgeMysqlDao.update(knowledge);
		} catch (Exception e) {
			//this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,null,null, true, false, false, false, false);
			logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表更新
        KnowledgeReference updatedReference = null;
        if (knowledgeReference != null) {
            try {
                updatedReference = this.knowledgeReferenceDao.update(knowledgeReference);
            } catch (Exception e) {
                //this.updateRollBack(knowledgeId, columnId, oldKnowledgeDetail, knowledge, null, true, true, false, false, false);
                logger.error("知识来源表更新失败！失败原因：\n" + e.getCause().toString());
                return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
            }
        }
		
		//大数据MQ推送更新
        /*
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_UPDATE, afterSaveKnowledgeMongo, user);
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		//动态推送更新（仅推送观点）
        /*
		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, diaryService));
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}

	@Override
	public InterfaceResult deleteByKnowledgeId(long knowledgeId, short columnId) throws Exception {
		
        KnowledgeDetail oldKnowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		
		//知识简表删除
		KnowledgeBase knowledge = this.knowledgeMysqlDao.getById(knowledgeId);
		try {
			this.knowledgeMysqlDao.deleteById(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId, oldKnowledgeDetail,null,null, true, false, false, false, false);
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.deleteById(knowledgeId);
		} catch (Exception e) {
			//this.deleteRollBack(knowledgeId, columnId,oldKnowledgeDetail,knowledge,null, true, true, false, false, false);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
        /*
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_DELETE, oldKnowledgeMongo, user);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送删除（仅推送观点）
		try {
			userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}

	@Override
	public InterfaceResult deleteByKnowledgeIds(List<Long> knowledgeIds, short columnId) throws Exception {
		
		List<KnowledgeDetail> oldKnowledgeMongoList = this.knowledgeMongoDao.getByIdsAndColumnId(knowledgeIds, columnId);
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdsAndColumnId(knowledgeIds, columnId);
		
		//知识简表删除
		try {
			this.knowledgeMysqlDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.deleteByIds(knowledgeIds);
		} catch (Exception e) {
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
        /*
		try {
			bigDataService.sendMessage(IBigDataService.KNOWLEDGE_DELETE, oldKnowledgeMongoList, user);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeDetailList,oldKnowledgeReferenceList, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送删除（仅推送观点）
		try {
			for(long knowledgeId : knowledgeIds) 
				userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeDetailList,oldKnowledgeReferenceList, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeIds);
	}

	@Override
	public InterfaceResult<KnowledgeDetail> getDetailById(long knowledgeId, short columnId) throws Exception {

        KnowledgeDetail knowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		if (knowledgeDetail != null) {
            return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeDetail);
        }
        else {
            //TODO: should return why can't get the detail info.
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
		
	}

	@Override
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId) throws Exception {
		
		KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getById(knowledgeId);
		
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(knowledgeBase,knowledgeReference));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByIds(List<Long> knowledgeIds) throws Exception {
		
		List<KnowledgeBase> knowledgeList = this.knowledgeMysqlDao.getByIds(knowledgeIds);
		
		List<KnowledgeReference> knowledgeReferenceList = this.knowledgeReferenceDao.getByIds(knowledgeIds);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(KnowledgeReference.putReferenceList2BaseList(knowledgeList,knowledgeReferenceList));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseAll(int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getAll(start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserId(long userId,int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByCreateUserId(userId, start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnId(long userId,short columnId,int start,int size) throws Exception {
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByCreateUserIdAndColumnId(userId, columnId, start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndType(long userId,short type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByCreateUserIdAndType(userId, type, start, size)));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnIdAndType(long UserId,short columnId, short type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByCreateUserIdAndTypeAndColumnId(UserId, type, columnId, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByType(short type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByType(type, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByColumnId(short columnId,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByColumnId(columnId, start, size)));
	}
	
	@Override
	public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndType(short columnId,short type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByTypeAndColumnId(type, columnId, start, size)));
	}
	
	/**
	 * 插入时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:30:18
	 * @throws Exception
	 */
	private void insertRollBack(long knowledgeId, short columnId,boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		if(isBase) this.knowledgeMysqlDao.deleteById(knowledgeId);
		if(isReference) this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
		if(isBigData) this.bigDataService.deleteMessage(knowledgeId, columnId, null);
		//if(isUserFeed) this.userFeedService.deleteDynamicKnowledge(knowledgeId);
	}
	
	/**
	 * 更新时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:30:54
	 * @throws Exception
	 */
	private void updateRollBack(long knowledgeId, long columnId,
                                KnowledgeDetail oldKnowledgeDetail,KnowledgeBase oldKnowledge,KnowledgeReference oldKnowledgeReference,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeDetail, null);
		if(isBase) this.knowledgeMysqlDao.insertAfterDelete(oldKnowledge);
		if(isReference) this.knowledgeReferenceDao.insertAfterDelete(oldKnowledgeReference, knowledgeId);
		//if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_UPDATE, oldKnowledgeMongo, user);
		//if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, diaryService));
	}
	
	/**
	 * 单条删除时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:31:29
	 * @throws Exception
	 */
	private void deleteRollBack(long knowledgeId, short columnId,
                                KnowledgeDetail oldKnowledgeMongo,KnowledgeBase knowledge,KnowledgeReference oldKnowledgeReference,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insert(oldKnowledgeMongo);
		if(isBase) this.knowledgeMysqlDao.insert(knowledge);
		if(isReference) this.knowledgeReferenceDao.insert(oldKnowledgeReference, knowledgeId);
		//if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, oldKnowledgeMongo, user);
		//if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, diaryService));
	}
	
	/**
	 * 批量删除时异常手动回滚方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:32:13
	 * @throws Exception
	 */
	private void deleteListRollBack(List<KnowledgeDetail> oldKnowledgeDetailList,List<KnowledgeBase> oldKnowledgelList,List<KnowledgeReference> oldKnowledgeReferenceList,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insertList(oldKnowledgeDetailList);
		if(isBase) this.knowledgeMysqlDao.insertList(oldKnowledgelList);
		if(isReference) this.knowledgeReferenceDao.insertList(oldKnowledgeReferenceList);
		//if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, oldKnowledgeMongoList, user);
		if(isUserFeed) {
			//for (KnowledgeDetail oldKnowledgeMongo: oldKnowledgeMongoList)
			//this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, diaryService));
		}
		
	}
	
	/**
	 * 返回数据包装方法
	 * @author 周仕奇
	 * @date 2016年1月15日 上午11:32:58
	 * @param knowledgeList
	 * @return
	 */
	private List<DataCollection> getReturn(List<KnowledgeBase> knowledgeList) {

		List<DataCollection> returnList = new ArrayList<DataCollection>();
		if(knowledgeList != null && !knowledgeList.isEmpty())
			for (KnowledgeBase data : knowledgeList)
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

    private DataCollection getReturn(KnowledgeDetail knowledgeDetail, KnowledgeReference knowledgeReference) {

        DataCollection dataCollection = new DataCollection();

        dataCollection.setKnowledgeDetail(knowledgeDetail);

        dataCollection.setReference(knowledgeReference);

        return dataCollection;
    }
	
}