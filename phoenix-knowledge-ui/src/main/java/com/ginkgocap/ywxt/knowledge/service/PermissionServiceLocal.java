package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.common.phoenix.permission.entity.Permission;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chen Peifeng on 2016/6/21.
 */
public class PermissionServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private BlockingQueue<Permission> permissionQueue = new LinkedBlockingQueue<Permission>();

    public boolean addAssociate(Permission permission)
    {
        return permissionQueue.offer(permission);
    }
}
