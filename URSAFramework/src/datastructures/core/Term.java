package datastructures.core;


public class Term extends DataFeature {
	private String value;
	private double absoluteFrequency;
	private double relativeFrequency;
	
	public Term(String value, double weight) {
		this.value = value;
		this.absoluteFrequency = weight;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public double getAbsoluteFrequency() {
		return this.absoluteFrequency;
	}
	
	public void setAbsoluteFrequency(double absoluteFrequency) {
		this.absoluteFrequency = absoluteFrequency;
	}
	
	public double getRelativeFrequency() {
		return this.relativeFrequency;
	}
	
	public void setRelativeFrequency(double relativeFrequency) {
		this.relativeFrequency = relativeFrequency;
	}

	@Override
	public boolean equals(DataFeature feature) {
		return this.value.equals(((Term)feature).getValue());
	}
	
	@Override
	public String toString() {
		return String.format("%s ; %.0f ; %f", this.value, this.absoluteFrequency, this.relativeFrequency);
	}
}
