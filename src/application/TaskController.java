package application;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TaskController {
	
	@FXML TextField taskName;
	@FXML TextArea taskDescription;
	@FXML Label alertMessageOutput;
	@FXML VBox dueDateEnabledVbox;
	@FXML Hyperlink addDueDateHyperlink;
	@FXML Hyperlink addChecklistHyperlink;
	@FXML VBox checklistEnabledVbox;
	@FXML DatePicker dueDatePicker;
	@FXML Label statusLabel;
	@FXML CheckBox completionCheckBox;
	@FXML VBox actionItemVbox;
	@FXML ProgressBar checklistProgressBar;
	@FXML Label checklistPercentageLabel;
	
	private SmartBoard smartBoard;
	private String windowName;
	private SmartBoardController smartBoardController;
	private Stage stage;
	private boolean dueDateOption = false;
	private boolean checklistOption = false;
	private boolean completionStatus = false;
	private TaskController taskSceneController;
	
	public void setUp(SmartBoard smartBoard, String windowName, 
			SmartBoardController smartBoardController, 
			TaskController taskSceneController, Stage stage) {
		
		this.smartBoard = smartBoard;
		this.windowName = windowName;
		this.smartBoardController = smartBoardController;
		this.stage = stage;
		this.taskSceneController = taskSceneController;
		smartBoard.getTempActionItems().clear();

		if(windowName.equals("Edit task")) {	
			if(smartBoard.getCurrentTask().getTaskDueDate() != null) {
				dueDateOption = true;
				dueDatePicker.setValue(smartBoard.getCurrentTask().getTaskDueDate());
				completionStatus = smartBoard.getCurrentTask().getCompletionStatus();
				completionCheckBox.setSelected(completionStatus);
			}
			if(smartBoard.getCurrentTask().getActionItems().size() > 0) {
				checklistOption = true;
				smartBoard.makeTempActionItemList();
			}
			taskName.setText(smartBoard.getCurrentTask().getTaskName());
			taskDescription.setText(smartBoard.getCurrentTask().getTaskDescription());
		}
		loadActionItems();
		loadStatus();
		loadDueDateVbox();
		loadChecklistVbox();
	}
	
	public void loadStatus() {
		statusLabel.setText(smartBoard.checkProgressStatus(dueDatePicker.getValue(), completionStatus));
	}
	
	public void addDueDateOption() {
		dueDateOption = true;
		dueDatePicker.setValue(LocalDate.now());
		completionStatus = false;
		loadDueDateVbox();
	}
	
	public void removeDueDateOption() {
		dueDateOption = false;
		dueDatePicker.setValue(null);
		completionStatus = false;
		completionCheckBox.setSelected(completionStatus);
		loadDueDateVbox();
	}
	
	public void completionCheckBox() {
		if(completionCheckBox.isSelected()) {
			completionStatus = true;
		} else {
			completionStatus = false;
		}
		loadStatus();
	}
	
	public void loadDueDateVbox() {
		if(dueDateOption == true) {	
			dueDateEnabledVbox.setVisible(true);
			dueDateEnabledVbox.setManaged(true);
			addDueDateHyperlink.setVisible(false);
			addDueDateHyperlink.setManaged(false);	
			
		} else {
			dueDateEnabledVbox.setVisible(false);
			dueDateEnabledVbox.setManaged(false);
			addDueDateHyperlink.setVisible(true);
			addDueDateHyperlink.setManaged(true);
		}
	}

	public void submit() throws IOException {
		boolean result;
		if(windowName.equals("New task")) {
			result = smartBoard.submitNewTask(taskName.getText(), taskDescription.getText(), 
					dueDatePicker.getValue(), completionStatus);
		} else {
			result = smartBoard.submitEditTask(taskName.getText(), taskDescription.getText(), 
					dueDatePicker.getValue(), completionStatus);
		}
		if(result == true) {
			boolean dataSaved = smartBoard.saveData();
			if(dataSaved == false) {
				errorAlert();
			}
			smartBoardController.loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName());
			alertMessageOutput.setTextFill(Color.GREEN);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
		} else {
			alertMessageOutput.setTextFill(Color.RED);
			alertMessageOutput.setText(smartBoard.getAlertMessage());
		}
	}
	
	public void loadChecklistVbox() {
		if(checklistOption == true) {
			checklistEnabledVbox.setVisible(true);
			checklistEnabledVbox.setManaged(true);
			addChecklistHyperlink.setVisible(false);
			addChecklistHyperlink.setManaged(false);	
			
		} else {
			checklistEnabledVbox.setVisible(false);
			checklistEnabledVbox.setManaged(false);
			addChecklistHyperlink.setVisible(true);
			addChecklistHyperlink.setManaged(true);	
		}
	}
	
	public void addChecklistOption() {
		checklistOption = true;
		loadProgressBar();
		loadActionItems();
		loadChecklistVbox();
	}
	
	public void removeChecklistOption() {
		checklistOption = false;
		smartBoard.getTempActionItems().clear();
		loadChecklistVbox();
	}
	
	public void addActionItem() throws IOException {
		basicInput("Add Action Item");
	}
	
	public void editActionItem() throws IOException {
		basicInput("Rename Action Item");
	}

	public void loadActionItems() {
		actionItemVbox.getChildren().clear();
		for(ActionItem existingActionItem : smartBoard.getTempActionItems()) {
			CheckBox actionItemCheckBox = new CheckBox(existingActionItem.getActionItemName());
			actionItemCheckBox.setSelected(existingActionItem.getActionItemCompletionStatus());
			actionItemCheckBox.setOnAction(e -> {
				if(actionItemCheckBox.isSelected()) {
					existingActionItem.setActionItemCompletionStatus(true);
					loadProgressBar();
				} else {
					existingActionItem.setActionItemCompletionStatus(false);
					loadProgressBar();
				}
			});

			Hyperlink editHyperlink = new Hyperlink("Edit");
			editHyperlink.setOnAction(e -> {
				smartBoard.setCurrentActionItem(existingActionItem);
				try {
					basicInput("Rename Action Item");
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			});
			
			Hyperlink deleteHyperlink = new Hyperlink("Delete");
			deleteHyperlink.setOnAction(e -> {
				smartBoard.setCurrentActionItem(existingActionItem);
				try {
					confirmation("Delete Action Item");
				} catch (IOException exception) {
					exception.printStackTrace();
				}	
			});
			
			HBox linksHbox = new HBox();
			linksHbox.getChildren().addAll(editHyperlink, deleteHyperlink);

			GridPane actionItemGridPane = new GridPane();
			actionItemGridPane.addRow(0, actionItemCheckBox, linksHbox);
			GridPane.setHalignment(linksHbox, HPos.RIGHT);
		    
			ColumnConstraints col1 = new ColumnConstraints();
		    col1.setPercentWidth(80);
		    ColumnConstraints col2 = new ColumnConstraints();
		    col2.setPercentWidth(20);
		    actionItemGridPane.getColumnConstraints().addAll(col1, col2);
		
			actionItemVbox.getChildren().add(actionItemGridPane);
			loadProgressBar();
		}
	}
	
	public void loadProgressBar() {
		String percentage= String.format("%.0f", smartBoard.actionItemPercentage());
		checklistPercentageLabel.setText(percentage + "%");
		checklistProgressBar.setProgress(smartBoard.actionItemPercentage() / 100);
	}
	
	public void basicInput(String windowName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BasicInputScene.fxml"));
		Parent root = loader.load();		
		BasicInputController basicInputController = loader.getController();
		Stage stage = new Stage();
		basicInputController.setUp(smartBoard, windowName, taskSceneController, stage);
		stage.setScene(new Scene(root));
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.centerOnScreen();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);		
		stage.setResizable(false);
		stage.show();
	}
	
	public void confirmation(String windowName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationScene.fxml"));
		Parent root = loader.load();		
		ConfirmationController confirmationController = loader.getController();
		Stage stage = new Stage();
		confirmationController.setUp(smartBoard, windowName, taskSceneController, stage);
		stage.setScene(new Scene(root));
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.centerOnScreen();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);		
		stage.setResizable(false);
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
	
	public void close() {
		stage.close();
	}
}
