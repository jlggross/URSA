package br.ufrgs.inf.jlggross.documentclustering.strategies.clustering;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataCluster;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.ClusteringStrategy;

public class OldBestStarClusteringStrategy extends ClusteringStrategy {
	private double threshold;
	
	public OldBestStarClusteringStrategy(double threshold) {
		this.threshold = threshold;
	}
	
	// For each object, tries to make a new cluster:
	//	If the object is not allocated, create a new cluster and add whoever should be there;
	//	Else, tries to create a new cluster with it but deletes the new cluster in case no other object is added to it.
	
	@Override
	public List<DataCluster> executeClustering(List<DataObject> dataObjects, Matrix2D similarityMatrix) {
		List<DataCluster> dataClusters = new ArrayList<DataCluster>();
		List<DataObject> allocatedObjects = new ArrayList<DataObject>();
		List<DataObject> centerObjects = new ArrayList<DataObject>();
		
		// For each object...
		int totalDocs = dataObjects.size();
		for (int i = 0; i < totalDocs; i++) {
			DataObject currentObject = dataObjects.get(i);
			
			if (!allocatedObjects.contains(currentObject)) {
				// The current object is not allocated yet, so starts a new cluster.
				DataCluster star = new DataCluster();
				star.addDataObject(currentObject);
				allocatedObjects.add(currentObject);
				centerObjects.add(currentObject);
				
				// For each other object, checks the similarity between them (except centers!).
				for (int j = i+1; j < totalDocs; j++) {
					DataObject anotherObject = dataObjects.get(j);
					if (!centerObjects.contains(anotherObject)) {
						double similarity = similarityMatrix.get(i, j);
						if (similarity >= this.threshold) {
							if (!allocatedObjects.contains(anotherObject)) {
								// The object is not allocated yet - add it to this cluster.
								allocatedObjects.add(anotherObject);
								star.addDataObject(anotherObject);
							} else {
								// The object is already allocated - moves it if similarity is higher.
								DataCluster anotherCluster = this.getClusterOfObject(dataClusters, anotherObject);
								DataObject center = anotherCluster.getDataObjects().get(0);
								int centerIndex = dataObjects.indexOf(center);
								if (similarity > similarityMatrix.get(j, centerIndex)) {
									anotherCluster.getDataObjects().remove(anotherObject);
									star.addDataObject(anotherObject);
								}
							}
						}
					}
				}
				dataClusters.add(star);
			} else {
				// The object is already allocated but tries to create a new cluster with it.
				DataCluster star = new DataCluster();
				star.addDataObject(currentObject);
				centerObjects.add(currentObject);
				
				// Gets similarity on the original cluster.
				DataCluster previousCluster = this.getClusterOfObject(dataClusters, currentObject);
				DataObject previousCenter = previousCluster.getDataObjects().get(0);
				int previousCenterIndex = dataObjects.indexOf(previousCenter);
				double previousSimilarity = similarityMatrix.get(i, previousCenterIndex);
				
				boolean validCluster = false;
				
				// For each other object, checks the similarity between them (except centers!).
				for (int j = i+1; j < totalDocs; j++) {
					DataObject anotherObject = dataObjects.get(j);
					if (!centerObjects.contains(anotherObject)) {
						double similarity = similarityMatrix.get(i, j);
						if (similarity > previousSimilarity) {
							if (!allocatedObjects.contains(anotherObject)) {
								// The object is not allocated yet - add it to this cluster.
								allocatedObjects.add(anotherObject);
								star.addDataObject(anotherObject);
								validCluster = true;
							} else {
								// The object is already allocated - moves it if similarity is higher.
								DataCluster anotherCluster = this.getClusterOfObject(dataClusters, anotherObject);
								DataObject center = anotherCluster.getDataObjects().get(0);
								int centerIndex = dataObjects.indexOf(center);
								if (similarity > similarityMatrix.get(j, centerIndex)) {
									anotherCluster.getDataObjects().remove(anotherObject);
									star.addDataObject(anotherObject);
									validCluster = true;
								}
							}
						}
					}
				}
				
				// If cluster had only the center object, remove it and leave the object on the original cluster.
				if (validCluster) {
					previousCluster.getDataObjects().remove(currentObject);
					dataClusters.add(star);
				} else {
					centerObjects.remove(currentObject);
				}
			}
			//this.setProgress((double)i/totalDocs);
		}
		
		/*
		// Remove clusters of one document.
		List<DataCluster> realClusters = new ArrayList<DataCluster>();
		for (DataCluster cluster : dataClusters) {
			if (cluster.getDataObjects().size() > 1) {
				realClusters.add(cluster);
			}
		}
		
		return realClusters;
		*/
		
		return dataClusters;
	}
	
	private DataCluster getClusterOfObject(List<DataCluster> dataClusters, DataObject object) {
		DataCluster clusterOfObject = null;
		
		for (DataCluster cluster : dataClusters) {
			if (cluster.getDataObjects().contains(object)) {
				clusterOfObject = cluster;
				break;
			}
		}
		
		return clusterOfObject;
	}

}

