package com.ginkgocap.ywxt.knowledge.service.category.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.article.ArticleDao;
import com.ginkgocap.ywxt.knowledge.dao.category.CategoryDao;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.category.CategoryService;
/**
 * 知识管理分类的service实现类
 * @author lk
 * @创建时间：2013-03-29 10:40
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{
	private CategoryHelper helper = new CategoryHelper();
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleDao articleDao;
	@Override
	public Category selectByPrimaryKey(long id) {
		return categoryDao.selectByPrimaryKey(id);
	}
	@Override
	public String delete(long id) {
		Category cat = categoryDao.selectByPrimaryKey(id);
		//此分类下子分类的个数
		long childCount = categoryDao.selectChildCountById(id);
		//此分类下文章的个数
		long articleCount = articleDao.selectCountByCategoryId(cat.getId());
		//判断若删除此分类，1.此分类下没有子分类  2.此分类下没有发布的文章
		if (childCount <= 0){
			if (articleCount <= 0){
				categoryDao.delete(id);
			}else{
				return "articleNotNull";
			}
		}else{
			return "childNotNull";
		}
		return "";
	}
	@Override
	public List<Category> selectTreeOfSortByUserid(long uid, String state) {
		return categoryDao.selectTreeOfSortByUserid(uid, state);
	}
	@Override
	public Category insert(Category category) {
		try {
			//得到要添加的分类的父类parentId
			long parentId = category.getParentId();
			//得到要添加的分类的父类sortId
			String parentSortId = parentId > 0 ? categoryDao.selectByPrimaryKey(parentId).getSortId() : "";
			//通过parentSortId得到子类最大已添加的sortId
			String childMaxSortId = categoryDao.selectMaxSortId(category.getUid(), parentSortId);
			//如果用户第一次添加，将childMaxSortId赋值
			String newSortId = new String("");
			if (childMaxSortId == null || "null".equals(childMaxSortId) || "".equals(childMaxSortId)){
				newSortId = parentSortId + "000000001";
			}else{
				newSortId = helper.generateSortId(childMaxSortId);
			}
			//通过已添加的最大的SortId生成新的SortId
			//设置最新的sortId
			category.setSortId(newSortId);
			//返回存入的对象
			return categoryDao.insert(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void update(Category category) {
		categoryDao.update(category);
	}
	@Override
	public Category selectBySortId(long uid, String sortId) {
		return categoryDao.selectBySortId(uid ,sortId);
	}

	@Override
	public Category[] selectCategoryPathBySortId(long uid, String sortId) {
		if (sortId == null || "".equals(sortId) || "null".equals(sortId))return null;
		int len = sortId.length() / 9;
		Category[] categories = new Category[len];
		for(int i = 0 ; i < categories.length; i ++){
			String pathSortId = sortId.substring(0 * 9,9 + (i * 9));
			categories[i] = categoryDao.selectBySortId(uid, pathSortId);
		}
		return categories;
	}
	@Override
	public List<Category> selectChildBySortId(long uid, String sortId) {
		return categoryDao.selectChildBySortId(uid, sortId);
	}
}
