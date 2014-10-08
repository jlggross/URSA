package utility.clusteranalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.implementations.datatypes.TextFile;

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
	
	
	/**
	 * Definition: Build cluster file.
	 * 
	 * @param dataPath : indicates where the data is located.
	 * @param destPath : indicates the folder where the class file will be written. 
	 */
	public static void buildClusterFile(String dataPath, String destPath) {
		File docFolder = new File(dataPath);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(destPath + "classes.txt"));
			
			for (File folder : docFolder.listFiles()) {
				String s;
				s = folder.getName() + ": ";
				
				int i = 0;
				for (File doc : folder.listFiles()) {
					i++;
				}
				
				writer.append(s + i + "\n");
				for (File doc : folder.listFiles()) {
					//System.out.println(doc.getPath());
					//TextFile doc = (TextFile) object;
					s = doc.getName() + "\n";
					writer.append(s);
				}
				writer.append("\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
