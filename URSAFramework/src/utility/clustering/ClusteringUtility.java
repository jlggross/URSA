package utility.clustering;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import main.ClusteringProcess;
import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.implementations.datatypes.AudioMediaFile;
import datastructures.implementations.datatypes.TextFile;
import datastructures.implementations.datatypes.VideoMediaFile;

public class ClusteringUtility {

	/**
	 * Definition: Write the calculated clusters to a file.
	 * 
	 * @param process : the object for the clustering process.
	 * @param strategy : the strategy used in the clustering.
	 * @param identifier : the identifier for this clustering result, besides
	 * the strategy used.
	 */
	public static void writeClusterOfTextFiles(ClusteringProcess process, String strategy, 
			String identifier) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"output/clusters-" + identifier + "-" + strategy + ".txt"));
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
	 * Definition: Write the calculated clusters to a file.
	 * 
	 * @param process : the object for the clustering process.
	 * @param strategy : the strategy used in the clustering.
	 * @param identifier : the identifier for this clustering result, besides
	 * the strategy used.
	 * @param filetype : indicates the filetype (audio or video).
	 */
	public static void writeClusterOfAudioVideoFiles(ClusteringProcess process, String strategy, 
			String identifier, String filetype) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"output/clusters-" + identifier + "-" + strategy + ".txt"));
			String s;
			System.out.println("Clusters: ");
			for (DataCluster cluster : process.getDataClusters()) {
				s = Integer.toString(cluster.getDataObjects().size()) + "\n";
				System.out.printf(s);
				writer.append(s);

				for (DataObject object : cluster.getDataObjects()) {
					
					if (filetype == "audio") {
						AudioMediaFile a = (AudioMediaFile) object;
						s = a.getArtist() + " - " + a.getAlbum() + " - " + a.getTitle() + "\n";
					}
					else if (filetype == "video") {
						VideoMediaFile v = (VideoMediaFile) object;
						s = v.getTitle() + "\n";
					} else {
						throw new RuntimeException("ClusteringUtility.java : filetype should be 'audio' or 'video'.");
					}
					
					System.out.printf(s);
					writer.append(s);
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
