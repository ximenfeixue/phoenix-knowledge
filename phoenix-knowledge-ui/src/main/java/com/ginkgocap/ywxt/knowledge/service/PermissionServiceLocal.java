package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.ChangePwdService;
import com.gintong.common.phoenix.permission.ResourceType;
import com.gintong.common.phoenix.permission.entity.Permission;
import com.gintong.common.phoenix.permission.service.PermissionCheckService;
import com.gintong.common.phoenix.permission.service.PermissionRepositoryService;
import com.gintong.frame.util.dto.CommonResultCode;
import com.gintong.frame.util.dto.InterfaceResult;
import com.gintong.frame.util.dto.Notification;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.hibernate.annotations.SourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
    private ChangePwdService passwdService;

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

    public boolean canUpdate(final long knowledgeId, final long userId)
    {
        boolean canUpdate = false;
        try {
            InterfaceResult<Boolean> result = permissionCheckService.isUpdatable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
            if (result == null || result.getResponseData() == null) {
                logger.error("permission validate failed, please check if user have permission!");
            }
            canUpdate = result.getResponseData().booleanValue();
        } catch (Throwable ex) {
            logger.error("permission query failed, knowledgeId: {}, userId: {} error: {}", knowledgeId, userId, ex.getMessage());
        }
        return canUpdate;
    }

    public boolean canDelete(final long knowledgeId, final long userId)
    {
        boolean canDelete = false;
        try {
            InterfaceResult<Boolean> result = permissionCheckService.isDeletable(ResourceType.KNOW.getVal(), knowledgeId, userId, APPID);
            if (result == null || result.getResponseData() == null) {
                logger.error("permission validate failed, please check if user have permission!");
            }
            canDelete = result.getResponseData().booleanValue();
        } catch (Throwable ex) {
            logger.error("permission query failed, knowledgeId: {}, userId: {} error: {}", knowledgeId, userId, ex.getMessage());
        }
        return canDelete;
    }

    public Permission savePermissionInfo(final long userId, final long knowledgeId, final Permission permission)
    {
        if (permission == null) {
            logger.error("No permission info give, so skip to save");
            return null;
        }
        Permission newPermission = null;
        try {
            newPermission = permissionInfo(permission, knowledgeId, userId);
            if (newPermission != null) {
                permissionRepositoryService.insert(newPermission);
            }
        } catch (Exception e) {
            logger.error("Insert knowledge permission failed : error: {}", e.getMessage());
            return null;
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return newPermission;
    }

    public Permission updatePermissionInfo(final long userId, final long knowledgeId, final Permission permission)
    {
        if (permission == null) {
            logger.error("No permission info give, so skip to update");
            return null;
        }
        Permission newPermission = null;
        try {
            newPermission = permissionInfo(permission, knowledgeId, userId);
            if (newPermission != null) {
                permissionRepositoryService.update(newPermission);
            }
        } catch (Exception e) {
            logger.error("Update knowledge permission failed : error: {}", e.getMessage());
            return null;
            //return InterfaceResult.getInterfaceResultInstance(CommonResultCode.PARAMS_DB_OPERATION_EXCEPTION);
        }
        return newPermission;
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
                logger.error("can't get knowledge permission failed: knowledgeId: {}, errorMsg: {}", knowledgeId, notif.getNotifInfo());
            }
        }catch (Exception ex) {
            logger.error("get knowledge permission failed: knowledgeId: {}, error: {}", knowledgeId, ex.getMessage());
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

    public Permission defaultPrivatePermission(final long userId,final long resId)
    {
        Permission permission = new Permission();
        permission.setAppId(APPID);
        permission.setResId(resId);
        permission.setResOwnerId(userId);
        permission.setConnectFlag(0);
        permission.setPublicFlag(0);
        permission.setShareFlag(0);
        permission.setResType((short)sourceType);
        return permission;
    }
}
