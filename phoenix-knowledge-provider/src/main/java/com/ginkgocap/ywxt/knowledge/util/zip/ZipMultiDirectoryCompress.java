package com.ginkgocap.ywxt.knowledge.util.zip;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipMultiDirectoryCompress {

    public static void main(String[] args) {
        //初始化支持多级目录压缩的ZipMultiDirectoryCompress
        ZipMultiDirectoryCompress zipCompress = new ZipMultiDirectoryCompress();
        //压缩目录，可以指向一个文件
        String directory = "D:/ziptest/zip";
        //生成的压缩文件
        String destFile = "D:/ziptest/zip1.rar";
        //默认的相对地址，为根路径
        String defaultParentPath = "";
        ZipOutputStream zos = null;
        try {
            //创建一个Zip输出流
            zos = new ZipOutputStream(new FileOutputStream(destFile));
            //启动压缩进程
            zipCompress.startCompress(zos,defaultParentPath,directory);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } finally{
            try {
                if(zos != null)zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }   
       
    }   
   
   
    public void startCompress(ZipOutputStream zos, String oppositePath, String directory){
        File file = new File(directory);
        if(file.isDirectory()){
            //如果是压缩目录
            File[] files = file.listFiles();
            for(int i=0;i < files.length; i ++){ 
            		File aFile = files[i];               
                if(aFile.isDirectory()){
                    //如果是目录，修改相对地址
                    String newOppositePath = oppositePath + aFile.getName() + "/";
                    //创建目录
                    compressDirectory(zos, oppositePath, aFile);
                    //进行递归调用
                    startCompress(zos,newOppositePath,aFile.getPath());
                } else {
                    //如果不是目录，则进行压缩
                    compressFile(zos,oppositePath,aFile);
                }
            }
        } else {
            //如果是压缩文件，直接调用压缩方法进行压缩
            compressFile(zos,oppositePath,file);
        }
    }
   
   
    public void compressFile(ZipOutputStream zos, String oppositePath, File file){
        //创建一个Zip条目，每个Zip条目都是必须相对于根路径
        ZipEntry entry = new ZipEntry(oppositePath + file.getName());
        InputStream is = null;
        try{
            //将条目保存到Zip压缩文件当中
            zos.putNextEntry(entry);
            //从文件输入流当中读取数据，并将数据写到输出流当中.
            is = new FileInputStream(file);           
            int length = 0;
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            while((length=is.read(buffer,0,bufferSize))>=0){
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }catch(IOException ex){
            ex.printStackTrace();
        } finally {
            try{
                if(is != null)is.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }       
    }
   
   
    public void compressDirectory(ZipOutputStream zos, String oppositePath, File file){
        //压缩目录，这是关键，创建一个目录的条目时，需要在目录名后面加多一个"/"
        ZipEntry entry = new ZipEntry(oppositePath + file.getName() + "/");
        try {
            zos.putNextEntry(entry);
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}