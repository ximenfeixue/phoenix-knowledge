package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCollectDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseSync;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.EModuleType;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.model.mobile.EActionType;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private KnowledgeCollectDao knowledgeCollectDao;

    private static final String endContent = "若涉及版权或第三方网络侵权问题，请及时与我公司服务中心联系。";
    private static final int endLength = endContent.length();
    private static final String suffix = "</br>版权归原作者所有，金桐网依法保护版权，保护权利人合法权益。金桐网部分文章、知识等若未能第一时间与作者取得联系或标注原始出处，请谅解。若涉及版权或第三方网络侵权问题，请及时与我公司服务中心联系。";

    @Override
    public InterfaceResult<Long> insert(DataCollect data) {
        //知识详细信息插入
        data.initPermission();
        Knowledge savedDetail = insertKnowledgeDetail(data.getKnowledgeDetail());
        if (savedDetail == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        long knowledgeId = savedDetail.getId();

        //知识基础表插入
        KnowledgeBase base = data.generateKnowledge();
        try {
            this.knowledgeMysqlDao.insert(base);
            logger.info("insert knowledge to tb_knowledge_base success. knowledgeId: " + base.getId());
        } catch (Throwable e) {
            short columnType = KnowledgeUtil.parserShortType(savedDetail.getColumnType());
            knowledgeMongoDao.backupKnowledgeBase(new KnowledgeBaseSync(knowledgeId, columnType, base.getPrivated(), EActionType.EAdd.getValue()));
            logger.error("知识基础表插入失败！失败原因：" + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //知识来源表插入
        KnowledgeReference reference = data.getReference();
        if (reference != null) {
            try {
                reference.setId(knowledgeId);
                reference.setKnowledgeId(knowledgeId);
                this.knowledgeReferenceDao.insert(reference);
            } catch (Throwable e) {
                logger.error("知识引用表插入失败！失败原因： " + e.getMessage());
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
                logger.info("insert knowledge List to mongo success. size: " + savedList.size());
            }
            else {
                logger.info("insert knowledge List to mongo failed." );
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
            }
        } catch (Exception ex) {
            logger.error("create knowledge detail failed: error:  " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public Knowledge update(Knowledge detail) {
        return updateKnowledgeDetail(detail, -1);
    }

    @Override
    public InterfaceResult<Knowledge> update(DataCollect dataCollect) {

        dataCollect.initPermission();
        Knowledge detail = dataCollect.getKnowledgeDetail();
        //knowledgeMongo.createContendDesc();
        //知识详细表更新
        Knowledge updatedKnow = updateKnowledgeDetail(detail, dataCollect.getOldType());
        if (updatedKnow == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION, "知识更新失败");
        }
        logger.info("update knowledge Detail, knowledgeId: " + updatedKnow.getId());

        //Update knowledge base
        dataCollect.setKnowledgeDetail(updatedKnow);
        KnowledgeBase knowledge = dataCollect.generateKnowledge();
        final long knowledgeId = detail.getId();
        final short columnType = knowledge.getType();
        //知识简表更新
        try {
            this.knowledgeMysqlDao.update(knowledge);
        } catch (Exception e) {
            knowledgeMongoDao.backupKnowledgeBase(new KnowledgeBaseSync(knowledgeId, columnType, knowledge.getPrivated(), EActionType.EUpdate.getValue()));
            logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //知识来源表更新
        KnowledgeReference reference = dataCollect.getReference();
        if (reference != null) {
            try {
                reference.setId(knowledgeId);
                reference.setKnowledgeId(knowledgeId);
                this.knowledgeReferenceDao.update(reference);
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
    public boolean updateKnowledge(DataCollect dataCollect) {

        KnowledgeBase base = dataCollect.getKnowledge();
        Knowledge knowledgeDetail = dataCollect.getKnowledgeDetail();

        updateKnowledgeDetail(knowledgeDetail, dataCollect.getOldType());
        //知识简表更新
        try {
            if (base != null) {
                base.setStatus((short) defaultStatus);
                this.knowledgeMysqlDao.update(base);
            }
        } catch (Exception e) {
            logger.error("知识基础表更新失败！失败原因：\n"+e.getMessage());
            return false;
        }

        return true;
    }

    public boolean updatePermission(Permission perm) {
        if (perm == null) {
            logger.error("permission info is null, so skip.");
            return false;
        }

        try {
            final long knowledgeId = perm.getResId();
            KnowledgeBase base = knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
            if (base == null) {
                logger.error("can 't find knowledge base by id: " + knowledgeId);
                return false;
            }
            final short columnType = base.getType();
            /*
            Knowledge detail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnType);
            if (detail == null) {
                logger.error("can 't find knowledge detail by id: " + knowledgeId + " type: " + columnType);
                return false;
            }*/
            short privated = DataCollect.privated(perm);
            base.setPrivated(privated);
            boolean result = knowledgeMysqlDao.update(base);
            if (!result) {
                logger.error("update knowledge base failed. by id: " + knowledgeId + " type: " + columnType);
                return false;
            }
            knowledgeMongoDao.updatePrivated(knowledgeId, columnType, privated);

            privated = DataCollect.privated(perm, false);
            knowledgeCollectDao.updateCollectedKnowledgePrivate(knowledgeId, columnType, privated);

        } catch (Exception e) {
            logger.error("update knowledge permsiion failed, id: " + perm.getResId());
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean addTag(long userId, long knowledgeId, int type, List<Long> tagIdList) {
        return this.knowledgeMongoDao.addTag(userId, knowledgeId, type, tagIdList);
    }

    @Override
    public boolean addDirectory(long userId, long knowledgeId, int type, List<Long> directoryIdList) {
        return this.knowledgeMongoDao.addDirectory(userId, knowledgeId, type, directoryIdList);
    }

    @Override
    public boolean updateTag(long userId, long knowledgeId, int type, List<Long> tagIdList) {
        return this.knowledgeMongoDao.updateIds(userId, knowledgeId, type, tagIdList, EModuleType.ETag);
    }

    @Override
    public boolean updateDirectory(long userId, long knowledgeId, int type, List<Long> directoryIdList) {
        return this.knowledgeMongoDao.updateIds(userId, knowledgeId, type, directoryIdList, EModuleType.EDirectory);
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

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeId);
    }

    @Override
    public boolean deleteTag(long userId, long knowledgeId, int type, List<Long> idList) throws Exception {
        return this.knowledgeMongoDao.deleteTag(userId, knowledgeId, type, idList);
    }

    @Override
    public boolean deleteDirectory(long userId, long knowledgeId, int type, List<Long> idList) throws Exception {
        return this.knowledgeMongoDao.deleteDirectory(userId, knowledgeId, type, idList);
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
            this.knowledgeMysqlDao.batchDeleteByIds(knowledgeIds);
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

        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeIds);
    }

    @Override
    public InterfaceResult logicDelete(List<Long> knowledgeIds, final int type)
    {
        if (CollectionUtils.isEmpty(knowledgeIds)) {
            logger.warn("knowledge is is null, skip logic delete" + knowledgeIds + " columnType: " + type);
        }

        //知识详细表删除
        boolean result = this.knowledgeMongoDao.logicDeleteByIdsType(knowledgeIds, type);
        if (!result) {
            logger.error("logic delete knowledge failed. knowledgeIds：" + knowledgeIds + " columnType: " + type);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }

        //知识简表删除
        try {
            this.knowledgeMysqlDao.logicDeleteByIds(knowledgeIds);
        } catch (Exception e) {
            logger.error("logic delete knowledge base failed： error: " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, true);
    }

    @Override
    public InterfaceResult logicRecovery(List<Long> knowledgeIds, final int type)
    {
        if (CollectionUtils.isEmpty(knowledgeIds)) {
            logger.warn("knowledge is is null, skip logic delete" + knowledgeIds + " columnType: " + type);
        }

        //知识详细表删除
        boolean result = this.knowledgeMongoDao.logicRecoveryByIdsType(knowledgeIds, type);
        if (!result) {
            logger.error("logic recovery knowledge failed. knowledgeIds：" + knowledgeIds + " columnType: " + type);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }

        //知识简表删除
        try {
            this.knowledgeMysqlDao.logicRecoveryByIds(knowledgeIds);
        } catch (Exception e) {
            logger.error("logic recovery knowledge base failed： error: " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, true);
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
    public int countByCreateUserId(long userId) throws Exception
    {
        /*if (userId ==0) {
            return this.knowledgeMongoDao.c
        }*/
        return this.knowledgeMysqlDao.getKnowledgeCount(userId);
    }


    @Override
    public List<KnowledgeBase> getByCreateUserId(long userId,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByCreateUserId(userId, start, size);
    }

    @Override
    public int countByCreateUserName(String userName) throws Exception
    {
        /*if (userId ==0) {
            return this.knowledgeMongoDao.c
        }*/
        return this.knowledgeMysqlDao.countByCreateUserName(userName);
    }


    @Override
    public List<KnowledgeBase> getByCreateUserName(String userName,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByCreateUserName(userName, start, size);
    }

    public int countByTitle(String title) throws Exception
    {
        return this.knowledgeMysqlDao.countByTitle(title);
    }


    public List<KnowledgeBase> getByTitle(String title,int start, int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByTitle(title, start, size);
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
    public int getCountByUserIdKeyWord(long userId,String keyWord) throws Exception
    {
        return this.knowledgeMysqlDao.getCountByUserIdKeyWord(userId, keyWord);
    }

    @Override
    public List<KnowledgeBase> getByUserIdKeyWord(long userId,String keyWord,int start,int size) throws Exception
    {
        return this.knowledgeMysqlDao.getByUserIdKeyWord(userId, keyWord, start, size);
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

    @Override
    public void addTopKnowledge(List<Long> ids, short type) {
        this.knowledgeMongoDao.addTopKnowledge(ids, type);
    }

    @Override
    public void deleteTopKnowledge(List<Long> ids, short type) {
        this.knowledgeMongoDao.deleteTopKnowledge(ids, type);
    }

    @Override
    public List<KnowledgeBase> getTopKnowledgeByPage(final short type, final int page, int size) {
        return this.knowledgeMongoDao.getTopKnowledgeByPage(type, page, size);
    }

    private boolean syncKnowledgeBase(final KnowledgeBaseSync knowSync, final boolean isAdd)
    {
        boolean result = false;
        Knowledge detail = knowledgeMongoDao.getByIdAndColumnId(knowSync.getId(), knowSync.getType());
        final String knowInfo = "knowledgeId: " + knowSync.getId() + ", columnType: " + knowSync.getType() + ", isAdd: " + isAdd;
        if (detail != null) {
            KnowledgeBase base = DataCollect.generateKnowledge(detail, knowSync.getType());
            base.setPrivated(knowSync.getPrivated());
            KnowledgeBase newBase = null;
            try {
                if (isAdd) {
                    newBase = knowledgeMysqlDao.insert(base);
                }
                else {
                    if (knowledgeMysqlDao.update(base)) {
                        newBase = base;
                    }
                }
                logger.info("sync knowledge base success. " + knowInfo);
            } catch (Throwable e) {
                logger.error("Sync knowledge base failed. " + knowInfo);
                e.printStackTrace();
                return false;
            }

            //Make sure insert/update success;
            if (newBase != null) {
                result = deleteBackupKnowledgeBase(knowSync.getId(), knowInfo);
            }
        } else {
            logger.info("Knowledge can't find, maybe have been deleted. " + knowInfo);
            //result = deleteBackupKnowledgeBase(knowSync.getId(), knowInfo);
        }
        return result;
    }

    private boolean deleteBackupKnowledgeBase(final long knolwedgeId, final String knowInfo) {
        try {
            knowledgeMongoDao.deleteBackupKnowledgeBase(knolwedgeId);
            logger.info("delete backup knowledge success. " + knowInfo);
        } catch (Throwable e) {
            logger.error("delete backup knowledge base failed. " + knowInfo);
            e.printStackTrace();
            return false;
        }
        return true;
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

    private Knowledge insertKnowledgeDetail(Knowledge detail)
    {
        //知识详细信息插入
        Knowledge savedDetail = null;
        try {
            filterKnowledge(detail);
            detail.setStatus(4);
            this.knowledgeMongoDao.insert(detail);
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
        logger.info("Insert knowledge detail success. knowledgeId: " + detail.getId());

        return savedDetail;
    }

    private Knowledge updateKnowledgeDetail(Knowledge detail, int oldType)
    {
        //知识详细表更新
        if (detail == null) {
            logger.info("Knowledge Detail is null, so skip to save.");
            return null;
        }
        filterKnowledge(detail);
        Knowledge updateDetail = this.knowledgeMongoDao.update(detail, oldType);
        if (updateDetail != null) {
            logger.info("update knowledge Detail success, knowledgeId: " + updateDetail.getId());
        }
        return updateDetail;
    }

    private void filterKnowledge(Knowledge detail)
    {
        detail.setStatus(defaultStatus);
        String orgContent = detail.getContent();
        if (orgContent != null) {
            List<String> prefixList = Arrays.asList("<br>", "</br>", "<p>", "</p>", "");
            for (String prefix : prefixList) {
                String filterContent = filerString(orgContent, prefix);
                if (filterContent != null) {
                    detail.setContent(filterContent);
                    return;
                }
            }
        }
    }


    private String filerString(final String originContent, final String prefix)
    {
        final String filterString = prefix + "版权归原作者所有，金桐网依法保护版权";
        final int startIndex = originContent.indexOf(filterString);
        if (startIndex > 0) {
            logger.info("filter string: " + filterString + " done.");
            return originContent.substring(0, startIndex);
        }
        return null;
    }
}
