package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.dao.knowledge.KnowledgeDao;
import com.ginkgocap.ywxt.knowledge.dao.knowledgecategory.KnowledgeCategoryDAO;
import com.ginkgocap.ywxt.knowledge.dao.macro.KnowledgeMacroDAO;
import com.ginkgocap.ywxt.knowledge.model.KnowledgeMacro;
import com.ginkgocap.ywxt.knowledge.service.KnowledgeMacroService;

@Service("knowledgeMacroService")
public class KnowledgeMacroServiceImpl implements KnowledgeMacroService {

	@Autowired
	private KnowledgeDao knowledgeDao;

	@Autowired
	private KnowledgeMacroDAO knowledgeMacroDAO;

	@Autowired
	private KnowledgeCategoryDAO knowledgeBetweenDAO;

	@Override
	public void deleteKnowledge(long[] ids) {

		knowledgeMacroDAO.deleteKnowledge(ids);
	}

	@Override
	public void updateKnowledge(String title, long userid, String uname,
			long cid, String cname, String cpath, String content, String pic,
			String desc, String essence, String taskid, String tags,
			long knowledgeid) {

		knowledgeMacroDAO.updateKnowledge(title, userid, uname, cid, cname,
				cpath, content, pic, desc, essence, taskid, tags, knowledgeid);
	}

	@Override
	public KnowledgeMacro selectKnowledge(long knowledgeid) {

		return knowledgeMacroDAO.selectKnowledge(knowledgeid);
	}

	@Override
	public List<KnowledgeMacro> selectByParam(Long columnid, long source,
			Long userid, List<Long> ids, int page, int size) {
		return knowledgeMacroDAO.selectByParam(columnid, source, userid, ids,
				page, size);
	}

	@Override
	public void deleteKnowledgeByid(long knowledgeid) {
		knowledgeMacroDAO.deleteKnowledgeByid(knowledgeid);
	}

	@Override
	public KnowledgeMacro insertknowledge(String title, long userid,
			String uname, long cid, String cname, String cpath, String content,
			String pic, String desc, String essence, String taskid,
			String tags, long knowledgeid, long columnid) {

		return knowledgeMacroDAO.insertknowledge(title, userid, uname, cid,
				cname, cpath, content, pic, desc, essence, taskid, tags,
				knowledgeid, columnid);
	}

	@Override
	public void restoreKnowledgeByid(long knowledgeid) {
		knowledgeMacroDAO.restoreKnowledgeByid(knowledgeid);

	}

}
