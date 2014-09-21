package com.ginkgocap.ywxt.knowledge.service;

import com.ginkgocap.ywxt.knowledge.model.File;

/**
 * 文件元数据的service接口
 * @author lk
 * @创建时间：2013-03-29 10:40
 */
public interface FileService {
    /**
     * 通过主键获得文件元数据记录
     * @param id
     * @return
     */
    File selectByPrimaryKey(long id);
    /**
     * 删除文件元数据
     * @param id
     */
    void delete(long id);
    /**
     * 存入文件元数据
     * @param file
     * @return
     */
    File insert(File file);
    /**
     * 修改文件
     * @param category
     */
    void update(File file);
}
