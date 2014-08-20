package br.ufrgs.inf.jlggross.documentclustering;

import br.ufrgs.inf.jlggross.clustering.DataFeature;

public class MetaData extends DataFeature {
	private String id;
	private String value;
	
	public MetaData(String id, String value) {
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getValue() {
		return this.value;
	}

	@Override
	public boolean equals(DataFeature feature) {
		return this.value.equals(((MetaData)feature).getValue());
	}
}
