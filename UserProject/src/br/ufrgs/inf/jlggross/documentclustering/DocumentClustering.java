package br.ufrgs.inf.jlggross.documentclustering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrgs.inf.jlggross.clustering.ClusteringProcess;
import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.documentclustering.strategies.clustering.BestStarClusteringStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.clustering.DBSCANClusteringStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.clustering.FuzzyCMeansClusteringStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.clustering.KmeansClusteringStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.clustering.KmedoidsClusteringStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.featureselection.TermSelectionStrategy;
import br.ufrgs.inf.jlggross.documentclustering.strategies.similarity.FuzzyMeansSimilarityStrategy;

public class DocumentClustering {
	
	/**
	 * Run Tests
	 */
	public static void main(String args[]) {		
		TestClusteringStrategies();
		//reutersClustering();
	}
	
	private static void TestClusteringStrategies() {
		int[] dataSetSizes = {5, 10, 15};
		String[] filenames = {"similarity5", "similarity10", "similarity15"};
		
		Document doc;
		for (int i = 0; i < dataSetSizes.length; i++) {
			ClusteringProcess process = new ClusteringProcess();

			// Create DataObjects
			int size = dataSetSizes[i];
			for (int j = 0; j < size; j++) { 	
				doc = new Document("OBJ" + j + ".txt", "", j);
				process.addDataObject(doc);
				System.out.println("Added dataObject: " + doc.getTitle());
			}
			
			// Load similarity matrix for dataSet1
			Matrix2D similarityMatrix = new Matrix2D(process.getDataObjectCount());
			similarityMatrix.loadMatrix("similarity/" + filenames[i] + ".txt", process.dataObjects.size());
			System.out.println(similarityMatrix.toString());
		
			// -------------------------------------------------------------------------------
			
			// K-medoids
			int k = 2; // Number of clusters
			int iterations = 2; // Number of iterations
			KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 1);
			KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 2);

			// K-means
			k = 2; // Number of clusters
			iterations = 2; // Number of iterations
			KmeansTest(filenames[i], process, similarityMatrix, k, iterations);
		
			// DBSCAN
			double epsilon = 0.2;
			int minObjs = 2; // Minimum number of objects per cluster
			DBSCANTest(filenames[i], process, similarityMatrix, epsilon, minObjs);
			
			// Fuzzy C-Means
			double fuzziness = 2.0; // Normally  the fuzziness is set to 2.0
			k = 2; iterations = 2;
			FuzzyCMeansTest(filenames[i], process, similarityMatrix, fuzziness, k, iterations);
			
			// BestStar
			double GSM = 0.5;
			BestStarTest(filenames[i], process, similarityMatrix, GSM);

			// -------------------------------------------------------------------------------
		}
	}
	
	
	/**
	 * Test BestStar clustering strategy algorithm
	 */
	private static void FuzzyCMeansTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double fuzziness, int numClusters, int iterations) {
		
		// Set and run K-Means Clustering Strategy 
		process.setClusteringStrategy(new FuzzyCMeansClusteringStrategy(fuzziness, numClusters, iterations));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "FuzzyCMeans";
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Test BestStar clustering strategy algorithm
	 */
	private static void BestStarTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double GSM) {
		
		// Set and run K-Means Clustering Strategy 
		process.setClusteringStrategy(new BestStarClusteringStrategy(GSM));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "BestStar";
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Test DBSCAN clustering strategy algorithm
	 */
	private static void DBSCANTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double epsilon, int minObjs) {
		
		// Set and run DBSCAN Clustering Strategy 
		process.setClusteringStrategy(new DBSCANClusteringStrategy(epsilon, minObjs));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "DBSCAN";
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Test K-medoids clustering strategy algorithm
	 */
	private static void KmedoidsTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			int numClusters, int iterations, int centroidStrategy) {		
		
		// Set and run K-Means Clustering Strategy		
		process.setClusteringStrategy(new KmedoidsClusteringStrategy(numClusters, iterations, centroidStrategy));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
		
		// Write clusters on file
		String strategy = "";
		if (centroidStrategy == 1)
			strategy = "Kmedoids-CentroidStrategy1";
		else if (centroidStrategy == 2)
			strategy = "Kmedoids-CentroidStrategy2";
			
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Test K-means clustering strategy algorithm
	 */
	private static void KmeansTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			int numClusters, int iterations) {		
		
		// Set and run K-Means Clustering Strategy		
		process.setClusteringStrategy(new KmeansClusteringStrategy(numClusters, iterations));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
		
		String strategy = "Kmeans";
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Write clusters result in .txt file
	 */
	private static void writeCluster(ClusteringProcess process, String strategy, String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("output/clusters-" + filename + "-" + strategy + ".txt"));
			String s;
			System.out.println("Clusters: ");
			for (DataCluster cluster : process.getDataClusters()) {
				s = Integer.toString(cluster.getDataObjects().size()) + "\n";
				System.out.printf(s);
				writer.append(s);
				
				for (DataObject object : cluster.getDataObjects()) {
					Document doc = (Document) object;
					s = doc.getTitle() + "\n";
					System.out.printf(s);
					writer.append(s);
				}
			}			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test clustering over reuters data
	 */
	private void reutersClustering() {
		ClusteringProcess process = new ClusteringProcess();
		
		// Setup stopwords.
		Set<String> stopwords = new HashSet<String>();

		// Stopwords for reuters set of files
		stopwords.addAll(this.getStopwords("consoantes"));
		stopwords.addAll(this.getStopwords("english"));
		stopwords.addAll(this.getStopwords("sgml"));
		stopwords.addAll(this.getStopwords("vogais"));
		
		// Register the strategies that will be used.
		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new FuzzyMeansSimilarityStrategy());
		//process.setClusteringStrategy(new CliquesClusteringStrategy(1.0));
		//process.setClusteringStrategy(new StarsClusteringStrategy(1.0));
		//process.setClusteringStrategy(new FullStarsClusteringStrategy(1.0));
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.5));
		
		// Register the observers.
		process.addObserver(new ConsoleObserver());
		//process.addObserver(new ClusteringWindow(process));
		
		// Run this code to extract reuters documents from .sgm files located in data/reuters
		/*
		try {
			ExtractReuters.main(null);
		} catch (IOException e1) {
			System.out.println("ExtractReuters.main : reuters documents extracted to data/extratec.");
			e1.printStackTrace();
		}
		*/
		
		// Add data objects (documents!).
		try {
			//File docFolder = new File("data/reuters-extracted");
			//File docFolder = new File("data/wikipedia");
			File docFolder = new File("data/teste1-beststar");
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				
				process.addDataObject(new Document(doc.toString(), content.toString(), index));
				index++;
				
				System.out.println("DataObject added: " + doc.toString());
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		process.executeClusteringProcess();
		
		/*// All thresholds for testing.
		double[] thresholds = new double[] { 0.0, 0.05, 0.1, 0.15, 0.2, 0.5, 1.0 };
		
		for (int i = 0; i < thresholds.length; i++) {
			//System.out.println("\nClick (" + thresholds[i] + ")");
			process.setClusteringStrategy(new ClickClusteringStrategy(thresholds[i]));
			process.executeClusteringProcess();

			//System.out.println("\nBest-star (" + thresholds[i] + ")");
			process.setClusteringStrategy(new BestStarClusteringStrategy(thresholds[i]));
			process.executeClusteringProcess();

			//System.out.println("\nStars (" + thresholds[i] + ")");
			process.setClusteringStrategy(new StarsClusteringStrategy(thresholds[i]));
			process.executeClusteringProcess();

			//System.out.println("\nFull-stars (" + thresholds[i] + ")");
			process.setClusteringStrategy(new FullStarsClusteringStrategy(thresholds[i]));
			process.executeClusteringProcess();
		}*/
		
		/*System.out.println();
		for (DataObject obj : process.getDataObjectsArray()) {
			System.out.println(obj.toString());
		}
		System.out.println(process.getSimilarityMatrix().toString());*/
		
	}
	
	/**
	 * 
	 */
	private List<String> getStopwords(String wordsCollection) {
		List<String> stopwords = new ArrayList<String>();
		
		File file = new File("data/stopwords/" + wordsCollection + ".txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer content = new StringBuffer();
			while (null != (line = reader.readLine())) {
				content.append(line);
			}
			
			String[] words = content.toString().toLowerCase().replace(" ", "").split(",");
			stopwords.addAll(Arrays.asList(words));
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stopwords;
	}

}
