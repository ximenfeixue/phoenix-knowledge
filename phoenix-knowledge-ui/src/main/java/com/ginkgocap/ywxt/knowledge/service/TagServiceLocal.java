package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.ginkgocap.parasol.tags.exception.TagServiceException;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.ResItem;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.xml.ws.Service;
import java.util.*;

/**
 * Created by gintong on 2016/7/19.
 */
@Repository("tagServiceLocal")
public class TagServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private final Logger logger = LoggerFactory.getLogger(TagServiceLocal.class);

    @Resource
    private TagService tagService;

    @Resource
    private TagSourceService tagSourceService;

    //TODO: this just test interface, need to delete before deploy to online system
    public long createTag(final long userId, final String tagName) throws Exception
    {
        Tag tag = new Tag();
        tag.setAppId(APPID);
        tag.setTagType(sourceType);
        tag.setTagName(tagName);
        tag.setUserId(userId);

        try {
            return this.tagService.createTag(userId, tag);
        } catch (TagServiceException ex) {
            logger.error("create tag failed!  userId: {}, tagName: {}", tag.getUserId(), tag.getTagName());
        }
        return -1L;
    }

    public List<Tag> getTagList(final long userId) throws Exception
    {
        try {
            return this.tagService.getTagsByUserIdAppidTagType(userId, APPID, (long) sourceType);
        } catch (TagServiceException ex) {
            logger.error("can't get tag list by userId: {} appId: {} error: ", userId, APPID, ex.getMessage());
        }

        return null;
    }
    //End

    public InterfaceResult batchTags(KnowledgeService knowledgeService,final long userId, final List<ResItem> tagItems) throws Exception
    {
        if (tagItems == null || tagItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION, "tagItems is null");
        }

        for (ResItem batchItem : tagItems) {
            long knowledgeId = batchItem.getResId();
            short type = batchItem.getType();
            List<Long> tagIds = batchItem.getIds();

            //Update knowledge Detail
            DataCollect data = null;
            try {
                data = knowledgeService.getKnowledge(knowledgeId, type);
            } catch (Exception ex) {
                logger.error("find knowledge failed. knowledgeId: {}, error: {}", knowledgeId, ex.getMessage());
                continue;
            }

            Knowledge knowledgeDetail = data.getKnowledgeDetail();
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
            InterfaceResult result = createTagSource(userId, tagIds, knowledgeDetail);
            if (!"0".equals(result.getNotification().getNotifCode())) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION,"标签添加全部失败: "+result.getNotification().getNotifInfo());
            }
            List<Long> successIds = (List<Long>)result.getResponseData();

            //Update knowledge Detail
            List<Long> existTags = knowledgeDetail.getTagList();
            if (CollectionUtils.isEmpty(existTags)) {
                existTags = successIds;
            }
            else {
                existTags.addAll(successIds);
            }
            knowledgeDetail.setTagList(existTags);

            //Update knowledge base
            String tagString = convertLongValueListToString(successIds, knowledgeBase.getTags());
            knowledgeBase.setTags(tagString);

            knowledgeService.updateKnowledge(new DataCollect(knowledgeBase, knowledgeDetail));
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
        boolean overMaxLimit = false;
        for (int index = 0; index < tagItems.size(); index++) {
            Map<String, Object> map = tagItems.get(index);
            Object resId = map.get("resId");
            if (resId == null) {
                logger.error("Can't get knowledge Id, so skip..");
                continue;
            }
            Object type = map.get("type");
            if (type == null) {
                logger.error("Can't get knowledge type, so skip..");
                //continue;
                //TODO: need remove
                type = "1";
            }
            Object ids = map.get("ids");
            if (ids == null) {
                logger.error("Can't get tag ids, so skip..");
                continue;
            }

            long knowledgeId = KnowledgeUtil.parserStringIdToLong(resId.toString());
            List<Object> tagIds = (List<Object>)ids;

            //tag id list check
            List<Long> newTagIds = convertObjectListToLongList(tagIds);
            if (newTagIds == null || newTagIds.size() <= 0) {
                logger.error("There is no valid and safe tag Id, so skip. knowledgeId: {}", knowledgeId);
                continue;
            }

            //Update knowledge Detail
            DataCollect data = null;
            try {
                short columnType = KnowledgeUtil.parserShortType(type.toString());
                data = knowledgeService.getKnowledge(knowledgeId, columnType);
            } catch (Exception ex) {
                logger.error("find knowledge failed. knowledgeId: {}, error: {}", knowledgeId, ex.getMessage());
            }
            if (data == null) {
                logger.error("can't find knowledge. knowledgeId: {}, skip to add tag", knowledgeId);
                continue;
            }
            Knowledge knowledgeDetail = data.getKnowledgeDetail();
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
            InterfaceResult result = createTagSource(userId, newTagIds, knowledgeDetail);
            if (!"0".equals(result.getNotification().getNotifCode())) {
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, "标签添加全部失败: " + result.getNotification().getNotifInfo());
            }
            List<Long> successIds = (List<Long>) result.getResponseData();
            //Update knowledge base
            List<Long> existTagsIds = knowledgeDetail.getTagList();
            if (existTagsIds == null || existTagsIds.size() <= 0) {
                existTagsIds = successIds;
            } else if (successIds != null && successIds.size() > 0) {
                existTagsIds.addAll(successIds);
            }
            knowledgeDetail.setTagList(existTagsIds);

            //Update knowledge base
            String tagString = convertLongValueListToString(existTagsIds);
            knowledgeBase.setTags(tagString);
            try {
                knowledgeService.updateKnowledge(new DataCollect(knowledgeBase, knowledgeDetail));
                logger.info("batch tags to knowledge success!  knowledgeId: {}", knowledgeId);
            } catch (Exception ex) {
                logger.info("batch tags to knowledge failed!  knowledgeId: {} error: {}", knowledgeId, ex.getMessage());
            }
            successResult = + successIds.size();
            failedResult = + newTagIds.size() - successIds.size();
        }

        return batchResult(successResult, failedResult, overMaxLimit);
    }

    public MappingJacksonValue getTagListByIds(long userId,List<Long> tagIds) throws Exception
    {
        logger.error("tag list is: " + tagIds.toString());
        List<Tag> tags = getTagList(userId, tagIds);
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

    public List<Tag> getTagList(long userId,List<Long> tagIds)
    {
        try {
            return tagService.getTags(-1L,tagIds);
        } catch (TagServiceException ex) {
            logger.error("get tag failed: {}", tagIds);
        }
        return null;
    }

    public InterfaceResult getTagSourceCountByIds(long AppId,List<Long> tagIds) throws Exception
    {
        if (tagIds == null || tagIds.size() <= 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance("TagId List is null or size is 0!");
        }
        Map<Long,Integer> sourceMap = new HashMap<Long,Integer>(tagIds.size());
        for (Long tagId : tagIds) {
            try {
                int count = tagSourceService.countTagSourcesByAppIdTagId(APPID, tagId);
                sourceMap.put(tagId, count);
            } catch (TagSourceServiceException ex) {
                logger.error("get tag count failed. error: {}", ex.getMessage());
                ex.printStackTrace();
            }
        }

        logger.info(".......Get Tag count success......");
        return InterfaceResult.getSuccessInterfaceResultInstance(sourceMap);

    }

    public boolean saveTagSource(long userId, Knowledge detail)
    {
        List<Long> tagsList = detail.getTagList();
        if (tagsList == null || tagsList.size() <= 0) {
            logger.error("tag List is empty, so skip to save..");
            return false;
        }

        for (int index = 0; index < tagsList.size(); index++) {
            logger.info("tagId: {}", tagsList.get(index));
            if (tagsList.get(index) == null || !(tagsList.get(index) instanceof Long)){
                logger.error("tagId: {} is invalidated, so skip to add to knowledge, id: {}", tagsList.get(index), detail.getId());
                continue;
            }

            try {
                Long tagId = tagsList.get(index);
                if (tagId > 0) {
                    TagSource tagSource = newTagSourceObject(userId, tagId, detail);
                    tagSourceService.createTagSource(tagSource);
                }
                logger.info("Save tag success tagId:" + tagId);
            }
            catch (Throwable ex) {
                logger.error("Save Tag info failed: {}" + ex.getMessage());
            }
        }

        return true;
    }

    public boolean updateTagSource(long userId, Knowledge knowledgeDetail)
    {

        List<Long> tagsList = knowledgeDetail.getTagList();
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
            int removeTagsFlag = tagSourceService.removeTagSourceBySourceId(APPID, userId, knowledgeId, (long)sourceType);
            if(removeTagsFlag <= 0){
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

    private InterfaceResult createTagSource(long userId, List<Long> tagIds, Knowledge knowledgeDetail)
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
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SERVICES_EXCEPTION, ex.getMessage());
            }
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(successIds);
    }

    public List<Long> getKnowledgeIdsByTagId(long tagId, int start, int size)
    {
        try {
            List<TagSource> tagSources = tagSourceService.getTagSourcesByAppIdTagIdAndType(APPID, tagId, (long)sourceType, start, size);
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

    public void tagCleanUp(long userId)
    {
        List<Tag> tagList = null;
        try {
            tagList = tagService.getTagsByUserIdAppidTagType(userId, APPID, (long)sourceType);
        } catch (TagServiceException e) {
            logger.info("Get Tag failed: error: {}", e.getMessage());
        }

        //remove tag source
        if (tagList != null && tagList.size() > 0) {
            logger.info("Get Tag Size: {}", tagList.size());
            for (Tag tag : tagList) {
                try {
                    boolean ret = tagSourceService.removeTagSourcesByTagId(APPID, tag.getId());
                    if (!ret) {
                        logger.info("remove Tag source failed: tagId: {}", tag.getId());
                    }
                } catch (TagSourceServiceException ex) {
                    logger.info("remove Tag source failed: tagId: {} error: {}", tag.getId(), ex.getMessage());
                }
            }
        }

        //remove tag
        for (Tag tag : tagList) {
            try {
                boolean ret = tagService.removeTag(userId, tag.getId());
                if (!ret) {
                    logger.info("remove Tag failed: tagId: {}", tag.getId());
                }
            } catch (Exception ex) {
                logger.info("remove Tag failed: tagId: {} error: {}", tag.getId(), ex.getMessage());
            }
        }
    }

    private TagSource newTagSourceObject(long userId, Long tagId, Knowledge knowledge)
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

    public MappingJacksonValue convertInterfaceResult(List<Tag> tags) {
        InterfaceResult result = InterfaceResult.getSuccessInterfaceResultInstance(tags);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
        SimpleFilterProvider filterProvider = KnowledgeUtil.tagFilterProvider(Tag.class.getName());
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }
}