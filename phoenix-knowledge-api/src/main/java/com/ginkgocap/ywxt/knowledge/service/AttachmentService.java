package com.ginkgocap.ywxt.knowledge.service;

import java.util.Map;

import com.ginkgocap.ywxt.knowledge.entity.Attachment;

public interface AttachmentService {

	Map<String, Object> addAttachmentFile(Attachment att);

	Map<String, Object> addAttachmentFile(String taskId, String fileName,
			Integer fileSize, String fileType, String downloadUrl, long userId);
}
