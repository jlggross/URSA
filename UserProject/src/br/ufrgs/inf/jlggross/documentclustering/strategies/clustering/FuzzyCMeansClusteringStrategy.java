package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

/* -----------------------------------------------------------------------------------------------
 *  
 * Fuzzy C-Means Algorithm:
 * 1. Create and initialize membership matrix. Each uij of the matrix is the membership between the
 * ith dataObject with the jth centroid. The matrix is n x m, where n is the number of objects and
 * m the number of clusters.
 * 	1.1. The initialization can be of random values from [0, 1] 
 * 2. Recalculate the centroids
 * 3. Update membership
 * 4. Test convergence. If the algorithm has not converged, then repeat 2. and 3.
 * 
 * 
 * References:
 * 1. http://home.deib.polimi.it/matteucc/Clustering/tutorial_html/cmeans.html
 * 2. http://en.wikipedia.org/wiki/Fuzzy_clustering
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class FuzzyCMeansClusteringStrategy extends ClusteringStrategy {
	
	/**
	 * */
	public FuzzyCMeansClusteringStrategy() {
		
	}
	
	
	/**
	 * */
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();		
		return dataClusters;
	}

}
