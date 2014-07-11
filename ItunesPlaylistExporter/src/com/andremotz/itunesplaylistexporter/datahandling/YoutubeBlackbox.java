package com.andremotz.itunesplaylistexporter.datahandling;

import java.util.ArrayList;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemSnippet;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Youtube Blackbox that handles youtube-relevant stuff
 * Copy'n'paste from Google youtube-api help
 * 
 */
public class YoutubeBlackbox {

	static Logger log = Logger.getLogger(YoutubeBlackbox.class.getName());

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;

	/**
	 * Global instance of the max number of videos we want returned (50 = upper
	 * limit per page).
	 */
	private static final long NUMBER_OF_VIDEOS_RETURNED = 10;

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	public YoutubeBlackbox() {

	}

	public ArrayList<YoutubeVideo> getYoutubeVideosForQuery(String queryTerm) {
		ArrayList<YoutubeVideo> currentYoutubeVideos = new ArrayList<YoutubeVideo>();

		try {
			/*
			 * The YouTube object is used to make all API requests. The last
			 * argument is required, but because we don't need anything
			 * initialized when the HttpRequest is initialized, we override the
			 * interface and provide a no-op function.
			 */
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					new HttpRequestInitializer() {
						public void initialize(HttpRequest request)
								throws IOException {
						}
					}).setApplicationName("youtube-cmdline-search-sample")
					.build();

			YouTube.Search.List search = youtube.search().list("id,snippet");
			/*
			 * It is important to set your developer key from the Google
			 * Developer Console for non-authenticated requests (found under the
			 * API Access tab at this link: code.google.com/apis/). This is good
			 * practice and increased your quota.
			 */
			Properties googleProperties = new Properties();
			googleProperties.load(YoutubeBlackbox.class.getClassLoader()
					.getResourceAsStream("google.properties"));
			String apiKey = googleProperties.getProperty("apiKey");
			search.setKey(apiKey);
			search.setQ(queryTerm);
			/*
			 * We are only searching for videos (not playlists or channels). If
			 * we were searching for more, we would add them as a string like
			 * this: "video,playlist,channel".
			 */
			search.setType("video");
			/*
			 * This method reduces the info returned to only the fields we need
			 * and makes calls more efficient.
			 */
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
			SearchListResponse searchResponse = search.execute();

			List<SearchResult> searchResultList = searchResponse.getItems();

			if (searchResultList != null) {
				Iterator<SearchResult> iteratorSearchResults = searchResultList
						.iterator();
				if (!iteratorSearchResults.hasNext()) {
					log.debug(" There aren't any results for your query.");
				}

				while (iteratorSearchResults.hasNext()) {

					SearchResult singleVideo = iteratorSearchResults.next();
					ResourceId rId = singleVideo.getId();

					// Double checks the kind is video.
					if (rId.getKind().equals("youtube#video")) {

						YoutubeVideo currentYoutubeVideo = new YoutubeVideo();

						Thumbnail thumbnail = singleVideo.getSnippet()
								.getThumbnails().get("default");
						currentYoutubeVideo.setThumbnail(thumbnail);

						String videoID = rId.getVideoId();
						currentYoutubeVideo.setVideoID(videoID);

						String title = singleVideo.getSnippet().getTitle();
						currentYoutubeVideo.setTitle(title);

						currentYoutubeVideos.add(currentYoutubeVideo);

					}
				}
			}
		} catch (GoogleJsonResponseException e) {
			log.error("There was a service error: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			log.error("There was an IO error: " + e.getCause() + " : "
					+ e.getMessage());
		} catch (Throwable t) {
			log.error(t.getMessage());
		}

		return currentYoutubeVideos;
	}

	/*
	 * Generates Youtube playlist from all Tracks in current edited playlist
	 */
	public String createYoutubePlaylist(
			ArrayList<TableTracksItem> allTracksInCurrentPlaylist) {

		String returnMessage = null;

		// General read/write scope for YouTube APIs.
		List<String> scopes = Lists
				.newArrayList("https://www.googleapis.com/auth/youtube");

		try {
			// Authorization.
			Credential credential = authorize(scopes);

			// YouTube object used to make all API requests.
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					credential).setApplicationName(
					"youtube-cmdline-playlistupdates-sample").build();

			// Creates a new playlist in the authorized user's channel.
			String playlistId = insertPlaylist();

			// If a valid playlist was created, adds a new playlistitem with a
			// video to that playlist.
			for (TableTracksItem currentTableTracksItem : allTracksInCurrentPlaylist) {
				String currentVideoID = currentTableTracksItem.getVideoID();
				insertPlaylistItem(playlistId, currentVideoID);

			}

		} catch (GoogleJsonResponseException e) {
			log.error("There was a service error: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			log.error("Throwable: " + t.getMessage());
			log.error(t.getStackTrace());
			t.printStackTrace();
		}

		return returnMessage;

	}

	/**
	 * Creates YouTube PlaylistItem with specified video id and adds it to the
	 * specified playlist id for the authorized account.
	 * 
	 * @param playlistId
	 *            assign to newly created playlistitem
	 * @param videoId
	 *            YouTube video id to add to playlistitem
	 */
	private static String insertPlaylistItem(String playlistId, String videoId)
			throws IOException {

		/*
		 * The Resource type (video,playlist,channel) needs to be set along with
		 * the resource id. In this case, we are setting the resource to a video
		 * id, since that makes sense for this playlist.
		 */
		ResourceId resourceId = new ResourceId();
		resourceId.setKind("youtube#video");
		resourceId.setVideoId(videoId);

		/*
		 * Here we set all the information required for the snippet section. We
		 * also assign the resource id from above to the snippet object.
		 */
		PlaylistItemSnippet playlistItemSnippet = new PlaylistItemSnippet();
		playlistItemSnippet.setTitle("First video in the test playlist");
		playlistItemSnippet.setPlaylistId(playlistId);
		playlistItemSnippet.setResourceId(resourceId);

		/*
		 * Now that we have all the required objects, we can create the
		 * PlaylistItem itself and assign the snippet object from above.
		 */
		PlaylistItem playlistItem = new PlaylistItem();
		playlistItem.setSnippet(playlistItemSnippet);

		/*
		 * This is the object that will actually do the insert request and
		 * return the result. The first argument tells the API what to return
		 * when a successful insert has been executed. In this case, we want the
		 * snippet and contentDetails info. The second argument is the
		 * playlistitem we wish to insert.
		 */
		YouTube.PlaylistItems.Insert playlistItemsInsertCommand = youtube
				.playlistItems().insert("snippet,contentDetails", playlistItem);
		PlaylistItem returnedPlaylistItem = playlistItemsInsertCommand
				.execute();

		// Pretty print results.

		log.trace("New PlaylistItem name: "
				+ returnedPlaylistItem.getSnippet().getTitle());
		log.trace(" - Video id: "
				+ returnedPlaylistItem.getSnippet().getResourceId()
						.getVideoId());
		log.trace(" - Posted: "
				+ returnedPlaylistItem.getSnippet().getPublishedAt());
		log.trace(" - Channel: "
				+ returnedPlaylistItem.getSnippet().getChannelId());
		return returnedPlaylistItem.getId();

	}

	/**
	 * Authorizes the installed application to access user's protected data.
	 * 
	 * @param scopes
	 *            list of scopes needed to run upload.
	 */
	private static Credential authorize(List<String> scopes) throws Exception {

		Properties googleProperties = new Properties();
		
		googleProperties.load(YoutubeBlackbox.class.getClassLoader()
				.getResourceAsStream("google.properties"));
		
		String client_id = googleProperties.getProperty("client_id");
		String client_secret = googleProperties.getProperty("client_secret");

		// Load client secrets.
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JSON_FACTORY, YoutubeBlackbox.class
						.getResourceAsStream("/client_secrets.json"));

		// Checks that the defaults have been replaced (Default =
		// "Enter X here").
		if (client_id.startsWith("Enter") || client_secret.startsWith("Enter ")) {
			log.debug("Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
					+ "into youtube-cmdline-playlistupdates-sample/src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// Set up file credential store.
		FileCredentialStore credentialStore = new FileCredentialStore(new File(
				System.getProperty("user.home"),
				".credentials/youtube-api-playlistupdates.json"), JSON_FACTORY);

		// Set up authorization code flow.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes)
				.setCredentialStore(credentialStore).build();

		// Build the local server and bind it to port 9000
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder()
				.setPort(8080).build();

		// Authorize.
		return new AuthorizationCodeInstalledApp(flow, localReceiver)
				.authorize("user");
	}

	/**
	 * Creates YouTube Playlist and adds it to the authorized account
	 */
	private static String insertPlaylist() throws IOException {

		/*
		 * We need to first create the parts of the Playlist before the playlist
		 * itself. Here we are creating the PlaylistSnippet and adding the
		 * required data.
		 */
		PlaylistSnippet playlistSnippet = new PlaylistSnippet();
		playlistSnippet.setTitle("Test Playlist "
				+ Calendar.getInstance().getTime());
		playlistSnippet
				.setDescription("A private playlist created with the YouTube API v3");

		// Here we set the privacy status (required).
		PlaylistStatus playlistStatus = new PlaylistStatus();
		playlistStatus.setPrivacyStatus("private");

		/*
		 * Now that we have all the required objects, we can create the Playlist
		 * itself and assign the snippet and status objects from above.
		 */
		Playlist youTubePlaylist = new Playlist();
		youTubePlaylist.setSnippet(playlistSnippet);
		youTubePlaylist.setStatus(playlistStatus);

		/*
		 * This is the object that will actually do the insert request and
		 * return the result. The first argument tells the API what to return
		 * when a successful insert has been executed. In this case, we want the
		 * snippet and contentDetails info. The second argument is the playlist
		 * we wish to insert.
		 */
		YouTube.Playlists.Insert playlistInsertCommand = youtube.playlists()
				.insert("snippet,status", youTubePlaylist);
		Playlist playlistInserted = playlistInsertCommand.execute();

		// Pretty print results.

		log.trace("New Playlist name: "
				+ playlistInserted.getSnippet().getTitle());
		log.trace(" - Privacy: "
				+ playlistInserted.getStatus().getPrivacyStatus());
		log.trace(" - Description: "
				+ playlistInserted.getSnippet().getDescription());
		log.trace(" - Posted: "
				+ playlistInserted.getSnippet().getPublishedAt());
		log.trace(" - Channel: " + playlistInserted.getSnippet().getChannelId()
				+ "\n");
		return playlistInserted.getId();

	}
}
