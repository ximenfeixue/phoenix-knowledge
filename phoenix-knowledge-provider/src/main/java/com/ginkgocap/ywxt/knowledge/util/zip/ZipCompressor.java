package com.ginkgocap.ywxt.knowledge.util.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

public class ZipCompressor {
	static final int BUFFER = 8192;

	private File zipFile;

	public ZipCompressor(String pathName) {
		zipFile = new File(pathName);
	}

	public void compress(String srcPathName) {
		File file = new File(srcPathName);
		if (!file.exists())
			throw new RuntimeException(srcPathName + "不存在！");
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());
			ZipOutputStream out = new ZipOutputStream(cos);
			String basedir = "";
//			compress(file, out, basedir);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void compress(File file, String basedir) {
		/* 判断是目录还是文件 */
		
		if (file.isDirectory()) {
			System.out.println(basedir + file.getName());
			compress(file,basedir);
		} else {
			System.out.println(basedir + file.getName());
		}
	}

	/** 压缩一个目录 */
	private void compressDirectory(File dir,  String basedir) {
		if (!dir.exists())
			return;

		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
//			System.out.println(files[i].getName());
			/* 递归 */
			compress(files[i], basedir + dir.getName() + "/");
		}
	}

	/** 压缩一个文件 */
	private void compressFile(File file, ZipOutputStream out, String basedir) {
		if (!file.exists()) {
			return;
		}
		try {
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			ZipEntry entry = new ZipEntry(basedir + file.getName());
			out.putNextEntry(entry);
			int count;
			byte data[] = new byte[BUFFER];
			while ((count = bis.read(data, 0, BUFFER)) != -1) {
				out.write(data, 0, count);
			}
			bis.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public static void main(String[] args){
		ZipCompressor zc = new ZipCompressor("D:\\zipTest.zip");
//		zc.compress("D:\\zipTest");
		zc.compressDirectory(new File("D:\\ziptest\\zip"),  "");
	}
}
