package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
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

/**
 * Created by gintong on 2016/10/30.
 */
@Controller
@RequestMapping("/knowledge/admin")
public class KnowledgeAdminControl extends BaseKnowledgeController
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeAdminControl.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @ResponseBody
    @RequestMapping(value="/create", method = RequestMethod.POST)
    public InterfaceResult createKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        User user = this.getUser(request);
        if(user == null || user.getId() != 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        return this.create(request, user);
    }

    @ResponseBody
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public InterfaceResult updateKnowledge(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        User user = this.getUser(request);
        if(user == null || user.getId() != 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }
        return this.updateKnowledge(request, user);
    }


    @ResponseBody
    @RequestMapping(value="/delete/{id}/{type}", method = RequestMethod.DELETE)
    public InterfaceResult deleteKnowledge(HttpServletRequest request, HttpServletResponse response,
                                           @PathVariable long id, @PathVariable int type) throws Exception
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(value="/batchDelete/{type}", method = RequestMethod.DELETE)
    public InterfaceResult batchDeleteKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable int type) throws Exception
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(value="/batchRecovery/{type}", method = RequestMethod.DELETE)
    public InterfaceResult batchRecoveryKnowledge(HttpServletRequest request, HttpServletResponse response,
                                                @PathVariable int type) throws Exception
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(value="/getKnowledgeByPage", method = RequestMethod.POST)
    public InterfaceResult getKnowledgeByPage(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(value="/comment/delete", method = RequestMethod.DELETE)
    public InterfaceResult deleteKnowledgeComment(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    public Logger logger() { return this.logger; }
}
