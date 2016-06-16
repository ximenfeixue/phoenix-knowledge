package com.ginkgocap.ywxt.knowledge.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.ywxt.user.model.User;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 2016/3/24.
 */
public final class KnowledgeUtil {

    private static final boolean writeNumberAsString = false;;
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
        user.setId(201003L);
        user.setUid(201003L);
        user.setUserName("UnitTestUser");
        user.setName("UnitTestUser");
        return user;
    }

    public static <T> T readValue(Class<T> valueType, final String jsonContent, String... values)
    {
        return readValue(null, valueType, jsonContent, values);
    }

    public static <T> T readValue(final FilterProvider filterProvider, Class<T> valueType, final String content, String... values)
    {
        try {
            JsonNode node = getJsonNode(content, values);
            if (node != null) {
                return readValue(filterProvider, valueType, node.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T readValue(Class<T> valueType, final String jsonContent) {
        return readValue(null, valueType, jsonContent);
    }
    public static <T> T readValue(final FilterProvider filterProvider,Class<T> valueType, final String jsonContent) {
        if (StringUtils.isBlank(jsonContent)) {
            throw new IllegalArgumentException("Content is null");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (filterProvider != null) {
                objectMapper.setFilters(filterProvider);
            }
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            return objectMapper.readValue(jsonContent, valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T readValue(TypeReference javaType, final String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            return objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> readListValue(Class<T> valueType, final String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null");
        }
        try {
            TypeReference javaType = new TypeReference<List<T>>(){};
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            return objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JsonNode readTree(final String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("Content is null");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        try {
            return objectMapper.readTree(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String writeObjectToJson(final Object jsonContent)
    {
        return writeObjectToJson(null, jsonContent);
    }

    public static String writeObjectToJson(final FilterProvider filterProvider, final Object jsonContent)
    {
        if (jsonContent == null) {
            throw new IllegalArgumentException("Content is null");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            if (filterProvider != null) {
                objectMapper.setFilters(filterProvider);
            }
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            return objectMapper.writeValueAsString(jsonContent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            JsonNode node = objectMapper.readTree(jsonObject);
            dataCollection = objectMapper.readValue(jsonObject, DataCollection.class);
        } catch (Exception e) {
            //logger.error("json转换对象失败,json字符串:{},exp:{}", jsonObject, e.toString());
            System.out.println(e);
        }
        return dataCollection;
    }

    public static SimpleFilterProvider assoFilterProvider(final String className) {
        Set<String> filter = new HashSet<String>(8); //this number must be increased by fields
        filter.add("id"); // id',
        filter.add("appId");
        filter.add("assocTypeId");
        filter.add("assocDesc"); // '关联描述，比如文章的作者，或者编辑等；关联标签描述',
        filter.add("assocTypeId"); // '被关联的类型可以参考AssociateType对象，如：知识, 人脉,组织，需求，事件等',
        filter.add("assocId"); // '被关联数据ID',
        filter.add("assocTitle"); // '被关联数据标题',
        filter.add("assocMetadata"); // '被关联数据的的摘要用Json存放，如图片，连接URL定义等',

        return simpleFilterProvider(className, filter);
    }

    public static SimpleFilterProvider tagFilterProvider(final String className) {
        Set<String> filter = new HashSet<String>(6); //this number must be increased by fields
        filter.add("id"); // id',
        filter.add("tagType"); // tagType',
        filter.add("tagName"); // Tag名称

        return simpleFilterProvider(className, filter);
    }

    public static SimpleFilterProvider directoryFilterProvider(final String className) {
        Set<String> filter = new HashSet<String>(6); //this number must be increased by fields
        filter.add("id"); // id',
        filter.add("name"); // '分类名称',
        filter.add("typeId"); // '应用的分类分类ID',
        filter.add("appId"); // '应用的分类分类ID',
        filter.add("userId"); // '应用的分类分类ID',

        return simpleFilterProvider(className, filter);
    }

    public static SimpleFilterProvider simpleFilterProvider(final String className, final Set<String> filter) {
        if (filter != null && filter.size() > 0) {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter(className, SimpleBeanPropertyFilter.filterOutAllExcept(filter));
            return filterProvider;
        }
        else {
            return null;
        }
    }

    public static SimpleFilterProvider simpleFilterProvider(final String className, final String fileds) {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        // 请求指定字段
        String[] filedNames = StringUtils.split(fileds, ",");
        Set<String> filter = null;
        if (filedNames != null && filedNames.length > 0) {
            filter = new HashSet<String>(filedNames.length);
            for (int i = 0; i < filedNames.length; i++) {
                String filedName = filedNames[i];
                if (!StringUtils.isEmpty(filedName)) {
                    filter.add(filedName);
                }
            }
        }

        filterProvider.addFilter(className, SimpleBeanPropertyFilter.filterOutAllExcept(filter));
        return filterProvider;
    }

    //This is just for Unit Test code
    public static String getJsonNodeContentFromObject(final String jsonContent, final String nodeName)
    {
        String jsonNodeContent = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
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

    /**
     * 返回数据包装方法
     * @param knowledgeList
     * @return
     */
    public static List<DataCollection> getReturn(List<KnowledgeBase> knowledgeList) {

        List<DataCollection> returnList = new ArrayList<DataCollection>(knowledgeList.size());
        if(knowledgeList != null && !knowledgeList.isEmpty())
            for (KnowledgeBase data : knowledgeList)
                returnList.add(getReturn(data,null));

        return returnList;
    }

    /**
     * 返回数据包装方法
     * @param knowledgeBase
     * @param knowledgeReference
     * @return
     */
    public static DataCollection getReturn(KnowledgeBase knowledgeBase, KnowledgeReference knowledgeReference) {

        DataCollection dataCollection = new DataCollection();

        dataCollection.setKnowledge(knowledgeBase);

        dataCollection.setReference(knowledgeReference);

        return dataCollection;
    }
    //For Unit Test end
}
