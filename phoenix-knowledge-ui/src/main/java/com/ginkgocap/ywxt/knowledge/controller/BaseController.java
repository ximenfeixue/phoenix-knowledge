package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.dynamic.model.DynamicNews;
import com.ginkgocap.ywxt.knowledge.model.BusinessTrackLog;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.common.IdTypeUid;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.utils.CommonUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.Encodes;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.cache.redis.RedisCacheService;
import com.gintong.frame.util.UserUtil;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.ywxt.im.model.MessageNotify;
import com.gintong.ywxt.im.model.ResourceMessage;
import org.apache.commons.lang.StringUtils;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnSelfService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public abstract class BaseController {

    @Resource
    private RedisCacheService redisCacheService;

    @Autowired
    protected KnowledgeCountService knowledgeCountService;

    protected final static short KNOWLEDGE_CREATE = 1;
    protected final static short KNOWLEDGE_COLLECT = 2;
    protected final static short KNOWLEDGE_SHARE = 3;
    protected final static short KNOWLEDGE_ALL = -1;

    private static final int sessionExpiredTime = 60 * 60 * 24 * 7;

    /**
     * 从body中获得参数
     * @param request
     * @return
     * @throws Exception
     */
    protected String getBodyParam(HttpServletRequest request) {
        StringBuffer jsonIn = new StringBuffer();
        try {
        	BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonIn.append(line);
            }
        } catch (IOException e) {
            logger().error("read request body failed : "+e.getMessage());
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
        final String key = UserUtil.getUserSessionKey(request);
        User user = (User) redisCacheService.getRedisCacheByKey(key);
        if (user != null) {
            logger().info("login userId: " + user.getId() + " userName: " + user.getName());
            redisCacheService.expireRedisCacheByKey(key, sessionExpiredTime);
        }
        return user;
    }

    protected long getUserId(User user) {
        //This return Id or Uid
        return user.getId();
    }

    protected boolean isAdmin(HttpServletRequest request) {
        User user = this.getUser(request);
        boolean isAdmin = (user != null && user.getId() == 1);
        logger().info("admin permission verify " + (isAdmin ? "success" : "failed."));
        return isAdmin;
    }

    protected boolean isGinTong(HttpServletRequest request) {
        final String jtUserValue = request.getHeader(KnowledgeConstant.GIN_USER_KEY);
        if (KnowledgeConstant.GIN_USER_VALUE.equals(jtUserValue)) {
            return true;
        }
        return false;
    }

    protected String getJsonIn(HttpServletRequest request) throws IOException {
        String requestJson=(String)request.getAttribute("requestJson");
        if(requestJson==null){
            return "";
        }
        return requestJson;
    }

    protected boolean isWeb(HttpServletRequest request) {
        return CommonUtil.isWeb(request);
    }

    protected User getJTNUser(HttpServletRequest request) {
        User user = getUser(request);
        if(null == user){
            return JTNUser();
        }
        return user;
    }

    protected User JTNUser() {
        User user = new User();
        user.setId(KnowledgeConstant.UserType.jinTN.v());//金桐脑
        user.setName(KnowledgeConstant.UserType.jinTN.c());
        user.setUserName(KnowledgeConstant.UserType.jinTN.c());
        return user;
    }

    protected String [] getChildIdListByColumnId(ColumnSelfService columnSelfService, int columnId, long userId)
    {
        List<ColumnSelf> columnList = null;
        try {
            columnList = columnSelfService.queryListByPidAndUserId((long) columnId, userId);
        } catch (Exception ex) {
            logger().error("invoke queryListByPidAndUserId failed: error: {}", ex.getMessage());
        }
        return getColumnIds(columnList, columnId);
    }

    protected String [] getColumnIds(List<ColumnSelf> columnList,int columnId)
    {
        String [] idList = null;
        if (columnList != null && columnList.size() > 0) {
            idList =  new String[columnList.size()];
            int index = 0;
            for (ColumnSelf column : columnList) {
                idList[index++] = column.getId().toString();
            }
        }
        if (idList == null) {
            idList = new String[1];
            idList[0]= String.valueOf(columnId);
        }

        return idList;
    }

    protected InterfaceResult checkColumn(int column)
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

    protected MappingJacksonValue knowledgeDetail(DataCollect data)
    {
        InterfaceResult result = InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        result.setResponseData(data);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setFilters(KnowledgeUtil.assoFilterProvider(Associate.class.getName()));
        return jacksonValue;
    }


    protected void setSessionAndErr(HttpServletRequest request, HttpServletResponse response, String errCode, String errMessage) {
        User user = this.getUser(request);
        if (user != null) {
            /*
            // user = userService.selectByPrimaryKey(user.getId());
            JTMember jtmember = (JTMember) cache.getByRedis(sessionId);
            redisCacheService.setByRedis("user" + sessionId, user, 60 * 60 * 24 * 3);// 设定过期日期为1天
            // 如需修改可滞后
            cache.setByRedis(sessionId, jtmember, 60 * 60 * 24 * 3);// 设定过期日期为1天
            // 如需修改可滞后
            cache.setByRedis("mobileLoginUser" + user.getId() + "SessionId", sessionId, 60 * 60 * 24 * 3);// 设定国企日期为1天
            // 如需修改可滞后
            response.setHeader("sessionID", sessionId);*/
        }
        response.setHeader("errorCode", errCode);
        response.setHeader("errorMessage", Encodes.encodeBase64(errMessage.getBytes()));
    }

    protected InterfaceResult errorResult(CommonResultCode code, String message)
    {
        return InterfaceResult.getInterfaceResultInstance(code, message);
    }

    protected InterfaceResult queryKnowledgeEnd()
    {
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS,"到达最后一页，知识已经取完。");
    }

    protected InterfaceResult queryRunning()
    {
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS,"正在努力查询，或无符合条件的知识，请稍后再试。");
    }

    protected boolean Success(InterfaceResult result)
    {
        return (result != null && result.getNotification() != null &&
                CommonResultCode.SUCCESS.getCode().equals(result.getNotification().getNotifCode()));
    }

    protected boolean Failed(InterfaceResult result)
    {
        return (result == null || result.getNotification() == null ||
                !CommonResultCode.SUCCESS.getCode().equals(result.getNotification().getNotifCode()));
    }

    protected boolean FailedGetKnowledge(InterfaceResult<DataCollect> result, MappingJacksonValue jacksonValue, final long knowledgeId, final int type)
    {
        if (Failed(result)) {
            logger().error("Query knowledge detail failed. knowledgeId: " + knowledgeId + " type: " + type);
            return true;
        }

        DataCollect data = result.getResponseData();
        if (data == null || data.getKnowledgeDetail() == null) {
            logger().error("Query knowledge detail failed: knowledgeId: " + knowledgeId + " type: " + type);
            jacksonValue.setValue(CommonResultCode.PARAMS_EXCEPTION);
            return true;
        }
        return false;
    }

    protected void convertKnowledgeContent(Knowledge knowledge, String content, List<String> listImageUrl, List<String> listImageUrl2, String orgUrl, boolean isWeb)
    {
        if (StringUtils.isEmpty(content)) {
            logger().error("Knowledge Content is null, please check the knowledge..");
            return;
        }
        knowledge.setContent(content);
        // 区分APP和WEB
        /*
        if (isWeb) {
            knowledge.setContent(content);
        } else {
            String htmlContent = Utils.txt2Html(content, listImageUrl, listImageUrl2, orgUrl);
            // htmlContent =
            // StringEscapeUtils.escapeHtml4(htmlContent);//入库的时候转换特殊字符
            knowledge.setContent(htmlContent);
        }*/
    }

    abstract Logger logger();
}