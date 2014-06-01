package br.ufrgs.inf.jlggross.clustering;

import java.util.ArrayList;
import java.util.List;

public abstract class DataObject {
	protected List<DataFeature> featureCollection;
	
	public DataObject() {
		this.clearFeatureList();
	}
	
	public void addFeature(DataFeature dataFeature) {
		this.featureCollection.add(dataFeature);
	}
	
	public List<DataFeature> getFeatureList() {
		return this.featureCollection;
	}
	
	public void clearFeatureList() {
		this.featureCollection = new ArrayList<DataFeature>();
	}
	
	public abstract String toString();
}
