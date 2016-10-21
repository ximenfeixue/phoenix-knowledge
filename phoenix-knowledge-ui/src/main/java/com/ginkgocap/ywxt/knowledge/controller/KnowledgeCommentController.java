package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
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
import java.util.List;

/**
 * Created by Chen Peifeng on 2016/2/2.
 */
@Controller
@RequestMapping("knowledgeComment")
public class KnowledgeCommentController extends BaseController
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeCommentController.class);

    @Autowired
    private KnowledgeCommentService knowledgeCommentService;

    //@Autowired
    //KnowledgeCountService knowledgeCountService;

    /**
     * des:创建评论
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value="/{knowledgeId}", method = RequestMethod.POST)
    @ResponseBody
    public InterfaceResult createKnowledgeComment(@PathVariable Long knowledgeId, HttpServletRequest request, HttpServletResponse reponse)
    {
        String requestJson = null;
        try{
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }

            requestJson = getBodyParam(request);
            if(StringUtils.isBlank(requestJson)){
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            KnowledgeComment knowledgeComment  = KnowledgeUtil.readValue( KnowledgeComment.class, requestJson);
            if (knowledgeId == null || knowledgeComment == null || StringUtils.isBlank(knowledgeComment.getContent())) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            knowledgeComment.setOwnerId(user.getId());
            knowledgeComment.setOwnerName(user.getName());
            long commentId = knowledgeCommentService.create(knowledgeComment);
            if (commentId > 0) {
                //knowledgeCountService.updateCommentCount(knowledgeId);
                return InterfaceResult.getSuccessInterfaceResultInstance(commentId);
            }
            logger.error("Save Knowledge Comment to Mongo failed : knowledgeId: " + knowledgeId + " Comment: " + knowledgeComment.getContent());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);

        }catch(Exception e){
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:更新评论
     * @param commentId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value="/{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public InterfaceResult updateKnowledgeComment(@PathVariable Long commentId, HttpServletRequest request, HttpServletResponse reponse)
    {
        String knowledgeComment = null;
        try{
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }

            knowledgeComment = getBodyParam(request);
            if(commentId == null || StringUtils.isBlank(knowledgeComment)){
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            if (knowledgeCommentService.update(commentId, user.getId(), knowledgeComment)) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
            }

            logger.error("Update Knowledge Comment to Mongo failed : knowledgeId: " + commentId + " Comment: " + knowledgeComment);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_COMMENT_EXCEPTION_60051);
        }catch(Exception e){
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:删除评论
     * @param commentId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public InterfaceResult deleteKnowledgeComment(HttpServletRequest request, HttpServletResponse response,@PathVariable Long commentId)
    {
        try{
            if (commentId == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }
            if(knowledgeCommentService.delete(commentId, user.getId())){
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
            }
            logger.error("Delete Knowledge Comment failed : knowledgeId: " + commentId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_COMMENT_EXCEPTION_60051);
        } catch(Exception e){
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:获取评论列表
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value="/list/{knowledgeId}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult getKnowledgeCommentList(@PathVariable Long knowledgeId, HttpServletRequest request, HttpServletResponse reponse)
    {
        try{
            User user = getJTNUser(request);
            if (user != null) {
                logger.info("Query knowledge comment list. knowledgeId: " + knowledgeId + " userId: " + user.getId());
            }

            if (knowledgeId == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            List<KnowledgeComment> commentList = knowledgeCommentService.getKnowledgeCommentList(knowledgeId);
            if (logger.isDebugEnabled()) {
                logger.debug("Get Knowledge comment : size:" + commentList.size() + " Content: " + commentList);
            }
            return InterfaceResult.getSuccessInterfaceResultInstance(commentList);
        } catch(Exception e){
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:获取评论数量
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value="/count/{knowledgeId}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult getKnowledgeCommentCount(@PathVariable Long knowledgeId, HttpServletRequest request, HttpServletResponse reponse)
    {
        try{
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }

            if (knowledgeId == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            Long count = knowledgeCommentService.getKnowledgeCommentCount(knowledgeId);
            return InterfaceResult.getSuccessInterfaceResultInstance(count);
        } catch(Exception e){
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }
}
