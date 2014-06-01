package br.ufrgs.inf.jlggross.clustering.strategy;

import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;

public abstract class ClusteringStrategy extends BaseStrategy {

	public abstract List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix);
	
}
