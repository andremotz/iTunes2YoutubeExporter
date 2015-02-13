package com.andremotz.itunesplaylistexporter.datahandling;

import java.util.ArrayList;
import java.util.HashMap;

import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.items.playlists.PlaylistItem;
import com.gps.itunes.lib.items.tracks.AdditionalTrackInfo;
import com.gps.itunes.lib.items.tracks.Track;
import com.gps.itunes.lib.tasks.TracksRetriever;
import com.gps.itunes.lib.types.LibraryObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ItunesLibraryDataParser {
	
	public ItunesLibraryDataParser() {
		
	}

	Logger log = LogManager
			.getLogger(ItunesLibraryDataParser.class.getName());
			
	/*
	 * Return all data for relevant Tracks like Artis, Track id etc
	 * for further displaying as ArrayList
	 */
	public ArrayList<TableTracksItem> getAllTracksInCurrentPlaylist() {
		// TODO Auto-generated method stub
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = new ArrayList<TableTracksItem>();
		
		Playlist currentPlaylist = Datahandler.getCurrentPlaylist();

		LibraryObject iTunesLibraryObject = Datahandler
				.getiTunesLibraryObject();
		TracksRetriever tracksRetriever = new TracksRetriever(
				iTunesLibraryObject);
		Track[] iTunesLibraryAllTracks = null;
		HashMap<Long, Track> allTracksInItunesLibrary = new HashMap<Long, Track>();
		
		try {
			iTunesLibraryAllTracks = tracksRetriever.getTracks();
		} catch (NoChildrenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * All tracks of iTunes-library in a HashMap
		 */

		for (int i = 0; i < iTunesLibraryAllTracks.length; i++) {
			long currentTrackId = iTunesLibraryAllTracks[i].getTrackId();
			Track currentTrack = iTunesLibraryAllTracks[i];
			allTracksInItunesLibrary.put(currentTrackId, currentTrack);
		}

		/*
		 * All tracks of current chosen playlist in a List
		 */
		PlaylistItem[] currentPlaylistItems = currentPlaylist
				.getPlaylistItems();

		allTracksInCurrentPlaylist = new ArrayList<TableTracksItem>();
		
		for (int i = 0; i < currentPlaylistItems.length; i++) {
			long currentTrackId = currentPlaylistItems[i].getTrackId();
			Track currentTrack = allTracksInItunesLibrary.get(currentTrackId);

			AdditionalTrackInfo currentTrackAddiionalTrackInfo = currentTrack
					.getAdditionalTrackInfo();
			String whichInfo = "Artist";
			String currentTrackArtist = currentTrackAddiionalTrackInfo
					.getAdditionalInfo(whichInfo);

			String currentTrackName = currentTrack.getTrackName();

			log.trace("Track in playlist: " + currentTrackArtist + " - "
					+ currentTrackName);
			
			TableTracksItem currentTableTrackItem = new TableTracksItem();
			currentTableTrackItem.setTrackArtist(currentTrackArtist);
			currentTableTrackItem.setTrackName(currentTrackName);
			currentTableTrackItem.setTrack(currentTrack);
			
			allTracksInCurrentPlaylist.add(currentTableTrackItem);

		}
		
		return allTracksInCurrentPlaylist;
	}
	
}
