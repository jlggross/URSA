package utility.datatypes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import main.ClusteringProcess;
import datastructures.implementations.datatypes.TextFile;

public class TextFilesUtility {

	/**
	 * Definition: Adds text files to a clustering process. 
	 * 
	 * @param process : the object for the clustering process.
	 * @param filespath : path indicating where the media files are.
	 */
	public static void addDataObjects(ClusteringProcess process, String filespath) {
		try { 
			File docFolder = new File(filespath);
			int index = 0;
			for (File doc : docFolder.listFiles()) {
				BufferedReader reader = new BufferedReader(new FileReader(doc));
				String line = "";
				StringBuffer content = new StringBuffer();
				while (null != (line = reader.readLine())) {
					content.append(line);
				}
				String[] name = doc.toString().split("\\\\");
				process.addDataObject(new TextFile(name[name.length - 1],
						content.toString(), index));
				index++;
				System.out.println("DataObject added: " + name[name.length - 1]);
				reader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
