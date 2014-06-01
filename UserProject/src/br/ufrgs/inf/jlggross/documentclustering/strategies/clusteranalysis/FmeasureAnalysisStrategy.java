package br.ufrgs.inf.jlggross.documentclustering.strategies.clusteranalysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;

public class FmeasureAnalysisStrategy {

	// TODO Try to pass some classes to test it!
	public double executeAnalysis(List<DataObject> objects, List<DataCluster> dataClusters, List<DataCluster> dataClasses) {
		double fmeasure = 0.0;
		int iterations = dataClusters.size();

		// For each cluster, finds out what class have most of its objects.
		for (DataCluster cluster : dataClusters) {
			int classIndex = -1;
			int classIntersec = 0;
			for (int i = 0; i < dataClasses.size(); i++) {
				int count = 0;
				List<DataObject> classObjects = dataClasses.get(i).getDataObjects();
				for (DataObject object : cluster.getDataObjects()) {
					if (classObjects.contains(object)) {
						count++;
					}
				}
				if (count > classIntersec) {
					classIntersec = count;
					classIndex = i;
				}
			}
			
			if (classIndex != -1) {
				fmeasure += this.fmeasure(cluster, dataClasses.get(classIndex));
			}
		}
		
		return fmeasure / iterations;
	}
	
	private double fmeasure(DataCluster cluster, DataCluster clazz) {
		return 2 * ((this.precision(cluster, clazz) * this.recall(cluster, clazz))
				/ (this.precision(cluster, clazz) + this.recall(cluster, clazz)));
	}
	
	private double precision(DataCluster cluster, DataCluster clazz) {
		int count = 0;
		
		for (DataObject object : cluster.getDataObjects()) {
			if (clazz.getDataObjects().contains(object)) {
				count++;
			}
		}
		
		return (double) count / cluster.getDataObjects().size();
	}
	
	private double recall(DataCluster cluster, DataCluster clazz) {
		int count = 0;
		
		for (DataObject object : cluster.getDataObjects()) {
			if (clazz.getDataObjects().contains(object)) {
				count++;
			}
		}
		
		return (double) count / clazz.getDataObjects().size();
	}
	
	public double microaveragePrecision(List<DataCluster> dataClusters, List<DataCluster> dataClasses) {
		return 0.0;
	}
	
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

}
