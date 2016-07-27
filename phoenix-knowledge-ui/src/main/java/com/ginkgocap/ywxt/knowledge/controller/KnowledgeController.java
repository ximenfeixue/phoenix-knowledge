package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.associate.exception.AssociateServiceException;
import com.ginkgocap.parasol.associate.exception.AssociateTypeServiceException;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.associate.model.AssociateType;
import com.ginkgocap.parasol.associate.service.AssociateService;
import com.ginkgocap.parasol.associate.service.AssociateTypeService;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.tags.model.Tag;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.parasol.column.entity.ColumnCustom;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnCustomService;
import com.ginkgocap.ywxt.dynamic.model.DynamicNews;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Page;
import com.ginkgocap.ywxt.knowledge.model.common.*;
import com.ginkgocap.ywxt.knowledge.model.mobile.*;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.service.DynamicNewsServiceLocal;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.service.PermissionCheckService;
import com.gintong.common.phoenix.permission.service.PermissionRepositoryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.frame.util.dto.Notification;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;

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
public class KnowledgeController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    KnowledgeHomeService knowledgeHomeService;

    @Autowired
    KnowledgeOtherService knowledgeOtherService;

    @Autowired
    ColumnCustomService columnCustomService;

    @Autowired
    private UserService userService;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private AssociateTypeService assoTypeService;

    @Autowired
    private PermissionRepositoryService permissionRepositoryService;

    @Autowired
    private PermissionCheckService permissionCheckService;

    @Autowired
    private TagServiceLocal tagServiceLocal;

    @Autowired
    private DirectoryServiceLocal directoryServiceLocal;

    @Autowired
    DynamicNewsServiceLocal dynamicNewsServiceLocal;

    @Autowired
    KnowledgeCountService knowledgeCountService;

    @Autowired
    JTFileService jtFileService;

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
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"知识详情错误");
        }
        initKnowledgeTime(data);

        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            data.serUserInfo(user);
            result = this.knowledgeService.insert(data);
        } catch (Exception e) {
            logger.error("Insert knowledge failed : " + e.getMessage());
            return result;
        }

        if (result == null || result.getNotification()== null || result.getResponseData() == null) {
            logger.error("Insert knowledge failed!");
            return result;
        }
        long knowledgeId = Long.valueOf(result.getResponseData().toString());

        Knowledge detail = data.getKnowledgeDetail();
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
            List<Associate> as  = data.getAsso();
            if (as != null) {
                assoMap = createAssociate(as, knowledgeId, userId);
            } else {
                logger.error("associate it null or converted failed, so skip to save!");
            }

        }catch (Throwable e) {
            logger.error("Insert associate failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        boolean syncToDynamic = false;
        try {
            Permission permission = permissionInfo(data.getPermission(), knowledgeId, userId);
            if (permission != null) {
                syncToDynamic = (permission.getPublicFlag() != 0 && data.getUpdateDynamic() == 1);
                permissionRepositoryService.insert(permission);
            }
        } catch (Exception e) {
            logger.error("Insert knowledge permission failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //Sync to dynamic news
        if (syncToDynamic) {
            String dynamicNews = createDynamicNews(detail);
            dynamicNewsServiceLocal.addDynamicToAll(dynamicNews, userId, assoMap);
            /*
            DataSync dataSync = createDynamicNewsDataSync(knowledgeDetail);
            if (dataSyncTask != null) {
                dataSyncTask.saveDataNeedSync(dataSync);
            }*/
        }

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

        long knowledgeId = detail.getId();
        InterfaceResult<Boolean> result = permissionCheckService.isUpdatable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
        if (result == null || result.getResponseData() == null || !result.getResponseData().booleanValue()) {
            logger.error("permission validate failed, please check if user have permission!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION,"No permission to update!");
        }

        try {
            data.serUserInfo(user);
            result = this.knowledgeService.update(data);
        } catch (Exception e) {
            logger.error("知识更新失败！失败原因："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION);
        }

        if (result == null || !CommonResultCode.SUCCESS.getCode().equals(result.getNotification().getNotifCode())) {
            logger.error("知识更新失败！");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }

        try {
            Permission permission = permissionInfo(data.getPermission(), knowledgeId, userId);
            if (permission != null) {
                permissionRepositoryService.update(permission);
            }
        } catch (Exception e) {
            logger.error("Update knowledge permission failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //Update tag info
        tagServiceLocal.updateTagSource(userId, detail);

        //Update Directory info
        directoryServiceLocal.updateDirectorySource(userId, detail);

        //Update Assso info
        try {
            Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, (long) KnowledgeBaseService.sourceType, knowledgeId);
            if (assomap == null) {
                logger.error("asso it null or converted failed...");
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
            }
            //TODO: If this step failed, how to do ?
            for (Iterator i = assomap.values().iterator(); i.hasNext(); ) {
                List<Associate> associateList = (List) i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    associateService.removeAssociate(APPID, user.getId(), associateList.get(j).getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("Asso update failed！reason：" + e.getMessage());
        }

        List<Associate> as = data.getAsso();
        createAssociate(as, knowledgeId, userId);

        logger.info(".......update knowledge success......");
        return result;
    }

    /**
     * 删除数据
     * @param knowledgeId 知识主键
     * @param columnId 栏目主键
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value="/{knowledgeId}/{columnId}", method = RequestMethod.DELETE)
    public InterfaceResult delete(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable long knowledgeId,@PathVariable int columnId) throws Exception {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if(knowledgeId <= 0 || columnId <= 0){
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        long userId = user.getId();
        InterfaceResult<Boolean> result = permissionCheckService.isDeletable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
        if (result == null || result.getResponseData() == null || !result.getResponseData().booleanValue()) {
            logger.error("permission validate failed, please check if user have permission!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION,"permission validate failed.");
        }

        try {
            result = this.knowledgeService.deleteByKnowledgeId(knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("knowledge delete failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"删除知识失败!");
        }

        //delete tags
        tagServiceLocal.deleteTags(userId, knowledgeId);

        //delete directory
        directoryServiceLocal.deleteDirectory(userId, knowledgeId);
        //delete Assso info
        AssociateType assoType = assoTypeService.getAssociateTypeByName(APPID,"知识");
        Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, 7L, knowledgeId);
        if (assomap == null) {
            logger.error("asso id is null or converted failed...");
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
        }
        //TODO: If this step failed, how to do ?
        try {
            for (Iterator i = assomap.values().iterator(); i.hasNext(); ) {
                List<Associate> associateList = (List) i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    associateService.removeAssociate(APPID, userId, associateList.get(j).getId());
                }
            }
        } catch (AssociateServiceException ex) {
            logger.error("delete Associate failed: {}", ex.getMessage());
        }
        catch (Exception ex) {
            logger.error("delete Associate failed: {}", ex.getMessage());
            ex.printStackTrace();
        }

        //delete permission info
        deletePermissionInfo(userId, knowledgeId);

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
            InterfaceResult<Boolean> result = permissionCheckService.isDeletable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
            if (result == null || !result.getResponseData().booleanValue()) {
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
            deleteAssociate(knowledgeId, user.getId());
            //delete tags
            tagServiceLocal.deleteTags(userId, knowledgeId);

            //delete directory
            directoryServiceLocal.deleteDirectory(userId, knowledgeId);

            //delete permission information
            deletePermissionInfo(userId, knowledgeId);
        }
        String resp = "successId: "+permDeleteIds.toString()+","+"failedId: " + failedIds.toString();
        logger.info("delete knowledge success: {}", resp);

        result.setResponseData(resp);
        return result;
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param columnId 栏目主键
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/{knowledgeId}/{columnId}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detail(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable int columnId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }


        if(knowledgeId <= 0) {
            return mappingJacksonValue(CommonResultCode.PARAMS_NULL_EXCEPTION,"知识Id无效");
        }

        Knowledge detail = null;
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            detail = this.knowledgeService.getDetailById(knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }

        //数据为空则直接返回异常给前端
        if(detail == null) {
            logger.error("get knowledge failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"获取知识详情失败!");
            return mappingJacksonValue(result);
        }
        String hContent = HtmlToText.htmlToText(detail.getContent());
        int maxLen = hContent.length() >= 250 ? 250 : hContent.length();
        hContent = hContent.substring(0, maxLen);
        detail.setHcontent(hContent);
        detail.setVirtual(user.isVirtual() ? (short)2 : (short)1);
        DataCollect data = new DataCollect(null, detail);

        boolean isCollected = false;
        long userId = user.getId();
        try {
            isCollected = knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, columnId);
        } catch (Exception ex) {
            logger.error("Query knowledge is collected or not failed: userId: {}, knowledgeId: {}, columnId: {}", userId, knowledgeId, columnId);
        }
        detail.setCollected(isCollected ? (short) 1 : (short) 0);

        try {
            InterfaceResult<Permission> ret = permissionRepositoryService.selectByRes(knowledgeId, ResourceType.KNOW, APPID);
            Notification noti = ret.getNotification();
            if (noti != null && noti.getNotifCode().equals(CommonResultCode.SUCCESS.getCode())) {
                data.setPermission(ret.getResponseData());
            }
            else {
                logger.error("can't get knowledge permission info: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            }
        }catch (Exception ex) {
            logger.error("get knowledge permission info failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            ex.printStackTrace();
        }

        try {
            List<Associate> associateList = associateService.getAssociatesBySourceId(APPID, user.getId(), knowledgeId);
            data.setAsso(associateList);
        } catch (Exception ex) {
            logger.error("get knowledge associate info failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            ex.printStackTrace();
        }

        logger.info(".......get knowledge detail complete......");
        MappingJacksonValue jacksonValue = knowledgeDetail(data);

        //Click count this should be in queue
        try {
            //knowledgeCountService.updateClickCount(knowledgeId);
        } catch (Exception ex) {
            logger.error("count knowledge click failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            ex.printStackTrace();
        }

        return jacksonValue;
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param columnId 栏目主键
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/web/{knowledgeId}/{columnId}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailWeb(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable long knowledgeId,@PathVariable int columnId) throws Exception {
        User user = this.getUser(request);
        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, columnId);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if (result != null) {
            DataCollect data = result.getResponseData();
            if (data == null || data.getKnowledgeDetail() == null) {
                logger.error("get knowledge detail failed: knowledgeId: {}", knowledgeId);
                return jacksonValue;
            }
            long userId = user.getId();
            Knowledge detail = data.getKnowledgeDetail();
            List<Long> tags = detail.getTagList();
            List<Long> directoryIds = detail.getDirectorys();
            List<IdName> minTags = this.getMinTagList(userId, tags);
            List<IdNameType> minDirectoryList = this.getMinDirectoryList(userId, directoryIds);
            logger.debug("get minTags: {} minDirectoryList: {}", minTags, minDirectoryList);
            ColumnCustom columnCustom = getColumn(detail.getColumnid());
            IdName column = columnCustom != null ? new IdName(columnCustom.getId(), columnCustom.getColumnname()) : null;
            KnowledgeWeb webDetail = new KnowledgeWeb(detail, minTags, minDirectoryList, column);
            data.setKnowledgeDetail(webDetail);
            jacksonValue = knowledgeDetail(data);
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

        System.err.println("----keyWord: " + keyword+"\n");
        byte[] byteWord = keyword.getBytes("ISO-8859-1"); //以"ISO-8859-1"方式解析name字符串
        String keyWord1= new String(byteWord, "UTF-8");
        logger.info("---keyWord: {}", keyWord1);
        System.err.println("----keyWord: " + keyWord1+"\n");

        long userId = user.getId();
        Map<String, List<KnowledgeBase>> resultMap = new HashMap<String, List<KnowledgeBase>>();
        List<KnowledgeBase> createdKnowledgeItems = this.getCreatedKnowledge(userId, start, size, keyword);
        if (createdKnowledgeItems != null && createdKnowledgeItems.size() > 0 ) {
            resultMap.put("created", createdKnowledgeItems);
        }

        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledge(userId, start, size, keyword);
        if (collectedKnowledgeItems != null && collectedKnowledgeItems.size() > 0) {
            resultMap.put("collected", collectedKnowledgeItems);
        }

        logger.info(".......get all knowledge success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(resultMap);
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
                logger.info("get created knowledge size: {}", createdKnowledgeList.size());
                return knowledgeListPage(total, page, size, createdKnowledgeList);
            }
        }

        if (createdKnowledgeList != null && createdKnowledgeList.size() > 0) {
            int restSize = size - createdKnowledgeList.size();
            List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledge(userId, 0, restSize, keyword);
            logger.info("get created knowledge size: {} collected size: {}", createdKnowledgeList.size(), collectedKnowledgeList.size());
            createdKnowledgeList.addAll(collectedKnowledgeList);
            return knowledgeListPage(total, page, createdKnowledgeList.size(), createdKnowledgeList);
        }


        page = gotTotal - createCount;
        List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledge(userId, page, size, keyword);
        if (collectedKnowledgeList != null && collectedKnowledgeList.size() > 0) {
            logger.info("get collected size: {}", collectedKnowledgeList.size());
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
     * 提取所有知识数据
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
     * 提取所有知识数据
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
            total = getCollectedKnowledgeCount(userId);
        }

        int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> createdKnowledgeList = this.getCreatedKnowledge(userId, start, size, keyword);

        InterfaceResult<Page<KnowledgeBase>> result = this.knowledgeListPage(total, page, size, createdKnowledgeList);
        logger.info(".......get all created knowledge success. size: {}", createdKnowledgeList.size());
        return result;
    }

    /**
     * 提取所有知识数据
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
     * 提取所有知识数据
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
        logger.info(".......get all collected knowledge success. size: {}", collectedKnowledgeList.size());
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
            logger.error("Query knowledge failed！reason：{}",e.getMessage());
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
                knowledgeBaseItems = this.knowledgeService.getBaseByKeyWord(usrId, start, size, keyWord);
            }
            result.setResponseData(knowledgeBaseItems);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}",e.getMessage());
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
            logger.error("Query knowledge failed！reason：{}",e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeBasesItems);
    }


    @ResponseBody
    @RequestMapping(value = "/allKnowledgeByColumnAndSource/{type}/{columnId}/{source}/{page}/{size}/{total}", method = RequestMethod.GET)
    public InterfaceResult<Page<KnowledgeBase>> getKnowledgeByColumnAndSource(HttpServletRequest request, HttpServletResponse response,
                                                                              @PathVariable short type,@PathVariable int columnId,
                                                                              @PathVariable short source, @PathVariable int page,
                                                                              @PathVariable int size, @PathVariable long total) throws Exception
    {
        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        if(type <= 0 || columnId <= 0 || page < 0 || size <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        long userId = this.getUserId(user);
        int start = page * size;
        List<KnowledgeBase> knowledgeList = null;
        if (source == KnowledgeConstant.SOURCE_GINTONG_BRAIN) {
            //First get total;
            userId = KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID;
            List<ColumnSelf> columnList = columnCustomService.queryListByPidAndUserId((long)columnId,userId);
            String [] idList = getColumnIds(columnList, columnId);
            if (total == -1) {
                logger.info("begin to get knowledge count:");
                total = knowledgeHomeService.getKnowledgeCountByUserIdAndColumnID(idList, (long)KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID, type);
                logger.info("end to get knowledge count:" + total);
            }
            if (total > 0 && start < total) {
                logger.info("start to get knowledge:" + total);
                List<Knowledge> detailList = this.knowledgeHomeService.getKnowledge(idList, userId, type, start, size);
                logger.info("end to get knowledge: size: " + (detailList != null ? detailList.size() : 0));
                knowledgeList = convertKnowledgeDetailListToBase(detailList);
                logger.info("convert knowledge size: " + (knowledgeList != null ? knowledgeList.size() : 0));
            } else {
                return queryKnowledgeEnd();
            }
        }
        else if (source == KnowledgeConstant.SOURCE_ALL_PLATFORM) {
            ColumnCustom column = columnCustomService.queryByCid((long) type);
            if (total == -1) {
                total = 9999L; //default value//this.knowledgeService.getBasePublicCountByColumnId(type, KnowledgeConstant.PRIVATED);
            }
            if (column == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "获取栏目信息失败，请检查栏目Id是否正确!");
            }
            else if (total > 0 && start < total) {
                List<Knowledge> detailList = this.knowledgeHomeService.getAllByParam(type, column.getPathName(), columnId, userId, page, size);
                knowledgeList = convertKnowledgeDetailListToBase(detailList);
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
        else {
            if (total == -1) {
                total = this.knowledgeService.getBaseAllPublicCount(KnowledgeConstant.PRIVATED);
            }
            if (total > 0 && start < total) {
                knowledgeList = this.knowledgeService.getBaseAllPublic(start, size, KnowledgeConstant.PRIVATED);
            } else {
                return queryKnowledgeEnd();
            }
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

            ColumnCustom column = columnCustomService.queryByCid((long) type);
            if (column != null) {
                type = column.getType().shortValue();
            }
            ColumnCustom c = null;
            ColumnCustom co = null;
            co = columnCustomService.queryByCid((long) type);// 一级栏目
            c = columnCustomService.queryByCid((long) columnId);// 当前栏目

            // 获取栏目列表
            Map<String, Object> model = new HashMap<String, Object>(20);
            List<ColumnSelf> cl = columnCustomService.queryListByPidAndUserId((long)type, userId);
            model = putKnowledge(model, type, columnId, userId, page, size);
            model.put("cl", cl);
            model.put("column", c);
            model.put("columnone", co);
            model.put("columnid", columnId);
        } catch (Exception e) {
            logger.error("查询栏目出错,错误信息:{}", e.toString());
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
            logger.error("Query knowledge failed！reason：{}",e.getMessage());
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
            logger.error("Query knowledge failed！userId: {} reason：{}", userId, e.getMessage());
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
        List<Long> knowledgeIds = tagServiceLocal.getKnowlegeIdsByTagId(tagId, start, size);
        if (knowledgeIds == null || knowledgeIds.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS,"No result.");
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

        List<KnowledgeBase> knowledgeBaseList = null;
        try {
            List<Long> knowledgeIds = directoryServiceLocal.getKnowledgeIdListByDirectoryId(user.getId(), directoryId, start, size);
            if (knowledgeIds == null || knowledgeIds.size() <= 0) {
                logger.error("get knowledge list is null by directoryId: " + directoryId);
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"get knowledge list is null this directoryId");
            }

            knowledgeBaseList = this.knowledgeService.getBaseByIds(knowledgeIds);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}", e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"Query knowledge failed");
        }
        logger.info(".......get all knowledge by columnId success......");
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
    @RequestMapping(value = "/collect/{knowledgeId}/{columnId}", method = RequestMethod.POST)
    public InterfaceResult<DataCollection> collect(HttpServletRequest request, HttpServletResponse response,
                                                   @PathVariable long knowledgeId, @PathVariable int columnId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

        if (knowledgeId <= 0 || columnId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeOtherService.collectKnowledge(userId, knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("collect knowledge failed！：" + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance();
        }

        //collect count
        //knowledgeCountService.updateCollectCount(knowledgeId);
        logger.info(".......collect knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /**
     * 取消收藏的知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/collect/{knowledgeId}/{columnId}", method = RequestMethod.DELETE)
    public InterfaceResult<DataCollection> cancelCollection(HttpServletRequest request, HttpServletResponse response,
                                                            @PathVariable long knowledgeId, @PathVariable int columnId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = user.getId();
        if (knowledgeId <= 0 || columnId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeOtherService.deleteCollectedKnowledge(userId, knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("cancel collected knowledge failed！：" + e.getMessage());
        }
        logger.info(".......cancel collect knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /**
     * 保存知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/save/{knowledgeId}/{columnId}", method = RequestMethod.POST)
    public InterfaceResult<DataCollection> save(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable long knowledgeId, @PathVariable int columnId) throws Exception {

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
        if (batchItems == null || batchItems.size() <= 0) {
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
            return this.directoryServiceLocal.batchCatalogs(knowledgeService, user.getId(), requestJson);
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
        List<Long> tagIds = KnowledgeUtil.readValue(List.class, requestJson);
        if (tagIds == null || tagIds.size() <= 0) {
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
        List<Long> tagIds = KnowledgeUtil.readValue(List.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (tagIds == null || tagIds.size() <= 0) {
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
        List<Long> directoryIds = KnowledgeUtil.readValue(List.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (directoryIds == null || directoryIds.size() <= 0) {
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
        List<Long> directoryIds = KnowledgeUtil.readValue(List.class, requestJson);
        //String [] ids = KnowledgeUtil.readValue(List.class, requestJson);requestJson.split(",");
        if (directoryIds == null || directoryIds.size() <= 0) {
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

        long userId = user.getId();
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
        userKnowledgeBase = knowledgeService.getBaseByCreateUserId(user.getId(), start, size);

        if (userKnowledgeBase != null && userKnowledgeBase.size() > 0) {
            userKnowledge = convertKnowledgeBaseToMini(userKnowledgeBase);
        }

        responseDataMap.put("listPlatformKnowledge", platformKnowledge);
        responseDataMap.put("listUserKnowledge", userKnowledge);
        responseDataMap.put("type", type);
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    private List<JSONObject>  getSearchList(HttpServletRequest request,long userId, int pno, int psize, String keyword, String module,Map<String, String> conditions) throws Exception {

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

        List<Knowledge> knowledgeList = knowledgeHomeService.getAllByParam(type, null, columnId, userId, page, size);
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

        return km;
    }

    /*
        private List<KnowledgeMini2> changeKnowledgeMini2(List<DataCollection> data) {
        List<KnowledgeMini2> kbds = new ArrayList<KnowledgeMini2>();
        for (DataCollection collection : data) {
            KnowledgeBase kbd = collection.getKnowledge();
            if (kbd != null) {
                changeKnowledgeToMini2(kbd);
            }
        }
        return kbds;
    }

    public List<KnowledgeMini2> convertKnowledgeBaseList(List<KnowledgeBase> baseList)
    {
        if (baseList == null || baseList.size() <= 0) {
            return null;
        }

        List<KnowledgeMini2> km2List =  new ArrayList<KnowledgeMini2>(baseList.size());
        for(KnowledgeBase base : baseList) {
            km2List.add(changeKnowledgeToMini2(base));
        }

        return km2List;
    }

    public KnowledgeMini2 changeKnowledgeToMini2(KnowledgeBase knowledgeBase)
    {
        KnowledgeMini2 km2 = new KnowledgeMini2();

        //分享给我的知识专用
        //km2.setShareMeId(knowledgeBase.getShareMeId());

        Connections connections = new Connections();
        // 作者id
        long userId = knowledgeBase.getCreateUserId();
        if(userId>0) {//判断是否传过来知识作者id - 剔除 全平台与金桐网
            User detailUser = userService.selectByPrimaryKey(userId);
            if(null != detailUser) {
                connections.setType(detailUser.getType());
                connections.setId(userId);//存在隐患
                if(detailUser.getType()==1) {
                    JTContactMini mini = new JTContactMini();//参考loginConfiguration 175 ConnectionsController 1909
                    mini.setId(userId+"");
                    mini.setName(detailUser.getName());
                    mini.setLastName("");
                    mini.setNameChar(detailUser.getNameFirst());
                    mini.setImage(detailUser.getPicPath());
                    mini.setCompany(detailUser.getCompanyName());
                    List<String> emials = new ArrayList<String>();
                    emials.add(detailUser.getEmail());
                    mini.setListEmail(emials);

                    mini.setOwnerid(userId);
                    mini.setOwnername(detailUser.getName());
                    connections.setJtContactMini(mini);
                } else {
                    OrganizationMini mini = new OrganizationMini();
                    mini.setId("" + userId);
                    mini.setOwnerid(userId);
                    mini.setIsOnline(true);
                    mini.setFullName(detailUser.getName());
                    mini.setOwnername(detailUser.getName());
                    mini.setShortName(detailUser.getShortName());
                    connections.setOrganizationMini(mini);
                }
            }
            km2.setConnections(connections);// 关系对象
        }
        km2.setUrl("");//知识链接
        km2.setType(knowledgeBase.getColumnId());//知识类型
        km2.setColumntype(knowledgeBase.getColumnId());
        km2.setId(knowledgeBase.getKnowledgeId());// 知识id
        km2.setTag(knowledgeBase.getTags());// 标签

        Knowledge kn = null;
        if(knowledgeBase.getKnowledgeId() !=0 && knowledgeBase.getColumnId() != 0) {
            try {
                kn = knowledgeService.getDetailById(knowledgeBase.getKnowledgeId(), knowledgeBase.getColumnId());
            } catch (Exception ex) {
                logger.error("Get Knowledge Detail failed: KnowledgeId: {}, ColumnId: {}, error: {}",
                        knowledgeBase.getKnowledgeId(), knowledgeBase.getColumnId(), ex.getMessage());
            }
        }
        if(kn!=null) {
            km2.setListTag(kn.getTagList());//标签
            km2.setSource(kn.getSource());// 来源
            km2.setTitle(kn.getTitle());// 标题
            km2.setPic(changeImage(kn.getPic()));// 封面地址
            km2.setDesc(getDesc(kn.getDesc()));// 描述
            km2.setModifytime(Long.parseLong(kn.getModifytime()));// 最后修改时间
        } else {
            List<Long> tags = KnowledgeUtil.convertStringToLongList(knowledgeBase.getTags());
            km2.setListTag(tags);//标签
            km2.setSource(knowledgeBase.getSource());// 来源
            km2.setTitle(knowledgeBase.getTitle());// 标题
            km2.setPic(changeImage(knowledgeBase.getCoverPic()));// 封面地址
            km2.setDesc(knowledgeBase.getContentDesc());// 描述
            km2.setModifytime(knowledgeBase.getModifyDate());// 最后修改时间
        }
        km2.setColumnpath(getDisposeString(knowledgeBase.getCpath()));
        return km2;
    }*/

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

    private String getDisposeString(String str) {

        if(null == str || str.equals("")) {
            return str;
        }
        String[] tempStr = str.split("/");

        if (tempStr.length > 0) {
            return tempStr[0];
        }

        return str;
    }

    private Map<Long, List<Associate>> createAssociate(List<Associate> as, long knowledgeId, long userId)
    {
        if (as == null || as.size() <= 0) {
            return null;
        }

        //now only 4 type asso
        Map<Long, List<Associate>> assoMap = new HashMap<Long, List<Associate>>(4);
        for (int index = 0; index < as.size(); index++) {
            Associate associate = as.get(index);
            associate.setSourceId(knowledgeId);
            associate.setSourceTypeId(KnowledgeBaseService.sourceType);
            //associate.setAssocTypeId(assoType.getId());
            associate.setUserId(userId);
            associate.setAppId(APPID);
            try {
                long assoId = associateService.createAssociate(APPID, userId, associate);
                if (assoMap.get(associate.getAssocTypeId()) == null) {
                    List<Associate> assoList = new ArrayList<Associate>(2);
                    assoMap.put(associate.getAssocTypeId(), assoList);
                }
                assoMap.get(associate.getAssocTypeId()).add(associate);
                logger.info("assoid:" + assoId);
            }catch (AssociateServiceException e) {
                logger.error("update Asso failed！reason：" + e.getMessage());
            } catch (Throwable e) {
                logger.error("update Asso failed！reason：" + e.getMessage());
            }
        }

        return assoMap;
    }

    private InterfaceResult deleteAssociate(long knowledgeId, long userId)
    {
        AssociateType assoType = null;
        try {
            assoType = assoTypeService.getAssociateTypeByName(APPID,"知识");
            if (assoType == null) {
                logger.error("can't get assoType for knowledge...");
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
            }
            Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, assoType.getId(), knowledgeId);
            if (assomap == null) {
                logger.error("asso item null or converted failed...");
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
            }
            //TODO: If this step failed, how to do ?
            for (Iterator i =  assomap.values().iterator(); i.hasNext();) {
                List<Associate> associateList = (List)i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    try {
                        associateService.removeAssociate(APPID, userId, associateList.get(j).getId());
                    }catch (Exception ex) {
                        logger.error("delete Associate failed, reason: {}", ex.getMessage());
                    }
                }
            }
        } catch (AssociateTypeServiceException ex) {
            logger.error("delete Associate failed, reason: {}", ex.getMessage());
        }
        catch (AssociateServiceException ex) {
            logger.error("delete Associate failed, reason: {}", ex.getMessage());
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    private List<KnowledgeBase> getCreatedKnowledge(long userId, int start, int size, String keyWord)
    {
        List<KnowledgeBase> createdKnowledgeItems = null;
        try {
            if (keyWord == null || "null".equals(keyWord)) {
                createdKnowledgeItems = this.knowledgeService.getBaseByCreateUserId(userId, start, size);

            }
            else {
                createdKnowledgeItems = this.knowledgeService.getBaseByKeyWord(userId, start, size, keyWord);
            }
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }
        return createdKnowledgeItems;
    }

    private List<KnowledgeBase> getCollectedKnowledge(long userId, int start, int size,String keyword) throws Exception {
        List<KnowledgeBase> collectedKnowledgeItems = null;
        List<KnowledgeCollect> collectItems = knowledgeOtherService.myCollectKnowledge(userId, (short)-1, start, size);
        if (collectItems != null && collectItems.size() > 0) {
            List<Long> knowledgeIds =  new ArrayList<Long>(collectItems.size());
            for (KnowledgeCollect collect : collectItems) {
                if (!knowledgeIds.contains(collect.getKnowledgeId())) {
                    knowledgeIds.add(collect.getKnowledgeId());
                }
            }
            logger.info(" knowledgeIds: {}, keyword: {}", knowledgeIds, keyword);
            collectedKnowledgeItems = this.knowledgeService.getMyCollected(knowledgeIds,keyword);
        }

        return collectedKnowledgeItems;
    }

    private Permission permissionInfo(Permission permission,long knowledgeId,long userId)
    {
        if (permission != null) {
            permission.setAppId(APPID);
            permission.setResId(knowledgeId);
            permission.setResOwnerId(userId);
            permission.setResType(ResourceType.KNOW.getVal());
            if (permission.getPerTime() == null) {
                permission.setPerTime(new Date());
            }
        }
        return permission;
    }

    private boolean deletePermissionInfo(long userId,long knowledgeId)
    {
        try {
            Permission permission = permissionInfo(new Permission(), knowledgeId, userId);
            InterfaceResult<Boolean> ret = permissionRepositoryService.delete(permission);
            if (ret == null || !ret.getResponseData()) {
                logger.error("Delete Permission failed, knowledgeId: " + knowledgeId);
            }
        }catch (Exception ex) {
            logger.error("Delete Permission failed, knowledgeId: " + knowledgeId + " Reason: "+ex.getMessage());
            return false;
        }

        return true;
    }

    private InterfaceResult<DataCollect> knowledgeDetail(User user,long knowledgeId, int columnId) {
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if(knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION, "知识Id无效");
        }

        Knowledge detail = null;
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            detail = this.knowledgeService.getDetailById(knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }

        //数据为空则直接返回异常给前端
        if(detail == null) {
            logger.error("get knowledge failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"获取知识失败!");
        }
        String hContent = HtmlToText.htmlToText(detail.getContent());
        int maxLen = hContent.length() >= 250 ? 250 : hContent.length();
        hContent = hContent.substring(0, maxLen);
        detail.setHcontent(hContent);
        detail.setVirtual(user.isVirtual() ? (short)2 : (short)1);
        DataCollect data = new DataCollect(null, detail);

        boolean isCollected = false;
        long userId = user.getId();
        try {
            isCollected = knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, columnId);
        } catch (Exception ex) {
            logger.error("Query knowledge is collected or not failed: userId: {}, knowledgeId: {}, columnId: {}", userId, knowledgeId, columnId);
        }
        detail.setCollected(isCollected ? (short) 1 : (short) 0);

        try {
            InterfaceResult<Permission> ret = permissionRepositoryService.selectByRes(knowledgeId, ResourceType.KNOW, APPID);
            Notification noti = ret.getNotification();
            if (noti != null && noti.getNotifCode().equals(CommonResultCode.SUCCESS.getCode())) {
                data.setPermission(ret.getResponseData());
            }
            else {
                logger.error("can't get knowledge permission info: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            }
        }catch (Exception ex) {
            logger.error("get knowledge permission info failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            ex.printStackTrace();
        }

        try {
            List<Associate> associateList = associateService.getAssociatesBySourceId(APPID, user.getId(), knowledgeId);
            data.setAsso(associateList);
        } catch (Exception ex) {
            logger.error("get knowledge associate info failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            ex.printStackTrace();
        }

        result.setResponseData(data);
        logger.info(".......get knowledge detail complete......");

        return result;
    }

    private long getKnowledgeCount(long userId)
    {
        int createCount = getCreatedKnowledgeCount(userId);
        long collectedCount = getCollectedKnowledgeCount(userId);
        logger.info("createCount: {}, collectedCount: {}", createCount, collectedCount);

        return createCount + collectedCount;
    }

    private int getCreatedKnowledgeCount(long userId)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.getKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get created knowledge count failed: userId: {}, error: {}", userId, ex.getMessage());
        }

        logger.info("createCount: {}", createCount);

        return createCount;
    }

    private long getCollectedKnowledgeCount(long userId)
    {
        long collectedCount = 0;
        try {
            collectedCount = knowledgeOtherService.myCollectKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get collected knowledge count failed: userId: {}, error: {}", userId, ex.getMessage());
        }
        logger.info("collectedCount: {}", collectedCount);

        return collectedCount;
    }

    private List<IdName> getMinTagList(long userId, List<Long> tagIds)
    {
        List<Tag> tags = tagServiceLocal.getTagList(userId, tagIds);
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
        page.setList(knowledgeBaseItems);
        return InterfaceResult.getSuccessInterfaceResultInstance(page);
    }

    private DataSync createDynamicNewsDataSync(Knowledge detail)
    {
        DataSync data = new DataSync();
        data.setIdType((short)5);
        data.setResId(detail.getId());
        data.setUserId(detail.getCid());
        data.setAction(EActionType.EAddDynamic.getValue());
        data.setContent(createDynamicNews(detail));
        return data;
    }

    private String createDynamicNews(Knowledge detail)
    {
        DynamicNews dynamic = new DynamicNews();
        dynamic.setType("10"); //创建知识
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
        dynamic.setKnowledgeCount(1);
        String createType = detail.getVirtual() == 1 ? "1" : "2";
        dynamic.setCreateType(createType);
        dynamic.setScope(String.valueOf(0));
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

    private List<KnowledgeBase> convertKnowledgeDetailListToBase(List<Knowledge> detailList)
    {
        if (detailList == null || detailList.size() <=0) {
            return null;
        }

        List<KnowledgeBase> baseList = new ArrayList<KnowledgeBase>(detailList.size());
        for (Knowledge detail : detailList) {
            KnowledgeBase base = DataCollect.generateKnowledge(detail);
            if (base != null) {
                baseList.add(base);
            }
        }
        return baseList;
    }

    private ColumnCustom getColumn(String columnId)
    {
        long id = KnowledgeUtil.parserStringIdToLong(columnId);
        if (id > 0) {
            return columnCustomService.queryByCid(id);
        }
        logger.error("ColumnId is invalidated. columnId: "+columnId);
        return null;
    }
}