package com.ginkgocap.ywxt.knowledge.util;

import java.io.File;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.OfficeManager;

public class OpenOfficeConvert {

	private OfficeManager officeManager;
	
	public OpenOfficeConvert(){
		officeManager = OpenOfficeServer.getInstance().getOfficeManager();
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
	public OfficeManager getOfficeManager() {
		return officeManager;
	}
	private void setOfficeManager(OfficeManager officeManager) {
		this.officeManager = officeManager;
	}
	
}
