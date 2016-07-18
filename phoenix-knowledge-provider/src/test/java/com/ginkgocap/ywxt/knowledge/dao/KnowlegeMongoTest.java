package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeDetail;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
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
public class KnowlegeMongoTest {

    private static long userId = 1234567L;
    private static String dataBase = "knowledge-test";

    private static MongoTemplate mongoTemplate;

    static {
        Mongo mongo = null;
        try {
            mongo = new Mongo("192.168.101.131");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MongoDbFactory mongoDbFactory =  getMongoDbFactory(mongo, dataBase);
        mongoTemplate = new MongoTemplate(mongoDbFactory, getDefaultMongoConverter(mongoDbFactory));
    }

    public static void main(String[] args) throws UnknownHostException {

        // For Annotation
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
    	//ApplicationContext ctx = new GenericXmlApplicationContext("classpath*:mongo-config.xml");
    	//mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");

        KnowledgeDetail knowledgeDetail = insertKnowledge();
        knowledgeDetail.setTitle("Update Title");
        List<Long> tagIds = new ArrayList<Long>(2);
        tagIds.add(123456L);
        tagIds.add(123458L);
        knowledgeDetail.setTags(tagIds);
        updateKnowledge(knowledgeDetail);
    }

    public static KnowledgeDetail insertKnowledge()
    {
        DataCollection data = TestData.getDataCollection(userId, (short)2, "insertKnowledge");
        KnowledgeDetail knowledgeDetail = data.getKnowledgeDetail();
        knowledgeDetail.setId(2L);
        mongoTemplate.insert(knowledgeDetail, "Knowledge");

        return knowledgeDetail;
    }
    public static void updateKnowledge(KnowledgeDetail knowledgeDetail)
    {

        Criteria c = Criteria.where(Constant._ID).is(knowledgeDetail.getId());
        Query query = new Query(c);

        KnowledgeDetail existValue = mongoTemplate.findOne(query, KnowledgeDetail.class, Constant.Collection.Knowledge);
        if (existValue != null) {
            mongoTemplate.save(knowledgeDetail, Constant.Collection.Knowledge);
        }
        else {
            System.err.println("can't find this knowledge, so skip update. knowledgeId: "+knowledgeDetail.getId());
        }
    }

    private static final MongoConverter getDefaultMongoConverter(MongoDbFactory factory) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        converter.afterPropertiesSet();
        return converter;
    }

    private static MongoDbFactory getMongoDbFactory(Mongo mongo, String databaseName)
    {
        return new SimpleMongoDbFactory(mongo, databaseName);
    }

    private static Update getUpdate(KnowledgeDetail knowledgeDetail) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("$set", knowledgeDetail);
        Update update = new BasicUpdate(basicDBObject);

        return update;
    }
}
