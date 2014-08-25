package mystrategies.clusteranalysis;

import java.util.ArrayList;
import java.util.List;

import utility.clusteranalysis.ClusterAnalysisUtility;
import clusteringstrategies.core.AnalysisStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public class MyAnalysisStrategy extends AnalysisStrategy {

	List<List<String>> dataClasses;
	
	/**
	 * Definition: My analysis algorithm constructor 
	 * 
	 * @param filename : refers to a file with the expected classes. These classes
	 * are a reference for the clusters that the clustering algorithm must return. 
	 */
	public MyAnalysisStrategy(String filename) {
		// Initialize the data classes
		this.dataClasses = new ArrayList<List<String>>();
		this.dataClasses = ClusterAnalysisUtility.loadDataClasses(filename);
	}
	
	
	/**
	 * Definition: My analysis algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param dataClusters : list of data clusters. These clusters were calculated using a 
	 * clustering algorithm.  
	 */
	@Override
	public void executeAnalysis(List<DataObject> dataObjects, List<DataCluster> dataClusters) {
		// TODO				
		return;
	}
	
	
	/**
	 * Definition: My analysis algorithm execution.
	 * 
	 * @param dataObjects : list of data objects.
	 * @param dataClusters : list of data clusters. These clusters were calculated using a 
	 * clustering algorithm.  
	 * @param similarityMatrix : similarity matrix calculated by a similarity algorithm.
	 */
	@Override
	public double executeAnalysis(List<DataObject> dataObjects, List<DataCluster> dataClusters, Matrix2D similarityMatrix) {
		// TODO
		return 0;
	}
}
