package com.ginkgocap.ywxt.knowledge.util.process;

import java.util.Observable;

public class ImportWatched  extends Observable{
	private boolean done = false;
	private long data;
	private String taskId;
	private long total;
	private String option;
	private boolean flag;
	
	public void changeData(long data,String option,boolean flag) {
		this.flag = flag;
		this.data = data;
		this.option = option;
		setChanged();
		notifyObservers();
	}

	
	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}


	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}
	
}
