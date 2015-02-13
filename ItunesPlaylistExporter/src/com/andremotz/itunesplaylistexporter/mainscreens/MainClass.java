package com.andremotz.itunesplaylistexporter.mainscreens;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andremotz.itunesplaylistexporter.datahandling.GlobalFunctions;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClass extends Application {

	Logger log = LogManager
			.getLogger(MainClass.class.getName());

	@Override
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
