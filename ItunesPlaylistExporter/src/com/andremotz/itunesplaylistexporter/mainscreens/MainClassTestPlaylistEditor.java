package com.andremotz.itunesplaylistexporter.mainscreens;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.TableTracksItem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClassTestPlaylistEditor extends Application {

	static Logger log = Logger.getLogger(MainClassTestPlaylistEditor.class
			.getName());

	@Override
	public void start(Stage stage) throws Exception {

		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = getFakeData();
		Datahandler.setAllTracksInCurrentPlaylist(allTracksInCurrentPlaylist);

		Parent root = FXMLLoader.load(getClass().getResource(
				"MaskPlaylistEditor.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.show();
	}

	/*
	 * Fills Datahandler with fake data so that for testing purposes not all the
	 * time new data has to be located.
	 */
	private ArrayList<TableTracksItem> getFakeData() {
		ArrayList<TableTracksItem> fakeTracksItems = new ArrayList<TableTracksItem>();

		TableTracksItem track1 = new TableTracksItem();
		track1.setTrackArtist("Marc Renton");
		track1.setTrackName("Freefloat");
		fakeTracksItems.add(track1);

		TableTracksItem track2 = new TableTracksItem();
		track2.setTrackArtist("Paul van Dyk");
		track2.setTrackName("Namistai");
		fakeTracksItems.add(track2);

		TableTracksItem track3 = new TableTracksItem();
		track3.setTrackArtist("Bungle");
		track3.setTrackName("No time 2 love");
		fakeTracksItems.add(track3);

		return fakeTracksItems;
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}