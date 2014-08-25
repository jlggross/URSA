package clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clusteringstrategies.core.ClusteringStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.core.Pair;

/* -----------------------------------------------------------------------------------------------
 *  
 * Algorithm:
 * 
 * 1) Choose k centroids/clusters.
 * 2) For each non-centroid element group it with a centroid that has the highest similatiry with it.
 * 3) Choose a new centroid for each one of the k groups.
 * 	3.1) We sum all the similarities of each pair of objects, take the mean value and the new centroid
 *  is the one whose mean similarity with the other objects is the closest to the overall mean similarity. 
 * 4) Repeat 2) and 3) for a number i of iterations. (all non-centroid elements have no groups at this
 * point)
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class KmeansClusteringStrategy extends ClusteringStrategy {
	private int numCentroids;
	private int numObjects;
	private int iterations;	
	
	/**
	 * Definition: K-Means Constructor.
	 * 
	 * @param k : number of groups.
	 * @param iterations : number of iterations. Run the algorithm for the n iterations or until
	 * convergence. Must be equal or below 1000. If the value is -1, then run the iterations until 
	 * convergence, with no concern on the number of iterations.
	 * @param centroidStrategy : select the strategy to choose the new centroid at the end of each
	 * iteration.
	 */
	public KmeansClusteringStrategy(int k, int iterations) {
		this.numCentroids = k;
		this.iterations = iterations;
	}
	
	
	/**
	 * Definition: K-Means core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		this.numObjects = dataObjects.size();
		
		if (this.numObjects < this.numCentroids) {
			throw new RuntimeException("K should be equals or smaller than the number of objects.");
		}

		// Choose centroids
		// centroidsIndex : stores the position of each centroid in 'dataObjects' as file OBJ0, OBJ1, ...
		List<Integer> centroidsIndex = new ArrayList<Integer>(); 
		centroidsIndex = this.chooseCentroidsSPSS(this.numCentroids, centroidsIndex, similarityMatrix);
		
		// Create matrix for the centroids	
		double[][] centroidsMatrix = new double[this.numCentroids][this.numObjects];
		for (int i = 0; i < this.numCentroids; i++) {
			int centroidIndex = centroidsIndex.get(i);
			
			// Copy similarities
			for (int j = 0; j < this.numObjects; j++) {
				if (centroidIndex == j)
					centroidsMatrix[i][j] = 0;
				else
					centroidsMatrix[i][j] = similarityMatrix.get(centroidIndex, j);
			}
			
			// For the zero values calculate a mean, based on the similarities between the obj with 
			// zero similarity and the others objs.
			double sum = 0;
			for (int j = 0; j < this.numObjects; j++) {
				sum += centroidsMatrix[i][j];
			}
			sum = sum / this.numObjects;
			centroidsMatrix[i][centroidIndex] = sum;
		}
		
		
		// print
		for (int i = 0; i < this.numCentroids; i++) {
			System.out.printf("Centroid %d: ", i);
			for (int j = 0; j < this.numObjects; j++) {
				System.out.printf("%6.2f", centroidsMatrix[i][j]);
			}
			System.out.printf("\n");
		}
		
		// Check @param iterations 
		int numIterations;
		if (this.iterations == -1)
			numIterations = Integer.MAX_VALUE;
		else 
			numIterations = this.iterations;
		
		// Iterate K-means until convergence
		for (int it = 0; it < numIterations; it++) {
			
			// Update similarities
			for (int i = 0; i < this.numCentroids; i++) {
				// Get mean similarity in the cluster
				double mean = getMeanSimilarity(centroidsMatrix[i]);
				double diff = getDiffMeanSimilarity(centroidsMatrix[i], mean);
				
				for (int j = 0; j < this.numObjects; j++) {
					if( (centroidsMatrix[i][j] - mean) > 0 )
						centroidsMatrix[i][j] = centroidsMatrix[i][j] - centroidsMatrix[i][j] * diff;
					else
						centroidsMatrix[i][j] = centroidsMatrix[i][j] + centroidsMatrix[i][j] * diff;
				}
			}	
			
			// Test convergence
			// TODO
		}
		
		// Construct the clusters
		return buildClusters(centroidsMatrix, dataObjects, centroidsIndex);
	}

	
	/**
	 * Definition: Calculates the difference between the similarities in @param centroidRow,
	 * then add these differences and calculate the mean of the differences.
	 * 
	 * @param centroidRow : stores the similarities of each object with the centroid it
	 * represents.
	 * @param mean : the mean of the similarities in @param centroidRow  
	 */
	private double getDiffMeanSimilarity(double[] centroidRow, double mean) {
		double diff = 0.0;
		for (int i = 0; i < this.numObjects; i++) {
			diff += Math.abs(centroidRow[i] - mean);
		}
		
		return (diff / this.numObjects);
	}
	
	
	/**
	 * Definition: Calculates the mean of similarities of the objects with a 
	 * given centroid. 
	 * 
	 * @param centroidRow : stores the similarities of each object with the centroid it
	 * represents.
	 */
	private double getMeanSimilarity(double[] centroidRow) {
		double mean = 0.0;
		for (int i = 0; i < this.numObjects; i++) {
			mean += centroidRow[i];
		}
		
		return (mean / this.numObjects);
	}
	
	
	/**
	 * Definition: Build the list of clusters based in the results of the
	 * K-means algorithm. This list is required for other steps in the
	 * application.  
	 * 
	 * @param centroidsMatrix : each line of the matrix corresponds to one centroid.
	 * Each column corresponds to an object. The value stored in (line, column)
	 * corresponds to the similarity between the object and the given centroid.
	 * @param dataObjects : list of data objects.
	 * @param centroidsIndex :
	 */
	private List<DataCluster> buildClusters(double[][] centroidsMatrix, List<DataObject> dataObjects, List<Integer> centroidsIndex) {
		// Create clusters
		DataCluster[] clusters = new DataCluster[this.numCentroids];
		for (int i = 0; i < this.numCentroids; i++) {
			clusters[i] = new DataCluster();
		}
		
		// Populate clusters
		for (int i = 0; i < this.numObjects; i++) {
			double current = -1;
			double max = -1;
			int cluster = -1;
			for (int j = 0; j < this.numCentroids; j++) {
				current = centroidsMatrix[j][i];
				if (current > max) {
					max = current;
					cluster = j;
				}
			}
			
			DataObject d = dataObjects.get(i);
			clusters[cluster].addDataObject(d);
		}
		
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		for (int i = 0; i < this.numCentroids; i++) {
			dataClusters.add(clusters[i]);
		}
		
		return dataClusters;
	}
	
	
	
	/**
	 * Definition: Choose centroids using Single Pass Seed Selection (SPSS) Algorithm.
	 * 
	 * SPSS Algorithm: 
	 * 1. Calculate similarity matrix
	 * 2. Find sumv, which is a vector of distances where the i position
	 * correspond to the sum of the similarities of the ith object with all
	 * the other objects.
	 * 3. Find the max value of sumv.
	 * 4. Add first centroid (the one with the maximum value in sumv) to C.   
	 * 5. For each point xi, set D(xi) to be the highest similarity of xi
	 * to a centroid in C.
	 * 6. Find y as the sum of similarities of the first n/k highest similarities
	 * in D.
	 * 7-8. Described in the code.
	 * 
	 * @param k : number of centroids.
	 * @param centroidsIndex : the indexes of the centroids.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.
	 */	
	private List<Integer> chooseCentroidsSPSS(int k, List<Integer> centroidsIndex, Matrix2D similarityMatrix) {
		// 2. Create list of distances
		List<Double> similaritySum = new ArrayList<Double>();
		for (int i = 0; i < this.numObjects; i++) {
			double sum = 0;
			for (int j = 0; j < this.numObjects; j++) {
				if (i == j) 
					continue;
				sum += similarityMatrix.get(i,j); 
			}
			similaritySum.add(sum);
		}
		
		// 3. Find max value in similaritySum
		double maxSimSum = Double.MIN_VALUE;
		int maxSimSumIndex = -1;
		for (int i = 0; i < this.numObjects; i++) {
			double sum = similaritySum.get(i);
			if (sum > maxSimSum) {
				maxSimSum = sum;
				maxSimSumIndex = i;
			}
		}
		
		// 4. Add first centroid
		centroidsIndex.add(maxSimSumIndex);
		
		do {
			// 5. Set distances list
			ArrayList<Pair> similarityDistances = new ArrayList<Pair>();
		
			for (int i = 0; i < this.numObjects; i++) {
				// Exclude centroids
				if (centroidsIndex.contains(i))
					continue;
				
				// Populate list
				double maxSim = Double.MIN_VALUE;
				for (int j = 0; j < centroidsIndex.size(); j++) {
					int centroidIndex = centroidsIndex.get(j);
					if (similarityMatrix.get(i, centroidIndex) > maxSim) {
						maxSim = similarityMatrix.get(i, centroidIndex);
					}
				}
				similarityDistances.add(new Pair(maxSim, i));
			}
			
			// 6. Find y
	        Collections.sort(similarityDistances, new Pair.PairSimilarityComparator());
	        
			double y = 0;
			//for (int i = 0; i < this.numObjects/k; i++)
			for (int i = (this.numObjects/k - 1); i >=0 ; i--)
				y += similarityDistances.get(i).getSimilarity();
			
			// 7-8. Find next centroid
			for (int i = 2; i < similarityDistances.size(); i++) {
				double leftSum = 0, rightSum = 0;
				for (int j = 0; j < (i+1); j++) {
					if (j < i)
						leftSum += Math.pow(similarityDistances.get(j).getSimilarity(), 2);
					if (j == i) {
						rightSum = leftSum;
						leftSum += Math.pow(similarityDistances.get(j).getSimilarity(), 2);
					}
				}
				if (leftSum >= y && y > rightSum) {
					centroidsIndex.add(
							similarityDistances.get(i).getIndex());
					break;
				}
				if (i == (similarityDistances.size() - 1)) {
					throw new RuntimeException("Kmeans class, function chooseCentroidsSPSS : No centroid was selected");
				}
			}
		} while (centroidsIndex.size() < k);
		
		return centroidsIndex;
	}
	
	
}
