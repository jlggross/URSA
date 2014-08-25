package clusteringstrategies.core;

import java.util.List;

import datastructures.core.DataObject;

public abstract class FeatureSelectionStrategy extends BaseStrategy {
	
	public abstract List<DataObject> executeFeatureSelection(List<DataObject> dataObjects);
	
}
