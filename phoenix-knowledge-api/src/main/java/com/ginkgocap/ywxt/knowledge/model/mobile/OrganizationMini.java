package com.ginkgocap.ywxt.knowledge.model.mobile;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;


public class OrganizationMini implements Serializable {

    private static final long serialVersionUID = 1665797765473286399L;

    private String id;//机构id

    private String logo;//机构logo图片url1

    private int guestType;//客户类型，1.金融机构 2一般企业 3.政府组织 4.中介机构 5.专业媒体 6.期刊报纸 7.研究机构 8.电视广播 9.互联网媒体,10.通用类型

    private int friendState;//0-好友；1-非好友；2-等待对方验证；3-对方请求我为好友待我通过

    private int joinState;//0-已加入机构、1-无关系、2-已申请加入机构，待审批、3-机构已邀请加入，待我审批

    private String fullName;//机构全称

    private String shortName;//客户简称

    private boolean isOnline;//是否线上机构

    private List<String> investTagList;//投资意向关键字

    private List<String> financingList;//融资意向关键字

    private String nameChar;

    private boolean isOffline;//是否线下人脉

    private List<String> listEmail;// 电子邮件集合

    private List<MobilePhone> listMobilePhone;//电话集合
    private List<MobilePhone>  listFixedPhone;//固定电话集合

    private String trade;//行业

    private Long ownerid; //人脉拥有者

    private String ownername;//人脉拥有者名称
    private String address;//机构地址


    public static OrganizationMini createDemo(){
        OrganizationMini self = new OrganizationMini();
        self.setId("" + 1);
        self.setFullName("你好，再见");
        self.setNameChar("N");
        self.setShortName("金桐");
        self.setGuestType(0);
        self.setIsOnline(true);
        self.setIsOffline(true);
        self.setFriendState(0);
        self.setJoinState(0);
        List<String> emial = new ArrayList<String>();
        emial.add("test@gintong.com");
        self.setListEmail(emial);
        List<MobilePhone> lmp = new ArrayList<MobilePhone>();
        MobilePhone m = new MobilePhone();
        m.setName("金桐");
        m.setMobile("38192831");
        lmp.add(m);
        self.setListMobilePhone(lmp);
        return self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getGuestType() {
        return guestType;
    }

    public void setGuestType(int guestType) {
        this.guestType = guestType;
    }

    public int getFriendState() {
        return friendState;
    }

    public void setFriendState(int friendState) {
        this.friendState = friendState;
    }

    public int getJoinState() {
        return joinState;
    }

    public void setJoinState(int joinState) {
        this.joinState = joinState;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public List<String> getInvestTagList() {
        return investTagList;
    }

    public void setInvestTagList(List<String> investTagList) {
        this.investTagList = investTagList;
    }

    public List<String> getFinancingList() {
        return financingList;
    }

    public void setFinancingList(List<String> financingList) {
        this.financingList = financingList;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNameChar() {
        return nameChar;
    }

    public void setNameChar(String nameChar) {
        this.nameChar = nameChar;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public List<String> getListEmail() {
        return listEmail;
    }

    public void setListEmail(List<String> listEmail) {
        this.listEmail = listEmail;
    }

    public List<MobilePhone> getListMobilePhone() {
        return listMobilePhone;
    }

    public void setListMobilePhone(List<MobilePhone> listMobilePhone) {
        this.listMobilePhone = listMobilePhone;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static OrganizationMini getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, OrganizationMini.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static OrganizationMini getByJsonObject(Object jsonEntity) {
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
    public static List<OrganizationMini> getListByJsonString(String object) {
        return JSON.parseArray(object, OrganizationMini.class);
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
    public static List<OrganizationMini> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }

    public Long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Long ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<MobilePhone> getListFixedPhone() {
        return listFixedPhone;
    }

    public void setListFixedPhone(List<MobilePhone> listFixedPhone) {
        this.listFixedPhone = listFixedPhone;
    }
}
