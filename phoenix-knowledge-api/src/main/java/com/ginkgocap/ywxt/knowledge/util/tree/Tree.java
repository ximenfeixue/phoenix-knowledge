package com.ginkgocap.ywxt.knowledge.util.tree;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.ginkgocap.ywxt.knowledge.model.Category;

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
    public static Node build(List<Node> categories){
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
        }catch(Exception e){        }
        
        return;
    }
    
    //测试生成树方法
    public static void main(String[] args) {
        Category c1 = new Category();
        Category c11 = new Category();
        Category c111 = new Category();
        Category c2 = new Category();
        Category c12 = new Category();
        Category c21 = new Category();
        Category c22 = new Category();
        c1.setSortId("100");
        c1.setId(1);
        c1.setParentId(0);
        c1.setUid(4444);
        c11.setId(2);
        c11.setSortId("100100");
        c11.setUid(44445);
        c11.setParentId(1);
        c12.setId(201);
        c12.setUid(444456);
        c12.setSortId("100200");
        c12.setParentId(1);
        c2.setId(30);
        c2.setUid(444);
        c2.setSortId("200");
        c2.setParentId(0);
        c21.setId(4);
        c21.setSortId("200100");
        c21.setUid(4447);
        c21.setParentId(30);
        c22.setId(300);
        c22.setSortId("200200");
        c22.setUid(44478);
        c22.setParentId(30);
        c111.setId(3);
        c111.setSortId("100100100");
        c111.setUid(444789);
        c111.setParentId(2);
        List<Category> tempList = new ArrayList<Category>();
        tempList.add(c1);
        tempList.add(c11);
        tempList.add(c111);
        tempList.add(c12);
        tempList.add(c2);
        tempList.add(c21);
        tempList.add(c22);
        List<Node> nl =ConvertUtil.convert2Node(tempList,"uid", "id", "name", "parentId", "sortId");
        String a=JSONObject.fromObject(Tree.build(nl)).toString();
        System.out.println(a);
        p(rootNode);//打印树
    }

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
