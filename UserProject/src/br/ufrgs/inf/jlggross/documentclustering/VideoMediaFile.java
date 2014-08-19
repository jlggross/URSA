package br.ufrgs.inf.jlggross.documentclustering;

import java.io.File;

import br.ufrgs.inf.jlggross.clustering.DataFeature;

public class VideoMediaFile extends MediaFile {
	
	// Media info
	private long mediaDuration;	
	private long mediaFileSize;
	private long mediaBitrate;
	private int numberStreams;
	
	// Video Stream info
	private String videoCodecType;
	private int videoWidth;
	private int videoHeight;
	private float videoFramerate;
	
	// Audio Stream info
	private String audioCodecType;
	private String audioLanguage;
	private String timebase;
	private int samplerate;	
	
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
	
	public String getVideoCodecType() {
		return videoCodecType;
	}

	public void setVideoCodecType(String videoCodecType) {
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

	public float getVideoFramerate() {
		return videoFramerate;
	}

	public void setVideoFramerate(float videoFramerate) {
		this.videoFramerate = videoFramerate;
	}

	public String getAudioCodecType() {
		return audioCodecType;
	}

	public void setAudioCodecType(String audioCodecType) {
		this.audioCodecType = audioCodecType;
	}

	public String getAudioLanguage() {
		return audioLanguage;
	}

	public void setAudioLanguage(String audioLanguage) {
		this.audioLanguage = audioLanguage;
	}

	public String getTimebase() {
		return timebase;
	}

	public void setTimebase(String timebase) {
		this.timebase = timebase;
	}

	public int getSamplerate() {
		return samplerate;
	}

	public void setSamplerate(int samplerate) {
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
