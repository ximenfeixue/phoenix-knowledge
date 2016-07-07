package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.associate.exception.AssociateServiceException;
import com.ginkgocap.parasol.associate.exception.AssociateTypeServiceException;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.associate.model.AssociateType;
import com.ginkgocap.parasol.associate.service.AssociateService;
import com.ginkgocap.parasol.associate.service.AssociateTypeService;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.mobile.Connections;
import com.ginkgocap.ywxt.knowledge.model.mobile.JTContactMini;
import com.ginkgocap.ywxt.knowledge.model.mobile.KnowledgeMini2;
import com.ginkgocap.ywxt.knowledge.model.mobile.OrganizationMini;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.knowledge.utils.ContantDefine;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.UserService;
import com.ginkgocap.ywxt.util.HttpClientHelper;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.service.PermissionCheckService;
import com.gintong.common.phoenix.permission.service.PermissionRepositoryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.frame.util.dto.Notification;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    KnowledgeOtherService knowledgeOtherService;

    //@Autowired
    //KnowledgeCountService knowledgeCountService;

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

    @Value("#{configuers.knowledgeBigDataSearchUrl}")
    private String knowledgeBigDataSearchUrl;

    private ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
	/**
	 * 插入数据
	 * @throws IOException
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
		DataCollection dataCollection = KnowledgeUtil.getDataCollection(requestJson);

		InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		try {
            dataCollection.serUserInfo(user);
            result = this.knowledgeService.insert(dataCollection);
		} catch (Exception e) {
			logger.error("Insert knowledge failed : " + e.getMessage());
			return result;
		}

        if (result == null || result.getNotification()== null || result.getResponseData() == null) {
            logger.error("Insert knowledge failed!");
            return result;
        }
        long knowledgeId = Long.valueOf(result.getResponseData().toString());

        KnowledgeDetail knowledgeDetail = dataCollection.getKnowledgeDetail();
        knowledgeDetail.setId(knowledgeId);

        if (!tagServiceLocal.saveTagSource(userId, knowledgeDetail)) {
            logger.error("Save Tag info failed, userId: {} knowledgeId: ", userId, knowledgeId);
        }

        List<Long> successIds = directoryServiceLocal.saveDirectorySource(userId, knowledgeDetail);
        if (successIds != null && successIds.size() >0) {
            logger.error("Save Directory success. userId: {} knowledgeId: {}, plan size: {}, success size: {}",
                    userId, knowledgeId, knowledgeDetail.getCategoryIds().size(), successIds.size());
        }
        else {
            logger.error("Save Directory info have error, userId: {} knowledgeId: {}", userId, knowledgeId);
        }
        //save asso information

        //TODO: If this step failed, how to do ?
        try {
            List<Associate> as  = dataCollection.getAsso();
            if (as != null) {
                createAssociate(as, knowledgeId, userId);
            } else {
                logger.error("associate it null or converted failed, so skip to save!");
            }

        }catch (Exception e) {
            logger.error("Insert associate failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        try {
            Permission permission = permissionInfo(dataCollection.getPermission(), knowledgeId, userId);
            if (permission != null) {
                permissionRepositoryService.insert(permission);
            }
        } catch (Exception e) {
            logger.error("Insert knowledge permission failed : " + e.getMessage());
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        logger.info(".......create knowledge success......");
		return result;
	}

	/**
	 * 更新数据
	 * @throws IOException
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
		DataCollection data = KnowledgeUtil.getDataCollection(requestJson);
        if (data == null) {
            logger.error("request data is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
        if (knowledgeDetail == null) {
            logger.error("request knowledgeDetail is null or incorrect");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        long knowledgeId = knowledgeDetail.getId();
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
        tagServiceLocal.updateTagSource(userId, knowledgeDetail);

        //Update Directory info
        directoryServiceLocal.updateDirectorySource(userId, knowledgeDetail);

        //Update Assso info
        try {
            Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, (long)KnowledgeBaseService.sourceType, knowledgeId);
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
	 * @throws IOException
	 */
    @ResponseBody
	@RequestMapping(value="/{knowledgeId}/{columnId}", method = RequestMethod.DELETE)
	public InterfaceResult delete(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long knowledgeId,@PathVariable short columnId) throws Exception {
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
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
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
     * @throws IOException
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
	 * @throws IOException
	 */
	@RequestMapping(value = "/{knowledgeId}/{columnId}", method = RequestMethod.GET)
	@ResponseBody
	public MappingJacksonValue detail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long knowledgeId,@PathVariable short columnId) throws Exception {

        User user = this.getUser(request);
        if (user == null) {
            return mappingJacksonValue(CommonResultCode.PERMISSION_EXCEPTION);
        }


		if(knowledgeId <= 0) {
            return mappingJacksonValue(CommonResultCode.PARAMS_NULL_EXCEPTION,"知识Id无效");
		}

        DataCollection data = new DataCollection();
		KnowledgeDetail knowledgeDetail = null;
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		try {
            knowledgeDetail = this.knowledgeService.getDetailById(knowledgeId, columnId);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason：" + e.getMessage());
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}

		//数据为空则直接返回异常给前端
		if(knowledgeDetail == null) {
            logger.error("get knowledge failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);//.DATA_DELETE_EXCEPTION);
		}
        String hContent = HtmlToText.htmlToText(knowledgeDetail.getContent());
        int maxLen = hContent.length() >= 250 ? 250 : hContent.length();
        hContent = hContent.substring(0, maxLen);
        knowledgeDetail.setHcontent(hContent);
        knowledgeDetail.setVirtual(user.isVirtual() ? (short)2 : (short)1);
        data.setKnowledgeDetail(knowledgeDetail);

        boolean isCollected = false;
        long userId = user.getId();
        try {
            isCollected = knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, columnId);
        } catch (Exception ex) {
            logger.error("Query knowledge is collected or not failed: userId: {}, knowledgeId: {}, columnId: {}", userId, knowledgeId, columnId);
        }
        knowledgeDetail.setCollected(isCollected ? (short)1 : (short)0);

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

        logger.info(".......get knowledge detail success......");
        result.setResponseData(data);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setFilters(KnowledgeUtil.assoFilterProvider(Associate.class.getName()));

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
     * @throws IOException
     *
    @RequestMapping(value = "/web/{knowledgeId}/{columnId}", method = RequestMethod.GET)
    @ResponseBody
    public MappingJacksonValue detailWeb(HttpServletRequest request, HttpServletResponse response,
                                      @PathVariable long knowledgeId,@PathVariable short columnId) throws Exception {
        User user = this.getUser(request);
        InterfaceResult<DataCollection> result = knowledgeDetail(user, knowledgeId, columnId);
        if (result != null) {
            DataCollection data = result.getResponseData();
            MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
            if (data == null || data.getKnowledgeDetail() == null) {
                logger.error("get knowledge detail failed: knowledgeId: {}", knowledgeId);
                return jacksonValue;
            }
            KnowledgeDetail detail = data.getKnowledgeDetail();
            List<Long> tags = detail.getTags();
            List<Long> directorys = detail.getCategoryIds();
            //WList<Tag> = tagServiceLocal.getTagList(user.getId(), tags);
            directoryServiceLocal.getDirectoryList(user.getId(), directorys);

        }
    }*/
	
	/**
	 * 提取所有知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
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
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
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
    }	/**
     * 提取所有知识数据
     * @param start 分页起始
     * @param size 分页大小
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allCollected/{start}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getAllCollected(HttpServletRequest request, HttpServletResponse response,
                                  @PathVariable int start,@PathVariable int size,@PathVariable String keyword) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = user.getId();
        List<KnowledgeBase> collectedKnowledgeItems = this.getCollectedKnowledge(userId, start, size, keyword);

        logger.info(".......get all collected knowledge success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(collectedKnowledgeItems);
    }


	/**
	 * 根据栏目提取知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
    @ResponseBody
	@RequestMapping(value = "/allByColumn/{columnId}/{start}/{size}", method = RequestMethod.GET)
	public InterfaceResult getAllByColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable short columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
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
                                                                            @PathVariable String keyWord,@PathVariable short columnId,
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
    @RequestMapping(value = "/allByColumnAndKeyword/{columnId}/{keyWord}/{start}/{size}", method = RequestMethod.GET)
    public InterfaceResult<List<KnowledgeBase>> allByColumnIdAndKeyWord(HttpServletRequest request, HttpServletResponse response,
                                                                           @PathVariable short columnId,@PathVariable String keyWord,
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
	 * @throws IOException
	 */
    @ResponseBody
	@RequestMapping(value = "/user/{start}/{size}", method = RequestMethod.GET)
	public InterfaceResult<List<DataCollection>> getByCreateUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		if(start < 0 || size <= 0 ) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }
		
		List<DataCollection> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
		try {
            dataCollectionList = KnowledgeUtil.getReturn(this.knowledgeService.getBaseByCreateUserId(user.getId(), start, size));
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        logger.info(".......get all knowledge by create userId success......");
		return InterfaceResult.getSuccessInterfaceResultInstance(dataCollectionList);
	}
	
	/**
	 * 根据栏目提取当前用户的知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
    @ResponseBody
	@RequestMapping(value = "/user/{columnId}/{start}/{size}", method = RequestMethod.GET)
	public InterfaceResult<List<KnowledgeBase>> getByCreateUserIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable short columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
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
	 * 首页获取大数据知识热门推荐和发现热门知识
	 * @param type 1,推荐 2,发现
	 * @param start
	 * @param size
	 * @throws Exception
	 */
    @ResponseBody
	@RequestMapping(value = "/recommended/{type}/{start}/{size}", method = RequestMethod.GET)
	public InterfaceResult<Map<String, Object>> getRecommendedKnowledge(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable short type,@PathVariable int start, @PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String url = (String) request.getSession().getServletContext().getAttribute("newQueryHost");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("page", String.valueOf(start)));
		pairs.add(new BasicNameValuePair("rows", String.valueOf(size)));
		pairs.add(new BasicNameValuePair("type", String.valueOf(type)));// 1,推荐 2,发现
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			String responseJson = HttpClientHelper.post(url + "/API/hotKno.do", pairs);
			model.put("list", PackingDataUtil.getRecommendResult(responseJson));
		} catch (Exception e) {
			logger.error("connect big data service failed！");
			e.printStackTrace();
		}
		return InterfaceResult.getSuccessInterfaceResultInstance(model);
	}

    /**
     * 收藏知识
     * @param knowledgeId 知识Id
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/collect/{knowledgeId}/{columnId}", method = RequestMethod.POST)
    public InterfaceResult<DataCollection> collect(HttpServletRequest request, HttpServletResponse response,
                                                   @PathVariable long knowledgeId, @PathVariable short columnId) throws Exception {

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
                                                            @PathVariable long knowledgeId, @PathVariable short columnId) throws Exception {

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
                                                @PathVariable long knowledgeId, @PathVariable short columnId) throws Exception {

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
                                  @PathVariable long knowledgeId, @PathVariable short columnId) throws Exception {

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
    public InterfaceResult countBy(HttpServletRequest request,HttpServletResponse response, @PathVariable short columnId) throws Exception {
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

        long userId = user.getId();
        long createCount = 0L;
        try {
            createCount = this.knowledgeService.getKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get created knowledge count failed: userId: {}, error: {}", userId, ex.getMessage());
        }

        long collectedCount = 0L;
        try {
            collectedCount = knowledgeOtherService.myCollectKnowledgeCount(userId);
        }catch (Exception ex) {
            logger.error("get collected knowledge count failed: userId: {}, error: {}", userId, ex.getMessage());
        }
        logger.info("createCount: {}, collectedCount: {}", createCount, collectedCount);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, createCount+collectedCount);
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
            List<Long> tagIds = this.tagServiceLocal.createTag(user.getId(), tagName);
            return InterfaceResult.getSuccessInterfaceResultInstance(tagIds);
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("create Tag failed!");
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
            List<Long> directoryIds = this.directoryServiceLocal.createDirectory(user.getId(), directoryName);
            return InterfaceResult.getSuccessInterfaceResultInstance(directoryIds);
        } catch (Exception e) {
            logger.error("Get directory count failed！reason："+e.getMessage());
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("create Directory failed!");
    }

    /**
     * 推荐获取大数据推荐及个人关联知识
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "/knowledgeRelated/{type}/{start}/{size}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeRelatedResources(HttpServletRequest request, HttpServletResponse response,
                                                            @PathVariable short type,@PathVariable int start,
                                                            @PathVariable int size, @PathVariable String keyword) throws Exception {

        Map<String, Object> responseDataMap = new HashMap<String, Object>();

        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

//        String jsonContent =  this.getJsonParamStr(request);
//        if (StringUtils.isEmpty(jsonContent)) {
//            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"查询内容为空！");
//        }

        //"keyword": "知识标题或关键字", "type": "1-关联需求；2-关联人脉；3-关联组织；4-关联知识"
        /** 关键字 */
        //JSONObject json = JSONObject.fromObject(jsonContent);
        //String keywordSRC = StringUtils.trim(json.optString("keyword"));
        //String keyword = java.net.URLEncoder.encode(keywordSRC);

        long userId = user.getId();
        // 金桐网推荐的相关“知识”数据
        String durl = knowledgeBigDataSearchUrl + "/bigdata/query"; //request.getSession().getServletContext().getAttribute("bigdataQueryHost") + "/bigdata/query";
        System.out.println("------durl: "+durl);
        logger.info("------durl: "+durl);
        durl = durl + "?scope=4&title=" + keyword + "&userid=" + userId;

        //String bigDataQueryHost = "http://192.168.101.9:8090"; //TODO: This need to read from configure file.
        //String durl = request.getSession().getServletContext().getAttribute("bigdataQueryHost") + "/bigdata/query";

        String djson = "";
        try {
            djson = HttpClientHelper.get(durl);
        } catch (java.net.ConnectException e) {
            logger.error("knowledge /getKnowledgeRelatedResources.json");
        } catch (java.net.SocketTimeoutException ste) {
            logger.error("knowledge /getKnowledgeRelatedResources.json");
        }

        int RECOMMEND_COUNT = 5;
        JsonNode jsonNode = null;
        List<KnowledgeMini2> listPlatformKnowledge = new ArrayList<KnowledgeMini2>(); // 金桐脑推荐的知识
        if (null != djson && djson.trim().length() > 0 ) {
            jsonNode = KnowledgeUtil.readTree(djson);
            int status = jsonNode.get("status").intValue();
            if (status != 0) {
                if (jsonNode.get("k") != null) {
                    List<JsonNode> tempList = jsonNode.findValues("k");
                    if (tempList.size() > 0) {
                        KnowledgeMini2 km2 = null;
                        RECOMMEND_COUNT = RECOMMEND_COUNT < tempList.size() ? RECOMMEND_COUNT : tempList.size();
                        for (int i = 0; i < RECOMMEND_COUNT; i++) {
                            JsonNode m = tempList.get(i);
                            Object tempId = m.get("id");
                            Object oid = m.get("ownerid");
                            if (null == tempId || oid == null) {
                                continue;
                            }
                            String id = tempId.toString();
                            km2 = new KnowledgeMini2();
                            km2.setTitle(m.get("title") == null ? "" : m.get("title").toString());
                            km2.setId(Long.parseLong(id));
                            km2.setType(Integer.parseInt(m.get("columntype").toString()));
                            km2.setColumntype(Integer.parseInt(m.get("columntype").toString()));
                            Object objColumnPath = m.get("columnpath");
                            String columnPath = objColumnPath == null ? "" : objColumnPath.toString();
                            //TODO: fix the cptah
                            //km2.setPictureId(commonService.getDisposeString(columnPath));

                            Connections connections = new Connections();
                            String ownerid = oid.toString();
                            connections.setId(Long.parseLong(ownerid));

                            User organizationUser = null;
                            /** 推荐 用户为金桐脑 */
                            if (0 == connections.getId()) {
                                organizationUser = new User();
                                organizationUser.setType(2);// 金桐脑为机构用户
                            } else if (-1 == connections.getId()) {
                                organizationUser = new User();
                                organizationUser.setType(2);// 全平台机构用户
                            } else {
                                //TODO: need to fix this
                                //organizationUser = userService.selectByPrimaryKey(Long.parseLong(m.get("ownerid").toString()));
                            }
                            if (null != organizationUser) {
                                /** 判断用户属性 */
                                int usertype = organizationUser.getType();
                                String ownername = m.get("ownername").toString();
                                int temp = Connections.TYPE_PERSON;
                                if (usertype != 1)
                                    temp = Connections.TYPE_ORGANIZATION;
                                connections.setType(temp);
                                if (usertype == Connections.TYPE_PERSON) {
                                    JTContactMini mini = new JTContactMini();
                                    mini.setName(ownername);
                                    mini.setIsOffline(true);
                                    mini.setOwnerid(Long.parseLong(ownerid));
                                    mini.setOwnername(ownername);
                                    connections.setJtContactMini(mini);
                                } else {
                                    OrganizationMini om = new OrganizationMini();
                                    om.setFullName(ownername);
                                    om.setIsOnline(true);
                                    om.setOwnerid(Long.parseLong(ownerid));
                                    om.setOwnername(ownername);
                                    connections.setOrganizationMini(om);
                                }
                            }
                            km2.setConnections(connections);
                            listPlatformKnowledge.add(km2);
                        }
                        }
                    }
            }
        }

        // 用户自己的所有知识
        List<KnowledgeMini2> listUserKnowledge = null; // 用户自己的知识
        List<KnowledgeBase> userKnowledgeBase = null;
        List<DataCollection> result = null;
        if ("null".equals(keyword) || keyword == null) {
            userKnowledgeBase = knowledgeService.getBaseByCreateUserId(user.getId(), start, size);
        } else {
            userKnowledgeBase = knowledgeService.getBaseByKeyWord(user.getId(), start, size, keyword);
        }

        if (userKnowledgeBase != null && userKnowledgeBase.size() > 0) {
            listUserKnowledge = convertKnowledgeBaseList(userKnowledgeBase);
        }

        responseDataMap.put("listPlatformKnowledge", listPlatformKnowledge);
        responseDataMap.put("listUserKnowledge", listUserKnowledge);
        responseDataMap.put("type", type);
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    private List<KnowledgeMini2> changeKnowledgeMini2(List<DataCollection> data) {
        List<KnowledgeMini2> kbds = new ArrayList<KnowledgeMini2>();
        for (DataCollection collection : data) {
            KnowledgeBase kbd = collection.getKnowledge();
            if (kbd != null) {
                chanageKnowledgeToMini2(kbd);
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
            km2List.add(chanageKnowledgeToMini2(base));
        }

        return km2List;
    }

    public KnowledgeMini2 chanageKnowledgeToMini2(KnowledgeBase knowledgeBase)
    {
        KnowledgeMini2 km2 = new KnowledgeMini2();

        //分享给我的知识专用
        km2.setShareMeId(knowledgeBase.getShareMeId());

        Connections connections = new Connections();
        /**作者id*/
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

        KnowledgeDetail kn = null;
        if(knowledgeBase.getKnowledgeId() !=0 && knowledgeBase.getColumnId() != 0) {
            try {
                kn = knowledgeService.getDetailById(knowledgeBase.getKnowledgeId(), knowledgeBase.getColumnId());
            } catch (Exception ex) {
                logger.error("Get Knowledge Detail failed: KnowledgeId: {}, ColumnId: {}, error: {}",
                        knowledgeBase.getKnowledgeId(), knowledgeBase.getColumnId(), ex.getMessage());
            }
        }
        if(kn!=null) {
            km2.setListTag(kn.getTags());//标签
            km2.setSource(kn.getSource());// 来源
            km2.setTitle(kn.getTitle());// 标题
            km2.setPic(changeImage(kn.getPic()));// 封面地址
            km2.setDesc(getDesc(kn.getDesc()));// 描述
            km2.setModifytime(kn.getModifyTime());// 最后修改时间
        } else {
            List<Long> tags = KnowledgeUtil.convertStringToLongList(knowledgeBase.getTags());
            km2.setListTag(tags);//标签
            km2.setSource(knowledgeBase.getSource());// 来源
            km2.setTitle(knowledgeBase.getTitle());// 标题
            km2.setPic(changeImage(knowledgeBase.getPictureId()));// 封面地址
            km2.setDesc(knowledgeBase.getContentDesc());// 描述
            km2.setModifytime(knowledgeBase.getModifyDate());// 最后修改时间
        }
        km2.setColumnpath(getDisposeString(knowledgeBase.getCpath()));
        return km2;
    }

    /** 图片头部信息拼接 */
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
                picPath = ContantDefine.ORGAN_DEFAULT_PIC_PATH;
            else
                picPath = ContantDefine.USER_DEFAULT_PIC_PATH_MALE;
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

    private List<Long> createAssociate(List<Associate> as, long knowledgeId, long userId)
    {
        if (as == null || as.size() <= 0) {
            return null;
        }

        List<Long> assoIdList = new ArrayList<Long>();
        try {
            for (int index = 0; index < as.size(); index++) {
                Associate associate = as.get(index);
                associate.setSourceId(knowledgeId);
                associate.setSourceTypeId(KnowledgeBaseService.sourceType);
                //associate.setAssocTypeId(assoType.getId());
                associate.setUserId(userId);
                associate.setAppId(APPID);
                long assoId = associateService.createAssociate(APPID, userId, associate);
                assoIdList.add(assoId);
                logger.info("assoid:" + assoId);
            }
        }catch (AssociateServiceException e) {
            logger.error("update Asso failed！reason：" + e.getMessage());
            return null;
        }

        return assoIdList;
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
                    associateService.removeAssociate(APPID, userId, associateList.get(j).getId());
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
            System.out.println(" knowledgeIds:" + knowledgeIds.toString() + " keyword:"+keyword);
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

    private InterfaceResult<DataCollection> knowledgeDetail(User user,long knowledgeId, short columnId) {
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if(knowledgeId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION, "知识Id无效");
        }

        DataCollection data = new DataCollection();
        KnowledgeDetail knowledgeDetail = null;
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        try {
            knowledgeDetail = this.knowledgeService.getDetailById(knowledgeId, columnId);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：" + e.getMessage());
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //数据为空则直接返回异常给前端
        if(knowledgeDetail == null) {
            logger.error("get knowledge failed: knowledgeId: {}, columnId: {}", knowledgeId, columnId);
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        String hContent = HtmlToText.htmlToText(knowledgeDetail.getContent());
        int maxLen = hContent.length() >= 250 ? 250 : hContent.length();
        hContent = hContent.substring(0, maxLen);
        knowledgeDetail.setHcontent(hContent);
        knowledgeDetail.setVirtual(user.isVirtual() ? (short)2 : (short)1);
        data.setKnowledgeDetail(knowledgeDetail);

        boolean isCollected = false;
        long userId = user.getId();
        try {
            isCollected = knowledgeOtherService.isCollectedKnowledge(userId, knowledgeId, columnId);
        } catch (Exception ex) {
            logger.error("Query knowledge is collected or not failed: userId: {}, knowledgeId: {}, columnId: {}", userId, knowledgeId, columnId);
        }
        knowledgeDetail.setCollected(isCollected ? (short)1 : (short)0);

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
}