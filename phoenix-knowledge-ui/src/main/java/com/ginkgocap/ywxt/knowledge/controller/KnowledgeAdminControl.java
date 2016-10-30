package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.service.KnowledgeService;
import com.gintong.frame.util.dto.InterfaceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by gintong on 2016/10/30.
 */
@Controller
@RequestMapping("/knowledgeAdmin")
public class KnowledgeAdminControl extends BaseController
{
    @Autowired
    private KnowledgeService knowledgeService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public InterfaceResult update(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE)
    public InterfaceResult delete(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        return null;
    }
}
