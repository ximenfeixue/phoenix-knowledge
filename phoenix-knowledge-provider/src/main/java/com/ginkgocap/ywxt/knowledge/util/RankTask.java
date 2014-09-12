package com.ginkgocap.ywxt.knowledge.util;


import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
  
/**
 * 排行任务
 * <p>于2014-9-11 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 *
 */
@Component("rankTask")
public class RankTask {

    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    RankSchedule schedule;
    
    public void startStatistics(String[] obj) {
        schedule.setObj(obj);
        Future future = threadPoolTaskExecutor.submit(schedule);
        // future.get();
    }
}
