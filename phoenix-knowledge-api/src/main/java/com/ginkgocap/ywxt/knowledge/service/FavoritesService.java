package com.ginkgocap.ywxt.knowledge.service.knowledge;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Favorites;

/**收藏夹业务操作
 * @author liuyang
 *
 */
public interface FavoritesService {
	
	/**
	 * 添加收藏
	 * @param userId 当前用户id
	 * @param moduleId 模块id
	 * @param type 模块
	 * @return Favorites
	 */
	Favorites save(long userId, long moduleId, String type);
	/**
	 * 查询我收藏的东东
	 * @param userId 当前用户
	 * @param currentPage 起始页
	 * @param pageSize 页大小
	 * @return Map<String,Object>
	 */
	Map<String,Object> findMyCollect(long userId, int currentPage, int pageSize, String title);
	/**
	 * 收藏的条数
	 * @param moduleId 对应的mysql的主键
	 * @param type 模块类型
	 * @return int
	 */
	int collectCount(long moduleId, String type);
	/**
	 * 删除我的收藏
	 * @param userId 当前用户
	 * @param moduleId 对应的mysql的主键
	 * @param type 模块类型
	 */
	void deleteMyCollect(long userId, long moduleId, String type);
	/**
	 * 查询详情
	 * @param userId 当前用户
	 * @param moduleId 对应的mysql的主键
	 * @param type 模块类型
	 * @return Favorites
	 */
	Favorites findOne(long userId, long moduleId, String type);
}
