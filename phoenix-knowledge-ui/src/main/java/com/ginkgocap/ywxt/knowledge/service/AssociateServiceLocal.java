package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.parasol.associate.model.AssociateType;
import com.ginkgocap.parasol.associate.service.AssociateService;
import com.ginkgocap.parasol.associate.service.AssociateShareService;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.user.model.User;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chen Peifeng on 2016/6/21.
 */
@Repository("associateServiceLocal")
public class AssociateServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private Logger logger = LoggerFactory.getLogger(AssociateServiceLocal.class);

    /*
    public boolean addAssociate(Associate asso)
    {
        return associateQueue.offer(asso);
    }*/

    @Autowired
    private AssociateService associateService;

    @Autowired
    private AssociateShareService associateShareService;

    public Map<Long, List<Associate>> createAssociate(List<Associate> as, long knowledgeId, User user)
    {
        if (CollectionUtils.isEmpty(as)) {
            logger.error("associate it null or converted failed, so skip to save!");
            return null;
        }

        //now only 4 type asso
        long userId = this.getUserId(user);
        Map<Long, List<Associate>> assoMap = new HashMap<Long, List<Associate>>(4);
        for (int index = 0; index < as.size(); index++) {
            Associate associate = as.get(index);
            if (associate == null) {
                logger.error("associate is null, so skip save it.");
                continue;
            }
            associate.setUserId(userId);
            associate.setUserName(user.getName());
            associate.setSourceId(knowledgeId);
            associate.setSourceTypeId(KnowledgeBaseService.sourceType);
            //associate.setAssocTypeId(assoType.getId());
            associate.setUserId(userId);
            associate.setAppId(APPID);
            try {
                long assoId = associateService.createAssociate(APPID, userId, associate);
                if (assoMap.get(associate.getAssocTypeId()) == null) {
                    List<Associate> assoList = new ArrayList<Associate>(2);
                    assoMap.put(associate.getAssocTypeId(), assoList);
                }
                assoMap.get(associate.getAssocTypeId()).add(associate);
                logger.info("assoId:" + assoId);
            }catch (Exception e) {
                logger.error("create Asso failed！reason：" + e.getMessage());
            } catch (Throwable e) {
                logger.error("create Asso failed！reason：" + e.getMessage());
            }
        }

        return assoMap;
    }

    public boolean deleteAssociate(long knowledgeId, long userId)
    {
        Map<AssociateType, List<Associate>> assomap = null;
        try {
            assomap =  associateService.getAssociatesBy(APPID, (long)KnowledgeBaseService.sourceType, knowledgeId);
            if (assomap == null) {
                logger.error("asso item null or converted failed...");
                return false;
            }
        } catch (Exception ex) {
            logger.error("delete Associate failed, reason: " + ex.getMessage());
            return false;
        }

        //TODO: If this step failed, how to do ?
        for (Iterator i = assomap.values().iterator(); i.hasNext();) {
            List<Associate> associateList = (List)i.next();
            for (int j = 0; j < associateList.size(); j++) {
                try {
                    final Associate associate = associateList.get(j);
                    if (associate == null) {
                        logger.error("associate is null, so skip save it.");
                        continue;
                    }
                    associateService.removeAssociate(APPID, userId, associate.getId());
                }catch (Exception ex) {
                    logger.error("delete Associate failed, reason: " + ex.getMessage());
                    return false;
                }
            }
        }

        return true;
    }

    public InterfaceResult updateAssociate(long knowledgeId, User user, DataCollect data)
    {
        Map<AssociateType, List<Associate>> assomap = null;
        try {
            assomap = associateService.getAssociatesBy(APPID, (long) KnowledgeBaseService.sourceType, knowledgeId);
            if (assomap == null) {
                logger.error("asso item null or converted failed...");
                return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SYSTEM_EXCEPTION);
            }
        }
        catch (Exception e) {
            logger.error("Asso update failed！reason：" + e.getMessage());
        }

        //TODO: If this step failed, how to do ?
        if (MapUtils.isNotEmpty(assomap)) {
            for (Iterator i = assomap.values().iterator(); i.hasNext(); ) {
                List<Associate> associateList = (List) i.next();
                for (int j = 0; j < associateList.size(); j++) {
                    Associate associate = associateList.get(j);
                    if (associate == null) {
                        logger.error("Associate object is null, index: " + j);
                        continue;
                    }
                    try {
                        associateService.removeAssociate(APPID, this.getUserId(user), associate.getId());
                    } catch (Exception e) {
                        logger.error("Asso update failed！reason：" + e.getMessage());
                    }
                }
            }
        }

        List<Associate> as = data.getAsso();
        createAssociate(as, knowledgeId, user);

        return InterfaceResult.getInterfaceResultInstance(CommonResultCode.SUCCESS);
    }

    public List<Associate> getAssociateList(long userId, long knowledgeId) throws Exception {
        return associateService.getAssociatesBySourceId(APPID, userId, knowledgeId, (long)sourceType);
    }

    public String getAssoiateShareContent(long share) throws Exception{
        return associateShareService.getAssociateShare(share);
    }
}
