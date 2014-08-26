package utility.datatypes;

import java.io.File;

import main.ClusteringProcess;
import datastructures.implementations.datatypes.AudioMediaFile;
import datastructures.implementations.datatypes.VideoMediaFile;

public class AudioVideoFilesUtility {

	/**
	 * Definition: Adds the audio or video files to a clustering process. 
	 * 
	 * @param process : the object for the clustering process.
	 * @param mediapath : path indicating where the media files are.
	 * @param filetype : indicates the file type to be added in the clustering process object 
	 * list of dataObjects. 
	 */
	public static void addDataObjects(ClusteringProcess process, String mediapath, String filetype) {
		File mediaDir = new File(mediapath);
		File[] medias = mediaDir.listFiles();
		int index = 0;
		for (File mediaFile : medias) {
			String filename[] = mediaFile.getPath().split("\\\\");
			
			if (filetype == "audio") {
				process.addDataObject(new AudioMediaFile(filename[2], mediaFile
					.getPath(), index));
			} else if (filetype == "video") {
				process.addDataObject(new VideoMediaFile(filename[2], mediaFile
					.getPath(), index));
			} else {
				throw new RuntimeException("AudioVideoFiles.java : filetype should be 'audio' or 'video'.");
			} 
			
			index++;
		}
	}
}
