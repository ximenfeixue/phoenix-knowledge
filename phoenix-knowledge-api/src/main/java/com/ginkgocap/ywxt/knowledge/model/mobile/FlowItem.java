package com.ginkgocap.ywxt.knowledge.model.mobile;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class FlowItem implements Serializable {

    private static final long serialVersionUID = -4049995003947589761L;

    private String id;  //"需求、事务id",

    private int newMatchCount; //新匹配的数量，0表示没新匹配

    private List<Comment> listComment; //最新三条评论

    private int commentCount; // 评论数量，是总数量。listComment中只返回部分，最多3个",

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNewMatchCount() {
        return newMatchCount;
    }

    public void setNewMatchCount(int newMatchCount) {
        this.newMatchCount = newMatchCount;
    }

    public List<Comment> getListComment() {
        return listComment;
    }

    public void setListComment(List<Comment> listComment) {
        this.listComment = listComment;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static FlowItem getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, FlowItem.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static FlowItem getByJsonObject(Object jsonEntity) {
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
    public static List<FlowItem> getListByJsonString(String object) {
        return JSON.parseArray(object, FlowItem.class);
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
    public static List<FlowItem> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }
}
