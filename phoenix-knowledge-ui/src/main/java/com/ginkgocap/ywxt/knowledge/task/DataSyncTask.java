package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.model.mobile.EActionType;
import com.ginkgocap.ywxt.knowledge.service.DataSyncService;
import com.ginkgocap.ywxt.knowledge.service.DynamicNewsServiceLocal;
import com.gintong.ywxt.im.model.MessageNotify;
import com.gintong.ywxt.im.service.MessageNotifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    DataSyncService dataSyncService;

    @Autowired
    DynamicNewsServiceLocal dynamicNewsServiceLocal;

    @Autowired
    private MessageNotifyService messageNotifyService;

    private BlockingQueue<Object> dataSyncQueue = new ArrayBlockingQueue<Object>(MAX_QUEUE_NUM);

    public void addQueue(Object object) {
        if (object != null) {
            dataSyncQueue.offer(object);
        } else {
            logger.error("sync object is null, so skip it.");
        }
    }

    public boolean saveDataNeedSync(DataSync data)
    {
        try {
            dataSyncService.saveDataSync(data);
            dataSyncQueue.offer(data);
        } catch (Exception ex) {
            logger.error("save sync data failed: dataSync: {}",data);
            return false;
        }
        return true;
    }

    public void run() {
        try {
            while(true) {
                Object data = dataSyncQueue.poll();
                if (data != null) {
                    if (data instanceof MessageNotify) {
                        sendMessageNotify((MessageNotify) data);
                    }
                } else {
                    Thread.sleep(2000);
                    //logger.info("sync data queue is null, so sleep 2 second");
                }

            }
        } catch (InterruptedException ex) {
            logger.error("queues thread interrupted. so exit this thread.");
        }
    }

    private void sendMessageNotify(MessageNotify message) {
        try {
            boolean resilt = messageNotifyService.sendMessageNotify(message);
            if (resilt) {
                logger.info("send comment notify message success. fromId: " + message.getFromId() + " toId: " + message.getToId());
            }
        } catch (Exception ex) {
            logger.error("send comment notify message failed. error: " + ex.getMessage());
        }

    }
}
