package com.ginkgocap.ywxt.knowledge.util.zip;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * �ļ�ѹ����
 * 
 * @author lk
 * @version 2013-04-11
 */
public class ZipUtil extends ZipOutputStream {

	public ZipUtil(OutputStream outputStream) {
		this(outputStream, defaultEncoding, defaultLevel);
	}

	public ZipUtil(String file) throws IOException {
		this(new FileOutputStream(new File(file)), defaultEncoding,
				defaultLevel);
	}

	public ZipUtil(File file) throws IOException {
		this(new FileOutputStream(file), defaultEncoding, defaultLevel);
	}
	/**
	 * ͳһ���õĹ��캯��
	 * 
	 * @param outputStream
	 *            �����(���·��),*.zip
	 * @param encoding
	 *            ����
	 * @param level
	 *            ѹ������ 0-9
	 * */
	public ZipUtil(OutputStream outputStream, String encoding, int level) {
		super(outputStream);

		buf = new byte[1024];// 1024 KB����

		if (encoding != null || !"".equals(encoding))
			this.setEncoding(encoding);

		if (level < 0 || level > 9)
			level = 7;
		this.setLevel(level);

		comment = new StringBuffer();
	}

	public String put(String fileName) throws IOException {
		return put(fileName, "");
	}

	/**
	 * ����Ҫѹ�����ļ����ļ���
	 * 
	 * @param fileName
	 *            ����һ���ļ�,��һ���ļ���
	 * @param pathName
	 *            ����ZIPʱ�ӵ��ļ���·��
	 * @return fileName
	 * */
	public String put(String fileName, String pathName) throws IOException {
		File file = new File(fileName);

		if (!file.exists()) {
			comment.append("����һ�������ڵ��ļ���Ŀ¼: ").append(fileName).append("\n");
			return null;
		}

		// �ݹ�����ļ�
		if (file.isDirectory()) {
			pathName += file.getName() + "/";
			String fileNames[] = file.list();
			if (fileNames != null) {
				for (String f : fileNames)
					put(fileName + File.separator + f, pathName);
			}
			return fileName;
		}

		fileCount++;
		// System.out.println(fileCount + " = " + fileName);
		// System.out.println("file = " + file.getAbsolutePath());

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(file));
			out = new BufferedOutputStream(this);
			if (userFullPathName)
				pathName += file.getPath();
			this.putNextEntry(new ZipEntry(pathName + file.getName()));
			int len;
			// BufferedOutputStream���Զ�ʹ�� this.buf,�����ʹ��in.read(buf)���ݻ����
			while ((len = in.read()) > -1)
				out.write(len);
		} catch (IOException ex) {
			comment.append("һ���ļ���ȡд��ʱ����: ").append(fileName).append("\n");
		}

		if (out != null)
			out.flush();
		if (in != null)
			in.close();

		this.closeEntry();
		return file.getAbsolutePath();
	}

	public String[] put(String[] fileName) throws IOException {
		return put(fileName, "");
	}

	public String[] put(String[] fileName, String pathName) throws IOException {
		for (String file : fileName)
			put(file, pathName);
		return fileName;
	}

	private List tree(File f) {
		
		if (f.isDirectory()) {
			File[] cf = f.listFiles();
			for (int i = 0; i < cf.length; i++) {
				fileList.add(cf[i].getPath());
				if (cf[i].isDirectory()) {
					tree(cf[i]);
				}
			}
		} else {
			fileList.add(f.getPath());
		}
		return fileList;
	}

	/**
	 * ѹ�����ļ�����
	 * 
	 * @return int
	 * */
	public int getFileCount() {
		return this.fileCount;
	}

	
	// ����
	public static void main(String[] args) {
		try {
			java.util.Date d1 = new java.util.Date();
			ZipUtil util = new ZipUtil("D:/GENPATH_10000/zip.zip");
			// util.buf = new byte[1024*2]; //����ָ������
			util.comment.append("������������!\n\n");
//			File file = new File("D:/GENPATH_10000/2013-05-03-17-32-51");
//			File[] files = file.listFiles();
//			String[] fileNames = new String[files.length];
//			for(int i =0; i < fileNames.length; i ++){
//				fileNames[i] = files[i].getPath();
//			}
			util.put(new String[]{"D:/GENPATH_10000/2013-05-03-17-32-51"});
			util.comment.append("\n���ɹ�ѹ���ļ�: ").append(util.getFileCount())
					.append(" ��!");
			util.setComment(util.comment.toString());
			util.close();
			java.util.Date d2 = new java.util.Date();
			System.out.println("used time = " + (d2.getTime() - d1.getTime()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private List fileList = new ArrayList();
	// ѹ������:0-9
	public static int defaultLevel = 7;
	// ����,����:GB2312,����:BIG5
	public static String defaultEncoding = "UTF-8";
	// ѹ��ʱ��ȫ·��,�����ɶ�Ӧ��Ŀ¼,false:����·��,ֻ���ļ���
	public static boolean userFullPathName = false;
	// ע��
	public StringBuffer comment;

	private int fileCount = 0;
}