package clusteringstrategies.implementation.clusteranalysis;

import java.util.ArrayList;
import java.util.List;

import utility.clusteranalysis.ClusterAnalysisUtility;
import clusteringstrategies.core.AnalysisStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.implementations.datatypes.TextFile;

/* ---------------------------------------------------------------------------------
 * 
 * Entropy
 * 
 * The degree to which each cluster consists of objects of a single class.
 * For each cluster, the class distribution of the data is calculated first, i.e.,
 * for cluster i we compute pij, the probability that a member of cluster i belongs
 * to class j as pij = mij / mi, where mi is the number of objects in cluster i and
 * mij is the number of objects of class j in cluster i. Using this class distribution,
 * the entropy of each cluster i is calculated using the standard formula,
 * 
 * ei = - sum from j=1 to L{ pij * log2 pij }, 
 * 
 * where L is the number of classes. The total entropy for a set of clusters is calculated
 * as the sum of the entropies of each cluster weighted by the size of each cluster, i.e.,
 * 
 * e = sum from i=1 to K{ (mi / m) * ei },
 * 
 * where K is the number of clusters and m is the total number of data points.
 * 
 * ---------------------------------------------------------------------------------
 * 
 * Entropy algorithm:
 * 1. Load the classes into a data structure.
 * 2. For each cluster checks how many of its objects belong to each class.
 * 3. Calculate entropy with the results of each iteration. 
 * 
 * ---------------------------------------------------------------------------------
 * 
 * The perfect Entropy is 0.  
 * 
 * ---------------------------------------------------------------------------------
 */

public class EntropyAnalysisStrategy extends AnalysisStrategy {

	List<List<String>> dataClasses;
		
	/**
	 * Definition: Entropy Analysis Constructor 
	 * 
	 * @param filename : refers to a file with the expected classes. These classes
	 * are a reference for the clusters that the clustering algorithm must return. 
	 */
	public EntropyAnalysisStrategy(String filename) {
		// Initialize the data classes
		this.dataClasses = new ArrayList<List<String>>();
		this.dataClasses = ClusterAnalysisUtility.loadDataClasses(filename);
	}
	
	
	/**
	 * Definition: Entropy core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param dataClusters : list of data clusters. These clusters were calculated using a 
	 * clustering algorithm.  
	 */
	@Override
	public void executeAnalysis(List<DataObject> dataObjects, List<DataCluster> dataClusters) {
		// First gets the data objects names of each cluster
		List<List<String>> clusters = new ArrayList<List<String>>();
		for (DataCluster c : dataClusters) {
			List<String> newCluster = new ArrayList<String>();
			int size = c.getDataObjects().size();
			for (int i = 0; i < size; i++) {
				TextFile doc = new TextFile("doc", "", 0);
				doc = (TextFile) c.getObject(i);
				newCluster.add(doc.getTitle());
			}
			clusters.add(newCluster);
		}
				
		// Find the entropy for each cluster, according to the classes	
		double[] entropy = new double[dataClusters.size()];
		double totalEntropy = 0;
		
		int clusterIndex = 0;
		for (List<String> cluster : clusters) {
			for (List<String> aClass : this.dataClasses) {
				int clusterSize = cluster.size();
				int count = 0;
				for (int i = 0; i < clusterSize; i++) {
					String s = cluster.get(i);
					if (aClass.contains(s)) {
						count++;
					}
				}
				double pij = (double) count / clusterSize;
				if (pij == 0)
					continue;
				entropy[clusterIndex] += pij * (Math.log(pij) / Math.log(2)); 
			}
			
			entropy[clusterIndex] = entropy[clusterIndex] * (-1);
			totalEntropy += (double) (cluster.size() / dataObjects.size()) * entropy[clusterIndex];
			
			//System.out.println("Cluster " + clusterIndex);
			//System.out.println("\tEntropy:\t" + entropy[clusterIndex]);
			clusterIndex++;
		}
		
		System.out.println("Total Entropy:\t" + totalEntropy);
				
		return;
	}
}
