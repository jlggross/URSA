package main;

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

import main.ClusteringProcess;
import utility.ExtractReuters;
import clusteringstrategies.implementation.clusteranalysis.FmeasureAnalysisStrategy;
import clusteringstrategies.implementation.clusteranalysis.PurityAnalysisStrategy;
import clusteringstrategies.implementation.clusteranalysis.SilhouetteAnalysisStrategy;
import clusteringstrategies.implementation.clustering.AgglomerativeHierarchicalClusteringStrategy;
import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import clusteringstrategies.implementation.clustering.DBSCANClusteringStrategy;
import clusteringstrategies.implementation.clustering.FullStarsClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmeansClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmedoidsClusteringStrategy;
import clusteringstrategies.implementation.featureselection.AudioMetaDataSelectionStrategy;
import clusteringstrategies.implementation.featureselection.TermSelectionStrategy;
import clusteringstrategies.implementation.featureselection.VideoMetaDataSelectionStrategy;
import clusteringstrategies.implementation.similarity.AudioFileSimilarityStrategy;
import clusteringstrategies.implementation.similarity.TextFileFuzzyMeansSimilarityStrategy;
import clusteringstrategies.implementation.similarity.VideoFileSimilarityStrategy;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.implementations.datatypes.AudioMediaFile;
import datastructures.implementations.datatypes.TextFile;
import datastructures.implementations.datatypes.VideoMediaFile;

public class DocumentClustering {
	
	/**
	 * Run Tests
	 */
	public static void main(String args[]) {		
		//TestClusteringStrategies();
		reutersClustering();
		//TestClusteringWikipedia12();
		//TestClusteringWikipedia13();
		//TestVideoFiles();
		//TestAudioFiles();
	}
	
	
	/**
	 * 
	 * */
	private static void TestAudioFiles() {
		ClusteringProcess process = new ClusteringProcess();
		process.setFeatureSelectionStrategy(new AudioMetaDataSelectionStrategy());
		process.setSimilarityStrategy(new AudioFileSimilarityStrategy());
		
		// Add data objects
		String mediapath = "data/resAudio/";
		File mediaDir = new File(mediapath);
		File[] medias = mediaDir.listFiles();
		int index = 0;
		for (File mediaFile : medias) {
			String filename[] = mediaFile.getPath().split("\\\\");
			process.addDataObject(new AudioMediaFile(filename[2], mediaFile.getPath(), index));
			index++;
		}	

		// Processing
		process.dataObjects = process.featureSelectionStrategy.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy.executeSimilarity(process.dataObjects);	
		System.out.print(process.similarityMatrix.toString());
		
		for (DataObject dataObject : process.dataObjects) {
			System.out.println(dataObject.toString());
		}
	}
	
	
	
	/**
	 * 
	 * */
	private static void TestVideoFiles() {
		ClusteringProcess process = new ClusteringProcess();
		process.setFeatureSelectionStrategy(new VideoMetaDataSelectionStrategy());
		process.setSimilarityStrategy(new VideoFileSimilarityStrategy());
		
		// Add data objects
		String mediapath = "data/resVideo/";
		File mediaDir = new File(mediapath);
		File[] medias = mediaDir.listFiles();
		int index = 0;
		for (File mediaFile : medias) {
			String filename[] = mediaFile.getPath().split("\\\\");
			process.addDataObject(new VideoMediaFile(filename[2], mediaFile.getPath(), index));
			index++;
		}	

		// Processing
		process.dataObjects = process.featureSelectionStrategy.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy.executeSimilarity(process.dataObjects);	
		System.out.print(process.similarityMatrix.toString());
		
		for (DataObject dataObject : process.dataObjects) {
			//VideoMediaFile aVideo = (VideoMediaFile) dataObject; 
			System.out.println(dataObject.toString());
		}
	}
	
	
	
	/**
	 * 
	 * */
	private static void TestClusteringWikipedia12() {
		ClusteringProcess process = new ClusteringProcess();
		
		Set<String> stopwords = new HashSet<String>();
		stopwords.addAll(getStopwords("consoantes"));
		stopwords.addAll(getStopwords("english"));
		stopwords.addAll(getStopwords("vogais"));
		
		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.10));
		
		try { // Add data objects (documents!).
			File docFolder = new File("data/wikipedia12");
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				String[] name = doc.toString().split("\\\\");
				process.addDataObject(new TextFile(name[name.length-1], content.toString(), index));
				index++;
				System.out.println("DataObject added: " + name[name.length-1]);
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		process.dataObjects = process.featureSelectionStrategy.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy.executeSimilarity(process.dataObjects);	
		System.out.print(process.similarityMatrix.toString());
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, process.similarityMatrix);	
						
		// Write clusters on file
		String strategy = "BestStar";
		writeCluster(process, strategy, "wikipedia12");
		
		// Analysis
		process.setAnalysisStrategy(new FmeasureAnalysisStrategy("tests/test01-wikipedia12/classes.txt"));
		process.analysisStrategy.executeAnalysis(process.dataObjects, process.dataClusters);
	}
	
	
	/**
	 * 
	 * 
	 * */
	private static void TestClusteringWikipedia13() {
		ClusteringProcess process = new ClusteringProcess();
		
		Set<String> stopwords = new HashSet<String>();
		stopwords.addAll(getStopwords("consoantes"));
		stopwords.addAll(getStopwords("english"));
		stopwords.addAll(getStopwords("vogais"));
		
		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.03));
		
		try { // Add data objects (documents!).
			File docFolder = new File("data/wikipedia13");
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				String[] name = doc.toString().split("\\\\");
				process.addDataObject(new TextFile(name[name.length-1], content.toString(), index));
				index++;
				System.out.println("DataObject added: " + name[name.length-1]);
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		process.dataObjects = process.featureSelectionStrategy.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy.executeSimilarity(process.dataObjects);	
		System.out.print(process.similarityMatrix.toString());
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, process.similarityMatrix);	
						
		// Write clusters on file
		String strategy = "BestStar";
		writeCluster(process, strategy, "wikipedia13");
		
		// Analysis
		//process.setAnalysisStrategy(new FmeasureAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		//process.setAnalysisStrategy(new EntropyAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		process.setAnalysisStrategy(new PurityAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		process.analysisStrategy.executeAnalysis(process.dataObjects, process.dataClusters);
	}
	
	
	/**
	 * 
	 * 
	 * */
	private static void TestClusteringStrategies() {
		int[] dataSetSizes = {4, 5, 6, 10, 15};
		String[] filenames = {"similarity4", "similarity5", "similarity6", "similarity10", "similarity15"};
		
		TextFile doc;
		for (int i = 0; i < dataSetSizes.length; i++) {
			ClusteringProcess process = new ClusteringProcess();

			// Create DataObjects
			int size = Integer.parseInt(filenames[i].substring(10));
			for (int j = 0; j < size; j++) { 	
				doc = new TextFile("OBJ" + j + ".txt", "", j);
				process.addDataObject(doc);
				System.out.println("Added dataObject: " + doc.getTitle());
			}
			
			// Load similarity matrix for each data set
			Matrix2D similarityMatrix = new Matrix2D(process.getDataObjectCount());
			similarityMatrix.loadMatrix("similarity/" + filenames[i] + ".txt", process.dataObjects.size());
			System.out.println(similarityMatrix.toString());
		
			// -------------------------------------------------------------------------------
			
			/*
			int k = 2; // Number of clusters
			int iterations = 2; // Number of iterations
			KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 1);
			KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 2);
			*/

			/*
			k = 2; // Number of clusters
			iterations = 2; // Number of iterations
			KmeansTest(filenames[i], process, similarityMatrix, k, iterations);
			*/
		
			/*
			double epsilon = 0.2;
			int minObjs = 2; // Minimum number of objects per cluster
			DBSCANTest(filenames[i], process, similarityMatrix, epsilon, minObjs);
			*/
			
			/*
			double fuzziness = 2.0; // Normally  the fuzziness is set to 2.0
			k = 2; iterations = 2;
			FuzzyCMeansTest(filenames[i], process, similarityMatrix, fuzziness, k, iterations);
			*/
			
			/*
			AgglomerativeHierarchicalTest(filenames[i], process, similarityMatrix, 0.10, 0.70, 1);
			*/
			
			
			BestStarTest(filenames[i], process, similarityMatrix, 0.5);
		
			
			//FullStarsTest(filenames[i], process, similarityMatrix, 0.5);
		
		}
	}
	
	
	/**
	 * Test Fuzzy C-Means clustering strategy algorithm
	 */ 
	/*
	private static void FuzzyCMeansTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double fuzziness, int numClusters, int iterations) {
		
		// Set and run Fuzzy C-Means Clustering Strategy 
		process.setClusteringStrategy(new FuzzyCMeansClusteringStrategy(fuzziness, numClusters, iterations));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "FuzzyCMeans";
		writeCluster(process, strategy, filename);
	}
	*/
	
	
	/**
	 * Test Agglomerative Hierarchical clustering strategy algorithm
	 */
	private static void AgglomerativeHierarchicalTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double thresholdLvl, double minThreshold, int clusteringMethod) {
		
		// Set and run Agglomerative Hierarchical Clustering Strategy 
		process.setClusteringStrategy(new AgglomerativeHierarchicalClusteringStrategy(
				thresholdLvl, minThreshold, clusteringMethod));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "Agglomerative";
		writeCluster(process, strategy, filename);
		
		// Cluster Analysis
		similarityMatrix.loadMatrix("similarity/" + filename + ".txt", process.dataObjects.size()); //re-load similarity matrix
		SilhouetteAnalysisStrategy s = new SilhouetteAnalysisStrategy();
		double silhouette = s.executeAnalysis(process.dataObjects, process.dataClusters, similarityMatrix);
		System.out.println("Silhouette: " + silhouette);
	}
	
	
	/**
	 * Test FullStar clustering strategy algorithm
	 */
	private static void FullStarsTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double GSM) {
		
		// Set and run Full Stars Clustering Strategy 
		process.setClusteringStrategy(new FullStarsClusteringStrategy(GSM));
		process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, similarityMatrix);
				
		// Write clusters on file
		String strategy = "FullStars";
		writeCluster(process, strategy, filename);
	}
	
	
	/**
	 * Test BestStar clustering strategy algorithm
	 */
	private static void BestStarTest(String filename, ClusteringProcess process, Matrix2D similarityMatrix,
			double GSM) {
		
		// Set and run Best Star Clustering Strategy 
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
		
		// Set and run K-Medoids Clustering Strategy		
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
					TextFile doc = (TextFile) object;
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
	private static void reutersClustering() {
		ClusteringProcess process = new ClusteringProcess();
		
		// Setup stopwords.
		Set<String> stopwords = new HashSet<String>();

		// Stopwords for reuters set of files
		stopwords.addAll(getStopwords("consoantes"));
		stopwords.addAll(getStopwords("english"));
		stopwords.addAll(getStopwords("sgml"));
		stopwords.addAll(getStopwords("vogais"));
		
		// Register the strategies that will be used.
		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
		//process.setClusteringStrategy(new CliquesClusteringStrategy(1.0));
		//process.setClusteringStrategy(new StarsClusteringStrategy(1.0));
		//process.setClusteringStrategy(new FullStarsClusteringStrategy(1.0));
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.05));
		
		// Register the observers.
		process.addObserver(new ConsoleObserver());
		//process.addObserver(new ClusteringWindow(process));
		
		// Run this code to extract reuters documents from .sgm files located in data/reuters-sgm
		try {
			ExtractReuters.main(null);
		} catch (IOException e1) {
			System.out.println("ExtractReuters.main : reuters documents extracted to data/reuters-extracted.");
			e1.printStackTrace();
		}
		
		// Add data objects (documents!).
		try {
			File docFolder = new File("data/reuters-extracted");
			//File docFolder = new File("data/wikipedia");
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				
				process.addDataObject(new TextFile(doc.toString(), content.toString(), index));
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
	private static List<String> getStopwords(String wordsCollection) {
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
