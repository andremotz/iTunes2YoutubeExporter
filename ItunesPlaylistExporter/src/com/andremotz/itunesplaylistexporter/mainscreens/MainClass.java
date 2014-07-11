package com.andremotz.itunesplaylistexporter.mainscreens;

import org.apache.log4j.Logger;

import com.andremotz.itunesplaylistexporter.annotations.ClassPreamble;
import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClass extends Application {

	static Logger log = Logger.getLogger(MainClass.class.getName());

	@Override
	@ClassPreamble(author = "Andre Motz", date = "11/02/2013", currentRevision = 1, lastModified = "11/12/2013", lastModifiedBy = "Andr?? Motz", notes = "")
	public void start(Stage stage) throws Exception {
		log.trace(GlobalFunctions.getMethodName(0));
		Parent root = FXMLLoader.load(getClass().getResource(
				"MaskLibraryChooser.fxml"));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
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
