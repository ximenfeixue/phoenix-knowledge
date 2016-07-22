package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.BasicUpdate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/6/1.
 */
public class KnowledgeMongoTest extends TestBase {

    private static long userId = 1234567L;
    private static String dataBase = "knowledge-test";

    @Autowired
    private KnowledgeMongoDao knowledgeMongoDao;

    @Test
    public void testInsertKnowledge()
    {
        DataCollect data = TestData.getDataCollect(userId, (short) 2, "insertKnowledge");
        Knowledge detail = data.getKnowledgeDetail();
        detail.setId(System.currentTimeMillis());
        Knowledge newDetail = null;
        try {
            newDetail = knowledgeMongoDao.insert(detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TestCase.assertNotNull(newDetail);
    }

    public static void updateKnowledge(Knowledge detail)
    {
    }

}
