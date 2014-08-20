package br.ufrgs.inf.jlggross.documentclustering;

import java.io.File;

import com.xuggle.xuggler.ICodec.ID;

import br.ufrgs.inf.jlggross.clustering.DataFeature;

public class VideoMediaFile extends MediaFile {
	
	// Media info
	private int numberStreams;
	
	// Video Stream info
	private ID videoCodecType;
	private int videoWidth;
	private int videoHeight;
	private double videoFramerate;
	
	// Audio Stream info
	private ID audioCodecType;
	private String audioLanguage;
	private long timebase;
	private String samplerate;	
	
	// Constructor
	public VideoMediaFile(String title, String filepath, int index) {
		super(title, filepath, index);
	}

	// Getters and Setters
	public long getMediaDuration() {
		return mediaDuration;
	}

	public void setMediaDuration(long mediaDuration) {
		this.mediaDuration = mediaDuration;
	}

	public long getMediaFileSize() {
		return mediaFileSize;
	}

	public void setMediaFileSize(long mediaFileSize) {
		this.mediaFileSize = mediaFileSize;
	}

	public long getMediaBitrate() {
		return mediaBitrate;
	}

	public void setMediaBitrate(long mediaBitrate) {
		this.mediaBitrate = mediaBitrate;
	}

	public int getNumberStreams() {
		return numberStreams;
	}

	public void setNumberStreams(int numberStreams) {
		this.numberStreams = numberStreams;
	}
	
	public ID getVideoCodecType() {
		return videoCodecType;
	}

	public void setVideoCodecType(ID videoCodecType) {
		this.videoCodecType = videoCodecType;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}

	public double getVideoFramerate() {
		return videoFramerate;
	}

	public void setVideoFramerate(double videoFramerate) {
		this.videoFramerate = videoFramerate;
	}

	public ID getAudioCodecType() {
		return audioCodecType;
	}

	public void setAudioCodecType(ID audioCodecType) {
		this.audioCodecType = audioCodecType;
	}

	public String getAudioLanguage() {
		return audioLanguage;
	}

	public void setAudioLanguage(String audioLanguage) {
		this.audioLanguage = audioLanguage;
	}

	public long getTimebase() {
		return timebase;
	}

	public void setTimebase(long timebase) {
		this.timebase = timebase;
	}

	public String getSamplerate() {
		return samplerate;
	}

	public void setSamplerate(String samplerate) {
		this.samplerate = samplerate;
	}
	
	// Printing
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("mediaDuration: " + this.mediaDuration + "\n");
		str.append("mediaFileSize: " + this.mediaFileSize + "\n");
		str.append("mediaBitrate: " + this.mediaBitrate + "\n");
		str.append("numberStreams: " + this.numberStreams + "\n");
		
		str.append("videoCodecType: " + this.videoCodecType + "\n");
		str.append("videoWidth: " + this.videoWidth + "\n");
		str.append("videoHeight: " + this.videoHeight + "\n");
		str.append("videoFramerate: " + this.videoFramerate + "\n");
		str.append("audioCodecType: " + this.audioCodecType + "\n");
		str.append("audioLanguage: " + this.audioLanguage + "\n");
		str.append("timebase: " + this.timebase + "\n");
		str.append("samplerate: " + this.samplerate + "\n");
		
		return str.toString();
	}
}
