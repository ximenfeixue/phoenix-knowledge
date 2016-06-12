package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.parasol.directory.exception.DirectorySourceServiceException;
import com.ginkgocap.parasol.directory.model.Directory;
import com.ginkgocap.parasol.directory.model.DirectorySource;
import com.ginkgocap.parasol.directory.service.DirectoryService;
import com.ginkgocap.parasol.directory.service.DirectorySourceService;
import com.ginkgocap.parasol.tags.exception.TagSourceServiceException;
import com.ginkgocap.parasol.tags.model.Tag;
import com.ginkgocap.parasol.tags.model.TagSource;
import com.ginkgocap.parasol.tags.service.TagService;
import com.ginkgocap.parasol.tags.service.TagSourceService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMongoDao;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.*;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeOtherService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Chen Peifeng on 2016/4/15.
 */

@Service("knowledgeOtherService")
public class KnowledgeOtherServiceImpl implements KnowledgeOtherService, KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(KnowledgeOtherServiceImpl.class);
    @Autowired
    private KnowledgeMysqlDao knowledgeMysqlDao;

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeCommonService knowledgeCommonService;

    @Autowired
    KnowledgeMongoDao knowledgeMongoDao;

    @Autowired
    private DirectorySourceService directorySourceService;

    @Autowired
    private DirectoryService directoryService;

    @Autowired
    private TagSourceService tagSourceService;

    @Autowired
    private TagService tagService;

    private int maxCount = 100;
    private int maxSize = 10;

    @Override
    public InterfaceResult collectKnowledge(long userId,long knowledgeId, short columnId) throws Exception {
        KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, columnId);
        if (knowledgeDetail == null) {
            InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }

        Query query = knowledgeColumnIdAndOwnerId(userId, knowledgeId, columnId);
        if (mongoTemplate.findOne(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect) == null) {
            KnowledgeCollect collect = new KnowledgeCollect();
            collect.setId(knowledgeCommonService.getKnowledgeSeqenceId());
            collect.setKnowledgeId(knowledgeId);
            collect.setColumnId(columnId);
            collect.setCreateTime(System.currentTimeMillis());
            collect.setKnowledgeTitle(knowledgeDetail.getTitle());
            collect.setOwnerId(userId);

            mongoTemplate.save(collect, Constant.Collection.KnowledgeCollect);

            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }

        return InterfaceResult.getSuccessInterfaceResultInstance("Have collected this knowledge!");

    }

    @Override
    public InterfaceResult deleteCollectedKnowledge(long ownerId,long knowledgeId,short columnId) throws Exception
    {
        Query query = knowledgeColumnIdAndOwnerId(ownerId, knowledgeId, columnId);
        KnowledgeCollect collect = mongoTemplate.findAndRemove(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        if (collect == null) {
            logger.error("delete knowledge collect error, no this knowledge collect or no permission to delete: ownerId: "+ ownerId+ " knowledgeId: " + knowledgeId + " columnId: "+columnId);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    @Override
    public List<KnowledgeCollect> myCollectKnowledge(long userId,short columnId,int start, int size) throws Exception
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(userId));
        if (columnId != -1) {
            query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        }
        long count = mongoTemplate.count(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);
        if (start >= count) {
            start = 0;
        }
        if (size > maxSize) {
            size = maxSize;
        }
        if (start+size > count) {
            size = (int)count - start;
        }

        query.skip(start);
        query.limit(size);
        List<KnowledgeCollect> collectedItem = mongoTemplate.find(query, KnowledgeCollect.class, Constant.Collection.KnowledgeCollect);

        return collectedItem;
    }

    @Override
    public InterfaceResult reportKnowledge(KnowledgeReport report) throws Exception
    {
        Query query = knowledgeColumnIdAndOwnerId(report.getUserId(), report.getKnowledgeId(), report.getColumnId());
        if (mongoTemplate.findOne(query, KnowledgeReport.class, Constant.Collection.KnowledgeReport) == null) {
            if (report.getCreateTime() <= 0) {
                report.setCreateTime(System.currentTimeMillis());
            }
            mongoTemplate.save(report, Constant.Collection.KnowledgeReport);
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
        }
        return InterfaceResult.getSuccessInterfaceResultInstance("Have reported this knowledge!");
    }

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

    public List<Long> createDirectory(long userId,short type,String directoryName) throws Exception
    {
        List<Long> directoryIds = new ArrayList<Long>();
        List<Directory> directoryList = directoryService.getDirectorysForRoot(APPID, userId, 3933392601350164L);
        if (directoryList != null && directoryList.size() >= 5) {
            for (Directory directory : directoryList) {
                directoryIds.add(directory.getId());
            }
            return directoryIds;
        }

        Directory directory = new Directory();
        directory.setUserId(userId);
        directory.setAppId(APPID);
        directory.setName(directoryName);
        directory.setTypeId(3933392601350164L);//This get from DB
        Long directoryId =  directoryService.createDirectoryForRoot(userId, directory);
        directoryIds.add(directoryId);
        return directoryIds;
    }
    //End

    public InterfaceResult batchTags(long userId, List<ResItem> tagItems) throws Exception {
        if (tagItems == null || tagItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        try {
            for (ResItem tagItem : tagItems) {
                String title = tagItem.getTitle();
                long knowledgeId = tagItem.getId();
                List<Long> tagIds = tagItem.getTagIds();

                //Update knowledge Detail
                KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, (short)-1);
                if (knowledgeDetail != null) {
                    knowledgeDetail.setTags(tagIds);
                    knowledgeMongoDao.update(knowledgeDetail);
                } else {
                    logger.error("can't find this knowledge by Id: {}", knowledgeId);
                }
                //Update knowledge base
                KnowledgeBase knowledgeBase = knowledgeMysqlDao.getByKnowledgeId(knowledgeId);
                if (knowledgeBase != null) {
                    knowledgeBase.setTags(tagIds.toString());
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
                        logger.info("tagId:" + tagId);
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

    public InterfaceResult batchCatalogs(long userId,List<ResItem> directoryItems) throws Exception {
        if (directoryItems == null || directoryItems.size() <= 0) {
            return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_EXCEPTION);
        }

        try {
            for (ResItem directory : directoryItems) {
                String title = directory.getTitle();
                long knowledgeId = directory.getId();
                List<Long> directoryIds = directory.getTagIds();

                //Update knowledge Detail
                KnowledgeDetail knowledgeDetail = knowledgeMongoDao.getByIdAndColumnId(knowledgeId, (short)-1);
                if (knowledgeDetail != null) {
                    knowledgeDetail.setCategoryIds(directoryIds);
                    knowledgeMongoDao.update(knowledgeDetail);
                } else {
                    logger.error("can't find this knowledge by Id: {}", knowledgeId);
                }

                if (knowledgeDetail != null) {
                    for (Long directoryId : directoryIds) {
                        DirectorySource directorySource = new DirectorySource();
                        directorySource.setUserId(userId);
                        directorySource.setDirectoryId(directoryId);
                        directorySource.setAppId(APPID);
                        directorySource.setSourceId(knowledgeId);
                        directorySource.setSourceTitle(title);
                        //source type 为定义的类型id:exp(用户为1,人脉为2,知识为3,需求为4,事务为5)
                        directorySource.setSourceType((int) sourceType);
                        directorySource.setCreateAt(new Date().getTime());
                        directorySourceService.createDirectorySources(directorySource);
                        logger.info("directoryId:" + directoryId);
                    }
                }
                else {
                    logger.error("can't find this knowledge detail info, so skip add directory to this knowledge, knowledgeId: {}", knowledgeId);
                }
            }
        } catch (DirectorySourceServiceException ex) {
            ex.printStackTrace();
        }

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public InterfaceResult getTagListByIds(long userId,List<Long> tagIds) throws Exception
    {
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
    }

    private Query knowledgeColumnIdAndOwnerId(long ownerId,long knowledgeId,short columnId)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where(Constant.OwnerId).is(ownerId));
        query.addCriteria(Criteria.where(Constant.KnowledgeId).is(knowledgeId));
        query.addCriteria(Criteria.where(Constant.ColumnId).is(columnId));
        return query;
    }
}
