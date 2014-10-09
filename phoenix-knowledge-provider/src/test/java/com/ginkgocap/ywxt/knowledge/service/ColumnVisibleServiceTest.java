package com.ginkgocap.ywxt.knowledge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.ginkgocap.ywxt.knowledge.base.TestBase;
import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeColumn;
import com.ginkgocap.ywxt.knowledge.service.CategoryService;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.util.KCHelper;
import com.ginkgocap.ywxt.util.DateFunc;

public class ColumnVisibleServiceTest extends TestBase {
    @Autowired
    private ColumnVisibleService columnVisibleService;

    @Test
    @Rollback(false)
    public void save() throws Exception {
        // cvs.saveCids(10132, "3", 1);
         
       long count= columnVisibleService.countListByPidAndUserId(10132, 0l);
       if(count==0){
           long gtnid=0;
           columnVisibleService.init(10132, gtnid);
       }
       columnVisibleService.updateCids(10132, "4,6");
    }

}
