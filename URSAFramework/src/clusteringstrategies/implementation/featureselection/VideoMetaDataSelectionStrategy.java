package clusteringstrategies.implementation.featureselection;

import java.io.File;
import java.util.List;

import clusteringstrategies.core.FeatureSelectionStrategy;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

import datastructures.core.DataObject;
import datastructures.core.MetaData;
import datastructures.implementations.datatypes.VideoMediaFile;

/* -----------------------------------------------------------------------------------------------
 *  
 * Video Meta Data Selection: 
 * 
 * This class is responsible for extracting a selected set of meta data from the .mp4 and .mkv files.
 * This information in then used for comparing the data objects, stablising their level of
 * similarity.
 * 
 * The extraction is made using the Xuggle API. Here just .mp4 files are been handled, but the
 * API is capable of manipulating almost every audio and video container.
 * 
 * For info about the Xuggle API: http://www.xuggle.com/xuggler
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class VideoMetaDataSelectionStrategy extends FeatureSelectionStrategy {
	private int processedDocuments;
		
	/**
	 * Definition: VideoMetaDataSelectionStrategy
	 */
	public VideoMetaDataSelectionStrategy() {
	}

	
	/**
	 * Definition: The extraction if meta data begins. A set of meta data is captured.
	 * 
	 * @param dataObjects : list of data objects.
	 */
	@Override
	public List<DataObject> executeFeatureSelection(List<DataObject> dataObjects) {
		this.processedDocuments = 0;
				
		for (DataObject dataObject : dataObjects) {
			VideoMediaFile videoFile = (VideoMediaFile) dataObject;
			videoFile.clearFeatureList();
			
			this.extractMetaData(videoFile);
			this.populateFeatureList(videoFile);
			
			this.setProgress((double)++this.processedDocuments/dataObjects.size());
		}
		
		return dataObjects;
	}
	
	
	/**
	 * Definition: Extract meta data from each video.
	 *  
	 * @param videoFile : a video media file.
	 */
	private void extractMetaData(VideoMediaFile videoFile) {
		// Create a Xuggler container object
        IContainer container = IContainer.make();
        
	    // Open up the container
        File aVideo = new File(videoFile.getFilepath());
	    if (container.open(aVideo.getPath(), IContainer.Type.READ, null) < 0)
	    	throw new IllegalArgumentException("could not open file: " + aVideo.getPath());
	    
	    // Get meta data
	    int numStreams = container.getNumStreams();
	  
	    // mediaDuration
	    if (container.getDuration() == Global.NO_PTS)
	    	videoFile.setMediaDuration(-1);
	    else
	    	videoFile.setMediaDuration(container.getDuration()/1000);
	    
	    // mediaFileSize
	    videoFile.setMediaFileSize(container.getFileSize());
	    
	    // mediaBitrate
	    videoFile.setMediaBitrate(container.getBitRate());
	    
	    // numberStreams
	    if (numStreams > 2)
	    	videoFile.setNumberStreams(2);
	    else 
	    	videoFile.setNumberStreams(1);
	    
		boolean audioStream = false;
		boolean videoStream = false;
		for (int i = 0; i < numStreams; i++) {
			// Find the stream object
			IStream stream = container.getStream(i);
			// Get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = stream.getStreamCoder();
	      	      
			if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO && !audioStream) {
				audioStream = true;
				
				// audioCodecType
				videoFile.setAudioCodecType(coder.getCodecID());

				// audioLanguage
				if (stream.getLanguage() == null)
					videoFile.setAudioLanguage("unknown");
				else
					videoFile.setAudioLanguage(stream.getLanguage());
				
				// timebase
				videoFile.setTimebase(coder.getSampleRate());
				
				// samplerate
				String str = stream.getTimeBase().getNumerator() + "/" + stream.getTimeBase().getDenominator();
				videoFile.setSamplerate(str);
				
			} else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO && !videoStream) {
				videoStream = true;
				videoFile.setVideoCodecType(coder.getCodecID());
				videoFile.setVideoWidth(coder.getWidth());
				videoFile.setVideoHeight(coder.getHeight());
				videoFile.setVideoFramerate(coder.getFrameRate().getDouble());
			}
	    }
	}
	
	
	/**
	 * Definition: After all the meta data has been extracted there is the need to
	 * store this information in the Feature set of data, because the similarity 
	 * algorithm uses this to calculate the similarity between objects.
	 *  
	 * @param videoFile : a video media file.
	 */
	private void populateFeatureList(VideoMediaFile videoFile) {
		
		videoFile.addFeature(new MetaData("mediaDuration", Long.toString(videoFile.getMediaDuration())));
		videoFile.addFeature(new MetaData("mediaFileSize", Long.toString(videoFile.getMediaFileSize())));
		videoFile.addFeature(new MetaData("mediaBitrate", Long.toString(videoFile.getMediaBitrate())));
		videoFile.addFeature(new MetaData("numberStreams", Integer.toString(videoFile.getNumberStreams())));
		
		// Video Stream info
		videoFile.addFeature(new MetaData("videoCodecType", videoFile.getVideoCodecType().toString()));
		videoFile.addFeature(new MetaData("videoWidth", Integer.toString(videoFile.getVideoWidth())));
		videoFile.addFeature(new MetaData("videoHeight", Integer.toString(videoFile.getVideoHeight())));
		videoFile.addFeature(new MetaData("videoFramerate", Double.toString(videoFile.getVideoHeight())));
		
		// Audio Stream info
		videoFile.addFeature(new MetaData("audioCodecType", videoFile.getAudioCodecType().toString()));
		videoFile.addFeature(new MetaData("audioLanguage", videoFile.getAudioLanguage()));
		videoFile.addFeature(new MetaData("timebase", Long.toString(videoFile.getTimebase())));
		videoFile.addFeature(new MetaData("samplerate", videoFile.getSamplerate()));		
	}

}
