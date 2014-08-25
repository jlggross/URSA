package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import main.ClusteringProcess;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.implementations.datatypes.TextFile;

public class ConsoleObserver implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		ClusteringProcess process = (ClusteringProcess) o;
		//System.out.println(String.format("Completed steps: %d/%d", process.getProcessedSteps(), process.getTotalSteps()));
		switch (process.getProcessedSteps()) {
			case 0:
				break;
			case 1:
				// Write file with documents and their terms.
				this.writeDataObjects(process);
				break;
			case 2:
				// Write file with similarity matrix.
				this.writeSimilarityMatrix(process);
				break;
			case 3:
				// Write file with clusters configuration.
				this.writeClusters(process);
				break;
			default:
		}
		
		if (process.getProcessedSteps() == 3) {
			List<DataCluster> clusters = process.getDataClusters();
			List<DataObject> objects = process.getDataObjects();
			Matrix2D similarities = process.getSimilarityMatrix();
			
			double cohesion = 0.0;
			int clusterIndex = 0;
			for (DataCluster cluster : clusters) {
				double clusterCohesion = 0.0;
				for (int i = 0; i < cluster.getDataObjects().size(); i++) {
					for (int j = i + 1; j < cluster.getDataObjects().size(); j++) {
						clusterCohesion += similarities.get(objects.indexOf(cluster.getDataObjects().get(i)), objects.indexOf(cluster.getDataObjects().get(j)));
					}
				}
				clusterCohesion /= cluster.getDataObjects().size();
				System.out.println(String.format("Cluster %d cohesion: %.2f", clusterIndex++, clusterCohesion));
				cohesion += clusterCohesion;
			}
			cohesion /= clusterIndex;
			System.out.println(String.format("Overall cohesion: %.2f", cohesion));
		}
		
		/*if (process.getProcessedSteps() < 3) {
			// If a step is currently running, print its current progress.
			//System.out.println(String.format(" Current step progress: %.2f%%", process.getStepProgress()*100));
		} else {
			// If all steps are done, print the cluster configuration.
			List<DataCluster> clusters = process.getDataClusters();
			for (DataCluster cluster : clusters) {
				cluster.print();
			}
			int clusteredObjects = 0;
			for (DataCluster cluster : clusters) {
				clusteredObjects += cluster.getDataObjects().size();
				//System.out.println("Cluster size: " + cluster.getDataObjects().size());
			}
			double average = (double) clusteredObjects / clusters.size();
			System.out.println("Average documents per cluster: " + average);
			System.out.println("Total clusters: " + clusters.size());
			System.out.println("Total objects: " + process.getDataObjectCount());
			System.out.println("Total clustered objects: " + clusteredObjects);
			
			/*SilhouetteAnalysisStrategy analysis = new SilhouetteAnalysisStrategy();
			double silh = analysis.executeAnalysis(Arrays.asList(process.getDataObjects()), process.getDataClusters(), process.getSimilarityMatrix());
			System.out.println("Silhouette: " + silh);
			
			FmeasureAnalysisStrategy fmeasure = new FmeasureAnalysisStrategy();
			List<DataCluster> classes = ExtractReuters.getClasses(Arrays.asList(process.getDataObjects()));
			double fm = fmeasure.executeAnalysis(Arrays.asList(process.getDataObjects()), clusters, classes);
			System.out.println("F-measure: " + fm);
			System.out.println("Microaverage recall: " + fmeasure.microaverageRecall(clusters, classes));
		}
		//System.out.println();*/
	}
	
	private void writeDataObjects(ClusteringProcess process) {
		try {
			File file = new File("output/documents/");
			if (!file.exists()) {
				file.mkdirs();
			}
			for (DataObject object : process.getDataObjects()) {
				TextFile document = (TextFile) object;
				String[] title = document.getTitle().split("\\\\");
				BufferedWriter writer = new BufferedWriter(new FileWriter("output/documents/" + title[title.length-1]));
				writer.append(object.toString());
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeSimilarityMatrix(ClusteringProcess process) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("output/similarity.txt"));
			writer.append(process.getSimilarityMatrix().toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeClusters(ClusteringProcess process) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("output/clusters.txt"));
			for (DataCluster cluster : process.getDataClusters()) {
				writer.append(cluster.getDataObjects().size() + "\n");
				for (DataObject object : cluster.getDataObjects()) {
					TextFile doc = (TextFile) object;
					writer.append(doc.getTitle()).append("\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
