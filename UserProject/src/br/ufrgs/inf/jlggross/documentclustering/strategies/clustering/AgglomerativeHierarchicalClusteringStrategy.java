package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

/* -----------------------------------------------------------------------------------------------
 *  
 * Agglomerative Hierarchical Clustering Algorithm: 
 * 
 * Given a similarity matrix and a set of data objects, do:
 * 1. Initialize each data object as being a cluster.
 * 2. Find the nearest two clusters xi and xj, according to global threshold.
 * 3. Merge xi and xj into a cluster.
 * 4. Keep doing 2. and 3. until no more clusters can be merged.
 * 5. Update global threshold adding a constant value to it.
 * 6. Keep doing 2., 3. and 5. until the maximum global threshold is reached.  
 * 
 * Reference: http://cgm.cs.mcgill.ca/~soss/cs644/projects/siourbas/sect5.html
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class AgglomerativeHierarchicalClusteringStrategy extends ClusteringStrategy {

	private double threshold; // Threshold of the level
	private double thresholdLvl; // Threshold decreased in each level
	private double minThreshold; // The minimum reachable threshold
	private int clusteringMethod; // Clustering Method
	
	/**
	 * Definition: Agglomerative Hierarchical Constructor
	 * 
	 * @param thresholdLvl : the amount of similarity decreased in each iteration for the
	 * global threshold.  
	 * @param minThreshold : minimum possible threshold. 
	 * @param clusteringMethod : a clustering method is chosen for the agglomerative hierarchical 
	 * clustering algorithm.
	 */
	public AgglomerativeHierarchicalClusteringStrategy(double thresholdLvl, double minThreshold, 
			int clusteringMethod) {
		
		double result = 1.0 - 2*thresholdLvl;
		if (result < minThreshold)
			throw new RuntimeException("1.0 - 2*'thresholdLvl' must be bigger than 'minThreshold'.");
		
		if (thresholdLvl > 1.0 || minThreshold > 1.0)
			throw new RuntimeException("Both 'thresholdLvl' and 'minThreshold' must be below 1.0.");
		
		this.threshold = 1.0;
		this.thresholdLvl = thresholdLvl;
		this.minThreshold = minThreshold;
		
		this.clusteringMethod = clusteringMethod;
	}
	

	/**
	 * Definition: Agglomerative Hierarchical Clustering core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		
		// Variables
		int dataSize = dataObjects.size();
		int[][] clusters = new int[dataSize][dataSize];
		
		// Initialize list of clustered objects and clusters
		for (int i = 0; i < dataSize; i++) {
			for (int j = 0; j < dataSize; j++) {
				clusters[i][j] = -1;
			}
			clusters[i][i] = 1; // Each object belongs to one unique cluster
		}
		
		switch(this.clusteringMethod) {
			case 1: clusters = SingleLinkageClusteringMethod(dataObjects, similarityMatrix, clusters);
					break;
			default: throw new RuntimeException("Agglomerative Hierarchical Clustering: " +
					"Clustering Method does not exist");
		}
		
		// Build DataClusters
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		int numClusters = 1;
		
		for (int i = 0; i < dataSize; i++) {
			DataCluster d = new DataCluster("Cluster " + numClusters);
			
			int newCluster = -1;
			for (int j = 0; j < dataSize; j++) {
				if (clusters[i][j] == 1) {
					d.addDataObject(dataObjects.get(j));
					newCluster = 1;
				}
			}
			
			if (newCluster == 1) {
				dataClusters.add(d);
				numClusters++;
			}
		}
		
		return dataClusters;
	}
	
	
	/**
	 * Definition: Single Linkage Clustering Method. It s based on the simple minimum euclidean distance
	 * between two data objects. In our case the minimum euclidean distance between two data objects is the
	 * highest similarity value between two data objects. If they are very similar (high similarity value),
	 * they are very close (low distance value). 
	 * 
	 * Single Linkage Clustering is also known as Nearest Neighbor CLustering.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.
	 * @param clusters : matrix that stores the clusters created during the algorithm.  
	 */
	private int[][] SingleLinkageClusteringMethod(List<DataObject> dataObjects, Matrix2D similarityMatrix,
			int[][] clusters) {

		// Variables
		int dataSize = dataObjects.size();
		
		do {
			
			// 5. Update global threshold adding a constant value to it.
			this.threshold -= this.thresholdLvl;
			if (this.threshold < this.minThreshold)
				this.threshold = this.minThreshold;
			
			// 2. Find the nearest two clusters xi and xj, according to global threshold.
			for (int i = 0; i < dataSize; i++) {
				double sim = -1;
				for (int j = i+1; j < dataSize; j++) {
					sim = similarityMatrix.get(i, j);
					
					if (sim >= this.threshold) {
						similarityMatrix.set(i, j, -1.0); // will not be analysed again
						
						// 3. Merge xi and xj into a cluster.
						int clusteri = -1, clusterj = -1;
						
						// Find the cluster to which the object i belongs 
						for (int ii = 0; ii < dataSize; ii++) {
							if (clusters[ii][i] == 1) {
								clusteri = ii;
							}
							if (clusters[ii][j] == 1) {
								clusterj = ii;
							}
						}

						if (clusteri == clusterj)
							continue;
						
						// Merge
						for (int ii = 0; ii < dataSize; ii++) {
							if (clusters[clusterj][ii] == 1) {
								clusters[clusteri][ii] = 1;
								clusters[clusterj][ii] = -1;
							}
						}
						
					}
				}
			}
			
		} while (this.threshold > this.minThreshold);
		
		return clusters;
	}	
}
