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
 * Reuters-21578
 * 
 * A collection of documents that appeared on Reuters newswire in 1987. The documents 
 * were assembled and indexed with categories.
 * 
 * Currently the most widely used test collection for text categorization research, 
 * though likely to be superceded over the next few years by RCV1. The data was originally 
 * collected and labeled by Carnegie Group, Inc. and Reuters, Ltd. in the course of 
 * developing the CONSTRUE text categorization system.  
 * 
 * Various researchers have prepared data files useful for work with Reuters-21578.  
 * 
 * Useful link:
 * 1. http://www.daviddlewis.com/resources/testcollections/reuters21578/ (for general
 * informations about the collection of documents)
 * 2. http://kdd.ics.uci.edu/databases/reuters21578/reuters21578.html (to download the
 * collection)
 * 
 * ---------------------------------------------------------------------------------
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
	
	private static String titleIni = "<TITLE>";
	private static String titleEnd = "</TITLE>";
	private static String datelineIni = "<DATELINE>";
	private static String datelineEnd = "</DATELINE>";
	private static String bodyIni = "<BODY>";
	private static String bodyEnd = "</BODY>";
	
	private static Pattern reutersDoc = Pattern.compile("<reuters (.*?)</reuters>");
	private static Pattern cgisplit = Pattern.compile("cgisplit=\"published-testset\"");
	private static Pattern text = Pattern.compile("<body(.*?)</body>");
	private static Pattern newid = Pattern.compile("newid=\"(.*?)\"");
	
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
	 * It extracts the Reuters-21578 into 20.841 .txt files. It was supposed to
	 * have 21578 files in this collection, but maybe we got a little less files
	 * because of the way we made the parsing of each .sgm.
	 * 
	 * @param sgmFile : an .sgm file.
	 */
	public static void extractFile(File sgmFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(sgmFile));

		String line = "";
		StringBuffer buffer = new StringBuffer(1024);
		while (null != ((line = reader.readLine()))) {
			buffer.append(line).append("\n");
		}
		
		String[] files = buffer.toString().split(titleIni);
		
		boolean first = false;
		int count = 0; 
		for (String afile : files) {
			if (!first) {
				first = true;
				continue;
			}
				
			String[] a = afile.split(titleEnd);
			afile = (a.length == 2) ? a[0] + a[1] : a[0];

			a = afile.split(datelineIni);
			afile = (a.length == 2) ? a[0] + a[1] : a[0];
			
			a = afile.split(datelineEnd);
			afile = (a.length == 2) ? a[0] + a[1] : a[0];
			
			a = afile.split(bodyIni);
			afile = (a.length == 2) ? a[0] + a[1] : a[0];
			
			a = afile.split(bodyEnd);
			afile = a[0];
			
			//System.out.println("file: " + afile);
			
			String[] name = sgmFile.getName().split(".sgm");
			
			if (count < 10)
				name[0] = "data/reuters-extracted/" + name[0] + "-0000" + count + ".txt";
			else if (count < 100)
				name[0] = "data/reuters-extracted/" + name[0] + "-000" + count + ".txt";
			else if (count < 1000)
				name[0] = "data/reuters-extracted/" + name[0] + "-00" + count + ".txt";
			else if (count < 10000)
				name[0] = "data/reuters-extracted/" + name[0] + "-0" + count + ".txt";
			else 
				name[0] = "data/reuters-extracted/" + name[0] + "-" + count + ".txt";
				
			BufferedWriter writer = new BufferedWriter(new FileWriter(name[0]));
			writer.append(afile);
			System.out.println("File '" + name[0] + "' created.");
			
			count++;
			writer.close();
		}
		
		reader.close();
	}
	
	
	/**
	 * Definition: Given a .sgm file all its content is extracted to .txt files.
	 * This is the first implementation made, but it extracts all the 22 .sgm file
	 * into 722 .txt files. The Reuters-21578 has 21578 files, 1000 for each .sgm
	 * file, plus 578 in the last one.
	 * 
	 * This method is not being used, because it seems to be parsing the .sgm in 
	 * a wrong way. Better use method extractFile  
	 * 
	 * @param sgmFile : an .sgm file.
	 */
	public static void extractFile2(File sgmFile) throws IOException {
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
