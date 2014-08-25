package clusteringstrategies.implementation.featureselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import clusteringstrategies.core.FeatureSelectionStrategy;
import datastructures.core.DataFeature;
import datastructures.core.DataObject;
import datastructures.core.Term;
import datastructures.implementations.datatypes.TextFile;

public class TermSelectionStrategy extends FeatureSelectionStrategy {
	private Set<String> stopWords;
	private int processedDocuments;
	private int totalDocuments;
	
	public TermSelectionStrategy(Set<String> stopWords) {
		this.stopWords = stopWords;
	}
	
	public int getProcessedDocuments() {
		return this.processedDocuments;
	}
	
	public int getTotalDocuments() {
		return this.totalDocuments;
	}

	@Override
	public List<DataObject> executeFeatureSelection(List<DataObject> dataObjects) {
		this.processedDocuments = 0;
		this.totalDocuments = dataObjects.size();
		
		for (DataObject dataObject : dataObjects) {
			TextFile doc = (TextFile) dataObject;
			doc.clearFeatureList();
			this.tokenize(doc);
			this.calculateRelative(doc);
			//this.dumpSelectedTerms(doc);
			this.setProgress((double)++this.processedDocuments/dataObjects.size());
		}
		
		return dataObjects;
	}
	
	private void tokenize(TextFile doc) {
		final HashMap<String, Integer> bagOfWords = new HashMap<String, Integer>();
		String[] tokens = doc.getContent().toLowerCase()
				.split("[^\\p{Alnum}]");
				//.split("[\\s]|[.][^0-9]|[,][^0-9]|[<]|[>]|[\"]|[-]|[(]|[)]|[;]|[:]|[!]|[?]");
				//.split("[[(]|[)]|[.]|[,]|[;]|[:]|[\"]|[/]|[<]|[>]|[-]]+[[\\s]|[\\n]]*|[\\s]+[[(]|[)]|[.]|[,]|[;]|[:]|[\"]|[/]|[<]|[>]|[-]]*[\\s]*");
		for (String token : tokens) {
			if (!token.isEmpty()) {
				Integer frequency = bagOfWords.get(token);
				if (frequency == null)
					frequency = 0;
				frequency++;
				bagOfWords.put(token, frequency);
			}
		}
		
		List<String> terms = new ArrayList<String>(bagOfWords.keySet());
		Collections.sort(terms, new Comparator<String>() {
			@Override
	        public int compare(String term1, String term2) {
	            return bagOfWords.get(term2) - bagOfWords.get(term1);
	        }
	    });
		
		int count = 500;
		for (String term : terms) {
			if (!this.stopWords.contains(term)) {
				doc.addFeature(new Term(term, bagOfWords.get(term)));
				count--;
				if (count == 0) {
					break;
				}
			}
		}
	}
	
	private void calculateRelative(TextFile doc) {
		List<DataFeature> features = doc.getFeatureList();
		
		double featuresCount = 0;
		for (DataFeature feature : features) {
			Term term = (Term) feature;
			featuresCount += term.getAbsoluteFrequency();
		}

		for (DataFeature feature : features) {
			Term term = (Term) feature;
			double absFrequency = term.getAbsoluteFrequency();
			term.setRelativeFrequency(absFrequency/featuresCount);
		}
	}
	
	/*
	private void dumpSelectedTerms(TextFile doc) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("data/selected_terms.txt", true));
			for (DataFeature feature : doc.getFeatureList()) {
				Term term = (Term) feature;
				writer.append(term.getValue() + "(" + term.getAbsoluteFrequency() + ")").append("\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

}
