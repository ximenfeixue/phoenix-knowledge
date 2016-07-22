package com.ginkgocap.ywxt.knowledge.base;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {

    private final String database = "knowledge-test";
    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public @Bean Mongo mongo() throws Exception {
        return new Mongo("192.168.120.135");
    }
    @Override
    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), database);
    }
}