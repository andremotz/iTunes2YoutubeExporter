package com.andremotz.itunesplaylistexporter.datahandling;

import java.util.ArrayList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import com.gps.itunes.lib.items.tracks.Track;

/*
 * This class represents entries in the PlaylistEditor's
 * TableView. It contains fetched songs from a chosen
 * playlist.
 */
/*
 * TODO: Simplify to youtubeVideosFromQuery
 */
public class TableTracksItem {
	private SimpleStringProperty trackArtist;
	private SimpleStringProperty trackName;
	private SimpleStringProperty videoID;
	private Track track;
	private ObjectProperty<YoutubeVideo> video;
	private ArrayList<YoutubeVideo> youtubeVideosFromQuery;
	
	public TableTracksItem() {
		trackArtist = new SimpleStringProperty();
		trackName = new SimpleStringProperty();
		videoID = new SimpleStringProperty();
		video = new SimpleObjectProperty<YoutubeVideo>();
		setYoutubeVideosFromQuery(new ArrayList<YoutubeVideo>());
		
	}
	
	public String getTrackName() {
		return trackName.get();
	}
	public void setTrackName(String ftrackName) {
		trackName.set(ftrackName);
	}
	public String getTrackArtist() {
		return trackArtist.get();
	}
	public void setTrackArtist(String ftrackArtist) {
		trackArtist.set(ftrackArtist);
	}

	public String getVideoID() {
		return videoID.get();
	}

	public void setVideoID(String ftrackUrl) {
		videoID.set(ftrackUrl);
	}
	
	public Track getTrack() {
		return track;
	}
	public void setTrack(Track track) {
		this.track = track;
	}

	public Object getVideo() {
		return video.get();
	}

	public void setVideo(YoutubeVideo fvideo) {
		this.video.set(fvideo);
	}

	public ArrayList<YoutubeVideo> getYoutubeVideosFromQuery() {
		return youtubeVideosFromQuery;
	}

	public void setYoutubeVideosFromQuery(ArrayList<YoutubeVideo> youtubeVideosFromQuery) {
		this.youtubeVideosFromQuery = youtubeVideosFromQuery;
	}


}
