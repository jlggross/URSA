package br.ufrgs.inf.jlggross.clustering.strategy;

import java.util.List;

import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.Matrix2D;

public abstract class SimilarityStrategy extends BaseStrategy {

	public abstract Matrix2D executeSimilarity(List<DataObject> dataObjects);
	
}
