package main;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utility.clustering.ClusteringUtility;
import utility.datatypes.AudioVideoFilesUtility;
import utility.datatypes.TextFilesUtility;
import utility.featureselection.ReutersSGMtoTXT;
import utility.featureselection.StopWords;
import clusteringstrategies.implementation.clusteranalysis.EntropyAnalysisStrategy;
import clusteringstrategies.implementation.clusteranalysis.FmeasureAnalysisStrategy;
import clusteringstrategies.implementation.clusteranalysis.PurityAnalysisStrategy;
import clusteringstrategies.implementation.clusteranalysis.SilhouetteAnalysisStrategy;
import clusteringstrategies.implementation.clustering.AgglomerativeHierarchicalClusteringStrategy;
import clusteringstrategies.implementation.clustering.BestStarClusteringStrategy;
import clusteringstrategies.implementation.clustering.DBSCANClusteringStrategy;
import clusteringstrategies.implementation.clustering.FullStarsClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmeansClusteringStrategy;
import clusteringstrategies.implementation.clustering.KmedoidsClusteringStrategy;
import clusteringstrategies.implementation.clustering.StarsClusteringStrategy;
import clusteringstrategies.implementation.featureselection.AudioMetaDataSelectionStrategy;
import clusteringstrategies.implementation.featureselection.TermSelectionStrategy;
import clusteringstrategies.implementation.featureselection.VideoMetaDataSelectionStrategy;
import clusteringstrategies.implementation.similarity.AudioFileSimilarityStrategy;
import clusteringstrategies.implementation.similarity.TextFileFuzzyMeansSimilarityStrategy;
import clusteringstrategies.implementation.similarity.VideoFileSimilarityStrategy;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.implementations.datatypes.TextFile;

public class DocumentClustering {

	/**
	 * Run Tests
	 */
	public static void main(String args[]) {
		//TestClusteringStrategies();
		// reutersClustering();
		TestClusteringWikipedia12();
		// TestClusteringWikipedia13();
		//TestAudioVideoFiles("audio", "BestStarClusteringStrategy");
		
		//ClusterAnalysisUtility.buildClusterFile("data/test05-video-data", "data/test05-video-analysis/");
	}

	
	private static void TestAudioVideoFiles(String type, String clusteringStrategy) {
		ClusteringProcess process = new ClusteringProcess();
		
		if (type == "audio") {
			process.setFeatureSelectionStrategy(new AudioMetaDataSelectionStrategy());
			process.setSimilarityStrategy(new AudioFileSimilarityStrategy());
			AudioVideoFilesUtility.addDataObjects(process, "data/resAudio/", "audio");
		} else if (type == "video") {
			process.setFeatureSelectionStrategy(new VideoMetaDataSelectionStrategy());
			process.setSimilarityStrategy(new VideoFileSimilarityStrategy());
			AudioVideoFilesUtility.addDataObjects(process, "data/resVideo/", "video");
		} else {
			throw new RuntimeException("type should be 'audio' or 'video'.");
		}

		process.setClusteringStrategy(new BestStarClusteringStrategy(0.40));
		
		// Processing
		process.dataObjects = process.featureSelectionStrategy
			.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy
			.executeSimilarity(process.dataObjects);
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, process.similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfAudioVideoFiles(process, type, type, type);
		
		for (DataObject dataObject : process.dataObjects) {
			System.out.println(dataObject.toString());
		}
	}
	
	
	/**
	 * 
	 * */
	private static void TestClusteringWikipedia12() {
		Set<String> stopwords = new HashSet<String>();
		stopwords.addAll(StopWords.getStopwords("consoantes"));
		stopwords.addAll(StopWords.getStopwords("english"));
		stopwords.addAll(StopWords.getStopwords("vogais"));
		
		//List<Double> list = Arrays.asList(0.0, 0.10, 0.20, 0.30, 0.40, 0.50, 0.60, 0.70, 0.80, 0.90, 1.00); //GSM
		//List<Double> list = Arrays.asList(1.0, 0.99, 0.98, 0.97, 0.96, 0.95, 0.94, 0.93, 0.92, 0.91, 0.9, 0.85, 0.80, 0.70, 0.60, 0.5, 0.4, 0.3, 0.2, 0.1, 0.0); //Epsilon - DBSCAN
		List<Integer> list = Arrays.asList(2, 3, 4, 5, 6, 7, 8); //k - KMeans
		for (int k : list) {
		for (int i = 0; i < 5; i++) {
			long startTime, termSelectionTime, similarityTime, clusteringTime, fmeasureTime, silhouetteTime, entropyTime, purityTime;		
			
			System.out.println("----------------------------------------------------");
			//System.out.println("GSM: " + gsm);
			System.out.println("k: " + k + "\n");
			
			ClusteringProcess process = new ClusteringProcess();
			//process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
			//process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
			
			process.setFeatureSelectionStrategy(new VideoMetaDataSelectionStrategy());
			process.setSimilarityStrategy(new VideoFileSimilarityStrategy());
			
			/*
			process.setFeatureSelectionStrategy(new VideoMetaDataSelectionStrategy());
			process.setSimilarityStrategy(new VideoFileSimilarityStrategy());
			AudioVideoFilesUtility.addDataObjects(process, "data/resVideo/", "video");
			*/
			
			//process.setClusteringStrategy(new BestStarClusteringStrategy(gsm));
			//process.setClusteringStrategy(new CliquesClusteringStrategy(gsm));
			//process.setClusteringStrategy(new StarsClusteringStrategy(gsm));
		 	//process.setClusteringStrategy(new FullStarsClusteringStrategy(gsm));
			//process.setClusteringStrategy(new AgglomerativeHierarchicalClusteringStrategy(0.01, gsm, 1));
			//process.setClusteringStrategy(new DBSCANClusteringStrategy(epsilon, 2));
			//process.setClusteringStrategy(new KmeansClusteringStrategy(k, 200));
			process.setClusteringStrategy(new KmedoidsClusteringStrategy(k, 50, 2, 2));
			 
			// Add data objects (documents!).
			//TextFilesUtility.addDataObjects(process, "data/test03-reuters-top10-data");
			AudioVideoFilesUtility.addDataObjects(process, "data/test05-video-data/", "video");

			// Execution
			
			startTime = System.nanoTime();
			
			
			process.dataObjects = process.featureSelectionStrategy.executeFeatureSelection(process.dataObjects);
			
			termSelectionTime = System.nanoTime();
			termSelectionTime = (termSelectionTime - startTime) / 1000000;
			
			startTime = System.nanoTime();
			process.similarityMatrix = process.similarityStrategy.executeSimilarity(process.dataObjects);
			similarityTime = System.nanoTime();
			similarityTime = (similarityTime - startTime) / 1000000;
			
			//System.out.println("\n" + process.similarityMatrix.toString() + "\n");
			
			startTime = System.nanoTime();
			process.dataClusters = process.clusteringStrategy.executeClustering(process.dataObjects, process.similarityMatrix);
			clusteringTime = System.nanoTime();
			clusteringTime = (clusteringTime - startTime) / 1000000;
			
			// Print clusters
			ClusteringUtility.printClusters(process, "video");
			
			// Write clusters on file
			//ClusteringUtility.writeClusterOfAudioVideoFiles(process, "_", "audio", "audio");
			
			// Analysis
			process.similarityMatrix = process.similarityStrategy
				.executeSimilarity(process.dataObjects);
			
			System.out.printf("\n");
			
			startTime = System.nanoTime();
			process.setAnalysisStrategy(new FmeasureAnalysisStrategy(
				"data/test05-video-analysis/classes.txt", "video"));
			process.analysisStrategy.executeAnalysis(process.dataObjects,
				process.dataClusters);
			fmeasureTime = System.nanoTime();
			fmeasureTime = (fmeasureTime - startTime) / 1000000;
			
			
			startTime = System.nanoTime();
			process.setAnalysisStrategy(new SilhouetteAnalysisStrategy());
			process.analysisStrategy.executeAnalysis(process.dataObjects,
				process.dataClusters, process.similarityMatrix);
			silhouetteTime = System.nanoTime();
			silhouetteTime = (silhouetteTime - startTime) / 1000000;
			
			
			startTime = System.nanoTime();
			process.setAnalysisStrategy(new EntropyAnalysisStrategy(
				"data/test05-video-analysis/classes.txt", "video"));
			process.analysisStrategy.executeAnalysis(process.dataObjects,
				process.dataClusters);
			entropyTime = System.nanoTime();
			entropyTime = (entropyTime - startTime) / 1000000;
			
			
			startTime = System.nanoTime();
			process.setAnalysisStrategy(new PurityAnalysisStrategy(
				"data/test05-video-analysis/classes.txt", "video"));
			process.analysisStrategy.executeAnalysis(process.dataObjects,
				process.dataClusters);
			purityTime = System.nanoTime();
			purityTime = (purityTime - startTime) / 1000000;
			
			/*
			System.out.println("Times");
			System.out.println("Term Selection: " + termSelectionTime + "ms");
			System.out.println("Similarity: " + similarityTime + "ms");
			System.out.println("Clustering: " + clusteringTime + "ms");
			System.out.println("Fmeasure: " + fmeasureTime + "ms");
			System.out.println("Silhouette: " + silhouetteTime + "ms");
			System.out.println("Entropy: " + entropyTime + "ms");
			System.out.println("Purity: " + purityTime + "ms \n");
			*/
		}
		}
	}

	
	/**
	 * 
	 * 
	 * */
	private static void TestClusteringWikipedia13() {
		ClusteringProcess process = new ClusteringProcess();

		Set<String> stopwords = new HashSet<String>();
		stopwords.addAll(StopWords.getStopwords("consoantes"));
		stopwords.addAll(StopWords.getStopwords("english"));
		stopwords.addAll(StopWords.getStopwords("vogais"));

		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.03));

		// Add data objects (documents!).
		TextFilesUtility.addDataObjects(process, "data/wikipedia13");

		process.dataObjects = process.featureSelectionStrategy
				.executeFeatureSelection(process.dataObjects);
		process.similarityMatrix = process.similarityStrategy
				.executeSimilarity(process.dataObjects);
		System.out.print(process.similarityMatrix.toString());
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, process.similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfTextFiles(process, "BestStar", "wikipedia13");

		// Analysis
		// process.setAnalysisStrategy(new
		// FmeasureAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		// process.setAnalysisStrategy(new
		// EntropyAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		//process.setAnalysisStrategy(new PurityAnalysisStrategy("tests/test02-wikipedia13/classes.txt"));
		//process.analysisStrategy.executeAnalysis(process.dataObjects, process.dataClusters);
	}

	/**
	 * 
	 * 
	 * */
	private static void TestClusteringStrategies() {
		int[] dataSetSizes = { 4, 5, 6, 10, 15 };
		//int[] dataSetSizes = { 12 };
		//String[] filenames = { "similarity12" };
		String[] filenames = { "similarity4", "similarity5", "similarity6",
				"similarity10", "similarity15" };

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
			Matrix2D similarityMatrix = new Matrix2D(
					process.getDataObjectCount());
			similarityMatrix.loadMatrix("similarity/tests/" + filenames[i] + ".txt",
					process.dataObjects.size());
			System.out.println(similarityMatrix.toString());

			// -------------------------------------------------------------------------------

			
			 int k = 2; // Number of clusters 
			 int iterations = 2; // Number of iterations
			 KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 1); 
			 //KmedoidsTest(filenames[i], process, similarityMatrix, k, iterations, 2);

			/*
			 * k = 2; // Number of clusters iterations = 2; // Number of
			 * iterations KmeansTest(filenames[i], process, similarityMatrix, k,
			 * iterations);
			 */

			
			 double epsilon = 0.2; int minObjs = 2; // Minimum number of objects per cluster
			 DBSCANTest(filenames[i], process, similarityMatrix, epsilon, minObjs);

			/*
			 * double fuzziness = 2.0; // Normally the fuzziness is set to 2.0 k
			 * = 2; iterations = 2; FuzzyCMeansTest(filenames[i], process,
			 * similarityMatrix, fuzziness, k, iterations);
			 */

			/*
			 * AgglomerativeHierarchicalTest(filenames[i], process,
			 * similarityMatrix, 0.10, 0.70, 1);
			 */

			// BestStarTest(filenames[i], process, similarityMatrix, 0.5);

			// FullStarsTest(filenames[i], process, similarityMatrix, 0.5);

		}
	}


	/**
	 * Test Agglomerative Hierarchical clustering strategy algorithm
	 */
	private static void AgglomerativeHierarchicalTest(String filename,
			ClusteringProcess process, Matrix2D similarityMatrix,
			double thresholdLvl, double minThreshold, int clusteringMethod) {

		// Set and run Agglomerative Hierarchical Clustering Strategy
		process.setClusteringStrategy(new AgglomerativeHierarchicalClusteringStrategy(
			thresholdLvl, minThreshold, clusteringMethod));
		process.dataClusters = process.clusteringStrategy.executeClustering(
			process.dataObjects, similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfTextFiles(process, "Agglomerative", filename);

		// Cluster Analysis
		similarityMatrix.loadMatrix("similarity/" + filename + ".txt",
			process.dataObjects.size()); // re-load similarity matrix
		SilhouetteAnalysisStrategy s = new SilhouetteAnalysisStrategy();
		s.executeAnalysis(process.dataObjects,
			process.dataClusters, similarityMatrix);
		//System.out.println("Silhouette: " + silhouette);
	}

	
	/**
	 * Test FullStar clustering strategy algorithm
	 */
	private static void FullStarsTest(String filename,
			ClusteringProcess process, Matrix2D similarityMatrix, double GSM) {

		// Set and run Full Stars Clustering Strategy
		process.setClusteringStrategy(new FullStarsClusteringStrategy(GSM));
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfTextFiles(process, "FullStars", filename);
	}

	
	/**
	 * Test BestStar clustering strategy algorithm
	 */
	private static void BestStarTest(String filename,
			ClusteringProcess process, Matrix2D similarityMatrix, double GSM) {

		// Set and run Best Star Clustering Strategy
		process.setClusteringStrategy(new BestStarClusteringStrategy(GSM));
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfTextFiles(process, "BestStar", filename);
	}

	
	/**
	 * Test DBSCAN clustering strategy algorithm
	 */
	private static void DBSCANTest(String filename, ClusteringProcess process,
			Matrix2D similarityMatrix, double epsilon, int minObjs) {

		// Set and run DBSCAN Clustering Strategy
		process.setClusteringStrategy(new DBSCANClusteringStrategy(epsilon,
				minObjs));
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, similarityMatrix);

		// Write clusters on file
		ClusteringUtility.writeClusterOfTextFiles(process, "DBSCAN", filename);
	}

	
	/**
	 * Test K-medoids clustering strategy algorithm
	 */
	private static void KmedoidsTest(String filename,
			ClusteringProcess process, Matrix2D similarityMatrix,
			int numClusters, int iterations, int centroidStrategy) {

		// Set and run K-Medoids Clustering Strategy
		process.setClusteringStrategy(new KmedoidsClusteringStrategy(
				numClusters, iterations, centroidStrategy, 2));
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, similarityMatrix);

		// Write clusters on file
		if (centroidStrategy == 1)
			ClusteringUtility.writeClusterOfTextFiles(process, "Kmedoids-CentroidStrategy1", filename);
		else if (centroidStrategy == 2)
			ClusteringUtility.writeClusterOfTextFiles(process, "Kmedoids-CentroidStrategy2", filename);
	}

	
	/**
	 * Test K-means clustering strategy algorithm
	 */
	private static void KmeansTest(String filename, ClusteringProcess process,
			Matrix2D similarityMatrix, int numClusters, int iterations) {

		// Set and run K-Means Clustering Strategy
		process.setClusteringStrategy(new KmeansClusteringStrategy(numClusters,
				iterations));
		process.dataClusters = process.clusteringStrategy.executeClustering(
				process.dataObjects, similarityMatrix);

		ClusteringUtility.writeClusterOfTextFiles(process, "Kmeans", filename);
	}

	
	/**
	 * Test clustering over reuters data
	 */
	private static void reutersClustering() {
		ClusteringProcess process = new ClusteringProcess();

		// Setup stopwords.
		Set<String> stopwords = new HashSet<String>();

		// Stopwords for reuters set of files
		stopwords.addAll(StopWords.getStopwords("consoantes"));
		stopwords.addAll(StopWords.getStopwords("english"));
		stopwords.addAll(StopWords.getStopwords("sgml"));
		stopwords.addAll(StopWords.getStopwords("vogais"));

		// Register the strategies that will be used.
		process.setFeatureSelectionStrategy(new TermSelectionStrategy(stopwords));
		process.setSimilarityStrategy(new TextFileFuzzyMeansSimilarityStrategy());
		// process.setClusteringStrategy(new CliquesClusteringStrategy(1.0));
		// process.setClusteringStrategy(new StarsClusteringStrategy(1.0));
		// process.setClusteringStrategy(new FullStarsClusteringStrategy(1.0));
		process.setClusteringStrategy(new BestStarClusteringStrategy(0.05));

		// Register the observers.
		process.addObserver(new ConsoleObserver());
		// process.addObserver(new ClusteringWindow(process));

		// Run this code to extract reuters documents from .sgm files located in data/reuters-sgm
		try {
			ReutersSGMtoTXT.main(null);
		} catch (IOException e1) {
			System.out.println("ExtractReuters.main : reuters documents extracted to data/reuters-extracted.");
			e1.printStackTrace();
		}

		// Add data objects (documents!).
		TextFilesUtility.addDataObjects(process, "data/reuters-extracted");

		process.executeClusteringProcess();
	}
}
