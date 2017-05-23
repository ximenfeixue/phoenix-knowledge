package com.ginkgocap.ywxt.knowledge.controller;

import com.ginkgocap.ywxt.bean.util.BeanUtil;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCommentService;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.task.DataSyncTask;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.ywxt.im.model.MessageNotify;
import com.gintong.ywxt.im.model.MessageNotifyType;
import com.gintong.ywxt.im.service.MessageNotifyService;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen Peifeng on 2016/2/2.
 */
@Controller
@RequestMapping("knowledgeComment")
public class KnowledgeCommentController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(KnowledgeCommentController.class);

    @Autowired
    private KnowledgeCommentService knowledgeCommentService;

    @Autowired
    private DataSyncTask dataSyncTask;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public InterfaceResult createComment(HttpServletRequest request, HttpServletResponse reponse, @PathVariable Long knowledgeId) {
        return createKnowledgeComment(request, reponse, -1L);
    }
    /**
     * des:创建评论
     *
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value = "/{knowledgeId}", method = RequestMethod.POST)
    @ResponseBody
    public InterfaceResult createKnowledgeComment(HttpServletRequest request, HttpServletResponse reponse, @PathVariable Long knowledgeId) {
        String requestJson = null;
        try {
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }

            requestJson = getBodyParam(request);
            if (StringUtils.isBlank(requestJson)) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            KnowledgeComment comment = KnowledgeUtil.readValue(KnowledgeComment.class, requestJson);
            if (comment.getKnowledgeId() <= 0 || comment.getColumnId() <= 0 || comment == null || StringUtils.isBlank(comment.getContent())) {
                logger.error("param is invalidated. knowId: " + comment.getKnowledgeId() + " columnType: " + comment.getColumnId() + " content: " + comment.getContent());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            if (comment.getColumnType() <= 0) {
                comment.setColumnType(comment.getColumnId());
            }
            comment.setOwnerId(user.getId());
            comment.setOwnerName(user.getName());
            comment.setPic(user.getPicPath());
            long commentId = knowledgeCommentService.create(comment);
            if (commentId > 0) {
                if (user.getId() != comment.getToId()) {
                    MessageNotify message = createMessageNotify(comment, user);
                    dataSyncTask.saveDataNeedSync(new DataSync(0, message));
                } else {
                    logger.info("comment self knowledge, so skip to send message notify.");
                }
                knowledgeCountService.updateCommentCount(comment.getKnowledgeId(), (short)comment.getColumnId());
                return InterfaceResult.getSuccessInterfaceResultInstance(commentId);
            }

            logger.error("Save Knowledge Comment to Mongo failed : knowledgeId: " + knowledgeId + " Comment: " + comment.getContent());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);

        } catch (Exception e) {
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:更新评论
     *
     * @param commentId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
    @ResponseBody
    public InterfaceResult updateKnowledgeComment(@PathVariable Long commentId, HttpServletRequest request, HttpServletResponse reponse) {
        String knowledgeComment = null;
        try {
            User user = getUser(request);
            if (user == null) {
                logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION);
            }

            knowledgeComment = getBodyParam(request);
            if (commentId == null || StringUtils.isBlank(knowledgeComment)) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            if (knowledgeCommentService.update(commentId, user.getId(), knowledgeComment)) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
            }

            logger.error("Update Knowledge Comment to Mongo failed : knowledgeId: " + commentId + " Comment: " + knowledgeComment);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DEMAND_COMMENT_EXCEPTION_60051);
        } catch (Exception e) {
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:删除评论
     *
     * @param commentId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    @ResponseBody
    public InterfaceResult deleteKnowledgeComment(HttpServletRequest request, HttpServletResponse response, @PathVariable long commentId) {

        if (commentId <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION,false);
        }
        User user = getUser(request);
        if (user == null) {
            logger.error(CommonResultCode.PERMISSION_EXCEPTION.getMsg());
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PERMISSION_EXCEPTION,false);
        }

        boolean result = false;
        CommonResultCode resultCode = CommonResultCode.PARAMS_EXCEPTION;
        final long userId = user.getId();
        try {
            result = knowledgeCommentService.delete(commentId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            resultCode = CommonResultCode.SYSTEM_EXCEPTION;
        }
        if (result) {
            logger.info("delete comment success. commentId: " + commentId + " userId: " + userId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS, result);
        }
        logger.info("delete comment failed. commentId: " + commentId + " userId: " + userId);
        return InterfaceResult.getInterfaceResultInstance(resultCode, result);
    }

    /**
     * des:获取评论列表
     *
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value = "/list/{knowledgeId}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult getKnowledgeCommentList(@PathVariable Long knowledgeId, HttpServletRequest request, HttpServletResponse reponse) {
        try {
            User user = getJTNUser(request);
            if (user != null) {
                logger.info("Query knowledge comment list. knowledgeId: " + knowledgeId + " userId: " + user.getId());
            }

            if (knowledgeId == null) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_NULL_EXCEPTION);
            }

            List<KnowledgeComment> commentList = knowledgeCommentService.getKnowledgeCommentList(knowledgeId);
            int commentSize = commentList != null ? commentList.size() : 0;
            if (logger.isDebugEnabled()) {
                logger.debug("Get Knowledge comment : size:" + commentList.size());
            }
            if (commentSize > 0) {
                Iterator iterator = commentList.iterator();
                while (iterator.hasNext()) {
                    KnowledgeComment comment = (KnowledgeComment) iterator.next();
                    if (comment.getVisible() == 0 && comment.getOwnerId() != user.getId()) {
                        iterator.remove();
                    }
                }
            }
            return InterfaceResult.getSuccessInterfaceResultInstance(commentList);
        } catch (Exception e) {
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * des:获取评论数量
     *
     * @param knowledgeId
     * @param request
     * @param reponse
     * @return
     */
    @RequestMapping(value = "/count/{knowledgeId}", method = RequestMethod.GET)
    @ResponseBody
    public InterfaceResult getKnowledgeCommentCount(@PathVariable Long knowledgeId, HttpServletRequest request, HttpServletResponse reponse) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            return InterfaceResult.getInterfaceResultInstanceWithException(CommonResultCode.SYSTEM_EXCEPTION, e);
        }
    }

    private MessageNotify createMessageNotify(final KnowledgeComment comment, final User user) {
        if (comment == null) {
            logger.error("knowledge comment is null, so skip to send message notify");
            return null;
        }

        final long toId =  comment.getToId();
        if (comment.getToId() == user.getId()) {
            logger.error("toId is equal userId, so skip send message notify. toId: " + toId);
        }

        MessageNotify message = new MessageNotify();
        final String uName = comment.getOwnerName();
        message.setType(MessageNotifyType.EKnowledge.value());
        if (comment.getTargetUid() >= 0) {
            message.setTitle(uName + "回复了你的评论");
            message.setToId(comment.getTargetUid());
        } else {
            message.setTitle(uName + "评论了你的知识");
            message.setToId(toId);
        }
        message.setFromId(comment.getOwnerId());
        message.setFromName(comment.getOwnerName());
        message.setContent(converToJson(comment.getKnowledgeId(), comment.getColumnId()));
        initHeaderPicAndVirtual(message, user);

        return message;
    }

    private void initHeaderPicAndVirtual(MessageNotify message, User user) {
        message.setPicPath(user.getPicPath());
        final short virtual = user.isVirtual() ? (short) 1 : (short) 0;
        message.setVirtual(virtual);
    }

    private String converToJson(long knowledgeId, int columnType) {
        Map<String, Object> map = mapContent(knowledgeId, columnType);
        return KnowledgeUtil.writeObjectToJson(map);
    }

    private Map<String, Object> mapContent(long knowledgeId, int columnType) {
        Map<String, Object> map = new HashMap<String, Object>(3);
        map.put("id", knowledgeId);
        map.put("columnType", columnType);
        map.put("type", MessageNotifyType.EKnowledge.value());
        return map;
    }

    public Logger logger() { return this.logger; }
}
