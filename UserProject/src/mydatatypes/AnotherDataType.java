package mydatatypes;

import datastructures.core.DataFeature;
import datastructures.core.DataObject;

public class AnotherDataType extends DataObject {
	
	public AnotherDataType() {
		// TODO
	}
	
	// TODO
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DataFeature feature : this.featureCollection) {
			str.append(feature.toString()).append("\n");
		}
		return str.toString();
	}
}
