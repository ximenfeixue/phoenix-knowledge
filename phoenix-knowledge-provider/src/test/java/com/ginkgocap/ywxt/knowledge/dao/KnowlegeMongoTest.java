package com.ginkgocap.ywxt.knowledge.dao;

import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.common.Constant;
import com.mongodb.Mongo;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Admin on 2016/6/1.
 */
public class KnowlegeMongoTest {

    private static String dataBase = "knowledge-test";
    private static MongoTemplate mongoTemplate = null;
    static {
        Mongo mongo = null;
        try {
            mongo = new Mongo("192.168.120.131");
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

        getKnowledgeCommentList(1234567L);
    }

    public static List<KnowledgeComment> getKnowledgeCommentList(Long knowledgeId)
    {
        if(knowledgeId == null){
            return null;
        }

        Criteria c = Criteria.where(Constant.KnowledgeId).is(knowledgeId);
        Query query = new Query(c);

        return mongoTemplate.find(query, KnowledgeComment.class, Constant.Collection.KnowledgeComment);
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
}
