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
	@Autowired
	private DiaryService diaryService;

    @Autowired
    private DirectorySourceService directorySourceService;

    @Autowired
    private TagSourceService tagSourceService;

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
			logger.error("知识基础表插入失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表插入
        KnowledgeReference savedKnowledgeReference = null;
        if (knowledgeReference != null) {
            try {
                knowledgeReference.setKnowledgeId(knowledgeId);
                savedKnowledgeReference = this.knowledgeReferenceDao.insert(knowledgeReference);
            } catch (Exception e) {
                logger.error("知识基础表插入失败！失败原因：\n" + e.getCause().toString());
                return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
            }
        }

        //save directory
        List<String> categorysList = knowledgeDetail.getCategoryIds();
        if(categorysList != null && categorysList.size() > 0){
            try {
                for (String directoryId : categorysList) {
                    DirectorySource directorySource = new DirectorySource();
                    directorySource.setUserId(userId);
                    directorySource.setDirectoryId(Long.parseLong(directoryId));
                    directorySource.setAppId(APPID);
                    directorySource.setSourceId(knowledgeId);
                    //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                    directorySource.setSourceType((int) sourceType);
                    directorySource.setCreateAt(new Date().getTime());
                    directorySourceService.createDirectorySources(directorySource);
                    logger.info("dircetoryId:" + directoryId);
                }
            } catch (DirectorySourceServiceException ex) {
                ex.printStackTrace();
            }

        }
        //save tags
        List<String> tagsList = knowledgeDetail.getTags();
        if (tagsList != null && tagsList.size() > 0) {
            try {
                for (String tagId : tagsList) {
                    TagSource tagSource = new TagSource();
                    tagSource.setUserId(userId);
                    tagSource.setAppId(APPID);
                    tagSource.setSourceId(knowledgeId);
                    //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                    tagSource.setSourceType(sourceType);
                    tagSource.setTagId(Long.parseLong(tagId));
                    tagSource.setCreateAt(new Date().getTime());
                    tagSourceService.createTagSource(tagSource);
                    logger.info("tagId:" + tagId);
                }
            } catch (TagSourceServiceException ex) {
                ex.printStackTrace();
            }
        }

		//大数据MQ推送
        /*
		try {
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_INSERT, KnowledgeMongo.clone(knowledge), savedKnowledgeDetail.getOwnerId());
		} catch (Exception e) {
			this.insertRollBack(knowledgeId, columnId, userId, true, true, true, false, false);
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送（仅推送观点）

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
        long userId = knowledgeDetail.getOwnerId();
		short columnId = knowledgeDetail.getColumnId();

		//knowledgeMongo.createContendDesc();

		//知识详细表更新
        KnowledgeDetail ret = this.knowledgeMongoDao.update(knowledgeDetail);

        KnowledgeBase knowledge = dataCollection.generateKnowledge();
		
		//知识简表更新
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

        //Update directory
        try{
            boolean removeDdirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, 4, knowledgeId);
            if(removeDdirectoryFlag){
                List<String> categorysList = knowledgeDetail.getCategoryIds();
                if(categorysList != null && categorysList.size() > 0){
                    for(String directoryId : categorysList){
                        DirectorySource directorySource = new DirectorySource();
                        directorySource.setUserId(userId);
                        directorySource.setDirectoryId(Long.parseLong(directoryId));
                        directorySource.setAppId(1);
                        directorySource.setSourceId(knowledgeId);
                        //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                        directorySource.setSourceType(3);
                        directorySource.setCreateAt(new Date().getTime());
                        directorySourceService.createDirectorySources(directorySource);
                        logger.info("dircetoryId:"+directoryId);
                    }
                }
            }
            else{
                logger.error("update categorys remove failed...userid=" + userId+ ", knowledgeId=" + knowledgeId);
            }
        }catch(DirectorySourceServiceException e1){
            logger.error("update categorys remove failed...userid=" + userId + ", knowledgeId=" + knowledgeId);
        }
        //Update tags
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(removeTagsFlag){
                List<String> tagsList = knowledgeDetail.getTags();
                if(tagsList != null){
                    for(String tagId : tagsList){
                        if(tagId != null && StringUtils.isNotBlank(tagId)){
                            TagSource  tagSource  = new TagSource ();
                            tagSource.setUserId(userId);
                            tagSource.setAppId(APPID);
                            tagSource.setSourceId(knowledgeId);
                            //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                            tagSource.setSourceType(sourceType);
                            tagSource.setTagId(Long.parseLong(tagId));
                            tagSource.setCreateAt(new Date().getTime());
                            tagSourceService.createTagSource(tagSource);
                            logger.info("tagId:"+tagId);
                        }
                    }
                }
            }
            else{
                logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
            }
        }catch(TagSourceServiceException e2){
            logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
        }
		
		//大数据MQ推送更新
		/*
        try {
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_UPDATE, KnowledgeMongo.clone(knowledge), knowledge.getCreateUserId());
		} catch (Exception e) {
			logger.error("知识MQ推送失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//动态推送更新（仅推送观点）

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
        long userId = oldKnowledgeDetail.getOwnerId();
		
		//知识详细表删除
		this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnId);
		
		//知识简表删除
		try {
			this.knowledgeMysqlDao.deleteByKnowledgeId(knowledgeId);
		} catch (Exception e) {
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
		} catch (Exception e) {
			//this.deleteRollBack(knowledgeId, columnId,oldKnowledgeDetail,knowledge,null, true, true, false, false, false);
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}

        //delete directory
        try{
            boolean removeDdirectoryFlag = directorySourceService.removeDirectorySourcesBySourceId(userId, APPID, 4, knowledgeId);
            if(!removeDdirectoryFlag){
                logger.error("categorys remove failed...userid=" + userId + ", knowledgeId=" + knowledgeId);
            }
        }catch(DirectorySourceServiceException e1){
            logger.error("categorys remove failed...userid=" + userId + ", knowledgeId=" + knowledgeId);
            e1.printStackTrace();
        }
        //delete tags
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(!removeTagsFlag){
                logger.error("tags remove failed...userid=" + userId + ", knowledgeId=" + knowledgeId);
            }
        }catch(TagSourceServiceException e2){
            logger.error("tags remove failed...userid=" + userId + ", knowledgeId=" + knowledgeId);
            e2.printStackTrace();
        }
		
		//大数据MQ推送删除
        /*
		try {
			bigDataService.deleteMessage(knowledgeId, columnId, userId);
		} catch (Exception e) {
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
			logger.error("知识基础表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//知识来源表删除
		try {
			this.knowledgeReferenceDao.batchDeleteByKnowledgeIds(knowledgeIds);
		} catch (Exception e) {
			logger.error("知识来源表删除失败！失败原因：\n"+e.getCause().toString());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}
		
		//大数据MQ推送删除
		/*
		try {
            long userId = oldKnowledgeMongoList.get(0).getOwnerId();
			bigDataService.sendMessage(BigDataService.KNOWLEDGE_DELETE, KnowledgeMongo.clone(oldKnowledgeMongoList), userId);
		} catch (Exception e) {
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
	public InterfaceResult<DataCollection> getBaseById(long knowledgeId) throws Exception
    {
		KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
		KnowledgeReference knowledgeReference = this.knowledgeReferenceDao.getById(knowledgeId);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(knowledgeBase,knowledgeReference));
	}

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByIds(List<Long> knowledgeIds) throws Exception
    {
		List<KnowledgeBase> knowledgeList = this.knowledgeMysqlDao.getByKnowledgeIds(knowledgeIds);
        if (knowledgeList == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }
		List<KnowledgeReference> referenceList = this.knowledgeReferenceDao.getByIds(knowledgeIds);
		
		return InterfaceResult.getSuccessInterfaceResultInstance(putReferenceList2BaseList(knowledgeList,referenceList));
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
        if ("null".equals(keyword)) {
            return this.knowledgeMysqlDao.getByKnowledgeIds(knowledgeIds);
        } else {
            return this.knowledgeMysqlDao.getByKnowledgeIdKeyWord(knowledgeIds, keyword);
        }
    }

	@Override
	public InterfaceResult<List<DataCollection>> getBaseByCreateUserIdAndColumnId(long userId,short columnId,int start,int size) throws Exception
    {
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
    public List<KnowledgeBase> getBaseByKeyWord(long userId,int start,int size,String keyWord) throws Exception {
        return this.knowledgeMysqlDao.getByCreateUserIdKeyWord(userId, keyWord, start, size);
    }

    @Override
    public InterfaceResult<List<DataCollection>> getBaseByTagId(long tagId,int start,int size) throws Exception
    {
        List<TagSource> tagSources = tagSourceService.getTagSourcesByAppIdTagId(APPID, tagId, start, size );
        List<Long> knowledgeIds = new ArrayList<Long>(tagSources.size());
        if (tagSources == null || tagSources.size() <= 0) {
            for (TagSource tag : tagSources) {
                knowledgeIds.add(tag.getSourceId());
            }
        }

        return getBaseByIds(knowledgeIds);
    }

    @Override
    public InterfaceResult<List<DataCollection>> getBaseByColumnIdAndKeyWord(String keyWord,short columnId,int start,int size) throws Exception {
        return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByColumnIdAndKeyWord(keyWord, columnId, start, size)));
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
		if(isMongo) this.knowledgeMongoDao.insertAfterDelete(oldKnowledgeDetail, null);
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