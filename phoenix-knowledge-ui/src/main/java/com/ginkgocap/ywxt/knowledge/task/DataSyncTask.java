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
            this.offerToQueue(data);
        } catch (Exception ex) {
            logger.error("save sync data failed: dataSync: " + data);
            return false;
        }
        return true;
    }

    public <T> boolean saveDataNeedSync(T data) {
        DataSync<T> dataSync = this.createDataSync(0, data);
        return this.saveDataNeedSync(dataSync);
    }

    public void run() {
        try {
            while(true) {
                DataSync dataSync = dataSyncQueue.take();
                if (dataSync != null) {
                    Object data = dataSync.getData();
                    final long dataSyncId = dataSync.getId();
                    boolean result = processDataSync(data, dataSyncId);
                    if (!result) {
                        logger.error("dataSync process failed, try again id: " + dataSyncId);
                        Object resetData = dataSyncData(dataSync);
                        //delete the bad dataSync
                        if (resetData == null) {
                            result = processDataSync(data, dataSyncId);
                        }
                    }
                    if (result) {
                        this.deleteDataSync(dataSyncId);
                    }
                    logger.info("one data sync complete. id: " + dataSync.getId() + " result: " + result);
                } else {
                    logger.info("data is null, so delete it.");
                    this.deleteDataSync(dataSync.getId());
                }
            }
        } catch (Exception ex) {
            logger.error("queues thread interrupted. so exit this thread. error: " + ex.getMessage());
        }
    }

    private boolean processDataSync(Object data, long id) {
        boolean result = false;
        if (data != null) {
            if (data instanceof MessageNotify) {
                MessageNotify message = (MessageNotify) data;
                result = sendMessageNotify(message);
            } else if (data instanceof Permission) {
                final Permission perm = (Permission) data;
                final short privated = DataCollect.privated(perm, false);
                result = this.updateCollectedKnowledgePrivate(perm.getResId(), -1, privated);
                if (!result) {
                    //for the knowledge has been cancel colllect, not need to update
                    boolean isCollected = this.isCollectedKnowledge(perm.getResId(), -1);
                    if (!isCollected) {
                        result = true;
                        logger.info("delete sync, collected knowledge id: " + perm.getResId());
                    }
                }
            } else if (data instanceof IdTypeUid) {
                final IdTypeUid idTypeUid = (IdTypeUid) data;
                result = deleteKnowledgeOtherResource(idTypeUid);
            } else if (data instanceof DynamicNews) {
                final DynamicNews dynamicNews = (DynamicNews) data;
                final String dynamicContent = KnowledgeUtil.writeObjectToJson(dynamicNews);
                result = dynamicNewsServiceLocal.addDynamicToAll(dynamicContent, dynamicNews.getCreaterId());
            } else if (data instanceof ResourceMessage) {
                ResourceMessage resourceMessage = (ResourceMessage) data;
                result = this.insertResMessage(resourceMessage);
            } else if (data instanceof BusinessTrackLog) {
                BusinessTrackLog log = (BusinessTrackLog) data;
                //for userId is 0 or 1, skip to write business log
                if (log.getUserId() > 1) {
                    BusinessTrackUtils.addTbBusinessTrackLog(log.getLogger(), log.getTrackLogger(), log.getBusinessModel(),
                            log.getKnowledgeId(), null, log.getOptType(), log.getRequest(), log.getUserId(), log.getUserName());
                    result = true;
                }
            } else {
                logger.error("can't find the type of data sync. id: " + id);
            }
        }
        return result;
    }

    public boolean offerToQueue(DataSync data) {
        if (data != null) {
            try {
                return dataSyncQueue.offer(data);
            } catch (Exception ex) {
                logger.error("add sync data to queue failed.");
            }
        } else {
            logger.error("sync object is null, so skip it.");
        }
        return false;
    }

    private void putToQueue(DataSync data) {
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

    public <T> void putQueue(T data) {
        DataSync<T> dataSync = this.createDataSync(0, data);
        this.putToQueue(dataSync);
    }

    private boolean isCollectedKnowledge(long knowledgeId, int typeId) {
        try {
            return knowledgeOtherService.isCollectedKnowledge(knowledgeId, typeId);
        } catch (Exception ex) {
            logger.error("check knowledge collected failed. knowledgeId: " + knowledgeId + " typeId: " + typeId);
        }
        return false;
    }

    private boolean sendMessageNotify(MessageNotify message) {
        try {
            boolean result = messageNotifyService.sendMessageNotify(message);
            logger.info("send comment notify message : " +(result ? "success" : "failed") + " fromId: " + message.getFromId() + " toId: " + message.getToId());
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

    private void deleteDataSync(final long syncId) {
        if (syncId > 0) {
            try {
                boolean delResult = dataSyncService.deleteDataSync(syncId);
                logger.info("delete dataSync " + (delResult ? "success" : "failed") + " id: " + syncId);
            } catch (Exception ex) {
                logger.error("delete data sync failed. id: " + syncId + " error: " + ex.getMessage());
            }
        }
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

    /*
    private DataSync resetDataSync(DataSync dataSync)
    {
        Object data = dataSyncData(dataSync);
        if (data != null) {
            logger.info("reset dataSync success. id: " + dataSync.getId());
            dataSync.setData(data);
            putToQueue(dataSync);
            return dataSync;
        } else {
            logger.info("reset dataSync failed. id: " + dataSync.getId());
            return null;
        }
    }*/

    private <T> DataSync createDataSync(long id, T data) {
        if (data instanceof MessageNotify) {
            logger.info("create MessageNotify dataSync..");
            return new DataSync(0, DataSync.EResType.EMsgNotify.value(), data);
        }
        else if (data instanceof Permission) {
            logger.info("create Permission dataSync..");
            return new DataSync(0, DataSync.EResType.EPerm.value(), data);
        }
        else if (data instanceof IdTypeUid) {
            logger.info("create knowledge dataSync..");
            return new DataSync(0, DataSync.EResType.EIdTypeUid.value(), data);
        }
        else if (data instanceof DynamicNews) {
            logger.info("create DynamicNews dataSync..");
            return new DataSync(0, DataSync.EResType.EDynamic.value(), data);
        }
        else if (data instanceof ResourceMessage) {
            logger.info("create ResourceMessage dataSync..");
            return new DataSync(0, DataSync.EResType.EResMsg.value(), data);
        }
        else if (data instanceof BusinessTrackLog) {
            logger.info("create BusinessTrackLog dataSync..");
            return new DataSync(0, 0, data);
        }
        else {
            logger.info("create dataSync not belong.  data: " + data.getClass());
            return null;
        }
    }

    private Object dataSyncData(DataSync dataSync) {
        final long dataSyncId = dataSync.getId();
        final int resType = dataSync.getResType();
        final Object data = dataSync.getData();
        if (data == null) {
            logger.error("DataSync data is null. id: " + dataSyncId);
            return null;
        }
        final String objContent = KnowledgeUtil.writeObjectToJson(data);
        if (objContent == null) {
            logger.error("DataSync data content is null. id: " + dataSyncId);
            return null;
        }
        logger.info("dataSyncId: " + dataSyncId + "resType: " + resType);
        if (resType > 0) {
            switch (resType) {
                case 1: {
                    return KnowledgeUtil.readValue(MessageNotify.class, objContent);
                }
                case 2: {
                    return KnowledgeUtil.readValue(Permission.class, objContent);
                }
                case 3: {
                    return KnowledgeUtil.readValue(IdTypeUid.class, objContent);
                }
                case 4: {
                    return KnowledgeUtil.readValue(DynamicNews.class, objContent);
                }
                case 5: {
                    return KnowledgeUtil.readValue(ResourceMessage.class, objContent);
                }
                /*case 6: {
                    return KnowledgeUtil.readValue(BusinessTrackLog.class, objContent);
                }*/
                default: {
                    logger.error("Unknowledge resType: " + resType + " data: " + dataSync.getData());
                    return null;
                }
            }
        }
        else {
            Object dataObject = KnowledgeUtil.readValue(MessageNotify.class, objContent);
            if (dataObject != null) {
                logger.info("convert MessageNotify object success..");
                dataSync.setResType(DataSync.EResType.EMsgNotify.value());
                return dataObject;
            }

            dataObject = KnowledgeUtil.readValue(ResourceMessage.class, objContent);
            if (dataObject != null) {
                logger.info("convert ResourceMessage object success..");
                dataSync.setResType(DataSync.EResType.EResMsg.value());
                return dataObject;
            }

            dataObject = KnowledgeUtil.readValue(DynamicNews.class, objContent);
            if (dataObject != null) {
                logger.info("convert DynamicNews object success. id: " + dataSyncId);
                dataSync.setResType(DataSync.EResType.EDynamic.value());
                return dataObject;
            }
            else {
                logger.error("dataSync not belong, id: " + dataSyncId);
            }
        }
        return null;
    }
}
