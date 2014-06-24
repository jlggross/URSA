package br.ufrgs.inf.jlggross.clustering;

import java.util.ArrayList;
import java.util.List;

public class DataCluster {
	private String label;
	private List<DataObject> dataObjects;
	
	public DataCluster() {
		this.dataObjects = new ArrayList<DataObject>();
	}
	
	public DataCluster(String label) {
		this();
		this.label = label;
	}
	
	public void addDataObject(DataObject dataObject) {
		this.dataObjects.add(dataObject);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<DataObject> getDataObjects() {
		return this.dataObjects;
	}
	
	public DataObject getObject(int index) {
		return this.dataObjects.get(index);
	}

	public void print() {
		System.out.println("Cluster:");
		for (DataObject dataObject : this.dataObjects) {
			System.out.println(" " + dataObject.toString());
		}
	}
}
