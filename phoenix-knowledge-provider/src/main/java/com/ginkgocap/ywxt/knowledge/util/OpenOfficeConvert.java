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
			// ɾ����ʱ�ļ�      
			HTML.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// HTML�ļ��ַ���   
		String htmlStr =  "";
		htmlStr = htmlSb.toString();
		System.out.println(htmlStr);
		// ���ؾ�������html�ı�    
		return clearFormat(htmlStr, HTML.getPath());
	}
	
	/**     * ���һЩ����Ҫ��html��� 
	 *     *      * @param htmlStr     *             
	 *        ���и���html��ǵ�html���    
	 *         * @return ȥ���˲���Ҫhtml��ǵ����     */
	protected static String clearFormat(String htmlStr, String docImgPath) {
		// ��ȡbody���ݵ�����    
		String bodyReg = "<BODY .*</BODY>";
		Pattern bodyPattern = Pattern.compile(bodyReg);
		Matcher bodyMatcher = bodyPattern.matcher(htmlStr);
		if (bodyMatcher.find()) {
			// ��ȡBODY���ݣ���ת��BODY��ǩΪDIV       
			htmlStr = bodyMatcher.group().replaceFirst("<BODY", "<DIV")
					.replaceAll("</BODY>", "</DIV>");
		}
		// ����ͼƬ��ַ   
		htmlStr = htmlStr.replaceAll("<IMG SRC=\"", "<IMG SRC=\"" + docImgPath
				+ "/");
		// ��<P></P>ת����</div></div>������ʽ    // content = content.replaceAll("(<P)([^>]*>.*?)(<\\/P>)",    // "<div$2</div>");   
		// ��<P></P>ת����</div></div>��ɾ����ʽ  
		htmlStr = htmlStr.replaceAll("(<P)([^>]*)(>.*?)(<\\/P>)", "<p$3</p>");
		// ɾ������Ҫ�ı�ǩ    
		htmlStr = htmlStr
				.replaceAll(
						"<[/]?(font|FONT|span|SPAN|xml|XML|del|DEL|ins|INS|meta|META|[ovwxpOVWXP]:\\w+)[^>]*?>",
						"");
		// ɾ������Ҫ������   
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
