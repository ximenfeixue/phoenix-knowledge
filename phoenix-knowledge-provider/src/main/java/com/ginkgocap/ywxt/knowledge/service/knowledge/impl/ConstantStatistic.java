package com.ginkgocap.ywxt.knowledge.service.knowledge.impl;

/**   
 * 统计常用值  
 * <p>于2014-9-11 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 * @since <p>TODO 项目版本号</p> 
 *
 */
public enum ConstantStatistic {

    INFOMATION("info", "资讯热评"), LAW("law", "法律热评"), INDUSTRY("industry", "行业"), INVESTMENT("investment", "投融工具"), ASSET(
            "asset", "资产管理"), MACRO("macro", "宏观");
    String shortname;
    String description;

    ConstantStatistic(String shortname, String description) {
        this.shortname = shortname;
        this.description = description;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
