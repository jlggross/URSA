package clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

/* -----------------------------------------------------------------------------------------------
 *  
 * Stars algorithm:
 * 1. Select an object that is not part of any cluster and put it as the center of a new cluster.
 * 2. Put all the objects similar to it in the new cluster. 
 * 3. If there are objects still not in any cluster, repeat steps 1 and 2.
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class StarsClusteringStrategy extends ClusteringStrategy {
	
	private double threshold;
	
	/**
	 * Definition: Stars Constructor
	 * 
	 * @param threshold : indicates the minimum similarity that an
	 * object need to be part of a cluster.
	 */
	public StarsClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}
	
	
	/**
	 * Definition: Stars core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
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
		
		return dataClusters;
	}
}
