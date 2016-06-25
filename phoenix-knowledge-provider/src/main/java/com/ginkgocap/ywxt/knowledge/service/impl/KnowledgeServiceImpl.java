package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.BigDataService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.user.service.DiaryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("knowledgeService")
public class KnowledgeServiceImpl implements KnowledgeService, KnowledgeBaseService {
	
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

	/**MQ大数据服务*/
	@Autowired
	private BigDataService bigDataService;
	/**动态推送服务*/
	//@Autowired
	//private UserFeedService userFeedService;
	/**心情日记*/
//	@Autowired
//	private DiaryService diaryService;

    boolean isBigData = false;
    boolean isUserFeed = false;

	@Override
	public InterfaceResult insert(DataCollection dataCollection) throws Exception {
		
        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();
		KnowledgeReference knowledgeReference = dataCollection.getReference();

        //knowledgeDetail.createContendDesc();
		
		//知识详细信息插入
        KnowledgeDetail savedKnowledgeDetail = this.knowledgeMongoDao.insert(knowledgeDetail);
        if (savedKnowledgeDetail == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        short columnId = knowledgeDetail.getColumnId();
        long knowledgeId = savedKnowledgeDetail.getId();
        long userId = knowledgeDetail.getOwnerId();

        //知识基础表插入
        KnowledgeBase knowledge = dataCollection.generateKnowledge();
		try {
            knowledge.setKnowledgeId(knowledgeId);
			this.knowledgeMysqlDao.insert(knowledge);
		} catch (Exception e) {
			logger.error("知识基础表插入失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, e);
		}
		
		//知识来源表插入
        KnowledgeReference savedKnowledgeReference = null;
        if (knowledgeReference != null) {
            try {
                knowledgeReference.setKnowledgeId(knowledgeId);
                savedKnowledgeReference = this.knowledgeReferenceDao.insert(knowledgeReference);
            } catch (Exception e) {
                logger.error("知识引用表插入失败！失败原因：\n" + e.getMessage());
                return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, e);
            }
        }

        //save directory
        /* move to web control to save these info
        List<Long> categorysList = knowledgeDetail.getCategoryIds();
        if(categorysList != null && categorysList.size() > 0){
            try {
                for (int index = 0; index < categorysList.size(); index++) {
                    logger.info("directoryId: {}", categorysList.get(index));
                    long directoryId = Long.valueOf(categorysList.get(index));
                    if (directoryId > 0) {
                        DirectorySource directorySource = createDirectorySource(userId, directoryId, knowledgeDetail);
                        directorySourceService.createDirectorySources(directorySource);
                    }
                    logger.info("directoryId:" + directoryId);
                }
            } catch (DirectorySourceServiceException ex) {
                ex.printStackTrace();
            }

        }
        //save tags
        List<Long> tagsList = knowledgeDetail.getTags();
        if (tagsList != null && tagsList.size() > 0) {
            try {
                for (int index = 0; index < tagsList.size(); index++) {
                    logger.info("directoryId: {}", tagsList.get(index));
                    Long tagId = Long.valueOf(tagsList.get(index));
                    if (tagId > 0) {
                        TagSource tagSource = createTagSource(userId, tagId, knowledgeDetail);
                        tagSourceService.createTagSource(tagSource);
                    }
                    logger.info("tagId:" + tagId);
                }
            } catch (TagSourceServiceException ex) {
                ex.printStackTrace();
            }
        }*/

		//大数据MQ推送
        /*
		try {
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_INSERT, KnowledgeMongo.clone(knowledge), savedKnowledgeDetail.getOwnerId());
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, userId, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送（仅推送观点）

		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, diaryService));
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
	}

	@Override
	public InterfaceResult update(DataCollection dataCollection) throws Exception {

        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();
		KnowledgeReference knowledgeReference = dataCollection.getReference();
		
		Long knowledgeId = knowledgeDetail.getId();
        long userId = knowledgeDetail.getOwnerId();
		short columnId = knowledgeDetail.getColumnId();

		//knowledgeMongo.createContendDesc();

		//知识详细表更新
        KnowledgeDetail ret = this.knowledgeMongoDao.update(knowledgeDetail);
        logger.info("knowledgeDetail: {}", knowledgeDetail);
        KnowledgeBase knowledge = dataCollection.generateKnowledge();
		
		//知识简表更新
		try {
			this.knowledgeMysqlDao.update(knowledge);
		} catch (Exception e) {
			//this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,null,null, true, false, false, false, false);
			logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
		
		//知识来源表更新
        if (knowledgeReference != null) {
            try {
                this.knowledgeReferenceDao.update(knowledgeReference);
            } catch (Exception e) {
                //this.updateRollBack(knowledgeId, columnId, oldKnowledgeDetail, knowledge, null, true, true, false, false, false);
                logger.error("知识来源表更新失败！失败原因：\n" + e.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
            }
        }

        //Update directory
        /* move to web control
        try{
            boolean removeDirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId);
            if(removeDirectoryFlag){
                List<Long> categoryList = knowledgeDetail.getCategoryIds();
                if(categoryList != null && categoryList.size() > 0){
                    for(Long directoryId : categoryList){
                        if(directoryId >= 0) {
                            DirectorySource directorySource = createDirectorySource(userId, directoryId, knowledgeDetail);
                            directorySourceService.createDirectorySources(directorySource);
                        }
                        logger.info("directoryId:" + directoryId);
                    }
                }
            }
            else{
                logger.error("update category remove failed...userId=" + userId+ ", knowledgeId=" + knowledgeId);
            }
        }catch(DirectorySourceServiceException e1){
            logger.error("update category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
        }
        //Update tags
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(removeTagsFlag){
                List<Long> tagsList = knowledgeDetail.getTags();
                if(tagsList != null){
                    for(Long tagId : tagsList){
                        if(tagId > 0){
                            TagSource tagSource = createTagSource(userId, tagId, knowledgeDetail);
                            tagSourceService.createTagSource(tagSource);
                            logger.info("tagId:"+tagId);
                        }
                    }
                }
            }
            else{
                logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
            }
        }catch(TagSourceServiceException ex){
            logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
        }*/
		
		//大数据MQ推送更新
		/*
        try {
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_UPDATE, KnowledgeMongo.clone(knowledge), knowledge.getCreateUserId());
		} catch (Exception e) {
			logger.error("知识MQ推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送更新（仅推送观点）

		try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, diaryService));
		} catch (Exception e) {
			this.updateRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}

    @Override
    public boolean updateKnowledge(DataCollection dataCollection) throws Exception {

        KnowledgeBase knowledge = dataCollection.getKnowledge();
        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();

        try {
            //知识详细表更新
            if (knowledgeDetail != null) {
                this.knowledgeMongoDao.update(knowledgeDetail);
            }

            //知识简表更新
            if (knowledge != null) {
                this.knowledgeMysqlDao.update(knowledge);
            }
        } catch (Exception e) {
            logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
            return false;
        }

        return true;
    }

	@Override
	public InterfaceResult deleteByKnowledgeId(long knowledgeId, short columnId) throws Exception {
		
        KnowledgeDetail oldKnowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
        long userId = oldKnowledgeDetail.getOwnerId();
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		
		//知识简表删除
		try {
			this.knowledgeMysqlDao.deleteByKnowledgeId(knowledgeId);
		} catch (Exception e) {
			logger.error("知识基础表删除失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
		} catch (Exception e) {
			//this.deleteRollBack(knowledgeId, columnId,oldKnowledgeDetail,knowledge,null, true, true, false, false, false);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}

        //delete directory
        /* move to web control do these operation
        try{
            boolean removeDirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, sourceType, knowledgeId);
            if(!removeDirectoryFlag){
                logger.error("category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
            }
        }catch(DirectorySourceServiceException ex){
            logger.error("category remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
        }
        //delete tags
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(!removeTagsFlag){
                logger.error("tags remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
            }
        }catch(TagSourceServiceException ex){
            logger.error("tags remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
        }*/
		
		//大数据MQ推送删除
        /*
		try {
			bigDataService.deleteMessage(knowledgeId, columnId, userId);
		} catch (Exception e) {
			logger.error("知识MQ推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}


		//动态推送删除（仅推送观点）
		try {
			userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteRollBack(knowledgeId, columnId,oldKnowledgeMongo,oldKnowledgeDetail,oldKnowledgeReference, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
	}

	@Override
	public InterfaceResult batchDeleteByKnowledgeIds(List<Long> knowledgeIds, short columnId) throws Exception {
		
		List<KnowledgeDetail> oldKnowledgeMongoList = this.knowledgeMongoDao.getByIdsAndColumnId(knowledgeIds, columnId);
        if (oldKnowledgeMongoList == null || oldKnowledgeMongoList.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdsAndColumnId(knowledgeIds, columnId);
		
		//知识简表删除
		try {
			this.knowledgeMysqlDao.batchDeleteByKnowledgeIds(knowledgeIds);
		} catch (Exception e) {
			logger.error("知识基础表删除失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.batchDeleteByKnowledgeIds(knowledgeIds);
		} catch (Exception e) {
			logger.error("知识来源表删除失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
		/*
		try {
            long userId = oldKnowledgeMongoList.get(0).getOwnerId();
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_DELETE, KnowledgeMongo.clone(oldKnowledgeMongoList), userId);
		} catch (Exception e) {
			logger.error("知识MQ推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}


		//动态推送删除（仅推送观点）
		try {
			for(long knowledgeId : knowledgeIds) 
				userFeedService.deleteDynamicKnowledge(knowledgeId);
		} catch (Exception e) {
			this.deleteListRollBack(oldKnowledgeMongoList,oldKnowledgeDetailList,oldKnowledgeReferenceList, true, true, true, true, false);
			logger.error("动态推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/
		
		return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeIds);
	}

	@Override
	public KnowledgeDetail getDetailById(long knowledgeId, short columnId) throws Exception {

        KnowledgeDetail knowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
		if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        return knowledgeDetail;
	}

    public DataCollection getKnowledge(long knowledgeId,short columnId) throws Exception
    {
        KnowledgeDetail knowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
        if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
        if (knowledgeBase == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        if (knowledgeBase != null && knowledgeDetail != null) {
            return new DataCollection(knowledgeBase, knowledgeDetail);
        }

        return null;
    }

	@Override
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId) throws Exception
    {
		KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(knowledgeBase,knowledgeReference));
	}

	@Override
	public List<KnowledgeBase> getBaseByIds(List<Long> knowledgeIds) throws Exception
    {
		List<KnowledgeBase> knowledgeList = this.knowledgeMysqlDao.getByKnowledgeIds(knowledgeIds);
        if (knowledgeList == null || knowledgeList.size() <= 0 ) {
            logger.info("can't get any knowledge by Ids: "+knowledgeIds.toString());
        }

        return knowledgeList;
	}

	@Override
	public List<KnowledgeBase> getBaseAll(int start,int size) throws Exception
    {
		return this.knowledgeMysqlDao.getAll(start, size);
	}

	@Override
	public List<KnowledgeBase> getBaseByCreateUserId(long userId,int start,int size) throws Exception
    {
		return this.knowledgeMysqlDao.getByCreateUserId(userId, start, size);
	}

    @Override
    public List<KnowledgeBase> getMyCollected(List<Long> knowledgeIds,String keyword) throws Exception
    {
        if (keyword == null || "null".equals(keyword)) {
            return this.knowledgeMysqlDao.getByKnowledgeIds(knowledgeIds);
        } else {
            return this.knowledgeMysqlDao.getByKnowledgeIdKeyWord(knowledgeIds, keyword);
        }
    }

	@Override
	public List<KnowledgeBase> getBaseByCreateUserIdAndColumnId(long userId,short columnId,int start,int size) throws Exception
    {
		return this.knowledgeMysqlDao.getByCreateUserIdAndColumnId(userId, columnId, start, size);
	}

	@Override
	public List<KnowledgeBase> getBaseByCreateUserIdAndType(long userId,short type,int start,int size) throws Exception {
		return this.knowledgeMysqlDao.getByCreateUserIdAndType(userId, type, start, size);
	}

	@Override
	public List<KnowledgeBase> getBaseByCreateUserIdAndColumnIdAndType(long UserId,short columnId, short type,int start,int size) throws Exception {
		return this.knowledgeMysqlDao.getByCreateUserIdAndTypeAndColumnId(UserId, type, columnId, start, size);
	}
	
	@Override
	public List<KnowledgeBase> getBaseByType(short type,int start,int size) throws Exception {
		return this.knowledgeMysqlDao.getByType(type, start, size);
	}
	
	@Override
	public List<KnowledgeBase> getBaseByColumnId(short columnId,int start,int size) throws Exception {
		return this.knowledgeMysqlDao.getByColumnId(columnId, start, size);
	}

    @Override
    public List<KnowledgeBase> getBaseByKeyWord(long userId,int start,int size,String keyWord) throws Exception {
        return this.knowledgeMysqlDao.getByCreateUserIdKeyWord(userId, keyWord, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByColumnIdAndKeyWord(String keyWord,short columnId,int start,int size) throws Exception {
        return this.knowledgeMysqlDao.getByColumnIdAndKeyWord(keyWord, columnId, start, size);
    }

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndType(short columnId,short type,int start,int size) throws Exception {
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByTypeAndColumnId(type, columnId, start, size)));
	}

	/**
	 * 插入时异常手动回滚方法
	 * @throws Exception
	 */
	private void insertRollBack(long knowledgeId, short columnId,long userId,boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		if(isBase) this.knowledgeMysqlDao.deleteByKnowledgeId(knowledgeId);
		if(isReference) this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
		if(isBigData) this.bigDataService.deleteMessage(knowledgeId, columnId, userId);
		//if(isUserFeed) this.userFeedService.deleteDynamicKnowledge(knowledgeId);
	}
	
	/**
	 * 更新时异常手动回滚方法
	 * @throws Exception
	 */
	private void updateRollBack(long knowledgeId, long columnId,
                                KnowledgeDetail oldKnowledgeDetail,KnowledgeBase oldKnowledge,KnowledgeReference oldKnowledgeReference,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeDetail);
		if(isBase) this.knowledgeMysqlDao.insertAfterDelete(oldKnowledge);
		if(isReference) {
            oldKnowledgeReference.setKnowledgeId(knowledgeId);
            this.knowledgeReferenceDao.insertAfterDelete(oldKnowledgeReference);
        }
		//if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_UPDATE, oldKnowledgeMongo, user);
		//if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, diaryService));
	}
	
	/**
	 * 单条删除时异常手动回滚方法
	 * @throws Exception
	 */
	private void deleteRollBack(long knowledgeId, short columnId,
                                KnowledgeDetail oldKnowledgeMongo,KnowledgeBase knowledge,KnowledgeReference oldKnowledgeReference,
			boolean isMongo,boolean isBase,boolean isReference,boolean isBigData,boolean isUserFeed) throws Exception {
		if(isMongo) this.knowledgeMongoDao.insert(oldKnowledgeMongo);
		if(isBase) this.knowledgeMysqlDao.insert(knowledge);
		if(isReference) {
            oldKnowledgeReference.setKnowledgeId(knowledgeId);
            this.knowledgeReferenceDao.insert(oldKnowledgeReference);
        }
		//if(isBigData) this.bigDataService.sendMessage(IBigDataService.KNOWLEDGE_INSERT, oldKnowledgeMongo, user);
		//if(isUserFeed) this.userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(oldKnowledgeMongo, diaryService));
	}
	
	/**
	 * 批量删除时异常手动回滚方法
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
	 * @param knowledgeList
	 * @return
	 */
	private List<DataCollection> getReturn(List<KnowledgeBase> knowledgeList) {

		List<DataCollection> returnList = new ArrayList<DataCollection>(knowledgeList.size());
		if(knowledgeList != null && !knowledgeList.isEmpty())
			for (KnowledgeBase data : knowledgeList)
				returnList.add(getReturn(data,null));
		
		return returnList;
	}
	
	/**
	 * 返回数据包装方法
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

    private List<DataCollection> putReferenceList2BaseList(List<KnowledgeBase> knowledgeBaseList,List<KnowledgeReference> referenceList) {

        if(knowledgeBaseList == null || knowledgeBaseList.size() <= 0) {
            return null;
        }

        int referenceSize = referenceList != null ? referenceList.size() : 0;
        Map<Long, KnowledgeReference> referenceMap = new HashMap<Long, KnowledgeReference>(referenceSize > 0 ? referenceSize : 1);
        for (KnowledgeReference reference : referenceList) {
            referenceMap.put(reference.getKnowledgeId(), reference);
        }

        int knowledgeSize = knowledgeBaseList.size();
        List<DataCollection> returnList = new ArrayList<DataCollection>(knowledgeSize);
        for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
            KnowledgeReference reference = referenceMap.get(knowledgeBase.getKnowledgeId());
            DataCollection dataCollection = new DataCollection(knowledgeBase, reference);
            returnList.add(dataCollection);
        }

        return returnList;
    }
}