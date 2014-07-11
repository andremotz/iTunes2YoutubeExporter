package com.andremotz.itunesplaylistexporter.mainscreens;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;
import com.andremotz.itunesplaylistexporter.datahandling.TableTracksItem;
import com.andremotz.itunesplaylistexporter.datahandling.YoutubeBlackbox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ExportYoutubeConfirmedController implements Initializable{
	
	static Logger log = Logger.getLogger(ExportYoutubeConfirmedController.class
			.getName());
	
	public Label lblMessage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		YoutubeBlackbox youtubeBlackbox = new YoutubeBlackbox();
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = Datahandler
				.getAllTracksInCurrentPlaylist();
		
		String youtubeBlackboxMessage = youtubeBlackbox.createYoutubePlaylist(allTracksInCurrentPlaylist);
		lblMessage.setText(youtubeBlackboxMessage);
		
	}
	
	@FXML
	public void handleBtnCloseAction(ActionEvent event) {
		// TODO implement!
		log.trace(GlobalFunctions.getMethodName(0));
	}

}
