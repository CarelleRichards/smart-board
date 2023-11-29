package application;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignInController {
	
	@FXML TextField usernameInput;
	@FXML TextField passwordInput;
	@FXML Label alertMessageOutput;
	@FXML ImageView logoImage;
	
	private SmartBoard smartBoard;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void setUp(SmartBoard smartBoard) {
		this.smartBoard = smartBoard;
		logoImage.setImage(new Image(smartBoard.getLogoImage().toURI().toString()));
	}
	
	public void submitSignIn(ActionEvent event) throws IOException {
		boolean result = smartBoard.submitSignIn(usernameInput.getText(), passwordInput.getText());
		
		if(result == true) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SmartBoardScene.fxml"));
			root = loader.load();
			SmartBoardController smartBoardController = loader.getController();
			smartBoardController.setUp(smartBoard, smartBoardController);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Smart Board");
			stage.centerOnScreen();
			stage.show();			
		} else {
			alertMessageOutput.setTextFill(Color.RED);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
		}
	}
	
	public void newUser(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("NewUserScene.fxml"));
		root = loader.load();
		NewUserController newUserController = loader.getController();
		newUserController.setUp(smartBoard);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Create a new Smart Board user");
		stage.centerOnScreen();
		stage.show();
	}
	
	public void close() {
		Platform.exit();
	}
}