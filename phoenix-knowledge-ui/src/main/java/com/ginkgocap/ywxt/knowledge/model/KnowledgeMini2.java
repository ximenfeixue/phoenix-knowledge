package com.ginkgocap.ywxt.knowledge.model;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.model.Connections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/5/17.
 */
public class KnowledgeMini2  implements Serializable {
    private static final long serialVersionUID = 5946644419907638023L;

    private long id;// 知识id

    private int type;// 类型：1-资讯，2-投融工具，3-行业，4-经典案例，5-图书报告，6-资产管理，7-宏观，8-观点，9-判例，10-法律法规，11-文章

    private String title;// 标题

    private String desc;// 描述

    private String source;// 来源

    private long modifytime;// 最后修改时间

    private String pic;// 封面地址

    private String tag;// 标签

    private String url;//知识链接

    private List<Long> listTag;

    private Connections connections;//关系集合
    private String name;//在生态关联中， 知识标题为 name，需要设置为title


    private String columnpath; //栏目路径
    private int columntype; //知识类型

    private long shareMeId;//分享给我的知识记录id

    public long getShareMeId() {
        return shareMeId;
    }

    public void setShareMeId(long shareMeId) {
        this.shareMeId = shareMeId;
    }

    /*
     * 创建一个模拟假数据的对象
     * @param: id , 模拟知识对象id
     */
    public static KnowledgeMini2 createDemo(int id){
        KnowledgeMini2 self = new KnowledgeMini2();
        self.setId(id);
        self.setPic("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        self.setTitle("测试标题" + id);
        self.setDesc("测试描述" + id);
        self.setSource("手工输入" + id);
        self.setTag("标签" + id + ";");

        Connections connections = Connections.createDemo(id+1,true);
        self.setConnections(connections);
        return self;
    }

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

    /**
     * 知识id
     * */
    public long getId() {
        return id;
    }

    /**
     * 知识id
     * */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 标题
     * */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     * */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 描述
     * */
    public String getDesc() {
        return desc;
    }

    /**
     * 描述
     * */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 来源
     * */
    public String getSource() {
        return source;
    }

    /**
     * 来源
     * */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 最后修改时间
     * */
    public long getModifytime() {
        return modifytime;
    }

    /**
     * 最后修改时间
     * */
    public void setModifytime(long modifytime) {
        this.modifytime = modifytime;
    }

    /**
     * 封面地址
     * */
    public String getPic() {
        return pic;
    }

    /**
     * 封面地址
     * */
    public void setPic(String pic) {
        this.pic = pic;
    }

    /**
     * 标签
     * */
    public String getTag() {
        return tag;
    }

    /**
     * 标签
     * */
    public void setTag(String tag) {
        this.tag = tag;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public List<Long> getListTag() {
        return listTag;
    }
    public void setListTag(List<Long> listTag) {
        this.listTag = listTag;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static KnowledgeMini2 getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return KnowledgeUtil.readValue(KnowledgeMini2.class, jsonEntity);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static KnowledgeMini2 getByJsonObject(Object jsonEntity) {
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
    public static List<KnowledgeMini2> getListByJsonString(String object) {
        return JSON.parseArray(object, KnowledgeMini2.class);
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
    public static List<KnowledgeMini2> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }

    public String getColumnpath() {
        return columnpath;
    }
    public void setColumnpath(String columnpath) {
        this.columnpath = columnpath;
    }

    public int getColumntype() {
        return columntype;
    }

    public void setColumntype(int columntype) {
        this.columntype = columntype;
    }

    /** 虚假数据填充项 */
    static Page getSpPage() {
        Page page = new Page();
        List<KnowledgeMini2> km2s = new ArrayList<KnowledgeMini2>();
//		Connections connections = Connections.createDemo(1, true);
        for (int i = 1; i < 5; i++) {
            km2s.add(KnowledgeMini2.createDemo(i));
        }
        page.setIndex(0);/** 当前页码 */
        page.setSize(4);/** 该页内容数 */
        page.setListKnowledgeMini(km2s);/** 集合 */
        page.setTotal(4);
        return page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
