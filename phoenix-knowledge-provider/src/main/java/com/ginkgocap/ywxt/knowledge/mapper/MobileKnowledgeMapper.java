package com.ginkgocap.ywxt.knowledge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MobileKnowledgeMapper {

	@SuppressWarnings("rawtypes")
	List selectKnowledgeByTagsAndKeyWords(@Param("userId") long userId,
			@Param("tag") String tag, @Param("keyword") String keyword,
			@Param("start") int start, @Param("size") int size);

	int selectCountKnowledgeByTagsAndKeyWords(@Param("userId") long userId,
			@Param("tag") String tag, @Param("keyword") String keyword);

	@SuppressWarnings("rawtypes")
	List selectKnowledgeByMyCollectionAndKeyWords(@Param("userId") long userId,
			@Param("keyword") String keyword, @Param("start") int start,
			@Param("size") int size);

	int selectCountKnowledgeByMyCollectionAndKeyWords(
			@Param("userId") long userId, @Param("keyword") String keyword);

	@SuppressWarnings("rawtypes")
	List selectKnowledgeBySourceAndColumn(@Param("columnId") long columnId,
			@Param("userId") long userId, @Param("start") int start,
			@Param("size") int size);

	int selectCountKnowledgeBySourceAndColumn(@Param("columnId") long columnId,
			@Param("userId") long userId);

	@SuppressWarnings("rawtypes")
	List selectMyFriendKnowledgeByKeyWords(@Param("friends") long[] friends,
			@Param("columnId") long columnId, @Param("start") int start,
			@Param("size") int size);

	int selectCountForMyFriendKnowledgeByKeyWords(
			@Param("friends") long[] friends, @Param("columnId") long columnId);

	@SuppressWarnings("rawtypes")
	List selectMyFriendKnowledgeByColumnId(@Param("columnId") long columnId,
			@Param("userId") long userId, @Param("start") int start,
			@Param("size") int size);
	
	int selectCountForMyFriendKnowledgeByColumnId(@Param("columnId") long columnId,
			@Param("userId") long userId);
	
	@SuppressWarnings("rawtypes")
	List selectKnowledgeForSourceByColumn(@Param("columnId") long columnId,
			@Param("userId") long userId, @Param("start") int start,
			@Param("size") int size);
	
	int selectCountKnowledgeForSourceByColumn(@Param("columnId") long columnId,
			@Param("userId") long userId);
	
	@SuppressWarnings("rawtypes")
	List selectKnowledgeByPermissionAllPlatform(@Param("userId") long userId,
			@Param("columnId") long columnId, @Param("start") int start,
			@Param("size") int size);
	
	int selectKnowledgeCountByPermissionAllPlatform(@Param("userId") long userId,
			@Param("columnId") long columnId);
	
	@SuppressWarnings("rawtypes")
	List selectKnowledgeByPermissionMyFriends(@Param("userId") long userId,
			@Param("columnId") long columnId, @Param("start") int start,
			@Param("size") int size);
	
	int selectKnowledgeCountByPermissionMyFriends(@Param("userId") long userId,
			@Param("columnId") long columnId);
	
	@SuppressWarnings("rawtypes")
	List selectKnowledgeByPermission(@Param("userId") long userId,
			@Param("columnId") long columnId, @Param("start") int start,
			@Param("size") int size);
	
	int selectKnowledgeCountByPermission(@Param("userId") long userId,
			@Param("columnId") long columnId);
	
	int selectMyKnowledgeCountByTag(@Param("userId") long userId,
			@Param("keyword") String keyword);
}