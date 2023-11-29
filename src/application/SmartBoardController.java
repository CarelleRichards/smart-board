package application;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SmartBoardController {

	@FXML Label currentUserLabel;
	@FXML Label bookQuoteLabel;
	@FXML TabPane tabPane;
	@FXML MenuItem setDefaultMenuItem;
	@FXML MenuItem unsetDefaultMenuItem;
	@FXML MenuItem deleteProjectMenuItem;
	@FXML MenuItem renameProjectMenuItem;
	@FXML MenuItem addColumnMenuItem;
	@FXML ImageView profilePhotoImageView;

	private SmartBoard smartBoard;
	private SmartBoardController smartBoardController;
	private Stage stage;
	private Scene scene;
	private Parent root;	
	private static final String VBOX_DRAG_KEY = "vbox";

	public void setUp(SmartBoard smartBoard, SmartBoardController smartBoardController) {
		this.smartBoard = smartBoard;
		currentUserLabel.setText(smartBoard.getCurrentUser().getUsername());
		bookQuoteLabel.setText(smartBoard.getRandomQuote());
		loadProfilePhoto();
		loadProjectBoardTabs("Default");
		this.smartBoardController = smartBoardController;
	}
	
	public void loadProfilePhoto() {
		profilePhotoImageView.setImage(new Image(smartBoard.getCurrentUser().getProfilePhoto().toURI().toString()));
	}

	public void loadProjectBoardTabs(String selectedBoard) {
		tabPane.getTabs().clear();
	
		for(ProjectBoard existingProjectBoard : smartBoard.getCurrentUser().getProjectBoards()) {	
			HBox hBox = new HBox();
			Tab tab = new Tab(existingProjectBoard.getProjectBoardName(), hBox);
			tabPane.getTabs().add(tab);
			tabPane.setPrefHeight(Control.USE_COMPUTED_SIZE);
			tabPane.setPrefWidth(Control.USE_COMPUTED_SIZE);
			
			if(existingProjectBoard.getDefaultStatus() == true) {
				tab.setGraphic(new Circle(0, 0, 5));
			}	
			if(selectedBoard.equals("Default") && existingProjectBoard.getDefaultStatus() == true){
				tabPane.getSelectionModel().select(tab);
			} else if(selectedBoard.equals(existingProjectBoard.getProjectBoardName())) {
				tabPane.getSelectionModel().select(tab);
			}
			for(Column existingColumn : existingProjectBoard.getColumns()) {
				VBox columnVbox = loadColumnVbox(existingColumn);
				hBox.getChildren().add(columnVbox);				
				
				for(Task existingTask : existingColumn.getTasks()) {
					VBox taskVbox = loadTaskVbox(existingTask, existingColumn);
					columnVbox.getChildren().add(taskVbox);
				}
				VBox trailingVbox = loadTrailingVbox(existingColumn);
				columnVbox.getChildren().add(trailingVbox);
			}	
		}
	}	
	
	public VBox loadTrailingVbox(Column existingColumn) {
		VBox trailingVbox = new VBox();
		trailingVbox.setPrefHeight(80);
		
		trailingVbox.setOnDragOver(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && 
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null) {	
                	smartBoard.setHoverLocation(existingColumn.getTasks().size());
                	smartBoard.setHoverColumn(existingColumn);
                	event.acceptTransferModes(TransferMode.MOVE);
                	event.consume();
                }
        	}
		});
		
		trailingVbox.setOnDragDropped(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
        		if (dragboard.hasString() &&  
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null) {
        			getSelectedProjectBoard();
        			smartBoard.getCopiedColumn().getTasks().remove(smartBoard.getCopiedTask());
        			smartBoard.getHoverColumn().getTasks().add(smartBoard.getHoverLocation(), smartBoard.getCopiedTask());
        			smartBoard.setCopiedTask(null);
        			loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName()); 
        			event.setDropCompleted(true);
                    event.consume();
        		}
        	}
        });
		return trailingVbox;
	}
	
	public VBox loadColumnVbox(Column existingColumn) {
		SplitMenuButton splitMenuButton = new SplitMenuButton();
		splitMenuButton.setText("Add Task");
		splitMenuButton.setOnAction(e -> {
			try {
				newTask(existingColumn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});

		MenuItem menuItem1 = new MenuItem("Rename Column");	
		menuItem1.setOnAction(e -> {
			try {
				renameColumn(existingColumn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});
		
		MenuItem menuItem2 = new MenuItem("Delete Column");
		menuItem2.setOnAction(e -> {
			try {
				deleteColumn(existingColumn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});

		splitMenuButton.getItems().addAll(menuItem1, menuItem2);
		
		Label columnNameLabel = new Label(existingColumn.getColumnName());
		columnNameLabel.setWrapText(true);
		
		GridPane gridPane = new GridPane();
		gridPane.addRow(0, columnNameLabel, splitMenuButton);
		GridPane.setHalignment(splitMenuButton, HPos.RIGHT);
	    ColumnConstraints col1 = new ColumnConstraints();
	    col1.setPercentWidth(60);
	    ColumnConstraints col2 = new ColumnConstraints();
	    col2.setPercentWidth(40);
	    gridPane.getColumnConstraints().addAll(col1, col2);
		gridPane.setStyle("-fx-border-color: #bebebe; "
				+ "-fx-background-color: #ffffff;"
				+ "-fx-padding: 10;"
				+ "-fx-border-radius: 3;"
				+ "-fx-background-radius: 3;");
		gridPane = addOnDragOver(gridPane, existingColumn);
		gridPane = addOnDragDropped(gridPane, existingColumn);
		
		VBox columnVbox = new VBox();
		columnVbox.setPrefHeight(Control.USE_COMPUTED_SIZE);

		columnVbox.setPadding(new Insets(10, 10, 10, 10));
		columnVbox.setSpacing(10);
		columnVbox.setPrefWidth(300);
		columnVbox.getChildren().add(gridPane);
		return columnVbox;
	}
	
	public GridPane addOnDragOver(GridPane gridPane, Column existingColumn) {
		gridPane.setOnDragOver(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
                if (dragboard.hasString() && 
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null) {	
                	smartBoard.setHoverLocation(0);
                	smartBoard.setHoverColumn(existingColumn);
                	event.acceptTransferModes(TransferMode.MOVE);
                	event.consume();
                }
        	}
		});
		return gridPane;
	}
	
	public GridPane addOnDragDropped(GridPane gridPane, Column existingColumn) {
		gridPane.setOnDragDropped(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
        		if (dragboard.hasString() &&  
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null) {
        			getSelectedProjectBoard();
        			smartBoard.getCopiedColumn().getTasks().remove(smartBoard.getCopiedTask());
        			smartBoard.getHoverColumn().getTasks().add(smartBoard.getHoverLocation(), smartBoard.getCopiedTask());
        			smartBoard.setCopiedTask(null);
        			loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName()); 
        			event.setDropCompleted(true);
                    event.consume();
        		}
        	}
        });
		return gridPane;
	}

	public VBox loadTaskVbox(Task existingTask, Column existingColumn) {
		Hyperlink editHyperlink = new Hyperlink("Edit");
		editHyperlink.setOnAction(e -> {
			try {
				editTask(existingTask, existingColumn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});
		
		Hyperlink deleteHyperlink = new Hyperlink("Delete");
		deleteHyperlink.setOnAction(e -> {
			try {
				deleteTask(existingTask, existingColumn);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		});
		
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.TOP_RIGHT);
		hBox.getChildren().addAll(editHyperlink, deleteHyperlink);

		Label taskNameLabel = new Label(existingTask.getTaskName());
		Label taskDescriptionLabel = new Label(existingTask.getTaskDescription());
		taskDescriptionLabel.setPadding(new Insets(5, 0, 0, 0));
		taskNameLabel.setWrapText(true);
		taskNameLabel.getStyleClass().add("boldHeading");
		taskDescriptionLabel.setWrapText(true);
		
		FlowPane taskFlowPane = new FlowPane();
		taskFlowPane.setHgap(5);

		if(existingTask.getTaskDueDate() != null) {
			Label dueDateLabel = loadDueDateLabel(existingTask);
			taskFlowPane.getChildren().add(dueDateLabel);
			FlowPane.setMargin(dueDateLabel, new Insets(5, 0, 0, 0));
		}
		if(existingTask.getActionItems().size() != 0) {
			Label checklistLabel = loadChecklistLabel(existingTask);
			taskFlowPane.getChildren().add(checklistLabel);
			FlowPane.setMargin(checklistLabel, new Insets(5, 0, 0, 0));
		}
		
		VBox taskVbox = new VBox();
		taskVbox.setStyle("-fx-border-color: #bebebe; -fx-background-color: #ffffff;"
				+ "-fx-padding: 10; -fx-border-radius: 3; -fx-background-radius: 3;");
		taskVbox = addOnDrag(taskVbox, existingTask, existingColumn);
		taskVbox = addOnDragOver(taskVbox, existingTask, existingColumn);
		taskVbox = addOnDragDropped(taskVbox, existingTask, existingColumn);
		taskVbox.getChildren().addAll(hBox, taskNameLabel, taskDescriptionLabel, taskFlowPane);
		return taskVbox;
	}
	
	public Label loadDueDateLabel(Task existingTask) {	
		File timerPhoto = new File("Timer.png");
		ImageView timerImageView = new ImageView();
		timerImageView.setImage(new Image(timerPhoto.toURI().toString()));
		timerImageView.setPreserveRatio(true);
		timerImageView.setFitHeight(17);
		
		Label dueDateLabel = new Label(existingTask.getFormattedDueDate());
		dueDateLabel.setGraphic(timerImageView);
		
		String progressStatus = smartBoard.checkProgressStatus(existingTask.getTaskDueDate(), existingTask.getCompletionStatus());
		if(progressStatus.equals("[COMPLETE]")) {
			dueDateLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #a4c950;");
		} else if(progressStatus.equals("[OVERDUE]")) {
			dueDateLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #f29d70;");
		} else if(progressStatus.equals("[ON TRACK]"))  {
			long days = LocalDate.now().until(existingTask.getTaskDueDate(), ChronoUnit.DAYS);
			if(days < 7) {
				dueDateLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #f8cc46;");
			}
			else {
				dueDateLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #d9d9d9;");
			}
		}
		return dueDateLabel;
	}
	
	public Label loadChecklistLabel(Task existingTask) {
		File checklistPhoto = new File("Checklist.png");
		ImageView checklistImageView = new ImageView();
		checklistImageView.setImage(new Image(checklistPhoto.toURI().toString()));
		checklistImageView.setPreserveRatio(true);
		checklistImageView.setFitHeight(17);
		
		int total = existingTask.getActionItems().size();
		int completed = existingTask.actionItemsCompleted();
		Label checklistLabel = new Label(completed + "/" + total);
		checklistLabel.setGraphic(checklistImageView);
		
		if(completed == total) {
			checklistLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #a4c950;");
		} else {
			checklistLabel.setStyle("-fx-background-radius: 3; -fx-padding: 4; -fx-background-color: #d9d9d9;");
		}
		return checklistLabel;
	}
	
	
	public VBox addOnDrag(VBox taskVbox, Task existingTask, Column existingColumn) {
		taskVbox.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Dragboard dragboard = taskVbox.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent clipboardContent = new ClipboardContent();
				clipboardContent.putString(VBOX_DRAG_KEY);
				dragboard.setContent(clipboardContent);
				smartBoard.setCopiedTask(existingTask);
				smartBoard.setCopiedColumn(existingColumn);
				event.consume();
			}
		});
		return taskVbox;
	}
	
	public VBox addOnDragOver(VBox taskVbox, Task existingTask, Column existingColumn) {
		taskVbox.setOnDragOver(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
        		
                if (dragboard.hasString() && 
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null && 
                		smartBoard.getCopiedTask().getTaskName() != existingTask.getTaskName()) {	
                	smartBoard.setHoverLocation(existingColumn.getTasks().indexOf(existingTask));
                	smartBoard.setHoverColumn(existingColumn);
                	event.acceptTransferModes(TransferMode.MOVE);
                	event.consume();
                }
        	}
		});
		return taskVbox;
	}
	
	public VBox addOnDragDropped(VBox taskVbox, Task existingTask, Column existingColumn) {
		taskVbox.setOnDragDropped(new EventHandler<DragEvent>() {
        	@Override
            public void handle(DragEvent event) {
        		final Dragboard dragboard = event.getDragboard();
        		if (dragboard.hasString() &&  
                		VBOX_DRAG_KEY.equals(dragboard.getString()) &&
                		smartBoard.getCopiedTask() != null && 
                		smartBoard.getCopiedTask().getTaskName() != existingTask.getTaskName()) {
        			getSelectedProjectBoard();
        			smartBoard.getCopiedColumn().getTasks().remove(smartBoard.getCopiedTask());
        			smartBoard.getHoverColumn().getTasks().add(smartBoard.getHoverLocation(), smartBoard.getCopiedTask());
        			smartBoard.setCopiedTask(null);
        			loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName()); 
        			event.setDropCompleted(true);
                    event.consume();
        		}
        	}
        });
		return taskVbox;
	}
	
	public void getSelectedProjectBoard() {
		int index = tabPane.getSelectionModel().getSelectedIndex();
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(index));
	}

	public void projectMenu() {
		if(smartBoard.getCurrentUser().getProjectBoards().size() == 0) {
			setDefaultMenuItem.setDisable(true);
			unsetDefaultMenuItem.setDisable(true);
			deleteProjectMenuItem.setDisable(true);
			renameProjectMenuItem.setDisable(true);
			addColumnMenuItem.setDisable(true);
		} else {
			getSelectedProjectBoard();
			deleteProjectMenuItem.setDisable(false);
			renameProjectMenuItem.setDisable(false);
			addColumnMenuItem.setDisable(false);
			if(smartBoard.getCurrentProjectBoard().getDefaultStatus()) {
				setDefaultMenuItem.setDisable(true);
				unsetDefaultMenuItem.setDisable(false);
			} else {
				setDefaultMenuItem.setDisable(false);
				unsetDefaultMenuItem.setDisable(true);
			}
		}
	}
	
	public void newProjectBoard() throws IOException {
		basicInput("New Project Board");
	}
	
	public void renameProjectBoard() throws IOException {
		basicInput("Rename Project Board");
	}
	
	public void deleteProjectBoard() throws IOException {
		confirmation("Delete Project Board");
	}
	
	public void setDefault() throws IOException {	
		smartBoard.setDefault();
		loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName());
		boolean dataSaved = smartBoard.saveData();
		if(dataSaved == false) {
			errorAlert();
		} 
	}
	
	public void unsetDefault() throws IOException {
		smartBoard.unsetDefault();
		loadProjectBoardTabs(smartBoard.getCurrentProjectBoard().getProjectBoardName());
		boolean dataSaved = smartBoard.saveData();
		if(dataSaved == false) {
			errorAlert();
		} 
	}
	
	public void newColumn() throws IOException {
		basicInput("New Column");
	}
	
	public void renameColumn(Column existingColumn) throws IOException  {
		getSelectedProjectBoard();
		smartBoard.setCurrentColumn(existingColumn);
		basicInput("Rename Column");
	}
	
	public void deleteColumn(Column existingColumn) throws IOException {
		getSelectedProjectBoard();
		smartBoard.setCurrentColumn(existingColumn);
		confirmation("Delete Column");
	}
	
	public void deleteTask(Task existingTask, Column existingColumn) throws IOException {
		getSelectedProjectBoard();
		smartBoard.setCurrentColumn(existingColumn);
		smartBoard.setCurrentTask(existingTask);
		confirmation("Delete Task");	
	}
	
	public void editTask(Task existingTask, Column existingColumn) throws IOException {
		getSelectedProjectBoard();
		smartBoard.setCurrentColumn(existingColumn);
		smartBoard.setCurrentTask(existingTask);
		taskInput("Edit task");
	}
	
	public void newTask(Column existingColumn) throws IOException {
		getSelectedProjectBoard();
		smartBoard.setCurrentColumn(existingColumn);
		taskInput("New task");
	}
	
	public void taskInput(String windowName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskScene.fxml"));
		Parent root = loader.load();		
		TaskController taskSceneController = loader.getController();
		Stage stage = new Stage();
		taskSceneController.setUp(smartBoard, windowName, smartBoardController, taskSceneController, stage);
		stage.setScene(new Scene(root));
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.centerOnScreen();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);	
		stage.setResizable(false);
		stage.show();
	}
	
	public void basicInput(String windowName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("BasicInputScene.fxml"));
		Parent root = loader.load();		
		BasicInputController basicInputController = loader.getController();
		Stage stage = new Stage();
		basicInputController.setUp(smartBoard, windowName, smartBoardController, stage);
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
		confirmationController.setUp(smartBoard, windowName, smartBoardController, stage);
		stage.setScene(new Scene(root));
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.centerOnScreen();
		stage.setTitle(windowName);
		stage.initModality(Modality.APPLICATION_MODAL);		
		stage.setResizable(false);
		stage.show();
	}
	
	public void editProfile() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditProfileScene.fxml"));
		Parent root = loader.load();		
		EditProfileController editProfileController = loader.getController();
		Stage stage = new Stage();
		editProfileController.setUp(smartBoard, smartBoardController, stage);
		stage.setScene(new Scene(root));
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.centerOnScreen();
		stage.setTitle("Edit Profile");
		stage.initModality(Modality.APPLICATION_MODAL);	
		stage.setResizable(false);
		stage.show();
	}
	
	public void signOut(ActionEvent event) throws IOException {
		smartBoard.submitSignOut();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignInScene.fxml"));
		root = loader.load();
        SignInController signInController = loader.getController();
		signInController.setUp(smartBoard);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.centerOnScreen();
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
}