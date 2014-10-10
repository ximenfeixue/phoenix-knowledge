package com.ginkgocap.ywxt.knowledge.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * node转换工具类
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 *
 */
public class ConvertUtil {
    public static <T> List<Node> convert2Node(List<T> l,String userid, String id, String name, String parentId, String sortId) {
        List<Node> nl = new ArrayList<Node>();
        for (T c : l) {
            Node n = new Node();
            try {
                n.setUserId((Long) c.getClass()
                        .getMethod("get" + userid.substring(0, 1).toUpperCase() + userid.substring(1, userid.length())).invoke(c));
                n.setId((Long) c.getClass()
                        .getMethod("get" + id.substring(0, 1).toUpperCase() + id.substring(1, id.length())).invoke(c));
                n.setName((String) c.getClass()
                        .getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length())).invoke(c));
                n.setParentId((Long) c.getClass()
                        .getMethod("get" + parentId.substring(0, 1).toUpperCase() + parentId.substring(1, parentId.length()))
                        .invoke(c));
                n.setSortId((String) c.getClass()
                        .getMethod("get" + sortId.substring(0, 1).toUpperCase() + sortId.substring(1, sortId.length()))
                        .invoke(c));
            } catch (Exception e) {
                e.printStackTrace();
            }
            nl.add(n);
        }
        return nl;

    }
}
