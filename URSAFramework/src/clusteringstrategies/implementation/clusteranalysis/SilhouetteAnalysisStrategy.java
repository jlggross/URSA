package clusteringstrategies.implementation.clusteranalysis;

import java.util.List;

import clusteringstrategies.core.AnalysisStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public class SilhouetteAnalysisStrategy extends AnalysisStrategy {

	@Override
	public double executeAnalysis(List<DataObject> dataObjects, List<DataCluster> dataClusters, Matrix2D similarityMatrix) {
		double silhouette = 0.0;
		
		for (DataCluster cluster : dataClusters) {
			double clusterAi = 0.0;
			for (DataObject dataObject1 : cluster.getDataObjects()) {
				// Calculates average similarity within cluster.
				double ai = 0.0;
				for (DataObject dataObject2 : cluster.getDataObjects()) {
					int index1 = dataObjects.indexOf(dataObject1);
					int index2 = dataObjects.indexOf(dataObject2);
					if (index1 != index2) {
						ai += similarityMatrix.get(index1, index2);
					}
				}
				ai /= (cluster.getDataObjects().size() - 1);
				
				// Calculates average similarities with other clusters and pick the closest one.
				double bi = 0.0;
				for (DataCluster cluster2 : dataClusters) {
					if (cluster != cluster2) {
						double biAux = 0.0;
						for (DataObject dataObject2 : cluster2.getDataObjects()) {
							int index1 = dataObjects.indexOf(dataObject1);
							int index2 = dataObjects.indexOf(dataObject2);
							biAux += similarityMatrix.get(index1, index2);
						}
						biAux /= cluster2.getDataObjects().size();
						if (biAux > bi) {
							bi = biAux;
						}
					}
				}
				
				double si = 0.0;
				if (ai > bi) {
					si = 1.0 - (bi/ai);
				} else if (ai < bi) {
					si = (ai/bi) - 1.0;
				}
				clusterAi += si;
			}
			silhouette += (clusterAi / cluster.getDataObjects().size());
		}
		silhouette /= dataClusters.size();
		
		return silhouette;
	}

}
