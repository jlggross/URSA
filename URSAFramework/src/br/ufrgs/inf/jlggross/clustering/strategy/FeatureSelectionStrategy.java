package br.ufrgs.inf.jlggross.clustering.strategy;

import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataObject;

public abstract class FeatureSelectionStrategy extends BaseStrategy {
	
	public abstract List<DataObject> executeFeatureSelection(List<DataObject> dataObjects);
	
}
