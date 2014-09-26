package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.news.KnowledgeNewsDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeNews;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeDraftService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("knowledgeDraftService")
public class KnowledgeDraftServiceImpl implements KnowledgeDraftService {

	@Autowired
	private KnowledgeNewsDAO knowledgeNewsDAO;

	@Override
	public int insertKnowledgeDraft(long id, String title, long userid,
			String uname, long cid, String cname, String pic, String desc,
			String content, int essence, String taskid, String tags) {
		KnowledgeNews knowledgenews = new KnowledgeNews();
		knowledgenews.setId(id);
		knowledgenews.setTitle(title);
		knowledgenews.setUid(userid);
		knowledgenews.setUname(uname);
		knowledgenews.setCid(cid);
		knowledgenews.setCname(cname);
		knowledgenews.setEssence(essence);
		knowledgenews.setCreatetime(new Date());
		knowledgenews.setStatus(Constants.Status.draft.v());
		knowledgenews.setReport_status(0);
		knowledgenews.setTaskid(taskid);
		knowledgenews.setTags(tags);
		KnowledgeNews result = knowledgeNewsDAO.insertknowledge(knowledgenews);
		if (result.getId() > 0) {
			return 1;
		} else {
			return 0;
		}
	}

}
