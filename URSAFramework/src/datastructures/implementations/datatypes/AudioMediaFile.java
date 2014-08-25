package datastructures.implementations.datatypes;

import datastructures.core.MediaFile;

public class AudioMediaFile extends MediaFile {
	
	// Audio header info
	private String album;
	private String artist;
	private String genre;
	private String title;
	private int track;
	private int date;
	
	// Constructor
	public AudioMediaFile(String title, String filepath, int index) {
		super(title, filepath, index);
	}

	// Getters and Setters
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTrack() {
		return track;
	}

	public void setTrack(int track) {
		this.track = track;
	}
	
	// Printing
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("mediaDuration: " + this.mediaDuration + "\n");
		str.append("mediaFileSize: " + this.mediaFileSize + "\n");
		str.append("mediaBitrate: " + this.mediaBitrate + "\n");

		str.append("album: " + this.album + "\n");
		str.append("artist: " + this.artist + "\n");
		str.append("genre: " + this.genre + "\n");
		str.append("title: " + this.title + "\n");
		str.append("track: " + this.track + "\n");
		str.append("date: " + this.date + "\n");
		
		return str.toString();
	}

	
}
