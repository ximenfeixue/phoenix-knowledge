package com.ginkgocap.ywxt.knowledge.controller.knowledge;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ginkgocap.ywxt.knowledge.controller.BaseController;
import com.ginkgocap.ywxt.knowledge.model.ColumnCustom;
import com.ginkgocap.ywxt.knowledge.service.IColumnCustomService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;

@Controller
@RequestMapping("/knowledge_columncustom")
public class ColumnCustomController extends BaseController {
	
	@Resource
	private IColumnCustomService columnCustomService;
	
	@RequestMapping(value="/showColumn/{pid}",method = RequestMethod.GET)
	@ResponseBody
	public InterfaceResult<List<ColumnCustom>> showColumn(HttpServletRequest request, HttpServletResponse response,@PathVariable Long pid) throws Exception{
		InterfaceResult<List<ColumnCustom>> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		User user = this.getUser(request);
		List<ColumnCustom> list=null;
		long uid=0l;
		if(user!=null){
			uid=user.getId();
		}

		list=this.columnCustomService.queryListByPidAndUserId(uid, pid);
		result.setResponseData(list);
		return result;
	}
	
	@RequestMapping(value="addColumn",method = RequestMethod.POST)
	@ResponseBody
	public InterfaceResult<ColumnCustom> addColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		User user = this.getUser(request);
		JSONObject requestJson = this.getRequestJson(request);
		//{"columnname":"l2","pid":0,"type":1,"pathName":"l2","tags":"ddd,kkk"}:
		if(!requestJson.containsKey("columnname")||!requestJson.containsKey("pid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long pid=requestJson.getLong("pid");
		ColumnCustom cc=new ColumnCustom();
		cc.setColumnLevelPath("1111111111");
		cc.setColumnName(requestJson.getString("columnname"));
		cc.setCreateTime(new Date());
		cc.setUpdateTime(new Date());
		cc.setOrderNum(0);
		cc.setPathName(requestJson.getString("pathName"));
		cc.setTopColType(0);
		cc.setUserOrSystem((short)1);
		long uid=0l;
		if(user!=null){
			uid=user.getId();
		}
		ColumnCustom newCC=this.columnCustomService.insert(cc, uid);
		result.setResponseData(newCC);
		return result;
	}
	
	@RequestMapping(value="",method = RequestMethod.PUT)
	@ResponseBody
	public InterfaceResult<ColumnCustom> updateColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		User user = this.getUser(request);
		JSONObject requestJson = this.getRequestJson(request);
		//{"columnid":"2854","columnName":"l21","tags":"栏目标签"}:
		if(!requestJson.containsKey("columnname")||!requestJson.containsKey("columnid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long columnid=requestJson.getLong("columnid");
		ColumnCustom cc=this.columnCustomService.queryById(columnid);
		cc.setColumnName(requestJson.getString("columnName"));
		this.columnCustomService.update(cc);
		return result;
	}
	
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	public InterfaceResult<ColumnCustom> deleteColumn(HttpServletRequest request, HttpServletResponse response,@PathVariable Long id) throws Exception{
		InterfaceResult<ColumnCustom> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		User user = this.getUser(request);
		JSONObject requestJson = this.getRequestJson(request);
		if(id==null){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		this.columnCustomService.del(id);
		return result;
	}

}
