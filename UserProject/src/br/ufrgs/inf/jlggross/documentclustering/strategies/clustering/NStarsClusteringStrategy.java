package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

public class NStarsClusteringStrategy extends ClusteringStrategy {
	private double threshold;
	
	public NStarsClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}
	
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		List<DataObject> allocatedObjects = new ArrayList<DataObject>();
		
		// While there is still not allocated objects in the collection.
		int totalDocs = dataObjects.size();
		while (allocatedObjects.size() < totalDocs) {
			// Gets an unnallocated one and start a new star.
			DataCluster star = new DataCluster();
			int currentObjectIndex = -1;

			// Selects an object that is not allocated yet and put it as the center of the star.
			for (int i = 0; i < totalDocs; i++) {
				DataObject object = dataObjects.get(i);
				if (!allocatedObjects.contains(object)) {
					allocatedObjects.add(object);
					star.addDataObject(object);
					currentObjectIndex = i;
					break;
				}
			}
			
			this.setProgress((double)allocatedObjects.size()/totalDocs);
			
			// Look for all similar objects and put it in the same cluster.
			for (int i = 0; i < totalDocs; i++) {
				DataObject anotherObject = dataObjects.get(i);
				if (!allocatedObjects.contains(anotherObject) && (currentObjectIndex != i)) {
					if (similarityMatrix.get(currentObjectIndex, i) >= this.threshold) {
						allocatedObjects.add(anotherObject);
						star.addDataObject(anotherObject);
						this.setProgress((double)allocatedObjects.size()/totalDocs);
					}
				}
			}
			
			dataClusters.add(star);
		}
		
		return dataClusters;
	}

}
