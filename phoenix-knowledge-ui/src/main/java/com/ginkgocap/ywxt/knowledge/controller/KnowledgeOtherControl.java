package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.knowledge.utils.HtmlToText;
import com.ginkgocap.ywxt.knowledge.utils.Utils;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Chen Peifeng on 2016/5/23.
 */
@Controller
@RequestMapping("/knowledgeOther")
public class KnowledgeOtherControl extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeOtherControl.class);

    @Autowired
    KnowledgeService knowledgeService;

    private final short DEFAULT_KNOWLEDGE_TYPE = 1;

    @RequestMapping(value = "/fetchExternalKnowledgeUrl", method = RequestMethod.POST)
    public @ResponseBody
    InterfaceResult FetchExternalKnowledgeUrl(HttpServletRequest request, HttpServletResponse response) {

        logger.info("into /FetchExternalKnowledgeUrl");
        Map<String, Object> responseDataMap = new HashMap<String, Object>();
        Knowledge2 knowledge = null;

        User user = getUser(request);
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
                        content = dataJson.get("content").toString();
                        title = dataJson.get("title").toString();
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
                    String time = dataJson.get("publish_time").toString();

                    // 测试条件
                    // content = getSping(content);
                    // title = getSping(title);
                    /** 以下条件，则大数据异常 */
                    if (StringUtils.isNotBlank(content) || StringUtils.isNotBlank(title)) {
                        content.replace("\\", "");
                        knowledge = new Knowledge2();

                        // 指定来源
                        knowledge.setS_addr(srcExternalUrl);
                        /*
                        String srcExternUrl = knowledge.getS_addr();
                        if (srcExternUrl.length() > 250) {
                            knowledge.setS_addr(srcExternUrl.substring(0, 250));
                        }*/

                        knowledge.setColumnId(DEFAULT_KNOWLEDGE_TYPE);
                        // 带样式标签内容
                        knowledge.setContent(content);
                        knowledge.setTitle(title);
                        knowledge.setCreateTime(System.currentTimeMillis());

                        @SuppressWarnings("unchecked")
                        List<String> imgs = null;
                        List<JsonNode> imgNodes = dataJson.findValues("imgs");
                        if (imgNodes != null && imgNodes.size() > 0) {
                            int size = imgNodes.size();
                            imgs = new ArrayList<String>(size);
                            for (JsonNode imgNode : imgNodes) {
                                imgs.add(imgNode.toString());
                            }
                        }
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
                                    knowledge.setPic(tempPic.get((int) (tempPic.size() / 2)));
                            }
                        }
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
                                return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
                            }

                            logger.info("/fetchExternalKnowledgeUrl.json createKnowledge result: {}", createResult);
                            Long kId = (Long) createResult.getResponseData();
                            // 知识ID
                            knowledge.setId(kId);
                        } else {
                            logger.warn("isCreate is not existing, so skip to create knowledge!");
                        }
                    } else {
                        // 大数据错误
                        setSessionAndErr(request, response, "-1", "请输入合法地址,请确保为新闻格式网址");
                        // 错误反馈
                        responseDataMap.put("knowledge2", null);
                        // 跳出
                        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
                    }
                } else {
                    // 大数据错误
                    setSessionAndErr(request, response, "-1", "请输入合法地址,请确保为新闻格式网址");
                    // 错误反馈
                    responseDataMap.put("knowledge2", null);
                    // 跳出
                    return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
                }
            }
        } catch (IOException e) {
            setSessionAndErr(request, response, "-1", "输入参数不合法");
            e.printStackTrace();
        } catch (Exception q) {
            setSessionAndErr(request, response, "-1", "未知异常");
            logger.warn(q.toString());
            logger.warn(q.getMessage());
            logger.info(q.toString());
            logger.info(q.getMessage());
        }
        responseDataMap.put("knowledge", knowledge);
        return InterfaceResult.getSuccessInterfaceResultInstance(responseDataMap);
    }

    /*
    @ResponseBody
    @RequestMapping(value="/hot/{limit}", method = RequestMethod.GET)
    public InterfaceResult commentCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable int limit) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        List<KnowledgeCount> knowledgeCountList = knowledgeCountService.getHotKnowledge(limit);
        if (knowledgeCountList != null && knowledgeCountList.size() > 0)
        {
            return InterfaceResult.getSuccessInterfaceResultInstance(knowledgeCountList);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }*/

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

    private InterfaceResult createKnowledge(Knowledge2 knowledge2, User user, String url) {

        KnowledgeDetail detail = getKnowledgeNewsVO(knowledge2, user, url);
        detail.setColumnId(DEFAULT_KNOWLEDGE_TYPE);// 手机端创建的知识默认栏目类型和栏目id都是1
        //vo.setColumnType(DEFAULT_KNOWLEDGE_TYPE);// 设置默认类型为咨询，目前手机端只支持咨询
        // 调用平台层插入知识
        DataCollection data = new DataCollection(null, detail);
        data.generateKnowledge();
        data.serUserId(this.getUserId(user));
        return knowledgeService.insert(data);
    }


    private KnowledgeDetail getKnowledgeNewsVO(Knowledge2 knowledge2, User user, String url) {

        KnowledgeDetail vo = genVo(knowledge2, url);
        /*
        JSONObject jsonAsso = JSONObject.fromObject(vo.getAsso());

        vo.setAsso(jsonAsso.toString());*/

        logger.info("KnowledgeNewsVO :: " + vo);

        return vo;
    }

    private KnowledgeDetail genVo(Knowledge2 knowledge2, String url) {

        if (knowledge2.getType() == KnowledgeType.Law.value()) {
            KnowledgeLaw knowledge = new KnowledgeLaw();
            knowledge.setTitanic(knowledge2.getTitanic());
            knowledge.setPostUnit(knowledge2.getPostUnit());
            knowledge.setPublishTime(knowledge2.getSubmitTime());
            knowledge.setPerformTime(knowledge2.getPerformTime());
            setKnowledgeContent(knowledge2, knowledge, null, url);
            return knowledge;
        }
        if (knowledge2.getType() == KnowledgeType.Investment.value()) {
            KnowledgeInvestment knowledge = new KnowledgeInvestment();
            knowledge.setSynonyms(knowledge2.getSynonyms());
            setKnowledgeContent(knowledge2, knowledge, null, url);
            return knowledge;
        }
        KnowledgeDetail knowledge = new KnowledgeDetail();
        setKnowledgeContent(knowledge2, knowledge, null, url);
        return knowledge;
        /*
        if (knowledge2.getId() != 0) {
            vo.setKnowledgeid(knowledge2.getId() + "");
            vo.setkId(knowledge2.getId());
        }*/


        // 关联管理器
        /*
        vo.setAsso(EntityFactory.genAsso(knowledge2.getListRelatedConnectionsNode(), knowledge2.getListRelatedOrganizationNode(),
                knowledge2.getListRelatedKnowledgeNode(), knowledge2.getListRelatedAffairNode()));

        logger.info(vo.getAsso());
        // 权限管理器
        vo.setSelectedIds(EntityFactory.genSelectIds(knowledge2.getListHightPermission(), knowledge2.getListMiddlePermission(),
                knowledge2.getListLowPermission()));*/
    }

    public void checkGroup(long userid) {
        List<Long> idtype = new ArrayList<Long>();
        idtype.add(0l);
        idtype.add(1l);
        //userCategoryService.checkNogroup(userid, idtype);
    }

    private void setKnowledgeContent(Knowledge2 knowledge2, KnowledgeDetail detail, List<String> urls, String url)
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
        List<Long> directoryList = null;
        if (knowledge2.getListUserCategory() != null && knowledge2.getListUserCategory().size() > 0) {
            int size = knowledge2.getListUserCategory().size();
            directoryList = new ArrayList<Long>(size);
            for (int i = 0; i < size; i++) {
                UserCategory uc = knowledge2.getListUserCategory().get(i);
                if (uc.getId() > 0) {
                    directoryList.add(uc.getId());
                }
                else {
                    logger.error("An invalidated directoryId: {}", uc.getId());
                }
            }
            detail.setCategoryIds(directoryList);
        }

        detail.setEssence(knowledge2.getEssence());
        //detail.setShareMessage("");// 分享消息内容

        if (knowledge2.getColumnId() <= 0) {
            // 如果没指定栏目， 默认为资讯
            detail.setColumnId(DEFAULT_KNOWLEDGE_TYPE);
        }
    }

}