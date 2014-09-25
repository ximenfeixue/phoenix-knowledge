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
			String uname, long cid, String cname, String source, String s_addr,
			String cpathid, String pic, String desc, long content,
			String hcontent, int essence, Date createtime, int status,
			int report_status, String taskid, String tags) {

		KnowledgeNews knowledgenews = new KnowledgeNews();
		knowledgenews.setId(id);
		knowledgenews.setTitle(title);
		knowledgenews.setUid(userid);
		knowledgenews.setUname(uname);
		knowledgenews.setCid(cid);
		knowledgenews.setCname(cname);
		knowledgenews.setSource(source);
		knowledgenews.setS_addr(s_addr);
		knowledgenews.setHcontent(hcontent);
		knowledgenews.setEssence(essence);
		knowledgenews.setCreatetime(createtime);
		knowledgenews.setStatus(Constants.Status.draft.v());
		knowledgenews.setReport_status(report_status);
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
