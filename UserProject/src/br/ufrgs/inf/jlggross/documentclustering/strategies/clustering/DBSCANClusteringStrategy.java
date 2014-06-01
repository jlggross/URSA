package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

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
 *   above will be in the same group. 
 * 
 * -----------------------------------------------------------------------------------------------   
 */

public class DBSCANClusteringStrategy extends ClusteringStrategy {
	private double epsilon; // must be from 0.0 to 1.0
	private int minObjs; 
	private int NOISE = -999;
	private int VISITED = 999;
	private int UNVISITED = 100;
	
	public DBSCANClusteringStrategy(double epsilon, int minObjs) {
		this.epsilon = epsilon;
		this.minObjs = minObjs;
	}
	
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


