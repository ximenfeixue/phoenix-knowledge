package com.ginkgocap.ywxt.knowledge.web.test;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;

/**
 * Created by Chen Peifeng on 2016/1/29.
 */
public class KnowledgeCommentWebTest extends BaseTestCase
{
    private Long KnowledgeId = 11111111L;
    List<KnowledgeComment> knowledgeCommentList = new ArrayList<KnowledgeComment>();
    private String CommentForCreate = "Comment For Update-UnitTest";
    private String CommentForUpdate = "Comment For Update-UnitTest";
    private String CommentForDelete = "Comment For Delete-UnitTest";

    private String listUrl = baseCommentUrl + "list/";

    public void testKnowledgeCommentCreate()
    {
        CommentForCreate = LogMethod();
        String comment = getKnowledgeComment(KnowledgeId, CommentForCreate +"_1");
        createKnowledgeComment(comment);

        comment = getKnowledgeComment(KnowledgeId, 1, CommentForUpdate +"_2", false);
        createKnowledgeComment(comment);

        comment = getKnowledgeComment(KnowledgeId, CommentForDelete +"_3");
        createKnowledgeComment(comment);
    }

    /*
    public void testKnowledgeCommentUpdate()
    {
        CommentForUpdate = LogMethod();
        createKnowledgeComment(getKnowledgeComment(KnowledgeId, CommentForUpdate));
        List<KnowledgeComment> comments = getKnowledgeCommentList();
        if(comments == null || comments.size() <= 0) {
            tryFail();
        }
        Long commentId = 0L;
        for (KnowledgeComment comment : comments) {
            if (CommentForUpdate.equals(comment.getContent())) {
                commentId = comment.getId();
                break;
            }
        }
        if (commentId == 0L) {
            tryFail();
        }
        String URL = baseCommentUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            notifNode = HttpRequestResult(HttpMethod.PUT, URL, CommentForUpdate + "--Update");
        } catch (Exception e) {
            writeException(e);
        }
        checkResult(notifNode);
    }*/
    
    public void testKnowledgeCommentUpdateFail()
    {
        CommentForUpdate = LogMethod();
        Long commentId = 0L;
        String URL = baseCommentUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            notifNode = HttpRequestResult(HttpMethod.PUT, URL, CommentForUpdate + "_failed");
        } catch (Exception e) {
            writeException(e);
        }
        checkResultFail(notifNode);
    }

    public void testKnowledgeCommentGetList()
    {
        LogMethod();
        String URL = listUrl + KnowledgeId;
        JsonNode notifNode = null;
        try {
            notifNode = HttpRequestFull(HttpMethod.GET, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        checkResultWithData(notifNode);
    }

    public void testKnowledgeCommentGetCount()
    {
        LogMethod();
        String URL = baseCommentUrl + "count/" + KnowledgeId;
        JsonNode notifNode = null;
        try {
            notifNode = HttpRequestFull(HttpMethod.GET, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        LOG("Get Knowledge Comment Count: " + getResponseData(notifNode));
        checkResultWithData(notifNode);
    }

    public void testKnowledgeCommentDelete()
    {
        CommentForDelete = LogMethod();
        long commentId = 0L;
        createKnowledgeComment(getKnowledgeComment(KnowledgeId, CommentForDelete));
        List<KnowledgeComment> comments = getKnowledgeCommentList();
        if(comments == null || comments.size() <= 0) {
            tryFail();
        }

        LOG("Knowledge Comment for for delete size: "+comments.size());
        for (KnowledgeComment comment : comments) {
            commentId = comment.getId();
            if ("testKnowledgeCommentDelete".equals(comment.getContent())) {
                break;
            }
        }
        if (commentId == 0L) {
            fail();
        }
        String URL = baseCommentUrl + commentId;
        JsonNode notifNode = null;
        try {
            LOG("Knowledge Comment for delete, commentId: " + commentId);
            notifNode = HttpRequestResult(HttpMethod.DELETE, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        checkResult(notifNode);
    }
    
    public void testKnowledgeCommentDeleteFail()
    {
        LogMethod();
        Long commentId = 0L;
        String URL = baseCommentUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            LOG("Knowledge Comment for delete: "+commentId);
            notifNode = HttpRequestResult(HttpMethod.DELETE, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        checkResultFail(notifNode);
    }

    //Private method
    private List<KnowledgeComment> getKnowledgeCommentList()
    {
        String URL = listUrl + KnowledgeId;
        JsonNode retNode = null;
        try {
            retNode = HttpRequestFull(HttpMethod.GET, URL, null);
            JsonNode retData = retNode.get("responseData");
            this.assertNotNull(retData);
            knowledgeCommentList = KnowledgeUtil.readListValue(KnowledgeComment.class, retData.toString());
        } catch (Exception e) {
            writeException(e);
        }
        return knowledgeCommentList;
    }

    private void createKnowledgeComment(String jsonContent)
    {
        String URL = baseCommentUrl + KnowledgeId;
        JsonNode notifNode = null;
        try {
            notifNode = HttpRequestResult(HttpMethod.POST, URL, jsonContent);
            checkRequestResultSuccess(notifNode);
        } catch (Exception e) {
            writeException(e);
        }
    }

}
