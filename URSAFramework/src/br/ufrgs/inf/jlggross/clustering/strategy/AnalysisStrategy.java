package br.ufrgs.inf.jlggross.clustering.strategy;

import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;

public abstract class AnalysisStrategy extends BaseStrategy {

	public double executeAnalysis(List<DataObject> objects, List<DataCluster> dataClusters) {
		return 0;
	}
	
	public double executeAnalysis(List<DataObject> objects, List<DataCluster> dataClusters, Matrix2D similarityMatrix) {
		return 0;
	}
}
