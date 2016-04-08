package com.ginkgocap.ywxt.knowledge.model;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.user.model.User;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Admin on 2016/3/24.
 */
public final class KnowledgeUtil {

    private static final boolean writeNumberAsString = false;
    private static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, writeNumberAsString);
    }

    /*
    public static Class<? extends Knowledge> getClassByColumnId(short columnId)
    {
        switch (columnId){
            case 1: return KnowledgeNews.class;
            case 2: return KnowledgeInvestment.class;
            case 3: return KnowledgeIndustry.class;
            case 4: return KnowledgeCase.class;
            case 5: return KnowledgeNewMaterials.class;
            case 6: return KnowledgeAsset.class;
            case 7: return KnowledgeMacro.class;
            case 8: return KnowledgeOpinion.class;
            case 9: return KnowledgeLaw.class;
            case 10: return KnowledgeArticle.class;
            /* For new requirement
            case 21: return KnowledgeInternet.class;
            case 22: return EKnowledgeSales.class;
            case 23: return EKnowledgeFinance.class;
            case 24: return EKnowledgeGame.class;
            case 25: return EKnowledgeEducation.class;
            case 26: return EKnowledgeMedical.class;
            case 27: return EKnowledgePolitical.class;
            case 28: return EKnowledgeAffair.class;
            case 29: return EKnowledgeScience.class;
            case 30: return EKnowledgeCulture.class;
            case 31: return EKnowledgeLiterature.class;
            case 32: return KnowledgeSports.class;
            case 33: return KnowledgeSociety.class;
            case 34: return KnowledgeMovie.class;

            default: return null;
        }
    }

    public static KnowledgeBase convertOldKnowledge(Knowledge knowledge)
    {
        if (knowledge == null) {
            throw new IllegalArgumentException("Knowledge is null");
        }
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(knowledge.getId());
        knowledgeBase.setColumnId(knowledge.getColumnId());
        knowledgeBase.setTitle(knowledge.getTitle());
        knowledgeBase.setContentDesc(knowledge.getDesc());
        knowledgeBase.setContent(knowledge.getContent());
        knowledgeBase.setCreateUserId(knowledge.getCId());
        knowledgeBase.setCreateUserName(knowledge.getCname());
        knowledgeBase.setCreateDate(new Date(knowledge.getCreatetime()).getTime());
        //TODO: check if picture Id is not a number
        knowledgeBase.setPictureId(Long.parseLong(knowledge.getPic()));
        knowledgeBase.setModifyDate(new Date(knowledge.getModifytime()).getTime());
        //knowledgeBase.setType();
        knowledgeBase.setReportStatus((short)knowledge.getReport_status());
        knowledgeBase.setStatus((short)knowledge.getStatus());
        knowledgeBase.setAuditStatus((short)knowledge.getStatus());

        return knowledgeBase;
    }*/

    //This is just for test, need do knowledge test but real user not exist
    public static User getDummyUser()
    {
        User user = new User();
        user.setId(1234567L);
        user.setUid(1234567L);
        user.setUserName("UnitTestUser");
        user.setName("UnitTestUser");
        return user;
    }

    public static <T> T readValue(Class<T> valueType, final String content, String... values)
    {
        try {
            JsonNode node = getJsonNode(content, values);
            if (node != null) {
                return readValue(valueType, node.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(Class<T> valueType, final String content) throws IOException {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null");
        }
        return objectMapper.readValue(content, valueType);
    }

    public static String writeObjectToJson(Object content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("Content is null");
        }

        return objectMapper.writeValueAsString(content);
    }

    public static JsonNode getJsonNode(final String jsonStr, final String... values) {
        if (StringUtils.isBlank(jsonStr)) {
            throw new IllegalArgumentException("jsonStr is null");
        }
        if (values == null || values.length <= 0) {
            throw new IllegalArgumentException("values is null");
        }

        JsonNode node = null;
        try {
            node = objectMapper.readTree(jsonStr);
            if (values != null && values.length > 0) {
                for (String v : values) {
                    node = node.path(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    public static DataCollection getDataCollection(String jsonObject)
    {
        if (StringUtils.isBlank(jsonObject)) {
            throw new IllegalArgumentException("jsonObject is null");
        }
        DataCollection dataCollection = null;
        try {
            JsonNode node = objectMapper.readTree(jsonObject);
            dataCollection = objectMapper.readValue(jsonObject, DataCollection.class);
        } catch (Exception e) {
            //logger.error("json转换对象失败,json字符串:{},exp:{}", jsonObject, e.toString());
            System.out.println(e);
        }
        return dataCollection;
    }

    //This is just for Unit Test code
    public static String getJsonNodeContentFromObject(final String jsonContent, final String nodeName)
    {
        String jsonNodeContent = null;
        try {
            JsonNode node = objectMapper.readTree(jsonContent);
            jsonNodeContent =  node.get(nodeName).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNodeContent;
    }

    public static String defaultJsonPath()
    {
        String defaultJsonPath = null;
        String usrHome = System.getProperty("user.home");
        String fileSeparator = System.getProperty("file.separator");
        defaultJsonPath = new StringBuffer().append(usrHome)
                .append(fileSeparator)
                .append("Json-Knowledge")
                .append(fileSeparator).toString();
        File file = new File(defaultJsonPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return defaultJsonPath;
    }

    public static String getJsonContentFromFile(String filePath)
    {
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try {
            File file = new File(filePath);
            System.out.println(filePath);
            if (!file.exists()) {
                System.err.println(filePath + " not exist!");
                return null;
            }
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                buffer.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        return buffer.toString();
    }
    //For Unit Test end
}