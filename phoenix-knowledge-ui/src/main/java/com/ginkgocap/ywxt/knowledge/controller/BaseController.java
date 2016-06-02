package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.cache.redis.RedisCacheService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class BaseController {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Resource
    private RedisCacheService redisCacheService;

    protected static final long APPID = 1l;

    /**
     * 从body中获得参数
     * @param request
     * @return
     * @throws Exception
     */
    protected String getBodyParam(HttpServletRequest request) throws Exception {
        BufferedReader reader = request.getReader();
        String line = null;
        StringBuffer jsonIn = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            jsonIn.append(line);
        }
        return jsonIn.toString();
    }

    /**
     * 从redis中获得user
     * @param request
     * @return
     * @throws Exception
     */
    protected User getUser(HttpServletRequest request) {
//        String key = UserUtil.getUserSessionKey(request);
//        User user = (User) redisCacheService.getRedisCacheByKey(key);
//        return user;
        return KnowledgeUtil.getDummyUser();
    }

    protected JsonNode getJsonNode(String jsonStr, String... values)
            throws Exception {
        return KnowledgeUtil.getJsonNode(jsonStr, values);
    }

    protected String getJsonIn(HttpServletRequest request) throws IOException {
        String requestJson=(String)request.getAttribute("requestJson");
        if(requestJson==null){
            return "";
        }
        return requestJson;
    }

    protected String getJsonParamStr(HttpServletRequest request)
            throws IOException {
        String result = getJsonIn(request);
        return result;
    }

    protected User getJTNUser(HttpServletRequest request) throws Exception {
        User user = getUser(request);

        if(null == user){
            user = new User();
            user.setId(0);//金桐脑
            return user;
        }
        return user;
    }

    public JSONObject getRequestJson(HttpServletRequest request) throws IOException {

        String requestString = (String)request.getAttribute("requestJson");

        JSONObject requestJson = JSONObject.fromObject(requestString == null ? "" : requestString);

        return requestJson;
    }

    protected InterfaceResult checkColumn(short column)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        result.setResponseData("column Id is invalidate!");
        return result;
    }

    protected InterfaceResult checkStartAndSize(int start, int size)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        result.setResponseData("start and size are invalidate!");
        return result;
    }

    protected InterfaceResult checkKeyword(String keyword)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        result.setResponseData("keyword invalidate!");
        return result;
    }

}