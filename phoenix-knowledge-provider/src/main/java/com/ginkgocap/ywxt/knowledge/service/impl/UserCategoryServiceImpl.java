package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.usercategory.UserCategoryDao;
import com.ginkgocap.ywxt.knowledge.entity.KnowledgeCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategory;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample;
import com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryMapper;
import com.ginkgocap.ywxt.knowledge.mapper.UserCategoryValueMapper;
import com.ginkgocap.ywxt.knowledge.model.Category;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeCategoryService;
import com.ginkgocap.ywxt.knowledge.service.UserCategoryService;
import com.ginkgocap.ywxt.knowledge.util.Constants;
import com.ginkgocap.ywxt.knowledge.util.tree.ConvertUtil;
import com.ginkgocap.ywxt.knowledge.util.tree.Tree;

/**
 * 知识目录左树实现
 * <p>
 * 于2014-9-16 由 bianzhiwei 创建
 * </p>
 * 
 * @author <p>
 *         当前负责人 bianzhiwei
 *         </p>
 * 
 */
@Service("userCategoryService")
public class UserCategoryServiceImpl implements UserCategoryService {
	private CategoryHelper helper = new CategoryHelper();
	// @Autowired
	// private UserCategoryDao userCategoryDao;
	// @Autowired
	// private KnowledgeCategoryService knowledgeCategoryService;
	@Autowired
	private UserCategoryValueMapper userCategoryValueMapper;
	@Autowired
	private UserCategoryMapper userCategoryMapper;
	@Autowired
	private KnowledgeCategoryService knowledgeCategoryService;

	@Override
	public UserCategory selectByPrimaryKey(long id) {
		return userCategoryMapper.selectByPrimaryKey(id);
	}

	@Override
	public String delete(long id) {

		return deleteNew(id);
		// 此分类下子分类的个数
		// long childCount = userCategoryValueMapper.selectChildCountById(id);
		// //此分类下文章的个数
		// long articleCount
		// =knowledgeCategoryService.countByKnowledgeCategoryId(id);
		// //判断若删除此分类，1.此分类下没有子分类 2.此分类下没有发布的文章
		// if (childCount <= 0){
		// if (articleCount <= 0){
		// userCategoryMapper.deleteByPrimaryKey(id);
		// }else{
		// return "articleNotNull";
		// }
		// }else{
		// return "childNotNull";
		// }
		// return "success";
	}

	public String deleteNew(long id) {
        UserCategory u = userCategoryMapper.selectByPrimaryKey(id);
        UserCategory un = null;
        if (u.getCategoryType() == 0) {// 删除目录知识
            un = this.selectUserCategoryByParams(u.getUserId(), 0l, 0l, "未分组").get(0);
            userCategoryValueMapper.delk(u.getUserId(), u.getCategoryType(), un.getId(), u.getSortid());
        } else if (u.getCategoryType() == 1) {// 删除收藏知识
            un = this.selectUserCategoryByParams(u.getUserId(), 0l, 1l, "未分组").get(0);
            userCategoryValueMapper.delc(u.getUserId(), u.getCategoryType(), un.getId(), u.getSortid());
        }
		userCategoryValueMapper.del(u.getUserId(), u.getCategoryType(),
		        u.getSortid());
		return "success";
	}

	@Override
	public String insert(UserCategory category) {
		// 得到要添加的分类的父类parentId
		try {
			long userid = category.getUserId();
			long parentId = category.getParentId();
			String cname = category.getCategoryname();
			Short ct = category.getCategoryType();
			List<UserCategory> lu = this.selectUserCategoryByParams(userid,
					parentId, ct, cname);
			if (lu != null && lu.size() > 0) {
				return "false";
			}
			// 得到要添加的分类的父类sortId
			String parentSortId = parentId > 0 ? userCategoryMapper
					.selectByPrimaryKey(parentId).getSortid() : "";
			// 通过parentSortId得到子类最大已添加的sortId
			String childMaxSortId = userCategoryValueMapper.selectMaxSortId(
					category.getUserId(), parentSortId,
					category.getCategoryType());
			if (StringUtils.isBlank(category.getSortid())) {
				// 如果用户第一次添加，将childMaxSortId赋值
				String newSortId = new String("");
				if (childMaxSortId == null || "null".equals(childMaxSortId)
						|| "".equals(childMaxSortId)) {
					newSortId = parentSortId + "000000001";
				} else {
					newSortId = helper.generateSortId(childMaxSortId);
				}
				// 通过已添加的最大的SortId生成新的SortId
				// 设置最新的sortId
				category.setSortid(newSortId);
			}
			// 返回存入的对象
			userCategoryMapper.insert(category);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	@Override
	public void update(UserCategory category) {
		userCategoryMapper.updateByPrimaryKey(category);
	}

	@Override
	public List<UserCategory> selectChildBySortId(long uid, String sortId,
			Byte type) {
		return userCategoryValueMapper.selectChildBySortId(uid, sortId, type);
	}

	@Override
	public String selectUserCategoryTreeBySortId(long userId, String sortId,
			Byte type) {
		List<UserCategory> cl = userCategoryValueMapper.selectChildBySortId(
				userId, sortId, type);
		if (cl != null && cl.size() > 0) {
			return JSONArray.fromObject(
					Tree.build(ConvertUtil.convert2Node(cl, "userId", "id",
							"categoryname", "parentId", "sortid"))).toString();
		}
		return "";
	}

	@Override
	public String selectUserCategoryTreeByParams(long userId, String sortId,
			Byte type, String columnType) {
		List<UserCategory> cl = userCategoryValueMapper.selectChildBySortId(
				userId, sortId, type);
		if (cl != null && cl.size() > 0) {
			JSONObject jo = JSONObject.fromObject(Tree.build(ConvertUtil
					.convert2Node(cl, "userId", "id", "categoryname",
							"parentId", "sortid")));
			JSONArray ja = jo.getJSONArray("list");
			for (int i = 0; i < ja.size(); i++) {
				JSONObject joi = ja.getJSONObject(i);
				if (columnType.equals(joi.getString("id"))) {
					return joi.getJSONArray("list").toString();
				}
			}
		}
		return "";
	}

	@Override
	public long selectChildCountById(long id) {
		return userCategoryValueMapper.selectChildCountById(id);
	}

	@Override
	public UserCategory selectByNameAndPid(String name, long pid) {
		UserCategoryExample example = new UserCategoryExample();
		example.createCriteria().andParentIdEqualTo(pid)
				.andCategorynameEqualTo(name);
		List<UserCategory> l = userCategoryMapper.selectByExample(example);
		return l.get(0);
	}

	@Override
	public void checkNogroup(Long uid, List<Long> idtypes) {
		for (long type : idtypes) {
			List<UserCategory> l = this.selectUserCategoryByParams(uid, 0l,
					type, "未分组");
			// 初始化未分组
			if (l.size() == 0) {
				UserCategory uc = new UserCategory();
				uc.setCategoryname("未分组");
				uc.setSortid("111111111");
				uc.setParentId(0l);
				uc.setCategoryType((short) type);
				uc.setUserId(uid);
				this.insert(uc);
			}
		}
	}

	@Override
	public List<UserCategory> selectUserCategoryByParams(Long uid, long pid,
			long type, String categoryname) {
		UserCategoryExample example = new UserCategoryExample();
		Criteria c = example.createCriteria();
		if (uid > 0) {
			c.andUserIdEqualTo(uid);
		}
		if (type >= 0) {
			c.andCategoryTypeEqualTo((short) type);
		}
		if (categoryname != null && !"".equals(categoryname)) {
			c.andCategorynameEqualTo(categoryname);
		}
		c.andParentIdEqualTo(pid);
		List<UserCategory> ll = userCategoryMapper.selectByExample(example);
		return ll;
	}

	@Override
	public String selecteidtUserCategoryTreeBySortId(long knowledgeid) {

		// 知识所在的目录
		List<KnowledgeCategory> categorylist = knowledgeCategoryService
				.selectKnowledgeCategory(knowledgeid);
		List<UserCategory> cl = new ArrayList<UserCategory>();
		if (categorylist != null && categorylist.size() > 0) {
			for (KnowledgeCategory knowledgeCategory : categorylist) {
				UserCategory usercategory = selectByPrimaryKey(knowledgeCategory
						.getCategoryId());
				cl.add(usercategory);
			}
			JSONArray jsonArray = JSONArray.fromObject(cl);
			return jsonArray.toString();
		}
		return "";
	}

	@Override
	public List<UserCategory> selectNoGroup(long userId, String sortId,
			Byte type) {
		UserCategoryExample example = new UserCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andSortidEqualTo(sortId);
		criteria.andCategoryTypeEqualTo((short) type);
		return userCategoryMapper.selectByExample(example);
	}

	@Override
	public String getDefaultCategoryId(long userId) {
		long[] cIds = null;
		UserCategoryExample example = new UserCategoryExample();
		com.ginkgocap.ywxt.knowledge.entity.UserCategoryExample.Criteria criteria = example
				.createCriteria();
		criteria.andSortidEqualTo(Constants.unGroupSortId);
		criteria.andUserIdEqualTo(userId);
		criteria.andCategoryTypeEqualTo((short) Constants.CategoryType.common
				.v());
		List<UserCategory> list = userCategoryMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			cIds = new long[1];
			cIds[0] = list.get(0).getId();
		}
		if (null != cIds && cIds.length > 0) {
			return cIds[0] + "";
		} else {
			return null;
		}
	}

    @Override
    public List<UserCategory> selectUserCategoryByParam(Long uid, Long pid, int type, String sortid,
            String categoryname) {
        UserCategoryExample example = new UserCategoryExample();
        Criteria c = example.createCriteria();
        if (uid != null) {
            c.andUserIdEqualTo(uid);
        }
        if (type >= 0) {
            c.andCategoryTypeEqualTo((short) type);
        }
        if (!"".equals(sortid) || null != sortid) {
            c.andSortidEqualTo(sortid);
        }
        if (pid != null) {
            c.andParentIdEqualTo(pid);
        }
        if (categoryname != null && !"".equals(categoryname)) {
            c.andCategorynameEqualTo(categoryname);
        }
        List<UserCategory> ll = userCategoryMapper.selectByExample(example);
        return ll;
    }

}
