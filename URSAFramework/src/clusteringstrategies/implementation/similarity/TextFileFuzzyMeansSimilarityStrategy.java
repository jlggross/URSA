package clusteringstrategies.implementation.similarity;

import java.util.ArrayList;
import java.util.List;

import clusteringstrategies.core.SimilarityStrategy;
import datastructures.core.DataFeature;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;
import datastructures.core.Term;
import datastructures.implementations.datatypes.TextFile;

/* -----------------------------------------------------------------------------------------------
 *  
 * TextFile FuzzyMeans Similarity Strategy
 * 
 * In general terms, checks how many words two objects share in common to calculate the similarity
 * between them.
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class TextFileFuzzyMeansSimilarityStrategy extends SimilarityStrategy {
	
	private final double EPSILON = 0.0000000000000001;

	/**
	 * Definition: TextFile Similarity core algorithm.
	 * 
	 * @param dataObjects : list of data objects.
	 */
	public Matrix2D executeSimilarity(List<DataObject> dataObjects) {
		int objectsCount = dataObjects.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);
		
		TextFile doc1 = null;
		TextFile doc2 = null;
		
		List<Term> smallerCommonTerms = new ArrayList<Term>();
		List<Term> biggerCommonTerms = new ArrayList<Term>();
		
		int p = 0;
		for (int i = 0; i < objectsCount; i++) {
			for (int j = i + 1; j < objectsCount; j++) {
				doc1 = (TextFile) dataObjects.get(i);
				doc2 = (TextFile) dataObjects.get(j);
				
				TextFile smaller = (doc1.getFeatureList().size() > doc2.getFeatureList().size())? doc2 : doc1;
				TextFile bigger = (doc1.getFeatureList().size() > doc2.getFeatureList().size())? doc1 : doc2;

				smallerCommonTerms.clear();
				biggerCommonTerms.clear();
				for (DataFeature feature1 : smaller.getFeatureList()) {
					for (DataFeature feature2 : bigger.getFeatureList()) {
						if (feature1.equals(feature2)) {
							smallerCommonTerms.add((Term) feature1);
							biggerCommonTerms.add((Term) feature2);
						}
					}
				}
				int commonTerms = smallerCommonTerms.size();
				
				double wordWeightMax = 0.0;
				double wordWeightMin = 0.0;
				double negationWordWeightMax = 0.0;
				double negationWordWeightMin = 0.0;
				double c1 = 0.0;
				double c2 = 0.0;
				double c3 = 0.0;
				double c4 = 0.0;
				double m1 = 0.0;
				double m2 = 0.0;
				double result = 0.0;
				
				// Calculate the similarity.
				for (int x = 0; x < commonTerms; x++) {
					wordWeightMax = biggerCommonTerms.get(x).getRelativeFrequency();
					wordWeightMin = smallerCommonTerms.get(x).getRelativeFrequency();
					negationWordWeightMax = 1 - wordWeightMax;
					negationWordWeightMin = 1 - wordWeightMin;
					
					c1 = (wordWeightMin == 0)? 1 : wordWeightMax / wordWeightMin;
					c2 = (wordWeightMax == 0)? 1 : wordWeightMin / wordWeightMax;
					c3 = (negationWordWeightMin == 0)? 1 : negationWordWeightMax / negationWordWeightMin;
					c4 = (negationWordWeightMax == 0)? 1 : negationWordWeightMin / negationWordWeightMax;
				
					m1 = Math.min(Math.min(c1, c2), 1);
					m2 = Math.min(Math.min(c3, c4), 1);
					
					result += 0.5 * (m1 + m2);
				}
				
				result = result / (smaller.getFeatureList().size() + bigger.getFeatureList().size() - commonTerms + this.EPSILON);
				
				similarityMatrix.set(i, j, result);
			}
			
			this.setProgress((double)++p/objectsCount);
		}
		
		return similarityMatrix;
	}

}
