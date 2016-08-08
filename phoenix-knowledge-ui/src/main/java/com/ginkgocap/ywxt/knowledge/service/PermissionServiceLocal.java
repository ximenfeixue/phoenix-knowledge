package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.parasol.associate.model.Associate;
import com.ginkgocap.ywxt.knowledge.service.common.KnowledgeBaseService;
import com.ginkgocap.ywxt.user.model.User;
import com.ginkgocap.ywxt.user.service.ChangePwdService;
import com.gintong.common.phoenix.permission.entity.Permission;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Chen Peifeng on 2016/6/21.
 */
@Repository("permissionServiceLocal")
public class PermissionServiceLocal extends BaseServiceLocal implements KnowledgeBaseService
{
    @Autowired
    private ChangePwdService passwdService;

    private BlockingQueue<Permission> permissionQueue = new LinkedBlockingQueue<Permission>();

    public boolean addAssociate(Permission permission)
    {
        return permissionQueue.offer(permission);
    }

    public void updatePassword(long userId,String password) {
        // 生成salt
        User user = new User();
        int hashIterations = 5000;
        RandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();

        String salt = saltGenerator.nextBytes().toHex();
        //Base64
        byte[] bt = Base64.decode(password);
        password = new String(bt);

        String newPass = new Sha256Hash(password, salt, hashIterations).toHex();
        String updateSQL = String .format("update tb_user set password='%s' salt='%s' where id=%d", newPass, salt, userId);
        System.out.println("Update SQL: " + updateSQL);
        user.setId(userId);
        user.setSalt(salt);
        user.setPassword(newPass);
        passwdService.updateUserPassWord(user);
    }
}
