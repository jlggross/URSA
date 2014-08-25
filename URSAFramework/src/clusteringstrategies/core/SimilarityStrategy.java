package clusteringstrategies.core;

import java.util.List;

import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public abstract class SimilarityStrategy extends BaseStrategy {

	public abstract Matrix2D executeSimilarity(List<DataObject> dataObjects);
	
}
