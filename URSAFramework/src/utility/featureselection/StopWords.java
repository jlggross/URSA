package utility.featureselection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopWords {

	public static List<String> getStopwords(String wordsCollection) {
		List<String> stopwords = new ArrayList<String>();
		
		File file = new File("data/stopwords/" + wordsCollection + ".txt");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			StringBuffer content = new StringBuffer();
			while (null != (line = reader.readLine())) {
				content.append(line);
			}
			
			String[] words = content.toString().toLowerCase().replace(" ", "").split(",");
			stopwords.addAll(Arrays.asList(words));
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return stopwords;
	}
	
}
