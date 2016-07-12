package com.ginkgocap.ywxt.knowledge.model.mobile;

import java.io.Serializable;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;

/**
 * Created by gintong on 2016/7/11.
 */
public class KnowledgeNode implements Serializable {

    private static final long serialVersionUID = -5218249226486256820L;

    private String type;

    private String memo;

    private List<KnowledgeMini2> listKnowledgeMini2;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public List<KnowledgeMini2> getListKnowledgeMini2() {
        return listKnowledgeMini2;
    }

    public void setListKnowledgeMini2(List<KnowledgeMini2> listKnowledgeMini2) {
        this.listKnowledgeMini2 = listKnowledgeMini2;
    }

    /**
     * @author xuxinjian
     * @return 如果数据为空返回null
     * @des 从平台层的knowledge对象中的asso属性中， 提取出KnowledgeNode，只填满相关字段即可
     * */
    public static KnowledgeNode getByAssoJson(JSONObject object) {
        try{
            KnowledgeNode self = new KnowledgeNode();
            self.memo = object.optString("tag");
            JSONArray conNodeArray = object.optJSONArray("conn");
            self.listKnowledgeMini2 = KnowledgeMini2.getListByJsonObject(conNodeArray);
            if(self.listKnowledgeMini2 != null){
                for(int i = 0; i < self.listKnowledgeMini2.size(); i++){
                    KnowledgeMini2 mini = self.listKnowledgeMini2.get(i);
                    mini.setType(mini.getColumntype());//对生态关联的type转换为知识的type
                    if(StringUtils.isBlank(mini.getTitle()))
                        mini.setTitle(mini.getName());//如果是生态关联，将name设置到type中去
                }
            }
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
     * getKnowledgeNodeByJsonObject(jsonData);
     * */
    public static KnowledgeNode getKnowledgeNodeByJsonString(String jsonEntity) {
        if(jsonEntity.equals("{}")) {
            return null; //无数据判断
        }
        return JSON.parseObject(jsonEntity, KnowledgeNode.class);
    }
    /**
     * @author zhangzhen
     * 如果数据为空返回null
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("Entity");
     * getKnowledgeNodeByJsonObject(jsonData);
     * */
    public static KnowledgeNode getKnowledgeNodeByJsonObject(Object jsonEntity) {
        return getKnowledgeNodeByJsonString(jsonEntity.toString());
    }

    /**
     * @author zhangzhen
     * 如果没有数据，返回空数组
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * String jsonData = j.getString("Entity");
     * getKnowledgeListNodeByJsonObject(jsonData);
     * */
    public static List<KnowledgeNode> getKnowledgeNodeListByJsonString(String object) {
        return JSON.parseArray(object, KnowledgeNode.class);
    }

    /**
     * @author zhangzhen
     * @CreateTime 2014-11-11
     * 如果没有数据，返回空数组
     *
     * 指导使用方法
     * JSONObject j = JSONObject.fromObject(requestJson);
     * Object jsonData = j.get("EntityList");
     * getKnowledgeListNodeByJsonObject(jsonData);
     * */
    public static List<KnowledgeNode> getKnowledgeNodeListByJsonObject(Object object) {
        try{
            List<KnowledgeNode> ret = getKnowledgeNodeListByJsonString(object.toString());
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