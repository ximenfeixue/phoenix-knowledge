package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.ywxt.knowledge.dao.DataSyncMongoDao;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private KnowledgeCommonService knowledgeCommonService;

    private final int maxSize = 50;

    private final String tbName = DataSync.class.getSimpleName();

    @Override
    public boolean saveDataSync(DataSync data) {
        try {
            //long id = knowledgeCommonService.getKnowledgeSequenceId();
            //data.setId(id);
            mongoTemplate.save(data, collectionName());
        } catch (Throwable ex) {
            logger.error("save datasync failed: data: " + data + " error: ", ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteDataSync(final long id)
    {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where(Constant._ID).is(id));
            DataSync data = mongoTemplate.findAndRemove(query, DataSync.class, tbName);
            return data != null;
        }
        catch (Throwable ex) {
            logger.error("delete dataSync failed: id:" + id + " error: " + ex.getMessage());
            return false;
        }
    }

    public List<DataSync> getDataSyncList()
    {
        Query query = new Query();
        query.skip(0);
        query.limit(maxSize);
        return mongoTemplate.find(query, DataSync.class, collectionName());
    }


    private String collectionName()
    {
        return "DataSync";
    }
}
