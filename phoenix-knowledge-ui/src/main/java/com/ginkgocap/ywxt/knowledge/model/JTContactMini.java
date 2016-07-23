package com.ginkgocap.ywxt.knowledge.model;

/**
 * Created by gintong on 2016/7/6.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.model.mobile.MobilePhone;

public class JTContactMini implements Serializable {

    private static final long serialVersionUID = -2615189450024250981L;

    private String id;//用户名称,如果用户信息没完善，该 person对象整体为null

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String nameChar;//名首字母缩写

    private String name;//名
    private String email;//名

    /**
     * 安卓权限备用姓名项
     */
    private String mName;

    private int gender;//性别

    private String lastName;//姓

    private String company;//公司

    private boolean isOnline;//是否线上人脉

    private boolean isOffline;//是否线上人脉

    private String image;//头像

    @SuppressWarnings("rawtypes")
    private List listOrganizationID;

    private Integer friendState;//0-好友；1-非好友；2-等待对方验证；3-对方请求我为好友待我通过

    private boolean isWorkmate;//是否同事

    private List<String> listEmail;//email

    private List<MobilePhone> listMobilePhone; //手机
    /**
     * 固话 人脉显示  hdyAdd 2015.01.19
     */
    private List<MobilePhone> listFixedPhone;

    private Long ownerid; //人脉拥有者

    private String ownername;//人脉拥有者名称

    private String career;//职业

    private String fromType;//人脉来源类型：1-我创建的；2-我保存的；3-我收藏的; 9-其他

    private String time; //模糊定义的时间 人脉与用户通用

    public static final String FROM_TYPE_CREATED = "1";
    public static final String FROM_TYPE_SAVED = "2";
    public static final String FROM_TYPE_REFERCNCE = "3";
    public static final String FROM_TYPE_OTHER = "9";


    public static JTContactMini createDemo() {
        JTContactMini self = new JTContactMini();
        self.setId("2");
        self.setName("开");
        self.setLastName("发");
        self.setNameChar("k");
        self.setCompany("金桐");
        self.setIsOffline(true);
        self.setIsOnline(true);
        self.setFriendState(0);
        self.setIsWorkmate(true);
        List<String> listemial = new ArrayList<String>();
        listemial.add("test@gintong.com");
        self.setListEmail(listemial);
        List<MobilePhone> listMobilePhone = new ArrayList<MobilePhone>();
        MobilePhone mp = new MobilePhone();
        mp.setName("test");
        mp.setMobile("1101210111");
        listMobilePhone.add(mp);
        self.setListMobilePhone(listMobilePhone);
        return self;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public boolean getIsOffline() {
        return isOffline;
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean getIsWorkmate() {
        return isWorkmate;
    }

    public void setIsWorkmate(boolean isWorkmate) {
        this.isWorkmate = isWorkmate;
    }

    @SuppressWarnings("rawtypes")
    public List getListOrganizationID() {
        return listOrganizationID;
    }

    @SuppressWarnings("rawtypes")
    public void setListOrganizationID(List listOrganizationID) {
        this.listOrganizationID = listOrganizationID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setFriendState(Integer friendState) {
        this.friendState = friendState;
    }

    public Integer getFriendState() {
        return friendState;
    }

    public void setWorkmate(boolean isWorkmate) {
        this.isWorkmate = isWorkmate;
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

    public String getNameChar() {
        return nameChar;
    }

    public void setNameChar(String nameChar) {
        this.nameChar = nameChar;
    }

    public String getId() {
        return id;
    }

    public void firstName(String id) {
        this.id = id;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     * <p/>
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     */
    public static JTContactMini getByJsonString(String jsonEntity) {
        if (jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, JTContactMini.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     * <p/>
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     */
    public static JTContactMini getByJsonObject(Object jsonEntity) {
        return getByJsonString(jsonEntity.toString());
    }

    /**
     * @author zhangzhen
     * 如果没有数据，返回空数组
     * <p/>
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     */
    public static List<JTContactMini> getListByJsonString(String object) {
        return JSON.parseArray(object, JTContactMini.class);
    }

    /**
     * @author zhangzhen
     * @CreateTime 2014-11-11
     * 如果没有数据，返回空数组
     * <p/>
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("EntityList");
     */
    public static List<JTContactMini> getListByJsonObject(Object object) {
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

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public List<MobilePhone> getListFixedPhone() {
        return listFixedPhone;
    }

    public void setListFixedPhone(List<MobilePhone> listFixedPhone) {
        this.listFixedPhone = listFixedPhone;
    }


    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}