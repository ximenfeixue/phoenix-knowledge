package com.ginkgocap.ywxt.knowledge.dao.favorites.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.ginkgocap.ywxt.knowledge.dao.favorites.FavoritesDao;
import com.ginkgocap.ywxt.knowledge.model.Favorites;
import com.ginkgocap.ywxt.util.DateFunc;
import com.ginkgocap.ywxt.util.MakePrimaryKey;

/**
 * @author liuyang
 *
 */
@Component("favoritesDao")
public class FavoritesDaoImpl implements FavoritesDao {

	
	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public Favorites save(Favorites favorites) {
		favorites.setCtime(DateFunc.getDate());
		favorites.setId(MakePrimaryKey.getPrimaryKey());
		mongoTemplate.save(favorites);
		return favorites;
	}

	@Override
	public List<Favorites> findMyCollect(long userId, int start, int end, String title) {
		Criteria criteria = Criteria.where("userId").is(userId);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		query.sort().on("ctime", Order.DESCENDING);
		query.skip(start);
		query.limit(end);
		return mongoTemplate.find(query, Favorites.class);
	}

	@Override
	public int findMyCollectCount(long userId, String title) {
		Criteria criteria = Criteria.where("userId").is(userId);
		if(StringUtils.isNotBlank(title)){
			criteria.and("title").regex(".*?"+title+".*");
		}
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query, Favorites.class);
	}

	@Override
	public int collectCount(long moduleId, String type) {
		Criteria criteria = Criteria.where("moduleId").is(moduleId).and("type").is(type);
		Query query = new Query(criteria);
		return (int) mongoTemplate.count(query, Favorites.class);
	}

	@Override
	public void deleteMyCollect(long userId, long moduleId, String type) {
		Criteria criteria = Criteria.where("userId").is(userId).and("moduleId").is(moduleId).and("type").is(type);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, Favorites.class);
	}

	@Override
	public Favorites findOne(long userId, long moduleId, String type) {
		Criteria criteria = Criteria.where("userId").is(userId).and("moduleId").is(moduleId).and("type").is(type);
		Query query = new Query(criteria);
		return mongoTemplate.findOne(query, Favorites.class);
	}

}
