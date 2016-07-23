package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.model.mobile.Flow;
import com.ginkgocap.ywxt.knowledge.model.mobile.SearchResult;

public class Page implements Serializable {
    private static final long serialVersionUID = -1952086533379737985L;

    private int total;//总数

    private int size;//该页内容数

    private int index;//页码 从1开始

    private List<Flow> listFlow; //存放的对象 。可是任意实体类

    private List<SearchResult> listSearchResult; //存放的对象 。可是任意实体类

    private List<KnowledgeMini> listKnowledge; //存放的对象 。可是任意实体类

    private List<KnowledgeMini2> listKnowledgeMini;

    private List<Object> list; //存放任意实体

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Flow> getListFlow() {
        return listFlow;
    }

    public void setListFlow(List<Flow> listFlow) {
        this.listFlow = listFlow;
    }

    public List<SearchResult> getListSearchResult() {
        return listSearchResult;
    }

    public void setListSearchResult(List<SearchResult> listSearchResult) {
        this.listSearchResult = listSearchResult;
    }

    public List<KnowledgeMini> getListKnowledge() {
        return listKnowledge;
    }

    public void setListKnowledge(List<KnowledgeMini> listKnowledge) {
        this.listKnowledge = listKnowledge;
    }

    public List<KnowledgeMini2> getListKnowledgeMini() {
        return listKnowledgeMini;
    }

    public void setListKnowledgeMini(List<KnowledgeMini2> listKnowledgeMini) {
        this.listKnowledgeMini = listKnowledgeMini;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static Page getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, Page.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static Page getByJsonObject(Object jsonEntity) {
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
    public static List<Page> getListByJsonString(String object) {
        return JSON.parseArray(object, Page.class);
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
    public static List<Page> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }
}
