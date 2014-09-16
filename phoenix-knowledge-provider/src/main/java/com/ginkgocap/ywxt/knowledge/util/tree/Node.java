package com.ginkgocap.ywxt.knowledge.util.tree;

import java.util.List;
/**
 * node节点 
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
public class Node {
    private long id;
    private long parentId;
    private String sortId;
    private String name;
    private List<Node> list;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getParentId() {
        return parentId;
    }
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
    public String getSortId() {
        return sortId;
    }
    public void setSortId(String sortId) {
        this.sortId = sortId;
    }
    public List<Node> getList() {
        return list;
    }
    public void setList(List<Node> list) {
        this.list = list;
    }
    
    
}
