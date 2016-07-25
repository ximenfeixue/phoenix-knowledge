package com.ginkgocap.ywxt.knowledge.web.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    private String baseUrl = hostUrl + "/knowledgeComment/";
    private String listUrl = baseUrl + "list/";

    public void testKnowledgeCommentCreate()
    {
        CommentForCreate = LogMethod();
        String comment = Util.getKnowledgeComment(KnowledgeId, CommentForCreate+"_1");
        createKnowledgeComment(comment);

        comment = Util.getKnowledgeComment(KnowledgeId, CommentForUpdate+"_2");
        createKnowledgeComment(comment);

        comment = Util.getKnowledgeComment(KnowledgeId, CommentForDelete+"_3");
        createKnowledgeComment(comment);
    }

    public void testKnowledgeCommentUpdate()
    {
        CommentForUpdate = LogMethod();
        createKnowledgeComment(Util.getKnowledgeComment(KnowledgeId, CommentForUpdate));
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
        String URL = baseUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            notifNode = Util.HttpRequestResult(Util.HttpMethod.PUT, URL, CommentForUpdate+"--Update");
        } catch (Exception e) {
            writeException(e);
        }
        checkResult(notifNode);
    }
    
    public void testKnowledgeCommentUpdateFail()
    {
        CommentForUpdate = LogMethod();
        Long commentId = 0L;
        String URL = baseUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            notifNode = Util.HttpRequestResult(Util.HttpMethod.PUT, URL, CommentForUpdate+"_failed");
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
            notifNode = Util.HttpRequestFull(Util.HttpMethod.GET, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        checkResultWithData(notifNode);
    }

    public void testKnowledgeCommentGetCount()
    {
        LogMethod();
        String URL = baseUrl + "count/" + KnowledgeId;
        JsonNode notifNode = null;
        try {
            notifNode = Util.HttpRequestFull(Util.HttpMethod.GET, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        LOG("Get Knowledge Comment Count: " + Util.getResponseData(notifNode));
        checkResultWithData(notifNode);
    }

    public void testKnowledgeCommentDelete()
    {
        CommentForDelete = LogMethod();
        long commentId = 0L;
        createKnowledgeComment(Util.getKnowledgeComment(KnowledgeId, CommentForDelete));
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
        String URL = baseUrl + commentId;
        JsonNode notifNode = null;
        try {
            LOG("Knowledge Comment for delete, commentId: " + commentId);
            notifNode = Util.HttpRequestResult(Util.HttpMethod.DELETE, URL, null);
        } catch (Exception e) {
            writeException(e);
        }
        checkResult(notifNode);
    }
    
    public void testKnowledgeCommentDeleteFail()
    {
        LogMethod();
        Long commentId = 0L;
        String URL = baseUrl + commentId.longValue();
        JsonNode notifNode = null;
        try {
            LOG("Knowledge Comment for delete: "+commentId);
            notifNode = Util.HttpRequestResult(Util.HttpMethod.DELETE, URL, null);
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
            retNode = Util.HttpRequestFull(Util.HttpMethod.GET, URL, null);
            JsonNode retData = retNode.get("responseData");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            JsonNode jsonNode = objectMapper.readTree(retData.asText());
            if (jsonNode != null && jsonNode.isArray()) {
                knowledgeCommentList = new ArrayList<KnowledgeComment>();
                Iterator<JsonNode> fields = jsonNode.iterator();
                while (fields.hasNext()) {
                    JsonNode node = fields.next();
                    if (node.getNodeType() == JsonNodeType.OBJECT) {
                        KnowledgeComment comment = (KnowledgeComment) objectMapper.readValue(node.toString(), KnowledgeComment.class);
                        knowledgeCommentList.add(comment);
                    }
                }
            }
        } catch (Exception e) {
            writeException(e);
        }
        return knowledgeCommentList;
    }

    private void createKnowledgeComment(String jsonContent)
    {
        String URL = baseUrl + KnowledgeId;
        JsonNode notifNode = null;
        try {
            notifNode = Util.HttpRequestResult(Util.HttpMethod.POST, URL, jsonContent);
            Util.checkRequestResultSuccess(notifNode);
        } catch (Exception e) {
            writeException(e);
        }
    }

}
