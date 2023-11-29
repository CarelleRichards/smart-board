
package application;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditProfileController {
	
	@FXML Label usernameLabel;
	@FXML Label alertMessageOutput;
	@FXML TextField firstNameTextField;
	@FXML TextField lastNameTextField;
	@FXML TextField profilePhotoTextField;
	@FXML ImageView profilePhotoImageView;
	
	private SmartBoard smartBoard;
	private SmartBoardController smartBoardController;
	private Stage stage;
	private File profilePhoto;
	
	public void setUp(SmartBoard smartBoard, SmartBoardController smartBoardController, Stage stage) {
		this.smartBoard = smartBoard;
		this.smartBoardController = smartBoardController;
		this.stage = stage;	
		usernameLabel.setText(smartBoard.getCurrentUser().getUsername());
		firstNameTextField.setText(smartBoard.getCurrentUser().getFirstName());
		lastNameTextField.setText(smartBoard.getCurrentUser().getLastName());
		profilePhotoImageView.setImage(new Image(smartBoard.getCurrentUser().getProfilePhoto().toURI().toString()));
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
	
	public void submitChanges() throws IOException {
		boolean result = smartBoard.submitEditProfile(firstNameTextField.getText(), 
				lastNameTextField.getText(), profilePhoto);
		
		if(result == true) {
			alertMessageOutput.setTextFill(Color.GREEN);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
			smartBoardController.loadProfilePhoto();
			boolean dataSaved = smartBoard.saveData();
			if(dataSaved == false) {
				errorAlert();
			} 
		} else {
			alertMessageOutput.setTextFill(Color.RED);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
		}
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
	
	public void close() {
		stage.close();
	}
}
