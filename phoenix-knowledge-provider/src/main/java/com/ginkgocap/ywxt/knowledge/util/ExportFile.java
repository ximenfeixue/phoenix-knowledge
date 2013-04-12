package com.ginkgocap.ywxt.knowledge.util;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import com.ginkgocap.ywxt.knowledge.model.Article;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.rtf.RtfWriter2;

public class ExportFile{

	public static void exportWordFile(Article article, String outPath,
			Map<String, String> data) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		RtfWriter2.getInstance(document, new FileOutputStream(outPath));
		document.open();
		// 添加页眉 
		HeaderFooter header = new HeaderFooter(new Phrase(article.getArticleTitle()), false); 
		header.setAlignment(Rectangle.ALIGN_CENTER);
		document.setHeader(header);
		// 添加页脚 
		HeaderFooter footer = new HeaderFooter(new Phrase(article.getArticleTitle()), false); 
		footer.setAlignment(Rectangle.ALIGN_CENTER); 
		document.setFooter(footer); 
		// 设置中文字体
		BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font contextFont = new Font(bfChinese, 10, Font.NORMAL);
		Paragraph context = new Paragraph("");
		context.setFont(contextFont);
		
		
//		
//		 StyleSheet ss = new StyleSheet();
//		 List htmlList = HTMLWorker.parseToList(new StringReader(article.getArticleContent()), ss);  
//		
//		 for (int i = 0; i < htmlList.size(); i++)
//	     {  
//	         com.lowagie.text.Element e = (com.lowagie.text.Element) htmlList.get(i);  
//	         context.add(e);  
//	     }
		
		
		context.add(article.getArticleContent());

		document.add(context);
		document.close();
	}
	

}
