package br.ufrgs.inf.jlggross.documentclustering.strategies.clusteranalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.strategy.AnalysisStrategy;
import br.ufrgs.inf.jlggross.documentclustering.Document;

/* ---------------------------------------------------------------------------------
 * 
 * Precision, Recall and F-Measure:
 * 1. Inform the expected classes of information (analysis with external information
 * - supervised analysis).
 * 2. Given the clusters, calculated by using an clustering algorithm, check how many
 * data objects of each class that a cluster have.
 * 3. Given a cluster, its precision and recall will be calculated considering the 
 * class to which it has the higher amount of data objects as reference. Example: If
 * a cluster has 2 data objects from class SERIES and 3 from class MOVIES, then the 
 * calculation will be based on the MOVIES class.      
 * 
 * ---------------------------------------------------------------------------------
 * Model of file for the classes:
 * (line 01) Animals: 2
 * (line 02) Cat.txt
 * (line 03) Dog.txt
 * (line 04)
 * (line 05) Languages: 3
 * (line 06) English.txt
 * (line 07) Portuguese.txt
 * (line 08)
 * (line 09) Series: 3
 * (line 10) Fringe.txt
 * (line 11) Supernatural.txt
 * (line 12) The-Walking-Dead.txt
 * 
 * An example can also be find in data/test01-wikipedia12/classes.txt
 * ---------------------------------------------------------------------------------
 */

public class FmeasureAnalysisStrategy extends AnalysisStrategy {

	List<List<String>> dataClasses;
	
	/**
	 * Definition: F-Measure Analysis Constructor
	 * 
	 * @param filename : refers to a file with the expected classes. These classes
	 * are a reference for the clusters that the clustering algorithm must return. 
	 */
	public FmeasureAnalysisStrategy(String filename) {
		// Initialize the data classes
		this.dataClasses = new ArrayList<List<String>>();
		this.dataClasses = this.loadDataClasses(filename);
	}
	
	
	/**
	 * Definition: F-Measure core algorithm execution.
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
		
		// Find the reference class for each cluster and calculate precision and recall	
		double[] precision = new double[dataClusters.size()];
		double[] recall = new double[dataClusters.size()];
		double[] fmeasure = new double[dataClusters.size()];
		
		int clusterIndex = 0;
		for (List<String> cluster : clusters) {
			int currentClassIndex = 0;
			int selectedClassIndex = -1;
			int max = -1;
			for (List<String> aClass : this.dataClasses) {
				int size = cluster.size();
				int count = 0;
				for (int i = 0; i < size; i++) {
					String s = cluster.get(i);
					if (aClass.contains(s)) {
						count++;
					}
				}
				
				if (count > max) {
					selectedClassIndex = currentClassIndex;
					max = count;
				}
				currentClassIndex++;
			}
						
			precision[clusterIndex] = max / cluster.size();
			recall[clusterIndex] = max / this.dataClasses.get(selectedClassIndex).size();
			fmeasure[clusterIndex] = fmeasure(precision[clusterIndex], recall[clusterIndex]);
			clusterIndex++;
		}
		
		return 0;
	}
	
	
	/**
	 * Definition: Load the data classes.
	 * 
	 * @param filename : refers to a file with the expected classes. 
	 */
	private List<List<String>> loadDataClasses(String filename) {
		
		List<List<String>> classes = new ArrayList<List<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while (null != (line = reader.readLine())) {
				if (!line.isEmpty()) {
					
					String[] parse = line.split(": ");
					List<String> newClass = new ArrayList<String>();
					int count = Integer.valueOf(parse[1]);
					
					for (int i = 0; i < count; i++) {
						line = reader.readLine();
						newClass.add(line);
					}
					
					line = reader.readLine();
					classes.add(newClass);
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("IOException in function loadMatrix of class Matrix2D.java");
			e.printStackTrace();
		}
		
		return classes;
	}
	
	
	private double fmeasure(double precision, double recall) {
		return 2 * ((precision * recall) / (precision + recall));
	}
	
	/* TODO
	public double microaverageRecall(List<DataCluster> dataClusters, List<DataCluster> dataClasses) {
		double clustered = 0.0;
		Set<DataObject> classifiedObjects = new HashSet<DataObject>();
		
		for (DataCluster cluster : dataClusters) {
			clustered += cluster.getDataObjects().size();
		}
		
		for (DataCluster clazz : dataClasses) {
			classifiedObjects.addAll(clazz.getDataObjects());
		}
		
		double classified = classifiedObjects.size();
		
		return clustered / classified;
	}
	*/

}
