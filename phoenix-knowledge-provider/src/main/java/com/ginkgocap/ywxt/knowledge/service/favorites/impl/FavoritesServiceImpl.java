package com.ginkgocap.ywxt.knowledge.service.favorites.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.favorites.FavoritesDao;
import com.ginkgocap.ywxt.knowledge.model.Favorites;
import com.ginkgocap.ywxt.knowledge.service.favorites.FavoritesService;
import com.ginkgocap.ywxt.user.model.Diary;
import com.ginkgocap.ywxt.user.service.DiaryService;
import com.ginkgocap.ywxt.util.FavoritesType;
import com.ginkgocap.ywxt.util.PageUtil;
@Service("favoritesService")
public class FavoritesServiceImpl implements FavoritesService {

	@Autowired
	private FavoritesDao favoritesDao;
	@Autowired
	private DiaryService diaryService;
	
	private Logger logger = LoggerFactory.getLogger(FavoritesServiceImpl.class);
	
	@Override
	public Favorites save(long userId, long moduleId, String type) {
		Favorites favorites = new Favorites(); 
		favorites.setUserId(userId);
		favorites.setModuleId(moduleId);
		favorites.setType(type);
		favorites.setTarget(this.setTargetInfo(moduleId, type));//保存收藏对象
		favorites = this.setTitleAndContent(moduleId, type, favorites);//保存收藏标题和内容
		favorites = favoritesDao.save(favorites);
		if(favorites==null){
			logger.debug("[ FavoritesServiceImpl ] save Favorites is fail");
		}
		return favorites;
	}

	@Override
	public Map<String, Object> findMyCollect(long userId, int currentPage,
			int pageSize, String title) {
		Map<String,Object> map = new HashMap<String,Object>();
		int count = favoritesDao.findMyCollectCount(userId, title);
		PageUtil page = new PageUtil(count, currentPage, pageSize);
		List<Favorites> list = favoritesDao.findMyCollect(userId, page.getPageStartRow()-1, pageSize, title);
		map.put("list", list);
		map.put("page", page);
		return map;
	}
	/**
	 * 赋值收藏的对象
	 * @param id 主键
	 * @param type 模块类型
	 * @return Object
	 */
	private Object setTargetInfo(long id, String type){
		Object obj = new Object();
		if(id!=0){
			//如果是观点那么就获取观点的对象
			if(type.equals(FavoritesType.FAVORITER_DIARY.toString())){
				obj = this.getDiaryInfo(id);
			}
		}else{
			logger.debug("[ FavoritesServiceImpl ] set Target Info is fail, because get id is 0");
		}
		return obj;
	}
	/**
	 * 获取观点
	 * @param id 主键
	 * @return Diary
	 */
	private Diary getDiaryInfo(long id){
		Diary diary = diaryService.selectDiaryById(id);
		if(diary == null){
			logger.debug("[ FavoritesServiceImpl ] get diary is fail, because diary doesn't exist");
		}
		return diary;
	}
	/**
	 * 设置收藏的标题和内容
	 * @param id 对象Id
	 * @param type 类型
	 * @param favorites 对象
	 * @return Favorites
	 */
	private Favorites setTitleAndContent(long id,String type, Favorites favorites){
		Object obj = this.setTargetInfo(id, type);
		String title = "";
		String content = "";
		if(obj != null){
			//如果是观点那么就获取观点的对象
			if(type.equals(FavoritesType.FAVORITER_DIARY.toString())){
				Diary diary = (Diary) obj;
				title = diary.getTitleName();
				content = diary.getContent();
			}
			favorites.setTitle(title);
			favorites.setContent(content);
		}else{
			logger.debug("[ FavoritesServiceImpl ] set Title and Content is fail, because get Object is null");
		}
		return favorites;
	}

	@Override
	public int collectCount(long moduleId, String type) {
		return favoritesDao.collectCount(moduleId, type);
	}

	@Override
	public void deleteMyCollect(long userId, long moduleId, String type) {
		favoritesDao.deleteMyCollect(userId, moduleId, type);
	}

	@Override
	public Favorites findOne(long userId, long moduleId, String type) {
		return favoritesDao.findOne(userId, moduleId, type);
	}
}
