package com.ginkgocap.ywxt.knowledge.dao.favorites;

import java.util.List;

import com.ginkgocap.ywxt.knowledge.model.Favorites;

/**收藏夹dao
 * @author liuyang
 *
 */
public interface FavoritesDao {
	
	/**
	 * 添加收藏
	 * @param favorites
	 * @return Favorites
	 */
	Favorites save(Favorites favorites);
	/**
	 * 查询我收藏的东东
	 * @param userId 当前用户
	 * @param start 偏移量
	 * @param end 量大小
	 * @return List<Favorites>
	 */
	List<Favorites> findMyCollect(long userId, int start, int end, String title);
	/**
	 *  查询我收藏的东东 行数
	 * @param userId 当前用户
	 * @return int
	 */
	int findMyCollectCount(long userId, String title);
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
