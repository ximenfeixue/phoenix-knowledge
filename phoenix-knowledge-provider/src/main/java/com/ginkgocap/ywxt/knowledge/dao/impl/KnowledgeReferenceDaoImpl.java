package com.ginkgocap.ywxt.knowledge.dao.impl;

import com.ginkgocap.parasol.common.service.impl.BaseService;
import com.ginkgocap.ywxt.knowledge.dao.KnowledgeReferenceDao;
import com.ginkgocap.ywxt.knowledge.model.common.KnowledgeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("knowledgeReferenceDao")
public class KnowledgeReferenceDaoImpl extends BaseService<KnowledgeReference> implements KnowledgeReferenceDao {
	private Logger logger = LoggerFactory.getLogger(KnowledgeReferenceDaoImpl.class);
	@Override
	public KnowledgeReference insert(KnowledgeReference knowledgeReference)
			throws Exception {
		
		if(knowledgeReference == null) {
            throw new IllegalArgumentException("knowledgeReference is null");
        }
		
		if(knowledgeReference.getKnowledgeId() <= 0) {
			throw new Exception("插入知识来源表时，知识ID缺失");
		}
		
		long currentDate = new Date().getTime();
		knowledgeReference.setModifyDate(currentDate);
		long id = (Long) this.saveEntity(knowledgeReference);
		
		return knowledgeReference;
	}
	
	@Override
	public List<KnowledgeReference> insertList(List<KnowledgeReference> knowledgeReferenceList)
			throws Exception {
		
		if(knowledgeReferenceList == null || knowledgeReferenceList.isEmpty()) {
			return null;
		}

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
	public KnowledgeReference update(KnowledgeReference knowledgeReference)
			throws Exception {
		if(knowledgeReference == null) {
			return null;
		}

		long knowledgeId = knowledgeReference.getKnowledgeId();
		KnowledgeReference oldValue = this.getByKnowledgeId(knowledgeId);
		if (oldValue == null) {
			logger.error("update an not exist knowledge, knowledgeId: {}",knowledgeId);
			return null;
		}

		knowledgeReference.setId(oldValue.getId());
		knowledgeReference.setModifyDate(new Date().getTime());
		this.updateEntity(knowledgeReference);
		
		return knowledgeReference;
	}

	@Override
	public KnowledgeReference insertAfterDelete(
			KnowledgeReference knowledgeReference) throws Exception {
		
		long id = knowledgeReference.getId();
		
		KnowledgeReference oldValue = null;
		
		if(id <= 0) {
			oldValue = this.getById(id);
			this.deleteById(id);
		}
		
		try {
			this.insert(knowledgeReference);
		} catch (Exception e) {
			if(oldValue != null && oldValue.getId() > 0) {
                this.insert(oldValue);
            }
			throw e;
		}
		
		return knowledgeReference;
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
		
		return this.deleteList("delete_reference_by_knowledgeId", new Object[]{knowledgeId});
	}

	@Override
	public int batchDeleteByKnowledgeIds(List<Long> knowledgeIds) throws Exception {
		
		for(Long knowledgeId : knowledgeIds) {
            this.deleteByKnowledgeId(knowledgeId);
        }
		return knowledgeIds.size();
        //this.deleteList("delete_reference_by_knowledgeIds", knowledgeIds.toArray());
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
		
		List<KnowledgeReference> list = this.getEntitys("get_reference_by_Id", new Object[]{knowledgeId});
		
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
		
		return this.getEntitys("get_reference_by_Ids", knowledgeIds.toArray());
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