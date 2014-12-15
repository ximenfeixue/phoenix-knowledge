package com.ginkgocap.ywxt.knowledge.util.tree;

import java.util.ArrayList; 
import java.util.List;

import net.sf.json.JSONObject;
 

/**
 * 生成知识目录树
 * <p>于2014-9-1 由 边智伟 创建 </p>
 * @author  <p>当前负责人 边智伟</p>     
 *
 */
public class Tree {
    //根节点
    public static Node rootNode;

    //当前节点
    private static Node curNode;
    
    //生成根和枝叶
    public synchronized static Node build(List<Node> categories){
        rootNode = new Node();
        curNode =  new Node();
        rootNode.setId(0);
        rootNode.setParentId(-1);
        rootNode.setList(null);
        buildTree(0, categories, null, 0, rootNode);
        return rootNode;
    }
    
  //获取父节点
    private static Node getPnode(Node node) {

        Node rec = new Node();
        if (node.getId() == curNode.getParentId()) {//如果是当前的父节点，返回父节点
            return node;
        } else {
            if (node != null && node.getList() != null) {
                for (Node c : node.getList()) {
                    rec = getPnode(c);                    
                }
            }
        }
        return rec;
    }

    /**
     * buildTree生成树方法
     * @param pid 父id
     * @param tList 节点列表
     * @param oList 旧节点列表
     * @param size sort长度
     * @param oc 旧节点
     */
    public static void buildTree(long pid, List<Node> tList, List<Node> oList, int size, Node oc) {

        if (tList.size() == 0) {//递归完毕
            return;
        }
        
        try{
        Node tc = tList.get(0);
        if (size < tc.getSortId().length()) {//子节点
            List<Node> nList = new ArrayList<Node>();

            if (pid == tc.getParentId()) {
                nList.add(tc);
                tList.remove(0);
                oc.setList(nList);
                buildTree(tc.getId(), tList, nList, tc.getSortId().length(), tc);
            } else {//同胞节点
                oList.add(tc);
                tList.remove(0);
                buildTree(tc.getId(), tList, oList, tc.getSortId().length(), tc);
            }
        } else {//父节点
            curNode.setId(tc.getId());
            curNode.setParentId(tc.getParentId());
            curNode.setSortId(tc.getSortId());
            Node pc = getPnode(rootNode);
            List<Node> lc = pc.getList();
            lc.add(tc);
            tList.remove(0);
            buildTree(tc.getId(), tList, lc, tc.getSortId().length(), tc);
        }
        }catch(Exception e){       
            System.err.println("error节点："+pid+"出错");
        }
        
        return;
    }
    
    //测试生成树方法
    public static void main(String[] args) { }

    private static void p(Node node) {
        if(node!=null){            
            System.out.println(node.getSortId());
            if(node.getList()!=null){                
                for (Node c : node.getList()) {
                    p(c);                    
                }
            }
        }
    }
}
