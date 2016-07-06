package com.ginkgocap.ywxt.knowledge.model.mobile;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gintong on 2016/7/6.
 */
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 3799748746576697718L;

    //标题 (名称)
    private String title;

    // 业务扩展项
    private String time;

    // 主键
    private long id;

    // 内容 （描述）
    private String content;

    private String image;

    // 业务扩展项
    private String source;

    //0 女 1男 2未知
    private String gender;

    //组织：行业(多个逗号隔开)
    private String industrys;

    //地区
    private String area;

    //公司
    private String company;

    //职位
    private String position;

    //地址
    private String address;

    //类型名称 （组织类型）
    private String typeName;

    //区分 用户与人脉 / 组织与客户
    private int flag;

    //业务产品线
    private int productType;

    private long createbyid;

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustrys() {
        return industrys;
    }

    public void setIndustrys(String industrys) {
        this.industrys = industrys;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    //知识所需类型 和　客户: 0客户 1组织
    private int type;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static SearchResult getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, SearchResult.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static SearchResult getByJsonObject(Object jsonEntity) {
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
    public static List<SearchResult> getListByJsonString(String object) {
        return JSON.parseArray(object, SearchResult.class);
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
    public static List<SearchResult> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getCreatebyid() {
        return createbyid;
    }

    public void setCreatebyid(long createbyid) {
        this.createbyid = createbyid;
    }
}
