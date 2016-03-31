package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.IKnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeReference;
import com.ginkgocap.ywxt.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
		
		long currentDate = new Date().getTime();
		knowledgeReference.setModifyDate(currentDate);
		
		long id = (Long) this.saveEntity(knowledgeReference);
		
		return this.getById(id);
	}
	
	@Override
	public List<KnowledgeReference> insertList(List<KnowledgeReference> knowledgeReferenceList,User user)
			throws Exception {
		
		if(knowledgeReferenceList == null || knowledgeReferenceList.isEmpty())
			return null;

        long currentDate = new Date().getTime();
		for (KnowledgeReference date : knowledgeReferenceList) {
			if(date.getKnowledgeId() <= 0) {
				throw new Exception("插入知识来源表时，知识主键缺失");
			}
			date.setModifyDate(currentDate);
		}
		
		return this.saveEntitys(knowledgeReferenceList);
		
	}

	@Override
	public KnowledgeReference update(KnowledgeReference knowledgeReference,User user)
			throws Exception {
		if(knowledgeReference == null)
			return null;

        long currentDate = new Date().getTime();
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
			if(oldValue != null && oldValue.getId() > 0) {
                this.insert(oldValue, oldValue.getKnowledgeId(), user);
            }
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
		
		return this.deleteList("delete_by_knowledgeIds", knowledgeIds.toArray());
	}

	@Override
	public KnowledgeReference getById(long id) throws Exception {
		
		return this.getEntity(id);
	}

	@Override
	public List<KnowledgeReference> getByIds(List<Long> ids) throws Exception {
		
		if(ids == null || ids.size() < 1)
			return null;
		
		return this.getEntityByIds(ids);
	}

	@Override
	public KnowledgeReference getByIdAndStatus(long id, String status)
			throws Exception {
		
		List<KnowledgeReference> list = this.getEntitys("get_by_id_status", new Object[]{id,status});
		
		if(list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public KnowledgeReference getByKnowledgeId(long knowledgeId)
			throws Exception {
		
		List<KnowledgeReference> list = this.getEntitys("get_by_knowledgeId", new Object[]{knowledgeId});
		
		if(list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	public List<KnowledgeReference> getByKnowledgeIds(List<Long> knowledgeIds)
			throws Exception {
		
		if(knowledgeIds == null || knowledgeIds.size() < 1)
			return null;
		
		return this.getEntitys("get_by_knowledgeIds", knowledgeIds.toArray());
	}

	@Override
	public KnowledgeReference getByKnowledgeIdAndStatus(long knowledgeId,
			String status) throws Exception {
		
		List<KnowledgeReference> list = this.getEntitys("get_by_knowledgeId_status", new Object[]{knowledgeId,status});
		
		if(list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
}