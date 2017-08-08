package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.dynamic.model.*;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.Page;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.task.BigDataSyncTask;
import com.ginkgocap.ywxt.knowledge.task.DataSyncTask;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.StringUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnSelfService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oem on 4/6/17.
 */
public abstract class BaseKnowledgeController extends BaseController {

    @Autowired
    protected KnowledgeService knowledgeService;

    @Autowired
    protected KnowledgeBatchQueryService knowledgeBatchQueryService;

    @Autowired
    protected ColumnSelfService columnSelfService;

    @Autowired
    protected AssociateServiceLocal associateServiceLocal;

    @Autowired
    protected TagServiceLocal tagServiceLocal;

    @Autowired
    protected DirectoryServiceLocal directoryServiceLocal;

    @Autowired
    protected PermissionServiceLocal permissionServiceLocal;

    @Autowired
    protected DynamicNewsServiceLocal dynamicNewsServiceLocal;

    @Autowired
    protected BigDataSyncTask bigDataSyncTask;

    @Autowired
    protected DataSyncTask dataSyncTask;

    protected InterfaceResult create(HttpServletRequest request, final User user)
    {
        long userId = user.getId();
        String requestJson = this.getBodyParam(request);
        DataCollect data = KnowledgeUtil.getDataCollect(requestJson);
        if (data == null || data.getKnowledgeDetail() == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"知识详情格式错误或者为空");
        }

        detailFaultTolerant(data.getKnowledgeDetail());
        //convertKnowledgeContent(detail, detail.getContent(), null, null, null, isWeb(request));

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            data.serUserInfo(user);
            result = this.knowledgeService.insert(data);
        } catch (Exception e) {
            logger().error("Insert knowledge failed : " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
        }

        if (result == null || result.getNotification()== null || !"0".equals(result.getNotification().getNotifCode())) {
            logger().error("Insert knowledge failed!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
        }
        long knowledgeId = Long.valueOf(result.getResponseData().toString());

        Knowledge detail = data.getKnowledgeDetail();
        detail.setId(knowledgeId);

        if (!tagServiceLocal.saveTagSource(userId, detail)) {
            logger().error("Save Tag info failed, userId: {} knowledgeId: ", userId, knowledgeId);
        }

        List<Long> successIds = directoryServiceLocal.saveDirectorySource(userId, detail);
        if (successIds != null && successIds.size() >0) {
            logger().error("Save Directory success. userId: {} knowledgeId: {}, plan size: {}, success size: {}",
                    userId, knowledgeId, detail.getDirectorys().size(), successIds.size());
        }
        else {
            logger().error("Save Directory info failed, userId: {} knowledgeId: {}", userId, knowledgeId);
        }

        //save asso information
        //TODO: If this step failed, how to do ?
        Map<Long, List<Associate>> assoMap = null;
        try {
            assoMap = associateServiceLocal.createAssociate(data.getAsso(), knowledgeId, user);
        }catch (Throwable e) {
            logger().error("Insert associate failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        boolean syncToDynamic = false;
        Permission permission = permissionServiceLocal.savePermissionInfo(userId, knowledgeId, data.getPermission());
        if (permission != null) {
            logger().info("save knowledge permission success. userId: " + userId + " knowledgeId: " + knowledgeId);
            syncToDynamic = (permission.getPublicFlag() != null && permission.getPublicFlag() != 0 && data.getUpdateDynamic() == 1);
        }

        //Sync to dynamic news
        if (syncToDynamic) {
            String dynamicNews = createDynamicNews(detail, user);
            try {
                dynamicNewsServiceLocal.addDynamicToAll(dynamicNews, userId, assoMap);
            } catch (Exception ex) {
                logger().info("sync knowledge to dynamic failed. userId: " + userId + " knowledgeId: " + knowledgeId + " error: " + ex.getMessage());
            }
            /*
            DataSync dataSync = createDynamicNewsDataSync(knowledgeDetail, user.isVirtual());
            if (dataSyncTask != null) {
                dataSyncTask.saveDataNeedSync(dataSync);
            }*/
        }

        //send new knowledge to bigdata
        bigDataSyncTask.addToMessageQueue(BigDataSyncTask.KNOWLEDGE_INSERT, data.toBigData());

        logger().info(".......create knowledge success......");
        return result;
    }

    protected InterfaceResult updateKnowledge(HttpServletRequest request, final User user) throws Exception
    {

        long userId = user.getId();
        String requestJson = this.getBodyParam(request);
        DataCollect data = KnowledgeUtil.getDataCollect(requestJson);
        if (data == null) {
            logger().error("request data is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        Knowledge detail = data.getKnowledgeDetail();
        if (detail == null) {
            logger().error("request knowledgeDetail is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        final int newType = KnowledgeUtil.parserColumnId(detail.getColumnType());
        if (newType == data.getOldType()) {
            logger().info("as oldType and newType same, so set oldType = 0");
            data.setOldType(0);
        }

        String columnType = data.getOldType() > 0 ? String.valueOf(data.getOldType()) : detail.getColumnType();
        long knowledgeId = detail.getId();
        if (!permissionServiceLocal.canUpdate(knowledgeId, columnType, userId)) {
            logger().error("permission validate failed, please check if user have permission!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION, "没有权限编辑知识!");
        }
        detailFaultTolerant(detail);
        //convertKnowledgeContent(detail, detail.getContent(), null, null, null, isWeb(request));

        InterfaceResult<Knowledge> result = null;
        try {
            data.serUserInfo(user);
            result = this.knowledgeService.update(data);
        } catch (Exception e) {
            logger().error("知识更新失败！失败原因： "+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        if (result == null || !CommonResultCode.SUCCESS.getCode().equals(result.getNotification().getNotifCode())) {
            logger().error("知识更新失败, knowledgeId: " + knowledgeId + " columnType: " + columnType);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        Knowledge updatedDetail = result.getResponseData();
        if (updatedDetail == null) {
            logger().error("知识更新失败, updated knowledge Detail is null");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        Permission permission = permissionServiceLocal.updatePermissionInfo(userId, knowledgeId, data.getPermission());
        if (permission != null) {
            logger().debug("update knowledge permission success. userId: " + userId + ", knowledgeId: " + knowledgeId);
        }

        //Update tag info
        tagServiceLocal.updateTagSource(userId, detail);

        //Update Directory info
        directoryServiceLocal.updateDirectorySource(userId, detail);

        //Update Assso info
        associateServiceLocal.updateAssociate(knowledgeId, user, data);

        //send new knowledge to bigdata
        data.setKnowledgeDetail(updatedDetail);
        bigDataSyncTask.addToMessageQueue(BigDataSyncTask.KNOWLEDGE_UPDATE, data.toBigData());

        dataSyncTask.saveDataNeedSync(new DataSync(0,permission));
        logger().info("update knowledge success. knowledgeId: " + knowledgeId + " userId: " + userId);
        return result;
    }

    protected Knowledge detailFaultTolerant(Knowledge detail)
    {
        if (StringUtil.inValidString(detail.getColumnType())) {
            logger().warn("column type is null, so set a default value");
            detail.setColumnType(String.valueOf(KnowledgeType.ENews.value()));
        } else {
            int columnType = KnowledgeUtil.parserShortType(detail.getColumnType());
            columnType = KnowledgeType.knowledgeType(columnType).value();
            detail.setColumnType(String.valueOf(columnType));
        }

        if (StringUtils.isBlank(detail.getColumnid())) {
            logger().warn("column Id is null, so set a default value");
            detail.setColumnid(String.valueOf(KnowledgeType.ENews.value()));
        } else {
            long columnId = KnowledgeUtil.parserStringIdToLong(detail.getColumnid());
            if (columnId <= 0) {
                logger().warn("column id: " + detail.getColumnid() + " is invalidated, so set a default value. ");
                columnId = KnowledgeType.ENews.value();
                detail.setColumnid(String.valueOf(columnId));
            }
        }

        if (StringUtils.isBlank(detail.getCpathid())) {
            String columnPath = null;
            try {
                long columnId = KnowledgeUtil.parserStringIdToLong(detail.getColumnid());
                ColumnSelf column = columnSelfService.selectByPrimaryKey(columnId);
                if (column != null) {
                    columnPath = column.getPathName();
                }
            } catch (Throwable ex) {
                logger().error("Query column failed. columnId: " + detail.getColumnid() + " error: " + ex.getMessage());
            }

            if (columnPath == null) {
                logger().error("column path is null, so set a default value");
                columnPath =  KnowledgeType.ENews.typeName();
            }
            detail.setCpathid(columnPath);
        }

        if (StringUtils.isEmpty(detail.getCreatetime())) {
            detail.setCreatetime(String.valueOf(System.currentTimeMillis()));
        }
        if (StringUtils.isEmpty(detail.getModifytime())) {
            detail.setModifytime(String.valueOf(System.currentTimeMillis()));
        }

        //set status to default 4
        detail.setStatus(4);

        return detail;
    }

    protected DataSync createDynamicNewsDataSync(Knowledge detail, User user)
    {
        DataSync data = new DataSync(0, createDynamicNewsObj(detail, user));
        return data;
    }

    protected String createDynamicNews(Knowledge detail,User user) {
        DynamicNews dynamic = createDynamicNewsObj(detail, user);
        return KnowledgeUtil.writeObjectToJson(dynamic);
    }

    protected Permission getPermission(final long userId, final long knowledgeId) {
        if (userId == 0 || userId ==1) {
            logger().info("create default public permission for userId: " + userId + " knowledgeId: " + knowledgeId);
            return DataCollect.defaultPublicPermission(userId, knowledgeId);
        }
        Permission permission = permissionServiceLocal.getPermissionInfo(knowledgeId);
        if (permission == null) {
            //set a default value
            logger().info("Can't get knowledge permission, so set a default value. knowledgeId: " + knowledgeId);
            return DataCollect.defaultPermission(userId, knowledgeId, 0);
        }
        return permission;
    }

    protected InterfaceResult<Page<KnowledgeBase>> knowledgeListPage(long total, int num, int size, List<KnowledgeBase> knowledgeBaseItems)
    {
        Page<KnowledgeBase> page = new Page<KnowledgeBase>();
        page.setTotalCount(total);
        page.setPageNo(num);
        page.setPageSize(size);
        page.setList(setReadCount(knowledgeBaseItems));
        //page.setList(knowledgeBaseItems);
        return InterfaceResult.getSuccessInterfaceResultInstance(page);
    }

    protected List<KnowledgeBase> setReadCount(List<KnowledgeBase> baseList) {
        if (CollectionUtils.isEmpty(baseList)) {
            return null;
        }
        for (KnowledgeBase knowledgeItem : baseList) {
            KnowledgeCount knowledgeCount = knowledgeCountService.getKnowledgeCount(knowledgeItem.getKnowledgeId());
            long readCount = knowledgeCount != null ? knowledgeCount.getClickCount() : 0L;
            knowledgeItem.setReadCount((int)readCount);
        }
        return baseList;
    }

    protected InterfaceResult<Page<KnowledgeBaseExt>> knowledgeExtListPage(long total, int num, int size, List<KnowledgeBase> knowledgeBaseItems)
    {
        Page<KnowledgeBaseExt> page = new Page<KnowledgeBaseExt>();
        page.setTotalCount(total);
        page.setPageNo(num);
        page.setPageSize(size);
        page.setList(setBaseExtReadCount(knowledgeBaseItems));
        //page.setList(knowledgeBaseItems);
        return InterfaceResult.getSuccessInterfaceResultInstance(page);
    }

    protected List<KnowledgeBaseExt> setBaseExtReadCount(List<KnowledgeBase> baseList) {
        if (CollectionUtils.isEmpty(baseList)) {
            return null;
        }

        List<KnowledgeBaseExt> baseExtList = new ArrayList<KnowledgeBaseExt>(baseList.size());
        for (KnowledgeBase baseItem : baseList) {
            KnowledgeCount knowledgeCount = knowledgeCountService.getKnowledgeCount(baseItem.getKnowledgeId());
            long readCount = knowledgeCount != null ? knowledgeCount.getClickCount() : 0L;
            baseItem.setReadCount((int)readCount);
            KnowledgeBaseExt baseExt = new KnowledgeBaseExt();
            baseExt.clone(baseItem);
            baseExt.setTypeName(KnowledgeType.knowledgeType(baseItem.getType()).typeName());
            baseExtList.add(baseExt);
        }
        return baseExtList;
    }

    private DynamicNews createDynamicNewsObj(Knowledge detail, User user)
    {
        DynamicNews dynamic = new DynamicNews();
        dynamic.setType("11"); //创建知识
        dynamic.setLowType(detail.getColumnType());
        dynamic.setTargetId(detail.getId());
        dynamic.setTitle(detail.getTitle());
        //dynamic.setContent(knowledge.getContent());
        dynamic.setContentPath(detail.getS_addr());
        dynamic.setCreaterId(detail.getCid());
        String clearContent = HtmlToText.html2Text(detail.getContent());
        clearContent = clearContent.length() > 250 ? clearContent.substring(0,250) : clearContent;
        dynamic.setClearContent(clearContent);
        dynamic.setPicPath(detail.getPic());
        dynamic.setCreaterName(detail.getCname());
        dynamic.setCtime(KnowledgeUtil.parserTimeToLong(detail.getCreatetime()));
        //dynamic.setDemandCount());
        //dynamic.setId();
        dynamic.setImgPath(detail.getPic());
        dynamic.setKnowledgeCount(0);
        String createType = user.isVirtual() ? "2" : "1";
        dynamic.setCreateType(createType);
        dynamic.setScope(String.valueOf(0));
        Location location = new Location();
        location.setDetailName("");
        location.setDimension("");
        location.setMobile("");
        location.setName("");
        location.setSecondLevel("");
        location.setType("");
        dynamic.setLocation(location);
        dynamic.setPicPath(user.getPicPath());
        logger().info("create knowledge, set userPicPath: "+user.getPicPath());
        dynamic.setPeopleRelation(new ArrayList<RelationUserInfo>(0));
        dynamic.setComments(new ArrayList<DynamicComment>(0));
        dynamic.setPicturePaths(new ArrayList<Picture>(0));
        //dynamic.setVirtual(knowledge.getVirtual());

        return dynamic;
    }
}
