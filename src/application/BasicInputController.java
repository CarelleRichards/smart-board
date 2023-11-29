/*
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *             Used for creating and renaming            *
 *         Project Boards, Columns and Action Items      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */

package application;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BasicInputController {

	@FXML TextField textInput;
	@FXML Label alertMessageOutput;

	private SmartBoard smartBoard;
	private String windowName;
	private SmartBoardController smartBoardController;
	private Stage stage;
	private TaskController taskSceneController;
	
	public void setUp(SmartBoard smartBoard, String windowName, 
			SmartBoardController smartBoardController, Stage stage) {
		
		this.smartBoard = smartBoard;
		this.windowName = windowName;
		this.smartBoardController = smartBoardController;
		this.stage = stage;
		if(windowName.equals("Rename Project Board")) {
			textInput.setText(smartBoard.getCurrentProjectBoard().getProjectBoardName());
		} else if(windowName.equals("Rename Column")) {
			textInput.setText(smartBoard.getCurrentColumn().getColumnName());
		}
	}
	
	public void setUp(SmartBoard smartBoard, String windowName, 
			TaskController taskSceneController, Stage stage) {
		
		this.smartBoard = smartBoard;
		this.windowName = windowName;
		this.taskSceneController = taskSceneController;
		this.stage = stage;
		if(windowName.equals("Rename Action Item")) {
			textInput.setText(smartBoard.getCurrentActionItem().getActionItemName());
		}
	}

	public void submitChanges() throws IOException {
		boolean result;
		if(taskSceneController != null) {
			if(windowName.equals("Add Action Item")) {	
				result = smartBoard.addTempActionItem(textInput.getText());
			} else {
				result = smartBoard.renameTempActionItem(textInput.getText());
			}
			if(result == true) {
				taskSceneController.loadActionItems();
				alertMessageOutput.setTextFill(Color.GREEN);
				alertMessageOutput.setText(smartBoard.getAlertMessage());
			} else {
				alertMessageOutput.setTextFill(Color.RED);
				alertMessageOutput.setText(smartBoard.getAlertMessage());
			}
		} else {	
			if(windowName.equals("Rename Project Board")) {
				result = smartBoard.submitRenameProjectBoard(textInput.getText());	
			} else if(windowName.equals("Rename Column")) {
				result = smartBoard.submitRenameColumn(textInput.getText());	
			} else if(windowName.equals("New Project Board")) {
				result = smartBoard.submitNewProjectBoard(textInput.getText());
			} else {
				result = smartBoard.submitNewColumn(textInput.getText());	
			}
			if(result == true) {
				if(smartBoard.getCurrentUser().getProjectBoards().size() < 2) {
					smartBoardController.loadProjectBoardTabs("Default");
				} else {
					smartBoardController.getSelectedProjectBoard();
					smartBoardController.loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName());
					alertMessageOutput.setTextFill(Color.GREEN);
					alertMessageOutput.setText(smartBoard.getAlertMessage());	
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
