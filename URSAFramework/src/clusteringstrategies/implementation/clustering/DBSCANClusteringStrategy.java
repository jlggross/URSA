package clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

/* -----------------------------------------------------------------------------------------------
 * 
 * Algorithm:
 * DBSCAN(D, eps, MinPts)
 * C = 0
 * for each unvisited point P in dataset D
 *    mark P as visited
 *    NeighborPts = regionQuery(P, eps)
 *    if sizeof(NeighborPts) < MinPts
 *       mark P as NOISE
 *    else
 *       C = next cluster
 *       expandCluster(P, NeighborPts, C, eps, MinPts)
 *         
 * expandCluster(P, NeighborPts, C, eps, MinPts)
 *    add P to cluster C
 *    for each point P' in NeighborPts 
 *       if P' is not visited
 *       mark P' as visited
 *       NeighborPts' = regionQuery(P', eps)
 *       if sizeof(NeighborPts') >= MinPts
 *          NeighborPts = NeighborPts joined with NeighborPts'
 *      if P' is not yet member of any cluster
 *         add P' to cluster C
 *         
 * regionQuery(P, eps)
 *    return all points within P's eps-neighborhood (including P)
 * 
 * -----------------------------------------------------------------------------------------------   
 *    
 * IMPORTANT INFORMATION
 * 
 *   1. epsilon must be understand as "The maximum difference in similarity between two objects".
 *   2. So, if epsilon is 0.20, then we are expecting that all the objects with similarity 0.8 or 
 *   above will be in the same cluster. 
 * 
 * -----------------------------------------------------------------------------------------------   
 */

public class DBSCANClusteringStrategy extends ClusteringStrategy {
	private double epsilon;
	private int minObjs; 
	private int NOISE = -999;
	private int VISITED = 999; 
	private int UNVISITED = 100;
	
	
	/**
	 * Definition: DBSCAN Constructor.
	 * 
	 * @param epsilon : epsilon is the maximum different in similarity between 
	 * two objects. So if epsilon if 0.15, then objects with similarity 0.85 or
	 * higher will be in the same cluster.
	 * @param minObjs : the minimum number of data objects in each cluster. 
	 */
	public DBSCANClusteringStrategy(double epsilon, int minObjs) {
		
		if (epsilon < 0.0 || epsilon > 1.0)
			throw new RuntimeException("DBSCAN: epsilon must be between 0.0 and 1.0.");
		
		if (minObjs < 2)
			throw new RuntimeException("DBSCAN: minObjs must be 2 or higher. Cluster with"
					+ "just one data object are not allowed");
		
		this.epsilon = epsilon;
		this.minObjs = minObjs;
	}
	
	
	/**
	 * Definition: DBCAN core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		int[] dataObjectsStatus = new int[dataObjects.size()];
		
		int clusterCount = 0;
		
		// Initialize data objects visited status
		int size = dataObjects.size();
		for (int i = 0; i < size; i++) {
			dataObjectsStatus[i] = UNVISITED;
		}
					
		// Check all the unvisited data objects
		for (int i = 0; i < size; i++) {
			if (dataObjectsStatus[i] == UNVISITED) {
				dataObjectsStatus[i] = VISITED;
				List<Integer> neighborObjs = new ArrayList<Integer>(
						regionQuery(i, 1.0 - this.epsilon, dataObjects, similarityMatrix));
				
				// Check if the new cluster has the minimum number of elements
				if (neighborObjs.size() < this.minObjs) {
					dataObjectsStatus[i] = NOISE;
					continue;
				}
				else { // Creates a new cluster
					DataCluster newCluster = createCluster(dataObjects, 
							similarityMatrix, neighborObjs, ++clusterCount, dataObjectsStatus);
					dataClusters.add(newCluster);
				}
			}
		}
		
		return dataClusters;
	}
	
	
	/**
	 * Get the indexes of the epsilon-neighborhood of a given object (indexOfObjsCluterStarter). 
	 */
	private List<Integer> regionQuery(int indexOfObjsClusterStarter, double similarityEpsilon, 
			List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		
		List<Integer> clusterObjIndexes = new ArrayList<Integer>();
		clusterObjIndexes.add(indexOfObjsClusterStarter);
		
		// Calculate epsilon-neighborhood
		int size = dataObjects.size();
		int x;
		for (int i = 0; i < size; i++) {
			
			// Check if still there is a data object to visit 
			if(i < clusterObjIndexes.size()) {
				x = clusterObjIndexes.get(i);
				for (int y = 0; y < size; y++) {
					if (similarityMatrix.get(x,y) >= similarityEpsilon) {
						if (!clusterObjIndexes.contains(y)) {
							clusterObjIndexes.add(y);
						}
					}
				}
			}
			
			// All the objects inside the cluster have already been visited
			else break; 
		}
		
		return clusterObjIndexes;
	}
	
	
	/**
	 * Given a data object that has a neighborhood of similar data objects, creates a new cluster. 
	 */
	private DataCluster createCluster(List<DataObject> dataObjects, Matrix2D similarityMatrix, 
			List<Integer> neighborObjs, int clusterNumber, int[] dataObjectsStatus) {
		
		// Creates the cluster, labeling it
		DataCluster dataCluster = new DataCluster("Cluster" + clusterNumber);
		
		// Populate cluster
		int size = neighborObjs.size();
		for (int i = 0; i < size; i++) {
			// Get the index of each data object (neighborObjs) and then the data object itself (dataObjects)
			dataCluster.addDataObject(dataObjects.get(neighborObjs.get(i)));
			dataObjectsStatus[neighborObjs.get(i)] = VISITED;
		}
		
		return dataCluster;
	}
}


