package clusteringstrategies.implementation.featureselection;

import java.io.File;
import java.util.Collection;
import java.util.List;

import clusteringstrategies.core.FeatureSelectionStrategy;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IMetaData;

import datastructures.core.DataObject;
import datastructures.core.MetaData;
import datastructures.implementations.datatypes.AudioMediaFile;

/* -----------------------------------------------------------------------------------------------
 *  
 * Audio Meta Data Selection: 
 * 
 * This class is responsible for extracting a selected set of meta data from the .mp3 files.
 * This information in then used for comparing the data objects, stablising their level of
 * similarity. The information extracted can be found in the AudioMediaFile.java class. 
 * 
 * The extraction is made using the Xuggle API. Here just .mp3 files are been handled, but the
 * API is capable of manipulating almost every audio and video container. The .mp3 format has been
 * chosen, because all the .mp3 we had at our disposal were full of meta data, which makes it easier
 * for the clustering algorithms (and the similarity algorithms) to have good results. 
 * 
 * For info about the Xuggle API: http://www.xuggle.com/xuggler
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class AudioMetaDataSelectionStrategy extends FeatureSelectionStrategy {
	
	private int processedDocuments;	
	
	/**
	 * Definition: Audio Meta Data Selection Strategy Constructor
	 */
	public AudioMetaDataSelectionStrategy() {}

	
	/**
	 * Definition: Extraction of meta data. A set of meta data is captured.
	 * 
	 * @param dataObjects : list of data objects.
	 */
	public List<DataObject> executeFeatureSelection(List<DataObject> dataObjects) {
		this.processedDocuments = 0;
				
		for (DataObject dataObject : dataObjects) {
			AudioMediaFile audioFile = (AudioMediaFile) dataObject;
			audioFile.clearFeatureList();
			
			this.extractMetaData(audioFile);
			this.populateFeatureList(audioFile);
			
			this.setProgress((double)++this.processedDocuments/dataObjects.size());
		}
		
		return dataObjects;
	}
	
	
	/**
	 * Definition: Extract meta data from each audio.
	 *  
	 * @param AudioMediaFile : an audio media file.
	 */
	private void extractMetaData(AudioMediaFile audioFile) {
		// Create a Xuggler container object
        IContainer container = IContainer.make();
        
	    // Open up the container
        File aVideo = new File(audioFile.getFilepath());
	    if (container.open(aVideo.getPath(), IContainer.Type.READ, null) < 0)
	    	throw new IllegalArgumentException("could not open file: " + aVideo.getPath());
	    
	    // Get meta data
	  
	    // mediaDuration
	    if (container.getDuration() == Global.NO_PTS)
	    	audioFile.setMediaDuration(-1);
	    else
	    	audioFile.setMediaDuration(container.getDuration()/1000);
	    
	    // mediaFileSize
	    audioFile.setMediaFileSize(container.getFileSize());
	    
	    // mediaBitrate
	    audioFile.setMediaBitrate(container.getBitRate());
	   
	    // album, artist, genre, title, track, date
	    IMetaData metaData = container.getMetaData();
	    Collection<String> keys = metaData.getKeys();
	    for (String aKey : keys) {
	    	String value = metaData.getValue(aKey);
	    	
	    	if (aKey.equals("album")) {
	    		audioFile.setAlbum(value);
	    	}
	    	else if (aKey.equals("artist")) {
	    		audioFile.setArtist(value);
	    	}
	    	else if (aKey.equals("genre")) {
	    		audioFile.setGenre(value);
	    	}
	    	else if (aKey.equals("title")) {
	    		audioFile.setTitle(value);
	    	}
	    	else if (aKey.equals("track")) {
	    		String[] d = value.split("/");
	    		if (value.equals(d[0]))
	    			d = value.split("-");
	    		
	    		audioFile.setTrack(Integer.parseInt(d[0]));
	    	}
	    	else if (aKey.equals("date")) {
	    		String[] d = value.split("/");
	    		if (value.equals(d[0]))
	    			d = value.split("-");
	    		
	    		audioFile.setDate(Integer.parseInt(d[0]));
		    	//System.out.printf("date: %s\n", d[0]);
	    	}
	    	
	    }
	}
	
	
	/**
	 * Definition: After all the meta data has been extracted there is the need to
	 * store this information in the Feature set of data, because the similarity 
	 * algorithm uses this to calculate the similarity between objects.
	 *  
	 * @param audioFile : an audio media file.
	 */
	private void populateFeatureList(AudioMediaFile audioFile) {
		
		audioFile.addFeature(new MetaData("mediaDuration", Long.toString(audioFile.getMediaDuration())));
		audioFile.addFeature(new MetaData("mediaFileSize", Long.toString(audioFile.getMediaFileSize())));
		audioFile.addFeature(new MetaData("mediaBitrate", Long.toString(audioFile.getMediaBitrate())));
		
		audioFile.addFeature(new MetaData("album", audioFile.getAlbum()));
		audioFile.addFeature(new MetaData("artist", audioFile.getArtist()));
		audioFile.addFeature(new MetaData("genre", audioFile.getGenre()));
		audioFile.addFeature(new MetaData("title", audioFile.getTitle()));
		audioFile.addFeature(new MetaData("track", Integer.toString(audioFile.getTrack())));
		audioFile.addFeature(new MetaData("date", Integer.toString(audioFile.getDate())));		
	}
}
