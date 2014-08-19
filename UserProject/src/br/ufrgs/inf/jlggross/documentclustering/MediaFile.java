package br.ufrgs.inf.jlggross.documentclustering;

import java.io.File;

import br.ufrgs.inf.jlggross.clustering.DataFeature;
import br.ufrgs.inf.jlggross.clustering.DataObject;

public class MediaFile extends DataObject {
	protected String title;
	protected String filepath;
	
	public MediaFile(String title, String filepath, int index) {
		this.title = title;
		this.filepath = filepath;
		this.index = index;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getFilepath() {
		return this.filepath;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DataFeature feature : this.featureCollection) {
			str.append(feature.toString()).append("\n");
		}
		return str.toString();
	}
}
