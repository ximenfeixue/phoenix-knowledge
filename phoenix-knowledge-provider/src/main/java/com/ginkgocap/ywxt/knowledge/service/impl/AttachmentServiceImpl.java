package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.HashMap;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.knowledge.entity.Attachment;
import com.ginkgocap.ywxt.knowledge.mapper.AttachmentMapper;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Resource
	private AttachmentMapper attachmentMapper;

	private final Logger logger = LoggerFactory
			.getLogger(AttachmentServiceImpl.class);

	@Override
	public Map<String, Object> addAttachmentFile(Attachment att) {
		logger.info("进入添加附件文件请求");
		Map<String, Object> result = new HashMap<String, Object>();
		int v = attachmentMapper.insertSelective(att);

		if (v > 0) {
			logger.info("添加附件到数据库请F求成功!");
			result.put(Constants.status, Constants.ResultType.success.v());
		} else {
			logger.error("添加附件到数据库请求失败,用户：{}", att.getUserid());
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
		att.setFileSize(Long.parseLong(fileSize == null ? "0" : fileSize + ""));
		att.setDownloadUrl(downloadUrl);
		att.setTaskid(taskId);
		att.setUserid(userId);
		att.setFileType(fileType);

		return addAttachmentFile(att);
	}

}
