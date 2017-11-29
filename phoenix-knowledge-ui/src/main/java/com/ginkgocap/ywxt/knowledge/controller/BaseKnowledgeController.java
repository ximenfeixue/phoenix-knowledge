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
import com.ginkgocap.ywxt.track.entity.constant.BusinessModelEnum;
import com.ginkgocap.ywxt.track.entity.constant.OptTypeEnum;
import com.ginkgocap.ywxt.track.entity.util.BusinessTrackUtils;
import com.ginkgocap.ywxt.user.model.SyncSwitch;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.SyncSourceService;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.ywxt.im.model.ResourceMessage;
import com.gintong.ywxt.im.service.ResourceMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnSelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by oem on 4/6/17.
 */
public abstract class BaseKnowledgeController extends BaseController {

    protected static final Logger TRACK_LOGGER = LoggerFactory.getLogger("trackLog");

    @Autowired
    protected KnowledgeService knowledgeService;

    @Autowired
    protected KnowledgeIndexService knowledgeIndexService;

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

    @Autowired
    protected SyncSourceService syncSourceService;

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

        InterfaceResult<Long> result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
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
        final long knowledgeId = result.getResponseData();

        Knowledge detail = data.getKnowledgeDetail();
        detail.setId(knowledgeId);

        if (!tagServiceLocal.saveTagSource(userId, detail)) {
            logger().error("Save Tag info failed, userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        List<Long> successIds = directoryServiceLocal.saveDirectorySource(userId, detail);
        if (successIds != null && successIds.size() >0) {
            logger().error("Save Directory success. userId: " + userId + " knowledgeId: " + knowledgeId + ", plan size: "
                            + detail.getDirectorys().size() + ", success size: " + successIds.size());
        }
        else {
            logger().error("Save Directory info failed, userId: " + userId + " knowledgeId: " + knowledgeId);
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

        Permission permission = permissionServiceLocal.savePermissionInfo(userId, knowledgeId, data.getPermission());
        if (permission != null) {
            logger().info("save knowledge permission success. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        logger().info("create knowledge success.  knowlegeId: " + knowledgeId + " userId: " + userId + " userName: " + user.getName());

        //Sync to dynamic news
        SyncSwitch syncSwitch = getSyncSwitch(userId);
        if (data.getUpdateDynamic() == 0 && syncSwitch.getDynamicType() == 1) {
            /*
            String dynamicNews = createDynamicNews(detail, user);
            try {
                dynamicNewsServiceLocal.addDynamicToAll(dynamicNews, userId);
            } catch (Exception ex) {
                logger().info("sync knowledge to dynamic failed. userId: " + userId + " knowledgeId: " + knowledgeId + " error: " + ex.getMessage());
            }*/
            DataSync dataSync = createDynamicNewsDataSync(detail, user);
            if (dataSyncTask != null) {
                dataSyncTask.saveDataNeedSync(dataSync);
            }
        }

        //Sync resource to freechat
        if (syncSwitch.getGroupType() == 1 || syncSwitch.getSpeciFriendType() == 1 || syncSwitch.getStarFriendType() == 1) {
            if (detail.getCid() > 1) {
                ResourceMessage resourceMessage = this.createResourceMessage(detail);
                logger().info("sync knowledge to feechat. knowledgeId: " + detail.getId());
                dataSyncTask.saveDataNeedSync(new DataSync(0, resourceMessage));
            }
        }

        //send new knowledge to bigdata
        bigDataSyncTask.addToMessageQueue(BigDataSyncTask.KNOWLEDGE_INSERT, data.toBigData());

        //Businsess log
        //BusinessTrackUtils.addTbBusinessTrackLog4AddOpt(logger(), TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(), detail.getId(), null, request, userId, user.getName());
        DataSync dataSync = new DataSync(0, new BusinessTrackLog(logger(), TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(), 0, OptTypeEnum.OPT_ADD.getKey(), detail.getId(), userId, user.getName(), request));
        dataSyncTask.addQueue(dataSync);
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

        //set status to default 4
        detail.setStatus(4);

        return detail;
    }

    protected DataSync createDynamicNewsDataSync(Knowledge detail, User user)
    {
        DataSync data = new DataSync(0, createDynamicNewsObj(detail, user));
        return data;
    }

    protected DynamicNews createDynamicNews(Knowledge detail,User user) {
        DynamicNews dynamic = createDynamicNewsObj(detail, user);
        return dynamic;
        //return KnowledgeUtil.writeObjectToJson(dynamic);
    }

    protected Permission getPermission(final long userId, final long knowledgeId, final int privated) {
        if (userId == 0 || userId ==1) {
            logger().info("create default public permission for userId: " + userId + " knowledgeId: " + knowledgeId);
            return DataCollect.defaultPublicPermission(userId, knowledgeId);
        }
        Permission permission = permissionServiceLocal.getPermissionInfo(knowledgeId);
        if (permission == null) {
            //set a default value
            logger().info("Can't get knowledge permission, so set a default value. knowledgeId: " + knowledgeId);
            return DataCollect.defaultPermission(userId, knowledgeId, privated);
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

    protected String resetKeyWord(final String keyWord) {
        if (StringUtils.isBlank(keyWord) || "null".equals(keyWord)) {
            logger().info("query keyword is null. keyWord: " + keyWord);
            return null;
        }
        return keyWord;
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

    private ResourceMessage createResourceMessage(Knowledge detail) {
        ResourceMessage resourceMessage = new ResourceMessage();
        resourceMessage.setResId(detail.getId());
        resourceMessage.setResType(8);
        resourceMessage.setType(KnowledgeUtil.parserShortType(detail.getColumnType()));
        resourceMessage.setTitle(detail.getTitle());
        final String content = HtmlToText.htmlToText(detail.getContent());
        final int totalLength = content.length();
        final int length = totalLength >= 250 ? 250 : totalLength;
        final String contentDes = content.substring(0, length);
        resourceMessage.setDesc(contentDes);
        resourceMessage.setOwnerId(detail.getCid());
        resourceMessage.setOwnerName(detail.getCname());
        resourceMessage.setPicPath(detail.getPic());

        return resourceMessage;
    }

    private SyncSwitch getSyncSwitch (final long userId) {
        SyncSwitch syncSwitch = null;
        try {
            syncSwitch = syncSourceService.getSyncSwitchStatus(userId, 8);
        } catch (Exception ex) {
            logger().error("query syncSwitch status failed. userId: " + userId);
        }

        if (syncSwitch == null) {
            syncSwitch = new SyncSwitch();
            syncSwitch.setDynamicType(0);
            syncSwitch.setGroupType(0);
            syncSwitch.setSpeciFriendType(0);
            syncSwitch.setStarFriendType(0);
        }
        return syncSwitch;
    }
}
