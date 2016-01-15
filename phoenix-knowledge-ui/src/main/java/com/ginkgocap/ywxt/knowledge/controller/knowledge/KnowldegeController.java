package com.ginkgocap.ywxt.knowledge.controller.knowledge;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ginkgocap.ywxt.knowledge.controller.BaseController;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.service.IKnowledgeService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;

@Controller
@RequestMapping("/knowledge")
public class KnowldegeController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(KnowldegeController.class);
	
	@Autowired
	private IKnowledgeService knowledgeService;
	
	/**
	 * 插入数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:51:59
	 * @throws IOException
	 */
	@RequestMapping(value = "",method = RequestMethod.POST)
	@ResponseBody
	public InterfaceResult<DataCollection> insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		
		JSONObject requestJson = this.getRequestJson(request);
		
		DataCollection dataCollection = (DataCollection) JSONObject.toBean(requestJson, DataCollection.class);
		
		InterfaceResult<DataCollection> affterSaveDataCollection = null;
		
		try {
			affterSaveDataCollection = this.knowledgeService.insert(dataCollection, user);
		} catch (Exception e) {
			logger.error("知识插入失败！失败原因："+affterSaveDataCollection.getNotification().getNotifInfo());
			return affterSaveDataCollection;
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
	}
	
	/**
	 * 更新数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午4:52:17
	 * @throws IOException
	 */
	@RequestMapping(value = "",method = RequestMethod.PUT)
	@ResponseBody
	public InterfaceResult<DataCollection> update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		
		JSONObject requestJson = this.getRequestJson(request);
		
		DataCollection dataCollection = (DataCollection) JSONObject.toBean(requestJson, DataCollection.class);
		
		InterfaceResult<DataCollection> affterSaveDataCollection = null;
		
		try {
			affterSaveDataCollection = this.knowledgeService.update(dataCollection, user);
		} catch (Exception e) {
			logger.error("知识更新失败！失败原因："+affterSaveDataCollection.getNotification().getNotifInfo());
			return affterSaveDataCollection;
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
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
	public InterfaceResult<DataCollection> delete(HttpServletRequest request, HttpServletResponse response,@PathVariable Long id,@PathVariable Long columnId) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		if(id <= 0 || columnId <= 0){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
		}
		
		InterfaceResult<DataCollection> affterDeleteDataCollection = null;
		try {
			affterDeleteDataCollection = this.knowledgeService.deleteByKnowledgeId(id, columnId, user);
		} catch (Exception e) {
			logger.error("知识删除失败！失败原因："+affterDeleteDataCollection.getNotification().getNotifInfo());
			return affterDeleteDataCollection;
		}
		
		return InterfaceResult.getSuccessInterfaceResultInstance(null);
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
	public InterfaceResult<DataCollection> getByIdAndColumnId(HttpServletRequest request, HttpServletResponse response,@PathVariable Long id,@PathVariable Long columnId) throws IOException {
		
		User user = this.getUser(request);
		
		InterfaceResult<DataCollection> affterDeleteDataCollection = null;
		try {
			affterDeleteDataCollection = this.knowledgeService.getDetailById(id, columnId, user);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+affterDeleteDataCollection.getNotification().getNotifInfo());
			return affterDeleteDataCollection;
		}
		
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
	public InterfaceResult<List<DataCollection>> getAll(HttpServletRequest request, HttpServletResponse response,@PathVariable int start,@PathVariable int size) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		
		InterfaceResult<List<DataCollection>> getDataCollection = null;
		try {
			getDataCollection = this.knowledgeService.getBaseAll(start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}
		
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
	public InterfaceResult<List<DataCollection>> getAllByColumnId(HttpServletRequest request, HttpServletResponse response,@PathVariable long columnId,@PathVariable int start,@PathVariable int size) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		
		InterfaceResult<List<DataCollection>> getDataCollection = null;
		try {
			getDataCollection = this.knowledgeService.getBaseByCreateUserId(user, start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}
		
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
	public InterfaceResult<List<DataCollection>> getCreateUserId(HttpServletRequest request, HttpServletResponse response,@PathVariable int start,@PathVariable int size) throws IOException {
		
		User user = this.getUser(request);
		
		if(user == null)
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
		
		InterfaceResult<List<DataCollection>> getDataCollection = null;
		try {
			getDataCollection = this.knowledgeService.getBaseByCreateUserId(user, start, size);
		} catch (Exception e) {
			logger.error("知识提取失败！失败原因："+getDataCollection.getNotification().getNotifInfo());
			return getDataCollection;
		}
		
		return getDataCollection;
	}
	
}