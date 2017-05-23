package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.cache.Cache;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
@Service("knowledgeCountService")
public class KnowledgeCountServiceImpl implements KnowledgeCountService, InitializingBean
{
    private final Logger logger = LoggerFactory.getLogger(KnowledgeCountServiceImpl.class);

    private final int defaultLimit = 50;
    private final int MAX_NUM = 10000;
    private final int defaultBatchSize = 50;

    private Set<Long> hotCountSet = new HashSet<Long>(MAX_NUM);
    /**知识简表*/
    @Autowired
    private KnowledgeCountDao knowledgeCountDao;

    @Autowired
    private Cache cache;

    private int count = 0;

    private int expiredTime = 60 * 60 * 24;

    @Override
    public KnowledgeCount updateClickCount(long userId, String userName, String title, long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setUserId(userId);
            knowledgeCount.setUserName(userName);
            knowledgeCount.setTitle(title);
            knowledgeCount.setClickCount(knowledgeCount.getClickCount() + 1);
            this.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateShareCount(long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setShareCount(knowledgeCount.getShareCount() + 1);
            this.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateCollectCount(long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCollectCount(knowledgeCount.getCollectCount() + 1);
            this.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public KnowledgeCount updateCommentCount(long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCommentCount(knowledgeCount.getCommentCount() + 1);
            this.setToCache(knowledgeCount);
        }
        return knowledgeCount;
    }

    @Override
    public List<KnowledgeCount> getHotKnowledge(int size)
    {
        size = size > 0 ? size : defaultLimit;
        return knowledgeCountDao.getHotKnowledge(size);
    }

    public List<KnowledgeCount> getHotKnowledgeByPage(int start,int size)
    {
        return knowledgeCountDao.getHotKnowledgeByPage(start, size);
    }

    //Only check exist or not
    @Override
    public KnowledgeCount getKnowledgeCount(long knowledgeId)
    {
        try {
            //return knowledgeCountDao.getKnowledgeCount(knowledgeId);
            KnowledgeCount knowledgeCount = this.getFromCache(knowledgeId);
            if (knowledgeCount != null ) {
                return knowledgeCount;
            }
            knowledgeCount = knowledgeCountDao.getKnowledgeCount(knowledgeId);
            if (knowledgeCount != null) {
                this.setToCache(knowledgeCount);
            }
            return knowledgeCount;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Long, Long> getKnowledgeCount(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return null;
        }

        Map<Long, Long> map = new HashMap<Long, Long>(idList.size());
        for (Long id : idList) {
            if (id != null) {
                KnowledgeCount knowledgeCount = getKnowledgeCount(id);
                if (knowledgeCount != null) {
                    map.put(id, knowledgeCount.getClickCount());
                } else {
                    logger.warn("get knowlegde count object failed. knowledgeId: " + id);
                }
            }
        }
        return map;
    }

    @Override
    public KnowledgeCount getKnowledgeCount(long knowledgeId, short type)
    {
        KnowledgeCount knowledgeCount = this.getFromCache(knowledgeId);
        if (knowledgeCount != null ) {
            return knowledgeCount;
        }

        try {
            knowledgeCount = knowledgeCountDao.getKnowledgeCount(knowledgeId);
        } catch (Exception e) {
            logger.error("get knowlegde count object failed. knowledgeId: " + knowledgeId);
            e.printStackTrace();
        }

        if (knowledgeCount == null) {
            knowledgeCount = new KnowledgeCount();
            knowledgeCount.setId(knowledgeId);
            //knowledgeCount.setUserId(userId);
            knowledgeCount.setType(type);
            try {
                knowledgeCountDao.saveKnowledgeCount(knowledgeCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return knowledgeCount;
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

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                batchSaveCountResult();
                logger.info("时间=" + new Date() + " 执行了" + count + "次"); // 1次
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
        //每天的date时刻执行task，每隔2小时重复执行
        timer.schedule(task, date, period);
        //每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        logger.info("Knowledge Count save timer starting...");
        startTimer();
        logger.info("Knowledge Count save timer start complete...");
    }

    private void setToCache(KnowledgeCount count)
    {
        if (count != null) {
            final long knowledgeId = count.getId();
            String key = knowledgeCountKey(knowledgeId);
            this.cache.setByRedis(key, count, expiredTime);
            if (!hotCountSet.contains(knowledgeId)) {
                hotCountSet.add(knowledgeId);
            }
        }
    }

    private KnowledgeCount getFromCache(long knowledgeId)
    {
        String key = knowledgeCountKey(knowledgeId);
        return (KnowledgeCount)this.cache.getByRedis(key);
    }

    private String knowledgeCountKey(long knowledgeId)
    {
        return "know_count_key_" + knowledgeId;
    }
}
