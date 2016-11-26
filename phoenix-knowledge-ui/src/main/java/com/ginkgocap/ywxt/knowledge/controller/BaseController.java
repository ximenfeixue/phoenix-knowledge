package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.CommonUtil;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeConstant;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.Encodes;
import com.gintong.frame.cache.redis.RedisCacheService;
import com.gintong.frame.util.UserUtil;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnSelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public abstract class BaseController {

    private final Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Resource
    private RedisCacheService redisCacheService;

    protected static final long APPID = KnowledgeConstant.DEFAULT_APP_ID;

    protected final static short KNOWLEDGE_MYCOLLECT = 1;
    protected final static short KNOWLEDGE_SHAREME = 2;
    protected final static short KNOWLEDGE_MYCREATE = 3;
    protected final static short KNOWLEDGE_ALL = -1;
	
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
            logger.info("login userId: " + user.getId() + " userName: " + user.getName());
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


    protected String getJsonIn(HttpServletRequest request) throws IOException {
        String requestJson=(String)request.getAttribute("requestJson");
        if(requestJson==null){
            return "";
        }
        return requestJson;
    }

    protected boolean isWeb(HttpServletRequest request)
    {
        return CommonUtil.isWeb(request);
    }

    protected User getJTNUser(HttpServletRequest request) throws Exception {
        User user = getUser(request);

        if(null == user){
            user = new User();
            user.setId(0);//金桐脑
            user.setName("金桐脑");
            user.setUserName("金桐脑");
            return user;
        }
        return user;
    }

    protected String [] getChildIdListByColumnId(ColumnSelfService columnSelfService, int columnId, long userId)
    {
        List<ColumnSelf> columnList = null;
        try {
            columnList = columnSelfService.queryListByPidAndUserId((long) columnId, userId);
        } catch (Exception ex) {
            logger.error("invoke queryListByPidAndUserId failed: error: {}", ex.getMessage());
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
                CommonResultCode.SUCCESS.equals(result.getNotification().getNotifCode()));
    }

    protected boolean Failed(InterfaceResult result)
    {
        return (result == null || result.getNotification() == null ||
                !CommonResultCode.SUCCESS.equals(result.getNotification().getNotifCode()));
    }

    protected void convertKnowledgeContent(Knowledge knowledge, String content, List<String> listImageUrl, String[] listImageUrl2, String orgUrl, boolean isWeb)
    {
        if (StringUtils.isEmpty(content)) {
            logger.error("Knowledge Content is null, please check the knowledge..");
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
}