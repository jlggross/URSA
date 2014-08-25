package in.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

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
	
	double fuzziness;
	int numClusters;
	int iterations;
	
	/**
	 * Definition: Fuzzy C-Means Constructor.
	 * 
	 * @param fuzziness : determines the level of cluster fuzziness. A large m 
	 * results in smaller memberships uij and hence, fuzzier clusters. Must be
	 * a value equal or higher than 1. [1, +infinite)
	 * @param numClusters : the number of clusters.
	 * @param iterations : the number of iterations for the clustering algorithm. 
	 * */
	public FuzzyCMeansClusteringStrategy(double fuzziness, int numClusters, int iterations) {
		this.fuzziness = fuzziness;
		this.numClusters = numClusters;
		this.iterations = iterations;
	}
	
	
	/**
	 * Definition: Fuzzy C-Means core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		
		// 1. Create membership matriz
		double[][] membership = new double[dataObjects.size()][this.numClusters];
		double[][] centroids = new double[dataObjects.size()][this.numClusters];
		
		// 1.1 Initialize membership matrix
		Random r = new Random();
		int dataSize = dataObjects.size();
		for (int i = 0; i < this.numClusters; i++) {
			for (int j = 0; j < dataSize; j++) {
				membership[j][i] = r.nextDouble();
			}
		}
		
		int it = 0;
		do {
			// 2. Calculate centroids
			
			// 3. update membership
			
			// 4. Test convergence
			
			it++;
		} while (it < this.iterations);
		
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		// build clusters
		
		return dataClusters;
	}

}
