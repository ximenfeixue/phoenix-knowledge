package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.task.BigDataSyncTask;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chen Peifeng on 2016/5/23.
 */
@Controller
@RequestMapping("/knowledgeOther")
public class KnowledgeOtherControl extends BaseKnowledgeController
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeOtherControl.class);

    private final String knowledgeSyncTaskKey = "knowledgeSync";

    private static final Map<String, Boolean> syncTaskMap = new ConcurrentHashMap<String, Boolean>();

    private static final String knowledgeCreateUrl;
    private static final boolean createNew;
    static {
        ResourceBundle resource = ResourceBundle.getBundle("application");
        knowledgeCreateUrl = resource.getString("knowledge.url.create");
        createNew = Boolean.valueOf(resource.getString("knowledge.url.create.new"));
    }

    @ResponseBody
    @RequestMapping(value = "/fetchKnowledgeByUrl", method = RequestMethod.POST)
    public InterfaceResult fetchKnowledgeByUrl(HttpServletRequest request, HttpServletResponse response) {
        return generateKnowledgeByUrl(request, response, false);
    }


    @ResponseBody
    @RequestMapping(value = "/fetchExternalKnowledgeUrl", method = RequestMethod.POST)
    public InterfaceResult fetchExternalKnowledgeUrl(HttpServletRequest request, HttpServletResponse response) {

        return generateKnowledgeByUrl(request, response, true);
    }

    @ResponseBody
    @RequestMapping(value="/shareCount/{knowledgeId}/{type}", method = RequestMethod.GET)
    public InterfaceResult shareCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable short type,@PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);

        logger.debug("shareCount request, userId: " + userId + " type: " + type +", knowledgeId: " + knowledgeId);
        knowledgeCountService.updateShareCount(knowledgeId, type);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/collectCount/{knowledgeId}/{type}", method = RequestMethod.GET)
    public InterfaceResult collectCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable short type,@PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);

        logger.debug("collectCount request, userId: " + userId + " type: " + type +", knowledgeId: " + knowledgeId);
        knowledgeCountService.updateCollectCount(knowledgeId, type);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    /*
    @ResponseBody
    @RequestMapping(value="/update/{userId}/{password}", method = RequestMethod.GET)
    public InterfaceResult updatePassword(HttpServletRequest request,HttpServletResponse response,
            @PathVariable long userId,@PathVariable String password) throws Exception {
        logger.info("update userId: {} password: {}", userId, password);
        permissionServiceLocal.updatePassword(userId, password);
        logger.info("update userId: {} password: {}", userId, password);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }*/

    @ResponseBody
    @RequestMapping(value="/knowledgeSync", method = RequestMethod.GET)
    public InterfaceResult knowledgeSync(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        boolean started = syncTaskMap.get(knowledgeSyncTaskKey) == null ? false : syncTaskMap.get(knowledgeSyncTaskKey);
        if (started) {
            logger.info("Knowledge base sync task have started...");
        } else {
            syncTaskMap.put(knowledgeSyncTaskKey, true);
            new Thread(new KnowledgeSyncTask(0, 20)).start();
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/tagCleanup", method = RequestMethod.GET)
    public InterfaceResult updatePassword(HttpServletRequest request,HttpServletResponse response) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        tagServiceLocal.tagCleanUp(this.getUserId(user));
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    private InterfaceResult generateKnowledgeByUrl(HttpServletRequest request, HttpServletResponse response, boolean old) {
        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        long userId = user.getId();
        Map<String, Object> responseDataMap = new HashMap<String, Object>();
        final String knowKey = old ? "knowledge2" : "knowledge";
        Knowledge knowledge = null;
        String requestJson = "";
        try {
            logger.info("begin generateKnowledgeByUrl");
            requestJson = getBodyParam(request);
            if (StringUtils.isNotEmpty(requestJson)) {
                JsonNode jsonNode = KnowledgeUtil.readTree(requestJson);
                if (jsonNode == null) {
                    logger.error("Json content is not json.");
                    return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,
                            "the request content format is not json, please check again.");
                }
                // 移动端传入url地址
                String externalUrl = jsonNode.get("externalUrl").textValue();
                if (StringUtils.isEmpty(externalUrl)) {
                    logger.error("the externalUrl is null, please check again.");
                    return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,
                            "the externalUrl is null, please check again.");
                }
                // 原始链接
                String srcExternalUrl = externalUrl;
                logger.info("srcExternalUrl: " + srcExternalUrl);

                // 反馈数据
                String bigDataResponse = "";
                // 反馈json对象
                JsonNode dataJson = null;

                // 反馈内容
                String content = "";
                // 反馈标题
                String title = "";
                // 解析异常处理机制容忍限制
                for (int x = 0; x < 3; x++) {
                    final long begin = System.currentTimeMillis();
                    if (createNew) {
                        bigDataResponse = fastFetchKnowledge(srcExternalUrl);
                        logger.info("new fetch Knowledge cost: " + (System.currentTimeMillis() - begin));
                    } else {
                        bigDataResponse = externalKnowledge(jsonNode.toString());
                        logger.info("old fetch Knowledge cost: " + (System.currentTimeMillis() - begin));
                    }
                    if (StringUtils.isNotEmpty(bigDataResponse)) {
                        JsonNode responseJson = KnowledgeUtil.readTree(bigDataResponse);
                        if (responseJson == null) {
                            logger.error("Return json content is invalidated..");
                            continue;
                        }
                        dataJson = responseJson.get("responseData");
                        if (dataJson == null) {
                            logger.error("no responseData in return json content..");
                            continue;
                        }
                        content = dataJson.get("content").textValue();
                        title = dataJson.get("title").textValue();
                        if (StringUtils.isNotEmpty(content) && StringUtils.isNotEmpty(title)) {
                            break;
                        }
                    }
                }

                if (StringUtils.isBlank(content) || StringUtils.isBlank(title) ||
                        StringUtils.isBlank(HtmlToText.html2Text(content))) {
                    return errorResult(CommonResultCode.PARAMS_EXCEPTION, "请输入合法地址,请确保为新闻格式网址");
                }

                //logger.warn(bigDataResponse);
                if (bigDataResponse.indexOf("<!DOCTYPE html>") == -1 && bigDataResponse.startsWith("{")) {
                    String time = dataJson.get("publish_time").textValue();

                    // 测试条件
                    // content = getSping(content);
                    // title = getSping(title);
                    /** 以下条件，则大数据异常 */

                    //content.replace("\\", "");
                    knowledge = new Knowledge();

                    // 指定来源
                    final boolean isWeb = isWeb(request);
                    knowledge.setS_addr(srcExternalUrl);

                    final String columnId = String.valueOf(KnowledgeType.ENews.value());
                    knowledge.setColumnType(columnId);
                    knowledge.setColumnid(columnId);
                    // 带样式标签内容
                    knowledge.setContent(content);
                    knowledge.setTitle(title);
                    final long createTime = KnowledgeUtil.parserTimeToLong(time);
                    knowledge.setCreatetime(String.valueOf(createTime));

                    @SuppressWarnings("unchecked")
                    List<String> imgs = null;
                    JsonNode imgNodes = dataJson.findValue("imgs");
                    if (imgNodes != null) {
                        String imgContent = imgNodes.toString();
                        if (StringUtils.isNotEmpty(imgContent)) {
                            imgs = KnowledgeUtil.readListValue(String.class, imgContent);
                        }
                    }

                    knowledge.setMultiUrls(imgs);
                    // 附件ID
                    knowledge.setTaskid(null);

                    // 设置默认标签
                    knowledge.setTags(null);
                    // 判断标示
                    JsonNode isCreate = jsonNode.get("isCreate");
                    if (isCreate != null && isCreate.asBoolean()) {
                        // 创建者ID
                        knowledge.setCid(userId);
                        // 创建人姓名
                        knowledge.setCname(user.getName());
                        // 用户ID
                        //knowledge.setUid();
                        // 用户名
                        //knowledge.setUname();
                        // 创建改知识
                        DataCollect data = createKnowledge(knowledge, userId, isWeb);
                        // 敏感字检测
                        if (data == null || data.getKnowledgeDetail() == null) {
                            // 弹窗提示
                            setSessionAndErr(request, response, "-1", "创建知识失败");
                            // 错误反馈
                            responseDataMap.put(knowKey, null);
                            // 跳出
                            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,responseDataMap);
                        }

                        BigData bigData = data.toBigData();
                        bigDataSyncTask.addToMessageQueue(BigDataSyncTask.KNOWLEDGE_INSERT, bigData);
                        logger.info("createKnowledge by url success, knowledgeId: " + bigData.getKid());

                    } else {
                        logger.warn("isCreate is not existing or false, so skip to create knowledge!");
                    }

                } else {
                    return errorResult(CommonResultCode.PARAMS_EXCEPTION, "请输入合法地址,请确保为新闻格式网址");
                }
            }
        } catch (Exception ex) {
            setSessionAndErr(request, response, "-1", "未知异常");
            logger.error("Generate knowledge failed: error: " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "未知异常");
        }
        responseDataMap.put(knowKey, KnowledgeExt.cloneExt(knowledge));
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    @SuppressWarnings("static-access")
    private static String externalKnowledge(final String jsonContent) {
        // 配置大数据地址
        logger.info("url: " + knowledgeCreateUrl);
        if (StringUtils.isEmpty(knowledgeCreateUrl) || StringUtils.isEmpty(jsonContent)) {
            return null;
        }
        String result = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.postForObject(knowledgeCreateUrl, jsonContent.replace("externalUrl", "url").getBytes("utf-8"), String.class);
        } catch (IOException e) {
            logger.warn("fetchExternalKnowledgeUrl : " + e.getMessage());
            result = "";
        } catch (Exception e) {
            logger.warn("fetchExternalKnowledgeUrl : " + e.getMessage());
            result = "";
        }
        return result;
    }

    private static String fastFetchKnowledge(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }

        String result = "";
        try {
            url = knowledgeCreateUrl + "?url=" + url;
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            logger.warn("fetchExternalKnowledgeUrl : " + e.getMessage());
            result = "";
        }
        return result;
    }

    /**
     * 创建知识抽离
     * */
    private DataCollect createKnowledge(Knowledge knowledge, final long userId, boolean isWeb)
    {
        //default is privated
        final int privated = userId <= 0 ? 0 : 1;
        knowledge.setPrivated((short)privated);
        Knowledge savedDetail = null;
        try {
            savedDetail = this.knowledgeService.insert(knowledge);
        } catch (Exception ex) {
            logger.error("save knowledgd detail failed. error: " + ex.getMessage());
            return null;
        }

        Permission permission = null;
        if (savedDetail != null && savedDetail.getId() > 0) {
            final long knowledgeId = savedDetail.getId();
            try {
                permission = DataCollect.defaultPermission(userId, knowledgeId);
                permissionServiceLocal.savePermissionInfo(permission);
            } catch (Exception ex) {
                logger.error("Save permission failed. userId: " + knowledge.getCid() + " knowledgeId: " + knowledgeId);
            }
        }

        DataCollect dataCollect = new DataCollect();
        dataCollect.setKnowledgeDetail(savedDetail);
        dataCollect.setPermission(permission);

        return dataCollect;
    }

    private class KnowledgeSyncTask implements Runnable {
        private int start;
        private int size;
        public KnowledgeSyncTask(int start, int size) {
            this.start = start;
            this.size  = size;
        }

        public void run() {
            while (true) {
                List<KnowledgeBaseSync> baseSyncList = null;
                logger.info("get backup knowledge base, start: " + start + ", size: " + size);
                try {
                    baseSyncList = knowledgeService.getBackupKnowledgeBase(start, size);
                }
                catch (Throwable e) {
                    logger.error("get knowledge sync base list failed. error: " + e.getMessage());
                }
                if (CollectionUtils.isEmpty(baseSyncList)) {
                    logger.info("reset knowledge base sync");
                    syncTaskMap.put(knowledgeSyncTaskKey, false);
                    break;
                }
                else {
                    logger.info("got backup knowledge base size: " + baseSyncList.size());
                    for (KnowledgeBaseSync baseSync : baseSyncList) {
                        if (baseSync == null) {
                            logger.error("Knowledge to sync is null.");
                            continue;
                        }
                        try {
                            knowledgeService.syncKnowledgeBase(baseSync);
                            Thread.sleep(1000);
                        }
                        catch (Throwable e) {
                            logger.error("sync knowledge failed. knowledgeId: " + baseSync.getId() + ", type: " + baseSync.getType());
                        }

                    }
                }
            }
        }
    }

    public Logger logger() { return this.logger; }
}
