package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.ywxt.dynamic.model.*;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.*;
import com.ginkgocap.ywxt.knowledge.model.common.Page;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.model.mobile.EActionType;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.task.BigDataSyncTask;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.knowledge.utils.StringUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnSelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/knowledge")
public class KnowledgeController extends BaseController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);

    @Autowired
    private KnowledgeService knowledgeService;

    //@Autowired
    //private KnowledgeHomeService knowledgeHomeService;

    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    @Autowired
    private ColumnSelfService columnSelfService;

    @Autowired
    private KnowledgeBatchQueryService knowledgeBatchQueryService;

    @Autowired
    private AssociateServiceLocal associateServiceLocal;

    @Autowired
    private TagServiceLocal tagServiceLocal;

    @Autowired
    private DirectoryServiceLocal directoryServiceLocal;

    @Autowired
    private PermissionServiceLocal permissionServiceLocal;

    @Autowired
    private DynamicNewsServiceLocal dynamicNewsServiceLocal;

    @Autowired
    private KnowledgeCountService knowledgeCountService;

    @Autowired
    private BigDataSyncTask bigDataSyncTask;

    //@Autowired
    //private Cache cache;

    //@Value("#{configuers.knowledgeBigDataSearchUrl}")
    //private String knowledgeBigDataSearchUrl;

    private ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
    /**
     * 插入数据
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public InterfaceResult create(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

        String requestJson = this.getBodyParam(request);
        DataCollect data = KnowledgeUtil.getDataCollect(requestJson);
        if (data == null || data.getKnowledgeDetail() == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"知识详情格式错误或者为空");
        }
        Knowledge detail = data.getKnowledgeDetail();
        columnTypeAndIdFaultTolerant(detail);

        initKnowledgeTime(data);
        //convertKnowledgeContent(detail, detail.getContent(), null, null, null, isWeb(request));

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            data.serUserInfo(user);
            result = this.knowledgeService.insert(data);
        } catch (Exception e) {
            logger.error("Insert knowledge failed : " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
        }

        if (result == null || result.getNotification()== null || !"0".equals(result.getNotification().getNotifCode())) {
            logger.error("Insert knowledge failed!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70001);
        }
        long knowledgeId = Long.valueOf(result.getResponseData().toString());
        detail.setId(knowledgeId);

        if (!tagServiceLocal.saveTagSource(userId, detail)) {
            logger.error("Save Tag info failed, userId: {} knowledgeId: ", userId, knowledgeId);
        }

        List<Long> successIds = directoryServiceLocal.saveDirectorySource(userId, detail);
        if (successIds != null && successIds.size() >0) {
            logger.error("Save Directory success. userId: {} knowledgeId: {}, plan size: {}, success size: {}",
                    userId, knowledgeId, detail.getDirectorys().size(), successIds.size());
        }
        else {
            logger.error("Save Directory info failed, userId: {} knowledgeId: {}", userId, knowledgeId);
        }

        //save asso information
        //TODO: If this step failed, how to do ?
        Map<Long, List<Associate>> assoMap = null;
        try {
            assoMap = associateServiceLocal.createAssociate(data.getAsso(), knowledgeId, user);
        }catch (Throwable e) {
            logger.error("Insert associate failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        boolean syncToDynamic = false;
        Permission permission = permissionServiceLocal.savePermissionInfo(userId, knowledgeId, data.getPermission());
        if (permission != null) {
            logger.info("save knowledge permission success. userId: " + userId + " knowledgeId: " + knowledgeId);
            syncToDynamic = (permission.getPublicFlag() != 0 && data.getUpdateDynamic() == 1);
        }

        //Sync to dynamic news
        if (syncToDynamic) {
            String dynamicNews = createDynamicNews(detail, user);
            dynamicNewsServiceLocal.addDynamicToAll(dynamicNews, userId, assoMap);
            /*
            DataSync dataSync = createDynamicNewsDataSync(knowledgeDetail, user.isVirtual());
            if (dataSyncTask != null) {
                dataSyncTask.saveDataNeedSync(dataSync);
            }*/
        }

        //send new knowledge to bigdata
        bigDataSyncTask.addToMessageQueue(BigDataSyncTask.KNOWLEDGE_INSERT, data.toBigData());

        logger.info(".......create knowledge success......");
        return result;
    }

    /**
     * 更新数据
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public InterfaceResult updateKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

        String requestJson = this.getBodyParam(request);
        DataCollect data = KnowledgeUtil.getDataCollect(requestJson);
        if (data == null) {
            logger.error("request data is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        Knowledge detail = data.getKnowledgeDetail();
        if (detail == null) {
            logger.error("request knowledgeDetail is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        String columnType = data.getOldType() > 0 ? String.valueOf(data.getOldType()) : detail.getColumnType();
        long knowledgeId = detail.getId();
        if (!permissionServiceLocal.canUpdate(knowledgeId, columnType, userId)) {
            logger.error("permission validate failed, please check if user have permission!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION, "没有权限编辑知识!");
        }
        columnTypeAndIdFaultTolerant(detail);
        //convertKnowledgeContent(detail, detail.getContent(), null, null, null, isWeb(request));

        InterfaceResult<Knowledge> result = null;
        try {
            data.serUserInfo(user);
            result = this.knowledgeService.update(data);
        } catch (Exception e) {
            logger.error("知识更新失败！失败原因： "+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        if (result == null || !CommonResultCode.SUCCESS.getCode().equals(result.getNotification().getNotifCode())) {
            logger.error("知识更新失败, result is null or result code is not success.");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        Knowledge updatedDetail = result.getResponseData();
        if (updatedDetail == null) {
            logger.error("知识更新失败, updated knowledge Detail is null");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70002);
        }

        Permission permission = permissionServiceLocal.updatePermissionInfo(userId, knowledgeId, data.getPermission());
        if (permission != null) {
            logger.debug("update knowledge permission success. userId: " + userId + ", knowledgeId: " + knowledgeId);
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

        logger.info(".......update knowledge success......");
        return result;
    }

    /**
     * 删除数据
     * @param knowledgeId 知识主键
     * @param columnType 栏目主键
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value="/{knowledgeId}/{columnType}", method = RequestMethod.DELETE)
    public InterfaceResult delete(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable long knowledgeId,@PathVariable int columnType) throws Exception {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if(knowledgeId <= 0 || columnType < 0){
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        long userId = this.getUserId(user);
        columnType = columnType == 0 ? 1 : columnType;
        if (!permissionServiceLocal.canDelete(knowledgeId, String.valueOf(columnType), userId)) {
            //Try if is collected knowledge.
            boolean cancelKnowledge = cancelKnowledge(userId, knowledgeId);
            if (!cancelKnowledge) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION, "没有权限删除知识!");
            }
        }

        InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance("");
        try {
            result = this.knowledgeService.deleteByKnowledgeId(knowledgeId, columnType);
        } catch (Exception e) {
            logger.error("knowledge delete failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_KOWLEDGE_EXCEPTION_70003);
        }
        if (result != null && result.getNotification() != null && "0".equals(result.getNotification().getNotifCode())) {
            logger.error("delete knowledge base and detail info success!");
        }
        else {
            return InterfaceResult.getInterfaceResultInstance(result.getNotification().getNotifCode(),"删除知识失败!");
        }

        //delete tags
        tagServiceLocal.deleteTags(userId, knowledgeId);

        //delete directory
        directoryServiceLocal.deleteDirectory(userId, knowledgeId);

        //delete Assso info
        associateServiceLocal.deleteAssociate(knowledgeId, userId);

        //delete permission info
        if (permissionServiceLocal.deletePermissionInfo(userId, knowledgeId)) {
            logger.debug("delete knowledge permission success. userId: {}, knowledgeId: {}", userId, knowledgeId);
        }

        //send new knowledge to bigdata
        bigDataSyncTask.deleteMessage(knowledgeId, columnType, userId);

        logger.info(".......delete knowledge success......");
        return result;
    }

    /**
     * 批量删除数据
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value="/batchDelete", method = RequestMethod.PUT)
    public InterfaceResult batchDelete(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        requestJson = requestJson.substring(1, requestJson.length()-1);
        String[] konwledgeIds = requestJson.split(",");
        //List<Integer> konwledgeIds = KnowledgeUtil.readValue(List.class, requestJson);
        if (konwledgeIds == null || konwledgeIds.length <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        long userId = user.getId();
        List<Long> permDeleteIds = new ArrayList<Long>();
        List<Long> failedIds = new ArrayList<Long>();
        for (String id : konwledgeIds) {
            long knowledgeId = Long.parseLong(id);
            boolean isCanDelete = permissionServiceLocal.canDelete(knowledgeId, "1", userId);
            if (!isCanDelete) {
                failedIds.add(knowledgeId);
                logger.error("permission validate failed, please check if user have permission, knowledgeId: " + knowledgeId);
            }
            else {
                permDeleteIds.add(knowledgeId);
            }
        }

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            result = this.knowledgeService.batchDeleteByKnowledgeIds(permDeleteIds, (short)-1);

        } catch (Exception e) {
            logger.error("knowledge delete failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //This need to change to batch delete
        for (long knowledgeId : permDeleteIds) {
            //delete Assso info
            associateServiceLocal.deleteAssociate(knowledgeId, user.getId());
            //delete tags
            tagServiceLocal.deleteTags(userId, knowledgeId);

            //delete directory
            directoryServiceLocal.deleteDirectory(userId, knowledgeId);

            //delete permission information
            permissionServiceLocal.deletePermissionInfo(userId, knowledgeId);
        }
        String resp = "successId: "+permDeleteIds.toString()+","+"failedId: " + failedIds.toString();
        logger.info("delete knowledge success: " + resp);

        result.setResponseData(resp);
        return result;
    }

    /**
     * 批量删除数据
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value="/batchDeleteKnow", method = RequestMethod.PUT)
    public InterfaceResult batchDeleteKnow(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        //requestJson = requestJson.substring(1, requestJson.length()-1);
        //String[] konwledgeIds = requestJson.split(",");
        List<IdType> idTypeList = KnowledgeUtil.readListValue(IdType.class, requestJson);
        if (CollectionUtils.isEmpty(idTypeList)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        final int maxDeleteSize = 20;
        if (idTypeList.size() > maxDeleteSize) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION,"");
        }

        int toDeleteSize = idTypeList.size();
        long userId = user.getId();
        Map<Integer,List<Long>> permDeleteMap = new HashMap<Integer,List<Long>>(toDeleteSize);
        List<Long> permDeleteIds = new ArrayList<Long>(toDeleteSize);
        List<Long> failedIds = new ArrayList<Long>(toDeleteSize);
        for (IdType idType : idTypeList) {
            if (idType != null) {
                long knowledgeId = idType.getId();
                boolean isCanDelete = permissionServiceLocal.canDelete(knowledgeId, String.valueOf(idType.getType()), userId);
                if (!isCanDelete) {
                    failedIds.add(knowledgeId);
                    logger.error("permission validate failed, please check if user have permission, knowledgeId: " + knowledgeId);
                } else {
                    if (permDeleteMap.get(idType.getType()) == null) {
                        List<Long> ids = new ArrayList<Long>();
                        permDeleteMap.put(idType.getType(), ids);
                    }
                    permDeleteIds.add(knowledgeId);
                    permDeleteMap.get(idType.getType()).add(knowledgeId);
                }
            } else {
                logger.error("null of idType, so skip it.");
            }
        }

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        for (Map.Entry<Integer, List<Long>> keyValue : permDeleteMap.entrySet()) {
            try {
                int type = keyValue.getKey();
                List<Long> deleIds = keyValue.getValue();
                result = this.knowledgeService.batchDeleteByKnowledgeIds(deleIds, (short)type);
                if (result == null || result.getNotification() == null || !"0".equals(result.getNotification().getNotifCode())) {
                    logger.error("delete knowledge failed. knowledgeId: " + deleIds + " type: " + type);
                }
            } catch (Exception e) {
                logger.error("knowledge delete failed！reason："+e.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
            }
        }

        //This need to change to batch delete
        for (long knowledgeId : permDeleteIds) {
            //delete Assso info
            associateServiceLocal.deleteAssociate(knowledgeId, user.getId());
            //delete tags
            tagServiceLocal.deleteTags(userId, knowledgeId);

            //delete directory
            directoryServiceLocal.deleteDirectory(userId, knowledgeId);

            //delete permission information
            permissionServiceLocal.deletePermissionInfo(userId, knowledgeId);
        }

        //Check if this is collected knowledge
        for (Long knowledgeId : failedIds) {
            cancelKnowledge(userId, knowledgeId);
        }

        String resp = "successId: "+permDeleteIds.toString()+","+"failedId: " + failedIds.toString();
        logger.info("delete knowledge success: " + resp);

        result.setResponseData(resp);
        return result;
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/{knowledgeId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detail(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable int type) throws Exception {
        if (isWeb(request)) {
            logger.info("Query knowledge from web....");
            return detailWeb(request, response, knowledgeId, type);
        }

        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, isWeb(request));
        if (result == null || result.getResponseData() == null) {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"获取知识详情失败!");
            return mappingJacksonValue(result);
        }
        DataCollect data = result.getResponseData();
        logger.info(".......get knowledge detail complete......");
        MappingJacksonValue jacksonValue = knowledgeDetail(data);

        return jacksonValue;
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/web/{knowledgeId}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailWeb(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable long knowledgeId,@PathVariable int type) throws Exception {
        User user = this.getJTNUser(request);
        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, isWeb(request));
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if (result != null) {
            DataCollect data = result.getResponseData();
            if (data == null || data.getKnowledgeDetail() == null) {
                logger.error("get knowledge detail failed: knowledgeId: " + knowledgeId);
                return jacksonValue;
            }
            long userId = user.getId();
            Knowledge detail = data.getKnowledgeDetail();
            List<Long> tags = detail.getTagList();
            List<Long> directoryIds = detail.getDirectorys();
            List<IdName> minTags = this.getMinTagList(userId, tags);
            List<IdNameType> minDirectoryList = this.getMinDirectoryList(userId, directoryIds);
            logger.debug("get minTags size: " + (minTags != null ?  + minTags.size() : 0) +
                         " minDirectoryList size: " + (minDirectoryList != null ? minDirectoryList.size() : 0));
            long columnId = KnowledgeUtil.parserStringIdToLong(detail.getColumnid());
            ColumnSelf columnSelf = getColumn(columnId);
            IdName column = columnSelf != null ? new IdName(columnId, columnSelf.getColumnname()) : null;
            if (column != null) {
                logger.info("get column info: Id: " + column.getId() + " name: " + column.getName());
            }
            KnowledgeWeb webDetail = new KnowledgeWeb(detail, minTags, minDirectoryList, column);
            data.setKnowledgeDetail(webDetail);
            jacksonValue = knowledgeDetail(data);
        } else {
            logger.info("Query knowledge detail failed.");
        }
        return jacksonValue;
    }

    /**
     * 提取所有知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/all/{start}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAll(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable int start,@PathVariable int size,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        logger.info("---keyWord: " + keyword);
        long userId = user.getId();
        Map<String, List<KnowledgeBase>> resultMap = new HashMap<String, List<KnowledgeBase>>();
        List<KnowledgeBase> createdKnowledgeItems = this.getCreatedKnowledge(userId, start, size, keyword);
        if (CollectionUtils.isNotEmpty(createdKnowledgeItems)) {
            createdKnowledgeItems = setReadCount(createdKnowledgeItems);
            resultMap.put("created", createdKnowledgeItems);
        }

        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledge(userId, start, size, keyword);
        collectedKnowledgeItems = setReadCount(collectedKnowledgeItems);
        resultMap.put("collected", collectedKnowledgeItems);

        logger.info(".......get all knowledge success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(resultMap);
    }

    private List<KnowledgeBase> setReadCount(List<KnowledgeBase> baseList) {
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

    /**
     * 提取所有知识数据
     * @param page 分页
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allByPage/{page}/{size}/{total}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllByPage(HttpServletRequest request, HttpServletResponse response,
                                        @PathVariable int page,@PathVariable int size,
                                        @PathVariable long total,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = getUserId(user);
        //First request need to get from server
        if (total == -1) {
            total = getKnowledgeCount(userId);
        }

        int gotTotal = page * size;
        if ( gotTotal >= total) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS,"到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> createdKnowledgeList = null;
        int createCount = getCreatedKnowledgeCount(userId);
        if (createCount > gotTotal) {
            createdKnowledgeList = this.getCreatedKnowledge(userId, gotTotal, size, keyword);
            if (createdKnowledgeList != null && createdKnowledgeList.size() >= size) {
                logger.info("get created knowledge size: " + createdKnowledgeList.size());
                return knowledgeListPage(total, page, size, createdKnowledgeList);
            }
        }

        if (createdKnowledgeList != null && createdKnowledgeList.size() > 0) {
            int restSize = size - createdKnowledgeList.size();
            List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledge(userId, 0, restSize, keyword);
            int collectedSize = collectedKnowledgeList != null ? collectedKnowledgeList.size() : 0;
            logger.info("get created knowledge size: " + createdKnowledgeList.size() + " collected size: " + collectedSize);
            if (collectedSize > 0) {
                createdKnowledgeList.addAll(collectedKnowledgeList);
            }
            return knowledgeListPage(total, page, createdKnowledgeList.size(), createdKnowledgeList);
        }

        page = gotTotal - createCount;
        List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledge(userId, page, size, keyword);
        if (collectedKnowledgeList != null && collectedKnowledgeList.size() > 0) {
            logger.info("get collected size: " + collectedKnowledgeList.size());
            return knowledgeListPage(total, page, collectedKnowledgeList.size(), collectedKnowledgeList);
        }

        logger.info(".......get all knowledge complete......");
        return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
    }

    @ResponseBody
    @RequestMapping(value = "/getKnowledgeByTypeAndKeyword/{type}{page}/{size}/{total}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeByTypeAndKeyword(HttpServletRequest request, HttpServletResponse response,
    													@PathVariable short type, @PathVariable int page,@PathVariable int size,
    													@PathVariable long total,@PathVariable String keyword) throws Exception {

        if (type == KNOWLEDGE_MYCOLLECT) {
			return this.getAllCreatedByPage(request, response, page, size, total, keyword);
		} else if (type == KNOWLEDGE_SHAREME) {
			return this.getAllCollectedByPage(request, response, page, size, total, keyword);
		} else if (type == KNOWLEDGE_MYCREATE) {
			return null;
		} else if (type == KNOWLEDGE_ALL) {
			return this.getAllByPage(request, response, page, size, total, keyword);
		}
        return InterfaceResult.getSuccessInterfaceResultInstance("No data");
    }

    /**
     * 提取所有知识创建数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allCreated/{start}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllCreated(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable int start,@PathVariable int size,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = user.getId();
        List<KnowledgeBase> createdKnowledgeItems = this.getCreatedKnowledge(userId, start, size, keyword);

        logger.info(".......get all created knowledge success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(createdKnowledgeItems);
    }

    /**
     * 提取所有知识创建数据
     * @param page 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allCreatedByPage/{page}/{size}/{total}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllCreatedByPage(HttpServletRequest request, HttpServletResponse response,
                                               @PathVariable int page,@PathVariable int size,
                                               @PathVariable long total,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        if (total == -1) {
            //TODO: need to check if long to int
            total = getCreatedKnowledgeCount(userId);
        }

        int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> createdKnowledgeList = this.getCreatedKnowledge(userId, start, size, keyword);
        InterfaceResult<Page<KnowledgeBase>> result = this.knowledgeListPage(total, page, size, createdKnowledgeList);
        logger.info(".......get all created knowledge success. size: " + (createdKnowledgeList != null ? createdKnowledgeList.size() : 0));
        return result;
    }

    /**
     * 提取所有知识收藏数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allCollected/{start}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllCollected(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable int start,@PathVariable int size,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledge(userId, start, size, keyword);

        logger.info(".......get all collected knowledge success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(collectedKnowledgeItems);
    }

    /**
     * 提取所有知识收藏数据
     * @param page 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allCollectedByPage/{page}/{size}/{total}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllCollectedByPage(HttpServletRequest request, HttpServletResponse response,
                                                 @PathVariable int page,@PathVariable int size,
                                                 @PathVariable long total,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        if (total == -1) {
            total = getCollectedKnowledgeCount(userId);
        }

        int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledge(userId, start, size, keyword);

        InterfaceResult<Page<KnowledgeBase>> result = this.knowledgeListPage(total, page, size, collectedKnowledgeList);
        logger.info(".......get all collected knowledge success. size: " + (collectedKnowledgeList != null ? collectedKnowledgeList.size() : 0));
        return result;
    }


    /**
     * 根据栏目提取知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allByColumn/{columnId}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult getAllByColumnId(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable int columnId,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if (columnId <= 0 || start < 0 || size <= 0 ) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<KnowledgeBase> knowledgeBasesItems = null;
        try {
            knowledgeBasesItems = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getId(), columnId, start, size);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason： " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBasesItems);
    }

    @ResponseBody
    @RequestMapping(value = "/allByKeyword/{keyWord}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult getAllByKeyWord(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable String keyWord,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(start < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            long usrId = user.getId();
            List<KnowledgeBase> knowledgeBaseItems = null;
            if (keyWord == null || keyWord.length() > 0) {
                knowledgeBaseItems = this.knowledgeService.getBaseByCreateUserId(usrId, start, size);
            }
            else {
                knowledgeBaseItems = this.knowledgeService.getByUserIdKeyWord(usrId, keyWord, start, size);
            }
            result.setResponseData(knowledgeBaseItems);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason： " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return result;
    }

    @Deprecated
    @ResponseBody
    @RequestMapping(value = "/allByKeywordAndColumn/{keyWord}/{columnId}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> getAllByColumnIdAndKeyWord(HttpServletRequest request, HttpServletResponse response,
                                                                           @PathVariable String keyWord,@PathVariable int columnId,
                                                                           @PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(columnId <= 0 || start < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<KnowledgeBase> knowledgeBasesItems = null;
        try {
            if (keyWord == null || keyWord.length() <= 0) {
                knowledgeBasesItems = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getId(), columnId, start, size);
            } else {
                knowledgeBasesItems = this.knowledgeService.getBaseByColumnIdAndKeyWord(keyWord, columnId, start, size);
            }
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason： " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBasesItems);
    }


    @ResponseBody
    @RequestMapping(value = "/allKnowledgeByColumnAndSource/{type}/{columnId}/{source}/{page}/{size}/{total}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeByColumnAndSource(HttpServletRequest request, HttpServletResponse response,
                                                                              @PathVariable short type,@PathVariable int columnId,
                                                                              @PathVariable short source, @PathVariable int page,
                                                                              @PathVariable int size, @PathVariable long total) throws Exception
    {
        User user = getJTNUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(columnId <= 0 || page < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }
        if (type <= 0) {
            type = (short)KnowledgeType.ENews.value();
        }

        long userId = this.getUserId(user);
        int start = page * size;
        List<KnowledgeBase> knowledgeList = null;
        if (source == KnowledgeConstant.SOURCE_GINTONG_BRAIN) {
            //First get total;
            userId = KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID;
            String [] idList = getChildIdListByColumnId(columnSelfService, columnId, userId);
            if (total == -1) {
                logger.info("begin to get knowledge count:");
                total = knowledgeBatchQueryService.getKnowledgeCountByUserIdAndColumnID(idList, (long)KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID, type);
                logger.info("end to get knowledge count:" + total);
            }
            if (total > 0 && start < total) {
                logger.info("start to get knowledge:" + total);
                List<Knowledge> detailList = knowledgeBatchQueryService.getKnowledgeDetailList(idList, userId, type, start, size);
                logger.info("end to get knowledge: size: " + (detailList != null ? detailList.size() : 0));
                knowledgeList = DataCollect.convertDetailToBaseList(detailList, type, true);
                logger.info("convert knowledge size: " + (knowledgeList != null ? knowledgeList.size() : 0));
            } else {
                return queryKnowledgeEnd();
            }
        }
        else if (source == KnowledgeConstant.SOURCE_ALL_PLATFORM) {
            ColumnSelf column = null;
            try {
                column = columnSelfService.selectByPrimaryKey((long) columnId);
            } catch (Exception ex) {
                logger.error("Get column failed: error: " + ex.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "获取栏目信息失败，栏目服务当前不可用!");
            }
            if (total == -1) {
                total = 200L; //default value//this.knowledgeService.getBasePublicCountByColumnId(type, KnowledgeConstant.PRIVATED);
            }
            if (column == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "获取栏目信息失败，请检查栏目Id是否正确!");
            }
            else if (total > 0 && start < total) {
                List<Knowledge> detailList = null;
                userId = KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID;
                try {
                    //detailList = this.knowledgeBatchQueryService.selectPlatform(type, columnId, column.getPathName(), userId, start, size);
                    knowledgeList = this.knowledgeBatchQueryService.selectPlatformBase(type, columnId, column.getPathName(), userId, start, size);
                } catch (Exception ex) {
                    logger.error("invoke selectPlatform() failed. type: {}, columnId: {}, columnPath: {},  userId: {} error: {}"
                            ,type, columnId, column.getPathName(), userId, ex.getMessage());
                }
                //knowledgeList = convertKnowledgeDetailListToBase(detailList, type);
                if (knowledgeList == null || knowledgeList.size() <= 0) {
                    return InterfaceResult.getSuccessInterfaceResultInstance("暂时没有符合条件的知识!");
                }
            } else {
                return queryKnowledgeEnd();
            }
        }
        else if (source == KnowledgeConstant.SOURCE_MY_SELF) {
            //First get total;
            if (total == -1) {
                total = getKnowledgeCount(userId);
            }
            if (total > 0 && start < total) {
                knowledgeList = this.getCreatedKnowledge(userId, start, size, null);
            } else {
                return queryKnowledgeEnd();
            }
        }
        else if (source == KnowledgeConstant.SOURCE_MY_FRIEND) {

        }
        //All SOURCE_ALL_PLATFORM
        else {
            if (total == -1) {
                try {
                    total = this.knowledgeService.getBaseAllPublicCount(KnowledgeConstant.PRIVATED);
                } catch (Exception ex) {
                    logger.error("invoke getBaseAllPublicCount failed: error: " + ex.getMessage());
                }
            }
            if (total > 0 && start < total) {
                try {
                    knowledgeList = this.knowledgeService.getBaseAllPublic(start, size, KnowledgeConstant.PRIVATED);
                } catch (Exception ex) {
                    logger.error("invoke getBaseAllPublic failed: error: " + ex.getMessage());
                }
            } else {
                return queryKnowledgeEnd();
            }
        }
        if (knowledgeList == null && knowledgeList.size() <= 0) {
            return queryRunning();
        }
        return knowledgeListPage(total, page, size, knowledgeList);
    }

    @ResponseBody
    @RequestMapping(value = "/allKnowledgeByColumnAndSourceWeb/{type}/{columnId}/{page}/{size}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeByColumnAndSourceWeb(HttpServletRequest request, HttpServletResponse response,
                                                            @PathVariable short type, @PathVariable int columnId,
                                                            @PathVariable int page,@PathVariable int size) throws Exception
    {
        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        try {
            // 获取分类
            long userId = this.getUserId(user);
            logger.info("进入知识分类首页！");

            ColumnSelf column = columnSelfService.selectByPrimaryKey((long) type);
            if (column != null) {
                type = column.getType().shortValue();
            }
            ColumnSelf c = null;
            ColumnSelf co = null;
            co = columnSelfService.selectByPrimaryKey((long) type);// 一级栏目
            c = columnSelfService.selectByPrimaryKey((long) columnId);// 当前栏目

            // 获取栏目列表
            Map<String, Object> model = new HashMap<String, Object>(20);
            List<ColumnSelf> cl = columnSelfService.queryListByPidAndUserId((long)type, userId);
            model = putKnowledge(model, type, columnId, userId, page, size);
            model.put("cl", cl);
            model.put("column", c);
            model.put("columnone", co);
            model.put("columnid", columnId);
        } catch (Exception e) {
            logger.error("查询栏目出错,错误信息: " + e.toString());
            e.printStackTrace();
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value = "/allByColumnAndKeyword/{columnId}/{keyWord}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> allByColumnIdAndKeyWord(HttpServletRequest request, HttpServletResponse response,
                                                                        @PathVariable int columnId,@PathVariable String keyWord,
                                                                        @PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(columnId <= 0 || start < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<KnowledgeBase> knowledgeBasesItems = null;
        try {
            if (keyWord == null || keyWord.length() <= 0) {
                knowledgeBasesItems = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getId(), columnId, start, size);
            } else {
                knowledgeBasesItems = this.knowledgeService.getBaseByColumnIdAndKeyWord(keyWord, columnId, start, size);
            }
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason： " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBasesItems);
    }

    @ResponseBody
    @RequestMapping(value = "/allNoDirectory/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> allByColumnIdAndKeyWord(HttpServletRequest request,HttpServletResponse response,
                                                                        @PathVariable int start,@PathVariable int size) throws Exception
    {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        List<KnowledgeBase> knowledgeBasesItemList = null;
        try {
            knowledgeBasesItemList = knowledgeService.getKnowledgeNoDirectory(userId, start, size);

        } catch (Exception e) {
            logger.error("Query knowledge failed！userId: " + userId + " reason： " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge list success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBasesItemList);
    }

    @ResponseBody
    @RequestMapping(value = "/tag/{tagId}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> getAllByTagId(HttpServletRequest request, HttpServletResponse response,
                                                              @PathVariable long tagId,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(tagId <= 0 || start < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }
        List<Long> knowledgeIds = tagServiceLocal.getKnowledgeIdsByTagId(tagId, start, size);
        if (knowledgeIds == null || knowledgeIds.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS,"该标签下无知识.");
        }

        List<KnowledgeBase> knowledgeBaseList = null;
        try {
            knowledgeBaseList = this.knowledgeService.getBaseByIds(knowledgeIds);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}", e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBaseList);
    }

    @ResponseBody
    @RequestMapping(value = "/byDirectory/{directoryId}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> getAllByDirectoryId(HttpServletRequest request, HttpServletResponse response,
                                                                    @PathVariable long directoryId,@PathVariable int start,@PathVariable int size) throws Exception
    {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(directoryId <= 0 || start < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<Long> knowledgeIds = directoryServiceLocal.getKnowledgeIdListByDirectoryId(user.getId(), directoryId, start, size);
        if (knowledgeIds == null || knowledgeIds.size() <= 0) {
            logger.error("get knowledge list is null by directoryId: " + directoryId);
            if (start == 0) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "该目录下的知识为空！");
            } else {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "该目录下的知识已经取完！");
            }
        }

        List<KnowledgeBase> knowledgeBaseList = null;
        try {
            knowledgeBaseList = this.knowledgeService.getBaseByIds(knowledgeIds);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}", e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "查询知识失败！");
        }

        if (knowledgeBaseList != null && knowledgeBaseList.size() > 0) {
            logger.info(".......get all knowledge by directory  success......");
            //check the corrupt knowledge, and log it.
            if (knowledgeIds.size() != knowledgeBaseList.size()) {
                for (KnowledgeBase base : knowledgeBaseList) {
                    if (!knowledgeIds.contains(base.getId())) {
                        logger.error("DirectorySource: corrupted knowledge, id: {}",base.getId());
                    }
                }
            }
        } else {
            logger.info(".......no knowledge under this directory, id: {}", directoryId);
        }
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBaseList);
    }

    /**
     * 提取当前用户的所有知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/user/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<DataCollect>> getByCreateUserId(HttpServletRequest request, HttpServletResponse response,
                                                                   @PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(start < 0 || size <= 0 ) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<DataCollect> dataList = null; //DummyData.resultObject(DummyData.getDataCollectList());
        try {
            dataList = KnowledgeUtil.getDataCollectReturn(this.knowledgeService.getBaseByCreateUserId(user.getId(), start, size));
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by create userId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(dataList);
    }

    /**
     * 根据栏目提取当前用户的知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/user/{columnId}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> getByCreateUserIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
                                                                             @PathVariable int columnId,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (columnId <= 0) {
            return checkColumn(columnId);
        }

        if (start < 0 || size <= 0 ) {
            return checkStartAndSize(start, size);
        }

        List<KnowledgeBase> KnowledgeBaseList = null;
        try {
            KnowledgeBaseList = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getId(), columnId, start, size);
        } catch (Exception e) {
            logger.error("Query knowledge by userId and columnId failed. error: {}", e.getMessage());
        }
        logger.info(".......get all knowledge by create userId and columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(KnowledgeBaseList);
    }

    /**
     * 收藏知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/collect/{knowledgeId}/{typeId}", method = RequestMethod.POST)
    public InterfaceResult collect(HttpServletRequest request, HttpServletResponse response,
                                                   @PathVariable long knowledgeId, @PathVariable int typeId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

        if (knowledgeId <= 0 || typeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeOtherService.collectKnowledge(userId, knowledgeId, typeId);
        } catch (Exception e) {
            logger.error("collect knowledge failed！：" + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance();
        }

        //collect count
        //knowledgeCountService.updateCollectCount(userId, knowledgeId, (short)typeId);
        logger.info(".......collect knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /**
     * 取消收藏的知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/collect/{knowledgeId}/{typeId}", method = RequestMethod.DELETE)
    public InterfaceResult cancelCollection(HttpServletRequest request, HttpServletResponse response,
                                                            @PathVariable long knowledgeId, @PathVariable int typeId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = user.getId();
        if (knowledgeId <= 0 || typeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeOtherService.deleteCollectedKnowledge(userId, knowledgeId, typeId);
        } catch (Exception e) {
            logger.error("cancel collected knowledge failed！：" + e.getMessage());
        }
        logger.info(".......cancel collect knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /**
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/collectCount", method = RequestMethod.GET)
    public InterfaceResult<Long> CollectCount(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable long knowledgeId, @PathVariable int columnId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long count = 0;
        final long userId = user.getId();
        try {
            count = getCollectedKnowledgeCount(userId);
        } catch (Exception e) {
            logger.error("cancel collected knowledge failed！ userId: "  + userId + " error:" + e.getMessage());
        }
        logger.info(".......cancel collect knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, count);
    }

    /**
     * 保存知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/save/{knowledgeId}/{typeId}", method = RequestMethod.POST)
    public InterfaceResult save(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable long knowledgeId, @PathVariable int typeId) throws Exception {

        InterfaceResult result = create(request, response);
        logger.info(".......save knowledge success......");
        return result;
    }

    /**
     * 举报知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/report/{knowledgeId}/{columnId}", method = RequestMethod.POST)
    public InterfaceResult report(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable long knowledgeId, @PathVariable int columnId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        KnowledgeReport report = KnowledgeUtil.readValue(KnowledgeReport.class, requestJson);
        if (report == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeOtherService.reportKnowledge(report);
        } catch (Exception e) {
            logger.error("Report knowledge failed！reason："+e.getMessage());
        }
        logger.info(".......report knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/count/{columnId}", method = RequestMethod.PUT)
    public InterfaceResult countBy(HttpServletRequest request,HttpServletResponse response, @PathVariable int columnId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/my/count", method = RequestMethod.GET)
    public InterfaceResult myCount(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        long knowledgeCount = getKnowledgeCount(userId);
        logger.info("totalCount: {}", knowledgeCount);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, knowledgeCount);
    }

    /**
     * 批量打标签
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/batchTags", method = RequestMethod.POST)
    public InterfaceResult batchTags(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        logger.info("batchTags: {}", requestJson );
        List<ResItem> batchItems = KnowledgeUtil.readListValue(ResItem.class, requestJson);
        if (CollectionUtils.isEmpty(batchItems)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            //return this.tagServiceLocal.batchTags(knowledgeService, user.getId(), batchItems);
            return this.tagServiceLocal.batchTags(knowledgeService, user.getId(), requestJson);
        } catch (Exception e) {
            logger.error("batch tags failed！reason："+e.getMessage());
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION,"batch create Tag service failed!");
        }
    }


    /**
     * 批打目录
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/batchCatalogs", method = RequestMethod.POST)
    public InterfaceResult batchCatalog(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        logger.info("batchCatalogs: "+requestJson);

        /*List<ResItem> batchItems = KnowledgeUtil.readListValue(ResItem.class, requestJson);
        if (batchItems == null || batchItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }*/

        try {
            //return this.directoryServiceLocal.batchCatalogs(knowledgeService, user.getId(), batchItems);
            return this.directoryServiceLocal.batchDirectory(knowledgeService, user.getId(), requestJson);
        } catch (Exception e) {
            logger.error("Batch catalogs failed！reason："+e.getMessage());
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "Batch catalogs failed");
        }
    }

    /**
     * get Tags
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/tagList", method = RequestMethod.POST)
    public MappingJacksonValue getTagsByIds(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> tagIds = KnowledgeUtil.readListValue(Long.class, requestJson);
        if (CollectionUtils.isEmpty(tagIds)) {
            logger.error("tag list is null...");
            return mappingJacksonValue(CommonResultCode.PARAMS_NULL_EXCEPTION,"tag list is null...");
        }

        try {
            return this.tagServiceLocal.getTagListByIds(user.getId(),tagIds);
        } catch (Exception e) {
            logger.error("Get Tag list failed！reason："+e.getMessage());
            return mappingJacksonValue(CommonResultCode.SERVICES_EXCEPTION,"Get Tag Service error");
        }
    }

    /**
     * get Tags
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/tagCount", method = RequestMethod.POST)
    public InterfaceResult getTagSourceCountByIds(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> tagIds = KnowledgeUtil.readListValue(Long.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (CollectionUtils.isEmpty(tagIds)) {
            logger.error("No tag list send.");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION,"No tag list send.");
        }

        try {
            return this.tagServiceLocal.getTagSourceCountByIds(user.getId(),tagIds);
        } catch (Exception e) {
            logger.error("Get TagCount failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "Get Tag Service error");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/createTag/{tagName}", method = RequestMethod.GET)
    public InterfaceResult createTag(HttpServletRequest request, HttpServletResponse response,@PathVariable String tagName) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (StringUtils.isEmpty(tagName)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "tagName is null");
        }

        try {
            long tagId = this.tagServiceLocal.createTag(user.getId(), tagName);
            return InterfaceResult.getSuccessInterfaceResultInstance(tagId);
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("create Tag failed!");
    }

    @ResponseBody
    @RequestMapping(value = "/getTagList", method = RequestMethod.GET)
    public MappingJacksonValue getTagList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return this.mappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION));
        }

        try {
            List<Tag> tagList = this.tagServiceLocal.getTagList(user.getId());
            if (tagList != null && tagList.size() > 0) {
                return tagServiceLocal.convertInterfaceResult(tagList);
            }
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return this.mappingJacksonValue(InterfaceResult.getSuccessInterfaceResultInstance("get Tag failed, or no tags create!"));
    }

    @ResponseBody
    @RequestMapping(value = "/createDirectory/{directoryName}", method = RequestMethod.GET)
    public InterfaceResult createDirectory(HttpServletRequest request, HttpServletResponse response, @PathVariable String directoryName) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (StringUtils.isEmpty(directoryName)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "directory Name is null");
        }

        try {
            long directoryId = this.directoryServiceLocal.createDirectory(user.getId(), directoryName);
            return InterfaceResult.getSuccessInterfaceResultInstance(directoryId);
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("create Directory failed!");
    }

    @ResponseBody
    @RequestMapping(value = "/getDirectoryList", method = RequestMethod.GET)
    public MappingJacksonValue getDirectoryList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return this.mappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION));
        }

        try {
            List<Directory> directoryList = this.directoryServiceLocal.getDirectoryList(user.getId());
            if (directoryList != null && directoryList.size() > 0) {
                return directoryServiceLocal.convertToMappingJacksonValue(directoryList);
            }
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return this.mappingJacksonValue(InterfaceResult.getSuccessInterfaceResultInstance("get Directory list failed or is empty "));
    }

    /**
     * 目录
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/directoryList", method = RequestMethod.POST)
    public MappingJacksonValue getDirectoryListByIds(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION));
        }

        String requestJson = this.getBodyParam(request);
        List<Long> directoryIds = KnowledgeUtil.readListValue(Long.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (CollectionUtils.isEmpty(directoryIds)) {
            logger.error("No any directory id send!");
            return mappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION,"No any directory id send!"));
        }
        try {
            return this.directoryServiceLocal.getDirectoryListByIds(user.getId(), directoryIds);
        }
        catch (Exception ex) {
            logger.error("Get directory list failed！reason："+ex.getMessage());
            ex.printStackTrace();
            return mappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"Get directory list failed！"));
        }
    }

    /**
     * 目录
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/directoryCount", method = RequestMethod.POST)
    public InterfaceResult getDirectoryCountByIds(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> directoryIds = KnowledgeUtil.readListValue(Long.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (CollectionUtils.isEmpty(directoryIds)) {
            logger.error("No any directory Id send..");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION,"No any directory Id send..");
        }

        try {
            return this.directoryServiceLocal.getDirectorySourceCountByIds(user.getId(), directoryIds);
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"Not any directory get");
        }

    }

    /**
     * 推荐获取大数据推荐及个人关联知识
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/knowledgeRelated/{type}/{page}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeRelatedResources(HttpServletRequest request, HttpServletResponse response,
                                                        @PathVariable short type,@PathVariable int page,
                                                        @PathVariable int size, @PathVariable String keyword) throws Exception {

        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = this.getUserId(user);
        final String queryType ="knowledge";
        /** 金桐网推荐的相关“知识”数据 */
        List<KnowledgeMini> platformKnowledge = null;
        List<JSONObject> list = getSearchList(request, userId, page, size, keyword, queryType, null);
        if(list != null && list.size() > 0){
            int recommend_count = 5;
            recommend_count = recommend_count < list.size() ? recommend_count : list.size();
            platformKnowledge = new ArrayList<KnowledgeMini>(recommend_count);
            for (int i = 0; i < recommend_count; i++) {
                Map<String, Object> map = list.get(i);
                Object tempId = map.get("kid");
                Object uid = map.get("kcid");
                if (null == tempId || uid == null ) {
                    continue;
                }
                String kId = tempId.toString();
                long knowledgeId = KnowledgeUtil.parserStringIdToLong(kId.toString());
                if (knowledgeId <= 0) {
                    logger.error("this knowledge is invalidated, knowledgeId: {}", kId);
                    continue;
                }

                long ownerId = KnowledgeUtil.parserStringIdToLong(uid.toString());
                if (ownerId < 0) {
                    logger.error("this knowledge is invalidated, ownerId: {}", uid);
                    continue;
                }

                KnowledgeMini mini = new KnowledgeMini();
                mini.setId(knowledgeId);
                mini.setUserId(ownerId);
                mini.setTitle(map.get("ktitle") == null ? "" : map.get("ktitle").toString());

                Object kcolumnType = map.get("kcolumntype");
                if (kcolumnType != null) {
                    short newType = KnowledgeUtil.parserShortType(kcolumnType.toString());
                    mini.setType(newType);
                }

                Object kcreateTime = map.get("kcreatetime");
                if (kcreateTime != null) {
                    long newTime = KnowledgeUtil.parserTimeToLong(kcreateTime.toString());
                    mini.setTime(newTime);
                }

                // check if need this
                //Object columnType = map.get("kcolumnid");
                //if (columnType != null) {
                //    int newType = KnowledgeUtil.parserColumnId(columnType.toString());
                //    mini.setColumnType(newType);
                //}
                platformKnowledge.add(mini);
            }
        }

        // 用户自己的所有知识
        Map<String, Object> responseDataMap = new HashMap<String, Object>(2);
        List<KnowledgeBase> userKnowledgeBase = null;
        List<KnowledgeMini> userKnowledge = null;
        int start = page * size;
        userKnowledgeBase = getCreatedKnowledge(userId, start, size, keyword);

        if (userKnowledgeBase != null && userKnowledgeBase.size() > 0) {
            userKnowledge = convertKnowledgeBaseToMini(userKnowledgeBase);
        }

        responseDataMap.put("listPlatformKnowledge", platformKnowledge);
        responseDataMap.put("listUserKnowledge", userKnowledge);
        responseDataMap.put("type", type);
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    private List<JSONObject>  getSearchList(HttpServletRequest request,long userId, int pno, int psize, String keyword, String module,Map<String, String> conditions) throws Exception
    {
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>(11);
        pairs.add(new BasicNameValuePair("userid", String.valueOf(userId)));
        pairs.add(new BasicNameValuePair("pno", String.valueOf(pno)));
        pairs.add(new BasicNameValuePair("psize", String.valueOf(psize)));
        pairs.add(new BasicNameValuePair("keyword", keyword));
        pairs.add(new BasicNameValuePair("module", module));
        pairs.add(new BasicNameValuePair("hlpre", null));
        pairs.add(new BasicNameValuePair("hlext", null));
        String url = (String) request.getSession().getServletContext().getAttribute("newSearchQueryHost");
        if (StringUtils.isEmpty(url)) {
            ResourceBundle resource = ResourceBundle.getBundle("application");
            url = resource.getString("knowledge.data.search.url");
        }
        // 多条件筛选集
        if (conditions != null) {
            Iterator<String> conditionIterator = conditions.keySet().iterator();
            while (conditionIterator.hasNext()) {
                String key = conditionIterator.next();
                pairs.add(new BasicNameValuePair(key, conditions.get(key)));
            }
        }
        String resultJson = null;
        JSONObject jo = null;
        logger.info("开始调用大数据接口,url是{},参数是{}", url + "/keyword/search", pairs.toString());
        try {
            resultJson = HttpClientHelper.post(url + "/keyword/search", pairs);
            logger.info("调用大数据搜索接口,返回信息是{}", resultJson);
            jo = JSONObject.fromObject(resultJson);
            JSONArray tempList = jo.getJSONArray("datas");
            List<JSONObject> list = null;
            if (tempList.size() > 0) {
                list = (List<JSONObject>) JSONArray.toCollection(tempList, JSONObject.class);
                list.removeAll(Collections.singleton(null));
            }
            return list;
        } catch (Exception e) {
            logger.error("调用大数据搜索发生错误,错误原因", e);
            logger.error("调用大数据搜索发生错误,url是{},message是{}", url + "/keyword/search", e.getMessage());
            return null;
        }
    }

    private Map<String, Object> putKnowledge(Map<String, Object> model, short type, int columnId, Long userId, int page, int size) {
        Map<Long, Object> pll = new HashMap<Long, Object>(size);
        Map<Long, Object> plr = new HashMap<Long, Object>(size);
        Map<Long, Object> plcontent = new HashMap<Long, Object>(size);

        List<Knowledge> knowledgeList = knowledgeBatchQueryService.getAllByParam(type, columnId, null, userId, page, size);
        if (model != null) {
            model.put("list", knowledgeList);
        }

        putRelationAndOther(type, knowledgeList, userId, plr, pll, plcontent, model);
        return model;
    }

    private void putRelationAndOther(short type, List<Knowledge> knl, Long userId, Map<Long, Object> plr, Map<Long, Object> pll,
                                     Map<Long, Object> plcontent, Map<String, Object> model) {
        for (Knowledge k : knl) {
            putBeRelation(type, k, userId, model, plr, pll);

        }
    }

    private void putBeRelation(short type, Knowledge k, Long userId, Map<String, Object> model, Map<Long, Object> plr, Map<Long, Object> pll) {
        // 1 自己 2好友 3 金桐脑 4全平台
        int relation = 0;
        if (userId == null) {
            if (k.getUid() == KnowledgeConstant.Ids.EGinTN.v()) {
                relation = 3;
            } else {
                relation = 4;
            }
            plr.put(k.getId(), relation);
            model.put("plr", plr);
            getCommentCount(type, k.getId(), userId, pll);
            model.put("pll", pll);

        } else {
            putBeRelation(type, k.getUid(), userId, model, plr, pll, k.getId());
        }
    }

    private void putBeRelation(short type, long ci, long userId, Map<String, Object> model, Map<Long, Object> plr, Map<Long, Object> pll, long knowledgeId) {
        if (userId < 0) {
            userId = 0l;
        }

        getCommentCount(type, knowledgeId, userId, pll);
        model.put("pll", pll);
    }

    private void getCommentCount(short type, long kId, long userId, Map<Long, Object> commentCountMap) {
        // 评论数
        KnowledgeCount kCount = knowledgeCountService.getKnowledgeCount(userId, kId, type);
        commentCountMap.put(kId, kCount);
    }


    public List<KnowledgeMini> convertKnowledgeBaseToMini(List<KnowledgeBase> baseList)
    {
        if (baseList == null || baseList.size() <= 0) {
            return null;
        }

        List<KnowledgeMini> km2List =  new ArrayList<KnowledgeMini>(baseList.size());
        for(KnowledgeBase base : baseList) {
            KnowledgeMini mini = changeKnowledgeToMini(base);
            if (mini != null) {
                km2List.add(mini);
            }
        }

        return km2List;
    }


    public KnowledgeMini changeKnowledgeToMini(KnowledgeBase base)
    {
        if (base == null) {
            logger.error("give knowledge base is null..");
            return null;
        }
        KnowledgeMini km = new KnowledgeMini();
        km.setId(base.getId());
        km.setTitle(base.getTitle());
        km.setUserId(base.getCreateUserId());
        km.setUserName(base.getCreateUserName());
        km.setTime(base.getCreateDate());
        km.setType(base.getType());
        km.setColumnId(base.getColumnId());
        return km;
    }

    // 图片头部信息拼接
    private String changeImage(String image) {
        if(image == null||"null".equals(image)) return "";
        //移动端图片
        if(image.indexOf("download") != -1) {
            return image;
        }
        if(image.indexOf("http") != -1) {
            return image;
        }
        //web端图片地址
        if(!StringUtils.isEmpty(image) && image.indexOf("default") == -1) {
            return resourceBundle.getString("nginx.root") +image;
        }
        return "";
    }

    /**
     * @param picPath 头像相对路径
     * @param userIsVirtual 用户是否组织
     * @return
     */
    private String changeUserImage(String picPath, boolean userIsVirtual) {
        if (StringUtils.isBlank(picPath)) {
            if (userIsVirtual)
                picPath = KnowledgeConstant.ORGAN_DEFAULT_PIC_PATH;
            else
                picPath = KnowledgeConstant.USER_DEFAULT_PIC_PATH_MALE;
        } else if (picPath.startsWith("http")) {
            return picPath;
        }
        return resourceBundle.getString("nginx.root") + picPath;
    }

    private String getMapValue(Object obj) {
        if(null == obj) {
            return "";
        }
        String temp = obj.toString();
        if(temp.startsWith("net")) {
            return "";
        }
        return temp;
    }

    private String getDesc(String desc) {
        if(desc != null && !desc.equals("")) {
            if(desc.length() > 50) {
                return desc.substring(0, 49);
            }
        }
        return desc;
    }

    private List<KnowledgeBase> getCreatedKnowledge(long userId, int start, int size, String keyWord)
    {
        List<KnowledgeBase> createdKnowledgeItems = null;
        try {
            if (keyWord == null || "null".equals(keyWord)) {
                createdKnowledgeItems = this.knowledgeService.getBaseByCreateUserId(userId, start, size);
            }
            else {
                createdKnowledgeItems = this.knowledgeService.getByUserIdKeyWord(userId, keyWord, start, size);
            }
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }
        return createdKnowledgeItems;
    }

    private List<KnowledgeBase> getCollectedKnowledge(long userId, int start, int size,String keyword) throws Exception {
        List<KnowledgeCollect> collectItems = null;
        List<KnowledgeBase> collectedKnowledgeItems = null;
        try {
            collectItems = knowledgeOtherService.myCollectKnowledge(userId, (short) -1, start, size, keyword);
        } catch (Exception ex) {
            logger.error("invoke myCollectKnowledge failed. userId: " + userId + " error: " + ex.getMessage());
        }
        final int collectedSize  = collectItems != null ? collectItems.size() : 0;
        logger.info("get collected knowledge size : " + collectedSize + " , keyword: " + keyword);
        collectedKnowledgeItems = convertCollectedKnowledge(collectItems);
        //collectedKnowledgeItems = this.knowledgeService.getMyCollected(knowledgeIds,keyword);

        return collectedKnowledgeItems;
    }

    private InterfaceResult<DataCollect> knowledgeDetail(User user,long knowledgeId, int columnType,boolean isWeb) {
        if (user != null) {
            logger.info("Query knowledge detail. knowledgeId: " + knowledgeId + " userId: " + user.getId());
        }

        if(knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION, "知识Id无效");
        }

        Knowledge detail = null;
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            detail = this.knowledgeService.getDetailById(knowledgeId, columnType);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }

        //数据为空则直接返回异常给前端
        if(detail == null) {
            logger.error("get knowledge failed: knowledgeId: " + knowledgeId + ", columnId: " + columnType);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"获取知识失败!");
        }
        /*
        if (!isWeb) {
            String convertContent =  Utils.txt2Html(detail.getContent(), null, null, detail.getS_addr());
            detail.setContent(convertContent);
        }*/
        String hContent = HtmlToText.htmlToText(detail.getContent());
        int maxLen = hContent.length() >= 250 ? 250 : hContent.length();
        hContent = hContent.substring(0, maxLen);
        detail.setHcontent(hContent);
        detail.setVirtual(user.isVirtual() ? (short)2 : (short)1);
        if (StringUtils.isEmpty(detail.getColumnType())) {
            detail.setColumnType(String.valueOf(columnType));
        }

        boolean isCollected = false;
        long userId = user.getId();
        try {
            isCollected = knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, columnType);
        } catch (Exception ex) {
            logger.error("Query knowledge is collected or not failed: userId: " + userId + " knowledgeId: " + knowledgeId + ", columnType: " + columnType);
        }
        detail.setCollected(isCollected ? (short) 1 : (short) 0);
        DataCollect data = new DataCollect(null, detail);

        //Convert the time to long
        if (StringUtils.isNotBlank(detail.getCreatetime())) {
            final String newTime = KnowledgeUtil.parserTimeToUCT(detail.getCreatetime());
            detail.setCreatetime(newTime);
        }

        if (StringUtils.isNotBlank(detail.getModifytime())) {
            final String newTime = KnowledgeUtil.parserTimeToUCT(detail.getModifytime());
            detail.setModifytime(newTime);
        }

        if (StringUtils.isNotBlank(detail.getPerformTime())) {
            final String newTime = KnowledgeUtil.parserTimeToUCT(detail.getPerformTime());
            detail.setPerformTime(newTime);
        }

        if (StringUtils.isNotBlank(detail.getSysTime())) {
            final String newTime = KnowledgeUtil.parserTimeToUCT(detail.getSysTime());
            detail.setSysTime(newTime);
        }

        if (StringUtils.isNotBlank(detail.getSubmitTime())) {
            final String newTime = KnowledgeUtil.parserTimeToUCT(detail.getSubmitTime());
            detail.setSubmitTime(newTime);
        }

        Permission permission = permissionServiceLocal.getPermissionInfo(knowledgeId);
        //set a default value
        if (permission == null) {
            logger.info("Can't get knowledge permission, so set a default value. knowledgeId: " + knowledgeId);
            permission = DataCollect.defaultPermission(detail.getCid(), knowledgeId);
        }
        data.setPermission(permission);

        try {
            List<Associate> associateList = associateServiceLocal.getAssociateList(userId, knowledgeId);
            data.setAsso(associateList);
        } catch (Exception ex) {
            logger.error("get associate info failed: knowledgeId: " + knowledgeId + ", userId: " + userId + " error: " + ex.getMessage());
            //ex.printStackTrace();
        }

        result.setResponseData(data);
        logger.info(".......get knowledge detail complete......");

        //Click count this should be in queue
        try {
            KnowledgeCount count = knowledgeCountService.updateClickCount(userId, knowledgeId, (short)columnType);
            final long readCount = count != null ? count.getClickCount() : 0L;
            data.setReadCount(readCount);
        } catch (Exception ex) {
            logger.error("count knowledge read failed: knowledgeId: " + knowledgeId + ", columnId: " + columnType);
            ex.printStackTrace();
        }

        return result;
    }

    private long getKnowledgeCount(long userId)
    {
        int createCount = getCreatedKnowledgeCount(userId);
        long collectedCount = getCollectedKnowledgeCount(userId);
        logger.info("createCount: " + createCount +", collectedCount: " + collectedCount);

        return createCount + collectedCount;
    }

    private int getCreatedKnowledgeCount(long userId)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.getKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get created knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }

        logger.info("createCount: " + createCount);
        return createCount;
    }

    private long getCollectedKnowledgeCount(long userId)
    {
        long collectedCount = 0;
        try {
            collectedCount = knowledgeOtherService.myCollectKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get collected knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }
        logger.info("collectedCount: " + collectedCount);
        return collectedCount;
    }

    private List<IdName> getMinTagList(long userId, List<Long> tagIds)
    {
        List<Tag> tags = tagServiceLocal.getTagList(tagIds);
        if (tags != null && tags.size() >0) {
            List<IdName> minTags = new ArrayList<IdName>(tags.size());
            for (Tag tag : tags) {
                IdName pair = new IdName(tag.getId(), tag.getTagName());
                minTags.add(pair);
            }
            return minTags;
        }
        return null;
    }

    private List<IdNameType> getMinDirectoryList(long userId, List<Long> directoryIds)
    {
        List<Directory> directorys = directoryServiceLocal.getDirectoryList(userId, directoryIds);
        if (directorys != null && directorys.size() >0) {
            List<IdNameType> minDirectoryList = new ArrayList<IdNameType>(directorys.size());
            for (Directory dir : directorys) {
                IdNameType minDirectory = new IdNameType(dir.getId(), dir.getName(), dir.getTypeId());
                minDirectoryList.add(minDirectory);
            }
            return minDirectoryList;
        }
        return null;
    }

    private InterfaceResult<Page<KnowledgeBase>> knowledgeListPage(long total, int num, int size, List<KnowledgeBase> knowledgeBaseItems)
    {
        Page<KnowledgeBase> page = new Page<KnowledgeBase>();
        page.setTotalCount(total);
        page.setPageNo(num);
        page.setPageSize(size);
        page.setList(setReadCount(knowledgeBaseItems));
        //page.setList(knowledgeBaseItems);
        return InterfaceResult.getSuccessInterfaceResultInstance(page);
    }

    private DataSync createDynamicNewsDataSync(Knowledge detail, User user)
    {
        DataSync data = new DataSync();
        data.setIdType((short)5);
        data.setResId(detail.getId());
        data.setUserId(detail.getCid());
        data.setAction(EActionType.EAddDynamic.getValue());
        data.setContent(createDynamicNews(detail, user));
        return data;
    }

    private String createDynamicNews(Knowledge detail,User user)
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
        logger.info("create knowledge, set userPicPath: "+user.getPicPath());
        dynamic.setPeopleRelation(new ArrayList<RelationUserInfo>(0));
        dynamic.setComments(new ArrayList<DynamicComment>(0));
        dynamic.setPicturePaths(new ArrayList<Picture>(0));
        //dynamic.setVirtual(knowledge.getVirtual());
        return KnowledgeUtil.writeObjectToJson(dynamic);
    }

    private void initKnowledgeTime(DataCollect data)
    {
        if (data != null) {
            Knowledge detail = data.getKnowledgeDetail();
            if (detail != null) {
                if (StringUtils.isEmpty(detail.getCreatetime())) {
                    detail.setCreatetime(String.valueOf(System.currentTimeMillis()));
                }
                if (StringUtils.isEmpty(detail.getModifytime())) {
                    detail.setModifytime(String.valueOf(detail.getModifytime()));
                }
            }
        }
    }

    private List<KnowledgeBase> convertDetailListToBase(List<Knowledge> detailList,short type)
    {
        if (CollectionUtils.isEmpty(detailList)) {
            return null;
        }

        List<KnowledgeBase> baseList = new ArrayList<KnowledgeBase>(detailList.size());
        for (Knowledge detail : detailList) {
            KnowledgeBase base = DataCollect.generateKnowledge(detail, type);
            if (base != null) {
                baseList.add(base);
            }
        }
        return baseList;
    }

    private Knowledge columnTypeAndIdFaultTolerant(Knowledge detail)
    {
        if (StringUtil.inValidString(detail.getColumnType())) {
            logger.warn("column type is null, so set a default value");
            detail.setColumnType(String.valueOf(KnowledgeType.ENews.value()));
        } else {
            int columnType = KnowledgeUtil.parserShortType(detail.getColumnType());
            columnType = KnowledgeType.knowledgeType(columnType).value();
            detail.setColumnType(String.valueOf(columnType));
        }

        if (StringUtils.isBlank(detail.getColumnid())) {
            logger.warn("column Id is null, so set a default value");
            detail.setColumnid(String.valueOf(KnowledgeType.ENews.value()));
        } else {
            long columnId = KnowledgeUtil.parserStringIdToLong(detail.getColumnid());
            if (columnId <= 0) {
                logger.warn("column id: {} is invalidated, so set a default value. ", detail.getColumnid());
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
                logger.error("Query column failed. columnId: {}, error: {}", detail.getColumnid(), ex.getMessage());
            }

            if (columnPath == null) {
                logger.error("column path is null, so set a default value");
                columnPath =  KnowledgeType.ENews.typeName();
            }
            detail.setCpathid(columnPath);
        }

        return detail;
    }

    private ColumnSelf getColumn(long columnId)
    {
        if (columnId > 0) {
            try {
                logger.info("Query column by Id. columnId: "+columnId);
                return columnSelfService.selectByPrimaryKey(columnId);
            } catch (Exception ex) {
                logger.error("Get column failed: error {}", ex.getMessage());
            }
        }
        logger.error("ColumnId is invalidated. columnId: "+columnId);
        return null;
    }

    private List<KnowledgeBase> convertCollectedKnowledge(List<KnowledgeCollect> collectItems)
    {
        List<KnowledgeBase> collectedKnowledgeItems = null;
        if (CollectionUtils.isNotEmpty(collectItems)) {
            collectedKnowledgeItems =  new ArrayList<KnowledgeBase>(collectItems.size());
            for (KnowledgeCollect collect : collectItems) {
                if (collect != null) {
                    KnowledgeBase base = new KnowledgeBase();
                    base.setType((short)collect.getColumnId());
                    base.setId(collect.getKnowledgeId());
                    base.setKnowledgeId(collect.getKnowledgeId());
                    base.setTitle(collect.getKnowledgeTitle());
                    base.setCreateUserId(collect.getOwnerId());
                    collectedKnowledgeItems.add(base);
                }
            }
        }
        return collectedKnowledgeItems;
    }

    private boolean cancelKnowledge(final long userId, final long knowledgeId)
    {
        try {
            boolean isCollected = this.knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, -1);
            if (isCollected) {
                knowledgeOtherService.deleteCollectedKnowledge(userId, knowledgeId, -1);
                logger.info("cancel collected knowledge success. userId: " + userId + " knowledgeId: " + knowledgeId);
            }
            return isCollected;
        } catch (Exception ex) {
            logger.error("invoke knowledgeOtherService service failed. error: " + ex.getMessage());
            return false;
        }
    }
}