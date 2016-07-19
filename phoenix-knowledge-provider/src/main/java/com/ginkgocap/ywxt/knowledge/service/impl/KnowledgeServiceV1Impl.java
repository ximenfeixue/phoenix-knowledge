package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDaoV1;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeServiceV1;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/7/19.
 */
@Service("knowledgeServiceV1")
public class KnowledgeServiceV1Impl implements KnowledgeServiceV1, KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeServiceV1Impl.class);

    /**知识简表*/
    @Autowired
    private KnowledgeMysqlDao knowledgeMysqlDao;
    /**知识详细表*/
    @Autowired
    private KnowledgeMongoDaoV1 knowledgeMongoDaoV1;
    /**知识来源表*/
    @Autowired
    private KnowledgeReferenceDao knowledgeReferenceDao;

    /**MQ大数据服务*/
    //@Autowired
    //private BigDataService bigDataService;
    // 动态推送服务
    //@Autowired
    //private UserFeedService userFeedService;

    /**心情日记*/
    //@Autowired
    //private DiaryService diaryService;

    boolean isBigData = false;
    boolean isUserFeed = false;

    @Override
    public InterfaceResult insert(DataCollect DataCollect) {

        Knowledge detail = DataCollect.getKnowledgeDetail();
        KnowledgeReference knowledgeReference = DataCollect.getReference();

        //knowledgeDetail.createContendDesc();
        //知识详细信息插入
        Knowledge savedDetail = null;
        try {
            savedDetail = this.knowledgeMongoDaoV1.insert(detail);
        } catch (Exception ex) {
            logger.error("Create knowledge detail failed: error:  {}", ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        if (savedDetail == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        long knowledgeId = savedDetail.getId();

        //知识基础表插入
        KnowledgeBase knowledgeBase = DataCollect.generateKnowledge();
        try {
            knowledgeBase.setPrivated(permissionValue(DataCollect));
            this.knowledgeMysqlDao.insert(knowledgeBase);
        } catch (Exception e) {
            logger.error("知识基础表插入失败！失败原因：\n"+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //知识来源表插入
        KnowledgeReference savedKnowledgeReference = null;
        if (knowledgeReference != null) {
            try {
                knowledgeReference.setId(knowledgeId);
                knowledgeReference.setKnowledgeId(knowledgeId);
                savedKnowledgeReference = this.knowledgeReferenceDao.insert(knowledgeReference);
            } catch (Exception e) {
                logger.error("知识引用表插入失败！失败原因：\n" + e.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
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
		}*/

        //动态推送（仅推送观点）
		/*try {
			userFeedService.saveOrUpdate(PackingDataUtil.packingSendFeedData(afterSaveKnowledgeMongo, diaryService));
		} catch (Exception e) {
			logger.error("动态推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
    }

    @Override
    public InterfaceResult update(DataCollect DataCollect) throws Exception {

        Knowledge knowledgeDetail = DataCollect.getKnowledgeDetail();
        KnowledgeReference knowledgeReference = DataCollect.getReference();

        Long knowledgeId = knowledgeDetail.getId();
        long userId = knowledgeDetail.getCid();
        int columnId = KnowledgeUtil.parserColumnId(knowledgeDetail.getColumnid());

        //knowledgeMongo.createContendDesc();

        //知识详细表更新
        Knowledge ret = this.knowledgeMongoDaoV1.update(knowledgeDetail);
        logger.info("update knowledgeDetail: {}", ret);
        KnowledgeBase knowledge = DataCollect.generateKnowledge();

        //知识简表更新
        try {
            knowledge.setPrivated(permissionValue(DataCollect));
            this.knowledgeMysqlDao.update(knowledge);
        } catch (Exception e) {
            logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //知识来源表更新
        if (knowledgeReference != null) {
            try {
                knowledgeReference.setId(knowledgeId);
                knowledgeReference.setKnowledgeId(knowledgeId);
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
    public boolean updateKnowledge(DataCollect DataCollect) throws Exception {

        KnowledgeBase knowledge = DataCollect.getKnowledge();
        Knowledge knowledgeDetail = DataCollect.getKnowledgeDetail();

        Knowledge update = null;
        try {
            //知识详细表更新
            if (knowledgeDetail != null) {
                update = this.knowledgeMongoDaoV1.update(knowledgeDetail);
            }
            if (update == null) {
                logger.error("知识详细表更新失败！\n");
                return false;
            }
        }
        catch (Exception e) {
            logger.error("知识详细表更新失败！失败原因：\n"+e.getMessage());
            return false;
        }

        //知识简表更新
        try {
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
    public InterfaceResult deleteByKnowledgeId(long knowledgeId, int columnId) throws Exception {

        Knowledge oldKnowledgeDetail = this.knowledgeMongoDaoV1.getByIdAndColumnId(knowledgeId, columnId);
        long userId = oldKnowledgeDetail.getCid();

        //知识详细表删除
        try {
            this.knowledgeMongoDaoV1.deleteByIdAndColumnId(knowledgeId, columnId);
        } catch (Exception ex) {
            logger.error("知识详细表删除失败！失败原因：\n"+ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识详细删除失败!");
        }

        //知识简表删除
        try {
            this.knowledgeMysqlDao.deleteByKnowledgeId(knowledgeId);
        } catch (Exception e) {
            logger.error("知识基础表删除失败！失败原因：\n"+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识删除失败!");
        }

        //知识来源表删除
        try {
            this.knowledgeReferenceDao.deleteByKnowledgeId(knowledgeId);
        } catch (Exception e) {
            //this.deleteRollBack(knowledgeId, columnId,oldKnowledgeDetail,knowledge,null, true, true, false, false, false);
            logger.error("知识来源表删除失败！失败原因：\n"+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, "知识来源表删除失败！");
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
    public InterfaceResult batchDeleteByKnowledgeIds(List<Long> knowledgeIds, int columnId) throws Exception {

        List<Knowledge> oldKnowledgeMongoList = this.knowledgeMongoDaoV1.getByIdsAndColumnId(knowledgeIds, columnId);
        if (oldKnowledgeMongoList == null || oldKnowledgeMongoList.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        //知识详细表删除
        this.knowledgeMongoDaoV1.deleteByIdsAndColumnId(knowledgeIds, columnId);

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
    public Knowledge getDetailById(long knowledgeId, int columnId) throws Exception {

        Knowledge knowledgeDetail = this.knowledgeMongoDaoV1.getByIdAndColumnId(knowledgeId, columnId);
        if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        return knowledgeDetail;
    }

    public DataCollect getKnowledge(long knowledgeId,int columnId) throws Exception
    {
        Knowledge knowledgeDetail = this.knowledgeMongoDaoV1.getByIdAndColumnId(knowledgeId, columnId);
        if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
        if (knowledgeBase == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: {}, columnId: {}", knowledgeId, columnId);
        }

        if (knowledgeBase != null && knowledgeDetail != null) {
            return new DataCollect(knowledgeBase, knowledgeDetail);
        }

        return null;
    }

    @Override
    public InterfaceResult<DataCollect> getBaseById(long knowledgeId) throws Exception
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
    public long getBaseAllPublicCount(short permission) throws Exception
    {
        return this.knowledgeMysqlDao.getAllPublicCount(permission);
    }

    @Override
    public List<KnowledgeBase> getBaseAllPublic(int start,int size,short permission) throws Exception
    {
        return this.knowledgeMysqlDao.getAllPublic(start, size, permission);
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
    public List<KnowledgeBase> getBaseByCreateUserIdAndColumnId(long userId,int columnId,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByCreateUserIdAndColumnId(userId, columnId, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByCreateUserIdAndType(long userId,short type,int start,int size) throws Exception {
        return this.knowledgeMysqlDao.getByCreateUserIdAndType(userId, type, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByCreateUserIdAndColumnIdAndType(long UserId,int columnId, short type,int start,int size) throws Exception {
        return this.knowledgeMysqlDao.getByCreateUserIdAndTypeAndColumnId(UserId, type, columnId, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByType(short type,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByType(type, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByColumnId(int columnId,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByColumnId(columnId,start,size);
    }

    @Override
    public long getBasePublicCountByColumnId(int columnId,short permission) throws Exception
    {
        return this.knowledgeMysqlDao.getPublicCountByColumnId(columnId, permission);
    }

    @Override
    public List<KnowledgeBase> getBasePublicByColumnId(int columnId,short permission,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getPublicByColumnId(columnId, permission, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByKeyWord(long userId,int start,int size,String keyWord) throws Exception
    {
        return this.knowledgeMysqlDao.getByCreateUserIdKeyWord(userId, keyWord, start, size);
    }

    @Override
    public List<KnowledgeBase> getBaseByColumnIdAndKeyWord(String keyWord,int columnId,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByColumnIdAndKeyWord(keyWord, columnId, start, size);
    }

    @Override
    public InterfaceResult<List<DataCollect>> getBaseByColumnIdAndType(int columnId,short type,int start,int size) throws Exception
    {
        return InterfaceResult.getSuccessInterfaceResultInstance(getReturn(this.knowledgeMysqlDao.getByTypeAndColumnId(type, columnId, start, size)));
    }

    @Override
    public int getKnowledgeCount(long userId) throws Exception
    {
        return this.knowledgeMysqlDao.getKnowledgeCount(userId);
    }

    @Override
    public List<KnowledgeBase> getKnowledgeNoDirectory(long userId,int start,int size) throws Exception
    {
        //return this.knowledgeMysqlDao.getKnowledgeNoDirectory(userId, start, size);
        List<Knowledge> detailList = null;
        try {
            detailList = knowledgeMongoDaoV1.getNoDirectory(userId, start, size);
        } catch (Exception ex) {
            logger.error("get knowledge detail list failed: userId: {}", userId);
        }
        if (detailList != null && detailList.size() >0) {
            List<KnowledgeBase> baseList = new ArrayList<KnowledgeBase>(detailList.size());
            for (Knowledge detail : detailList) {
                KnowledgeBase base = DataCollect.generateKnowledge(detail);
                baseList.add(base);
            }
            return baseList;
        }
        return null;
    }


    /**
     * 返回数据包装方法
     * @param knowledgeList
     * @return
     */
    private List<DataCollect> getReturn(List<KnowledgeBase> knowledgeList) {

        List<DataCollect> returnList = new ArrayList<DataCollect>(knowledgeList.size());
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
    private DataCollect getReturn(KnowledgeBase knowledgeBase, KnowledgeReference knowledgeReference) {

        DataCollect DataCollect = new DataCollect();

        DataCollect.setKnowledge(knowledgeBase);

        DataCollect.setReference(knowledgeReference);

        return DataCollect;
    }

    private DataCollect getReturn(Knowledge knowledgeDetail, KnowledgeReference knowledgeReference) {

        DataCollect DataCollect = new DataCollect();
        DataCollect.setKnowledgeDetail(knowledgeDetail);
        DataCollect.setReference(knowledgeReference);

        return DataCollect;
    }

    private List<DataCollect> putReferenceList2BaseList(List<KnowledgeBase> knowledgeBaseList,List<KnowledgeReference> referenceList) {

        if(knowledgeBaseList == null || knowledgeBaseList.size() <= 0) {
            return null;
        }

        int referenceSize = referenceList != null ? referenceList.size() : 0;
        Map<Long, KnowledgeReference> referenceMap = new HashMap<Long, KnowledgeReference>(referenceSize > 0 ? referenceSize : 1);
        for (KnowledgeReference reference : referenceList) {
            referenceMap.put(reference.getKnowledgeId(), reference);
        }

        int knowledgeSize = knowledgeBaseList.size();
        List<DataCollect> returnList = new ArrayList<DataCollect>(knowledgeSize);
        for (KnowledgeBase knowledgeBase : knowledgeBaseList) {
            KnowledgeReference reference = referenceMap.get(knowledgeBase.getKnowledgeId());
            DataCollect DataCollect = new DataCollect(knowledgeBase, reference);
            returnList.add(DataCollect);
        }

        return returnList;
    }

    private short permissionValue(DataCollect data)
    {
        short privated = 0; //default is private
        if (data.getPermission() != null && data.getPermission().getPublicFlag() != null) {
            privated = data.getPermission().getPublicFlag().shortValue();
        }
        return privated;
    }
}
