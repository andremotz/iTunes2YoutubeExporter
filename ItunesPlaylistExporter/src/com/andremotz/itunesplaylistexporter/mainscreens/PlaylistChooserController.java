package com.andremotz.itunesplaylistexporter.mainscreens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;
import com.andremotz.itunesplaylistexporter.datahandling.ItunesLibraryDataParser;
import com.andremotz.itunesplaylistexporter.datahandling.TableTracksItem;
import com.gps.itunes.lib.exceptions.LibraryParseException;
import com.gps.itunes.lib.exceptions.NoChildrenException;
import com.gps.itunes.lib.items.playlists.Playlist;
import com.gps.itunes.lib.tasks.PlaylistRetriever;
import com.gps.itunes.lib.types.LibraryObject;
import com.gps.itunes.lib.xml.XMLParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlaylistChooserController implements Initializable {

	private LibraryObject iTunesLibraryObject;
	private Playlist[] iTunesPlayLists;
	private HashMap<String, Playlist> iTunesPlayListsMap = new HashMap<String, Playlist>();
	
	Logger log = LogManager
			.getLogger(PlaylistChooserController.class.getName());

	@FXML
	private Label lblHeadline;
	public ComboBox<String> comboPlaylists;

	/*
	 * (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 * 
	 * Window-initialization that allows the user to choose an iTunes-Playlist
	 * from the former choosen iTunes-library.
	 */
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

		initializeComboPlaylists();
		
	}
	
	private void initializeComboPlaylists() {
		iTunesPlayLists = null;
		try {
			String iTunesLibraryLocation = Datahandler.getiTunesLibraryLocation();
			log.trace("String iTunesLibraryLocation: " + iTunesLibraryLocation);
			iTunesLibraryObject = new XMLParser().parseXML(iTunesLibraryLocation);
			Datahandler.setiTunesLibraryObject(iTunesLibraryObject);
			
			PlaylistRetriever playlistRetriever = new PlaylistRetriever(iTunesLibraryObject);
			Datahandler.setPlaylistRetriever(playlistRetriever);
			
			iTunesPlayLists = playlistRetriever.retrievePlaylist();
		} catch (LibraryParseException e) {
			log.error(e.getMessage());
		} catch (NoChildrenException e) {
			log.error(e.getMessage());
		}

		/*
		 * Format for playlist-dropdown "xx - Playlistname", so that the entries are unique
		 */
		ArrayList<String> iTunesPlaylistNameEntries = new ArrayList<String>();
		for (int i = 0; i < iTunesPlayLists.length; i++) {
			String currentParsediTunesPlaylistNameEntry = i + " - "
					+ iTunesPlayLists[i].getName();

			Playlist currentParsediTunesPlaylist = iTunesPlayLists[i];

			iTunesPlaylistNameEntries.add(currentParsediTunesPlaylistNameEntry);
			iTunesPlayListsMap.put(currentParsediTunesPlaylistNameEntry, currentParsediTunesPlaylist);
		}


		comboPlaylists.getItems().addAll(iTunesPlaylistNameEntries);
		
	}

	/*
	 * After a playlist was chosen, the form for the playlist-editor will load.
	 */
	@FXML
	private void handleBtnNextAction(ActionEvent event) {
		log.trace(GlobalFunctions.getMethodName(0));
		log.trace("Chosen playlist: " + comboPlaylists.getValue());
		
		String choosenPlaylist = comboPlaylists.getValue();
		Playlist currentPlaylist = iTunesPlayListsMap.get(choosenPlaylist);
		
		/*
		 * Datahandling and processing
		 */
		ItunesLibraryDataParser dataParser = new ItunesLibraryDataParser();
		Datahandler.setCurrentPlaylist(currentPlaylist);
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = dataParser.getAllTracksInCurrentPlaylist();
		Datahandler.setAllTracksInCurrentPlaylist(allTracksInCurrentPlaylist);
		
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass()
					.getResource("MaskPlaylistEditor.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

	@FXML
	private void handleBtnBackAction(ActionEvent event) {
		log.trace("handleBtnBackAction()");
		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass()
					.getResource("MaskLibraryChooser.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
