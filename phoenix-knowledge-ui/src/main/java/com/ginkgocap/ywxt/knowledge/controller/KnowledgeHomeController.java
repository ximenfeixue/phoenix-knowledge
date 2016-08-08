package com.ginkgocap.ywxt.knowledge.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.RecommendResult;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.utils.HttpClientHelper;
import com.ginkgocap.ywxt.knowledge.utils.PackingDataUtil;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import net.sf.json.JSONObject;
import org.parasol.column.entity.ColumnSelf;
import org.parasol.column.service.ColumnCustomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by gintong on 2016/7/23.
 */
@Controller
@RequestMapping("/webknowledge")
public class KnowledgeHomeController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeHomeController.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    KnowledgeBatchQueryService knowledgeBatchQueryService;

    @Autowired
    KnowledgeHomeService knowledgeHomeService;

    @Autowired
    KnowledgeOtherService knowledgeOtherService;

    @Autowired
    ColumnCustomService columnCustomService;

    @Autowired
    KnowledgeCountService knowledgeCountService;

    private final String  KNOWLEDGE_QUERY_HOT_URL = "knowledgeQueryHotUrl";
    private ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");

    @ResponseBody
    @RequestMapping(value = "/home/separate/{type}", method = RequestMethod.GET)
    public InterfaceResult separate(HttpServletRequest request, HttpServletResponse response,@PathVariable short type)
            throws IOException {
        User user = getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        Map<String, Object> model = new HashMap<String, Object>(2);
        List<Knowledge> knowledgeList = null;
        if (type == 4) {
            knowledgeList = knowledgeHomeService.selectIndexByParam(type, 1, 6);
            try {
                model.put("knowledgeRead", getReadCount(knowledgeList));
            } catch (Exception e) {
                logger.error("无法获取阅读个数");
                e.printStackTrace();
            }
        } else {
            knowledgeList = knowledgeHomeService.selectIndexByParam(type, 1, 6);
        }
        model.put("list", knowledgeList);

        logger.info(" call separate success...");
        return InterfaceResult.getSuccessInterfaceResultInstance(model);
    }
    @ResponseBody
    @RequestMapping(value = "/home/getHotTag/{count}", method = RequestMethod.GET)
    public InterfaceResult getHotTag(final HttpServletRequest request, final HttpServletResponse response,
                                     @PathVariable int count) throws Exception
    {
        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (count <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION,"请求参数不合法");
        }

        try {
            String url = (String) request.getSession().getServletContext().getAttribute("knowledgeQueryTagUrl");
            if (StringUtils.isEmpty(url)) {
                ResourceBundle resource = ResourceBundle.getBundle("application");
                url = resource.getString("knowledge.url.query.tag");
            }
            if (StringUtils.isEmpty(url) || url.indexOf("http") < 0) {
                url = "http://192.168.101.53:8090";
            }

            Map<String, String> params = new HashMap<String, String>(1);
            params.put("num", String.valueOf(count));
            String str = HttpClientHelper.post(url + "/user/tags/search.json", params);
            String tags = "";
            try {
                if (!org.apache.commons.lang.StringUtils.isBlank(str)) {
                    tags = str.replace("[", "").replace("]", "");
                }
            } catch (Exception e) {
                logger.error("搜索首页热门标签请求查询失败,{}", e.toString());
                e.printStackTrace();
            }
            Map<String, Object> model = new HashMap<String, Object>(1);
            model.put("list", tags);
            return InterfaceResult.getSuccessInterfaceResultInstance(model);
        } catch (Exception e) {
            logger.error("最热排行请求失败{}", e.toString());
            e.printStackTrace();
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    // 获取热点排行
    @ResponseBody
    @RequestMapping(value = "/home/getHotList/{type}")
    public InterfaceResult getHotList(HttpServletRequest request, HttpServletResponse response,@PathVariable int type)
            throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        Map<String, Object> model = new HashMap<String, Object>(1);
        try {
            String url = (String) request.getSession().getServletContext().getAttribute("knowledgeQueryHotUrl");
            if (StringUtils.isEmpty(url)) {
                ResourceBundle resource = ResourceBundle.getBundle("application");
                url = resource.getString("knowledge.url.query.hot");
            }
            if (StringUtils.isEmpty(url) || url.indexOf("http") < 0) {
                url = "http://192.168.101.53:8090";
            }
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", String.valueOf(type));
            String str = HttpClientHelper.post(url + "/knowledge/hot/all.json", params);
            if (StringUtils.isEmpty(str)) {
                InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"最热排行请求失败");
            }
            model.put("list", getKnowledgeList(str));
        } catch (Exception e) {
            logger.error("最热排行请求失败{}", e.toString());
            e.printStackTrace();
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(model);

    }

    // 获取评论排行
    @ResponseBody
    @RequestMapping(value = "/home/getCommentList/{type}")
    public InterfaceResult getCommentList(HttpServletRequest request, HttpServletResponse response,@PathVariable int type)
            throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);

        Map<String, Object> model = new HashMap<String, Object>(1);
        try {
            String url = (String) request.getSession().getServletContext().getAttribute("knowledgeQueryCommentUrl");
            if (StringUtils.isEmpty(url) || url.indexOf("http") < 0) {
                url = "http://192.168.101.53:8090";
            }
            //List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            //pairs.add(new BasicNameValuePair("type", String.valueOf(type)));

            Map<String, String> params = new HashMap<String, String>();
            params.put("type", String.valueOf(type));
            String str = HttpClientHelper.post(url + "knowledge/hot/comment.json", params);
            model.put("list", getKnowledgeList(str));
        } catch (Exception e) {
            logger.error("最热排行请求失败{}", e.toString());
            e.printStackTrace();
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(model);
    }

    // 获取聚合阅读
    @ResponseBody
    @RequestMapping(value = "/home/getAggregationRead/{type}/{page}/{size}")
    public InterfaceResult getAggregationRead(HttpServletRequest request, HttpServletResponse response, @PathVariable int type,
                                              @PathVariable int page, @PathVariable int size) throws Exception {

        User user = this.getUser(request);
        Long userId = 0L;
        if (user != null) {
            userId = this.getUserId(user);
        }
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            List<ColumnSelf> columnList = columnCustomService.queryListByPidAndUserId((long)type,userId);
            String [] columnIds = getColumnIds(columnList, type);
            if (columnIds.length > 0) {
                model = knowledgeHomeService.getAggregationRead(userId, columnIds, page, size);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return InterfaceResult.getSuccessInterfaceResultInstance(model);
    }

    @ResponseBody
    @RequestMapping(value = "/home/getDockingKnowledge/{targetType}/{targetId}/{page}/{size}/{scope}")
    public InterfaceResult getDockingKnowledge(HttpServletRequest request, HttpServletResponse response,
                                               @PathVariable short targetType,@PathVariable int targetId,
                                               @PathVariable int page, @PathVariable int size,
                                               @PathVariable int scope) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        long userId = this.getUserId(user);
        Map<String, Object> model = new HashMap<String, Object>(1);

        if (1 == targetType) {// 知识
            String url = (String) request.getSession().getServletContext().getAttribute("newQueryHost");
            if (StringUtils.isEmpty(url)) {
                ResourceBundle resource = ResourceBundle.getBundle("application");
                url = resource.getString("knowledge.new.query.host");
            }
            if (StringUtils.isEmpty(url) || url.indexOf("http") < 0) {
                url = "http://192.168.101.53:8090";
            }

            List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            pairs.add(new BasicNameValuePair("userid", String.valueOf(userId)));
            pairs.add(new BasicNameValuePair("targetType", String.valueOf(targetType)));
            pairs.add(new BasicNameValuePair("scope", String.valueOf(scope)));
            pairs.add(new BasicNameValuePair("page", String.valueOf(page)));
            pairs.add(new BasicNameValuePair("rows", String.valueOf(size)));
            pairs.add(new BasicNameValuePair("targetId", String.valueOf(targetId)));
            try {
                String responseJson = HttpClientHelper.post(url + "/API/relation.do", pairs);
                model.put("list", getKnowledge(responseJson));
            } catch (Exception e) {
                logger.error("连接大数据出错！");
                e.printStackTrace();
            }
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(model);
    }

    /**
     * 首页获取大数据知识热门推荐和发现热门知识
     * @param type 1,推荐 2,发现
     * @param page
     * @param size
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/home/getRecommendedKnowledge/{type}/{page}/{size}", method = RequestMethod.GET)
    public InterfaceResult<Map<String, Object>> getRecommendedKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                                        @PathVariable short type,@PathVariable int page, @PathVariable int size) throws Exception {

        User user = this.getUser(request);
        if(user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        //String url = (String) request.getSession().getServletContext().getAttribute("newQueryHost");
        ResourceBundle resource = ResourceBundle.getBundle("application");
        String url = resource.getString("knowledge.url.query");
        if (StringUtils.isEmpty(url) || url.indexOf("http") < 0) {
            url = "http://192.168.101.41:8090";
        }
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("page", String.valueOf(page)));
        pairs.add(new BasicNameValuePair("rows", String.valueOf(size)));
        pairs.add(new BasicNameValuePair("type", String.valueOf(type)));// 1,推荐 2,发现
        Map<String, Object> model = new HashMap<String, Object>();
        try {
            String responseJson = HttpClientHelper.post(url + "/API/hotKno.do", pairs);
            model.put("list", PackingDataUtil.getRecommendResult(responseJson));
        } catch (Exception e) {
            logger.error("connect big data service failed！");
            e.printStackTrace();
        }
        return InterfaceResult.getSuccessInterfaceResultInstance(model);
    }

    private Map<Long, Long> getReadCount(List<Knowledge> knowledgeList)
    {
        Map<Long, Long> m = new HashMap<Long, Long>();
        for (Knowledge knowledge : knowledgeList) {
            long kid = knowledge.getId();
            KnowledgeCount ks = knowledgeCountService.getKnowledgeCount(knowledge.getId());
            long rcount = (ks == null ? 0 : ks.getClickCount());
            m.put(kid, rcount);
        }
        return m;
    }

    private List<Map<String, Object>> getKnowledgeList(String str) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        // 转换bsn为MAP
        Map<String, Object> returnParams = KnowledgeUtil.readValue(Map.class, str);
        String bsn = returnParams.get("bsn") + "";
        JSONObject bsnObjects = JSONObject.fromObject(bsn);
        Map<String, Object> bsnMaps = (Map<String, Object>) bsnObjects;

        // 解析bsnMaps
        if (bsnMaps != null && bsnMaps.size() > 0) {
            for (int i = 1; i < bsnMaps.size() + 1; i++) {
                String bsnStr = bsnMaps.get(i + "") + "";
                if (StringUtils.isBlank(bsnStr) || StringUtils.equalsIgnoreCase(bsnStr, "null")) {
                    continue;
                }
                JSONObject bsnMap = JSONObject.fromObject(bsnStr);
                // 将字符串生成单个MAP
                Map<String, Object> singleMap = (Map<String, Object>) bsnMap;
                if (singleMap != null) {
                    list.add(singleMap);
                }
            }
        }
        return list;
    }

    public List<RecommendResult> getKnowledge(String str) throws Exception {
        List<RecommendResult> maps = new ArrayList<RecommendResult>();
        if (StringUtils.isNotBlank(str)) {
            JsonNode knowNode = getJsonNode(str, "knos");
            if (knowNode != null) {
                JsonNode datasNode = knowNode.get("datas");
                if (datasNode != null) {
                    RecommendResult sr = null;
                    Iterator<JsonNode> resultList = datasNode.elements();
                    while (resultList.hasNext()) {
                        JsonNode node = (JsonNode)resultList.next();
                        sr = new RecommendResult();
                        sr.setId(Long.parseLong(node.get("id").toString()));
                        sr.setTitle(node.get("name").toString());
                        sr.setKnowledgeType(node.get("knoType").intValue());
                        sr.setTagsScores(node.get("tagsScores").toString());
                        maps.add(sr);
                    }
                }
            }
        }
        return maps;
    }
}
