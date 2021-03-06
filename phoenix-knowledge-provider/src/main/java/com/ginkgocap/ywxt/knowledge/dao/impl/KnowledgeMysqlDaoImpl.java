package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import com.ginkgocap.ywxt.knowledge.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository("knowledgeMysqlDao")
public class KnowledgeMysqlDaoImpl extends BaseService<KnowledgeBase> implements KnowledgeMysqlDao {
	private final Logger logger = LoggerFactory.getLogger(KnowledgeMysqlDaoImpl.class);
	@Override
	public KnowledgeBase insert(KnowledgeBase knowledgeBase) throws Exception
    {
		if(knowledgeBase == null) {
			logger.error("knowledge base is null, skip to save.");
			return null;
        }

        if (this.saveEntity(knowledgeBase) != null) {
            return knowledgeBase;
        }
        return null;
	}
	
	@Override
	public List<KnowledgeBase> insertList(List<KnowledgeBase> baseList) throws Exception
    {
		if(CollectionUtils.isEmpty(baseList)) {
			logger.error("knowledge base list is null, skip to save.");
			return null;
		}
		
		return this.saveEntitys(baseList);
	}

	@Override
	public boolean update(KnowledgeBase knowledgeBase) throws Exception {
		
		if(knowledgeBase == null) {
			return false;
		}
		KnowledgeBase oldValue = this.getByKnowledgeId(knowledgeBase.getKnowledgeId());
		if (oldValue == null) {
			logger.error("update an not exist knowledge, knowledgeId: " + knowledgeBase.getKnowledgeId());
			return false;
		}
		knowledgeBase.setId(oldValue.getId());
		knowledgeBase.setModifyDate(System.currentTimeMillis());
		if (knowledgeBase.getModifyUserId() <= 0) {
			knowledgeBase.setModifyUserId(knowledgeBase.getCreateUserId());
		}
		if (StringUtil.inValidString(knowledgeBase.getModifyUserName())) {
			knowledgeBase.setModifyUserName(knowledgeBase.getCreateUserName());
		}
		return this.updateEntity(knowledgeBase);
		///return knowledgeBase;
	}

	@Override
	public boolean updateOrigin(KnowledgeBase knowledgeBase) throws Exception {

		if(knowledgeBase == null) {
			return false;
		}
		KnowledgeBase oldValue = this.getByKnowledgeId(knowledgeBase.getKnowledgeId());
		if (oldValue == null) {
			logger.error("update an not exist knowledge, knowledgeId: " + knowledgeBase.getKnowledgeId());
			return false;
		}
		knowledgeBase.setId(oldValue.getId());
		return this.updateEntity(knowledgeBase);
	}

	@Override
	public int deleteByKnowledgeId(long knowledgeId) throws Exception
	{
        return this.deleteList("delete_knowledge_by_knowledgeId", knowledgeId);
	}

	@Override
	public int batchDeleteByIds(List<Long> knowledgeIds) throws Exception
	{
		if (CollectionUtils.isNotEmpty(knowledgeIds)) {
			return this.deleteEntityByIds(knowledgeIds) ? knowledgeIds.size() : 0;
		}
		return 0;
	}

	@Override
	public boolean logicDeleteByIds(List<Long> knowledgeIds)
	{
		return this.updateKnowledgeStatus(knowledgeIds, 5);
	}

	@Override
	public boolean logicRecoveryByIds(List<Long> knowledgeIds) throws Exception
	{
		return this.updateKnowledgeStatus(knowledgeIds, 4);
	}

	@Override
	public int deleteByCreateUserId(long createUserId) throws Exception
	{
		int deleteStatus = this.deleteList("delete_by_createUserId", createUserId);
		return deleteStatus;
	}

	@Override
	public KnowledgeBase getByKnowledgeId(long knowledgeId) throws Exception
    {
		return this.getEntity(knowledgeId);
	}

    @Override
    public List<KnowledgeBase> getByKnowledgeIdKeyWord(List<Long> knowledgeIds,String keyword) throws Exception
    {
        List<KnowledgeBase> knowledgeBaseList = new ArrayList<KnowledgeBase>(knowledgeIds.size());
        for (Long knowledgeId : knowledgeIds) {
            List<KnowledgeBase> knowledgeBaseItems = this.getEntitys("get_knowledge_by_Id_keyword", knowledgeId, "%"+keyword+"%");
            KnowledgeBase base = (knowledgeBaseItems != null && knowledgeBaseItems.size() > 0) ? knowledgeBaseItems.get(0) : null;
            if (base != null) {
                knowledgeBaseList.add(base);
            }
        }

        return knowledgeBaseList;
    }

    @Override
	public List<KnowledgeBase> getByKnowledgeIds(List<Long> knowledgeIds) throws Exception
    {
		return this.getEntityByIds(knowledgeIds);
	}

	@Override
	public List<KnowledgeBase> getAllBase(int start,int size) throws Exception {
		
		return this.getSubEntitys("get_all_start_size", start, size, 0);
	}

	@Override
	public long getAllPublicCount(short permission) throws Exception
	{
		return this.countEntitys("get_all_public", permission);
	}

	@Override
	public List<KnowledgeBase> getAllPublic(int start,int size,short permission)	throws Exception {

		return this.getSubEntitys("get_all_public", start, size, permission);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserId(long userId,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_createUserId", start, size, userId);
	}

	@Override
	public int countByCreateUserName(String userName) throws Exception {

		return this.countEntitys("get_by_createUserName", userName);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserName(String userName, int start, int size) throws Exception {

		return this.getSubEntitys("get_by_createUserName", start, size, userName);
	}

	@Override
	public int countByTitle(String title) throws Exception {

		return this.countEntitys("get_by_title", "%" + title + "%");
	}

	@Override
	public List<KnowledgeBase> getByTitle(String title, int start, int size) throws Exception {

		return this.getSubEntitys("get_by_title", start, size, "%" + title + "%");
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndType(long userId, short type, int start, int size) throws Exception
	{
		return this.getSubEntitys("get_by_createUserId_type", start, size, userId, type);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnId(long userId, int columnId, int start, int size) throws Exception
	{
		return this.getSubEntitys("get_by_createUserId_columnId", start, size, userId, columnId);
	}

	@Override
	public List<KnowledgeBase> getByColumnId(int columnId,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_by_columnId", start, size, columnId);
	}

	@Override
	public long getPublicCountByColumnId(int columnId,short permission) throws Exception
	{
		return this.countEntitys("get_public_by_columnId", columnId, permission);
	}

	@Override
	public List<KnowledgeBase> getPublicByColumnId(int columnId,short permission,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_public_by_columnId", start, size, columnId, permission);
	}

	@Override
	public int countByUserIdKeyWord(long userId,String keyWord) throws Exception
	{
		return this.countEntitys("get_by_createUserId_keyWord", userId, "%" + keyWord + "%");
	}

    @Override
    public List<KnowledgeBase> getByUserIdKeyWord(long userId,String keyWord,int start,int size) throws Exception
	{
        return this.getSubEntitys("get_by_createUserId_keyWord", start, size, userId, "%"+keyWord+"%");
    }

    @Override
    public List<KnowledgeBase> getByColumnIdAndKeyWord(String keyWord,int columnId,int start,int size) throws Exception
	{
        return this.getSubEntitys("get_by_columnId_keyWord", start, size, columnId, "%"+keyWord+"%");
    }

    @Override
	public List<KnowledgeBase> getByTypeAndColumnId(short type, int columnId,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_by_type_columnId", start, size, type, columnId);
	}
	
	@Override
	public List<KnowledgeBase> getByType(short type,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_by_type", start, size, type);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndTypeAndColumnId(
			long createUserId, short type, int columnId,int start,int size) throws Exception {

		return this.getSubEntitys("get_by_createUserId_type_columnId", start, size, createUserId, type, columnId);
	}

	@Override
	public List<KnowledgeBase> getByBetweenCreateDate(Date beginDate, Date endDate,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_by_beginDate_endDate", start, size, getDate(beginDate,true), getDate(endDate,false));
	}

	@Override
	public List<KnowledgeBase> getByTypeAndBetweenCreateDate(short type,Date beginDate, Date endDate,int start,int size) throws Exception
	{
		return this.getSubEntitys("get_by_type_beginDate_endDate", start, size, type,getDate(beginDate,true), getDate(endDate,false));
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndBetweenCreateDate(
			long createUserId, Date beginDate, Date endDate,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_createUserId_beginDate_endDate", start, size, createUserId,getDate(beginDate,true),getDate(endDate,false));
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnIdAndBetweenCreateDate(
			long createUserId, int columnId, Date beginDate, Date endDate,int start,int size)
			throws Exception {
		
		return this.getSubEntitys("get_by_createUserId_columnId_beginDate_endDate", start, size, new Object[]{createUserId,columnId,getDate(beginDate,true),getDate(endDate,false)});
	}

	@Override
	public List<KnowledgeBase> getByStatus(short status,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_status", start, size, status);
	}

	@Override
	public List<KnowledgeBase> getByAuditStatus(short auditStatus,int start,int size)
			throws Exception {
		
		return this.getSubEntitys("get_by_auditStatus", start, size, auditStatus);
	}

	@Override
	public List<KnowledgeBase> getByReportStatus(short reportStatus,int start,int size)
			throws Exception {
		
		return this.getSubEntitys("get_by_reportStatus", start, size, reportStatus);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndStatus(long createUserId,
			short status,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_createUserId_status", start, size, createUserId, status);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndAuditStatus(
			long createUserId, short auditStatus,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_createUserId_auditStatus", start, size, createUserId, auditStatus);
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndReportStatus(
			long createUserId, short reportStatus,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_createUserId_reportStatus", start, size, createUserId, reportStatus);
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndStatus(int columnId,
			short status,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_columnId_status", start, size, columnId, status);
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndAuditStatus(int columnId,
			short auditStatus,int start,int size) throws Exception {
		
		return this.getSubEntitys("get_by_columnId_auditStatus", start, size, columnId, auditStatus);
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndReportStatus(int columnId,
			short reportStatus,final int start,final int size) throws Exception {
		
		return this.getSubEntitys("get_by_columnId_reportStatus", start, size, columnId, reportStatus);
	}

	@Deprecated
	@Override
	public List<KnowledgeBase> getKnowledgeNoDirectory(long userId,int start,int size) throws Exception
	{
		return this.getSubEntitys("", start, size, userId);
	}

	@Override
	public List getCreatedKnowledgeCountGroupByDay(long userId, long startDate, long endDate) {
		final String querySQL = "SELECT DATE_FORMAT(from_unixtime(create_date/1000),'%Y%m%d') day, count(id) FROM tb_knowledge_base " +
				"WHERE " + startDate + " <= create_date AND create_date <= " + endDate + " AND create_user_id = " + userId +" group by day;";

		try {
			return this.dao.getListBySQL(querySQL);
		} catch (Exception ex) {
			logger.error("query created kowledge failed. userId: " + userId + " startDate: " + startDate + " endDate: " + endDate + "error: " + ex.getMessage());
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public int getKnowledgeCount(long userId) throws Exception
	{
		return this.countEntitys("get_count_by_user", userId);
	}

	@Override
	public List<Long> getKnowledgeIdsByType(short type, int size) throws Exception
	{
		return this.getIds("get_id_list_by_type", 0, size, new Object[]{type});
	}

	@Override
	public int countAllNotModified() throws Exception
	{
		return this.countEntitys("get_not_modified", 0);
	}

	@Override
	public List<KnowledgeBase> getAllKnowledgeNotModified(int start,int size) throws Exception
	{
		return this.getSubEntitys("get_not_modified", start, size, 0);
	}

	/*
	@Override
	public int getKnowledgeListByUserIds(List<Long> userIds) throws Exception
	{
		return this.countEntitys("get_count_by_user", userId);
	}*/

	private Date getDate(Date date,boolean beginOrEnd) {
		
		if(date != null) 
			return date;
		
		if(beginOrEnd) {
			return new Date("9999-00-00 00:00:00");
		} else {
			return new Date("0000-00-00 00:00:00");
		}
	}

	private boolean updateKnowledgeStatus(List<Long> knowledgeIds, final int status) {
		if (CollectionUtils.isNotEmpty(knowledgeIds)) {
			String ids = knowledgeIds.toString();
			ids = ids.substring(1, ids.length()-1);
			final String updateSql = "update tb_knowledge_base set status = "+ status + " where id in (" + ids + ")";
			try {
				return this.updateByRawSql(updateSql);
			} catch (Exception ex) {
				logger.error("update tb_knowledge_base set status + " + status + " knowledge failed. error: " + ex.getMessage());
			}
		}
		return false;
	}

	private boolean updateByRawSql(final String sqlStr) throws Exception {
		return this.dao.updateBySQL(sqlStr);
	}
}