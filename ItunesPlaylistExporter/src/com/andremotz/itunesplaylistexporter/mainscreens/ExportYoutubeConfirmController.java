package com.andremotz.itunesplaylistexporter.mainscreens;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ExportYoutubeConfirmController  implements Initializable{
	
	static Logger log = Logger.getLogger(ExportYoutubeConfirmController.class.getName());

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	public void handleBtnExportAction(ActionEvent event) {
		log.trace(GlobalFunctions.getMethodName(0));

		Node node = (Node) event.getSource();
		Stage stage = (Stage) node.getScene().getWindow();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("MaskExportYoutubeConfirmed.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}

}
