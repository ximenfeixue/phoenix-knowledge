package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chen Peifeng on 2016/6/21.
 */
public class AssociateServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private BlockingQueue<Associate> associateQueue = new LinkedBlockingQueue<Associate>();

    public boolean addAssociate(Associate asso)
    {
        return associateQueue.offer(asso);
    }
}
