package com.ginkgocap.ywxt.knowledge.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.entity.Column;
import com.ginkgocap.ywxt.knowledge.mapper.ColumnMapper;
import com.ginkgocap.ywxt.knowledge.service.ColumnService;
import com.ginkgocap.ywxt.knowledge.service.ColumnSubscribeService;

@Component("columnTimerTasker")
public class ColumnTimerTasker {

    @Resource
    ColumnService cs;
    @Resource
    ColumnSubscribeService subcs;
    @Autowired
    ColumnMapper columnMapper;
    
    boolean b=true;

    public void execute() throws JobExecutionException {
        
//        SimpleDateFormat f=new SimpleDateFormat("E yyyy-MM-dd HH:mm:ss");
//        Date date=new Date();
//        System.out.println("ColumnTimerTasker.execute()   "+f.format(date)+"   ");
        
        List<Column> list= cs.queryAll();
        
        for (int i = 0; i < list.size(); i++) {
            Column c=list.get(i);
           
            long count=subcs.countByKC(c.getId());
            
            Column cc=new Column();
            cc.setId(c.getId());
            cc.setSubscribeCount(count);          
            
            if (b||count>0) {
                columnMapper.updateByPrimaryKeySelective(cc);
            }
            
        }
        
        if (b) {
            b=false;
        }
    }

}
