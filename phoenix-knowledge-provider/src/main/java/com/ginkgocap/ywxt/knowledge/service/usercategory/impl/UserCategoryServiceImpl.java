package com.ginkgocap.ywxt.knowledge.service.usercategory.impl;

import java.util.List; 
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.model.UserCategory;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.service.knowledgecategory.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

/**
 * 知识目录左树实现 
 * <p>于2014-9-16 由 bianzhiwei 创建 </p>
 * @author  <p>当前负责人 bianzhiwei</p>   
 *
 */
@Service("userCategoryService")
public class UserCategoryServiceImpl implements UserCategoryService {
    private CategoryHelper helper = new CategoryHelper();
    @Autowired
    private UserCategoryDao userCategoryDao;
    @Autowired
    private KnowledgeCategoryService knowledgeCategoryService;

    @Override
    public UserCategory selectByPrimaryKey(long id) {
        return userCategoryDao.selectByPrimaryKey(id);
    }

    @Override
    public String delete(long id) {
        //此分类下子分类的个数
        long childCount = userCategoryDao.selectChildCountById(id);
        //此分类下文章的个数
        long articleCount =knowledgeCategoryService.countByKnowledgeCategoryId(id);
        //判断若删除此分类，1.此分类下没有子分类  2.此分类下没有发布的文章
        if (childCount <= 0){
            if (articleCount <= 0){
                userCategoryDao.delete(id);
            }else{
                return "articleNotNull";
            }
        }else{
            return "childNotNull";
        }
        return "";
    }

    @Override
    public UserCategory insert(UserCategory category) {
        //得到要添加的分类的父类parentId
        try {
            long parentId = category.getParentId();
            //得到要添加的分类的父类sortId
            String parentSortId = parentId > 0 ? userCategoryDao.selectByPrimaryKey(parentId).getSortId() : "";
            //通过parentSortId得到子类最大已添加的sortId
            String childMaxSortId = userCategoryDao.selectMaxSortId(category.getUserId(), parentSortId);
            if (StringUtils.isBlank(category.getSortId())) {
                //如果用户第一次添加，将childMaxSortId赋值
                String newSortId = new String("");
                if (childMaxSortId == null || "null".equals(childMaxSortId) || "".equals(childMaxSortId)) {
                    newSortId = parentSortId + "000000001";
                } else {
                    newSortId = helper.generateSortId(childMaxSortId);
                }
                //通过已添加的最大的SortId生成新的SortId
                //设置最新的sortId
                category.setSortId(newSortId);
            }
            //返回存入的对象
            return userCategoryDao.insert(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(UserCategory category) {
        userCategoryDao.update(category);
    }
 
    @Override
    public List<UserCategory> selectChildBySortId(long uid, String sortId) {
        return userCategoryDao.selectChildBySortId(uid, sortId);
    }

    @Override
    public String selectUserCategoryTreeBySortId(long userId, String sortId) {
        List<UserCategory> cl = userCategoryDao.selectChildBySortId(userId, sortId);
        if (cl != null && cl.size() > 0) {
            return JSONObject.fromObject(Tree.build(ConvertUtil.convert2Node(cl, "id", "categoryName", "parentId", "sortId")))
                    .toString();
        }
        return "";
    }

    @Override
    public long selectChildCountById(long id) {
        return userCategoryDao.selectChildCountById(id);
    }

}
