package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.mobile.JTFile;
import java.util.List;

/**
 * Created by gintong on 2016/7/12.
 */
public interface JTFileService
{
    /**
     * 根据taskId查询附件列表
     * @param taskId
     * @return
     */
    public List<JTFile> getJTFileByTaskId(String taskId);

    /**
     * @desc 根据附件id删除附件
     * @param id
     */
    public void deleteJTFileById(long id);
}
