package com.ginkgocap.ywxt.knowledge.service;

import com.alibaba.dubbo.rpc.cluster.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
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

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by gintong on 2016/6/14.
 */
public class TagLocalService implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(TagLocalService.class);

    @Autowired
    private TagService tagService;

    @Autowired
    private TagSourceService tagSourceService;

    @Autowired
    //private KnowledgeMysqlDao knowledgeMysqlDao;

    @Resource
    private MongoTemplate mongoTemplate;

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
    /*
    public InterfaceResult batchTags(long userId, String requestJson) throws Exception {
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
                List<Long> tagIds = (List<Long>)map.get("tagIds");

                //Update knowledge Detail
                KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, (short)-1);
                if (knowledgeDetail != null) {
                    if (knowledgeDetail.getTags() == null) {
                        knowledgeDetail.setTags(tagIds);
                    } else {
                        List<Long> oldTags = knowledgeDetail.getTags();
                        oldTags.addAll(tagIds);
                    }
                    //knowledgeMongoDao.update(knowledgeDetail);
                    logger.info("add knowledge tag to Mongo knowledgeId: {}", knowledgeId);
                } else {
                    logger.error("can't find this knowledge by Id: {}", knowledgeId);
                }
                //Update knowledge base
                KnowledgeBase knowledgeBase = null; //knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
                if (knowledgeBase != null) {
                    String tagStr = tagIds.toString();
                    tagStr = tagStr.substring(1, tagStr.length()-1);
                    if (knowledgeBase.getTags() == null) {
                        knowledgeBase.setTags(tagStr);
                    }else {
                        String oldTags = knowledgeBase.getTags();
                        knowledgeBase.setTags(oldTags + "," + tagStr);
                    }
                    //knowledgeMysqlDao.update(knowledgeBase);
                    logger.info("add knowledge tag to mysql knowledgeId: {}", knowledgeId);
                } else {
                    logger.error("can't find this knowledge base info by Id: {}", knowledgeId);
                }

                if (knowledgeDetail != null && knowledgeBase != null) {
                    for (Long tagId : tagIds) {
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



    public InterfaceResult getTagListByIds(long userId,List<Long> tagIds) throws Exception
    {
        logger.error("tag list is: " + tagIds.toString());
        List<Tag> tags = tagService.getTags(-1L,tagIds);
        if (tags != null && tags.size() > 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance(tags);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Can't get any tags with given tag id");
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

    public InterfaceResult getDirectoryListByIds(long userId,List<Long> directoryIds) throws Exception
    {
        List<Directory> directoryList = directoryService.getDirectoryList(APPID, -1L, directoryIds);
        if (directoryList != null && directoryList.size() > 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance(directoryList);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Can't get any tags with given tag id");
    }

    public InterfaceResult getDirectorySourceCountByIds(long userId,List<Long> directoryIds) throws Exception
    {
        if (directoryIds == null || directoryIds.size() <= 0) {
            return InterfaceResult.getSuccessInterfaceResultInstance("directory List is null or size is 0!");
        }
        Map<Long,Integer> sourceMap = new HashMap<Long,Integer>(directoryIds.size());
        for (long directoryId : directoryIds) {
            int count = directorySourceService.countDirectorySourcesByDirectoryId(APPID, userId, directoryId);
            sourceMap.put(directoryId, count);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance(sourceMap);
    }*/
}
