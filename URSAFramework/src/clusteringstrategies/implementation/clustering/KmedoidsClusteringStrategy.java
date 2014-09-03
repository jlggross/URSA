package clusteringstrategies.implementation.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

public class KmedoidsClusteringStrategy extends ClusteringStrategy {
	
	private int centroidsNum;
	private int iterations;
	private int centroidStrategy;
	private int firstCentroids;
	private double MIN_VALUE = -9999.00;
	private int INFINITE = 9999;
	
	
	/**
	 * Definition: K-Medoids Constructor.
	 * 
	 * @param k : number of groups.
	 * @param iterations : number of iterations. Run the algorithm for the n iterations or until
	 * convergence. Must be equal or below 1000. If the value is -1, then run the iterations until 
	 * convergence, with no concern on the number of iterations.
	 * @param centroidStrategy : select the strategy to choose the new centroid at the end of each
	 * iteration.
	 * @param firstCentroids : must be 0, 1 or 2. Each number corresponds to a different strategy to
	 * find the first k centroids.
	 */
	public KmedoidsClusteringStrategy(int k, int iterations, int centroidStrategy, int firstCentroids) {
		this.centroidsNum = k;
		this.iterations = iterations;
		
		if (centroidStrategy != 1 && centroidStrategy != 2)
			throw new RuntimeException("K-means: centroidStrategy must be 1 or 2.");
		
		this.centroidStrategy = centroidStrategy;
		this.firstCentroids = firstCentroids;
	}
	
	
	/**
	 * Definition: K-Medoids core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.  
	 */
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		int totalDocs = dataObjects.size();
		
		if (totalDocs < this.centroidsNum) {
			throw new RuntimeException("K should be equals or smaller than the number of objects.");
		}
		
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		List<Integer> centroidsIndex = new ArrayList<Integer>(); // stores the position of each centroid in 'dataObjects' as file OBJ0, OBJ1, ...
		
		// Choose firt k centroids
		switch(this.firstCentroids) {
			case 0: // Uses SSPS algorithm - problems with y
				centroidsIndex = this.chooseCentroidsSPSS(this.centroidsNum, centroidsIndex, similarityMatrix);
				break;
			case 1: // Sequential centroid selection
				for (int i = 0; i < this.centroidsNum; i++)
					centroidsIndex.add(i);
				break;
			case 2: // Random centroid selection
				Random rand = new Random(); 
				do {
					int a = Math.abs(rand.nextInt() % dataObjects.size());
					if (!centroidsIndex.contains(a)) {
						centroidsIndex.add(a);
						System.out.println("index : " + a);
					}
				} while (centroidsIndex.size() < this.centroidsNum);
				break;
			default:
				throw new RuntimeException("Kmedoids : choose option 0, 1 or 2.");
		}
		/*
		
		*/
			
		for (int i = 0; i < centroidsIndex.size(); i++) {
			// Add new cluster
			DataCluster cluster = new DataCluster("Cluster" + i);
			cluster.addDataObject(dataObjects.get(centroidsIndex.get(i)));
			//cluster.addDataObject(dataObjects.get(i));
			dataClusters.add(cluster);
		}
		
		// Check @param iterations 
		int numIterations;
		if (this.iterations == -1)
			numIterations = INFINITE;
		else 
			numIterations = this.iterations;
		
		// Testing variable
		List<Integer> oldCentroids = null;
		
		// K-means core execution
		for (int it = 0; it < numIterations; it++) {
			
			// Cluster each dataObject to the nearest centroids.
			for (int i = 0; i < dataObjects.size(); i++) {
				DataObject d = dataObjects.get(i);
				
				// Tests if the dataObject is a centroid 
				if (centroidsIndex.contains(i))
					continue;
				else {
					int currentCentroidIndex = -1; 
					double currentSimilarity = MIN_VALUE;
					
					// Search the highest similarity between d and all the centroids
					// There are 'centroidsNum' centroids
					for (int j = 0; j < this.centroidsNum; j++) {
						int centroidIndex = centroidsIndex.get(j);  
						double newSimilarity = similarityMatrix.get(i, centroidIndex);
						if (newSimilarity > currentSimilarity) {
							currentSimilarity = newSimilarity;
							currentCentroidIndex = centroidIndex;
						}
					}
					
					// Get cluster index in list 'clusters' and select the cluster
					int clusterListIndex = centroidsIndex.indexOf(currentCentroidIndex);
					DataCluster currentDataCluster = dataClusters.get(clusterListIndex);
					
					// Add the dataObject d to the corresponding cluster
					currentDataCluster.addDataObject(d);
					
					// Remove cluster and centroidIndex from their corresponding lists and add again both,
					// so they have the same index, each one in its list
					dataClusters.remove(clusterListIndex);
					centroidsIndex.remove(clusterListIndex);
					dataClusters.add(currentDataCluster);
					centroidsIndex.add(currentCentroidIndex);
				}
			} 
			
			// Check if the last iteration has been reached
			if (it == (this.iterations - 1))
				break; 
			
			// Check if the centroids are still the same. If yes, then stop, because 
			// no changes occurred in two iterations.
			boolean stop = true;
			if (it == 0) { // First iteration
				stop = false;
			}
			else { // From the second iteration and on
				for (int c : centroidsIndex) {
					if (oldCentroids.contains(c))
						continue;
					else {
						stop = false;
						break;
					}
				}
			}
			if (stop)
				break;
			
			// Calculate new centroids. So first, clear the current centroids
			List<DataCluster> oldClusters = new ArrayList<DataCluster>(dataClusters);
			oldCentroids = new ArrayList<Integer>(centroidsIndex);
			dataClusters.clear();
			centroidsIndex.clear();
			
			for (int i = 0; i < this.centroidsNum; i++) {
				// Get a cluster and its list of data objects
				DataCluster currentDataCluster = oldClusters.get(i);
				List<DataObject> currentDataClusterContent = currentDataCluster.getDataObjects();
				
				// List to store the index of each dataObject from the cluster
				List<Integer> dataClusterContentIndexes = new ArrayList<Integer>();
				
				// Extract the indexes from the dataObjects of the cluster
				for (int j = 0; j < currentDataClusterContent.size(); j++) {
					DataObject currentDataObject = currentDataClusterContent.get(j);
					dataClusterContentIndexes.add(dataObjects.indexOf(currentDataObject));
				}
								
				// New centroid is selected
				int currentIndexCentroid = 0;
				if (this.centroidStrategy == 1)
					currentIndexCentroid = this.getNewCentroidStrategy1(dataClusterContentIndexes, similarityMatrix);
				else if (this.centroidStrategy == 2)
					currentIndexCentroid = this.getNewCentroidStrategy2(dataClusterContentIndexes, similarityMatrix);
				centroidsIndex.add(currentIndexCentroid);
				
				// Add new cluster
				DataCluster cluster = new DataCluster();
				cluster.addDataObject(dataObjects.get(currentIndexCentroid));
				dataClusters.add(cluster);
			}
			
			
			
			// Check @param iterations
			if (this.iterations == -1)
				it = 1; // 1 just to be different from 0. If 'iterations' is -1, then we run the algorithm until convergence 
		}
		
		return dataClusters;
	}

	
	/**
	 * Definition: Choose centroids using Single Pass Seed Selection (SPSS) Algorithm.
	 * 
	 * @param k : number of centroids.
	 * @param centroidsIndex : the indexes of the centroids.
	 * @param similarityMatrix : similarity matrix with the similarity between every pair of objects.
	 */	
	private List<Integer> chooseCentroidsSPSS(int k, List<Integer> centroidsIndex, Matrix2D similarityMatrix) {
		// 2. Create list of distances
		List<Double> similaritySum = new ArrayList<Double>();
		int size = similarityMatrix.getWidth();
		for (int i = 0; i < size; i++) {
			double sum = 0;
			for (int j = 0; j < size; j++) {
				if (i == j) 
					continue;
				sum += similarityMatrix.get(i,j); 
			}
			similaritySum.add(sum);
		}
		
		// 3. Find max value in similaritySum
		double maxSimSum = this.MIN_VALUE;
		int maxSimSumIndex = -1;
		for (int i = 0; i < size; i++) {
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
			//List<Double> similarityDistances = new ArrayList<Double>(); //non-ordered list
			for (int i = 0; i < size; i++) {
				// Exclude centroids
				if (centroidsIndex.contains(i))
					continue;
				
				// Populate list
				double maxSim = this.MIN_VALUE;
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
			y = similarityDistances.get(0).getSimilarity();
			
			//for (int i = 0; i < (size/k); i++)
				//y += similarityDistances.get(i).getSimilarity();
			
			// 7-8. Find next centroid
			for (int i = 2; i < similarityDistances.size(); i++) {
				double leftSum = 0, rightSum = 0;
				for (int j = 0; j < (i+1); j++) {
					if (j < i) {
						leftSum += Math.pow(similarityDistances.get(j).getSimilarity(), 2);
					}
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
				if (i == similarityDistances.size()) {
					throw new RuntimeException("Kmeans class, function chooseCentroidsSPSS : No cntroid was selected");
				}
			}
		} while (centroidsIndex.size() < k);
		
		return centroidsIndex;
	}
	
	
	/**
	 * Definition: First strategy to choose the new centroid. Given the objects of the cluster,
	 * chooses the new centroid.
	 * 1) Calculate the overall mean similarity of the cluster.
	 * 2) Calculate the mean similarity of each object.
	 * 3) Choose the centroid that has its mean similarity closest to the overall mean similarity.
	 * 
	 *  @param dataClusterContentIndexes : list with the index of each dataObject from a cluster.
	 *  @param similarityMatrix : similarity matrix with the similarity between every pair of objects.
	 */
	private int getNewCentroidStrategy1(List<Integer> dataClusterContentIndexes, Matrix2D similarityMatrix) {
		
		double meanOverallSimilarity = 0;
		int meanOverallSimilarityCount = 0;
		List<Double> meanSimilarities = new ArrayList<Double>();
		double currentMeanSimilarity;
		
		int x, y;
		int size = dataClusterContentIndexes.size();
		for (int j = 0; j < size; j++) {
			x = dataClusterContentIndexes.get(j);
			currentMeanSimilarity = 0;
			for (int k = 0; k < size; k++) {
				y = dataClusterContentIndexes.get(k);
				
				if (x != y) {
					currentMeanSimilarity += similarityMatrix.get(x, y);
				}
				
				if (x > y) {
					meanOverallSimilarity += similarityMatrix.get(x, y); 
					meanOverallSimilarityCount++;
				}
			}
			
			currentMeanSimilarity /= (size - 1);
			meanSimilarities.add(currentMeanSimilarity);
		}
		
		meanOverallSimilarity /= meanOverallSimilarityCount;
		
		// Choose the object with the closest mean similarity of the overallMeanSimilarity 
		int currentIndexCentroid = dataClusterContentIndexes.get(0); // Get the first object  
		double diff1, diff2;
		diff2 = Math.abs(meanOverallSimilarity - meanSimilarities.get(0));
		for (int j = 0; j < size; j++) {
			diff1 = Math.abs(meanOverallSimilarity - meanSimilarities.get(j));
			if(diff1 > diff2)
				currentIndexCentroid = dataClusterContentIndexes.get(j);
		}
		
		return currentIndexCentroid;
	}
	
	
	/**
	 * Definition: Second strategy to choose the new centroid. Given the objects of the cluster,
	 * chooses the new centroid.
	 * 1) Calculate the mean similarity of an object from the cluster with all the others.
	 * 2) Do this for each object.
	 * 3) The object with the highest mean similarity in the cluster is the new centroid.
	 * 
	 *  @param dataClusterContentIndexes : list with the index of each dataObject from a cluster.
	 *  @param similarityMatrix : similarity matrix with the similarity between every pair of objects.
	 */
	private int getNewCentroidStrategy2(List<Integer> dataClusterContentIndexes, Matrix2D similarityMatrix) {
		double currentMeanSimilarity = MIN_VALUE;
		int currentIndexCentroid = -1;
		
		int size = dataClusterContentIndexes.size();
		if (size == 1)
			return dataClusterContentIndexes.get(0);	
			
		for (int j = 0; j < size; j++) {
			double meanSimilarity = 0;
			int x, y;
			x = dataClusterContentIndexes.get(j);
			for (int k = 0; k < size; k++) {
				y = dataClusterContentIndexes.get(k);
				if (x != y)
					meanSimilarity += similarityMatrix.get(x, y);
			}
			
			// Get the meanSimilarity for the size-1 similarities
			// size-1, because the similarityMatrix.get(j, j) is not computed
			meanSimilarity = meanSimilarity / (size - 1);
			
			// Tests if the new meanSimilarity is the highest so far
			if (meanSimilarity > currentMeanSimilarity) {
				currentMeanSimilarity = meanSimilarity;
				currentIndexCentroid = x;
			}
		}
		
		return currentIndexCentroid;
	}	
}
