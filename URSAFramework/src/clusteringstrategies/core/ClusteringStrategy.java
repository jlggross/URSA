package clusteringstrategies.core;

import java.util.List;

import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public abstract class ClusteringStrategy extends BaseStrategy {

	public abstract List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix);
	
}
