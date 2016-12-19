package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.PermissionServiceLocal;
import com.ginkgocap.ywxt.knowledge.service.TagServiceLocal;
import com.ginkgocap.ywxt.knowledge.task.BigDataSyncTask;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.Utils;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chen Peifeng on 2016/5/23.
 */
@Controller
@RequestMapping("/knowledgeOther")
public class KnowledgeOtherControl extends BaseController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeOtherControl.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private KnowledgeCountService knowledgeCountService;

    @Autowired
    private TagServiceLocal tagServiceLocal;

    @Autowired
    private PermissionServiceLocal permissionServiceLocal;

    @Autowired
    private BigDataSyncTask bigDataSyncTask;

    private final short DEFAULT_KNOWLEDGE_TYPE = 1;
    private final String knowledgeSyncTaskKey = "knowledgeSync";

    private static final Map<String, Boolean> syncTaskMap = new ConcurrentHashMap<String, Boolean>();

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
        knowledgeCountService.updateShareCount(userId, knowledgeId, type);
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
        knowledgeCountService.updateCollectCount(this.getUserId(user), knowledgeId, type);
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
        if (started)
        {
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

                // 配置大数据地址
                ResourceBundle resource = ResourceBundle.getBundle("application");
                String url = resource.getString("knowledge.url.create");
                logger.warn(url);

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
                    bigDataResponse = externalKnowledge(url, jsonNode.toString());
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

                if (StringUtils.isBlank(bigDataResponse) || StringUtils.isBlank(title) ||
                        StringUtils.isBlank(HtmlToText.html2Text(content))) {
                    return errorResult(CommonResultCode.PARAMS_EXCEPTION, "请输入合法地址,请确保为新闻格式网址");
                }

                logger.warn(bigDataResponse);
                if (bigDataResponse.indexOf("<!DOCTYPE html>") == -1 && bigDataResponse.startsWith("{")) {
                    String time = dataJson.get("publish_time").textValue();

                    // 测试条件
                    // content = getSping(content);
                    // title = getSping(title);
                    /** 以下条件，则大数据异常 */
                    if (StringUtils.isNotBlank(content) || StringUtils.isNotBlank(title)) {
                        //content.replace("\\", "");
                        knowledge = new Knowledge();

                        // 指定来源
                        boolean isWeb = isWeb(request);
                        knowledge.setS_addr(srcExternalUrl);

                        final String columnId = String.valueOf(KnowledgeType.ENews.value());
                        knowledge.setColumnType(columnId);
                        knowledge.setColumnid(columnId);
                        // 带样式标签内容
                        knowledge.setContent(content);
                        knowledge.setTitle(title);
                        long createTime = KnowledgeUtil.parserTimeToLong(time);
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
                        setCoverPic(imgs, knowledge);
                        // 附件ID
                        knowledge.setTaskid(null);
                        // 摘除html底部图片
                        knowledge.setMultiUrls(null);

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
                            // 去除无用图片 - （仅针对移动端编辑时需要）
                            knowledge.setMultiUrls(null);
                            // 创建改知识
                            DataCollect data = createKnowledge(knowledge, srcExternalUrl, isWeb);
                            // 敏感字检测
                            if (data == null || data.getKnowledgeDetail().getId() <= 0) {
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
    private String externalKnowledge(String url, String jsonContent) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(jsonContent)) {
            return null;
        }
        String result = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.postForObject(url, jsonContent.replace("externalUrl", "url").getBytes("utf-8"), String.class);
        } catch (IOException e) {
            logger.warn("fetchExternalKnowledgeUrl : " + e.getMessage());
            result = "";
        } catch (Exception e) {
            logger.warn("fetchExternalKnowledgeUrl : " + e.getMessage());
            result = "";
        }
        return result;
    }


    /**
     * 创建知识抽离
     * */
    private DataCollect createKnowledge(Knowledge knowledge, String url, boolean isWeb) {

        //Knowledge vo = getKnowledgeNewsVO(knowledge, url, isWeb);

        //knowledge.setCid(this.getUserId(user));
        //knowledge.setCname(user.getName());
        //String columnId = String.valueOf(KnowledgeType.ENews.value());
        //knowledge.setColumnid(columnId);
        //knowledge.setColumnType(columnId);// 设置默认类型为咨询，目前手机端只支持咨询
        knowledge.setS_addr(url);
        // 调用平台层插入知识
        DataCollect data = new DataCollect(null, knowledge);
        data.defaultPublicPermission();
        InterfaceResult result = this.knowledgeService.insert(data);
        if (result != null && result.getResponseData() != null && result.getResponseData() instanceof Long) {
            Long knowledgeId = (Long)result.getResponseData();
            if (knowledgeId != null && knowledgeId.longValue() > 0) {
                data.getKnowledgeDetail().setId(knowledgeId);
                try {
                    permissionServiceLocal.savePermissionInfo(data.defaultPublicPermission());
                } catch (Exception ex) {
                    logger.error("Save permission failed. userId: " + knowledge.getCid() + " knowledgeId: " + knowledgeId);
                }
            }
        }
        return data;
    }


    private Knowledge getKnowledgeNewsVO(Knowledge knowledge, String url, boolean isWeb) {

        Knowledge vo = genVo(knowledge, url, isWeb);
        /*
        JSONObject jsonAsso = JSONObject.fromObject(vo.getAsso());

        vo.setAsso(jsonAsso.toString());*/

        logger.info("Knowledge : " + vo);

        return vo;
    }

    private Knowledge genVo(final Knowledge knowledge,final String url, final boolean isWeb) {

        Knowledge vo = new Knowledge();
        if (knowledge.getId() != 0) {
            vo.setId(knowledge.getId());
            vo.setKnowledgeMainId(knowledge.getId());
        }
        final int columnType = KnowledgeUtil.parserColumnId(knowledge.getColumnType());
        if (columnType == KnowledgeType.ELaw.value()) {
            vo.setTitanic(knowledge.getTitanic());
            vo.setPostUnit(knowledge.getPostUnit());
            vo.setSubmitTime(knowledge.getSubmitTime());
            vo.setPerformTime(knowledge.getPerformTime());
        }
        if (columnType == KnowledgeType.EInvestment.value()) {
            vo.setSynonyms(knowledge.getSynonyms());
        }
        vo.setTaskid(knowledge.getTaskid());
        vo.setTitle(knowledge.getTitle());
        vo.setS_addr(knowledge.getS_addr());
        try {
            List<String> listImageUrl = new ArrayList<String>();
            // 区分APP和WEB
            convertKnowledgeContent(vo, knowledge.getContent(), listImageUrl, knowledge.getMultiUrls(), url, isWeb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        vo.setPic(knowledge.getPic());

        String contentDes;
        if ((knowledge.getContent() != null) && (knowledge.getContent().length() > 0)) {
            String content = knowledge.getContent();
            content = HtmlToText.htmlToText(content);
            contentDes = content.length() < 40 ? content : content.substring(0, 40) + "...";

        } else {
            contentDes = " ";
        }
        vo.setDesc(contentDes);

        if (StringUtils.isNotBlank(knowledge.getTags())) {
            String tags = knowledge.getTags();
            vo.setTags(tags.substring(1, tags.length()-1));
        }

        vo.setSource(knowledge.getSource());
        vo.setEssence(knowledge.getEssence());
        //vo.setShareMessage("");// 分享消息内容

        vo.setColumnid(knowledge.getColumnid());
        vo.setColumnType(String.valueOf(KnowledgeType.ENews.value()));
        vo.setCpathid(knowledge.getCpathid());
        vo.setCpathid(KnowledgeType.ENews.typeName());

        return vo;
    }

    private void setCoverPic(List<String> imgs,Knowledge knowledge)
    {
        /* old method
        if (imgs != null && imgs.size() > 0) {
            int tempSize = imgs.size();
            if (tempSize > 0) {
                String[] strs = new String[tempSize];
                // 临时存储
                List<String> tempPic = new ArrayList<String>(tempSize);
                for (int i = 0; i < tempSize; i++) {
                    String tempStr = imgs.get(i);
                    // 2015-1-29 start
                    if (tempStr.length() < 255) {
                        // 将符合标准长度的图片地址存放在list中,防止在插入数据库中时报错
                        tempPic.add(tempStr);
                    }
                    strs[i] = tempStr;
                    // 2015-1-29 end
                }
                knowledge.setListImageUrl(strs);
                if (tempPic.size() > 0)
                    // 设置封面图片
                    knowledge.setPic(tempPic.get(0));
            }
        }*/
        //New
        if (imgs != null && imgs.size() > 0) {
            for (String img : imgs) {
                if (StringUtils.isEmpty(img) && img.length() < 255) {
                    knowledge.setPic(img);
                    break;
                }
            }
            if (knowledge.getPic() == null || "null".equals(knowledge.getPic()) || knowledge.getPic().trim().length() <= 0) {
                knowledge.setPic(imgs.get(0));
            }
        }
    }

    private class KnowledgeSyncTask implements Runnable {
        private int start;
        private int size;
        public KnowledgeSyncTask(int start, int size) {
            this.start = start;
            this.size  = size;
        }

        @Override
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
                        if (baseSync != null) {
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
    }
}
