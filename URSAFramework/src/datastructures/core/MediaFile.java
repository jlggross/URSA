package datastructures.core;

public class MediaFile extends DataObject {
	
	// Media info
	protected String title;
	protected String filepath;
	protected long mediaDuration;	
	protected long mediaFileSize;
	protected long mediaBitrate;
	
	// Constructor
	public MediaFile(String title, String filepath, int index) {
		this.title = title;
		this.filepath = filepath;
		this.index = index;
	}
	
	// Setter and Getters
	
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

	public String getTitle() {
		return this.title;
	}
	
	public String getFilepath() {
		return this.filepath;
	}
	
	// Printing
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DataFeature feature : this.featureCollection) {
			str.append(feature.toString()).append("\n");
		}
		return str.toString();
	}

	
}
