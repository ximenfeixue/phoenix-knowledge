package com.ginkgocap.ywxt.knowledge.model;

import java.io.Serializable;
import java.util.List;

/**   
 * 用户目录左树
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>     
 *
 */
public class UserCategory implements Serializable {

    private static final long serialVersionUID = 9210031377998416429L;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
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

    public List<UserCategory> getList() {
        return list;
    }

    public void setList(List<UserCategory> list) {
        this.list = list;
    }

    /** 主键 */
    private long id;
    /** phoenix_user.tb_user.id */
    private long userId;
    /** 分类名称 */
    private String categoryName;
    /** 父类ID */
    private long parentId;
    /** 排序ID，九位一级 如000000001000000001,为一级分类下的第一个分类 */
    private String sortId;
    /** 分类创建时间 */
    private String createTime;
    /** 最后修改时间 */
    private String modifyTime;
    /** 子分类列表 */
    private List<UserCategory> list;
}
