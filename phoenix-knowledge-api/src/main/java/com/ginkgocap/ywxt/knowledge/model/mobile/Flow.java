package com.ginkgocap.ywxt.knowledge.model.mobile;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class Flow implements Serializable{

    private static final long serialVersionUID = -8327503816104602549L;

    private String id;

    private String title; ;//动态标题，左上角部分

    private String source; //来自谁，对于需求，指发布者

    private int type;//动态类型： 1-投资；2-融资；3-业务需求；4-任务；5-项目；6-通知 ；7-知识

    private String content;//动态实体内容，显示在中间正文部分

    private String time;//发布时间

    private String url;//知识 对应的url

    private FlowItem flowItem; //当为 需求、任务、项目、业务需求时，该对象内容有效

    private String headImage;//首页用户头像

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FlowItem getFlowItem() {
        return flowItem;
    }

    public void setFlowItem(FlowItem flowItem) {
        this.flowItem = flowItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static Flow getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, Flow.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static Flow getByJsonObject(Object jsonEntity) {
        return getByJsonString(jsonEntity.toString());
    }

    /**
     * @author zhangzhen
     * 如果没有数据，返回空数组
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static List<Flow> getListByJsonString(String object) {
        return JSON.parseArray(object, Flow.class);
    }

    /**
     * @author zhangzhen
     * @CreateTime 2014-11-11
     * 如果没有数据，返回空数组
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("EntityList");
     * */
    public static List<Flow> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }
}
