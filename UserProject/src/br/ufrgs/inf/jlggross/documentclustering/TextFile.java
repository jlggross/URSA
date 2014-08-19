package br.ufrgs.inf.jlggross.documentclustering;

import br.ufrgs.inf.jlggross.clustering.DataFeature;
import br.ufrgs.inf.jlggross.clustering.DataObject;

public class TextFile extends DataObject {
	private String title;
	private String content;
	
	public TextFile(String title, String content, int index) {
		this.title = title;
		this.content = content;
		this.index = index;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DataFeature feature : this.featureCollection) {
			str.append(feature.toString()).append("\n");
		}
		return str.toString();
	}
}
