package clusteringstrategies.implementation.similarity;

import java.util.List;

import clusteringstrategies.core.SimilarityStrategy;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.core.MetaData;
import datastructures.implementations.datatypes.AudioMediaFile;

/* -----------------------------------------------------------------------------------------------
 *  
 * AudioFile Similarity Strategy
 * 
 * The similarity is calculated by comparing two objects. The similarity gets higher for every 
 * information that both objects share equally.
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class AudioFileSimilarityStrategy extends SimilarityStrategy {

	private int artistWeight = 3;
	private int albumWeight = 2;
	private int genreWeight = 1;
	
	/**
	 * Definition: AudioFile Similarity core algorithm.
	 * 
	 * @param dataObjects : list of data objects.
	 */
	public Matrix2D executeSimilarity(List<DataObject> dataObjects) {
		int objectsCount = dataObjects.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);
		
		AudioMediaFile audio1 = null;
		AudioMediaFile audio2 = null;
		
		int p = 0;
		for (int i = 0; i < objectsCount; i++) {
			for (int j = i + 1; j < objectsCount; j++) {
				audio1 = (AudioMediaFile) dataObjects.get(i);
				audio2 = (AudioMediaFile) dataObjects.get(j);
				
				int similarValues = 0;
				
				int featureListSize = audio1.getFeatureList().size();
				for (int k = 0; k < featureListSize; k++) {
					MetaData audio1MetaData = (MetaData) audio1.getFeatureList().get(k);
					MetaData audio2MetaData = (MetaData) audio2.getFeatureList().get(k);
					
					if (audio1MetaData.getId().equals("mediaDuration") || 
						audio1MetaData.getId().equals("mediaFileSize") ||
						audio1MetaData.getId().equals("mediaBitrate")) {
						double a1 = Double.valueOf(audio1MetaData.getValue()).doubleValue();
						double a2 = Double.valueOf(audio2MetaData.getValue()).doubleValue();
						double a3;
						if (a1 >= a2)
							a3 = ((a1 / a2) - 1) * 100;
						else
							a3 = ((a2 / a1) - 1) * 100;
						
						if (a3 > 5.0)
							similarValues++;
					}
					
					else if (audio1MetaData.getId().equals("album")) {
						String a1 = audio1MetaData.getValue();
						String a2 = audio2MetaData.getValue();
						if (a1.equals(a2)) {
							similarValues++;
							similarValues += this.albumWeight;
						}
					} 
					
					else if (audio1MetaData.getId().equals("artist")) {
						String a1 = audio1MetaData.getValue();
						String a2 = audio2MetaData.getValue();
						if (a1.equals(a2)) {
							similarValues++;
							similarValues += this.artistWeight;
						}
					} 
					
					else if (audio1MetaData.getId().equals("genre")) {
						String a1 = audio1MetaData.getValue();
						String a2 = audio2MetaData.getValue();
						if (a1.equals(a2)) {
							similarValues++;
							similarValues += this.genreWeight;
						}
					} 
					
					else if (audio1MetaData.getId().equals("title")) {
						String a1 = audio1MetaData.getValue();
						String a2 = audio2MetaData.getValue();
						
						if (a1.equals(a2)) {
							similarValues++;
						}
					}
					
					else if (audio1MetaData.getId().equals("track") ||
						audio1MetaData.getId().equals("date")) {
						int v1 = Integer.valueOf(audio1MetaData.getValue()).intValue();
						int v2 = Integer.valueOf(audio2MetaData.getValue()).intValue();
						if (v1 == v2)
							similarValues++;
					}
					
				}
				
				// Calculate the similarity.
				double result = (double) similarValues / (featureListSize + this.albumWeight + this.artistWeight + this.genreWeight);
				similarityMatrix.set(i, j, result);
			}
			
			this.setProgress((double)++p/objectsCount);
		}
		
		return similarityMatrix;
	}
}
