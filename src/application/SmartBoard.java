package application;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;

public class SmartBoard {

	private ArrayList<User> users = new ArrayList<>();
	private User currentUser;
	private ProjectBoard currentProjectBoard;
	private Column currentColumn;
	private Task currentTask;
	private ActionItem currentActionItem;
	private LinkedList<ActionItem> tempActionItems = new LinkedList<>(); 
	private String alertMessage;
	private String randomQuote;
	private Task copiedTask;
	private Column copiedColumn;
	private Column hoverColumn;
	private int hoverLocation;
	private File defaultProfilePhoto = new File("defaultProfilePhoto.jpg");
	private File logoImage = new File("SmartBoardLogo.png");
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *         User creation, editing & sign in/out          *
	 *       submitSignOut() sets current user to null.      *
	 *    Rest return true if successful, otherwise false.   *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public boolean submitSignIn(String username, String password) {	
		for(User existingUser : users) {
			if(existingUser.getUsername().equals(username) && 
					existingUser.getPassword().equals(password)) {
				currentUser = existingUser;
				randomQuote = generateRandomQuote();
				return true;
			} else if(existingUser.getUsername().equals(username) && 
					!existingUser.getPassword().equals(password)) {
				alertMessage = "Password is incorrect.";
				return false;
			}
		}
		alertMessage = "There is no existing user with these details.";
		return false;
	}
	
	public boolean submitNewUser(String username, String password, 
			String firstName, String lastName, File profilePhoto) {
		
		File checkedProfilePhoto = profilePhoto;
		
		for(User existingUser : users) {	
			if(existingUser.getUsername().equals(username)) {
				alertMessage = "That username is taken already.";
				return false;
			} 
		}
		if(!usernameValidation(username)) {
			alertMessage = "You must enter a valid username.";
			return false;
		} else if(!passwordValidation(password)) {
			alertMessage = "You must enter a valid password.";
			return false;
		} else if(!nameValidation(firstName)) {
			alertMessage = "You must enter a valid first name.";
			return false;
		} else if(!nameValidation(lastName)) {
			alertMessage = "You must enter a valid last name.";
			return false;
		} else {
			if(!fileValidation(profilePhoto)) {
				checkedProfilePhoto = defaultProfilePhoto;
			}
			LinkedList<ProjectBoard> projectBoards = new LinkedList<>();
			users.add(new User(username, password, firstName, lastName, checkedProfilePhoto, projectBoards));
			alertMessage = "User creation successful! Select close and sign in.";
			return true;
		}
	}
	
	public boolean submitEditProfile(String firstName, String lastName, File profilePhoto) {
		File checkedProfilePhoto = profilePhoto;
		
		if(!nameValidation(firstName)) {
			alertMessage = "You must enter a valid first name.";
			return false;
		} else if(!nameValidation(lastName)) { 
			alertMessage = "You must enter a valid last name.";
			return false;
		} else {
			if(!fileValidation(profilePhoto)) {
				checkedProfilePhoto = defaultProfilePhoto;
			}
			currentUser.setFirstName(firstName);
			currentUser.setLastName(lastName);
			currentUser.setProfilePhoto(checkedProfilePhoto);
			alertMessage = "Edit profile successful!";
			return true;
		}
	}
	
	public void submitSignOut() {
		currentUser = null;
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                    Random quote                       *
	 *       Returns a randomised quote from an array.       *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	public String generateRandomQuote() {
		String[] quotes = {"\"Do not fear failure but rather fear not trying.\" - Roy T. Bennett",
				"\"The way to get started is to quit talking and begin doing.\" - Walt Disney",
		"\"Fear is a powerful stimulant.\" - Margaret Atwood"};

		int randomIndex = new Random().nextInt(quotes.length);
		String randomQuote = quotes[randomIndex];
		return randomQuote;
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                    Project Boards                     *
	 *      Returns true if successful, otherwise false.     *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	public boolean submitNewProjectBoard(String projectBoardName) {
		for(ProjectBoard existingBoard : currentUser.getProjectBoards()) {
			if(existingBoard.getProjectBoardName().equals(projectBoardName)) {
				alertMessage = "There is already a Project Board with this name.";
				return false;
			} 
		}
		if(!textValidation(projectBoardName)) {
			alertMessage = "You must enter a valid Project Board name.";
			return false;
		} else {
			LinkedList<Column> columns = new LinkedList<>();
			currentUser.getProjectBoards().add(new ProjectBoard(projectBoardName, false, columns));
			alertMessage = "Project Board successfully created!";
			return true;
		}	
	}

	public boolean submitRenameProjectBoard(String projectBoardName) {
		for(ProjectBoard existingBoard : currentUser.getProjectBoards())  {
			if(existingBoard.getProjectBoardName().equals(projectBoardName)) {
				alertMessage = "There is already a Project Board with this name.";
				return false;
			}
		}
		if(!textValidation(projectBoardName)) {
			alertMessage = "You must enter a valid Project Board name.";
			return false;
		} else {
			currentProjectBoard.setProjectBoardName(projectBoardName);
			alertMessage = "Project Board successfully renamed!";
			return true;
		}
	}
	
	public boolean submitDeleteProjectBoard() {
		ProjectBoard projectBoardToDelete = null;
		boolean projectBoardFound = false;
		
		for(ProjectBoard existingProjectBoard : currentUser.getProjectBoards()) {
			if(existingProjectBoard.equals(currentProjectBoard)) {	
				projectBoardFound = true;
				projectBoardToDelete = existingProjectBoard;
				break;
			}					
		}
		if(projectBoardFound) {
			currentUser.getProjectBoards().remove(projectBoardToDelete);
			alertMessage = "Project Board successfully deleted!";
			projectBoardToDelete = null;
			currentProjectBoard = null;
			return true;
		} else {
			alertMessage = "Error, couldn't find this Project Board in the system.";
			return false;
		}
	}
	
	public boolean setDefault() {
		if(currentProjectBoard == null) {
			alertMessage = "Error, couldn't find that Project Board.";
			return false;
		} else {
			for(ProjectBoard existingBoard : currentUser.getProjectBoards()) {
				existingBoard.setDefaultStatus(false);
			}
			alertMessage = "Project Board successfully set as default.";
			currentProjectBoard.setDefaultStatus(true);
			return true;
		}	
	}
	
	public boolean unsetDefault() {
		if(currentProjectBoard == null) {
			alertMessage = "Error, couldn't find that Project Board.";
			return false;
		} else {
			alertMessage = "Project Board successfully unset as default.";
			currentProjectBoard.setDefaultStatus(false);
			return true;
		}
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                       Columns                         *
	 *      Returns true if successful, otherwise false.     *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public boolean submitNewColumn(String columnName) {
		for(Column existingColumn : currentProjectBoard.getColumns()) {
			if(existingColumn.getColumnName().equals(columnName)) {
				alertMessage = "There is already a Column with this name.";
				return false;
			}
		}
		if(!textValidation(columnName)) {
			alertMessage = "You must enter a valid Column name.";
			return false;
		} else {
			LinkedList<Task> tasks = new LinkedList<>();
			currentProjectBoard.getColumns().add(new Column(columnName, tasks));
			alertMessage = "Column successfully created!";
			return true;
		}	
	}

	public boolean submitRenameColumn(String columnName) {
		for(Column existingColumn : currentProjectBoard.getColumns()) {
			if(existingColumn.getColumnName().equals(columnName)) {
				alertMessage = "There is already a Column with this name.";
				return false;
			}
		}
		if(!textValidation(columnName)) {
			alertMessage = "You must enter a valid Column name.";
			return false;
		} else {
			currentColumn.setColumnName(columnName);
			alertMessage = "Column successfully renamed!";
			return true;
		}
	}
	
	public boolean submitDeleteColumn() {
		Column columnToDelete = null;
		boolean columnFound = false;
		
		for(Column existingColumn : currentProjectBoard.getColumns()) {
			if(existingColumn.equals(currentColumn)) {
				columnFound = true;
				columnToDelete = existingColumn;
				break;
			}
		}
		if(columnFound) {
			currentProjectBoard.getColumns().remove(columnToDelete);
			alertMessage = "Column successfully deleted!";
			columnToDelete = null;
			currentColumn = null;
			return true;
		} else {
			alertMessage = "Error, couldn't find this Column in the system.";
			return false;
		}
	}

	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                        Tasks                          *
	 *       checkProgressStatus() returns a String.         *
	 *    Rest return true if successful, otherwise false.   *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public boolean submitNewTask(String taskName, String taskDescription,
			LocalDate taskDueDate, boolean completionStatus) {
		
		for(Column existingColumn : currentProjectBoard.getColumns()) {
			for(Task existingTask : existingColumn.getTasks()) {
				if(existingTask.getTaskName().equals(taskName)) {
					alertMessage = "There is already a Task with this name.";
					return false;
				}
			}
		}
		if(!textValidation(taskName)) {
			alertMessage = "You must enter a valid Task name.";
			return false;	
		} else {
			LinkedList<ActionItem> actionItems = new LinkedList<>();
			for(ActionItem existingItem: tempActionItems) {
				actionItems.add(new ActionItem(existingItem.getActionItemName(), 
						existingItem.getActionItemCompletionStatus()));
			}
			currentColumn.getTasks().add(new Task(taskName, taskDescription, 
					taskDueDate, completionStatus, actionItems));
			alertMessage = "Task successfully created!";
			return true;
		}
	}

	public boolean submitEditTask(String taskName, String taskDescription, 
			LocalDate taskDueDate, boolean completionStatus) {
		
		if(!currentTask.getTaskName().equals(taskName)) {
			for(Column existingColumn : currentProjectBoard.getColumns()) {
				for(Task existingTask : existingColumn.getTasks()) {
					if(existingTask.getTaskName().equals(taskName)) {
						alertMessage = "There is already a Task with this name.";
						return false;
					}
				}
			}
		}
		if(!textValidation(taskName)) {
			alertMessage = "You must enter a valid Task name.";
			return false;
		} else {
			currentTask.setTaskName(taskName);
			currentTask.setTaskDescription(taskDescription);
			currentTask.setTaskDueDate(taskDueDate);
			currentTask.setCompletionStatus(completionStatus);
			LinkedList<ActionItem> actionItems = new LinkedList<>();
			for(ActionItem existingItem: tempActionItems) {
				actionItems.add(new ActionItem(existingItem.getActionItemName(), 
						existingItem.getActionItemCompletionStatus()));
			}
			currentTask.setActionItems(actionItems);
			alertMessage = "Task successfully updated!";
			return true;
		}
	}
	
	public boolean submitDeleteTask() {
		Task taskToDelete = null;
		boolean taskFound = false;
		
		for(Task existingTask : currentColumn.getTasks()) {	
			if(existingTask.equals(currentTask)) {
				taskFound = true;
				taskToDelete = existingTask;
				break;
			}
		}
		if(taskFound) {
			currentColumn.getTasks().remove(taskToDelete);
			alertMessage = "Task successfully deleted!";
			taskToDelete = null;
			currentTask = null;
			return true;
		} else {
			alertMessage = "Error, couldn't find this Task in the system.";
			return false;
		}
	}
	
	public String checkProgressStatus(LocalDate taskDueDate, boolean completionStatus) {
		String progressStatus;
		if(completionStatus == true) {
			progressStatus = "[COMPLETE]";
		} else if(taskDueDate == null) {
			progressStatus = ""; 
		} else if(LocalDate.now().isAfter(taskDueDate)) {
			progressStatus = "[OVERDUE]";
		} else {
			progressStatus = "[ON TRACK]";
		}
		return progressStatus;
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                    Action Items                       *
	 *     makeTempActionItemList() copies Action Items.     *
	 *      actionItemPercentage() returns % completed.      *
	 *    Rest return true if successful, otherwise false.   *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */

	public void makeTempActionItemList() {
		tempActionItems.clear();
		if(currentTask.getActionItems() != null) {
			for(ActionItem existingActionItem : currentTask.getActionItems()) {
				tempActionItems.add(new ActionItem(existingActionItem.getActionItemName(), 
						existingActionItem.getActionItemCompletionStatus()));
			} 
		}	
	}
	
	public boolean addTempActionItem(String actionItem) {
		for(ActionItem existingActionItem : tempActionItems) {
			if(existingActionItem.getActionItemName().equals(actionItem)) {
				alertMessage = "There is already an Action Item with this name.";
				return false;
			}
		}
		if(!textValidation(actionItem)) {
			alertMessage = "You must enter a valid Action Item name.";
			return false;
		} else {
			tempActionItems.add(new ActionItem(actionItem, false));
			alertMessage = "Action Item added!";
			return true;
		}	
	}
	
	public boolean renameTempActionItem(String actionItem) {		
		if(!currentActionItem.getActionItemName().equals(actionItem)) {
			for(ActionItem existingActionItem : tempActionItems) {
				if(existingActionItem.getActionItemName().equals(actionItem)) {
					alertMessage = "There is already an Action Item with this name.";
					return false;
				}
			}
		}
		if(!textValidation(actionItem)) {
			alertMessage = "You must enter a valid Action Item name.";
			return false;
		} else {
			currentActionItem.setActionItemName(actionItem);			
			alertMessage = "Action Item updated!";
			return true;
		}
	}
	
	public boolean deleteTempActionItem() {
		ActionItem actionItemToDelete = null;
		boolean actionItemFound = false;
		
		for(ActionItem existingActionItem : tempActionItems) {	
			if(existingActionItem.equals(currentActionItem)) {
				actionItemFound = true;
				actionItemToDelete = existingActionItem;
				break;
			}
		}
		if(actionItemFound) {
			tempActionItems.remove(actionItemToDelete);
			alertMessage = "Action Item successfully deleted!";
			actionItemToDelete = null;
			currentActionItem = null;
			return true;
		} else {
			alertMessage = "Error, couldn't find this Action Item in the system.";
			return false;
		}
	}

	public double actionItemPercentage() {
		double value = 0;
		if(tempActionItems.size() > 0) {
			for(ActionItem existingActionItem : tempActionItems) {
				if(existingActionItem.getActionItemCompletionStatus() == true) {
					value++;
				}
			}
			double percentage = (value / tempActionItems.size()) * 100;
			return percentage;
		} else {
			return 0;
		}
	}

	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                    Input validation                   *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public boolean nameValidation(String name) {
		String nameRegex = "^[a-zA-Z]+((['. -][a-zA-Z ])?[a-zA-Z]*)*$";
		return name.matches(nameRegex);
	}

	public boolean textValidation(String text) {
		String textRegex = "(.|\\s)*\\S(.|\\s)*";
		return text.matches(textRegex);
	}

	public boolean usernameValidation(String username) {
		String textRegex = "^[a-zA-Z0-9._]*$";
		return username.matches(textRegex) && username.length() > 5;
	}

	public boolean passwordValidation(String password) {
		String textRegex = "^\\S*$";
		return password.matches(textRegex) && password.length() > 5;
	}
	
	public boolean fileValidation(File profilePhoto) {
		try {
			return (ImageIO.read(profilePhoto) != null);
		}	catch (Exception e) {
			return false;
		} 
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                  Load & save data                     *
	 *      Returns true if successful, otherwise false.     *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public boolean saveData() {
        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;
        
        try {
        	fileOut = new FileOutputStream("SmartBoardUsers.ser");
        	objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(users);
            objectOut.flush();
            objectOut.close();
            return true;
        } catch(FileNotFoundException exception) {
        	alertMessage = "File could not be found 'SmartBoardUsers.ser'.";
        } catch(IOException exception) {
        	alertMessage = "Input/output exception occurred when saving 'SmartBoardUsers.ser'.";
        } catch(Exception exception) {
        	alertMessage = "Exception occurred when saving 'SmartBoardUsers.ser'.";
        }
        return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean loadData() {
        FileInputStream fileIn = null;
        ObjectInputStream objectIn = null;

        try {
        	fileIn = new FileInputStream("SmartBoardUsers.ser");
        	objectIn = new ObjectInputStream(fileIn);
            users = (ArrayList<User>) objectIn.readObject();
            objectIn.close();
            return true;
        } catch(FileNotFoundException exception) {
        	alertMessage = "File could not be found 'SmartBoardUsers.ser'.";
        } catch(IOException exception) {
        	alertMessage = "Input/output exception occurred when loading 'SmartBoardUsers.ser'.";
        } catch(ClassNotFoundException exception) {
        	alertMessage = "Data in 'SmartBoardUsers.ser' is the wrong format";
        } catch(Exception exception) {
        	alertMessage = "Exception occurred when loading 'SmartBoardUsers.ser'.";
        }
        return false;
	}
	
	/*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                   Getters & setters                   *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 */
	
	public ArrayList<User> getUserList() {
		return users;
	}
	
	public LinkedList<ActionItem> getTempActionItems() {
		return tempActionItems;
	}
	
	public void setCurrentActionItem(ActionItem newCurrentActionItem) {
		this.currentActionItem = newCurrentActionItem;
	}
	
	public ActionItem getCurrentActionItem() {
		return currentActionItem;
	}

	public String getRandomQuote() {
		return randomQuote;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentColumn(Column newCurrentColumn) {
		this.currentColumn = newCurrentColumn;
	}
	
	public Column getCurrentColumn() {
		return currentColumn;
	}
	
	public void setCurrentTask(Task newCurrentTask) {
		this.currentTask = newCurrentTask;
	}
	
	public Task getCurrentTask() {
		return currentTask;
	}
	
	public void setCurrentProjectBoard(ProjectBoard newCurrentProjectBoard) {
		this.currentProjectBoard = newCurrentProjectBoard;
	}
	
	public ProjectBoard getCurrentProjectBoard() {
		return currentProjectBoard;
	}

	public String getAlertMessage() {
		return alertMessage;
	}
	
	public void setCopiedTask(Task existingTask) {
		this.copiedTask = existingTask;
	}
	
	public Task getCopiedTask() {
		return copiedTask;
	}
	
	public void setCopiedColumn(Column existingColumn) {
		this.copiedColumn = existingColumn;
	}
	
	public Column getCopiedColumn() {
		return copiedColumn;
	}
	
	public void setHoverColumn(Column existingColumn) {
		this.hoverColumn = existingColumn;
	}
	
	public Column getHoverColumn() {
		return hoverColumn;
	}
	
	public void setHoverLocation(int index) {
		this.hoverLocation = index;
	}
	
	public int getHoverLocation() {
		return hoverLocation;
	}
	
	public File getdefaultProfilePhoto() {
		return defaultProfilePhoto;
	}
	
	public File getLogoImage() {
		return logoImage;
	}
}