package com.ginkgocap.ywxt.knowledge.util;

import java.io.File;

public class DG {
	public static void main(String[] args) {
		File f = new File("d:\\ziptest\\zip\\技术文档\\iTextAPI.doc");
//		System.out.println(f.getPath() + f.getName());

		tree(f);

	}

//	private static void tree(File f) {
//
//		File[] childs = f.listFiles();// 列出目录下所有的文件
//		for (int i = 0; i < childs.length; i++) {
//			System.out.println(childs[i].getPath() + childs[i].getName());
//			if (childs[i].isDirectory()) {
//				tree(childs[i]);// 如果是个目录,就继续递归列出其下面的文件.
//			}
//		}
//	}
	
	
	
	private static void tree(File f){
		if (f.isDirectory()){
			File[] list = f.listFiles();
			for (int i = 0; i < list.length; i ++){
				System.out.println(list[i].getPath());
				if (list[i].isDirectory()){
					tree(list[i]);
				}
			}
		}else{
			System.out.println(f.getPath());
		}
	}
}
