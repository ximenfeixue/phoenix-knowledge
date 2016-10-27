package com.ginkgocap.ywxt.knowledge.filter;

import com.ginkgocap.ywxt.knowledge.utils.CommonUtil;
import com.ginkgocap.ywxt.knowledge.utils.Utils;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.util.Encodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by gintong on 2016/8/11.
 */
public class AppFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(AppFilter.class);

    String excludedUrl = "";
    String[] excludedUrlArray = {};//任何情况都不需要登录，在web.xml里面配置
    //允许游客状态的接口
    String[] webExcludedUrl = { "mobileApp/getVCodeForPassword.json",
            "mobileApp/setNewPassword.json","register/pwd/reset.json",
            "org/dynamicNews/dynamicNewsList.json","dynamicNews/getDynamicComment",
            "person/peopleHomeList.json","person/getPeopleDetail.json","code/peopleCodeList.json",
            "code/peopleCodeListByName.json","get/area.json","/dynamicNews/getListDynamicNews.json","/register/sendValidateEmail.json",
            "person/getPeopleTemplate.json","/register/isExistByMobileOrByEmail.json","/knowledge/detail","/file/nginx/downloadFJ","/org/orgAndProInfo.json","/customer/findCusProfile.json","/demand/getDemandDetail.json","/demand/findDemandFile.json",
            "/webknowledge/home/separate","/webknowledge/home/getAggregationRead","/webknowledge/home/getHotTag","/webknowledge/home/getRecommendedKnowledge",
            "/knowledge/allKnowledgeByColumnAndSource","/webknowledge/queryMore","/webknowledge/home/getHotList","/webknowledge/home/getCommentList",
            "set/checkMailByStatus.json","/register/checkOrganRegister.json","metadata/search.json","get/code.json","/knowledgeComment/list","/org/getDiscoverList.json","/resource/hotList.json",
            "/register/checkIdentifyCode.json","/demand/getDemandList.json","/demandComment/getDemandCommentList.json","/file/nginx/downloadFJ2"};


    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String s = req.getHeader("s");
        logger.info("params s is {}", s);
        boolean requestIsFromWeb = CommonUtil.isWeb(req);

        Long userId = 0L;
        User user = CommonUtil.getUser(req);
        if (null != user && user.getId() > 0) {
            userId = user.getId();
            request.setAttribute("sessionUser", user);
        }

        String url = req.getRequestURI();
        // cookies不为空，则清除
        if (url.contains("file/") || url.contains("update/bindEmailStatus")
                || url.contains("/organ/uploadIdCardImg.json")) {
            chain.doFilter(request, response);
            return;
        }
        boolean loginFlag = true;
        for (String excludedUrl : excludedUrlArray) {
            if (url.contains(Utils.replaceSpecial(excludedUrl))) {
                loginFlag = false;
                break;
            }
        }

        if (requestIsFromWeb) {
            for (String excludedUrl : webExcludedUrl) {
                if (url.contains(excludedUrl)) {
                    loginFlag = false;
                    break;
                }
            }
        }

        if (loginFlag && null == user) {
            response.setCharacterEncoding("utf-8");
            res.setHeader("errorCode", "-1");
            try {
                String str = Encodes.encodeBase64("用户长时间未操作或已过期,请重新登录".getBytes());
                logger.info("用户长时间未操作或已过期");
                res.setHeader("errorMessage", str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.getWriter().write("{\"notification\":{\"notifCode\": \"1000\",\"notifInfo\": \"用户长时间未操作或已过期,请重新登录\"}}");
            return;
        }

        // 敏感词过滤
        /*
        BufferedReader reader = request.getReader();
        String line = null;
        StringBuffer jsonIn = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            jsonIn.append(line);
        }
        String result = jsonIn.toString();
        if (result.equals("")) {
            result = request.getParameter("json");
        }
        String requestJson = result;
        reader.close();

        String sessionId = ((HttpServletRequest) request).getHeader("sessionID");

        if (requestJson != null && !"".equals(requestJson)) {
            request.setAttribute("requestJson", requestJson);
            chain.doFilter(request, response);
            CommonUtil.setRequestIsFromWebFlag(false);
            return;
        } else {
            request.setAttribute("requestJson", "{}");
            chain.doFilter(request, response);
            CommonUtil.setRequestIsFromWebFlag(false);
            return;
        }*/
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        excludedUrl = config.getInitParameter("excludedUrl");
        if (StringUtils.isNotEmpty(excludedUrl)) {
            excludedUrlArray = excludedUrl.split(",");
        }
    }
}
