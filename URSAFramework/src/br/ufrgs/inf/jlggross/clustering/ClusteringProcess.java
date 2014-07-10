package br.ufrgs.inf.jlggross.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import br.ufrgs.inf.jlggross.clustering.strategy.AnalysisStrategy;
import br.ufrgs.inf.jlggross.clustering.strategy.BaseStrategy;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;
import br.ufrgs.inf.jlggross.clustering.strategy.FeatureSelectionStrategy;
import br.ufrgs.inf.jlggross.clustering.strategy.SimilarityStrategy;

/**
 * -----------------------------------------------------------------------------------------------
 * 
 * Main class of the Clustering Framework. Represents the clustering process
 * and must be configured with implementations of FeatureSelectionStrategy,
 * SimilarityStrategy and ClusteringStrategy.
 * 
 * @author Guilherme Haag Ribacki (2013)
 * @author João Luiz Grave Gross (2014)
 * 
 * -----------------------------------------------------------------------------------------------
 */
public class ClusteringProcess extends Observable implements Runnable, Observer {
	private final int TOTAL_STEPS = 3;
	private int processedSteps;
	private double stepProgress;
	
	//These three are public just for testing purposes - they must be private 
	public FeatureSelectionStrategy featureSelectionStrategy;
	public SimilarityStrategy similarityStrategy;
	public ClusteringStrategy clusteringStrategy;
	public AnalysisStrategy analysisStrategy;
	
	//These three are public just for testing purposes - they must be private
	public List<DataObject> dataObjects;
	public Matrix2D similarityMatrix;
	public List<DataCluster> dataClusters;
	
	public ClusteringProcess() {
		this.dataObjects = new ArrayList<DataObject>();
	}
	
	public void setFeatureSelectionStrategy(FeatureSelectionStrategy strategy) {
		this.featureSelectionStrategy = strategy;
		if (this.featureSelectionStrategy != null)
			this.featureSelectionStrategy.addObserver(this);
	}
	
	public void setSimilarityStrategy(SimilarityStrategy strategy) {
		this.similarityStrategy = strategy;
		if (this.similarityStrategy != null)
			this.similarityStrategy.addObserver(this);
	}
	
	public void setClusteringStrategy(ClusteringStrategy strategy) {
		this.clusteringStrategy = strategy;
		if (this.clusteringStrategy != null)
			this.clusteringStrategy.addObserver(this);
	}
	
	public void setAnalysisStrategy(AnalysisStrategy strategy) {
		this.analysisStrategy = strategy;
		if (this.analysisStrategy != null)
			this.analysisStrategy.addObserver(this);
	}
	
	public void addDataObject(DataObject dataObject) {
		this.dataObjects.add(dataObject);
	}
	
	public int getDataObjectCount() {
		return this.dataObjects.size();
	}
	
	public DataObject[] getDataObjectsArray() {
		return Arrays.copyOf(this.dataObjects.toArray(), this.dataObjects.size(), DataObject[].class);
	}
	
	public List<DataObject> getDataObjects() {
		return this.dataObjects;
	}
	
	public List<DataCluster> getDataClusters() {
		return this.dataClusters;
	}
	
	public Matrix2D getSimilarityMatrix() {
		return this.similarityMatrix;
	}
	
	public int getTotalSteps() {
		return this.TOTAL_STEPS;
	}
	
	public int getProcessedSteps() {
		return this.processedSteps;
	}
	
	public void setProcessedSteps(int processedSteps) {
		this.processedSteps = processedSteps;
		if (this.processedSteps < 3)
			this.stepProgress = 0.0;
		this.setChanged();
		this.notifyObservers();
	}
	
	public double getStepProgress() {
		return this.stepProgress;
	}
	
	public void update(Observable o, Object arg) {
		BaseStrategy strategy = (BaseStrategy) o;
		this.stepProgress = strategy.getProgress();
		this.setChanged();
		this.notifyObservers();
	}

	public void executeClusteringProcess() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	public void run() {
		System.out.println("DataObjects Qty: " + dataObjects.size());
		if (this.dataObjects.size() > 0) {
			this.setProcessedSteps(0);
			this.dataObjects = this.featureSelectionStrategy.executeFeatureSelection(this.dataObjects);
			this.setProcessedSteps(1);
			this.similarityMatrix = this.similarityStrategy.executeSimilarity(this.dataObjects);			
			this.setProcessedSteps(2);			
			this.dataClusters = this.clusteringStrategy.executeClustering(this.dataObjects, similarityMatrix);
			this.setProcessedSteps(3);
		} else {
			// Executed all steps for 0 data objects (no-op).
			this.processedSteps = this.TOTAL_STEPS;
			this.setChanged();
			this.notifyObservers();
		}
		
	}

}

