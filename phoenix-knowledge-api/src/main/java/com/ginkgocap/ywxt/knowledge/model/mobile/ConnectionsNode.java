package com.ginkgocap.ywxt.knowledge.model.mobile;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;

/**
 * @version [1.0]
 * @CreateTime 2014-10-31 人脉(组织)对象组
 * */

public class ConnectionsNode implements Serializable {

    private static final long serialVersionUID = 2149680763813662210L;

    private String type;

    private String memo;

    private List<Connections> listConnections;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<Connections> getListConnections() {
        return listConnections;
    }

    public void setListConnections(List<Connections> listConnections) {
        this.listConnections = listConnections;
    }

    /**
     * @author xuxinjian
     * @return 如果数据为空返回null
     * @des 从平台层的knowledge对象中的asso属性中， 提取出connectionNode，只填满相关字段即可
     * */
    public static ConnectionsNode getByAssoJson(JSONObject object) {
        try{
            ConnectionsNode self = new ConnectionsNode();
            self.memo = object.optString("tag");
            JSONArray conNodeArray = object.optJSONArray("conn");
            self.listConnections = Connections.createListSimpleFromAssoJson(conNodeArray);
            return self;
        }catch(Exception e){

        }
        return null;
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * */
    public static ConnectionsNode getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, ConnectionsNode.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static ConnectionsNode getByJsonObject(Object jsonEntity) {
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
    public static List<ConnectionsNode> getListByJsonString(String object) {
        return JSON.parseArray(object, ConnectionsNode.class);
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
    public static List<ConnectionsNode> getListByJsonObject(Object object) {
        try{
            List<ConnectionsNode> ret =  getListByJsonString(object.toString());
            return ret;
        }catch(Exception e){
            return null;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}