package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.*;
import com.ginkgocap.ywxt.knowledge.model.common.Page;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.organ.model.Enum.OrganResourcePermissionTypeEnum;
import com.ginkgocap.ywxt.organ.model.Enum.OrganSourceTypeEnum;
import com.ginkgocap.ywxt.organ.model.organ.OrganMember;
import com.ginkgocap.ywxt.organ.model.organ.OrganResource;
import com.ginkgocap.ywxt.organ.model.organ.OrganResourceVO;
import com.ginkgocap.ywxt.organ.service.organ.OrganMemberService;
import com.ginkgocap.ywxt.organ.service.organ.OrganResourceService;
import com.ginkgocap.ywxt.track.entity.constant.BusinessModelEnum;
import com.ginkgocap.ywxt.track.entity.constant.OptTypeEnum;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.utils.JsonUtils;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.parasol.column.entity.ColumnSelf;
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
import java.util.concurrent.Executors;

import static com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService.sourceType;

@Controller
@RequestMapping("/knowledge")
public class KnowledgeController extends BaseKnowledgeController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeController.class);

    //@Autowired
    //private KnowledgeHomeService knowledgeHomeService;

    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    //@Value("#{configuers.knowledgeBigDataSearchUrl}")
    //private String knowledgeBigDataSearchUrl;
    @Autowired
    private OrganMemberService organMemberService;

    @Autowired
    private OrganResourceService organResourceService;

    private ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
    // 贡献类型
    private static final byte contributeType = 2;


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
        return this.create(request, user);
    }

    /**
     * 组织插入数据
     * 与用户插入 不同在于 前端传入 virtual = 2 且 cid = organId cname 还是 当前用户名字
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/organ", method = RequestMethod.POST)
    public InterfaceResult createByOrgan(HttpServletRequest request) throws Exception
    {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        return this.create(request, user);
    }

    /**
     * 组织更新数据
     * 与用户插入 不同在于 前端传入 virtual = 2 且 cid = organId cname 还是 当前用户名字
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/organ", method = RequestMethod.PUT)
    public InterfaceResult updateKnowledgeByOrgan(HttpServletRequest request) throws Exception
    {
        User user = this.getUser(request);
        if (user == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

        return this.updateKnowledge(request, user);
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
        return updateKnowledge(request, user);
    }

    /**
     * 批量贡献资源
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/organ/batchContribute", method = RequestMethod.POST)
    public InterfaceResult batchContributeKnowledge(HttpServletRequest request) {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        String requestJson = this.getBodyParam(request);
        ContributeVO contributeVO = null;
        try {
            contributeVO = (ContributeVO) JsonUtils.jsonToBean(requestJson, ContributeVO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (contributeVO == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        // 批量贡献资源
        batchContribute(contributeVO, user, request);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /**
     * 贡献资源
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/organ/contribute", method = RequestMethod.POST)
    public InterfaceResult contributeKnowledge(HttpServletRequest request) {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        String requestJson = this.getBodyParam(request);
        ContributeData data = null;
        try {
            data = (ContributeData) JsonUtils.jsonToBean(requestJson, ContributeData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);

        return contribute(data, user, request);
    }

    private InterfaceResult contribute(ContributeData contributeData, User user, HttpServletRequest request) {

        Long id = contributeData.getId();
        short columnType = contributeData.getColumnType();
        Long organId = contributeData.getOrganId();
        Knowledge detail = null;
        try {
            detail = knowledgeService.getDetailById(id, columnType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (detail == null) {
            logger().error("知识 id 和 columnType 传入有误。 id : " + id + " columnType : " + columnType);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "当前知识已被删除");
        }
        OrganResource organResource = null;
        try {
            organResource = organResourceService.getContributedOrganResource(organId, OrganSourceTypeEnum.KNOWLEDGE.value(), id, contributeType, user.getId());
        } catch (Exception e) {
            logger().error("invoke organResourceService failure. method : " +
                    "[getContributedOrganResource]. organId : " + organId + " userId : " + user.getId());
        }
        // 已贡献过该资源
        if (organResource != null){
            logger().info("this know has been contributed. id : " + id + " type : " + columnType);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "已贡献过该资源");
        }
        DataCollect dataCollect = new DataCollect();
        setDataCollect(detail, dataCollect, organId);
        //  创建知识
        return this.createKnowledge(dataCollect, user, request);
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

        //Async delete knowledge related resource
        IdTypeUid idTypeUid = new IdTypeUid(knowledgeId, columnType, userId);
        //DataSync<IdTypeUid> dataSync = this.createDataSync(0, idTypeUid);
        dataSyncTask.saveDataNeedSync(idTypeUid);

        /*
        //delete tags
        tagServiceLocal.deleteTags(userId, knowledgeId);

        //delete directory
        directoryServiceLocal.deleteDirectory(userId, knowledgeId);

        //delete Assso info
        associateServiceLocal.deleteAssociate(knowledgeId, userId);

        //delete permission info
        if (permissionServiceLocal.deletePermissionInfo(userId, knowledgeId)) {
            logger.info("delete knowledge permission success. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //send new knowledge to bigdata
        bigDataSyncTask.deleteMessage(knowledgeId, columnType, userId);
        */

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
            //asynchronize delete other knowledge resource
            IdTypeUid idTypeUid = new IdTypeUid(knowledgeId, 1, userId);
            dataSyncTask.saveDataNeedSync(idTypeUid);

            /*
            //delete Assso info
            associateServiceLocal.deleteAssociate(knowledgeId, user.getId());
            //delete tags
            tagServiceLocal.deleteTags(userId, knowledgeId);

            //delete directory
            directoryServiceLocal.deleteDirectory(userId, knowledgeId);

            //delete permission information
            permissionServiceLocal.deletePermissionInfo(userId, knowledgeId);*/
        }
        String resp = "successId: " + permDeleteIds.toString() + " failedId: " + failedIds.toString();
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
                final int type = keyValue.getKey();
                List<Long> deleIds = keyValue.getValue();
                result = this.knowledgeService.batchDeleteByKnowledgeIds(deleIds, (short)type);
                if (result == null || result.getNotification() == null || !"0".equals(result.getNotification().getNotifCode())) {
                    logger.error("delete knowledge failed. knowledgeId: " + deleIds + " type: " + type);
                }
                //asynchronize delete other knowledge resource
                for (long knowledgeId : deleIds) {
                    IdTypeUid idTypeUid = new IdTypeUid(knowledgeId, type, userId);
                    //DataSync<IdTypeUid> dataSync = this.createDataSync(0, idTypeUid);
                    dataSyncTask.saveDataNeedSync(idTypeUid);
                }

            } catch (Exception e) {
                logger.error("knowledge delete failed！reason："+e.getMessage());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
            }
        }

        //This need to change to batch delete
        /*
        for (long knowledgeId : permDeleteIds) {
            //delete Assso info
            associateServiceLocal.deleteAssociate(knowledgeId, user.getId());
            //delete tags
            tagServiceLocal.deleteTags(userId, knowledgeId);

            //delete directory
            directoryServiceLocal.deleteDirectory(userId, knowledgeId);

            //delete permission information
            permissionServiceLocal.deletePermissionInfo(userId, knowledgeId);
        }*/

        //Check if this is collected knowledge
        for (Long knowledgeId : failedIds) {
            cancelKnowledge(userId, knowledgeId);
        }

        String resp = "successId: " + permDeleteIds.toString() + ", failedId: " + failedIds.toString();
        logger.info("delete knowledge success: " + resp);

        result.setResponseData(resp);
        return result;
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     *//*
    @RequestMapping(value = "/{knowledgeId}/{type}/{organId}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailByOrganId(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable int type, @PathVariable long organId) throws Exception {
        if (isWeb(request)) {
            logger.info("Query knowledge from web....");
            return detailWeb(request, response, knowledgeId, type);
        }

        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }
        DataCollect data = result.getResponseData();
        logger.info("Query knowledge detail success. knowledgeId: " + knowledgeId + " type: " + type);
        jacksonValue = knowledgeDetail(data);

        return jacksonValue;
    }*/

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

        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request, 0l);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }

        DataCollect data = result.getResponseData();
        logger.info("Query knowledge detail succcess. knowledgeId: " + knowledgeId + " type: " + type);
        jacksonValue = knowledgeDetail(data);

        return jacksonValue;
    }

    /**
     * 组织 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/{knowledgeId}/{type}/{organId}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailByOrganId(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable int type,
                                      @PathVariable long organId) throws Exception {
        if (isWeb(request)) {
            logger.info("organ Query knowledge from web....");
            return detailWeb(request, response, knowledgeId, type);
        }

        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request, organId);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }

        DataCollect data = result.getResponseData();
        logger.info("Query knowledge detail succcess. knowledgeId: " + knowledgeId + " type: " + type);
        jacksonValue = knowledgeDetail(data);

        return jacksonValue;
    }

    /**
     * 分享出去的知识查看详情
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/shareAsso/{parame}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailByAsso(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable String parame) throws Exception {
        User user = this.getJTNUser(request);
        Base64 base64 = new Base64();
        byte[] b = base64.decode(parame);
        String parames = new String(b);
        String[] kts = parames.split(",");
        Long knowledgeId = Long.parseLong(kts[0]);
        int type = Integer.parseInt(kts[1]);
        Long shareId = Long.parseLong(kts[2]);
        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request, 0l);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }

        DataCollect data = result.getResponseData();
        Knowledge detail = data.getKnowledgeDetail();
        List<Long> tags = detail.getTagList();
        List<Long> directoryIds = detail.getDirectorys();
        List<IdName> minTags = this.getMinTagList(tags);
        long userId = user.getId() == 0 ? detail.getCid() : user.getId();
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

        // 开始获取需要展示的数据
        if (!shareId.equals(0)) {
            String content = associateServiceLocal.getAssoiateShareContent(shareId);
            if (content != null ) {
                if (content.equals("")) {
                    data.setAsso(new ArrayList<Associate>());
                } else {
                    String[] shareIds = content.split(",");
                    if (shareIds.length > 0) {
                        List<Associate> associateList = data.getAsso();
                        List<Associate> newAssociateList = new ArrayList<Associate>(shareIds.length);
                        for (int i = 0; i < shareIds.length; i++) {
                            Long shareAssoId = Long.parseLong(shareIds[i]);
                            for (Associate associate : associateList) {
                                if (shareAssoId.equals(associate.getAssocId())) {
                                    newAssociateList.add(associate);
                                }
                            }
                        }
                        logger.info("newAssociateList size :  " + newAssociateList.size());
                        data.setAsso(newAssociateList);
                    }
                }
            }
        }

        logger.info("Query knowledge detail succcess. knowledgeId: " + knowledgeId + " type: " + type);
        return knowledgeDetail(data);
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/web/{knowledgeId}/{type}", method = RequestMethod.GET)
    public MappingJacksonValue detailWeb(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable long knowledgeId,@PathVariable int type) throws Exception {
        User user = this.getJTNUser(request);

        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request, 0l);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }

        DataCollect data = result.getResponseData();
        Knowledge detail = data.getKnowledgeDetail();
        List<Long> tags = detail.getTagList();
        List<Long> directoryIds = detail.getDirectorys();
        List<IdName> minTags = this.getMinTagList(tags);
        long userId = user.getId() == 0 ? detail.getCid() : user.getId();
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
        return knowledgeDetail(data);
    }

    /**
     * 提取知识详细信息，一般用在详细查看界面、编辑界面
     * @param knowledgeId 知识Id
     * @param type 栏目主键
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/detailFilter/{knowledgeId}/{type}", method = RequestMethod.GET)
    public MappingJacksonValue detailFilter(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable int type) throws Exception {
        User user = this.getJTNUser(request);
        InterfaceResult<DataCollect> result = knowledgeDetail(user, knowledgeId, type, request, 0l);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        if(FailedGetKnowledge(result, jacksonValue, knowledgeId, type)) {
            return jacksonValue;
        }
        /*
        if (Failed(result)) {
            logger.error("Query knowledge detail failed. knowledgeId: " + knowledgeId + " type: " + type);
            return mappingJacksonValue(result);
        }

        DataCollect data = result.getResponseData();
        if (data == null || data.getKnowledgeDetail() == null) {
            logger.error("Query knowledge detail failed: knowledgeId: " + knowledgeId + " type: " + type);
            return mappingJacksonValue(CommonResultCode.PARAMS_EXCEPTION);
        }*/

        DataCollect data = result.getResponseData();
        logger.info("Query knowledge detail succcess. knowledgeId: " + knowledgeId + " type: " + type);

        try {
            Knowledge detail = data.getKnowledgeDetail();
            final String content = HtmlToText.removeUnderLine(detail.getContent());
            detail.setContent(content);
            knowledgeService.updateKnowledgeDetail(detail);
        } catch (Exception ex) {
            logger.error("failter knowledge detail and update to database failed. error: " + ex.getMessage());
        }

        jacksonValue = knowledgeDetail(data);
        return jacksonValue;
    }

    /**
     * 提取所有知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/all/{start}/{size}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAll(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable int start,@PathVariable int size,@PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        logger.info("---keyWord: " + keyWord);
        keyWord = resetKeyWord(keyWord);

        long userId = user.getId();
        Map<String, List<KnowledgeBase>> resultMap = new HashMap<String, List<KnowledgeBase>>();
        List<KnowledgeBase> createdKnowledgeItems = this.getCreatedKnowledge(userId, start, size, keyWord);
        if (CollectionUtils.isNotEmpty(createdKnowledgeItems)) {
            createdKnowledgeItems = setReadCount(createdKnowledgeItems);
            resultMap.put("created", createdKnowledgeItems);
        }

        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledgeByIndex(userId, start, size, keyWord);
        collectedKnowledgeItems = setReadCount(collectedKnowledgeItems);
        resultMap.put("collected", collectedKnowledgeItems);

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
    @RequestMapping(value = "/allByPage/{page}/{size}/{total}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAllByPage(HttpServletRequest request, HttpServletResponse response,
                                        @PathVariable int page,@PathVariable int size,
                                        @PathVariable long total,@PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = getUserId(user);
        if (user.isVirtual()) {
            userId = user.getUid();
            logger.info("org user, will get the user knowledge. id: " + user.getId() + " uid: " + user.getUid());
        }

        keyWord = resetKeyWord(keyWord);
        //First request need to get from server
        if (total <= 0) {
            total = getKnowledgeCount(userId, keyWord);
        }

        /*
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
            List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledgeByIndex(userId, 0, restSize, keyword);
            int collectedSize = collectedKnowledgeList != null ? collectedKnowledgeList.size() : 0;
            logger.info("get created knowledge size: " + createdKnowledgeList.size() + " collected size: " + collectedSize);
            if (collectedSize > 0) {
                createdKnowledgeList.addAll(collectedKnowledgeList);
            }
            return knowledgeListPage(total, page, createdKnowledgeList.size(), createdKnowledgeList);
        }

        final int collecedIndex = (gotTotal - createCount);
        List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledgeByIndex(userId, collecedIndex, size, keyword);
        if (collectedKnowledgeList != null && collectedKnowledgeList.size() > 0) {
            logger.info("get collected size: " + collectedKnowledgeList.size());
            return knowledgeListPage(total, page, collectedKnowledgeList.size(), collectedKnowledgeList);
        }*/

        List<KnowledgeBase> baseList =  this.knowledgeService.getAllCreateAndCollected(userId, total, keyWord, page, size);
        if (CollectionUtils.isNotEmpty(baseList)) {
            return knowledgeListPage(total, page, baseList.size(), baseList);
        }

        logger.info(".......get all knowledge complete......");
        return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
    }

    @ResponseBody
    @RequestMapping(value = "/getKnowledgeByTypeAndKeyword/{type}{page}/{size}/{total}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeByTypeAndKeyword(HttpServletRequest request, HttpServletResponse response,
    													@PathVariable short type, @PathVariable int page,@PathVariable int size,
    													@PathVariable long total,@PathVariable String keyWord) throws Exception {
        keyWord = resetKeyWord(keyWord);
        if (type == KNOWLEDGE_CREATE) {
			return this.getAllCreatedByPage(request, response, page, size, total, keyWord);
		} else if (type == KNOWLEDGE_COLLECT) {
			return this.getAllCollectedByPage(request, response, page, size, total, keyWord);
		} else if (type == KNOWLEDGE_SHARE) {
			return null;
		} else if (type == KNOWLEDGE_ALL) {
			return this.getAllByPage(request, response, page, size, total, keyWord);
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
    @RequestMapping(value = "/allCreated/{start}/{size}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAllCreated(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable int start,@PathVariable int size,@PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        keyWord = resetKeyWord(keyWord);
        //long userId = user.getId();
        List<KnowledgeBase> createdKnowledgeItems = this.getCreatedKnowledge(user.getId(), start, size, keyWord);

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
    @RequestMapping(value = "/allCreatedByPage/{page}/{size}/{total}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAllCreatedByPage(HttpServletRequest request, HttpServletResponse response,
                                               @PathVariable int page,@PathVariable int size,
                                               @PathVariable long total,@PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        keyWord = resetKeyWord(keyWord);
        long userId = this.getUserId(user);
        if (total == -1) {
            //TODO: need to check if long to int
            total = getCreatedKnowledgeCount(userId, keyWord);
        }

        int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> createdKnowledgeList = this.getCreatedKnowledge(userId, start, size, keyWord);
        InterfaceResult<Page<KnowledgeBase>> result = this.knowledgeListPage(total, page, size, createdKnowledgeList);
        logger.info(".......get all created knowledge success. size: " + (createdKnowledgeList != null ? createdKnowledgeList.size() : 0));
        return result;
    }

    /**
     * 组织 提取知识创建数据 (不包括已经在当前组织贡献的知识)
     * 暂未实现
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/organ/myByPage", method = RequestMethod.POST)
    public InterfaceResult getMyByPage(HttpServletRequest request) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        OrganKnowQuery organKnowQuery = (OrganKnowQuery)JsonUtils.jsonToBean(requestJson, OrganKnowQuery.class);
        if (organKnowQuery == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);

        long userId = this.getUserId(user);
        Long organId = organKnowQuery.getOrganId();
        String keyword = organKnowQuery.getKeyword();
        int page = organKnowQuery.getPage();
        int size = organKnowQuery.getSize();
            //TODO: need to check if long to int
        int total = getCreatedKnowledgeCount(userId, keyword);

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
    @RequestMapping(value = "/allCollected/{start}/{size}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAllCollected(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable int start,@PathVariable int size,
                                           @PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        keyWord = resetKeyWord(keyWord);
        long userId = this.getUserId(user);
        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledgeByIndex(userId, start, size, keyWord);

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
    @RequestMapping(value = "/allCollectedByPage/{page}/{size}/{total}/{keyWord}", method = RequestMethod.GET)
    public InterfaceResult getAllCollectedByPage(HttpServletRequest request, HttpServletResponse response,
                                                 @PathVariable int page,@PathVariable int size,
                                                 @PathVariable long total,@PathVariable String keyWord) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        keyWord = resetKeyWord(keyWord);
        long userId = this.getUserId(user);
        if (total == -1) {
            total = getCollectedKnowledgeCount(userId, keyWord);
        }

        final int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> collectedKnowledgeList = this.getCollectedKnowledgeByPage(userId, total, page, size, keyWord);

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
            knowledgeBasesItems = this.knowledgeService.getByUserIdAndColumnId(user.getId(), columnId, start, size);
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

        keyWord = resetKeyWord(keyWord);
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            long usrId = user.getId();
            List<KnowledgeBase> knowledgeBaseItems = null;
            if (keyWord == null || keyWord.length() > 0) {
                knowledgeBaseItems = this.knowledgeService.getByUserId(usrId, start, size);
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

        keyWord = resetKeyWord(keyWord);
        List<KnowledgeBase> knowledgeBasesItems = null;
        try {
            if (keyWord == null || keyWord.length() <= 0) {
                knowledgeBasesItems = this.knowledgeService.getByUserIdAndColumnId(user.getId(), columnId, start, size);
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
                total = knowledgeIndexService.getKnowledgeCountByUserIdAndColumnId(idList, (long)KnowledgeConstant.SOURCE_GINTONG_BRAIN_ID, type);
                logger.info("end to get knowledge count:" + total);
            }
            if (total > 0 && start < total) {
                logger.info("start to get knowledge:" + total);
                List<Knowledge> detailList = knowledgeIndexService.getKnowledgeDetailList(idList, userId, type, start, size);
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
                    knowledgeList = this.knowledgeIndexService.getAllPublicByPage(type, columnId, column.getPathName(), userId, start, size);
                } catch (Exception ex) {
                    logger.error("invoke selectPlatform failed. type: {}, columnId: {}, columnPath: {},  userId: {} error: {}"
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
                    total = this.knowledgeService.getAllPublicCount(KnowledgeConstant.PRIVATED);
                } catch (Exception ex) {
                    logger.error("invoke getBaseAllPublicCount failed: error: " + ex.getMessage());
                }
            }
            if (total > 0 && start < total) {
                try {
                    knowledgeList = this.knowledgeService.getAllPublic(start, size, KnowledgeConstant.PRIVATED);
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

        keyWord = resetKeyWord(keyWord);
        List<KnowledgeBase> knowledgeBasesItems = null;
        try {
            if (keyWord == null || keyWord.length() <= 0) {
                knowledgeBasesItems = this.knowledgeService.getByUserIdAndColumnId(user.getId(), columnId, start, size);
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
    public InterfaceResult<List<KnowledgeBase>> allNoDirectory(HttpServletRequest request,HttpServletResponse response,
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
            logger.error("Query knowledge failed！reason：" + e.getMessage());
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
            dataList = KnowledgeUtil.getDataCollectReturn(this.knowledgeService.getByUserId(user.getId(), start, size));
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
            KnowledgeBaseList = this.knowledgeService.getByUserIdAndColumnId(user.getId(), columnId, start, size);
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

        return collectKnowledge(request, response, knowledgeId, typeId, 0);
    }

    /**
     * 收藏知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/collect/{knowledgeId}/{typeId}/{shareId}", method = RequestMethod.POST)
    public InterfaceResult collectKnowledge(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable long knowledgeId, @PathVariable int typeId, @PathVariable long shareId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

        if (knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance("");
        try {
            typeId = typeId <= 0 ? 1 : typeId;
            Permission perm = permissionServiceLocal.getPermissionInfo(knowledgeId);
            final short privated = DataCollect.privated(perm, false);
            result = this.knowledgeOtherService.collectKnowledge(userId, knowledgeId, typeId, shareId, privated);
        } catch (Exception ex) {
            logger.error("collect knowledge failed！：" + ex.getMessage());
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION,"收藏知识失败!");
            logger.error("collect knowledge failed！knowledgeId: " + knowledgeId + " type: " + typeId + " error: " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION,"收藏知识失败!");
        }

        //collect count
        knowledgeCountService.updateCollectCount(knowledgeId, (short)typeId);
        logger.info("collect knowledge success. knowledgeId: " + knowledgeId + " type: " + typeId);

        //Businsess log
        //BusinessTrackUtils.addTbBusinessTrackLog4CollectOpt(logger(), TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(), knowledgeId, null, request, userId, user.getName());
        BusinessTrackLog busLog = new BusinessTrackLog(logger, TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(),
                0, OptTypeEnum.OPT_COLLECT.getKey(), knowledgeId, userId, user.getName(), request);
        dataSyncTask.putQueue(busLog);
        return result;
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
        if (knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            typeId = typeId <= 0 ? 1 : typeId;
            InterfaceResult result = this.knowledgeOtherService.deleteCollectedKnowledge(userId, knowledgeId, typeId);
            logger.info("cancel collect knowledge success. knowledgeId: " + knowledgeId + " type: " + typeId);
            return result;
        } catch (Exception e) {
            logger.error("cancel collect knowledge failed. knowledgeId: " + knowledgeId + " type: " + typeId + " error:" + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }
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

        keyword = resetKeyWord(keyword);
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

    /**
     * 根据知识ID获取知识的关联信息
     * @param request
     * @param knowledgeId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = {"/knowledgeAsso/{knowledgeId}"}, method = RequestMethod.GET)
    public InterfaceResult getKnowledgeAsso(HttpServletRequest request, @PathVariable long knowledgeId) {
        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);
        try {
            List<Associate> associateList = associateServiceLocal.getAssociateList(userId, knowledgeId);
            return InterfaceResult.getSuccessInterfaceResultInstance(associateList);
        } catch (Exception ex) {
            logger.error("get associate info failed: knowledgeId: " + knowledgeId + ", userId: " + userId + " error: " + ex.getMessage());
            //ex.printStackTrace();
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }
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

        List<Knowledge> knowledgeList = knowledgeIndexService.selectPlatform(type, columnId, null, userId, page, size);
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
            if (k.getUid() == KnowledgeConstant.UserType.jinTN.v()) {
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
        KnowledgeCount kCount = knowledgeCountService.getKnowledgeCountByIdType(kId, type);
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

    private List<KnowledgeBase> getCreatedKnowledge(long userId, int start, int size, String keyWord)
    {
        List<KnowledgeBase> createdKnowledgeItems = null;
        try {
            createdKnowledgeItems = this.knowledgeService.getCreatedKnowledge(userId, start, size, keyWord);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }
        return createdKnowledgeItems;
    }

    private List<KnowledgeBase> getOrganCreatedKnowledge(long userId, int start, int size, String keyWord, List<Long> list)
    {
        List<KnowledgeBase> createdKnowledgeItems = null;
        try {
            createdKnowledgeItems = this.knowledgeService.getCreatedKnowledge(userId, start, size, keyWord);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
        }
        return createdKnowledgeItems;
    }

    private List<KnowledgeBase> getCollectedKnowledgeByPage(long userId, long total, int page, int size, String keyword) throws Exception {
        List<KnowledgeCollect> collectItems = null;
        List<KnowledgeBase> collectedKnowledgeItems = null;
        try {
            collectItems = knowledgeOtherService.myCollectedKnowledgeByPage(userId, total, (short)-1, page, size, keyword);
        } catch (Exception ex) {
            logger.error("invoke myCollectKnowledge failed. userId: " + userId + " error: " + ex.getMessage());
        }
        final int collectedSize  = collectItems != null ? collectItems.size() : 0;
        logger.info("get collected knowledge size : " + collectedSize + " , keyword: " + keyword);
        collectedKnowledgeItems = convertCollectedKnowledge(collectItems);
        //collectedKnowledgeItems = this.knowledgeService.getMyCollected(knowledgeIds,keyword);

        return collectedKnowledgeItems;
    }

    private List<KnowledgeBase> getCollectedKnowledgeByIndex(long userId, int index, int size, String keyword) throws Exception {
        List<KnowledgeBase> collectedBaseItems = null;
        try {
            collectedBaseItems = knowledgeService.getCollectedKnowledgeByIndex(userId, index, size, keyword);
        } catch (Exception ex) {
            logger.error("invoke myCollectKnowledge failed. userId: " + userId + " error: " + ex.getMessage());
        }
        final int collectedSize = collectedBaseItems != null ? collectedBaseItems.size() : 0;
        logger.info("get collected knowledge size : " + collectedSize + " , keyword: " + keyword);
        return collectedBaseItems;
    }

    private InterfaceResult<DataCollect> knowledgeDetail(User user,long knowledgeId, int columnType, HttpServletRequest request, long organId)
    {
        long userId = user.getId();
        logger.info("Query knowledge detail. knowledgeId: " + knowledgeId + " userId: " + userId);

        if(knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION, "知识Id无效");
        }

        columnType = columnType <= 0 ? 1 : columnType;
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
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION,"知识已删除!");
        }
        if (detail.getStatus() != 4) {
            logger.error("this knowledge is disabled: knowledgeId: " + knowledgeId + ", columnId: " + columnType + " status: " + detail.getStatus());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION, "该知识已经被禁用!");
        }
        /*
        if (detail.getCid() > 1) {
            if (detail.getPrivated() == 1 && userId != detail.getCid()) {
                logger.error("this knowledge is private: knowledgeId: " + knowledgeId + ", columnId: " + columnType);
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION, "该资源权限为私密，您没有权限查看！");
            }
        }*/

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

        Permission permission = getPermission(detail.getCid(), knowledgeId, detail.getPrivated());
        data.setPermission(permission);

        try {
            List<Associate> associateList = associateServiceLocal.getAssociateList(userId, knowledgeId);
            data.setAsso(associateList);
        } catch (Exception ex) {
            logger.error("get associate info failed: knowledgeId: " + knowledgeId + ", userId: " + userId + " error: " + ex.getMessage());
            //ex.printStackTrace();
        }

        // 若 organId 不是 0 返回组织资源数据
        if (organId != 0) {
            resetKnowData(knowledgeId, OrganSourceTypeEnum.KNOWLEDGE.value(), organId, data);
        }

        result.setResponseData(data);
        logger.info(".......get knowledge detail complete......");

        //Click count this should be in queue
        try {
            String userName = detail.getCname();
            if (userName != null && userName.length() > 100) {
                userName = userName.substring(0,100);
            }
            KnowledgeCount count = knowledgeCountService.updateClickCount(detail.getCid(), userName, detail.getTitle(), knowledgeId, (short)columnType);
            final long readCount = count != null ? count.getClickCount() : 0L;
            data.setReadCount(readCount);
        } catch (Exception ex) {
            logger.error("count knowledge read failed: knowledgeId: " + knowledgeId + ", columnId: " + columnType);
            ex.printStackTrace();
        }

        //Business log
        //BusinessTrackUtils.addTbBusinessTrackLog4ViewOpt(logger(), TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(), detail.getId(), null, request, userId, user.getName());
        BusinessTrackLog busLog = new BusinessTrackLog(logger, TRACK_LOGGER, BusinessModelEnum.BUSINESS_KNOWLEDGE.getKey(), 0, OptTypeEnum.OPT_VIEW.getKey(), detail.getId(), userId, user.getName(), request);
        dataSyncTask.putQueue(busLog);
        return result;
    }

    private void resetKnowData(long sourceId, byte type, long organId, DataCollect data) {

        OrganResource organResource = null;
        try {
            organResource = organResourceService.getOrganResourceBySIdTypeOrganId(sourceId, type, organId);
        } catch (Exception e) {
            logger.error("invoke organResourceService failure. method : [getOrganResourceBySIdTypeOrganId]");
            e.printStackTrace();
        }
        data.setOrganResource(organResource);
    }

    private long getKnowledgeCount(long userId)
    {
        try {
            return this.knowledgeService.countAllCreateAndCollected(userId, null);
        }  catch (Exception ex) {
            logger.error("get created knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }
        return 0;
    }

    private long getKnowledgeCount(long userId, String keyWord)
    {
        try {
            return this.knowledgeService.countAllCreateAndCollected(userId, keyWord);
        }  catch (Exception ex) {
            logger.error("get created knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }
        return 0;
    }


    private int getCreatedKnowledgeCount(long userId)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.countByUserId(userId);
        } catch (Exception ex) {
            logger.error("get created knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }

        logger.info("createCount: " + createCount);
        return createCount;
    }

    private int getCreatedKnowledgeCount(long userId, String keyWord)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.countCreated(userId, keyWord);
        } catch (Exception ex) {
            logger.error("get created knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }

        logger.info("createCount: " + createCount);
        return createCount;
    }


    private long getCollectedKnowledgeCount(long userId, String keyWord)
    {
        long collectedCount = 0;
        try {
            collectedCount = this.knowledgeService.countCollected(userId, keyWord);
        }catch (Exception ex) {
            logger.error("get collected knowledge count failed. userId: " + userId + ", error: " + ex.getMessage());
        }
        logger.info("collectedCount: " + collectedCount);
        return collectedCount;
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

    private List<IdName> getMinTagList(List<Long> tagIds)
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

    private ColumnSelf getColumn(long columnId)
    {
        if (columnId > 0) {
            try {
                logger.info("Query column by Id. columnId: " + columnId);
                return columnSelfService.selectByPrimaryKey(columnId);
            } catch (Exception ex) {
                logger.error("Get column failed: error " + ex.getMessage());
            }
        }
        logger.error("ColumnId is invalidated. columnId: "+columnId);
        return null;
    }

    private List<KnowledgeBase> convertCollectedKnowledge(List<KnowledgeCollect> collectItems)
    {
        return KnowledgeUtil.convertCollectedKnowledge(collectItems);
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

    public Logger logger() { return this.logger; }

    private void batchContribute(ContributeVO contributeVO, User user, HttpServletRequest request) {

        List<ContributeData> dataList = contributeVO.getDataList();
        Long organId = contributeVO.getOrganId();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (ContributeData data : dataList) {
                Long id = data.getId();
                short columnType = data.getColumnType();
                Knowledge detail = null;
                try {
                    detail = knowledgeService.getDetailById(id, columnType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (detail == null) {
                    logger().error("知识 id 和 columnType 传入有误。 id : " + id + " columnType : " + columnType);
                    continue;
                }
                OrganResource organResource = null;
                try {
                    organResource = organResourceService.getContributedOrganResource(organId, OrganSourceTypeEnum.KNOWLEDGE.value(), id, contributeType, user.getId());
                } catch (Exception e) {
                    logger().error("invoke organResourceService failure. method : " +
                            "[getContributedOrganResource]. organId : " + organId + " userId : " + user.getId());
                }
                // 已贡献过该资源
                if (organResource != null){
                    logger().info("this know has been contributed. id : " + id + " type : " + columnType);
                    continue;
                }
                DataCollect dataCollect = new DataCollect();
                setDataCollect(detail, dataCollect, organId);
                //  创建知识
                InterfaceResult result = this.createKnowledge(dataCollect, user, request);
                if (!"0".equals(result.getNotification().getNotifCode())) {
                    logger().error("contribute know failure. know id : " + id + " columnType : " + columnType);
                }
            }
        }
    }
    private void setDataCollect(Knowledge detail, DataCollect dataCollect, long organId) {

        Knowledge knowledgeDetail = new Knowledge();
        knowledgeDetail.setTitle(detail.getTitle());
        knowledgeDetail.setContent(detail.getContent());
        knowledgeDetail.setCpathid(detail.getCpathid());
        knowledgeDetail.setColumnid(detail.getColumnid());
        knowledgeDetail.setColumnType(detail.getColumnType());
        knowledgeDetail.setMultiUrls(detail.getMultiUrls());
        knowledgeDetail.setPic(detail.getPic());
        dataCollect.setKnowledgeDetail(knowledgeDetail);
        // 设置创建 组织资源参数
        OrganResourceVO organResourceVO = new OrganResourceVO();
        organResourceVO.setOrganId(organId);
        organResourceVO.setPrivated((byte) 0);
        organResourceVO.setPermissionType(OrganResourcePermissionTypeEnum.NO_PERMISSION.value());
        // 创建资源类型 1：创建 2：贡献
        organResourceVO.setCreateType((byte) 2);
        // 设置原始 资源 id
        organResourceVO.setOriginalSourceId(detail.getId());
        dataCollect.setOrganResourceVO(organResourceVO);
    }
}