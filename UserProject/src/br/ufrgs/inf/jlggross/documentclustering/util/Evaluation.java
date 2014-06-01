package br.ufrgs.inf.jlggross.documentclustering.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluation {
	public static void main(String args[]) throws IOException {
		File results = new File("results");
		for (File result : results.listFiles()) {
			BufferedReader reader = new BufferedReader(new FileReader(result));
			Map<String, List<String>> classes = new HashMap<String, List<String>>();
			List<List<String>> clusters = new ArrayList<List<String>>();
			
			// Create the clusters and classes!
			String line = "";
			while (null != (line = reader.readLine())) {
				if (!line.isEmpty()) {
					int count = Integer.valueOf(line);
					List<String> cluster = new ArrayList<String>();
					for (int i = 0; i < count; i++) {
						line = reader.readLine();
						String[] parse = line.split("\\\\");
						String className = parse[parse.length-2];
						String docName = parse[parse.length-1];
						List<String> clazz = classes.get(className);
						if (clazz == null) {
							clazz = new ArrayList<String>();
							classes.put(className, clazz);
						}
						clazz.add(docName);
						cluster.add(docName);
					}
					clusters.add(cluster);
				}
			}
			reader.close();
			
			// Calculate stuff.
			// For each cluster, gets the most similar class and calculate stuff.
			BufferedWriter writer = new BufferedWriter(new FileWriter("results/_metrics_" + result.getName()));
			double fmeasure = 0.0;
			double precision = 0.0;
			double recall = 0.0;
			int clusterIndex = 0;
			int clustersSize = clusters.size();
			for (List<String> cluster : clusters) {
				String clazz = "";
				int classIntersec = 0;
				for (String className : classes.keySet()) {
					int count = 0;
					List<String> classObjects = classes.get(className);
					for (String doc : cluster) {
						if (classObjects.contains(doc)) {
							count++;
						}
					}
					if (count > classIntersec) {
						classIntersec = count;
						clazz = className;
					}
				}
				
				if (!clazz.equals("")) {
					double p = precision(cluster, classes.get(clazz));
					double r = recall(cluster, classes.get(clazz));
					double f = fmeasure(p, r);
					writer.append(String.format("Cluster %d\n Precision:\t%.2f\n Recall:\t%.2f\n F-measure:\t%.2f\n",
							clusterIndex, p, r, f));
					
					precision += p;
					recall += r;
					fmeasure += f;
				}
				clusterIndex++;
			}
			precision /= clustersSize;
			recall /= clustersSize;
			fmeasure /= clustersSize;
			writer.append(String.format("\nMacroaverage\n Precision:\t%.2f\n Recall:\t%.2f\n F-measure:\t%.2f\n",
					precision, recall, fmeasure));
			writer.close();
		}
	}
	
	private static double fmeasure(double precision, double recall) {
		return 2 * ((precision * recall) / (precision + recall));
	}
	
	private static double precision(List<String> cluster, List<String> clazz) {
		int count = 0;
		
		for (String object : cluster) {
			if (clazz.contains(object)) {
				count++;
			}
		}
		
		return (double) count / cluster.size();
	}
	
	private static double recall(List<String> cluster, List<String> clazz) {
		int count = 0;
		
		for (String object : cluster) {
			if (clazz.contains(object)) {
				count++;
			}
		}
		
		return (double) count / clazz.size();
	}
}
