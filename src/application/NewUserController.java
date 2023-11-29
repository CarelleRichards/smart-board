package application;
import java.io.File;
import java.io.IOException;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class NewUserController {
	
	@FXML private TextField usernameInput;
	@FXML private TextField passwordInput;
	@FXML private TextField firstNameInput;
	@FXML private TextField lastNameInput;
	@FXML private TextField profilePhotoInput;
	@FXML private Label alertMessageOutput;
	@FXML private ImageView profilePhotoImageView;
	
	private SmartBoard smartBoard;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private File profilePhoto;
	
	public void setUp(SmartBoard smartBoard) {
		this.smartBoard = smartBoard;
		profilePhotoImageView.setImage(new Image(smartBoard.getdefaultProfilePhoto().toURI().toString()));
	}
	
	public void selectProfilePhoto() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose Profile Photo");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.PNG", "*.JPG"));
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		profilePhoto = fileChooser.showOpenDialog(null);

		if(profilePhoto != null) {
			if(smartBoard.fileValidation(profilePhoto)) {
				profilePhotoImageView.setImage(new Image(profilePhoto.toURI().toString()));
			} else {
				alertMessageOutput.setTextFill(Color.RED);
				alertMessageOutput.setText(smartBoard.getAlertMessage());
				profilePhotoImageView.setImage(new Image(smartBoard.getdefaultProfilePhoto().toURI().toString()));
			}
		}
	}
	
	public void submitNewUser() throws IOException {
		boolean result = smartBoard.submitNewUser(usernameInput.getText(), 
				passwordInput.getText(), firstNameInput.getText(), 
				lastNameInput.getText(), profilePhoto);
		
		if(result == true) {
			alertMessageOutput.setTextFill(Color.GREEN);
			boolean dataSaved = smartBoard.saveData();
			if(dataSaved == false) {
				errorAlert();
			}
		} else {
			alertMessageOutput.setTextFill(Color.RED);	
		}
		alertMessageOutput.setText(smartBoard.getAlertMessage());
	}
	
	public void close(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInScene.fxml"));
		root = loader.load();
        SignInController signInController = loader.getController();
		signInController.setUp(smartBoard);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
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