/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *             Used for confirming deletion of           *
 *    Project Boards, Columns, Tasks and Action Items    *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package application;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ConfirmationController {
	
	@FXML Label alertMessageOutput;
	@FXML Label messageLabel;
	@FXML Button confirmSubmitButton;
	
	private SmartBoard smartBoard;
	private SmartBoardController smartBoardController;
	private String windowName;
	private Stage stage;
	private TaskController taskSceneController;
	
	public void setUp(SmartBoard smartBoard, String windowName, 
			SmartBoardController smartBoardController, Stage stage) {
		
		this.smartBoard = smartBoard;
		this.smartBoardController = smartBoardController;
		this.windowName = windowName;
		this.stage = stage;
		setMessageLabel();
	}
	
	public void setUp(SmartBoard smartBoard, String windowName, 
			TaskController taskSceneController, Stage stage) {
		
		this.smartBoard = smartBoard;
		this.taskSceneController = taskSceneController;
		this.windowName = windowName;
		this.stage = stage;
		setMessageLabel();
	}
	
	public void setMessageLabel() {
		if(windowName.equals("Delete Project Board")) {
			messageLabel.setText("Are you sure you want to delete '" + 
					smartBoard.getCurrentProjectBoard().getProjectBoardName() + "'?");
		} else if(windowName.equals("Delete Column")) {
			messageLabel.setText("Are you sure you want to delete '" +
					smartBoard.getCurrentColumn().getColumnName() + "'?");
		} else if(windowName.equals("Delete Task")) {
			messageLabel.setText("Are you sure you want to delete '" +
					smartBoard.getCurrentTask().getTaskName() + "'?");
		} else {
			messageLabel.setText("Are you sure you want to delete '" +
					smartBoard.getCurrentActionItem().getActionItemName() + "'?");
		}
	}
	
	public void confirmSubmit() throws IOException {
		boolean result;
		if(windowName.equals("Delete Project Board")) {
			result = smartBoard.submitDeleteProjectBoard();	
		} else if(windowName.equals("Delete Column")) {
			result = smartBoard.submitDeleteColumn();
		}  else if(windowName.equals("Delete Task")) {
			result = smartBoard.submitDeleteTask();
		} else {
			result = smartBoard.deleteTempActionItem();
		}
		if(result == true) {
			alertMessageOutput.setTextFill(Color.GREEN);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
			confirmSubmitButton.setDisable(true);
			if(windowName.equals("Delete Action Item")) {
				taskSceneController.loadProgressBar();
				taskSceneController.loadActionItems();
			} else if(windowName.equals("Delete Project Board")) {
				smartBoardController.loadProjectBoardTabs("Default");
			} else {
				smartBoardController.loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName());
			} 
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