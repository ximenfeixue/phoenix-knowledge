package com.ginkgocap.ywxt.knowledge.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.knowledge.utils.DateUtil;
import com.ginkgocap.ywxt.user.model.User;

@Repository("KnowledgeReferenceDao")
public class KnowledgeReferenceDao extends BaseService<KnowledgeReference> implements IKnowledgeReferenceDao {

	@Override
	public KnowledgeReference insert(KnowledgeReference knowledgeReference,long knowledgeId,User user)
			throws Exception {
		
		if(knowledgeReference == null)
			return null;
		
		if(knowledgeId <= 0) {
			throw new Exception("插入知识来源表时，知识主键缺失");
		}
		
		knowledgeReference.setKnowledgeId(knowledgeId);
		
		String currentDate = DateUtil.formatWithYYYYMMDDHHMMSS(new Date());
		
		knowledgeReference.setCreateDate(currentDate);
		knowledgeReference.setModifyDate(currentDate);
		
		long id = (Long) this.saveEntity(knowledgeReference);
		
		return this.getById(id);
	}

	@Override
	public KnowledgeReference update(KnowledgeReference knowledgeReference,User user)
			throws Exception {
		if(knowledgeReference == null)
			return null;
		
		String currentDate = DateUtil.formatWithYYYYMMDDHHMMSS(new Date());

		knowledgeReference.setModifyDate(currentDate);
		
		this.updateEntity(knowledgeReference);
		
		return this.getById(knowledgeReference.getId());
	}

	@Override
	public KnowledgeReference insertAfterDelete(
			KnowledgeReference knowledgeReference,long knowledgeId,User user) throws Exception {
		
		long id = knowledgeReference.getId();
		
		KnowledgeReference oldValue = null;
		
		if(id <= 0) {
			oldValue = this.getById(id);
			
			this.deleteById(id);
		}
		
		try {
			this.insert(knowledgeReference, knowledgeId, user);
		} catch (Exception e) {
			if(oldValue != null && oldValue.getId() > 0)
				this.insert(oldValue, oldValue.getKnowledgeId(), user);
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
	public int deleteByKnowledgeId(long knowledgeId) throws Exception {
		
		return this.deleteList("delete_by_knowledgeId", knowledgeId);
	}

	@Override
	public int deleteByKnowledgeIds(List<Long> knowledgeIds) throws Exception {
		
		if(knowledgeIds == null || knowledgeIds.size() < 1)
			return 0;
		
		return this.deleteList("delete_by_knowledgeId", knowledgeIds.toArray());
	}

	@Override
	public KnowledgeReference getById(long id) throws Exception {
		
		return this.getEntity(id);
	}

	@Override
	public List<KnowledgeReference> getByIds(List<Long> ids) throws Exception {
		
		return this.getEntityByIds(ids);
	}

	@Override
	public List<KnowledgeReference> getByIdAndStatus(long id, String status)
			throws Exception {
		
		return this.getEntitys("get_by_id_status", new Object[]{id,status});
	}

	@Override
	public List<KnowledgeReference> getByKnowledgeId(long knowledgeId)
			throws Exception {
		
		return this.getEntitys("get_by_knowledgeId", new Object[]{knowledgeId});
	}

	@Override
	public List<KnowledgeReference> getByKnowledgeIds(List<Long> knowledgeIds)
			throws Exception {
		
		if(knowledgeIds == null || knowledgeIds.size() < 1)
			return null;
		
		return this.getEntitys("get_by_knowledgeId", knowledgeIds.toArray());
	}

	@Override
	public List<KnowledgeReference> getByKnowledgeIdAndStatus(long knowledgeId,
			String status) throws Exception {
		
		return this.getEntitys("get_by_knowledgeId_status", new Object[]{knowledgeId,status});
	}
	
}