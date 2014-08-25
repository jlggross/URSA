package mystrategies.clustering;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public class MyClusteringStrategy extends ClusteringStrategy {
	
	private double threshold;
	
	/**
	 * Definition: My algorithm constructor
	 * 
	 * @param threshold : indicates the minimum similarity that an
	 * object need to be part of a cluster.
	 */
	public MyClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}
		
	
	/**
	 * Definition: My clustering algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		// TODO
		return dataClusters;
	}
}
