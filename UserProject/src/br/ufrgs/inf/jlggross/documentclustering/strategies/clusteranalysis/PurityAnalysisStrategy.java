package br.ufrgs.inf.jlggross.documentclustering.strategies.clusteranalysis;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.strategy.AnalysisStrategy;
import br.ufrgs.inf.jlggross.documentclustering.Document;

/* ---------------------------------------------------------------------------------
 * 
 * Purity: Measures the extent to which a cluster contains objects of a single class.
 * The purity of cluster i is pi = max pij, where pij is the probability that a member
 * of cluster i belongs to class j. The overall purity of a clustering is,
 * 
 * purity = sum from i=1 to K{ (mi / m) * pi },
 * 
 * where mi is the number of objects in cluster i and m is the total number of data
 * points.
 * 
 * ---------------------------------------------------------------------------------
 * 
 * Purity algorithm:
 * 1. Load the classes into a data structure.
 * 2. For each cluster checks how many of its objects belong to each class.
 * 3. Calculate purity with the results of each iteration.
 * 
 * ---------------------------------------------------------------------------------
 */

public class PurityAnalysisStrategy extends AnalysisStrategy {

	List<List<String>> dataClasses;
	
	/**
	 * Definition: Purity Analysis Constructor 
	 * 
	 * @param filename : refers to a file with the expected classes. These classes
	 * are a reference for the clusters that the clustering algorithm must return. 
	 */
	public PurityAnalysisStrategy(String filename) {
		// Initialize the data classes
		this.dataClasses = new ArrayList<List<String>>();
		this.dataClasses = FmeasureAnalysisStrategy.loadDataClasses(filename);
	}
	
	
	/**
	 * Definition: Purity core algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param dataClusters : list of data clusters. These clusters were calculated using a 
	 * clustering algorithm.  
	 */
	@Override
	public double executeAnalysis(List<DataObject> dataObjects, List<DataCluster> dataClusters) {
		// First gets the data objects names of each cluster
		List<List<String>> clusters = new ArrayList<List<String>>();
		for (DataCluster c : dataClusters) {
			List<String> newCluster = new ArrayList<String>();
			int size = c.getDataObjects().size();
			for (int i = 0; i < size; i++) {
				Document doc = new Document("doc", "", 0);
				doc = (Document) c.getObject(i);
				newCluster.add(doc.getTitle());
			}
			clusters.add(newCluster);
		}
				
		// Find the purity for each cluster, according to the classes	
		double[] purity = new double[dataClusters.size()];
		double totalPurity = 0.0;
		double pij = 0;
		
		int clusterIndex = 0;
		for (List<String> cluster : clusters) {
			
			purity[clusterIndex] = 0.0;
			for (List<String> aClass : this.dataClasses) {
				int clusterSize = cluster.size();
				int count = 0;
				for (int i = 0; i < clusterSize; i++) {
					String s = cluster.get(i);
					if (aClass.contains(s)) {
						count++;
					}
				}
				
				pij = (double) count / clusterSize;
				if (pij > purity[clusterIndex])
					purity[clusterIndex] = pij; 
			}
			
			totalPurity += ((double) cluster.size() / dataObjects.size()) * purity[clusterIndex];
			System.out.println("Cluster " + clusterIndex);
			System.out.println("\tPurity:\t" + purity[clusterIndex]);
			clusterIndex++;
		}
		
		System.out.println("\nTotal Purity:\t" + totalPurity);
				
		return 0;
	}
}
