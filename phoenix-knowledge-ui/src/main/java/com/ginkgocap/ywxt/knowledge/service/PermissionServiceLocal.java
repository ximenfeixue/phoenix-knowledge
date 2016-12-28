package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.Knowledge;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeUtil;
import com.ginkgocap.ywxt.knowledge.model.common.DataCollect;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.service.PermissionCheckService;
import com.gintong.common.phoenix.permission.service.PermissionRepositoryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.frame.util.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chen Peifeng on 2016/6/21.
 */
@Repository("permissionServiceLocal")
public class PermissionServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    private final Logger logger = LoggerFactory.getLogger(PermissionServiceLocal.class);

    @Autowired
    private PermissionCheckService permissionCheckService;

    @Autowired
    private PermissionRepositoryService permissionRepositoryService;

    @Autowired
    private KnowledgeService knowledgeService;

    private BlockingQueue<Permission> permissionQueue = new LinkedBlockingQueue<Permission>();

    public boolean addPermission(Permission permission)
    {
        return permissionQueue.offer(permission);
    }

    /*
    public void updatePassword(final long userId, final String password) {
        // 生成salt
        User user = new User();
        int hashIterations = 5000;
        RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();

        String salt = saltGenerator.nextBytes().toHex();

        String newPass = new Sha256Hash(password, salt, hashIterations).toHex();
        String updateSQL = String .format("update tb_user set password='%s' salt='%s' where id=%d", newPass, salt, userId);
        System.out.println("Update SQL: " + updateSQL);
        user.setId(userId);
        user.setSalt(salt);
        user.setPassword(newPass);
        passwdService.updateUserPassWord(user);
    }*/

    public boolean canUpdate(final long knowledgeId, final String type, final long userId)
    {
        boolean canUpdate = false;
        try {
            InterfaceResult<Boolean> result = permissionCheckService.isUpdatable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
            if (result == null || result.getResponseData() == null) {
                logger.error("permission info is null, please check if user have permission!");
                return canUpdate;
            }
            else {
                canUpdate = result.getResponseData().booleanValue();
                if (!canUpdate) {
                    return isKnowledgeOwner(knowledgeId, type, userId);
                }
            }
        } catch (Throwable ex) {
            logger.error("permission query failed, knowledgeId: {}, userId: {} error: {}", knowledgeId, userId, ex.getMessage());
        }
        return canUpdate;
    }

    public boolean canDelete(final long knowledgeId, final String type, final long userId)
    {
        boolean canDelete = false;
        try {
            InterfaceResult<Boolean> result = permissionCheckService.isDeletable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
            if (result == null || result.getResponseData() == null) {
                logger.error("permission info is null, please check if user have permission!");
                return canDelete;
            }
            else {
                canDelete = result.getResponseData().booleanValue();
                if (!canDelete) {
                    return isKnowledgeOwner(knowledgeId, type, userId);
                }
            }
        } catch (Throwable ex) {
            logger.error("permission query failed, knowledgeId: {}, userId: {} error: {}", knowledgeId, userId, ex.getMessage());
        }
        return canDelete;
    }

    public boolean isKnowledgeOwner(final long knowledgeId, final String type, final long userId)
    {
        try {
            int columnType = KnowledgeUtil.parserColumnId(type);
            DataCollect data = knowledgeService.getKnowledge(knowledgeId, columnType);
            if (data != null) {
                KnowledgeBase base = data.getKnowledge();
                if (base != null) {
                    logger.info("check owner by base info. knowledgeId: " + knowledgeId + ", type: " + type + ", userId: " + userId);
                    return (base.getCreateUserId() == userId);
                }
                Knowledge detail = data.getKnowledgeDetail();
                if (detail != null) {
                    logger.info("check owner by detail info. knowledgeId: " + knowledgeId + ", type: " + type + ", userId: " + userId);
                    return (detail.getCid() == userId);
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Permission savePermissionInfo(final long userId, final long knowledgeId, final Permission permission)
    {
        if (permission == null) {
            logger.error("No permission info give, so skip to save. userId: " + userId + " knowledgeId: " + knowledgeId);
            return null;
        }
        Permission newPermission = permissionInfo(permission, knowledgeId, userId);
        boolean result = savePermissionInfo(newPermission);
        if (result) {
            return newPermission;
        }
        return null;
    }

    public boolean savePermissionInfo(final Permission permission)
    {
        if (permission == null) {
            logger.error("No permission info give, so skip to save.");
            return false;
        }
        try {
            InterfaceResult<Boolean> result = permissionRepositoryService.insert(permission);
            if (result != null && result.getResponseData() != null && result.getResponseData().booleanValue()) {
                logger.info("Insert knowledge permission success : userId: " + permission.getResOwnerId() + " knowledgeId: " + permission.getResId());
                return true;
            }
            else {
                logger.info("Insert knowledge permission failed : userId: " + permission.getResOwnerId() + " knowledgeId: " + permission.getResId());
            }
        } catch (Exception e) {
            logger.error("Insert knowledge permission failed : userId: " + permission.getResOwnerId() + " knowledgeId: " + permission.getResId() + " error: " + e.getMessage());
            return false;
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return false;
    }

    public Permission updatePermissionInfo(final long userId, final long knowledgeId, Permission permission)
    {
        if (permission == null) {
            logger.error("No permission info give, so skip to update. userId: " + userId + " knowledgeId: " + knowledgeId);
            return null;
        }

        try {
            permissionInfo(permission, knowledgeId, userId);
            InterfaceResult<Boolean> result = permissionRepositoryService.update(permission);
            if (result != null && result.getResponseData() != null && result.getResponseData().booleanValue()) {
                logger.info("update knowledge permission success : userId: " + userId + " knowledgeId: " + knowledgeId);
            }
            else {
                logger.info("update knowledge permission failed : userId: " + userId + " knowledgeId: " + knowledgeId);
            }
        } catch (Exception e) {
            logger.error("Update knowledge permission failed : error: " + e.getMessage());
            return null;
        }
        return permission;
    }

    public boolean deletePermissionInfo(long userId,long knowledgeId)
    {
        try {
            Permission permission = permissionInfo(new Permission(), knowledgeId, userId);
            InterfaceResult<Boolean> ret = permissionRepositoryService.delete(permission);
            if (ret == null || !ret.getResponseData()) {
                logger.error("Delete Permission failed, knowledgeId: " + knowledgeId);
            }
        }catch (Exception ex) {
            logger.error("Delete Permission failed, knowledgeId: " + knowledgeId + " Reason: "+ex.getMessage());
            return false;
        }

        return true;
    }

    public Permission getPermissionInfo(long knowledgeId)
    {
        try {
            InterfaceResult<Permission> ret = permissionRepositoryService.selectByRes(knowledgeId, ResourceType.KNOW, APPID);
            Notification notif = ret.getNotification();
            if (notif != null && CommonResultCode.SUCCESS.getCode().equals(notif.getNotifCode())) {
                return ret.getResponseData();
            }
            else {
                logger.error("can't get knowledge permission failed: knowledgeId: " + knowledgeId + ", errorMsg: " + notif.getNotifInfo());
            }
        }catch (Exception ex) {
            logger.error("get knowledge permission failed: knowledgeId: " + knowledgeId + ", error: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }

    private Permission permissionInfo(Permission permission,long knowledgeId,long userId)
    {
        if (permission != null) {
            permission.setAppId(APPID);
            permission.setResId(knowledgeId);
            permission.setResOwnerId(userId);
            permission.setResType(ResourceType.KNOW.getVal());
            if (permission.getPerTime() == null) {
                permission.setPerTime(new Date());
            }
        }
        return permission;
    }
}
