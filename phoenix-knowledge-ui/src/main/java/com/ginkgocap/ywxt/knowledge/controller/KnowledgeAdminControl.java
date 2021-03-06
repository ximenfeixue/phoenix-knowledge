package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBaseExt;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.Page;
import com.ginkgocap.ywxt.organ.model.organ.OrganMember;
import com.ginkgocap.ywxt.organ.service.organ.OrganMemberService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gintong on 2016/10/30.
 */
@Controller
@RequestMapping("/knowledge/admin")
public class KnowledgeAdminControl extends BaseKnowledgeController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeAdminControl.class);

    @Autowired
    private OrganMemberService organMemberService;

    @ResponseBody
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public InterfaceResult createKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        User user = this.getUser(request);
        return this.create(request, user);
    }

    @ResponseBody
    @RequestMapping(value="/bigdata/create", method = RequestMethod.POST)
    public InterfaceResult createKnowledgeForBigData(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (!isGinTong(request)) {
            logger().error("not from gintong user, so block it.");
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        User user = this.JTNUser();
        return this.create(request, user);
    }

    @ResponseBody
    @RequestMapping(value="/update", method = RequestMethod.PUT)
    public InterfaceResult updateKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        User user = this.getUser(request);
        return this.updateKnowledge(request, user);
    }


    @ResponseBody
    @RequestMapping(value="/delete/{id}/{type}", method = RequestMethod.DELETE)
    public InterfaceResult deleteKnowledge(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable long id, @PathVariable int type) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        List<Long> ids = Arrays.asList(id);
        return this.knowledgeService.logicDelete(ids, type);
    }

    /**
     * 组织 管理者 可以进行删除
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/organ/delete/{id}/{type}/{organId}", method = RequestMethod.DELETE)
    public InterfaceResult deleteOrganKnowledge(HttpServletRequest request,
                                                @PathVariable long id, @PathVariable int type,
                                                @PathVariable long organId) {
        User user = this.getUser(request);
        if (user == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);

        long currentUserId = user.getId();
        OrganMember memberDetail = null;
        try {
            memberDetail = organMemberService.findMemberDetail(organId, currentUserId);
        } catch (Exception e) {
            logger.error("invoke organMemberService failed. method : [findMemberDetail]. userId : " + currentUserId);
            e.printStackTrace();
        }
        // 是游客 的情况
        if (memberDetail == null)
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "没有权限删除该资源");
        // 是组织普通成员
        if (memberDetail != null && "m".equals(memberDetail.getRole()))
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "没有权限删除该资源");

        // 验证当前用户是否有 权限修改删除 资源
        List<Long> ids = Arrays.asList(id);
        return this.knowledgeService.logicDelete(ids, type);
    }

    @ResponseBody
    @RequestMapping(value="/batchDelete/{type}", method = RequestMethod.PUT)
    public InterfaceResult batchDeleteKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable int type) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> idList = KnowledgeUtil.readListValue(Long.class, requestJson);
        if (CollectionUtils.isEmpty(idList)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        return this.knowledgeService.logicDelete(idList, type);
    }

    @ResponseBody
    @RequestMapping(value="/batchRecovery/{type}", method = RequestMethod.PUT)
    public InterfaceResult batchRecoveryKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable int type) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> idList = KnowledgeUtil.readListValue(Long.class, requestJson);
        if (CollectionUtils.isEmpty(idList)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        return this.knowledgeService.logicRecovery(idList, type);
    }

    @ResponseBody
    @RequestMapping(value="/topKnowledge/{type}", method = RequestMethod.POST)
    public InterfaceResult topKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable short type) throws Exception  {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> idList = KnowledgeUtil.readListValue(Long.class, requestJson);
        if (CollectionUtils.isEmpty(idList)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeService.addTopKnowledge(idList, type);
        } catch (Exception ex) {
            logger.error("add top knowledge failed. error: " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }
        return InterfaceResult.getSuccessInterfaceResultInstance(true);
    }

    @ResponseBody
    @RequestMapping(value="/cancelTopKnowledge/{type}", method = RequestMethod.PUT)
    public InterfaceResult cancelTopKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                  @PathVariable short type) throws Exception {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        String requestJson = this.getBodyParam(request);
        List<Long> idList = KnowledgeUtil.readListValue(Long.class, requestJson);
        if (CollectionUtils.isEmpty(idList)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
        }

        try {
            this.knowledgeService.deleteTopKnowledge(idList, type);
        } catch (Exception ex) {
            logger.error("delete top knowledge failed. error: " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }
        return InterfaceResult.getSuccessInterfaceResultInstance(true);
    }

    @ResponseBody
    @RequestMapping(value="/getTopKnowledge/{type}/{page}/{size}", method = RequestMethod.GET)
    public InterfaceResult getTopKnowledge(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable short type, @PathVariable int page,
                                           @PathVariable int size) throws Exception {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        try {
            List<KnowledgeBase> baseList = this.knowledgeService.getTopKnowledgeByPage(type, page, size);
            return InterfaceResult.getSuccessInterfaceResultInstance(baseList);
        } catch (Exception ex) {
            logger.error("delete top knowledge failed. error: " + ex.getMessage());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION, false);
        }
    }

    @ResponseBody
    @RequestMapping(value="/getKnowledgeByPage/{userId}/{type}/{status}/{page}/{size}", method = RequestMethod.POST)
    public InterfaceResult getKnowledgeByPagePost(HttpServletRequest request, HttpServletResponse response,
                                              @PathVariable long userId,@PathVariable short type,
                                              @PathVariable short status, @PathVariable int page,
                                              @PathVariable int size) throws Exception
    {
        String keyWord = this.getBodyParam(request);
        return getKnowledgeByPage(request, response, userId, type, status, page, size, keyWord);
    }

    @ResponseBody
    @RequestMapping(value="/getKnowledgeByPage/{userId}/{type}/{status}/{page}/{size}/{title}", method = RequestMethod.GET)
    public InterfaceResult getKnowledgeByPage(HttpServletRequest request, HttpServletResponse response,
                                              @PathVariable long userId,@PathVariable short type,
                                              @PathVariable short status, @PathVariable int page,
                                              @PathVariable int size,@PathVariable String title) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        List<KnowledgeBase> baseList = knowledgeIndexService.getAllByType(userId, type, status, title, page, size);
        if (page  == 0) {
            List<KnowledgeBase> topBaseList = this.knowledgeService.getTopKnowledgeByPage(type, 0, 5);
            if (CollectionUtils.isNotEmpty(topBaseList)) {
                Map<Long, KnowledgeBase> baseMap = new HashMap<Long, KnowledgeBase>(size + 5);
                for (KnowledgeBase base : topBaseList) {
                    if (base != null) {
                        base.setEssence((short) 1);
                        baseMap.put(base.getId(), base);
                    } else {
                        topBaseList.remove(base);
                    }
                }
                for (KnowledgeBase base : baseList) {
                    if (base != null && !baseMap.containsKey(base.getId())) {
                        topBaseList.add(base);
                    }
                }
                return InterfaceResult.getSuccessInterfaceResultInstance(topBaseList);
            }
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(baseList);
    }

    /**
     * 提取所有知识创建数据
     * @param page 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allKnowledge/{page}/{size}/{total}/{type}", method = RequestMethod.POST)
    public InterfaceResult allKnowledgePost(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable int page, @PathVariable int size,
                                            @PathVariable int total, @PathVariable int type) throws Exception {
        final String keyword = this.getBodyParam(request);
        return allKnowledge(request, response, page, size, total, type, keyword);
    }

    /**
     * 提取所有知识创建数据
     * @param page 分页起始
     * @param size 分页大小
     * @throws java.io.IOException
     */
    @ResponseBody
    @RequestMapping(value = "/allKnowledge/{page}/{size}/{total}/{type}/{keyword}", method = RequestMethod.GET)
    public InterfaceResult allKnowledge(HttpServletRequest request, HttpServletResponse response,
                                            @PathVariable int page, @PathVariable int size,
                                            @PathVariable int total, @PathVariable int type, @PathVariable String keyword) throws Exception {

        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        keyword = resetKeyWord(keyword);

        if (keyword == null || keyword.trim().length() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        if (total == -1) {
            if (type == 1) {
                total = countKnowledgeByTitle(keyword);
            } else if (type == 2) {
                total = countKnowledgeByUserName(keyword);
            } else {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
            }
        }

        int start = page * size;
        if (start > total) {
            return InterfaceResult.getSuccessInterfaceResultInstance("到达最后一页，知识已经取完。");
        }

        List<KnowledgeBase> createdKnowledgeList = null;
        if (type == 1) {
            createdKnowledgeList = this.knowledgeService.getByTitle(keyword, start, size);
        } else if (type == 2) {
            createdKnowledgeList = this.knowledgeService.getByUserName(keyword, start, size);
        }
        InterfaceResult<Page<KnowledgeBaseExt>> result = this.knowledgeExtListPage(total, page, size, createdKnowledgeList);
        logger.info(".......get all created knowledge success. size: " + (createdKnowledgeList != null ? createdKnowledgeList.size() : 0));
        return result;
    }

    @ResponseBody
    @RequestMapping(value="/comment/delete", method = RequestMethod.DELETE)
    public InterfaceResult deleteKnowledgeComment(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    private int countKnowledgeByUserName(String userName)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.countByUserName(userName);
        }catch (Exception ex) {
            logger.error("get created knowledge count failed. userName: " + userName + ", error: " + ex.getMessage());
        }

        logger.info("createCount: " + createCount);
        return createCount;
    }

    private int countKnowledgeByTitle(String title)
    {
        int createCount = 0;
        try {
            createCount = this.knowledgeService.countByTitle(title);
        }catch (Exception ex) {
            logger.error("get created knowledge count failed. title: " + title + ", error: " + ex.getMessage());
        }

        logger.info("createCount: " + createCount);
        return createCount;
    }

    public Logger logger() { return this.logger; }
}
