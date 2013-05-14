package com.ginkgocap.ywxt.knowledge.util.gen;

import java.io.File;
import java.io.FileOutputStream;


public class GenHTML implements GenFile{

	@Override
	public File genFile(String content, String path,String fileName) {
		FileOutputStream fileoutputstream;
		File f = new File(path);
		try {
			if (!f.exists())f.mkdirs();
			fileoutputstream = new FileOutputStream(path + fileName);
			byte tag_bytes[] = content.getBytes();
			fileoutputstream.write(tag_bytes);
			fileoutputstream.close();
			System.out.print("文件输出路径:-------------" + path + fileName);
			f = new File(path + fileName);
			return f;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static void main(String[] arges){
		new GenHTML().genFile("asdfasdf", "D:/aa/ab/","a.html");
	}
}
