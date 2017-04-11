package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.IdType;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.List;

/**
 * Created by gintong on 2016/10/30.
 */
@Controller
@RequestMapping("/knowledge/admin")
public class KnowledgeAdminControl extends BaseKnowledgeController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeAdminControl.class);

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
    @RequestMapping(value="/update", method = RequestMethod.POST)
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

    @ResponseBody
    @RequestMapping(value="/batchDelete/{type}", method = RequestMethod.DELETE)
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
    @RequestMapping(value="/batchRecovery/{type}", method = RequestMethod.DELETE)
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
    @RequestMapping(value="/topKnowledge/{type}", method = RequestMethod.DELETE)
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
    @RequestMapping(value="/cancelTopKnowledge/{type}", method = RequestMethod.DELETE)
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
    @RequestMapping(value="/getKnowledgeByPage/{userId}/{type}/{status}/{title}/{page}/{size}", method = RequestMethod.POST)
    public InterfaceResult getKnowledgeByPage(HttpServletRequest request, HttpServletResponse response,
                                              @PathVariable long userId,@PathVariable short type,
                                              @PathVariable short status,@PathVariable String title,
                                              @PathVariable int page,@PathVariable int size) throws Exception
    {
        if (!isAdmin(request)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        List<KnowledgeBase> baseList = knowledgeBatchQueryService.getAllByPage(userId, type, status, title, page, size);
        return InterfaceResult.getSuccessInterfaceResultInstance(baseList);
    }

    @ResponseBody
    @RequestMapping(value="/comment/delete", method = RequestMethod.DELETE)
    public InterfaceResult deleteKnowledgeComment(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    public Logger logger() { return this.logger; }
}
