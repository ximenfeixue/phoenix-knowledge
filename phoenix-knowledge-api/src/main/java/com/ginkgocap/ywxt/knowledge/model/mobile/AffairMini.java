package com.ginkgocap.ywxt.knowledge.model.mobile;


import java.io.Serializable;
import java.util.List;
import com.alibaba.fastjson.JSON;

/**
 * Created by gintong on 2016/7/11.
 */
public class AffairMini implements Serializable {

    private static final long serialVersionUID = -8384944605448943537L;

    private Integer id; // 业务需求、任务、项目id

    private String name; // 发布人名字

    private String title; // 标题

    private String time; // 时间

    private Integer type; // 1-业务需求；2-任务；3-项目

    private String content; // 事务描述

    private String deadline; // 完成时间

    private Connections connections; //新增加关系列表

    private String requirementType; //存储需求类型

    private String reserve;//保留字段,需求：1-投资需求,2-融资需求,3-专家需求；任务：暂无；项目：暂无

    //获取知识拥有者id
    public Long getOwnerId(){
        if(null != connections){
            return connections.getOwnerId();
        }
        return new Long(0);
    }

    //获取关系拥有者名称
    public String getOwnerName(){
        if(null != connections){
            return connections.getOwnerName();
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public String getReserve() {
        return reserve;
    }

    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static AffairMini getAffairMiniByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, AffairMini.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static AffairMini getAffairMiniByJsonObject(Object jsonEntity) {
        return getAffairMiniByJsonString(jsonEntity.toString());
    }

    /**
     * @author zhangzhen
     * 如果没有数据，返回空数组
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static List<AffairMini> getAffairMiniListByJsonString(String object) {
        return JSON.parseArray(object, AffairMini.class);
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
    public static List<AffairMini> getAffairMiniListByJsonObject(Object object) {
        return getAffairMiniListByJsonString(object.toString());
    }

    public String getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }

}