package br.ufrgs.inf.jlggross.documentclustering.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.Matrix2D;

public class Silhouette {
	public static void main(String args[]) throws IOException {
		// Load documents.
		List<String> documents = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("silhouette/documents.txt"));
		String line = "";
		while (null != (line = reader.readLine())) {
			if (!line.isEmpty()) {
				String[] parse = line.split("\\\\");
				String docName = parse[parse.length-1];
				documents.add(docName);
			}
		}
		reader.close();
		
		// Load similarity matrix.
		Matrix2D similarity = new Matrix2D(documents.size());
		int x = 0;
		int y = 1;
		reader = new BufferedReader(new FileReader("silhouette/similarity.txt"));
		while (null != (line = reader.readLine())) {
			String[] values = line.split(" ");
			for (String value : values) {
				if (!value.isEmpty()) {
					similarity.set(x, y, Double.valueOf(value.replace(",", ".")));
					y++;
				}
			}
			x++;
			y = x + 1;
		}
		reader.close();
		
		// Load clusters.
		List<List<String>> clusters = new ArrayList<List<String>>();
		reader = new BufferedReader(new FileReader("silhouette/clusters.txt"));
		while (null != (line = reader.readLine())) {
			if (!line.isEmpty()) {
				int count = Integer.valueOf(line);
				List<String> cluster = new ArrayList<String>();
				for (int i = 0; i < count; i++) {
					line = reader.readLine();
					String[] parse = line.split("\\\\");
					String docName = parse[parse.length-1];
					cluster.add(docName);
				}
				clusters.add(cluster);
			}
		}
		reader.close();
		
		// Calculate silhouettes.
		double silhouette = 0.0;
		BufferedWriter writer = new BufferedWriter(new FileWriter("silhouette/results.txt"));
		for (List<String> cluster : clusters) {
			double clusterSi = 0.0;
			int count = 0;
			for (String doc : cluster) {
				
				// Compare with docs in the same cluster.
				double ai = 0.0;
				for (String doc2 : cluster) {
					if (!doc2.equals(doc)) {
						ai += similarity.get(documents.indexOf(doc), documents.indexOf(doc2));
					}
				}
				ai /= (cluster.size() - 1);
				
				// Finds the closest neighbor cluster.
				double bi = 0.0;
				//List<String> closestCluster = null;
				for (List<String> cluster2 : clusters) {
					if (!cluster2.equals(cluster)) {
						double biAux = 0.0;
						for (String doc2 : cluster2) {
							biAux += similarity.get(documents.indexOf(doc), documents.indexOf(doc2));
						}
						biAux /= cluster.size();
						if (biAux > bi) {
							bi = biAux;
							//closestCluster = cluster2;
						}
					}
				}

				double si = 0.0;
				if (ai > bi) {
					si = 1.0 - (bi/ai);
				} else if (ai < bi) {
					si = (ai/bi) - 1.0;
				}
				writer.append(String.format("%s = %.2f\n", doc, si));
				clusterSi += si;
				count++;
			}
			clusterSi /= count;
			writer.append(String.format("Cluster = %.2f\n", clusterSi));
			if (clusterSi < 0) {
				writer.append("NEGATIVE\n\n");
			} else if (clusterSi > 0) {
				writer.append("POSITIVE\n\n");
			} else {
				writer.append("ZERO\n\n");
			}
			silhouette += clusterSi;
		}
		silhouette /= clusters.size();
		writer.append(String.format("Overall = %.2f\n", silhouette));
		writer.close();
	}
}
