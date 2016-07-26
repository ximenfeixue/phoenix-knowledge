package com.ginkgocap.ywxt.knowledge.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.dynamic.service.DynamicNewsService;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.user.service.FriendsRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by gintong on 2016/7/9.
 */

@Repository("dynamicNewsServiceLocal")
public class DynamicNewsServiceLocal
{
    private Logger logger = LoggerFactory.getLogger(DynamicNewsServiceLocal.class);
    @Autowired
    DynamicNewsService dynamicNewsService;

    @Autowired
    private FriendsRelationService friendsRelationService;

    public void addDynamic(String jsonContent, long userId) throws IOException {
        //Map<String, Object> result = new HashMap<String, Object>();
        //Map<String, Object> responseData = new HashMap<String, Object>();
        try{
            JsonNode treeNode = KnowledgeUtil.readTree(jsonContent);
            if (treeNode == null) {
                logger.error("Dynamic json is not validate...");
                return;
            }

            int scope = 0;  //0，所有好友，1，部分好友，2，私密
            try {
                scope = treeNode.get("scope").asInt();
            } catch (Exception ex) {
                logger.error("scope is not existing or not validate. error: {}",ex.getMessage());
            }

            List<Long> receiverIds = null;
            long user_id = userId > 0 ? userId : 1l;
            if(scope == 0){ //所有好友
                Map<Long, String> friends = friendsRelationService.findAllFriend2Map(user_id);
                if (friends != null && friends.size() > 0){
                    receiverIds = new ArrayList<Long>(friends.size()+1);
                    receiverIds.addAll(friends.keySet());
                }
            }else if (scope == 1){
                JsonNode recvIdsNode = treeNode.get("receiverIds");
                if (recvIdsNode != null) {
                    receiverIds = KnowledgeUtil.readListValue(Long.class, recvIdsNode.asText());
                }
            }

            if (receiverIds == null) {
                receiverIds = new ArrayList<Long>(1);
                receiverIds.add(user_id);
            }
            else if(!receiverIds.contains(user_id)) {
                receiverIds.add(user_id);//加上自己
            }

            JsonNode newsNode = treeNode.get("dynamicNews");
            if (newsNode == null) {
                logger.error("Dynamic news json is not null ot not validate...");
                return;
            }
            Map<String, Object> news = new HashMap<String,Object>();
            news = KnowledgeUtil.readValue(Map.class, newsNode.toString());
            news.put("scope", scope);
            long dynamicId = dynamicNewsService.insertNewsAndRelation(news, receiverIds);
            for(long id : receiverIds){
                //this.setEtagStatus(id, "1");
            }
            logger.info("create dynamic success, dynamicId: {}", dynamicId);
        }catch(Exception e){
            logger.error("添加动态失败！",e);
        }
        return;
    }

    public boolean addDynamicToAll(String newsContent, long userId, Map<Long,List<Associate>> assoMap) throws IOException
    {
        try{
            List<Long> receiverIds = null;
            long user_id = userId > 0 ? userId : 1l;
            Map<Long, String> friends = friendsRelationService.findAllFriend2Map(user_id);
            if (friends != null && friends.size() > 0){
                receiverIds = new ArrayList<Long>(friends.size()+1);
                receiverIds.addAll(friends.keySet());
            }

            if (receiverIds == null) {
                receiverIds = new ArrayList<Long>(1);
                receiverIds.add(user_id);
            }
            else if(!receiverIds.contains(user_id)) {
                receiverIds.add(user_id);//加上自己
            }

            Map<String, Object> news = new HashMap<String,Object>();
            news = KnowledgeUtil.readValue(Map.class, newsContent);
            news.put("asso", assoMap != null ? assoMap : "{}");
            long dynamicId = dynamicNewsService.insertNewsAndRelation(news, receiverIds);
            for(long id : receiverIds){
                //this.setEtagStatus(id, "1");
            }
            logger.info("create dynamic success, dynamicId: {}", dynamicId);
        }catch(Exception e){
            logger.error("添加动态失败！",e);
            return false;
        }
        return true;
    }

    /*
    public void setEtagStatus(long userId,String status){
        cache.setByRedis(Constant.ETAGSTRDYNAMIC+userId, status,24*60*60);
    }*/
}