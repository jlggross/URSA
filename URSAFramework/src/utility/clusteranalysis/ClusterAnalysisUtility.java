package utility.clusteranalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClusterAnalysisUtility {

	/**
	 * Definition: Load the data classes.
	 * 
	 * @param filename : refers to a file with the expected classes. 
	 */
	public static List<List<String>> loadDataClasses(String filename) {
		
		List<List<String>> classes = new ArrayList<List<String>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = "";
			while (null != (line = reader.readLine())) {
				if (!line.isEmpty()) {
					
					String[] parse = line.split(": ");
					List<String> newClass = new ArrayList<String>();
					int count = Integer.valueOf(parse[1]);
					
					for (int i = 0; i < count; i++) {
						line = reader.readLine();
						newClass.add(line);
					}
					
					line = reader.readLine();
					classes.add(newClass);
				}
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("IOException in function loadMatrix of class Matrix2D.java");
			e.printStackTrace();
		}
		
		return classes;
	}
	
}
