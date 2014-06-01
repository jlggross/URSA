package br.ufrgs.inf.jlggross.documentclustering.strategies.similarity;

import java.util.ArrayList;
import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataFeature;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;
import br.ufrgs.inf.jlggross.clustering.strategy.SimilarityStrategy;
import br.ufrgs.inf.jlggross.documentclustering.Document;
import br.ufrgs.inf.jlggross.documentclustering.Term;

public class FuzzyMeansSimilarityStrategy extends SimilarityStrategy {
	private final double EPSILON = 0.0000000000000001;

	@Override
	public Matrix2D executeSimilarity(List<DataObject> dataObjects) {
		int objectsCount = dataObjects.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);
		
		Document doc1 = null;
		Document doc2 = null;
		
		List<Term> smallerCommonTerms = new ArrayList<Term>();
		List<Term> biggerCommonTerms = new ArrayList<Term>();
		
		int p = 0;
		for (int i = 0; i < objectsCount; i++) {
			for (int j = i + 1; j < objectsCount; j++) {
				doc1 = (Document) dataObjects.get(i);
				doc2 = (Document) dataObjects.get(j);
				
				Document smaller = (doc1.getFeatureList().size() > doc2.getFeatureList().size())? doc2 : doc1;
				Document bigger = (doc1.getFeatureList().size() > doc2.getFeatureList().size())? doc1 : doc2;

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
