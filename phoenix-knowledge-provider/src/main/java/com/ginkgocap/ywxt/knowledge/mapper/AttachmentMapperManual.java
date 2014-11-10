package com.ginkgocap.ywxt.knowledge.mapper;

import org.apache.ibatis.annotations.Param;

import com.ginkgocap.ywxt.knowledge.entity.Attachment;

public interface AttachmentMapperManual {

	long insertSelective(@Param("attachment") Attachment attachment);
}
