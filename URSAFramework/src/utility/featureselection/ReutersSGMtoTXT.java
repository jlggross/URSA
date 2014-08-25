package utility.featureselection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import datastructures.core.DataCluster;
import datastructures.core.DataObject;
import datastructures.implementations.datatypes.TextFile;

/* ---------------------------------------------------------------------------------
 * 
 * This class extract the content of .sgm files. It is used in the set of reuters
 * files, which are then converted to .txt files.
 * 
 * For this class to work you need the following:
 * - create a data/reuters-sgm folder with the reuters .sgm files
 * - create an empty data/reuters-extracted folder to where the .txt are going to 
 * be stored.
 * 
 * ---------------------------------------------------------------------------------
 */

public class ReutersSGMtoTXT {
	
	private static Pattern reutersDoc = Pattern.compile("<reuters (.*?)</reuters>");
	private static Pattern cgisplit = Pattern.compile("cgisplit=\"published-testset\"");
	private static Pattern text = Pattern.compile("<body(.*?)</body>");
	private static Pattern newid = Pattern.compile("newid=\"(.*?)\"");
	private static Pattern topic = Pattern.compile("<topics>(.*?)</topics>");
	private static Pattern topicD = Pattern.compile("<d>(.*?)</d>");
	
	/**
	 * Definition: Main method
	 * 
	 * @param args : not used.
	 */
	public static void main(String args[]) throws IOException {
		File reutersDir = new File("data/reuters-sgm");
		
		File[] sgmFiles = reutersDir.listFiles(new FileFilter() {			
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(".sgm");
			}
		});
				
		if (sgmFiles != null && sgmFiles.length > 0) {
			for (File sgmFile : sgmFiles) {
				extractFile(sgmFile);
			}
		}
	}
	
	
	/**
	 * Definition: Given a .sgm file all its content is extracted to .txt files.
	 * 
	 * @param sgmFile : an .sgm file.
	 */
	public static void extractFile(File sgmFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(sgmFile));
		
		String line = "";
		StringBuffer buffer = new StringBuffer(1024);
		while (null != ((line = reader.readLine()))) {
			buffer.append(line).append(" ");
		}
		
		String sgm = buffer.toString().toLowerCase();
		
		Matcher matchDocs = reutersDoc.matcher(sgm);
		while (matchDocs.find()) {
			String doc = matchDocs.group();
			Matcher matchHayes = cgisplit.matcher(doc);
			if (matchHayes.find()) {
				
				Matcher matchNewid = newid.matcher(doc);
				matchNewid.find();
				Matcher matchText = text.matcher(doc);
				
				BufferedWriter writer = new BufferedWriter(new FileWriter("data/reuters-extracted/" + matchNewid.group(1) + ".txt"));
				if (matchText.find()) {
					writer.append(matchText.group(1));
					System.out.println("File '" + matchNewid.group(1) + ".txt' created.");
				}
				writer.close();
			}
		}
		
		reader.close();
	}
}
