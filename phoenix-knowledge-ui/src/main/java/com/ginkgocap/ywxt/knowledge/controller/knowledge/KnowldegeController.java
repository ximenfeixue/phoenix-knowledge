package com.ginkgocap.ywxt.knowledge.controller.knowledge;

import com.ginkgocap.ywxt.asso.service.IAssoService;
import com.ginkgocap.ywxt.knowledge.controller.BaseController;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.service.IKnowledgeService;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.HttpClientHelper;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/knowledge")
public class KnowldegeController extends BaseController {
	
	private final Logger logger = LoggerFactory.getLogger(KnowldegeController.class);
	
	@Autowired
	private IKnowledgeService knowledgeService;

    @Autowired
    private IAssoService assoService;
	
	/**
	 * 插入数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:51:59
	 * @throws IOException
	 */
	@RequestMapping(value = "",method = RequestMethod.POST)
	@ResponseBody
	public InterfaceResult<DataCollection> create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = this.getUser(request);
		
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        /*
		String requestJson = this.getBodyParam(request);
		DataCollection dataCollection = KnowledgeUtil.getDataCollection(requestJson);
		
		InterfaceResult<DataCollection> affterSaveDataCollection = null;
		
		try {
			affterSaveDataCollection = this.knowledgeService.insert(dataCollection, user);
		} catch (Exception e) {
			logger.error("知识插入失败！失败原因："+affterSaveDataCollection.getNotification().getNotifInfo());
			return affterSaveDataCollection;
		}*/
        logger.info(".......create knowledge success......");
		return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}
	
	/**
	 * 更新数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:52:17
	 * @throws IOException
	 */
	@RequestMapping(value = "",method = RequestMethod.PUT)
	@ResponseBody
	public InterfaceResult<DataCollection> updateKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User user = this.getUser(request);
		
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		/*
		String requestJson = this.getBodyParam(request);
		DataCollection dataCollection = KnowledgeUtil.getDataCollection(requestJson);
		InterfaceResult<DataCollection> affterSaveDataCollection = null;
		
		try {
			affterSaveDataCollection = this.knowledgeService.update(dataCollection, user);
		} catch (Exception e) {
			logger.error("知识更新失败！失败原因："+affterSaveDataCollection.getNotification().getNotifInfo());
			return affterSaveDataCollection;
		}*/
        logger.info(".......update knowledge success......");
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}
	
	/**
	 * 删除数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:52:33
	 * @param id 知识主键
	 * @param columnId 栏目主键
	 * @throws IOException
	 */
	@RequestMapping(value = "/{id}/{columnId}", method = RequestMethod.DELETE)
	@ResponseBody
	public InterfaceResult<DataCollection> delete(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long id,@PathVariable Long columnId) throws Exception {
		
		User user = this.getUser(request);
		
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

		if(id <= 0 || columnId <= 0){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
		}

		InterfaceResult<DataCollection> affterDeleteDataCollection = null;
        /*
		try {
			affterDeleteDataCollection = this.knowledgeService.deleteByKnowledgeId(id, columnId, user);
		} catch (Exception e) {
			logger.error("知识删除失败！失败原因："+affterDeleteDataCollection.getNotification().getNotifInfo());
			return affterDeleteDataCollection;
		}*/
        logger.info(".......delete knowledge success......");
		return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
	}
	
	/**
	 * 提取知识详细信息，一般用在详细查看界面、编辑界面
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:53:25
	 * @param id 知识主键
	 * @param columnId 栏目主键
	 * @throws IOException
	 */
	@RequestMapping(value = "/{id}/{columnId}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<DataCollection> detail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long id,@PathVariable Long columnId) throws Exception {
		
		//判断参数合法性
		if(id <= 0 || columnId <= 0) {
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
		}
		
		User user = this.getUser(request);
		
		//如果是游客登录，则给予金桐脑权限
		if (null == user) {
			user = new User();
			user.setId(0);// 金桐脑
		}
		
		InterfaceResult<DataCollection> affterDeleteDataCollection = DummyData.knowledgeDetailObject();
        /*
		try {
			affterDeleteDataCollection = this.knowledgeService.getDetailById(id, columnId, user);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+affterDeleteDataCollection.getNotification().getNotifInfo());
			return affterDeleteDataCollection;
		}
		
		//数据为空则直接返回异常给前端
		if(affterDeleteDataCollection == null) {
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);//.DATA_DELETE_EXCEPTION);
		}*/
        logger.info(".......get knowledge detail success......");
		return affterDeleteDataCollection;
	}
	
	/**
	 * 提取所有知识数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:54:27
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
		
		InterfaceResult<List<DataCollection>> getDataCollection = DummyData.resultObject(DummyData.getDataCollectionList());
        /*
		try {
			getDataCollection = this.knowledgeService.getBaseAll(start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}*/
        logger.info(".......get all knowledge success......");
		return getDataCollection;
	}
	
	/**
	 * 根据栏目提取知识数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:54:27
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/all/{columnId}/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getAllByColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
		
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		
		InterfaceResult<List<DataCollection>> getDataCollection = DummyData.resultObject(DummyData.getDataCollectionList());;
        /*
		try {
			getDataCollection = this.knowledgeService.getBaseByCreateUserId(user, start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因：{}",getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}*/
        logger.info(".......get all knowledge by columnId success......");
		return getDataCollection;
	}
	
	/**
	 * 提取当前用户的所有知识数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:54:27
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
		
		InterfaceResult<List<DataCollection>> getDataCollection = DummyData.resultObject(DummyData.getDataCollectionList());
        /*
		try {
			getDataCollection = this.knowledgeService.getBaseByCreateUserId(user, start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}*/
        logger.info(".......get all knowledge by create userId success......");
		return getDataCollection;
	}
	
	/**
	 * 根据栏目提取当前用户的知识数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:54:27
	 * @param start 分页起始
	 * @param size 分页大小
	 * @throws IOException
	 */
	@RequestMapping(value = "/user/{columnId}/{start}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<DataCollection>> getByCreateUserIdAndColumnId(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long columnId,@PathVariable int start,@PathVariable int size) throws Exception {
		
		User user = this.getUser(request);
		
		if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
		
		InterfaceResult<List<DataCollection>> getDataCollection = DummyData.resultObject(DummyData.getDataCollectionList());
        /*
		try {
			getDataCollection = this.knowledgeService.getBaseByCreateUserIdAndColumnId(user, columnId, start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}*/
        logger.info(".......get all knowledge by create userId and columnId success......");
		return getDataCollection;
	}
	
	/**
	 * 首页获取大数据知识热门推荐和发现热门知识
	 * @author 周仕奇
	 * @date 2016年1月18日 上午10:46:43
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
			logger.error("连接大数据出错！");
			e.printStackTrace();
		}
		return InterfaceResult.getSuccessInterfaceResultInstance(model);
	}
}