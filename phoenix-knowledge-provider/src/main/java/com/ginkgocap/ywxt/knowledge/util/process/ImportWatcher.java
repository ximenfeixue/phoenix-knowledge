package com.ginkgocap.ywxt.knowledge.util.process;

import java.util.Observable;
import java.util.Observer;


public class ImportWatcher  implements Observer {
	public ImportWatcher(ImportWatched w) {
		w.addObserver(this);
	}

	private String mes;
	public void update(Observable ob, Object arg) {
		long data = ((ImportWatched) ob).getData();
		if (((ImportWatched) ob).isDone()){
			this.mes = "done";
		}else{
			this.mes = data + "-" + ((ImportWatched) ob).getTotal() + "-" + ((ImportWatched) ob).getOption() + "-" + ((ImportWatched) ob).isFlag();
		}
	}
	public String getMes(){
		return this.mes;
	}
	
}
