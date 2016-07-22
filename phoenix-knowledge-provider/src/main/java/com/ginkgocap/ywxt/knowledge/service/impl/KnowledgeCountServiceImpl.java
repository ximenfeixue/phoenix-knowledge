package com.ginkgocap.ywxt.knowledge.service.impl;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeCountDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeCount;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Chen Peifeng on 2016/5/24.
 */
@Service("knowledgeCountService")
public class KnowledgeCountServiceImpl implements KnowledgeCountService, InitializingBean
{

    private final static Logger logger = LoggerFactory.getLogger(KnowledgeCountServiceImpl.class);

    private final int defaultLimit = 50;
    private final int MAX_NUM = 500;

    private ConcurrentMap<Long, KnowledgeCount> hotCountMap = new ConcurrentHashMap<Long, KnowledgeCount>(MAX_NUM);
    /**知识简表*/
    @Autowired
    private KnowledgeCountDao knowledgeCountDao;

    private int count =0;
    //private volatile boolean timerStarted = false;

    @Override
    public boolean updateClickCount(long userId,long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(userId, knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setClickCount(knowledgeCount.getClickCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateShareCount(long userId,long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(userId, knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setShareCount(knowledgeCount.getClickCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCollectCount(long userId,long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(userId, knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCollectCount(knowledgeCount.getClickCount() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCommentCount(long userId,long knowledgeId,short type)
    {
        KnowledgeCount knowledgeCount = getKnowledgeCount(userId, knowledgeId, type);
        if (knowledgeCount != null) {
            knowledgeCount.setCommentCount(knowledgeCount.getClickCount() + 1);
            return true;
        }
        return false;
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

    @Override
    public KnowledgeCount getKnowledgeCount(long userId, long knowledgeId, short type)
    {
        KnowledgeCount knowledgeCount = hotCountMap.get(knowledgeId);
        if (knowledgeCount != null ) {
            return knowledgeCount;
        }

        try {
            knowledgeCount = knowledgeCountDao.getKnowledgeCount(knowledgeId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (knowledgeCount == null) {
            knowledgeCount = new KnowledgeCount();
            knowledgeCount.setId(knowledgeId);
            knowledgeCount.setUserId(userId);
            knowledgeCount.setType(type);
            try {
                knowledgeCountDao.saveKnowledgeCount(knowledgeCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return knowledgeCount;
        }

        hotCountMap.put(knowledgeId, knowledgeCount);
        return knowledgeCount;
    }

    private void batchSaveCountResult()
    {
        List<KnowledgeCount> KnowledgeCountList = new ArrayList<KnowledgeCount>(20);
        Set<Map.Entry<Long,KnowledgeCount>> knowledgeCountEntry = hotCountMap.entrySet();
        for (Map.Entry<Long,KnowledgeCount> knowledgeCount : knowledgeCountEntry) {
            KnowledgeCountList.add(knowledgeCount.getValue());
            //Batch save 20
            if (KnowledgeCountList.size() >= 20) {
                KnowledgeCountList = saveToDB(KnowledgeCountList);
            }
        }
        saveToDB(KnowledgeCountList);
    }

    private List<KnowledgeCount> saveToDB(List<KnowledgeCount> knowledgeCountList)
    {
        try {
            knowledgeCountDao.saveKnowledgeCountList(knowledgeCountList);
        } catch (Exception e) {
            logger.error("Save knowledge Count info failed: error: {}", e.getMessage());
            e.printStackTrace();
        } 
        return new ArrayList<KnowledgeCount>(20);
        
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
        Calendar calendar =Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
        //定制每天的24:00:00执行，
        calendar.set(year, month, day, 24, 0, 00);
        Date date = calendar.getTime();
        Timer timer = new Timer();
        System.out.println(date);

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
}
