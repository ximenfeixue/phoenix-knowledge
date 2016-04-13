package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeMysqlDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeBase;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("knowledgeMysqlDao")
public class KnowledgeMysqlDaoImpl extends BaseService<KnowledgeBase> implements KnowledgeMysqlDao {

	@Override
	public KnowledgeBase insert(KnowledgeBase knowledgeBase) throws Exception {
		
		if(knowledgeBase == null)
			return null;
		
		long id = (Long) this.saveEntity(knowledgeBase);
		
		return this.getById(id);
	}
	
	@Override
	public List<KnowledgeBase> insertList(List<KnowledgeBase> knowledgeBaseList) throws Exception {
		
		if(knowledgeBaseList == null || knowledgeBaseList.isEmpty())
			return null;
		
		return this.saveEntitys(knowledgeBaseList);
	}

	@Override
	public KnowledgeBase update(KnowledgeBase knowledgeBase) throws Exception {
		
		if(knowledgeBase == null)
			return null;
		
		this.updateEntity(knowledgeBase);
		
		return this.getById(knowledgeBase.getId());
	}

	@Override
	public KnowledgeBase insertAfterDelete(KnowledgeBase knowledgeBase)
			throws Exception {
		
		long id = knowledgeBase.getId();
		
		KnowledgeBase oldValue = null;
		
		if(id <= 0) {
			oldValue = this.getById(id);
			
			this.deleteById(id);
		}
		
		try {
			this.insert(knowledgeBase);
		} catch (Exception e) {
			if(oldValue != null && oldValue.getId() > 0)
				this.insert(oldValue);
			throw e;
		}
		
		return this.getById(id);
	}

	@Override
	public int deleteById(long id) throws Exception {
		
		boolean deleteStatus = this.deleteEntity(id);
		
		return deleteStatus ? 1 : 0;
	}

	@Override
	public int deleteByIds(List<Long> ids) throws Exception {
		
		boolean deleteStatus = this.deleteEntityByIds(ids);
		
		return deleteStatus ? 1 : 0;
	}

	@Override
	public int deleteByCreateUserId(long createUserId) throws Exception {
		
		int deleteStatus = this.deleteList("delete_by_createUserId", createUserId);
		
		return deleteStatus;
	}

	@Override
	public KnowledgeBase getById(long knowledgeId) throws Exception
    {
        List<KnowledgeBase> knowledgeBaseItems = this.getEntitys("get_knowledge_by_Id", new Object[]{knowledgeId});
        return (knowledgeBaseItems != null && knowledgeBaseItems.size() > 0) ? knowledgeBaseItems.get(0) : null;
	}

	@Override
	public List<KnowledgeBase> getByIds(List<Long> knowledgeIds) throws Exception
    {
		return this.getEntitys("get_knowledge_by_Ids", knowledgeIds.toArray());
	}

	@Override
	public List<KnowledgeBase> getAll(int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_start_size", new Object[]{start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserId(long createUserId,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_createUserId",  new Object[]{createUserId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndType(long createUserId,
			short type,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_type", new Object[]{createUserId,type,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnId(long createUserId,
			short columnId,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_columnId", new Object[]{createUserId,columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnId(short columnId,int start,int size) throws Exception {

		return this.getEntitys("get_by_columnId", new Object[]{columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByTypeAndColumnId(short type, short columnId,int start,int size)
			throws Exception {

		return this.getEntitys("get_by_type_columnId", new Object[]{type,columnId,start,size});
	}
	
	@Override
	public List<KnowledgeBase> getByType(short type,int start,int size)
			throws Exception {

		return this.getEntitys("get_by_type", new Object[]{type,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndTypeAndColumnId(
			long createUserId, short type, short columnId,int start,int size) throws Exception {

		return this.getEntitys("get_by_createUserId_type_columnId", new Object[]{createUserId,type,columnId,start,size});
	}

	@Override
	public List<KnowledgeBase> getByBetweenCreateDate(Date beginDate,
			Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_beginDate_endDate", new Object[]{getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByTypeAndBetweenCreateDate(short type,
			Date beginDate, Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_type_beginDate_endDate", new Object[]{type,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndBetweenCreateDate(
			long createUserId, Date beginDate, Date endDate,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_beginDate_endDate", new Object[]{createUserId,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndColumnIdAndBetweenCreateDate(
			long createUserId, short columnId, Date beginDate, Date endDate,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_createUserId_columnId_beginDate_endDate", new Object[]{createUserId,columnId,getDate(beginDate,true),getDate(endDate,false),start,size});
	}

	@Override
	public List<KnowledgeBase> getByStatus(short status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_status", new Object[]{status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByAuditStatus(short auditStatus,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_auditStatus", new Object[]{auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByReportStatus(short reportStatus,int start,int size)
			throws Exception {
		
		return this.getEntitys("get_by_reportStatus", new Object[]{reportStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndStatus(long createUserId,
			short status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_status", new Object[]{createUserId,status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndAuditStatus(
			long createUserId, short auditStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_auditStatus", new Object[]{createUserId,auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByCreateUserIdAndReportStatus(
			long createUserId, short reportStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_createUserId_reportStatus", new Object[]{createUserId,reportStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndStatus(short columnId,
			short status,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_status", new Object[]{columnId,status,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndAuditStatus(short columnId,
			short auditStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_auditStatus", new Object[]{columnId,auditStatus,start,size});
	}

	@Override
	public List<KnowledgeBase> getByColumnIdAndReportStatus(short columnId,
			short reportStatus,int start,int size) throws Exception {
		
		return this.getEntitys("get_by_columnId_reportStatus", new Object[]{columnId,reportStatus,start,size});
	}
	
	private Date getDate(Date date,boolean beginOrEnd) {
		
		if(date != null) 
			return date;
		
		if(beginOrEnd) {
			return new Date("9999-00-00 00:00:00");
		} else {
			return new Date("0000-00-00 00:00:00");
		}
		
	}
	
}