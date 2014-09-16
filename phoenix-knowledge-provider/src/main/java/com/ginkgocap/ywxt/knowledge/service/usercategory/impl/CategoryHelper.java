package com.ginkgocap.ywxt.knowledge.service.usercategory.impl;

public class CategoryHelper {
	//给新增的分类生成新的sortId
	public String generateSortId(String currentSortId) throws Exception{
		try{
			int clen = currentSortId.length();
			String lastCurrentSort = currentSortId.substring(clen - 9 ,clen);
			String temp = (Integer.parseInt(lastCurrentSort) + 1) + "";
			String tempstr = "";
			for(int i = 1; i <= (9-temp.length()); i ++)tempstr += "0";
			String newSortId  = currentSortId.substring(0,clen - 9) + tempstr + temp;
			return newSortId;
		}catch(Exception e){
			throw new Exception("generateSortId_err");
		}
	}
}
