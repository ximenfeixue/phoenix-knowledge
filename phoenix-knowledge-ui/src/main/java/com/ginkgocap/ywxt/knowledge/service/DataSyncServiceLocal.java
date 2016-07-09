package com.ginkgocap.ywxt.knowledge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.Resource;

/**
 * Created by gintong on 2016/7/9.
 */
public class DataSyncServiceLocal {

    private Logger logger = LoggerFactory.getLogger(DataSyncServiceLocal.class);
    @Resource
    private MongoTemplate mongoTemplate;
}
