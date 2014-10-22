package com.ginkgocap.ywxt.knowledge.service;


import java.util.ArrayList;
import java.util.List;

import org.junit.After; 
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.util.DateFunc;

/**
 *  userCategoryService的测试用例
 * @author bianzhiwei  
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 *
 */
public class UserCategoryServiceTest extends TestBase {
    @Autowired
    private UserCategoryService userCategoryService;

    private long userId = 1;
    private UserCategory uc;

    @Before
    public void setUp() throws Exception {
        uc = new UserCategory();
        uc.setCategoryname("n01");
        uc.setUserId((long) 10000);
        uc.setParentId((long) 1998);
        uc.setCreatetime(DateFunc.getRegDate());

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInsert() {
       userCategoryService.insert(uc);
    }

    @Test
    public void testSelectByPrimaryKey() {
        UserCategory cat = userCategoryService.selectByPrimaryKey(39);
    }

    @Test
    public void testUpdate() {
        userCategoryService.update(uc);
    }

    @Test
    public void testDel() {
        userCategoryService.delete(41);
    }

    @Test
    public void testSelectChildBySortId() {
        userCategoryService.selectChildBySortId(userId, "000000001",(byte) 0);
    }

    @Test
    public void testSelectUserCategoryTreeBySortId() {
        userCategoryService.selectUserCategoryTreeBySortId(userId, "000000001", (byte) 0);
    }
    
    @Test
    @Rollback(false)
    public void testcheckNogroup() {
        List<Long>l=new ArrayList<Long>();
        l.add(0l);
        l.add(1l);
        userCategoryService.checkNogroup(10132l,l);
    }
}
