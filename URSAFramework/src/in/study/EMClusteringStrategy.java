package in.study;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

/* -----------------------------------------------------------------------------------------------
 *  
 * Algorithm:
 *
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class EMClusteringStrategy extends ClusteringStrategy {	
	
	/**
	 */
	public EMClusteringStrategy() {
	}
	
	
	/**
	 * Definition: EM core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		
		return dataClusters;
	}
	
}