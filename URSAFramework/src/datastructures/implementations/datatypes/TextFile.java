package datastructures.implementations.datatypes;

import datastructures.core.DataFeature;
import datastructures.core.DataObject;


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
