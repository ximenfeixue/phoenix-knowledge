package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.exception.AssociateServiceException;
import com.ginkgocap.parasol.associate.exception.AssociateTypeServiceException;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.associate.model.AssociateType;
import com.ginkgocap.parasol.associate.service.AssociateService;
import com.ginkgocap.parasol.associate.service.AssociateTypeService;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReport;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.HttpClientHelper;
import com.gintong.common.phoenix.permission.PermissionAnnotation;
import com.gintong.common.phoenix.permission.PermissionOpType;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.service.PermissionRepositoryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.frame.util.dto.Notification;
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
    KnowledgeOtherService knowledgeOtherService;

    @Autowired
    private AssociateService associateService;

    @Autowired
    private AssociateTypeService assoTypeService;

    @Autowired
    private PermissionRepositoryService permissionRepositoryService;

    private static final long APPID = 1L;
	
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
        long userId = user.getUid();

		String requestJson = this.getBodyParam(request);
		DataCollection dataCollection = KnowledgeUtil.getDataCollection(requestJson);

		InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		try {
            dataCollection.serUserInfo(user);
            result = this.knowledgeService.insert(dataCollection);
		} catch (Exception e) {
			logger.error("Insert knowledge failed : " + e.getMessage());
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}

        if (result == null || result.getNotification()== null) {
            logger.error("Insert knowledge failed!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        long knowledgeId = Long.valueOf(result.getResponseData().toString());
        //save asso information

        //TODO: If this step failed, how to do ?
        try {
            AssociateType assoType = assoTypeService.getAssociateTypeByName(APPID, "知识");
            List<Associate> as  = dataCollection.getAsso();
            if (as == null) {
                logger.error("asso it null or converted failed...");
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
            }

            createAssociate(as, knowledgeId, userId, assoType);
        }catch (Exception e) {
            logger.error("Insert asso failed : " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        try {
            if (dataCollection.getPermission() != null) {
                dataCollection.getPermission().setResId(knowledgeId);
                dataCollection.getPermission().setResOwnerId(userId);
                dataCollection.getPermission().setResType(ResourceType.KNOW.getVal());
                if (dataCollection.getPermission().getPerTime() == null) {
                    dataCollection.getPermission().setPerTime(new Date());
                }
                permissionRepositoryService.insert(dataCollection.getPermission());
            }
        } catch (Exception e) {
            logger.error("Insert knowledge failed : " + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
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
    @PermissionAnnotation(perType= PermissionOpType.UPDATE, resType=ResourceType.KNOW)
	public InterfaceResult updateKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = user.getId();

		String requestJson = this.getBodyParam(request);
		DataCollection data = KnowledgeUtil.getDataCollection(requestJson);
		InterfaceResult result = null;

		try {
            data.serUserInfo(user);
            result = this.knowledgeService.update(data);
		} catch (Exception e) {
			logger.error("知识更新失败！失败原因："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}

        long knowledgeId = data.getKnowledgeDetail().getId();
        //Update Assso info
        AssociateType assoType = assoTypeService.getAssociateTypeByName(APPID,"知识");
        Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, assoType.getId(), knowledgeId);
        if (assomap == null) {
            logger.error("asso it null or converted failed...");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
        }
        //TODO: If this step failed, how to do ?
        try {
            for (Iterator i = assomap.values().iterator(); i.hasNext(); ) {
                List<Associate> associateList = (List) i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    associateService.removeAssociate(APPID, user.getId(), associateList.get(j).getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("Asso update failed！reason：" + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        List<Associate> as = data.getAsso();
        createAssociate(as, knowledgeId, userId, assoType);

        logger.info(".......update knowledge success......");
        return result;
	}
	
	/**
	 * 删除数据
	 * @param id 知识主键
	 * @param columnId 栏目主键
	 * @throws IOException
	 */
    @ResponseBody
	@RequestMapping(value="/{id}/{columnId}", method = RequestMethod.DELETE)
	public InterfaceResult delete(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id,@PathVariable short columnId) throws Exception {
		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

		if(id <= 0 || columnId <= 0){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
		}

        Permission per=new Permission();
        per.setResType(ResourceType.KNOW.getVal());
        per.setResId(id);
        InterfaceResult<Boolean> result = permissionRepositoryService.delete(per);
        if (result == null || !result.getResponseData().booleanValue()) {
            logger.error("permission validate failed, please check if user have permission!");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

		try {
            result = this.knowledgeService.deleteByKnowledgeId(id, columnId);
		} catch (Exception e) {
			logger.error("knowledge delete failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}

        //delete Assso info
        AssociateType assoType = assoTypeService.getAssociateTypeByName(APPID,"知识");
        Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, assoType.getId(), id);
        if (assomap == null) {
            logger.error("asso it null or converted failed...");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_EXCEPTION_60008);
        }
        //TODO: If this step failed, how to do ?
        for (Iterator i =  assomap.values().iterator(); i.hasNext();) {
            List<Associate> associateList = (List)i.next();
            for (int j = 0; j < associateList.size(); j++) {
                associateService.removeAssociate(APPID, user.getId(), associateList.get(j).getId());
            }
        }

        logger.info(".......delete knowledge success......");
		return result;
	}

    /**
     * 批量删除数据
     * @param columnId 栏目主键
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value="/delete/{columnId}", method = RequestMethod.PUT)
    public InterfaceResult batchDelete(HttpServletRequest request,HttpServletResponse response,@PathVariable short columnId) throws Exception {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if(columnId <= 0){
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        String[] konwledgeIds = requestJson.split(",");
        if (konwledgeIds == null || konwledgeIds.length <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        List<Long> permDeleteIds = new ArrayList<Long>();
        List<Long> failedIds = new ArrayList<Long>();
        for (String Id : konwledgeIds) {
            long knowledgeId = Long.parseLong(Id);
            Permission per = new Permission();
            per.setResType(ResourceType.KNOW.getVal());
            per.setResId(knowledgeId);
            InterfaceResult<Boolean> result = permissionRepositoryService.delete(per);
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
            result = this.knowledgeService.batchDeleteByKnowledgeIds(permDeleteIds, columnId);
        } catch (Exception e) {
            logger.error("knowledge delete failed！reason："+e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        //delete Assso info
        for (long knowledgeId : permDeleteIds) {
            deleteAssociate(knowledgeId, user.getId());
        }

        logger.info(".......delete knowledge success......");
        if (failedIds.size() > 0) {
            result.setResponseData("failedId:" + failedIds.toArray());
        }
        return result;
    }
	
	/**
	 * 提取知识详细信息，一般用在详细查看界面、编辑界面
	 * @param id 知识主键
	 * @param columnId 栏目主键
	 * @throws IOException
	 */
	@RequestMapping(value = "/{id}/{columnId}", method = RequestMethod.GET)
	@ResponseBody
	public MappingJacksonValue detail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long id,@PathVariable short columnId) throws Exception {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		if(id <= 0 || columnId <= 0) {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
		}

		User user = this.getUser(request);
		if (null == user) {
			user = new User();
			user.setId(0);// 金桐脑
		}

        DataCollection data = new DataCollection();
		InterfaceResult<KnowledgeDetail> knowledgeDetail = null; //DummyData.knowledgeDetailObject();
		try {
            knowledgeDetail = this.knowledgeService.getDetailById(id, columnId);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason：" + knowledgeDetail.getNotification().getNotifInfo());
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        data.setKnowledgeDetail(knowledgeDetail.getResponseData());

		//数据为空则直接返回异常给前端
		if(knowledgeDetail == null) {
            result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);//.DATA_DELETE_EXCEPTION);
		}

        InterfaceResult<Permission> ret = permissionRepositoryService.selectByRes(id, ResourceType.KNOW);
        Notification noti = ret.getNotification();
        if (noti != null && noti.getNotifCode().equals(CommonResultCode.SUCCESS.getCode())){
            data.setPermission(ret.getResponseData());
        }

        AssociateType assoType = assoTypeService.getAssociateTypeByName(APPID, "知识");
        Map<AssociateType, List<Associate>> assomap =  associateService.getAssociatesBy(APPID, assoType.getId(), id);
        if (assomap.values() != null) {
            List assoList = new ArrayList(assomap.size());
            for (Iterator i = assomap.values().iterator(); i.hasNext();) {
                List<Associate> associateList = (List)i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    assoList.add(associateList.get(j));
                }
            }
            data.setAsso(assoList);
        }

        logger.info(".......get knowledge detail success......");
        result.setResponseData(data);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setFilters(KnowledgeUtil.assoSimpleFilterProvider(null));

        return jacksonValue;
	}
	
	/**
	 * 提取所有知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/all/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getAll(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int start,@PathVariable int size) throws Exception {

		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

		InterfaceResult<List<DataCollection>> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
		try {
			dataCollectionList = this.knowledgeService.getBaseAll(start, size);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason：" + dataCollectionList.getNotification().getNotifInfo());
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        logger.info(".......get all knowledge success......");
		return dataCollectionList;
	}
	
	/**
	 * 根据栏目提取知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/all/{columnId}/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getAllByColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable short columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		
		InterfaceResult<List<DataCollection>> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
		try {
            dataCollectionList = this.knowledgeService.getBaseByColumnId(columnId, start, size);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason：{}",dataCollectionList.getNotification().getNotifInfo());
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        logger.info(".......get all knowledge by columnId success......");
		return dataCollectionList;
	}

    @RequestMapping(value = "/all/{keyWord}//{start}/{size}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult<List<DataCollection>> getAllByKeyWord(HttpServletRequest request, HttpServletResponse response,
                                                                 @PathVariable String keyWord,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<List<DataCollection>> dataCollectionList = null;
        try {
            dataCollectionList = this.knowledgeService.getBaseByKeyWord(keyWord, start, size);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}",dataCollectionList.getNotification().getNotifInfo());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return dataCollectionList;
    }

    @RequestMapping(value = "/tag/{tagId}//{start}/{size}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult<List<DataCollection>> getAllByTagId(HttpServletRequest request, HttpServletResponse response,
                                                                 @PathVariable String tagId,@PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<List<DataCollection>> dataCollectionList = null;
        try {
            dataCollectionList = this.knowledgeService.getBaseByKeyWord(tagId, start, size);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}",dataCollectionList.getNotification().getNotifInfo());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return dataCollectionList;
    }

    @RequestMapping(value = "/all/{keyWord}/{columnId}/{start}/{size}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult<List<DataCollection>> getAllByColumnIdAndKeyWord(HttpServletRequest request, HttpServletResponse response,
                                                                            @PathVariable String keyWord,@PathVariable short columnId,
                                                                            @PathVariable int start,@PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        InterfaceResult<List<DataCollection>> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
        try {
            dataCollectionList = this.knowledgeService.getBaseByColumnIdAndKeyWord(keyWord, columnId, start, size);
        } catch (Exception e) {
            logger.error("Query knowledge failed！reason：{}",dataCollectionList.getNotification().getNotifInfo());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        logger.info(".......get all knowledge by columnId success......");
        return dataCollectionList;
    }

	/**
	 * 提取当前用户的所有知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getByCreateUserId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		
		InterfaceResult<List<DataCollection>> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
		try {
            dataCollectionList = this.knowledgeService.getBaseByCreateUserId(user.getId(), start, size);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason："+dataCollectionList.getNotification().getNotifInfo());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        logger.info(".......get all knowledge by create userId success......");
		return dataCollectionList;
	}
	
	/**
	 * 根据栏目提取当前用户的知识数据
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/{columnId}/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getByCreateUserIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable short columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		
		InterfaceResult<List<DataCollection>> dataCollectionList = null; //DummyData.resultObject(DummyData.getDataCollectionList());
		try {
            dataCollectionList = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user.getId(), columnId, start, size);
		} catch (Exception e) {
			logger.error("Query knowledge failed！reason："+dataCollectionList.getNotification().getNotifInfo());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
		}
        logger.info(".......get all knowledge by create userId and columnId success......");
		return dataCollectionList;
	}
	
	/**
	 * 首页获取大数据知识热门推荐和发现热门知识
	 * @param type 1,推荐 2,发现
	 * @param start
	 * @param size
	 * @throws Exception
	 */
	@RequestMapping(value = "/all/{type}/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<Map<String, Object>> getRecommendedKnowledge(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String type,@PathVariable int start, @PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String url = (String) request.getSession().getServletContext().getAttribute("newQueryHost");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("page", String.valueOf(start)));
		pairs.add(new BasicNameValuePair("rows", String.valueOf(size)));
		pairs.add(new BasicNameValuePair("type", type));// 1,推荐 2,发现
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
		}
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
			logger.error("Delete knowledge failed！reason："+e.getMessage());
		}
        logger.info(".......report knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    private InterfaceResult createAssociate(List<Associate> as, long knowledgeId, long userId,AssociateType assoType)
    {
        try {
            for (int index = 0; index < as.size(); index++) {
                Associate associate = as.get(index);
                associate.setSourceId(knowledgeId);
                associate.setSourceTypeId(assoType.getId());
                associate.setAssocTypeId(assoType.getId());
                associate.setUserId(userId);
                associate.setAppId(APPID);
                long assoId = associateService.createAssociate(APPID, userId, associate);
                logger.info("assoid:" + assoId);
            }
        }catch (Exception e) {
            logger.error("update Asso failed！reason：" + e.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
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
            ex.printStackTrace();
        }
        catch (AssociateServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }
}