package com.ginkgocap.ywxt.knowledge.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.OfficeManager;

public class OpenOfficeConvert {

	private OfficeManager officeManager;
	
	public OpenOfficeConvert(OfficeManager officeManager){
		this.officeManager = officeManager;
	}
	
	public void htmlToWord (File html,String wordPath){
	    File wordFile = new File(wordPath);  
		OfficeDocumentConverter convertHtmlToWord = new OfficeDocumentConverter(officeManager);
		convertHtmlToWord.convert(html,wordFile); 
	}
	public void wordToHtml (File word,String htmlPath){
	    File htmlFile = new File(htmlPath);
	    OfficeDocumentConverter converterWordToHtml = new OfficeDocumentConverter(officeManager);
	    converterWordToHtml.convert(word,htmlFile);		
	}
	
	
	
	public String getHTML(File HTML){
		StringBuffer htmlSb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(HTML)));
			while (br.ready()) {
				htmlSb.append(br.readLine());
			}
			br.close();
			// 删除临时文件      
			HTML.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// HTML文件字符串   
		String htmlStr =  "";
		htmlStr = htmlSb.toString();
		// 返回经过清洁的html文本    
		return clearFormat(htmlStr, HTML.getPath());
	}
	
	/**     * 清除一些不需要的html标记 
	 *     *      * @param htmlStr     *             
	 *        带有复杂html标记的html语句    
	 *         * @return 去除了不需要html标记的语句     */
	protected static String clearFormat(String htmlStr, String docImgPath) {
		// 获取body内容的正则    
		String bodyReg = "<BODY .*</BODY>";
		Pattern bodyPattern = Pattern.compile(bodyReg);
		Matcher bodyMatcher = bodyPattern.matcher(htmlStr);
		if (bodyMatcher.find()) {
			// 获取BODY内容，并转化BODY标签为DIV       
			htmlStr = bodyMatcher.group().replaceFirst("<BODY", "<DIV")
					.replaceAll("</BODY>", "</DIV>");
		}
		// 调整图片地址   
		htmlStr = htmlStr.replaceAll("<IMG SRC=\"", "<IMG SRC=\"" + docImgPath
				+ "/");
		// 把<P></P>转换成</div></div>保留样式    // content = content.replaceAll("(<P)([^>]*>.*?)(<\\/P>)",    // "<div$2</div>");   
		// 把<P></P>转换成</div></div>并删除样式  
		htmlStr = htmlStr.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
		// 删除不需要的标签    
		htmlStr = htmlStr
				.replaceAll(
						"<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|[ovwxpOVWXP]:\\w+)[^>]*?>",
						"");
		// 删除不需要的属性   
		htmlStr = htmlStr
				.replaceAll(
						"<([^>]*)(?:lang|LANG|class|CLASS|style|STYLE|size|SIZE|face|FACE|[ovwxpOVWXP]:\\w+)=(?:'[^']*'|\"\"[^\"\"]*\"\"|[^>]+)([^>]*)>",
						"<$1$2>");
		return htmlStr;
	}
	
	
	
	
	
	
	public OfficeManager getOfficeManager() {
		return officeManager;
	}
	private void setOfficeManager(OfficeManager officeManager) {
		this.officeManager = officeManager;
	}
}
