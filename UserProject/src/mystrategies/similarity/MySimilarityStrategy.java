package mystrategies.similarity;

import java.util.List;

import clusteringstrategies.core.SimilarityStrategy;
import datastructures.core.DataObject;
import datastructures.core.Matrix2D;

public class MySimilarityStrategy extends SimilarityStrategy {

	/**
	 * Definition: My similarity core algorithm.
	 * 
	 * @param dataObjects : list of data objects.
	 */
	public Matrix2D executeSimilarity(List<DataObject> dataObjects) {
		int objectsCount = dataObjects.size();
		Matrix2D similarityMatrix = new Matrix2D(objectsCount);
		
		// TODO
		
		return similarityMatrix;
	}
}
