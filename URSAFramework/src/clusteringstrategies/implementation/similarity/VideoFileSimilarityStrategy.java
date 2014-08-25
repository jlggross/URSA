package clusteringstrategies.implementation.similarity;

import java.util.List;

import clusteringstrategies.core.SimilarityStrategy;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.core.MetaData;
import datastructures.implementations.datatypes.VideoMediaFile;

public class VideoFileSimilarityStrategy extends SimilarityStrategy {

	@Override
	public Matrix2D executeSimilarity(List<DataObject> dataObjects) {
		int objectsCount = dataObjects.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);
		
		VideoMediaFile video1 = null;
		VideoMediaFile video2 = null;
		
		int p = 0;
		for (int i = 0; i < objectsCount; i++) {
			for (int j = i + 1; j < objectsCount; j++) {
				video1 = (VideoMediaFile) dataObjects.get(i);
				video2 = (VideoMediaFile) dataObjects.get(j);
				
				int similarValues = 0;
				
				int featureListSize = video1.getFeatureList().size();
				for (int k = 0; k < featureListSize; k++) {
					MetaData video1MetaData = (MetaData) video1.getFeatureList().get(k);
					MetaData video2MetaData = (MetaData) video2.getFeatureList().get(k);
					
					if (video1MetaData.getId().equals("mediaDuration") || 
						video1MetaData.getId().equals("mediaFileSize") ||
						video1MetaData.getId().equals("mediaBitrate")  ||
						video1MetaData.getId().equals("videoFramerate") ||
						video1MetaData.getId().equals("timebase")) {
						double v1 = Double.valueOf(video1MetaData.getValue()).doubleValue();
						double v2 = Double.valueOf(video2MetaData.getValue()).doubleValue();
						double v3;
						if (v1 >= v2)
							v3 = ((v1 / v2) - 1) * 100;
						else
							v3 = ((v2 / v1) - 1) * 100;
						
						if (v3 > 5.0)
							similarValues++;
					}
					
					else if (video1MetaData.getId().equals("numberStreams") || 
						video1MetaData.getId().equals("videoWidth") ||
						video1MetaData.getId().equals("videoHeight")) {
						int v1 = Integer.valueOf(video1MetaData.getValue()).intValue();
						int v2 = Integer.valueOf(video2MetaData.getValue()).intValue();
						if (v1 == v2)
							similarValues++;
					}

					else if (video1MetaData.getId().equals("videoCodecType") ||
						video1MetaData.getId().equals("audioCodecType") ||
						video1MetaData.getId().equals("audioLanguage") ||
						video1MetaData.getId().equals("samplerate")) {
						String s1 = video1MetaData.getValue();
						String s2 = video1MetaData.getValue();
						if (s1.equals(s2))
							similarValues++;
					}
				}
				
				// Calculate the similarity.
				double result = (double) similarValues / featureListSize;
				similarityMatrix.set(i, j, result);
			}
			
			this.setProgress((double)++p/objectsCount);
		}
		
		return similarityMatrix;
	}

}
