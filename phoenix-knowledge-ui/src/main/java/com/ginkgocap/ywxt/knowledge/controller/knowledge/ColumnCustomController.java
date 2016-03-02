package com.ginkgocap.ywxt.knowledge.controller.knowledge;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	
	@RequestMapping("showColumn")
	@ResponseBody
	public InterfaceResult<List<ColumnCustom>> showColumn(HttpServletRequest request, HttpServletResponse response) throws Exception{
		InterfaceResult<List<ColumnCustom>> result=InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
		User user = this.getUser(request);
		JSONObject requestJson = this.getRequestJson(request);
		if(!requestJson.containsKey("pid")){
			return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
		}
		Long pid=requestJson.getLong("pid");
		List<ColumnCustom> list=null;
		long uid=0l;
		if(user!=null){
			uid=user.getId();
		}

		list=this.columnCustomService.queryListByPidAndUserId(uid, pid);
		result.setResponseData(list);
		return result;
	}

}
