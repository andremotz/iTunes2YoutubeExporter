package com.andremotz.itunesplaylistexporter.mainscreens;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LibraryChooserController implements Initializable {

	File iTunesLibrary;
	Logger log = LogManager
			.getLogger(LibraryChooserController.class.getName());
	Properties generalProperties = new Properties();

	@FXML
	private Label lblLibraryPath;
	public TextField textInputPath;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			generalProperties.load(getClass().getClassLoader().getResourceAsStream("general.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String latestItunesLibraryPath = generalProperties.getProperty("iTunesLibraryLocation");
		lblLibraryPath.setText(latestItunesLibraryPath);
	}

	@FXML
	private void handleBtnBrowseAction(ActionEvent event) {
		log.trace(GlobalFunctions.getMethodName(0));

		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"iTunes Music Libraries (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show open file dialog
		iTunesLibrary = fileChooser.showOpenDialog(null);

		String pathToLibrary = iTunesLibrary.getAbsoluteFile().toString();
		textInputPath.setText(pathToLibrary);
		generalProperties.setProperty("iTunesLibraryLocation", pathToLibrary);

	}

	@FXML
	private void handleBtnNextAction(ActionEvent event) {
		log.trace(GlobalFunctions.getMethodName(0));
		
		Datahandler.setiTunesLibLocation(textInputPath.getText());

		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(
					"MaskPlaylistChooser.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();
	}

}
