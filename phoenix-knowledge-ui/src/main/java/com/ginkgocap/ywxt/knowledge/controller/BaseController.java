package com.ginkgocap.ywxt.knowledge.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.ginkgocap.ywxt.user.model.User;

import net.sf.json.JSONObject;



public class BaseController {
	
	/**
	 * 获取用户
	 * @author 周仕奇
	 * @date 2016年1月15日 下午3:20:50
	 * @param request
	 * @return
	 */
	public User getUser(HttpServletRequest request) {
		//在AppFilter过滤器里面从cache获取了当前用户对象并设置到request中了
		return (User) request.getAttribute("sessionUser");
	}
	
	/**
	 * 获取前端数据
	 * @author 周仕奇
	 * @date 2016年1月15日 下午3:21:19
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public JSONObject getRequestJson(HttpServletRequest request) throws IOException {
		
		String requestString = (String)request.getAttribute("requestJson");
		
		JSONObject requestJson = JSONObject.fromObject(requestString == null ? "" : requestString);
		
		return requestJson;
	}
	
}