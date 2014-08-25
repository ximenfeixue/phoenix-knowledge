package com.ginkgocap.ywxt.knowledge.dao.favorites.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.favorites.FavoritesDao;
import com.ginkgocap.ywxt.knowledge.model.Favorites;

/**
 * @author liuyang
 *
 */
@Component("favoritesDao")
public class FavoritesDaoImpl implements FavoritesDao {

	@Override
	public Favorites save(Favorites favorites) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Favorites> findMyCollect(long userId, int start, int end,
			String title) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int findMyCollectCount(long userId, String title) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int collectCount(long moduleId, String type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteMyCollect(long userId, long moduleId, String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Favorites findOne(long userId, long moduleId, String type) {
		// TODO Auto-generated method stub
		return null;
	}


}
