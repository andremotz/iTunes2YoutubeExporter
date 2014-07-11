package com.andremotz.itunesplaylistexporter.datahandling;

import java.util.ArrayList;

import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.tasks.PlaylistRetriever;
import com.gps.itunes.lib.types.LibraryObject;

public class Datahandler {
	
	private static String iTunesLibPath;
	private static Playlist currentPlaylist;
	private static LibraryObject iTunesLibraryObject;
	private static PlaylistRetriever playlistRetriever;
	private static ArrayList<TableTracksItem> allTracksInCurrentPlaylist;
	
	Datahandler() {
	}
	
	public static String getiTunesLibraryLocation() {
		return iTunesLibPath;
	}

	public static void setiTunesLibLocation(String iTunesLibLocation) {
		iTunesLibPath = iTunesLibLocation;
	}

	public static void setCurrentPlaylist(Playlist playlist) {
		currentPlaylist = playlist;
	}

	public static Playlist getCurrentPlaylist() {
		return currentPlaylist;
	}

	public static LibraryObject getiTunesLibraryObject() {
		return iTunesLibraryObject;
	}

	public static void setiTunesLibraryObject(LibraryObject libraryObject) {
		Datahandler.iTunesLibraryObject = libraryObject;
	}

	public static PlaylistRetriever getPlaylistRetriever() {
		return playlistRetriever;
	}

	public static void setPlaylistRetriever(PlaylistRetriever playlistRetriever) {
		Datahandler.playlistRetriever = playlistRetriever;
	}


	public static ArrayList<TableTracksItem> getAllTracksInCurrentPlaylist() {
		return allTracksInCurrentPlaylist;
	}

	public static void setAllTracksInCurrentPlaylist(
			ArrayList<TableTracksItem> fAllTracksInCurrentPlaylist) {
		allTracksInCurrentPlaylist = fAllTracksInCurrentPlaylist;
	}

	
}
