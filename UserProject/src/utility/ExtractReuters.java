package utility;

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

public class ExtractReuters {
	private static Pattern reutersDoc = Pattern.compile("<reuters (.*?)</reuters>");
	private static Pattern cgisplit = Pattern.compile("cgisplit=\"published-testset\"");
	private static Pattern text = Pattern.compile("<body(.*?)</body>");
	private static Pattern newid = Pattern.compile("newid=\"(.*?)\"");
	private static Pattern topic = Pattern.compile("<topics>(.*?)</topics>");
	private static Pattern topicD = Pattern.compile("<d>(.*?)</d>");
	
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
