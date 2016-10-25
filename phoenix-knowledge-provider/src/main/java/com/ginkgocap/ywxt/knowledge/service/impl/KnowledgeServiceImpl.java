package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseSync;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.model.mobile.EActionType;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
@Service("knowledgeService")
public class KnowledgeServiceImpl implements KnowledgeService, KnowledgeBaseService
{
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

    private static final String endContent = "若涉及版权或第三方网络侵权问题，请及时与我公司服务中心联系。";
    private static final int endLength = endContent.length();
    private static final String suffix = "</br>版权归原作者所有，金桐网依法保护版权，保护权利人合法权益。金桐网部分文章、知识等若未能第一时间与作者取得联系或标注原始出处，请谅解。若涉及版权或第三方网络侵权问题，请及时与我公司服务中心联系。";

    @Override
    public InterfaceResult insert(DataCollect DataCollect) {

        Knowledge detail = DataCollect.getKnowledgeDetail();
        KnowledgeReference knowledgeReference = DataCollect.getReference();

        //knowledgeDetail.createContendDesc();
        //知识详细信息插入
        Knowledge savedDetail = insertKnowledgeDetail(detail);
        if (savedDetail == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        long knowledgeId = savedDetail.getId();

        //知识基础表插入
        KnowledgeBase knowledgeBase = DataCollect.generateKnowledge();
        try {
            knowledgeBase.setPrivated(permissionValue(DataCollect));
            this.knowledgeMysqlDao.insert(knowledgeBase);
            logger.info("End insert knowledge to tb_knowledge_base. knowledgeId: " + knowledgeBase.getId());
        } catch (Exception e) {
            short columnType = KnowledgeUtil.parserShortType(savedDetail.getColumnType());
            knowledgeMongoDao.backupKnowledgeBase(new KnowledgeBaseSync(knowledgeId, columnType, knowledgeBase.getPrivated(), EActionType.EAdd.getValue()));
            logger.error("知识基础表插入失败！失败原因：\n"+e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //知识来源表插入
        if (knowledgeReference != null) {
            try {
                knowledgeReference.setId(knowledgeId);
                knowledgeReference.setKnowledgeId(knowledgeId);
                this.knowledgeReferenceDao.insert(knowledgeReference);
            } catch (Exception e) {
                logger.error("知识引用表插入失败！失败原因：\n" + e.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
            }
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
    }

    @Override
    public Knowledge insert(Knowledge detail)
    {
        return insertKnowledgeDetail(detail);
    }

    @Override
    public InterfaceResult insert(List<Knowledge> knowledgeList, final int type)
    {
        if (CollectionUtils.isEmpty(knowledgeList)) {
            logger.error("Knowledge list is empty, so skip to save..");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        try {
            List<Knowledge> savedList = this.knowledgeMongoDao.insertList(knowledgeList, type);
            if (CollectionUtils.isNotEmpty(savedList)) {
                logger.info("End insert knowledge List to mongo success. size: " + savedList.size());
            }
            else {
                logger.info("End insert knowledge List to mongo failed." );
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
            }
        } catch (Exception ex) {
            logger.error("Create knowledge detail failed: error:  " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public Knowledge update(Knowledge detail) throws Exception
    {
        return updateKnowledgeDetail(detail);
    }

    @Override
    public InterfaceResult<Knowledge> update(DataCollect DataCollect) throws Exception {

        Knowledge knowledgeDetail = DataCollect.getKnowledgeDetail();
        KnowledgeReference knowledgeReference = DataCollect.getReference();

        //knowledgeMongo.createContendDesc();
        //知识详细表更新
        Knowledge updatedKnow = updateKnowledgeDetail(knowledgeDetail);
        if (updatedKnow == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识更新失败");
        }
        logger.info("update knowledgeDetail: " + updatedKnow);
        //Update knowledge detail
        DataCollect.setKnowledgeDetail(updatedKnow);
        KnowledgeBase knowledge = DataCollect.generateKnowledge();
        Long knowledgeId = knowledgeDetail.getId();
        short columnType = knowledge.getType();

        //知识简表更新
        try {
            knowledge.setPrivated(permissionValue(DataCollect));
            this.knowledgeMysqlDao.update(knowledge);
        } catch (Exception e) {
            knowledgeMongoDao.backupKnowledgeBase(new KnowledgeBaseSync(knowledgeId, columnType, knowledge.getPrivated(), EActionType.EUpdate.getValue()));
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

        InterfaceResult<Knowledge> result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        result.setResponseData(updatedKnow);
        logger.info("知识来更新成功\n");
        return result;
    }

    @Override
    public boolean updateKnowledge(DataCollect DataCollect) throws Exception {

        KnowledgeBase base = DataCollect.getKnowledge();
        Knowledge knowledgeDetail = DataCollect.getKnowledgeDetail();

        updateKnowledgeDetail(knowledgeDetail);

        //知识简表更新
        try {
            if (base != null) {
                this.knowledgeMysqlDao.update(base);
            }
        } catch (Exception e) {
            logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public InterfaceResult deleteByKnowledgeId(long knowledgeId, int columnType) throws Exception
    {
        //知识详细表删除
        try {
            boolean result = this.knowledgeMongoDao.deleteByIdAndColumnId(knowledgeId, columnType);
            if (!result) {
                logger.error("知识详细表删除失败: knowledgeId: " + knowledgeId + " columnType: " + columnType);
                Knowledge detail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnType);
                if (detail == null) {
                    logger.info("Maybe some reason, this knowledge had been deleted, so continue delete other info regards this knowledge. knowledgeId: " + knowledgeId + " columnType: " + columnType);
                } else {
                    return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识详细删除失败!");
                }
            }
        } catch (Exception ex) {
            logger.error("知识详细表删除失败！失败原因：\n"+ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识详细删除失败!");
        }

        //知识简表删除
        try {
            int result = this.knowledgeMysqlDao.deleteByKnowledgeId(knowledgeId);
            if (result <= 0) {
                logger.error("delete knowledge base failed. knowledgeId: " + knowledgeId);
            }
        } catch (Exception e) {
            //knowledgeMongoDao.backupKnowledgeBase(new KnowledgeBaseSync(knowledgeId, (short)columnType, (short)0, EActionType.EDelete.getValue()));
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
        //大数据MQ推送删除
        /*
		try {
			bigDataService.deleteMessage(knowledgeId, columnId, userId);
		} catch (Exception e) {
			logger.error("知识MQ推送失败！失败原因：\n"+e.getMessage());
			return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
		}*/

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
    }

    @Override
    public InterfaceResult batchDeleteByKnowledgeIds(List<Long> knowledgeIds, int columnType) throws Exception {

        List<Knowledge> oldKnowledgeMongoList = this.knowledgeMongoDao.getByIdsAndColumnId(knowledgeIds, columnType);
        if (CollectionUtils.isEmpty(oldKnowledgeMongoList)) {
            logger.warn("get knowledge detail list failed, maybe these knowledge have been deleted. knowledgeIds：" + knowledgeIds + " columnType: " + columnType);
        }
        else {
            //知识详细表删除
            boolean result = this.knowledgeMongoDao.deleteByIdsAndColumnType(knowledgeIds, columnType);
            if (!result) {
                logger.error("delete knowledge detail failed. knowledgeIds：" + knowledgeIds + " columnType: " + columnType);
            }
        }

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
		}*/

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeIds);
    }

    @Override
    public Knowledge getDetailById(long knowledgeId, int columnType) throws Exception {

        Knowledge knowledgeDetail = this.knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnType);
        if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: " + knowledgeId +", columnType: " + columnType);
        } else {
            //Add knowledge property
            filterKnowledge(knowledgeDetail);
            final String knowledgeContent = knowledgeDetail.getContent() + suffix;
            knowledgeDetail.setContent(knowledgeContent);
        }

        return knowledgeDetail;
    }

    @Override
    public DataCollect getKnowledge(long knowledgeId,int columnType) throws Exception
    {
        Knowledge knowledgeDetail = getDetailById(knowledgeId, columnType);
        if (knowledgeDetail == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: " + knowledgeId +", columnType: " + columnType);
        }

        KnowledgeBase knowledgeBase = this.knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
        if (knowledgeBase == null) {
            logger.error("Can't get knowledge detail by, knowledgeId: " + knowledgeId +", columnId: " + columnType);
        }

        if (knowledgeBase != null || knowledgeDetail != null) {
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
        /*if (userId ==0) {
            return this.knowledgeMongoDao.c
        }*/
        return this.knowledgeMysqlDao.getKnowledgeCount(userId);
    }

    @Override
    public List<KnowledgeBase> getKnowledgeNoDirectory(long userId,int start,int size) throws Exception
    {
        //return this.knowledgeMysqlDao.getKnowledgeNoDirectory(userId, start, size);
        List<Knowledge> detailList = null;
        try {
            detailList = knowledgeMongoDao.getNoDirectory(userId, start, size);
        } catch (Exception ex) {
            logger.error("get knowledge detail list failed: userId: {}", userId);
        }
        if (detailList != null && detailList.size() >0) {
            List<KnowledgeBase> baseList = new ArrayList<KnowledgeBase>(detailList.size());
            for (Knowledge detail : detailList) {
                KnowledgeBase base = DataCollect.generateKnowledge(detail, (short)0);
                baseList.add(base);
            }
            return baseList;
        }
        return null;
    }

    public List<KnowledgeBaseSync> getBackupKnowledgeBase(int start, int size)
    {
        return knowledgeMongoDao.getBackupKnowledgeBase(start, size);
    }

    public boolean syncKnowledgeBase(final KnowledgeBaseSync knowledgeSync) throws Exception
    {
        if (knowledgeSync == null) {
            logger.error("knowledgeSync is null, please check again..");
            return false;
        }
        switch (knowledgeSync.getAction()) {
            case 1:
                return syncKnowledgeBase(knowledgeSync, true);
            case 2:
                return syncKnowledgeBase(knowledgeSync, false);
            case 3:
            {
                try {
                    return knowledgeMysqlDao.deleteByKnowledgeId(knowledgeSync.getId()) > 0;
                } catch (Throwable e) {
                    logger.error("delete Knowledge base failed:  knowledgeId: {} type: {}", knowledgeSync.getId(), knowledgeSync.getType());
                    e.printStackTrace();
                    return false;
                }
            }
            default:
            {
                logger.error("Unknown Knowledge Sync Action..");
                return false;
            }
        }
    }

    private boolean syncKnowledgeBase(final KnowledgeBaseSync knowledgeSync, final boolean isAdd)
    {
        boolean result = false;
        Knowledge detail = knowledgeMongoDao.getByIdAndColumnId(knowledgeSync.getId(), knowledgeSync.getType());
        if (detail != null) {
            KnowledgeBase base = DataCollect.generateKnowledge(detail, knowledgeSync.getType());
            base.setPrivated(knowledgeSync.getPrivated());
            KnowledgeBase newBase = null;
            try {
                if (isAdd) {
                    newBase = knowledgeMysqlDao.insert(base);
                }
                else {
                    newBase = knowledgeMysqlDao.update(base);
                }
                logger.info("sync knowledge base success. knowledgeId: " + detail.getId() + ", columnType: " + detail.getColumnType() + ", isAdd: " + isAdd);
            } catch (Throwable e) {
                logger.error("Sync knowledge base failed. knowledgeId: " + detail.getId() + ", columnType: "+detail.getColumnType() + ", isAdd: "+isAdd);
                e.printStackTrace();
                return false;
            }

            //Make sure insert success;
            try {
                if (newBase != null) {
                    knowledgeMongoDao.deleteBackupKnowledgeBase(knowledgeSync.getId());
                    logger.info("delete backup knowledge. knowledgeId: " + detail.getId() + ", columnType: "+detail.getColumnType());
                }
                else {
                    return false;
                }
            } catch (Throwable e) {
                logger.error("delete backup knowledge base failed. knowledgeId: " + detail.getId() + ", columnType: "+detail.getColumnType() + ", isAdd: "+isAdd);
                e.printStackTrace();
                return false;
            }
            result = true;
        }
        return result;
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

    private Knowledge insertKnowledgeDetail(Knowledge detail)
    {
        //知识详细信息插入
        Knowledge savedDetail = null;
        try {
            filterKnowledge(detail);
            detail.setStatus(4);
            this.knowledgeMongoDao.insert(detail);
            logger.info("End insert knowledge to mongo. knowledgeId: " + detail.getId());
            //Get from mongo, make sure save success..
            final int columnType = KnowledgeUtil.parserColumnId(detail.getColumnType());
            savedDetail = this.knowledgeMongoDao.getByIdAndColumnId(detail.getId(), columnType);
        } catch (Exception ex) {
            logger.error("Create knowledge detail failed: error:  " + ex.getMessage());
            return null;
        }

        if (savedDetail == null) {
            logger.error("Create knowledge detail failed, knowledgeId: " + detail.getId());
            return null;
        }

        return savedDetail;
    }

    private Knowledge updateKnowledgeDetail(Knowledge detail)
    {
        //知识详细表更新
        if (detail == null) {
            logger.info("Knowledge Detail is null, so skip to save.");
            return null;
        }
        filterKnowledge(detail);
        Knowledge updateDetail = this.knowledgeMongoDao.update(detail);
        if (updateDetail != null) {
            logger.info("update knowledge Detail success, knowledgeId: " + updateDetail.getId());
        }
        return updateDetail;
    }

    private void filterKnowledge(Knowledge detail)
    {
        StringBuilder sb = new StringBuilder(detail.getContent());
        final int startIndex = sb.indexOf("版权归原作者所有，金桐网依法保护版权");
        final int endIndex = sb.indexOf(endContent) + endLength;
        if (startIndex > 0) {
            final String filterContent = sb.replace(startIndex, endIndex, "").toString();
            detail.setContent(filterContent);
        }
    }
}
