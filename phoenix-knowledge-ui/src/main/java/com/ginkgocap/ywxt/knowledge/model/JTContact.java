package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ginkgocap.ywxt.knowledge.model.mobile.Connections;
import com.ginkgocap.ywxt.knowledge.model.mobile.JTFile;
import com.ginkgocap.ywxt.message.letter.model.Person;
import com.ginkgocap.ywxt.user.model.User;
/**
 * Created by gintong on 2016/7/23.
 */
public class JTContact implements Serializable
{
    private static final long serialVersionUID = 2963173897987052164L;

    private int id;//用户名称,如果用户信息没完善，该 person对象整体为null

    private boolean isOnline;//是否线上人脉
    private boolean isOffline;//是否线下人脉
    private int friendState;//0-好友；1-非好友；2-等待对方验证；3-对方请求我为好友待我通过
    private boolean isWorkmate;//是否同事
    private String fromDes;//人脉来源描述， 如XX引荐、我主要请求、对方请求等

    private List<Connections> listConnections;
    private List<WorkExperience> listWorkExperience;
    private List<EduExperience> listEduExperience;

    private InvestKeyword inInvestKeyword; //投资关键字

    private InvestKeyword outInvestKeyword; //融资关键字

    private String image; //头像

    private String taskId;

    private User user;

    private Person people;

//	private List<PeopleSelectTag> myPeopleSelectTagList;
//
//	private List<PeopleGroupTemp> myPeopleGroupTempList;
//
//	private List<PeopleCustomer> myPeopleCustomerList;

    private List<JTFile> jtFileList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }
    public boolean getIsOffline() {
        return isOffline;
    }
    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public int getFriendState() {
        return friendState;
    }

    public void setFriendState(int friendState) {
        this.friendState = friendState;
    }

    public boolean isWorkmate() {
        return isWorkmate;
    }

    public void setWorkmate(boolean isWorkmate) {
        this.isWorkmate = isWorkmate;
    }

    public String getFromDes() {
        return fromDes;
    }

    public void setFromDes(String fromDes) {
        this.fromDes = fromDes;
    }

    public List<Connections> getListConnections() {
        return listConnections;
    }

    public void setListConnections(List<Connections> listConnections) {
        this.listConnections = listConnections;
    }

    public List<WorkExperience> getListWorkExperience() {
        return listWorkExperience;
    }

    public void setListWorkExperience(List<WorkExperience> listWorkExperience) {
        this.listWorkExperience = listWorkExperience;
    }

    public List<EduExperience> getListEduExperience() {
        return listEduExperience;
    }

    public void setListEduExperience(List<EduExperience> listEduExperience) {
        this.listEduExperience = listEduExperience;
    }

    public InvestKeyword getInInvestKeyword() {
        return inInvestKeyword;
    }

    public void setInInvestKeyword(InvestKeyword inInvestKeyword) {
        this.inInvestKeyword = inInvestKeyword;
    }

    public InvestKeyword getOutInvestKeyword() {
        return outInvestKeyword;
    }

    public void setOutInvestKeyword(InvestKeyword outInvestKeyword) {
        this.outInvestKeyword = outInvestKeyword;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person getPeople() {
        return people;
    }

    public void setPeople(Person people) {
        this.people = people;
    }
    /*
        public List<PeopleSelectTag> getMyPeopleSelectTagList() {
            return myPeopleSelectTagList;
        }

        public void setMyPeopleSelectTagList(List<PeopleSelectTag> myPeopleSelectTagList) {
            this.myPeopleSelectTagList = myPeopleSelectTagList;
        }

        public List<PeopleGroupTemp> getMyPeopleGroupTempList() {
            return myPeopleGroupTempList;
        }

        public void setMyPeopleGroupTempList(List<PeopleGroupTemp> myPeopleGroupTempList) {
            this.myPeopleGroupTempList = myPeopleGroupTempList;
        }

        public List<PeopleCustomer> getMyPeopleCustomerList() {
            return myPeopleCustomerList;
        }

        public void setMyPeopleCustomerList(List<PeopleCustomer> myPeopleCustomerList) {
            this.myPeopleCustomerList = myPeopleCustomerList;
        }
    */
    public List<JTFile> getJtFileList() {
        return jtFileList;
    }

    public void setJtFileList(List<JTFile> jtFileList) {
        this.jtFileList = jtFileList;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static JTContact getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, JTContact.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static JTContact getByJsonObject(Object jsonEntity) {
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
    public static List<JTContact> getListByJsonString(String object) {
        return JSON.parseArray(object, JTContact.class);
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
    public static List<JTContact> getListByJsonObject(Object object) {
        return getListByJsonString(object.toString());
    }
}
