package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.user.form.DataGridModel;
import com.ginkgocap.ywxt.user.model.User;

public interface KnowledgeReportService {

	/**
	 * 添加举报信息
	 * 
	 * @param kid
	 *            知识id
	 * @param type
	 *            举报类型
	 * @param desc
	 *            描述信息
	 * @param user
	 *            用户
	 * @param title
	 *            标题
	 * @param columnType
	 *            栏目类型
	 * @return
	 */
	Map<String, Object> addReport(long kid, String type, String desc,
			User user, String title, String columnType);

	/**
	 * 分页对象
	 * 
	 * @param dgm
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectByParam(DataGridModel dgm,
			Map<String, String> map);

	/**
	 * 处理
	 * 
	 * @param id
	 *            举报记录id
	 * @param status
	 *            状态 0 未处理 1已处理
	 * @param info
	 *            发聩内容
	 * @return
	 */
	public int updateStatus(long id, int status, String info);

	/**
	 * 删除知识下的举报记录
	 * 
	 * @param id
	 * @return
	 */
	public int deleteByKnowledgeId(long id);
}
