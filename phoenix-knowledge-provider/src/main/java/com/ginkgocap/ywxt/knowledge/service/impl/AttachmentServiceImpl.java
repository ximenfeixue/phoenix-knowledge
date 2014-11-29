package com.ginkgocap.ywxt.knowledge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;
import com.ginkgocap.ywxt.knowledge.entity.Attachment;
import com.ginkgocap.ywxt.knowledge.mapper.AttachmentMapper;
import com.ginkgocap.ywxt.knowledge.mapper.AttachmentMapperManual;
import com.ginkgocap.ywxt.knowledge.service.AttachmentService;
import com.ginkgocap.ywxt.knowledge.util.Constants;

@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

	@Resource
	private AttachmentMapper attachmentMapper;
	@Resource
	private AttachmentMapperManual attachmentMapperManual;
	@Resource
	private FileIndexService fileIndexService;

	private final Logger logger = LoggerFactory
			.getLogger(AttachmentServiceImpl.class);

	@Override
	public Map<String, Object> addAttachmentFile(Attachment att) {
		logger.info("进入添加附件文件请求");
		Map<String, Object> result = new HashMap<String, Object>();
		long attId = attachmentMapperManual.insertAndGetId(att);

		if (attId > 0) {
			logger.info("添加附件到数据库请F求成功,附件ID:{}", attId);
			result.put(Constants.status, Constants.ResultType.success.v());
			result.put("attId", att.getId());
		} else {
			logger.error("添加附件到数据库请求失败,用户：{}", att.getUserid());
			result.put(Constants.status, Constants.ResultType.fail.v());
			result.put(Constants.errormessage, "添加附件到数据库请求失败");
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
		att.setCreatetime(new Date());

		return addAttachmentFile(att);
	}

	@Override
	public Map<String, Object> queryAttachmentByTaskId(String taskId) {
		logger.info("进入查询知识附件请求,知识Id:{}", taskId);
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(taskId)) {
			result.put("hasAtt", false);
			return result;
		}
		/***-----------------这接口写的真气人-------不写注释我怎么知道status传个毛-------------------------------***/
		List<FileIndex> fiList = fileIndexService.selectByTaskId(taskId, "1");
		result.put("attList", fiList);
		result.put("hasAtt", fiList == null || fiList.size() == 0 ? false
				: true);
		return result;
	}

	@Override
	public Integer deleteAttachment(long attId) {
		logger.info("进入删除附件请求,附件Id:{}", attId);
		fileIndexService.delete(attId);

		logger.info("删除附件成功,附件Id:{}", attId);
		//删除方法没返回值，只能返回1
		return 1;
	}

	@Override
	public FileIndex selectByPrimaryKey(long attId) {
		logger.info("进入获取附件请求,附件Id:{}", attId);
		FileIndex fileIndex = fileIndexService.selectByPrimaryKey(attId);

		logger.info("获取附件成功,附件Id:{}", attId);
		return fileIndex;
	}
}
