package com.ginkgocap.ywxt.knowledge.test.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ginkgocap.ywxt.knowledge.model.DataCollection;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeComment;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.utils.TestData;
import com.ginkgocap.ywxt.knowledge.utils.TestDataUtil;
import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import java.io.IOException;

/**
 * Created by Chen Peifeng on 2016/1/23.
 */
public class ParserJsonTest extends BaseTestCase {

    private static boolean saveToDB = false;
    private static boolean createTestData = true;
    private MongoTemplate mongoTemplate;

	@Configuration
	public class AppConfig {
	    public @Bean Mongo mongo() throws Exception {
	        return new Mongo("0.0.0.0:27017");
	    }

	    public @Bean MongoTemplate mongoTemplate() throws Exception {
	        return new MongoTemplate(mongo(), "knowledge-test");
	    }
	}

    @SuppressWarnings("deprecation")
	@Override
    public void setUp()
    {	/*
        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
		String configPath = this.getClass().getResource("/mongo_config.xml").getPath();

		Resource resource = new FileSystemResource(new File(configPath));
        ApplicationContext ctx = new GenericXmlApplicationContext(resource);
        mongoTemplate = (MongoTemplate)ctx.getBean("mongoTemplate");*/

        try {
        	if (createTestData) {
        		TestData.main(new String[]{});
        		createTestData = false;
        	}
        	if (saveToDB && mongoTemplate == null) {
        		mongoTemplate = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "knowledge-test"));
        	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
    }

    public void testKnowledgeParserJson() throws Exception
    {
    	checkJsonPaser( DataCollection.class );
    }

    public void testServiceMedicalConsultParserJson() throws Exception
    {
    	checkJsonPaser( KnowledgeComment.class );
    }

    public void testKnowledgeCommentParserJson() throws Exception
    {
        String knowledgeCommentJson = TestData.knowledgeCommentJson(12345567L);
        ObjectMapper objectMapper = new ObjectMapper();
        KnowledgeComment knowledgeComment = objectMapper.readValue(knowledgeCommentJson, KnowledgeComment.class);
        assertNotNull(knowledgeComment);
    }

	private void checkJsonPaser(Class classz) throws IOException {
        String jsonPath = TestDataUtil.getJsonFile(classz.getSimpleName());
        String jsonContent = KnowledgeUtil.getJsonContentFromFile(jsonPath);
        assertNotNull(jsonContent);

        if (classz == DataCollection.class) {
            DataCollection knowledge = KnowledgeUtil.readValue(DataCollection.class, jsonContent);
            assertNotNull(knowledge);
        }
        else if(classz == KnowledgeComment.class) {
            KnowledgeComment comment = null;
            try {
                comment = KnowledgeUtil.readValue(KnowledgeComment.class, jsonContent);
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            assertNotNull(comment);
        }
	}
}
