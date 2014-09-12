package com.ginkgocap.ywxt.knowledge.util;

import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 排行计划
 * <p>于2014-9-11 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 */
@Component
public class RankSchedule implements Callable<String> {

    @Autowired
    private RankStatistic rs;

    private String[] obj;

    public String[] getObj() {
        return obj;
    }

    public void setObj(String[] obj) {
        this.obj = obj;
    }

    @Override
    public String call() throws Exception {
        rs.run(obj[0]);
        return null;
    }

}
