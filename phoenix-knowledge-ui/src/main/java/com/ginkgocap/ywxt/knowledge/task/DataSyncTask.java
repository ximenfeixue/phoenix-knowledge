package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.dynamic.model.DynamicNews;
import com.ginkgocap.ywxt.knowledge.model.BusinessTrackLog;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.model.common.IdTypeUid;
import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.*;
import com.ginkgocap.ywxt.knowledge.utils.KnowledgeUtil;
import com.ginkgocap.ywxt.track.entity.util.BusinessTrackUtils;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.ywxt.im.model.MessageNotify;
import com.gintong.ywxt.im.model.ResourceMessage;
import com.gintong.ywxt.im.service.MessageNotifyService;
import com.gintong.ywxt.im.service.ResourceMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by gintong on 2016/7/9.
 */
@Repository("dataSyncTask")
public class DataSyncTask implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(DataSyncTask.class);
    private static final int MAX_QUEUE_NUM = 2000;

    @Autowired
    private DataSyncService dataSyncService;

    @Autowired
    private MessageNotifyService messageNotifyService;

    @Autowired
    private KnowledgeOtherService knowledgeOtherService;

    @Autowired
    private KnowledgeCountService knowledgeCountService;

    @Autowired
    private KnowledgeCommentService knowledgeCommentService;

    @Autowired
    private KnowledgeIndexService knowledgeIndexService;

    @Autowired
    private AssociateServiceLocal associateServiceLocal;

    @Autowired
    private TagServiceLocal tagServiceLocal;

    @Autowired
    private DirectoryServiceLocal directoryServiceLocal;

    @Autowired
    private PermissionServiceLocal permissionServiceLocal;

    @Autowired
    private DynamicNewsServiceLocal dynamicNewsServiceLocal;

    @Autowired
    private ResourceMessageService resourceMessageService;

    @Autowired
    private BigDataSyncTask bigDataSyncTask;

    private BlockingQueue<DataSync> dataSyncQueue = new ArrayBlockingQueue<DataSync>(MAX_QUEUE_NUM);

    public boolean saveDataNeedSync(DataSync data)
    {
        try {
            final long id = dataSyncService.saveDataSync(data);
            data.setId(id);
            addQueue(data);
        } catch (Exception ex) {
            logger.error("save sync data failed: dataSync: " + data);
            return false;
        }
        return true;
    }

    public void run() {
        try {
            while(true) {
                DataSync dataSync = dataSyncQueue.take();
                if (dataSync != null) {
                    boolean result = false;
                    Object data = dataSync.getData();
                    if (data != null) {
                        if (data instanceof MessageNotify) {
                            result = sendMessageNotify((MessageNotify) data);
                        } else if(data instanceof Permission) {
                            final Permission perm = (Permission)data;
                            final short privated = DataCollect.privated(perm, false);
                            result = this.updateCollectedKnowledgePrivate(perm.getResId(), -1, privated);
                            if (!result) {
                                //for the knowledge has been cancel colllect, not need to update
                                boolean isCollected = knowledgeOtherService.isCollectedKnowledge(perm.getResId(), -1);
                                if (!isCollected) {
                                    result = true;
                                    logger.info("delete sync, collected knowledge id: " + perm.getResId());
                                }
                            }
                        } else if (data instanceof IdTypeUid) {
                            final IdTypeUid idTypeUid = (IdTypeUid)data;
                            result = deleteKnowledgeOtherResource(idTypeUid);
                        } else if (data instanceof DynamicNews) {
                            final DynamicNews dynamicNews = (DynamicNews)data;
                            final String dynamicContent = KnowledgeUtil.writeObjectToJson(dynamicNews);;
                            result = dynamicNewsServiceLocal.addDynamicToAll(dynamicContent, dynamicNews.getCreaterId());
                        } else if (data instanceof ResourceMessage) {
                            ResourceMessage resourceMessage = (ResourceMessage)data;
                            result = this.insertResMessage(resourceMessage);
                        }else if (data instanceof BusinessTrackLog) {
                            BusinessTrackLog log = (BusinessTrackLog)data;
                            //for userId is 0 or 1, skip to write business log
                            if (log.getUserId() > 1) {
                                BusinessTrackUtils.addTbBusinessTrackLog(log.getLogger(), log.getTrackLogger(), log.getBusinessModel(),
                                        log.getKnowledgeId(), null, log.getOptType(), log.getRequest(), log.getUserId(), log.getUserName());
                                result = true;
                            }
                        }
                    }
                    if (result) {
                        dataSyncService.deleteDataSync(dataSync.getId());
                    }
                } else {
                    logger.info("data is null, so skip to send.");
                }
            }
        } catch (InterruptedException ex) {
            logger.error("queues thread interrupted. so exit this thread.");
        }
    }

    public void addQueue(DataSync data) {
        if (data != null) {
            try {
                dataSyncQueue.put(data);
            } catch (Exception ex) {
                logger.error("add sync data to queue failed.");
            }
        } else {
            logger.error("sync object is null, so skip it.");
        }
    }

    private boolean sendMessageNotify(MessageNotify message) {
        try {
            boolean result = messageNotifyService.sendMessageNotify(message);
            if (result) {
                logger.info("send comment notify message success. fromId: " + message.getFromId() + " toId: " + message.getToId());
            }
            return result;
        } catch (Exception ex) {
            logger.error("send comment notify message failed. error: " + ex.getMessage());
        }
        return false;
    }

    private boolean updateCollectedKnowledgePrivate(long knowledgeId, int typeId, short privated) {
        try {
            return knowledgeOtherService.updateCollectedKnowledgePrivate(knowledgeId, typeId, privated);
        } catch (Exception ex) {
            logger.error("update collected knowedge permission failed. knowledgeId: " + knowledgeId + " error: " + ex.getMessage());
            return false;
        }
    }

    private boolean insertResMessage(ResourceMessage resourceMessage) {
        try {
            resourceMessageService.insertResMessage(resourceMessage);
        } catch (Exception ex) {
            logger.error("insert resource message failed. error: " + ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean deleteKnowledgeOtherResource(final IdTypeUid idTypeUid) {
        final long userId = idTypeUid.getUid();
        final long knowledgeId = idTypeUid.getId();
        final int columnType = idTypeUid.getType();
        logger.info("begin clean up knowlege regards resource. knowledgeId: " + knowledgeId + " type: " + columnType);

        //delete tags
        boolean reslut = tagServiceLocal.deleteTags(userId, knowledgeId);
        if (!reslut) {
            logger.info("delete knowledge tag failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }
        //delete directory
        reslut = directoryServiceLocal.deleteDirectory(userId, knowledgeId);
        if (!reslut) {
            logger.info("delete knowledge directory failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //delete Assso info
        reslut = associateServiceLocal.deleteAssociate(knowledgeId, userId);
        if (!reslut) {
            logger.info("delete knowledge associate failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //delete permission info
        reslut = permissionServiceLocal.deletePermissionInfo(userId, knowledgeId);
        if (!reslut) {
            logger.info("delete knowledge permission failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //delete knowledge count info
        try {
            knowledgeCountService.deleteKnowledgeCount(knowledgeId);
        } catch (Exception ex) {
            logger.error("delete knowledge count failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //delete knowledge comment info
        try {
            knowledgeCommentService.cleanComment(knowledgeId);
        } catch (Exception ex) {
            logger.error("delete knowledge comment failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //delete knowledge from knowledge index table
        try {
            knowledgeIndexService.deleteKnowledgeIndex(knowledgeId);
        } catch (Exception ex) {
            logger.error("delete knowledge index failed. userId: " + userId + " knowledgeId: " + knowledgeId);
        }

        //send new knowledge to bigdata
        bigDataSyncTask.deleteMessage(knowledgeId, columnType, userId);
        logger.info("clean up knowlege regards resource complete.");
        return true;
    }
}
