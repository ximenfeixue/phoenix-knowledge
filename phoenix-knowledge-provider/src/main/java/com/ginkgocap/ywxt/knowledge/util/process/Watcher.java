package com.ginkgocap.ywxt.knowledge.util.process;

import java.util.Observable;
import java.util.Observer;


public class Watcher  implements Observer {
	public Watcher(Watched w) {
		w.addObserver(this);
	}

	private String mes;
	public void update(Observable ob, Object arg) {
		long data = ((Watched) ob).getData();
		if (((Watched) ob).isDone()){
			this.mes = "done";
		}else{
			this.mes = data + "-" + ((Watched) ob).getTotal() + "-" + ((Watched) ob).getOption() + "-" + ((Watched) ob).isFlag();
		}
	}
	public String getMes(){
		return this.mes;
	}
	
}
