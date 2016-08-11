package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.service.PermissionServiceLocal;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.Utils;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Chen Peifeng on 2016/5/23.
 */
@Controller
@RequestMapping("/knowledgeOther")
public class KnowledgeOtherControl extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeOtherControl.class);

    @Autowired
    KnowledgeService knowledgeService;

    @Autowired
    KnowledgeCountService knowledgeCountService;

    @Autowired
    private PermissionServiceLocal permissionServiceLocal;

    private final short DEFAULT_KNOWLEDGE_TYPE = 1;

    @ResponseBody
    @RequestMapping(value = "/fetchExternalKnowledgeUrl", method = RequestMethod.POST)
    public InterfaceResult fetchExternalKnowledgeUrl(HttpServletRequest request, HttpServletResponse response) {

        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        logger.info("into /FetchExternalKnowledgeUrl");
        Map<String, Object> responseDataMap = new HashMap<String, Object>();
        Knowledge2 knowledge = null;
        String requestJson = "";
        try {
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
                logger.info("srcExternalUrl: {}", srcExternalUrl);
                externalUrl = URLEncoder.encode(externalUrl, "UTF-8");

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
                    bigDataResponse = externalException(url, jsonNode.toString());
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
                    // 大数据解析超时
                    setSessionAndErr(request, response, "-1", "请输入合法地址,请确保为新闻格式网址");
                    // 错误反馈
                    responseDataMap.put("knowledgeDetail", null);
                    // 跳出
                    return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
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
                        knowledge = new Knowledge2();

                        // 指定来源
                        knowledge.setKnowledgeUrl(srcExternalUrl);

                        knowledge.setType(DEFAULT_KNOWLEDGE_TYPE);
                        knowledge.setColumnId(DEFAULT_KNOWLEDGE_TYPE);
                        // 带样式标签内容
                        knowledge.setContent(content);
                        knowledge.setTitle(title);
                        long createTime = KnowledgeUtil.parserTimeToLong(time);
                        knowledge.setCreateTime(createTime);

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
                        knowledge.setTaskId(null);
                        // 摘除html底部图片
                        knowledge.setListImageUrl(null);

                        // 设置默认标签
                        knowledge.setTags(new ArrayList<Long>());
                        // 判断标示
                        JsonNode isCreate = jsonNode.get("isCreate");
                        if (isCreate != null && isCreate.asBoolean()) {
                            // 创建者ID
                            knowledge.setCid(user.getId());
                            // 创建人姓名
                            knowledge.setCname(user.getName());
                            // 用户ID
                            knowledge.setUid(user.getId());
                            // 用户名
                            knowledge.setUname(user.getName());
                            // 去除无用图片 - （仅针对移动端编辑时需要）
                            knowledge.setListImageUrl(null);
                            // 创建改知识
                            InterfaceResult createResult = createKnowledge(knowledge, user, srcExternalUrl);
                            // 敏感字检测
                            if (createResult != null && createResult.getNotification() != null &&
                                    createResult.getNotification().getNotifCode() != null &&
                                    !createResult.getNotification().getNotifCode().equals("0")) {
                                // 弹窗提示
                                setSessionAndErr(request, response, "-1", createResult.getNotification() + " ：" + createResult.getResponseData());
                                // 错误反馈
                                responseDataMap.put("knowledge2", null);
                                // 跳出
                                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,responseDataMap);
                            }

                            Long kId = (Long) createResult.getResponseData();
                            // 知识ID
                            knowledge.setId(kId);
                            logger.info("fetchExternalKnowledgeUrl createKnowledge knowledgeId: {}", kId);
                        } else {
                            logger.warn("isCreate is not existing or false, so skip to create knowledge!");
                        }
                    } else {
                        // 大数据错误
                        setSessionAndErr(request, response, "-1", "请输入合法地址,请确保为新闻格式网址");
                        // 错误反馈
                        responseDataMap.put("knowledge2", null);
                        // 跳出
                        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, responseDataMap);
                    }
                } else {
                    // 大数据错误
                    setSessionAndErr(request, response, "-1", "请输入合法地址,请确保为新闻格式网址");
                    // 错误反馈
                    responseDataMap.put("knowledge2", null);
                    // 跳出
                    return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, responseDataMap);
                }
            }
        } catch (IOException e) {
            setSessionAndErr(request, response, "-1", "输入参数不合法");
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "输入参数不合法");
        } catch (Exception q) {
            setSessionAndErr(request, response, "-1", "未知异常");
            logger.warn(q.toString());
            logger.warn(q.getMessage());
            logger.info(q.toString());
            logger.info(q.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "未知异常");
        }
        responseDataMap.put("knowledge2", knowledge);
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    @ResponseBody
    @RequestMapping(value="/shareCount/{type}/{knowledgeId}", method = RequestMethod.GET)
    public InterfaceResult shareCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable short type,@PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);

        logger.debug("shareCount request, userId: {} type: {}, knowledgeId: {}", userId, type, knowledgeId);
        knowledgeCountService.updateShareCount(userId, knowledgeId, type);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/collectCount/{type}/{knowledgeId}", method = RequestMethod.GET)
    public InterfaceResult collectCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable short type,@PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);

        logger.debug("collectCount request, userId: {} type: {}, knowledgeId: {}", userId, type, knowledgeId);
        knowledgeCountService.updateCollectCount(this.getUserId(user), knowledgeId, type);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }
    
    @ResponseBody
    @RequestMapping(value="/update/{userId}/{password}", method = RequestMethod.GET)
    public InterfaceResult updatePassword(HttpServletRequest request,HttpServletResponse response,
            @PathVariable long userId,@PathVariable String password) throws Exception {
        logger.info("update userId: {} password: {}", userId, password);
        permissionServiceLocal.updatePassword(userId, password);
        logger.info("update userId: {} password: {}", userId, password);
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @SuppressWarnings("static-access")
    private String externalException(String url, String jsonContent) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(jsonContent)) {
            return null;
        }
        String result = "";
        try {
            // result = (new HttpClientUtil()).getGintongPost(url,"",
            // json.toString().replace("externalUrl", "url"));
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
    private InterfaceResult createKnowledge(Knowledge2 knowledge, User user, String url) {

        Knowledge vo = getKnowledgeNewsVO(knowledge, url);
        vo.setCid(this.getUserId(user));
        vo.setCname(user.getName());
        vo.setColumnid(String.valueOf(KnowledgeType.ENews.value()));
        vo.setColumnType(String.valueOf(KnowledgeType.ENews.value()));// 设置默认类型为咨询，目前手机端只支持咨询
        // 调用平台层插入知识
        DataCollect data = new DataCollect(null, vo);
        data.generateKnowledge();
        return this.knowledgeService.insert(data);
    }


    private Knowledge getKnowledgeNewsVO(Knowledge2 knowledge, String url) {

        Knowledge vo = genVo(knowledge, url);
        /*
        JSONObject jsonAsso = JSONObject.fromObject(vo.getAsso());

        vo.setAsso(jsonAsso.toString());*/

        logger.info("Knowledge : " + vo);

        return vo;
    }

    private Knowledge genVo(Knowledge2 knowledge, String url) {

        Knowledge vo = new Knowledge();
        if (knowledge.getId() != 0) {
            vo.setId(knowledge.getId());
            vo.setKnowledgeMainId(knowledge.getId());
        }
        if (knowledge.getType() == KnowledgeType.ELaw.value()) {
            vo.setTitanic(knowledge.getTitanic());
            vo.setPostUnit(knowledge.getPostUnit());
            vo.setSubmitTime(knowledge.getSubmitTime());
            vo.setPerformTime(knowledge.getPerformTime());
        }
        if (knowledge.getType() == KnowledgeType.EInvestment.value()) {
            vo.setSynonyms(knowledge.getSynonyms());
        }
        vo.setTaskid(knowledge.getTaskId());
        vo.setTitle(knowledge.getTitle());
        try {
            List<String> listImageUrl = new ArrayList<String>();

            // 测试指定taskid的文件
            // knowledge.setTaskId("taskId-123456");

            // 根据taskid获取到附件列表
            /* this position we don't need it
            List<JTFile> listAttachFile = mJTFileService.getJTFileByTaskId(knowledge.getTaskId() + "");
            if (listAttachFile != null) {
                for (JTFile jt : listAttachFile) {
                    if (jt.getType() != null) {
                        if (jt.getType().equals(JTFile.TYPE_IMAGE)) {
                            // 如果是图片类型，则要合并到html里取
                            listImageUrl.add(jt.getUrl());
                        }
                    }
                }
            }*/
            // 区分APP和WEB
            if (knowledge.getWeb() > 0) {
                vo.setContent(knowledge.getContent());
            } else {
                String htmlContent = Utils.txt2Html(knowledge.getContent(), listImageUrl, knowledge.getListImageUrl(), url);
                // htmlContent =
                // StringEscapeUtils.escapeHtml4(htmlContent);//入库的时候转换特殊字符
                vo.setContent(htmlContent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        vo.setPic(knowledge.getPic());

        String contentDes;
        if ((knowledge.getContent() != null) && (knowledge.getContent().length() > 0)) {
            String content = knowledge.getContent();
            contentDes = filterHtml(content).length() < 40 ? filterHtml(content) : filterHtml(content).substring(0, 35) + "...";

        } else {
            contentDes = " ";
        }
        vo.setDesc(contentDes);

        if (knowledge.getTags() != null && knowledge.getTags().size() > 0) {
            String tags = knowledge.getTags().toString();
            vo.setTags(tags.substring(1, tags.length()-1));
        }

        vo.setSource(knowledge.getSource());
        // 知识所在目录
        /*
        String strUc = "";
        if (knowledge.getListUserCategory() != null) {
            for (int i = 0; i < knowledge.getListUserCategory().size(); i++) {
                UserCategory uc = knowledge.getListUserCategory().get(i);
                strUc += uc.getId();
                if (i < (knowledge.getListUserCategory().size() - 1))
                    strUc += ",";
            }
            vo.setDirectorys(strUc);
        }*/

        vo.setEssence(knowledge.getEssence());
        //vo.setShareMessage("");// 分享消息内容

        vo.setColumnid(String.valueOf(knowledge.getColumnId()));
        vo.setColumnType(String.valueOf(KnowledgeType.ENews.value()));
        vo.setCpathid(knowledge.getCpathid());
        vo.setCpathid(KnowledgeType.ENews.typeName());

        // 关联管理器
        /*
        vo.setAsso(EntityFactory.genAsso(knowledge.getListRelatedConnectionsNode(), knowledge.getListRelatedOrganizationNode(),
                knowledge.getListRelatedKnowledgeNode(), knowledge.getListRelatedAffairNode()));

        logger.info(vo.getAsso());
        // 权限管理器
        vo.setSelectedIds(EntityFactory.genSelectIds(knowledge.getListHightPermission(), knowledge.getListMiddlePermission(),
                knowledge.getListLowPermission()));*/

        return vo;
    }

    public void checkGroup(long userid) {
        List<Long> idtype = new ArrayList<Long>();
        idtype.add(0l);
        idtype.add(1l);
        //userCategoryService.checkNogroup(userid, idtype);
    }

    private void setKnowledgeContent(Knowledge2 knowledge2, Knowledge detail, List<String> urls, String url)
    {
        if (knowledge2.getWeb() > 0) {
            detail.setContent(knowledge2.getContent());
        } else {
            String htmlContent = Utils.txt2Html(knowledge2.getContent(), urls, knowledge2.getListImageUrl(), url);
            // htmlContent =
            // StringEscapeUtils.escapeHtml4(htmlContent);//入库的时候转换特殊字符
            detail.setContent(htmlContent);
        }
        String contentDes = null;
        if ((knowledge2.getContent() != null) && (knowledge2.getContent().length() > 0)) {
            String content = knowledge2.getContent();
            String filterContent = HtmlToText.filterHtml(content);
            if (filterContent != null) {
                contentDes =  filterContent.length() < 40 ? filterContent : filterContent.substring(0, 35) + "...";
            }

        }
        detail.setDesc(contentDes);
        detail.setPic(knowledge2.getPic());
        detail.setTags(detail.getTags());
        detail.setSource(knowledge2.getSource());

        // 知识所在目录
        /*
        List<Long> directoryList = null;
        if (knowledge2.getListUserCategory() != null && knowledge2.getListUserCategory().size() > 0) {
            int size = knowledge2.getListUserCategory().size();
            directoryList = new ArrayList<Long>(size);
            for (int i = 0; i < size; i++) {
                UserCategory uc = knowledge2.getListUserCategory().get(i);
                if (uc.getId() > 0) {
                    directoryList.add(uc.getId());
                } else {
                    logger.error("An invalidated directoryId: {}", uc.getId());
                }
            }
            detail.setDirectorys(directoryList);
        }*/

        detail.setEssence(knowledge2.getEssence());
        //detail.setShareMessage("");// 分享消息内容

        if (knowledge2.getColumnId() <= 0) {
            // 如果没指定栏目， 默认为资讯
            detail.setColumnid(String.valueOf(DEFAULT_KNOWLEDGE_TYPE));
        }
    }

    private void setCoverPic(List<String> imgs,Knowledge2 knowledge)
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

    public String filterHtml(String html) {
        Pattern pattern = Pattern.compile("<style[^>]*?>[\\D\\d]*?<\\/style>");
        Matcher matcher = pattern.matcher(html);
        String htmlStr = matcher.replaceAll("");
        pattern = Pattern.compile("<[^>]+>");
        String filterStr = pattern.matcher(htmlStr).replaceAll("");
        filterStr.replace("&nbsp;", " ");
        return filterStr;
    }

}
