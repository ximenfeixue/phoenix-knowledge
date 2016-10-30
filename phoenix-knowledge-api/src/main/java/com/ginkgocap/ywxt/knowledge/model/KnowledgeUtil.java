package com.ginkgocap.ywxt.knowledge.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.utils.StringUtil;
import com.ginkgocap.ywxt.user.model.User;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Admin on 2016/3/24.
 */
public final class KnowledgeUtil {
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeUtil.class);

    public static Class getKnowledgeClassType(int columnId)
    {
        return KnowledgeType.knowledgeType(columnId).cls();
    }

    /*
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

    public static String getKnowledgeCollectionName(int columnId)
    {
        return KnowledgeType.knowledgeType(columnId).tableName();
    }

    public static String getKnowledgeTypeName(int columnId)
    {
        return KnowledgeType.knowledgeType(columnId).typeName();
    }

    //This is just for test, need do knowledge test but real user not exist
    public static User getDummyUser()
    {
        User user = new User();
        user.setId(200562L);
        user.setUid(200562L);
        user.setUserName("LiuSuo");
        user.setName("LiuSuo");
        return user;
    }

    public static <T> T readValue(Class<T> valueType, final String jsonContent, String... values)
    {
        return readValue(null, valueType, jsonContent, values);
    }

    public static <T> T readValue(final FilterProvider filterProvider, Class<T> valueType, final String content, String... values)
    {
        JsonNode node = getJsonNode(content, values);
        return readValue(filterProvider, valueType, node.toString());

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
        } catch(JsonParseException ex) {
            ex.printStackTrace();
        } catch(JsonMappingException ex) {
            ex.printStackTrace();
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
            //TypeReference javaType = new TypeReference<List<T>>(){};
        	ObjectMapper objectMapper = new ObjectMapper();
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, valueType);
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

    public static DataCollect getDataCollect(String jsonObject)
    {
        if (StringUtils.isBlank(jsonObject)) {
            throw new IllegalArgumentException("jsonObject is null");
        }
        DataCollect dataCollection = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            JsonNode node = objectMapper.readTree(jsonObject);
            dataCollection = objectMapper.readValue(jsonObject, DataCollect.class);
        } catch (Exception e) {
            logger.error("json转换对象失败,json字符串:{},exp:{}", jsonObject, e.toString());
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

    public static List<Long> convertStringToLongList(String ids)
    {
        if (StringUtils.isEmpty(ids)) {
            return null;
        }
        List<Long> idList = null;
        if (ids.indexOf(",") <= 0) {
            idList = new ArrayList<Long>(1);
            idList.add(convertStringToLong(ids));
        }
        //TODO: performance bad, need to change it.
        String[] idStr = ids.split(",");
        idList = new ArrayList<Long>(idStr.length);
        for (String id : idStr) {
            try {
                if (id == null) {
                    logger.error("id is null.");
                    continue;
                }
                long newId =  Long.parseLong(id.trim());
                if(newId > 0) {
                    idList = new ArrayList<Long>(1);
                    idList.add(newId);
                }
            } catch (NumberFormatException ex) {
                logger.error("parse String to long error: " + ids);
                ex.printStackTrace();
            }
        }
        return idList;
    }

    public static long convertStringToLong(String number)
    {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException ex) {
            logger.error("parse String to long error: " + number);
            ex.printStackTrace();
            return -1;
        }
    }

    public static String convertLongListToBase(List<Long> ids) {
        if (ids == null || ids.size() <= 0) {
            return null;
        }

        StringBuffer tagIds = new StringBuffer();
        for (int index = 0; index < 8 && index < ids.size(); index++) {
            tagIds.append(String.valueOf(ids.get(index)));
            tagIds.append(",");
        }
        tagIds.deleteCharAt(tagIds.lastIndexOf(","));
        return tagIds.toString();
    }

    public static String convertTagsToBase(String tags)
    {
        if (StringUtils.isEmpty(tags)) {
            return null;
        }

        if (tags.length() > 255) {
            tags = tags.substring(0, 255);
            tags = tags.substring(0, tags.lastIndexOf(",")-1);
        }
        return tags;
    }

    public static String removeBlankOfTags(String tags)
    {
        if (tags.indexOf(" ") > 0) {
            return tags.replace(" ", ",");
        }
        return tags;
    }

    public static short parserShortType(String columnType)
    {
        //Default is 1
        short newColumnId = 1;
        if (StringUtils.isEmpty(columnType)) {
            return newColumnId;
        }

        try {
            newColumnId = Short.parseShort(columnType);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            logger.error("Parser columnType error. columnType: {}", columnType);
            return newColumnId;
        }

        return newColumnId;
    }

    public static int parserColumnId(String columnId)
    {
        int newColumnId = 1;
        if (StringUtils.isEmpty(columnId)) {
            return newColumnId;
        }

        try {
            newColumnId = Integer.parseInt(columnId);
        } catch (NumberFormatException ex) {
            logger.error("the convert columnId is invalidated. :" + columnId);
            return newColumnId;
        }

        return newColumnId;
    }

    public static long parserStringIdToLong(String id)
    {
        long newId = -1L;
        if (StringUtils.isEmpty(id)) {
            return newId;
        }

        try {
            newId = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        return newId;
    }

    public static long parserTimeToLong(String time)
    {
        long newTime = System.currentTimeMillis();
        if (StringUtils.isBlank(time)) {
            return newTime;
        }

        if (time.indexOf("-") > 0) {
            try {
                SimpleDateFormat sdf = null;
                if ( time.indexOf(":") > 0) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                }
                Date date = sdf.parse(time);
                if (date != null) {
                    return date.getTime();
                }
            } catch (Exception ex) {
                logger.error("Convert String to long error :"+ex.getMessage());
            }
        }

        try {
            newTime = Long.valueOf(time);
        } catch (NumberFormatException ex) {
            logger.error("Convert String to long error :"+ex.getMessage());
        }
        return newTime;
    }

    public static String parserTimeToUCT(String time)
    {
        if (StringUtils.isBlank(time)) {
            return String.valueOf(System.currentTimeMillis());
        }

        if (time.indexOf("-") > 0 && time.indexOf(":") > 0) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(time);
                if (date != null) {
                    return String.valueOf(date.getTime());
                }
            } catch (Exception ex) {
                logger.error("Convert String to long error :"+ex.getMessage());
            }
        }

        logger.info("The final UCT time: {}", time);
        return time;
    }

    //This only when update knowledge can use it.
    /*
    public static int [] getColumnType(final String columnType)
    {
        if (!StringUtil.inValidString(columnType)) {
            String tempString = columnType;
            int mid = tempString.indexOf(",");
            if (mid > 0) {
                String newType = tempString.substring(0, mid);
                String oldType = tempString.substring(mid + 1, tempString.length());
                return new int[]{KnowledgeUtil.parserColumnId(newType), KnowledgeUtil.parserColumnId(oldType)};
            }
            return new int[]{KnowledgeUtil.parserColumnId(columnType)};
        }
        return new int[]{KnowledgeType.ENews.value()};
    }*/

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
            if (!file.exists()) {
                logger.error(filePath + " not exist!");
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

    public static List<DataCollect> getDataCollectReturn(List<KnowledgeBase> knowledgeList) {

        List<DataCollect> returnList = new ArrayList<DataCollect>(knowledgeList.size());
        if(knowledgeList != null && !knowledgeList.isEmpty())
            for (KnowledgeBase data : knowledgeList)
                returnList.add(getDataCollectReturn(data, null));

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

    public static DataCollect getDataCollectReturn(KnowledgeBase knowledgeBase, KnowledgeReference knowledgeReference) {

        DataCollect data = new DataCollect();

        data.setKnowledge(knowledgeBase);

        data.setReference(knowledgeReference);

        return data;
    }
    //For Unit Test end
}
