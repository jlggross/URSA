package clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

/* -----------------------------------------------------------------------------------------------
 *  
 * Cliques algorithm:
 * 1. Select an object that is not part of any cluster and add it to a new cluster.
 * 2. Select another object and compare both.
 * 3. If the selected object is similar to all the other objects of the cluster, add it to the
 * cluster.
 * 4. While there are objects to be compared, go back to 2.
 * 5. While there are objects not added to any cluster, go back to 1. 
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class CliquesClusteringStrategy extends ClusteringStrategy {
	
	private double threshold;
	
	/**
	 * Definition: Cliques Constructor
	 * 
	 * @param threshold : indicates the minimum similarity that an
	 * object need to be part of a cluster.
	 */
	public CliquesClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}

	
	/**
	 * Definition: Cliques core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
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
