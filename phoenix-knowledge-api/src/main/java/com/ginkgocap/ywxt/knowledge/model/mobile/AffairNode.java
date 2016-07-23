package com.ginkgocap.ywxt.knowledge.model.mobile;

/**
 * Created by gintong on 2016/7/11.
 */
import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.AffairMini;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;

public class AffairNode {

    private String type;

    private String memo;

    private List<AffairMini> listAffairMini;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<AffairMini> getListAffairMini() {
        return listAffairMini;
    }

    public void setListAffairMini(List<AffairMini> listAffairMini) {
        this.listAffairMini = listAffairMini;
    }

    /**
     * @author xuxinjian
     * @return 如果数据为空返回null
     * @des 从平台层的knowledge对象中的asso属性中， 提取出AffairNode，只填满相关字段即可
     * */
    public static AffairNode getByAssoJson(JSONObject object) {
        try{
            AffairNode self = new AffairNode();
            self.memo = object.optString("tag");
            JSONArray conNodeArray = object.optJSONArray("conn");
            self.listAffairMini = AffairMini.getAffairMiniListByJsonObject(conNodeArray);
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
    public static AffairNode getByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, AffairNode.class);
    }

    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * */
    public static AffairNode getByJsonObject(Object jsonEntity) {
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
    public static List<AffairNode> getListByJsonString(String object) {
        return JSON.parseArray(object, AffairNode.class);
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
    public static List<AffairNode> getListByJsonObject(Object object) {
        try{
            List<AffairNode> ret = getListByJsonString(object.toString());
            return ret;
        }catch (Exception e){
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
