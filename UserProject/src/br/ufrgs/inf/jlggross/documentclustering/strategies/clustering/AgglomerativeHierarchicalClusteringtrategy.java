package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;

/* -----------------------------------------------------------------------------------------------
 *  
 * Agglomerative Hierarchical Clustering Algorithm: 
 * 
 * TODO
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class AgglomerativeHierarchicalClusteringtrategy {

	private double threshold; // Threshold added on each lvl
	private double thresholdLvl; // Total threshold

	/**
	 * Definition: Agglomerative Hierarchical Constructor
	 * 
	 * @param threshold : indicates the minimum similarity that an
	 * object need to be part of a cluster.
	 */
	public AgglomerativeHierarchicalClusteringtrategy(double threshold) {
		this.threshold = threshold;
		this.thresholdLvl = threshold;
	}
	

	/**
	 * Definition: Agglomerative Hierarchical Clustering core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		
		//TODO
		
		// 4. Build DataClusters
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		
		//TODO
		
		return dataClusters;
	}
	
}
