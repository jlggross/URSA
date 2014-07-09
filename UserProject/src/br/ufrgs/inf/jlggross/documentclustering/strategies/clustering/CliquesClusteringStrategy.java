package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

public class CliquesClusteringStrategy extends ClusteringStrategy {
	private double threshold;
	
	public CliquesClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		List<DataObject> allocatedObjects = new ArrayList<DataObject>();
		
		// While there is still not allocated objects in the collection.
		int totalDocs = dataObjects.size();
		while (allocatedObjects.size() < totalDocs) {
			DataCluster cluster = new DataCluster();
			int currentObjectIndex = -1;
			
			// Selects an object that is not allocated yet and put it into a new DataCluster.
			for (int i = 0; i < totalDocs; i++) {
				DataObject object = dataObjects.get(i);
				if (!allocatedObjects.contains(object)) {
					allocatedObjects.add(object);
					cluster.addDataObject(object);
					currentObjectIndex = i;
					break;
				}
			}
			
			this.setProgress((double)allocatedObjects.size()/totalDocs);
			
			// Put all objects that are similar to all other objects in the group together.
			for (int i = 0; i < totalDocs; i++) {
				DataObject anotherObject = dataObjects.get(i);
				if (!allocatedObjects.contains(anotherObject) && (currentObjectIndex != i)) {
					boolean isSimilar = true;
					for (int j = 0; j < cluster.getDataObjects().size(); j++) {
						if (similarityMatrix.get(currentObjectIndex, i) < this.threshold) {
							isSimilar = false;
							break;
						}
					}
					if (isSimilar) {
						allocatedObjects.add(anotherObject);
						cluster.addDataObject(anotherObject);
						this.setProgress((double)allocatedObjects.size()/totalDocs);
					}
				}
			}
			
			dataClusters.add(cluster);
		}
		
		return dataClusters;
	}

}
