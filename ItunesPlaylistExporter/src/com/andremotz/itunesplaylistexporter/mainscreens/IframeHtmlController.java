package com.andremotz.itunesplaylistexporter.mainscreens;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.TableTracksItem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class IframeHtmlController implements Initializable {
	
	@FXML
	public TextArea txtHtml;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		txtHtml.setText(getHtml2Include());
		
	}

	/*
	 * Generated HTML String that the user can copy & paste
	 * to his blog
	 */
	private String getHtml2Include() {
		String html = "";

		String iframeHeader = "<iframe width=\"420\" height=\"315\" src=\"https://www.youtube.com/embed/";
		String iframeFooter = "\" frameborder=\"0\" allowfullscreen></iframe>";
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = Datahandler
				.getAllTracksInCurrentPlaylist();
		
		/*
		 * Parse each video and its ID that has been chosen
		 */
		for (TableTracksItem currentTableTracksItem : allTracksInCurrentPlaylist) {
			String currentVideoID = currentTableTracksItem.getVideoID();
			
			// concatenate videostrings
			html = html + iframeHeader + currentVideoID + iframeFooter + "\n\n";
		}
		
		return html;
	}

}
