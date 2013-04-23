package com.ginkgocap.ywxt.knowledge.util.process;

import java.util.Observable;
import java.util.Observer;


public class ExportWatcher  implements Observer {
	public ExportWatcher(ExportWatched w) {
		w.addObserver(this);
	}

	private String mes;
	
	private String progen;
	
	public void update(Observable ob, Object arg) {
		long data = ((ExportWatched) ob).getData();
		ExportWatched watched = (ExportWatched)ob;
		if (((ExportWatched) ob).isDone()){
			this.mes = "done";
			//返回生成的压缩文件路径
			this.progen = watched.getDownloadPath();
		}else{
			this.mes = data + "-" +watched.getTotal() + "-" + watched.getOption() + "-" +watched.isFlag();
		}
	}
	public String getMes(){
		return this.mes;
	}
	public String getProgen() {
		return this.progen;
	}
}