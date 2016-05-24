package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
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
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/5/23.
 */
@Controller
@RequestMapping("/knowledgeCount")
public class KnowledgeCountControl extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(KnowledgeCountControl.class);

    @Autowired
    KnowledgeCountService knowledgeCountService;

    @ResponseBody
    @RequestMapping(value="/click/{knowledgeId}", method = RequestMethod.PUT)
    public InterfaceResult clickCount(HttpServletRequest request,HttpServletResponse response,
                                       @PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (!knowledgeCountService.updateClickCount(knowledgeId))
        {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/share/{knowledgeId}", method = RequestMethod.PUT)
    public InterfaceResult shareCount(HttpServletRequest request,HttpServletResponse response,
                                      @PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (!knowledgeCountService.updateShareCount(knowledgeId))
        {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/collect/{knowledgeId}", method = RequestMethod.PUT)
    public InterfaceResult collectCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (!knowledgeCountService.updateCollectCount(knowledgeId))
        {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/comment/{knowledgeId}", method = RequestMethod.PUT)
    public InterfaceResult commentCount(HttpServletRequest request,HttpServletResponse response,
                                        @PathVariable long knowledgeId) throws Exception {
        User user = this.getUser(request);
        if (user == null) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
        }

        if (!knowledgeCountService.updateCommentCount(knowledgeId))
        {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping(value="/hotKnowledge/{limit}", method = RequestMethod.GET)
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
    }
}
