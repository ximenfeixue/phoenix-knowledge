package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Attachment;

public interface AttachmentService {

	/**
	 * 添加附件
	 * @param att
	 * @return
	 */
	Map<String, Object> addAttachmentFile(Attachment att);

	/**
	 * 添加附件
	 * @param taskId 
	 * @param fileName 文件名
	 * @param fileSize 文件大小
	 * @param fileType 文件类型
	 * @param downloadUrl 下载地址
	 * @param userId 用户Id
	 * @return
	 */
	Map<String, Object> addAttachmentFile(String taskId, String fileName,
			Integer fileSize, String fileType, String downloadUrl, long userId);

	/**
	 * 查询附件
	 * @param taskid 任务Id(知识ID,为了与之前概念保持一致)
	 * @return
	 */
	Map<String, Object> queryAttachmentByTaskId(String taskid);
}
