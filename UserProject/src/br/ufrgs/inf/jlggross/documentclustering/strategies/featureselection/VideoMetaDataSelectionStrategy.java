package br.ufrgs.inf.jlggross.documentclustering.strategies.featureselection;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import br.ufrgs.inf.jlggross.clustering.DataFeature;
import br.ufrgs.inf.jlggross.clustering.DataObject;
import br.ufrgs.inf.jlggross.clustering.strategy.FeatureSelectionStrategy;
import br.ufrgs.inf.jlggross.documentclustering.Term;
import br.ufrgs.inf.jlggross.documentclustering.TextFile;
import br.ufrgs.inf.jlggross.documentclustering.VideoMediaFile;

import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;

/* -----------------------------------------------------------------------------------------------
 *  
 * Video Meta Data Selection: 
 * 
 * This class is responsible for extracting a selected set of meta data from the .mp4 and .mkv files.
 * This information in then used for comparing the data objects, stablising their level of
 * similarity.
 * 
 * The extraction is made using the XUggle API. Here just .mp4 files are been handled, but the
 * API is capable of manipulating almost every audio and video container.
 * 
 * For info about the Xuggle API: http://www.xuggle.com/xuggler
 * 
 * -----------------------------------------------------------------------------------------------
 */

public class VideoMetaDataSelectionStrategy extends FeatureSelectionStrategy {
	private Set<String> stopWords;
	private int processedDocuments;
	private int totalDocuments;
	
	
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
		this.totalDocuments = dataObjects.size();
				
		for (DataObject dataObject : dataObjects) {
			VideoMediaFile videoFile = (VideoMediaFile) dataObject;
			videoFile.clearFeatureList();
			
			this.extractMetaData(videoFile);
			
			//this.tokenize(doc);
			//this.calculateRelative(doc);
			
			this.setProgress((double)++this.processedDocuments/dataObjects.size());
		}
		
		return dataObjects;
	}
	
	
	/**
	 * Definition: Extract meta data.
	 *  
	 * @param mediaFile : contains a media file.
	 */
	private void extractMetaData(VideoMediaFile videoFile) {
		// Create a Xuggler container object
        IContainer container = IContainer.make();
        
	    // Open up the container
        File aVideo = new File(videoFile.getFilepath());
	    if (container.open(aVideo.getPath(), IContainer.Type.READ, null) < 0)
	    	throw new IllegalArgumentException("could not open file: " + aVideo.getPath());
	    
	    // query how many streams the call to open found
	    int numStreams = container.getNumStreams();
	    if (numStreams > 2)
	    	numStreams = 2;
	    	
	    if (container.getDuration() == Global.NO_PTS)
	    	videoFile.setMediaDuration(-1);
	    else
	    	videoFile.setMediaDuration(container.getDuration()/1000);
	    
	    videoFile.setMediaFileSize(container.getFileSize());
	    videoFile.setMediaBitrate(container.getBitRate());
		videoFile.setNumberStreams(numStreams);
	    
	    private String videoCodecType;
		private int videoWidth;
		private int videoHeight;
		private float videoFramerate;
		
		//Audio Stream info
		private String audioCodecType;
		private String audioLanguage;
		private String timebase;
		private int samplerate;	
	    
		for (int i = 0; i < numStreams; i++) {

			// Find the stream object
			IStream stream = container.getStream(i);
			
			// Get the pre-configured decoder that can decode this stream;
			IStreamCoder coder = stream.getStreamCoder();
	      
			
	      System.out.printf("\ttype: %s \n",     coder.getCodecType());
	      System.out.printf("\tcodec: %s \n",    coder.getCodecID());	      
	      
	      if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
	    	  System.out.printf("\tlanguage: %s \n", stream.getLanguage() == null ? "unknown" : stream.getLanguage());
	    	  System.out.printf("\tsample rate: %d\n", coder.getSampleRate());
	    	  System.out.printf("\ttimebase: %d/%d \n", stream.getTimeBase().getNumerator(), stream.getTimeBase().getDenominator());
	        
	      } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
	    	  System.out.printf("\twidth: %d\n",  coder.getWidth());
	    	  System.out.printf("\theight: %d\n", coder.getHeight());
	    	  System.out.printf("\tframe-rate: %5.2f\n", coder.getFrameRate().getDouble());
	      }
	    }
	}
	
	
	private void tokenize(TextFile doc) {
		final HashMap<String, Integer> bagOfWords = new HashMap<String, Integer>();
		String[] tokens = doc.getContent().toLowerCase()
				.split("[^\\p{Alnum}]");
				//.split("[\\s]|[.][^0-9]|[,][^0-9]|[<]|[>]|[\"]|[-]|[(]|[)]|[;]|[:]|[!]|[?]");
				//.split("[[(]|[)]|[.]|[,]|[;]|[:]|[\"]|[/]|[<]|[>]|[-]]+[[\\s]|[\\n]]*|[\\s]+[[(]|[)]|[.]|[,]|[;]|[:]|[\"]|[/]|[<]|[>]|[-]]*[\\s]*");
		for (String token : tokens) {
			if (!token.isEmpty()) {
				Integer frequency = bagOfWords.get(token);
				if (frequency == null)
					frequency = 0;
				frequency++;
				bagOfWords.put(token, frequency);
			}
		}
		
		List<String> terms = new ArrayList<String>(bagOfWords.keySet());
		Collections.sort(terms, new Comparator<String>() {
			@Override
	        public int compare(String term1, String term2) {
	            return bagOfWords.get(term2) - bagOfWords.get(term1);
	        }
	    });
		
		int count = 500;
		for (String term : terms) {
			if (!this.stopWords.contains(term)) {
				doc.addFeature(new Term(term, bagOfWords.get(term)));
				count--;
				if (count == 0) {
					break;
				}
			}
		}
	}
	
	private void calculateRelative(TextFile doc) {
		List<DataFeature> features = doc.getFeatureList();
		
		double featuresCount = 0;
		for (DataFeature feature : features) {
			Term term = (Term) feature;
			featuresCount += term.getAbsoluteFrequency();
		}

		for (DataFeature feature : features) {
			Term term = (Term) feature;
			double absFrequency = term.getAbsoluteFrequency();
			term.setRelativeFrequency(absFrequency/featuresCount);
		}
	}

}
