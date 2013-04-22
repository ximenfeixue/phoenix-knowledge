package com.ginkgocap.ywxt.knowledge.util.process;

import java.util.Observable;
import java.util.Observer;


public class ExportWatcher  implements Observer {
	public ExportWatcher(ExportWatched w) {
		w.addObserver(this);
	}

	private String mes;
	public void update(Observable ob, Object arg) {
		long data = ((ExportWatched) ob).getData();
		if (((ExportWatched) ob).isDone()){
			this.mes = "done";
		}else{
			this.mes = data + "-" + ((ExportWatched) ob).getTotal() + "-" + ((ExportWatched) ob).getOption() + "-" + ((ExportWatched) ob).isFlag();
		}
	}
	public String getMes(){
		return this.mes;
	}
	
}
