/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *               Used for displaying errors              *
 *           related to datasaving and loading           *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorController {
	
	@FXML Label errorMessageLabel;
	
	private SmartBoard smartBoard;
	private Stage stage;
	
	public void setUp(SmartBoard smartBoard, Stage stage) {
		this.smartBoard = smartBoard;
		this.stage = stage;
		setAlertMessage();
	}
	
	public void setAlertMessage() {
		errorMessageLabel.setText(smartBoard.getAlertMessage());
	}
	
	public void close() {
		stage.close();
	}
}
