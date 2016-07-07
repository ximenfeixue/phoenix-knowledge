package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.ResItem;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.cache.redis.RedisCacheService;
import com.gintong.frame.util.UserUtil;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

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
    protected String getBodyParam(HttpServletRequest request) {
        BufferedReader reader = null;
        StringBuffer jsonIn = new StringBuffer();
        try {
            reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonIn.append(line);
            }
        } catch (IOException e) {
            logger.error("read request body failed : "+e.getMessage());
            e.printStackTrace();
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
        String key = UserUtil.getUserSessionKey(request);
        User user = (User) redisCacheService.getRedisCacheByKey(key);
        if (user != null) {
            logger.info("login userId: {}, userName: {}", user.getId(), user.getName());
        }
        return user;
        //return KnowledgeUtil.getDummyUser();
    }

    protected long getUserId(User user) {
        //This return Id or Uid
        return user.getId();
    }

    protected String getUserName(User user) {
        //This return name or userName
        return user.getName();
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
        result.setResponseData("start and end are invalidate!");
        return result;
    }

    protected InterfaceResult checkKeyword(String keyword)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        result.setResponseData("keyword invalidate!");
        return result;
    }

    protected MappingJacksonValue mappingJacksonValue(InterfaceResult result)
    {
        return new MappingJacksonValue(result);
    }

    protected MappingJacksonValue mappingJacksonValue(CommonResultCode resultCode)
    {
        return new MappingJacksonValue(InterfaceResult.getInterfaceResultInstance(resultCode));
    }

    protected MappingJacksonValue mappingJacksonValue(CommonResultCode resultCode,String message)
    {
        return new MappingJacksonValue(InterfaceResult.getInterfaceResultInstance(resultCode,message));
    }

    protected MappingJacksonValue knowledgeDetail(DataCollection data)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        result.setResponseData(data);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setFilters(KnowledgeUtil.assoFilterProvider(Associate.class.getName()));
        return jacksonValue;
    }
}