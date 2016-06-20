package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by gintong on 2016/6/14.
 */
@Service("tagServiceLocal")
public class TagServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(TagServiceLocal.class);

    @Resource
    private TagService tagService;

    @Resource
    private TagSourceService tagSourceService;

    //@Resource
    //private KnowledgeService knowledgeService;


    //TODO: this just test interface, need to delete before deploy to online system
    public List<Long> createTag(long userId,short tagType,String tagName) throws Exception
    {
        List<Long> tagIds = new ArrayList<Long>();
        List<Tag> tagList = this.tagService.getTagsByUserIdAppidTagType(userId, APPID, (long)sourceType);
        if (tagList != null && tagList.size() >= 5) {
            for (Tag tag : tagList) {
                tagIds.add(tag.getId());
            }
            return tagIds;
        }

        Tag tag = new Tag();
        tag.setAppId(APPID);
        tag.setTagType(tagType);
        tag.setTagName(tagName);
        tag.setUserId(userId);

        Long tagId = this.tagService.createTag(userId, tag);
        tagIds.add(tagId);
        return tagIds;
    }


    //End

    public InterfaceResult batchTags(KnowledgeService knowledgeService,long userId, String requestJson) throws Exception {
        logger.info("batchTags: {}", requestJson );
        if(StringUtils.isEmpty(requestJson)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<LinkedHashMap<String, Object>> tagItems =  KnowledgeUtil.readValue(List.class, requestJson);
        if (tagItems == null || tagItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        try {
            for (int index = 0; index < tagItems.size(); index++) {
                Map<String, Object> map = tagItems.get(index);
                //Set<String> set = map.keySet();
                String title = map.get("title").toString();
                long knowledgeId = Long.parseLong(map.get("id").toString());
                List<String> tagIds = (List<String>)map.get("tagIds");

                //Update knowledge Detail
                DataCollection data = knowledgeService.getKnowledge(knowledgeId,(short)-1);
                if (data == null) {
                    logger.error("can't find this knowledge by Id: {}, skip to add tag", knowledgeId);
                    continue;
                }

                List<Long> newTagIds = convertStToLong(tagIds);
                KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
                knowledgeDetail.setTags(newTagIds);

                //Update knowledge base
                KnowledgeBase knowledgeBase = data.getKnowledge();

                String tagStr = tagIds.toString();
                tagStr = tagStr.substring(1, tagStr.length()-1);
                if (tagStr.length() > 255) {
                    int lastIndex = tagStr.lastIndexOf(",");
                    tagStr = tagStr.substring(0, lastIndex-1);
                }
                knowledgeBase.setTags(tagStr);
                //knowledgeMysqlDao.update(knowledgeBase);

                knowledgeService.updateKnowledge(new DataCollection(knowledgeBase, knowledgeDetail));
                logger.info("batch tags to knowledge success!  knowledgeId: {}", knowledgeId);

                if (knowledgeDetail != null && knowledgeBase != null) {
                    for (Long tagId : newTagIds) {
                        TagSource tagSource = new TagSource();
                        tagSource.setUserId(userId);
                        tagSource.setAppId(APPID);
                        tagSource.setSourceTitle(title);
                        tagSource.setSourceId(knowledgeId);
                        //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                        tagSource.setSourceType(sourceType);
                        tagSource.setTagId(tagId);
                        tagSource.setCreateAt(new Date().getTime());
                        tagSourceService.createTagSource(tagSource);
                        logger.info("create tag source tagId:" + tagId + " knowledgeId: " + knowledgeId);
                    }
                }
                else {
                    logger.error("can't find this knowledge base or detail info, so skip add tag to this knowledge, knowledgeId: {}", knowledgeId);
                }
            }
        } catch (TagSourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }



    public MappingJacksonValue getTagListByIds(long userId,List<Long> tagIds) throws Exception
    {
        logger.error("tag list is: " + tagIds.toString());
        List<Tag> tags = tagService.getTags(-1L,tagIds);
        if (tags != null && tags.size() > 0) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(tags);
            SimpleFilterProvider filterProvider = KnowledgeUtil.tagFilterProvider(Tag.class.getName());
            mappingJacksonValue.setFilters(filterProvider);
            return mappingJacksonValue;
        }

        logger.error("Can't get any tags with given tag id");
        return new MappingJacksonValue(InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS));
    }

    public InterfaceResult getTagSourceCountByIds(long AppId,List<Long> tagIds) throws Exception
    {
        if (tagIds == null || tagIds.size() <= 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance("TagId List is null or size is 0!");
        }
        Map<Long,Integer> sourceMap = new HashMap<Long,Integer>(tagIds.size());
        for (long tagId : tagIds) {
            int count = tagSourceService.countTagSourcesByAppIdTagId(APPID, tagId);
            sourceMap.put(tagId, count);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(sourceMap);

    }

    public boolean saveTagSource(long userId, KnowledgeDetail knowledgeDetail)
    {
        List<Long> tagsList = knowledgeDetail.getTags();
        if (tagsList == null || tagsList.size() <= 0) {
            logger.error("directory List is empty, so skip to save..");
            return false;
        }

        try {
            for (int index = 0; index < tagsList.size(); index++) {
                logger.info("directoryId: {}", tagsList.get(index));
                Long tagId = Long.valueOf(tagsList.get(index));
                if (tagId > 0) {
                    TagSource tagSource = createTagSource(userId, tagId, knowledgeDetail);
                    tagSourceService.createTagSource(tagSource);
                }
                logger.info("tagId:" + tagId);
            }
        }
        catch (Exception ex) {
            logger.error("Save Tag info failed: {}" + ex.getMessage());
            ex.printStackTrace();
        }

        return true;
    }

    public boolean updateTagSource(long userId, KnowledgeDetail knowledgeDetail)
    {
        long knowledgeId = knowledgeDetail.getId();
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(removeTagsFlag){
                List<Long> tagsList = knowledgeDetail.getTags();
                if(tagsList != null){
                    for(Long tagId : tagsList){
                        if(tagId > 0){
                            TagSource tagSource = createTagSource(userId, tagId, knowledgeDetail);
                            tagSourceService.createTagSource(tagSource);
                            logger.info("tagId:"+tagId);
                        }
                    }
                }
            }
            else{
                logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
                return false;
            }
        }catch(Exception ex){
            logger.error("update tags remove failed...userid=" + userId + ", knowledgeId=" +knowledgeId);
            return false;
        }

        return true;
    }

    public boolean deleteTags(long userId, long knowledgeId)
    {
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(!removeTagsFlag){
                logger.error("tags remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
                return false;
            }
        }catch(TagSourceServiceException ex){
            logger.error("tags remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
            return false;
        }
        catch(Exception ex){
            logger.error("tags remove failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
            return false;
        }

        return true;
    }

    private TagSource createTagSource(long userId, Long tagId,KnowledgeDetail knowledge)
    {
        TagSource tagSource = new TagSource();
        tagSource.setUserId(userId);
        tagSource.setAppId(APPID);
        tagSource.setSourceId(knowledge.getId());
        tagSource.setSourceTitle(knowledge.getTitle());
        //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
        tagSource.setSourceType(sourceType);
        tagSource.setTagId(tagId);
        tagSource.setCreateAt(new Date().getTime());

        return tagSource;
    }
}
