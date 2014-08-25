package clusteringstrategies.core;

import java.util.Observable;

public abstract class BaseStrategy extends Observable {
	private double progress;
	
	protected void setProgress(double progress) {
		this.progress = progress;
		this.setChanged();
		this.notifyObservers();
	}
	
	public double getProgress() {
		return progress;
	}
}
