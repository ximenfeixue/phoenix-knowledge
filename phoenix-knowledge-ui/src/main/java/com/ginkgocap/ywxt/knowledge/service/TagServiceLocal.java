package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.tags.exception.TagServiceException;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Chen Peifeng on 2016/6/14.
 */
@Service("tagServiceLocal")
public class TagServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(TagServiceLocal.class);

    @Resource
    private TagService tagService;

    @Resource
    private TagSourceService tagSourceService;


    //TODO: this just test interface, need to delete before deploy to online system
    public List<Long> createTag(long userId,String tagName) throws Exception
    {
        List<Long> tagIds = new ArrayList<Long>();
        try {
            List<Tag> tagList = this.tagService.getTagsByUserIdAppidTagType(userId, APPID, (long) sourceType);
            if (tagList != null && tagList.size() >= 5) {
                for (Tag tag : tagList) {
                    tagIds.add(tag.getId());
                }
                return tagIds;
            }
        } catch (TagServiceException ex) {
            logger.error("can't get tag list by userId: {} appId: {} error: ", userId, APPID, ex.getMessage());
        }

        Tag tag = new Tag();
        tag.setAppId(APPID);
        tag.setTagType(sourceType);
        tag.setTagName(tagName);
        tag.setUserId(userId);

        try {
            Long tagId = this.tagService.createTag(userId, tag);
            tagIds.add(tagId);
        } catch (TagServiceException ex) {
            logger.error("create tag failed!  userId: {}, tagName: {}", tag.getUserId(), tag.getTagName());
        }
        return tagIds;
    }
    //End

    public InterfaceResult batchTags(KnowledgeService knowledgeService,long userId, List<ResItem> tagItems) throws Exception
    {
        if (tagItems == null || tagItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "tagItems is null");
        }

        for (ResItem batchItem : tagItems) {
            long knowledgeId = batchItem.getResId();
            List<Long> tagIds = batchItem.getIds();

            DataCollection data = knowledgeService.getKnowledge(knowledgeId, (short) -1);
            if (data == null) {
                logger.error("can't find this knowledge by knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }

            KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
            if (knowledgeDetail == null) {
                logger.error("can't find this knowledge detail info, knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }

            KnowledgeBase knowledgeBase = data.getKnowledge();
            if (knowledgeBase == null) {
                logger.error("can't find this knowledge base info, knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }

            //First delete exist tag source
            if (deleteTagSourceByKnowledgeId(userId, knowledgeId)) {
                logger.error("delete old tags failed...userId: " + userId + ", knowledgeId: " + knowledgeId);
            }

            //add new tag to tag source services
            List<Long> successIds = createTagSource(userId, tagIds, knowledgeDetail);

            //Update knowledge Detail
            List<Long> existTags = knowledgeDetail.getTags();
            if (existTags == null || existTags.size() <= 0) {
                existTags = successIds;
            }
            else {
                existTags.addAll(successIds);
            }
            knowledgeDetail.setTags(existTags);

            //Update knowledge base
            String tagString = convertLongValueListToString(successIds, knowledgeBase.getTags());
            knowledgeBase.setTags(tagString);

            knowledgeService.updateKnowledge(new DataCollection(knowledgeBase, knowledgeDetail));
            logger.info("batch tags to knowledge success!  knowledgeId: {}", knowledgeId);
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult batchTags(KnowledgeService knowledgeService,long userId, String requestJson) throws Exception {
        logger.info("batchTags: {}", requestJson );
        if(StringUtils.isEmpty(requestJson)) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        List<LinkedHashMap<String, Object>> tagItems =  KnowledgeUtil.readValue(List.class, requestJson);
        if (tagItems == null || tagItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "tagItems is null");
        }

        int successResult = 0;
        int failedResult = 0;
        String errorMessage = "";
        for (int index = 0; index < tagItems.size(); index++) {
            Map<String, Object> map = tagItems.get(index);
            long knowledgeId = Long.parseLong(map.get("resId").toString());
            List<Object> tagIds = (List<Object>)map.get("ids");

            //tag id list check
            List<Long> newTagIds = convertObjectListToLongList(tagIds);
            if (newTagIds == null || newTagIds.size() <= 0) {
                logger.error("There is no valid and safe tag Id, so skip. knowledgeId: {}", knowledgeId);
                continue;
            }

            //Update knowledge Detail
            DataCollection data = knowledgeService.getKnowledge(knowledgeId,(short)-1);
            if (data == null) {
                logger.error("can't find this knowledge failed. knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }
            KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
            if (data.getKnowledgeDetail() == null) {
                logger.error("can't find this knowledge detail failed. knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }

            KnowledgeBase knowledgeBase = data.getKnowledge();
            if (knowledgeBase == null) {
                logger.error("can't find this knowledge base failed. knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }

            //First delete exist tag source
            /*if (!deleteTagSourceByKnowledgeId(userId, knowledgeId)) {
                logger.error("delete old tags failed...userId: " + userId + ", knowledgeId: " + knowledgeId);
            }*/

            logger.debug("create Tag for UserId: {}", userId);
            List<Long> successIds =  createTagSource(userId, newTagIds, knowledgeDetail);

            //Update knowledge base
            List<Long> existTagsIds = knowledgeDetail.getTags();
            if (existTagsIds == null || existTagsIds.size() <= 0) {
                existTagsIds = successIds;
            } else {
                if (existTagsIds.size() + newTagIds.size() > 10) {
                    errorMessage = tagLimitErrorMsg;
                }
                existTagsIds.addAll(successIds);
            }
            knowledgeDetail.setTags(existTagsIds);

            //Update knowledge base
            String tagString = convertLongValueListToString(successIds, knowledgeBase.getTags());
            knowledgeBase.setTags(tagString);

            knowledgeService.updateKnowledge(new DataCollection(knowledgeBase, knowledgeDetail));
            logger.info("batch tags to knowledge success!  knowledgeId: {}", knowledgeId);
            successResult = + successIds.size();
            failedResult = + newTagIds.size() - successIds.size();
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(batchResult(successResult,failedResult, errorMessage));
    }



    public MappingJacksonValue getTagListByIds(long userId,List<Long> tagIds) throws Exception
    {
        logger.error("tag list is: " + tagIds.toString());
        List<Tag> tags = tagService.getTags(-1L,tagIds);
        if (tags != null && tags.size() > 0) {
            InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance(tags);
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            SimpleFilterProvider filterProvider = KnowledgeUtil.tagFilterProvider(Tag.class.getName());
            mappingJacksonValue.setFilters(filterProvider);
            logger.info(".......Get Tag list success......");
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

        logger.info(".......Get Tag count success......");
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
                    TagSource tagSource = newTagSourceObject(userId, tagId, knowledgeDetail);
                    tagSourceService.createTagSource(tagSource);
                }
                logger.info("Save tag success tagId:" + tagId);
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

        List<Long> tagsList = knowledgeDetail.getTags();
        if(tagsList == null || tagsList.size() <= 0) {
            return false;
        }

        long knowledgeId = knowledgeDetail.getId();
        if (!deleteTagSourceByKnowledgeId(userId, knowledgeId)) {
            logger.error("delete tags failed...userId: " + userId + ", knowledgeId: " + knowledgeId);
        }

        for(Long tagId : tagsList){
            if(tagId > 0){
                TagSource tagSource = newTagSourceObject(userId, tagId, knowledgeDetail);
                try {
                    tagSourceService.createTagSource(tagSource);
                    logger.info("create tag success tagId:" + tagId);
                } catch (TagSourceServiceException ex) {
                    logger.error("update tags failed...userId: " + userId + " knowledgeId: " +knowledgeId + "error: "+ex.getMessage());
                }
                catch(Exception ex){
                    logger.error("update tags failed...userId: " + userId + " knowledgeId: " +knowledgeId + "error: "+ex.getMessage());
                }
            }
        }
        return true;
    }

    public boolean deleteTags(long userId, long knowledgeId)
    {
        try{
            boolean removeTagsFlag = tagSourceService.removeTagSource(APPID, userId, knowledgeId);
            if(!removeTagsFlag){
                logger.error("delete tags failed...userId=" + userId + ", knowledgeId=" + knowledgeId);
                return false;
            }
        }catch(TagSourceServiceException ex){
            logger.error("delete tag failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
            return false;
        }
        catch(Exception ex){
            logger.error("delete tag failed...userId=" + userId + ", knowledgeId=" + knowledgeId + "error: "+ex.getMessage());
            return false;
        }

        return true;
    }

    private List<Long> createTagSource(long userId, List<Long> tagIds, KnowledgeDetail knowledgeDetail)
    {
        logger.debug("create Tag for UserId: {}", userId);
        long knowledgeId = knowledgeDetail.getId();
        List<Long> successIds = new ArrayList<Long>(tagIds.size());
        for (Long tagId : tagIds) {
            TagSource tagSource = newTagSourceObject(userId, tagId, knowledgeDetail);
            try {
                logger.info("before create tag source. tagId:" + tagId + " knowledgeId: " + knowledgeId);
                tagSourceService.createTagSource(tagSource);
                successIds.add(tagId);
                logger.info("create tag source success. tagId:" + tagId + " knowledgeId: " + knowledgeId);
            }
            catch (TagSourceServiceException ex) {
                logger.error("create Tag failed, userId: {}, knowledgeId: {}, tagId: {}, error: {}",
                        userId, knowledgeId, tagId, ex.getMessage());
            }
        }

        return successIds;
    }

    public List<Long> getKnowlegeIdsByTagId(long tagId, int start, int size)
    {
        try {
            List<TagSource> tagSources = tagSourceService.getTagSourcesByAppIdTagIdAndType(APPID, tagId, (long)sourceType, start, size );
            if (tagSources != null && tagSources.size() > 0) {
                List<Long> knowledgeIds = new ArrayList<Long>(tagSources.size());
                for (TagSource tag : tagSources) {
                    knowledgeIds.add(tag.getSourceId());
                }
                return knowledgeIds;
            }
            else {
                logger.error("There is no any resource add this tag, tagId: {}", tagId);
            }
        } catch (TagSourceServiceException ex) {
            logger.error("Get related resource failed. tagId: {} error: ", tagId, ex.getMessage());
        }
        return null;
    }

    private TagSource newTagSourceObject(long userId, Long tagId, KnowledgeDetail knowledge)
    {
        TagSource tagSource = new TagSource();
        tagSource.setUserId(userId);
        tagSource.setAppId(APPID);
        tagSource.setSourceId(knowledge.getId());
        tagSource.setSourceTitle(knowledge.getTitle());
        tagSource.setSourceType(sourceType);//source type 为定义的类型id:exp(用户为1,人脉为2,需求为7,知识为8,事务为5)
        tagSource.setTagId(tagId);
        tagSource.setCreateAt(new Date().getTime());

        return tagSource;
    }

    private boolean deleteTagSourceByKnowledgeId(long userId, long knowledgeId)
    {
        List<TagSource> tagSources = null;
        try {
            tagSources = tagSourceService.getTagSourcesByAppIdSourceIdSourceType(APPID, knowledgeId, (long)sourceType);
        }
        catch (TagSourceServiceException ex) {
            logger.error("get tags from this knowledge, failed...userId: " + userId + ", knowledgeId: " +knowledgeId + "error: "+ex.getMessage());
            return false;
        }

        if (tagSources == null || tagSources.size() <= 0) {
            logger.warn("no tags add to this knowledge...userId: " + userId + ", knowledgeId: " + knowledgeId);
            return false;
        }

        for (TagSource tagSource : tagSources) {
            long tagSourceId = tagSource.getId();
            try {
                if (!tagSourceService.removeTagSource(APPID, userId, tagSourceId)) {
                    logger.error("delete tags failed...userId: " + userId + ", tagSourceId: " + tagSource.getId());
                }
            } catch (TagSourceServiceException ex) {
                logger.error("delete tags failed...userId: " + userId + ", tagSourceId: " +tagSourceId + "error: "+ex.getMessage());
            }
        }
        return true;
    }
}
