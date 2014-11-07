package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Attachment;
import com.ginkgocap.ywxt.knowledge.mapper.AttachmentMapper;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Resource
	private AttachmentMapper attachmentMapper;

	@Override
	public Map<String, Object> addAttachmentFile(Attachment att) {
		Map<String, Object> result = new HashMap<String, Object>();
		int v = attachmentMapper.insertSelective(att);
		if (v > 0) {
			result.put(Constants.status, Constants.ResultType.success.v());
		} else {
			result.put(Constants.status, Constants.ResultType.fail.v());
		}
		return result;
	}

	@Override
	public Map<String, Object> addAttachmentFile(String taskId,
			String fileName, Integer fileSize, String fileType,
			String downloadUrl, long userId) {

		Attachment att = new Attachment();
		att.setFileName(fileName);
		att.setFileSize(Long.parseLong(fileSize + ""));
		att.setDownloadUrl(downloadUrl);
		att.setTaskid(taskId);
		att.setUserid(userId);
		att.setFileType(fileType);
		return addAttachmentFile(att);
	}

}
