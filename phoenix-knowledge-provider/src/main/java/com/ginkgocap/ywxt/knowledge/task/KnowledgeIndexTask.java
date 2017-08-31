package com.ginkgocap.ywxt.knowledge.task;

import com.ginkgocap.ywxt.knowledge.dao.KnowledgeIndexDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeType;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by oem on 8/17/17.
 */
@Repository("knowledgeIndexTask")
public class KnowledgeIndexTask implements InitializingBean
{
    private final static Logger logger = LoggerFactory.getLogger(KnowledgeIndexTask.class);

    @Autowired
    private KnowledgeIndexDao knowledgeIndexDao;

    private int count = 0;
    //private final static int period = 3600 * 1000 * 24;
    private final static int defaultPage = 10;
    private final static int maxSize = 50;

    private void knowledgeIndexClean()
    {
        for(int type = KnowledgeType.ENews.value(); type <= KnowledgeType.EGovernment.value(); type++) {
            List<KnowledgeBase> baseList = knowledgeIndexDao.getKnowledgeIndexList((short)type, type, defaultPage, maxSize);
            while (CollectionUtils.isNotEmpty(baseList)) {
                logger.info("query knowledge index. type: " + type + " size: " + baseList.size());
                List<Long> idList = new ArrayList<Long>(baseList.size());
                for (KnowledgeBase base : baseList) {
                    if (base != null) {
                        idList.add(base.getId());
                    }
                }
                boolean result = knowledgeIndexDao.deleteKnowledgeIndex(idList);
                if (!result) {
                    logger.error("Clean up knowledge failed. idList: " + idList);
                }
                baseList = knowledgeIndexDao.getKnowledgeIndexList((short)type, type, defaultPage, maxSize);
            }
        }
    }

    private void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                ++count;
                knowledgeIndexClean();
                logger.info("KnowledgeIndex 时间=" + new Date() + " 执行了" + count + "次"); // 1次
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
        //每天的date时刻执行task，每隔24小时重复执行
        timer.schedule(task, date, period);
        //每天的date时刻执行task, 仅执行一次
        //timer.schedule(task, date);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Knowledge Index task timer starting...");
        startTimer();
        logger.info("Knowledge Index task timer start complete...");
    }
}
