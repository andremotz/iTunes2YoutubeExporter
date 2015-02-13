package com.andremotz.itunesplaylistexporter.mainscreens;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.Datahandler;
import com.andremotz.itunesplaylistexporter.datahandling.TableTracksItem;
import com.andremotz.itunesplaylistexporter.datahandling.YoutubeBlackbox;
import com.andremotz.itunesplaylistexporter.datahandling.YoutubeVideo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class PlaylistEditorController implements Initializable {
	
	
	Logger log = LogManager
			.getLogger(PlaylistEditorController.class.getName());
	
	

	@FXML
	public TableView<TableTracksItem> tableTracks;
	public TableColumn<TableTracksItem, String> tableColumnArtist;
	public TableColumn<TableTracksItem, String> tableColumnTrack;
	public TableColumn<TableTracksItem, String> tableColumnUrl;
	public TableColumn<TableTracksItem, YoutubeVideo> tablePreview;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		retrieveYoutubeVideosFromQuery();
		setUpTracksTableView();

	}

	/*
	 * Method retrieves Youtube videos from Query-String
	 */
	private void retrieveYoutubeVideosFromQuery() {

		YoutubeBlackbox youtubeBlackbox = new YoutubeBlackbox();
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = Datahandler
				.getAllTracksInCurrentPlaylist();

		for (TableTracksItem currentTableTracksItem : allTracksInCurrentPlaylist) {

			String currentTrackArtist = currentTableTracksItem.getTrackArtist();
			String currentTrackName = currentTableTracksItem.getTrackName();
			String queryTerm = currentTrackArtist + " - " + currentTrackName;

			ArrayList<YoutubeVideo> youtubeVideosFromQuery = youtubeBlackbox
					.getYoutubeVideosForQuery(queryTerm);

			/*
			 * TODO: Simplify to youtubeVideosFromQuery
			 */
			String videoID = youtubeVideosFromQuery.get(0).getVideoID();
			currentTableTracksItem.setVideoID(videoID);

			YoutubeVideo mostRecentVideo = new YoutubeVideo();
			mostRecentVideo = youtubeVideosFromQuery.get(0);

			currentTableTracksItem.setVideo(mostRecentVideo);
			currentTableTracksItem
					.setYoutubeVideosFromQuery(youtubeVideosFromQuery);
		}

	}

	/*
	 * Sets up TableView to display track data, that has been initialized
	 * before.
	 */
	private void setUpTracksTableView() {
		ArrayList<TableTracksItem> allTracksInCurrentPlaylist = Datahandler
				.getAllTracksInCurrentPlaylist();
		tableTracks.getItems().addAll(allTracksInCurrentPlaylist);

		tableColumnArtist
				.setCellValueFactory(new PropertyValueFactory<TableTracksItem, String>(
						"trackArtist"));
		tableColumnTrack
				.setCellValueFactory(new PropertyValueFactory<TableTracksItem, String>(
						"trackName"));

		tablePreview.setCellValueFactory(new PropertyValueFactory("video"));

		tablePreview
				.setCellFactory(new Callback<TableColumn<TableTracksItem, YoutubeVideo>, TableCell<TableTracksItem, YoutubeVideo>>() {

					@Override
					public TableCell<TableTracksItem, YoutubeVideo> call(
							TableColumn<TableTracksItem, YoutubeVideo> arg0) {
						TableCell<TableTracksItem, YoutubeVideo> cell = new TableCell<TableTracksItem, YoutubeVideo>() {
							@Override
							public void updateItem(YoutubeVideo video,
									boolean empty) {
								if (video != null) {
									HBox box = new HBox();
									box.setSpacing(10);
									VBox vbox = new VBox();
									vbox.getChildren().add(
											new Label(video.getTitle()));
									vbox.getChildren().add(
											new Label(video.getVideoID()));

									ImageView imageview = new ImageView();
									imageview.setFitHeight(50);
									imageview.setFitWidth(50);
									String thumbnailUrl = video.getThumbnailUrl();
									Image image = new Image(thumbnailUrl, 100, 0, false, false);
									imageview.setImage(image);

									box.getChildren().addAll(imageview, vbox);
									setGraphic(box);
								}
							}
						};
						return cell;
					}

				});

	}

	@FXML
	private void handleBtnBackAction(ActionEvent event) {
		log.trace("ActionEvent event()");
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
	
	@FXML
	private void handleBtnIFrameHtml(ActionEvent event) {
		log.trace("handleBtnIFrameHtml()");
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(
					"MaskIframeHtml.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
	
	@FXML
	private void handleBtnExportYoutubePlaylistAction(ActionEvent event) {
		log.trace("handleBtnExportYoutubePlaylistAction()");
		Stage stage = new Stage();
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource(
					"MaskExportYoutubeConfirm.fxml"));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
		
	}
}
