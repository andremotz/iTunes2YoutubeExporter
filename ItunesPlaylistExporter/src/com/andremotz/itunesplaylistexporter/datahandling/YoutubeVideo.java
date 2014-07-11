package com.andremotz.itunesplaylistexporter.datahandling;

import com.google.api.services.youtube.model.Thumbnail;

/*
 * Class contains basic information of a Youtube-video
 */
public class YoutubeVideo {
	
	private String videoID;
	private Thumbnail thumbnail;
	private String thumbnailUrl;
	private String title;
	
	
	public YoutubeVideo() {
		
	}


	public String getVideoID() {
		return videoID;
	}


	public void setVideoID(String videoID) {
		this.videoID = videoID;
	}


	public Thumbnail getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(Thumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getThumbnailUrl() {
		thumbnailUrl = thumbnail.getUrl();
		return thumbnailUrl;
	}


	public void setThumbnailUrl(String fthumbnailUrl) {
		this.thumbnailUrl = fthumbnailUrl;
	}

}
