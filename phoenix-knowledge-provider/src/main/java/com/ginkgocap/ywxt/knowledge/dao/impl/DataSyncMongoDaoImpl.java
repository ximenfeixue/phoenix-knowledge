package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.DataSyncMongoDao;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by gintong on 2016/7/9.
 */
@Repository("dataSyncMongoDao")
public class DataSyncMongoDaoImpl implements DataSyncMongoDao {
    private Logger logger = LoggerFactory.getLogger(DataSyncMongoDaoImpl.class);
    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    private KnowledgeIdService knowledgeIdService;

    private final static String collectionName = "DataSync";

    private final int maxSize = 50;

    @Override
    public long saveDataSync(DataSync data) {
        try {
            long id = knowledgeIdService.getUniqueSequenceId("4");
            data.setId(id);
            mongoTemplate.save(data, collectionName);
            return id;
        } catch (Exception ex) {
            logger.error("save datasync failed: id: " + data.getId() + " error: ", ex.getMessage());
        }
        return -1;
    }

    @Override
    public boolean deleteDataSync(final long id)
    {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(Constant._ID).is(id));
            DataSync data = mongoTemplate.findAndRemove(query, DataSync.class, collectionName);
            return data != null;
        }
        catch (Exception ex) {
            logger.error("delete dataSync failed: id:" + id + " error: " + ex.getMessage());
            return false;
        }
    }

    public List<DataSync> getDataSyncList(long fromIndex)
    {
        Query query = queryLimit();
        if (fromIndex > 0) {
            query.addCriteria(Criteria.where(Constant._ID).lt(fromIndex));
            logger.info("query from id : " + fromIndex);
        }
        return mongoTemplate.find(query, DataSync.class, collectionName);
    }

    public List<DataSync> getDataSyncListByTime(long time)
    {
        Query query =  queryLimit();
        query.addCriteria(Criteria.where(Constant.TIME).lt(time));
        return mongoTemplate.find(query, DataSync.class, collectionName);
    }

    private Query queryLimit() {
        Query query = new Query();
        query.skip(0);
        query.limit(maxSize);
        query.with(new Sort(Sort.Direction.DESC, Constant._ID));
        return query;
    }
}
