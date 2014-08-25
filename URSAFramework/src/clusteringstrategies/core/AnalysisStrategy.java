package clusteringstrategies.core;

import java.util.List;

import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public abstract class AnalysisStrategy extends BaseStrategy {

	public double executeAnalysis(List<DataObject> objects, List<DataCluster> dataClusters) {
		return 0;
	}
	
	public double executeAnalysis(List<DataObject> objects, List<DataCluster> dataClusters, Matrix2D similarityMatrix) {
		return 0;
	}
}
