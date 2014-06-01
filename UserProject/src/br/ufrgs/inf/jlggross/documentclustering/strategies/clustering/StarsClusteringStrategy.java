package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

public class StarsClusteringStrategy extends ClusteringStrategy {
	private double threshold;
	
	public StarsClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		List<DataObject> allocatedObjects = new ArrayList<DataObject>();
		
		// For each object...
		int totalDocs = dataObjects.size();
		for (int i = 0; i < totalDocs; i++) {
			DataObject currentObject = dataObjects.get(i);
			
			if (!allocatedObjects.contains(currentObject)) {
				// The current object is not allocated yet, so starts a new cluster.
				DataCluster star = new DataCluster();
				star.addDataObject(currentObject);
				this.setProgress((double)allocatedObjects.size()/totalDocs);
				
				// For each other object, checks the similarity between them.
				for (int j = 0; j < totalDocs; j++) {
					if (i != j) {
						DataObject anotherObject = dataObjects.get(j);
						if (!allocatedObjects.contains(anotherObject)) {
							// If similarity is above the threshold, adds the object to the cluster.
							if (similarityMatrix.get(i, j) >= this.threshold) {
								allocatedObjects.add(anotherObject);
								star.addDataObject(anotherObject);
								this.setProgress((double)allocatedObjects.size()/totalDocs);
							}
						}
					}
				}
				dataClusters.add(star);
			}
		}
		
		// Remove clusters of one document.
		/*List<DataCluster> realClusters = new ArrayList<DataCluster>();
		for (DataCluster cluster : dataClusters) {
			if (cluster.getDataObjects().size() > 1) {
				realClusters.add(cluster);
			}
		}*/
		
		return dataClusters;
	}
}
