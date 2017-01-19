package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.model.mobile.DataSync;
import com.ginkgocap.ywxt.knowledge.service.DataSyncService;
import com.ginkgocap.ywxt.knowledge.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oem on 1/19/17.
 */
public class DataSyncScheduler implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(DataSyncScheduler.class);

    @Autowired
    DataSyncService dataSyncService;

    @Autowired
    DataSyncTask dataSyncTask;

    private int count = 0;

    public void dataSyncTask()
    {
        //First get 50
        List<DataSync> dataSyncList = dataSyncService.getDataSyncList();
        if (dataSyncList != null && dataSyncList.size() >0) {
            for (DataSync data : dataSyncList) {
                dataSyncTask.addQueue(data);
            }
        }
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                dataSyncTask();
                String nowDate = DateUtil.convertDateToString(new Date());
                logger.info("时间=" + nowDate + " 执行了" + count + "次"); // 1次
            }
        };

        Timer timer = new Timer();
        int period = 7200 * 1000;
        //// 定制每天的24:00:00执行， 每天的date时刻执行task，每隔2小时重复执行
        // timer.schedule(task, getDate(24), period);

        // 每天的date时刻执行task, 仅执行一次
        // timer.schedule(task, 1000);

        // 立即开始执行, 2小时执行一次
        timer.schedule(task, 1000, period);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("DataSyncTask starting........");
        new Thread(dataSyncTask).start();
        //startTimer();
        logger.info("DataSyncTask start completed........");
    }
}
