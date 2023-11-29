/*
 * COSC2288 Further Programming, SP3, 2021
 * Assignment 2: Project Management Application
 * 
 * @author  Carelle Mulawa-Richards (s3749114)
 * @version 1.0
 * @since   21-11-2021
 */

package application;	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

	private SmartBoard smartBoard = new SmartBoard();	

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInScene.fxml"));
		Parent root = loader.load();
		SignInController signInController = loader.getController();
		signInController.setUp(smartBoard);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setTitle("Sign in to Smart Board");
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		boolean dataLoaded = smartBoard.loadData();
		if(dataLoaded == false) {
			errorAlert();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void errorAlert() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ErrorScene.fxml"));
		Parent root = loader.load();		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = new Stage();
		ErrorController errorController = loader.getController();
		errorController.setUp(smartBoard, stage);
		stage.setTitle("Error loading data");
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
}