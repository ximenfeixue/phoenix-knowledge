package com.ginkgocap.ywxt.knowledge.service;

/**
 * Created by gintong on 2016/7/23.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import com.ginkgocap.ywxt.knowledge.model.mobile.JTFile;
import com.ginkgocap.ywxt.knowledge.service.JTFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ginkgocap.ywxt.file.model.FileIndex;
import com.ginkgocap.ywxt.file.service.FileIndexService;

@Repository("jtFileService")
public class JTFileService {
    @Autowired
    private FileIndexService fileIndexService;

    public List<JTFile> getJTFileByTaskId(String taskId) {
        List<JTFile> jtFileList=new ArrayList<JTFile>();
        if (StringUtils.isNotBlank(taskId)) {
            List<FileIndex> fileList=fileIndexService.selectByTaskId(taskId, "1");

            if(fileList!=null && fileList.size()>0){
                for (Iterator<FileIndex> iterator = fileList.iterator(); iterator.hasNext();) {
                    FileIndex fileIndex = iterator.next();
                    JTFile jtFile = fileIndexToJTfile(fileIndex);
                    jtFileList.add(jtFile);
                }
            }
        }
        return jtFileList;
    }

    public void deleteJTFileById(long id){
        fileIndexService.delete(id);
    }

    public static JTFile fileIndexToJTfile(FileIndex fileIndex){
        JTFile jtFile=new JTFile();
        if(null!=fileIndex){
//			fileIndex.get.
            jtFile.setId(fileIndex.getId());
            jtFile.setFileName(fileIndex.getFileTitle());
            jtFile.setFileSize(fileIndex.getFileSize());
            jtFile.setModuleType(fileIndex.getModuleType()+"");
            String[] nameSplit=fileIndex.getFileTitle().split("\\.");
            jtFile.setSuffixName(nameSplit.length>0?nameSplit[nameSplit.length-1]:"");
            jtFile.setTaskId(fileIndex.getTaskId());
            ResourceBundle resourceBundle =  ResourceBundle.getBundle("application");
            String nginxRoot=resourceBundle.getString("nginx.root");
            jtFile.setUrl(nginxRoot+"/mobile/download?id="+fileIndex.getId());
        }
        return jtFile;
    }
}
