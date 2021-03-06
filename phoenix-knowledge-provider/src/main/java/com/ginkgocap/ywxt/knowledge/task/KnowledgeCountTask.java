package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.dao.impl.BaseDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by oem on 8/17/17.
 */
@Repository("knowledgeCountTask")
public class KnowledgeCountTask extends BaseDao implements InitializingBean
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCountTask.class);

    private int count = 0;

    private final static int MAX_NUM = 10000;
    private final static int defaultBatchSize = 50;
    private int expiredTime = 60 * 60 * 24;

    private Set<Long> hotCountSet = new HashSet<Long>(MAX_NUM);

    @Autowired
    private KnowledgeCountDao knowledgeCountDao;


    public void addIdToQueue(final long id) {
        hotCountSet.add(id);
    }

    public void setToCache(final KnowledgeCount count)
    {
        if (count != null) {
            final long knowledgeId = count.getId();
            String key = knowledgeCountKey(knowledgeId);
            this.saveToCache(key, count, expiredTime);
            addIdToQueue(knowledgeId);

        }
    }

    public KnowledgeCount getFromCache(final long knowledgeId)
    {
        String key = knowledgeCountKey(knowledgeId);
        return (KnowledgeCount)this.getFromCache(key);
    }

    public boolean deleteFromCache(final long knowledgeId)
    {
        String key = knowledgeCountKey(knowledgeId);
        this.removeFromCache(key);
        return true;
    }

    private String knowledgeCountKey(final long knowledgeId)
    {
        return "know_count_key_" + knowledgeId;
    }

    private List<KnowledgeCount> saveToDB(List<KnowledgeCount> knowledgeCountList, boolean complete)
    {
        if (knowledgeCountList != null && knowledgeCountList.size() > 0) {
            try {
                knowledgeCountDao.saveKnowledgeCountList(knowledgeCountList);
            } catch (Exception e) {
                logger.error("Save knowledge Count info failed: error: {}", e.getMessage());
                e.printStackTrace();
            }
        }
        if (complete) {
            hotCountSet.clear();
            return null;
        }
        else {
            return new ArrayList<KnowledgeCount>(defaultBatchSize);
        }
    }

    private void batchSaveCountResult()
    {
        logger.info("Knowledge Count batch save starting...");
        List<KnowledgeCount> KnowledgeCountList = null;
        if (hotCountSet.size() > defaultBatchSize) {
            KnowledgeCountList = new ArrayList<KnowledgeCount>(defaultBatchSize);
        } else {
            KnowledgeCountList = new ArrayList<KnowledgeCount>(hotCountSet.size());
        }
        for (long knowledgId : hotCountSet) {
            KnowledgeCount knowledgeCount = this.getFromCache(knowledgId);
            if (knowledgeCount != null) {
                KnowledgeCountList.add(knowledgeCount);
            }
            //Batch save 20
            if (KnowledgeCountList.size() >= defaultBatchSize) {
                KnowledgeCountList = saveToDB(KnowledgeCountList, false);
            }
        }
        saveToDB(KnowledgeCountList, true);
        logger.info("Knowledge Count batch save end...");
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                batchSaveCountResult();
                logger.info("KnowledgeCountTask 时间: " + new Date() + " 执行了" + count + "次"); // 1次
            }
        };

        //设置执行时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
        //定制每天的24:00:00执行，
        calendar.set(year, month, day, 24, 0, 00);
        Date date = calendar.getTime();
        Timer timer = new Timer();
        logger.info("current Date: " + date.toLocaleString());

        int period = 7200 * 1000;
        //// 定制每天的24:00:00执行， 每天的date时刻执行task，每隔2小时重复执行
        // timer.schedule(task, getDate(24), period);

        // 每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);

        // 每天的date时刻执行task, 仅执行一次
        // timer.schedule(task, 1000);

        // 立即开始执行, 2小时执行一次
        timer.schedule(task, 1000, period);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        logger.info("Knowledge Count save task timer starting...");
        startTimer();
        logger.info("Knowledge Count save task timer start complete...");
    }
}
