/*
 * The following tests include one positive and one negative scenario for 
 * each of the methods in the model. It doesn't include getters or setters. 
 * 
 * If any changes are made, they are reverted back at he end of the test to 
 * ensure all tests are based off the original startUp() data.
 */

package application;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.File;
import java.time.LocalDate;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

public class SmartBoardTest {
	
	private final static SmartBoard smartBoard = new SmartBoard();
	
	/*
	 * Creates a new User with Project Boards, Columns and Tasks. 
	 * These details are used in the following tests.
	 */
	
	@BeforeClass
	public static void startUp() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		smartBoard.submitNewUser("Carelle8", "welcome", "Carelle", "Mulawa-Richards", profilePhoto);
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.submitNewProjectBoard("Learn Java");
		smartBoard.submitNewProjectBoard("Learn HTML");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.submitNewColumn("To do");
		smartBoard.submitNewColumn("Doing");
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.submitNewTask("Learn Java syntax", "Aim to learn Java Syntax in a week.", null, false);
		smartBoard.submitNewTask("Write first progam", "Research and write a Hello World program.", null, false);
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(0));
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitSignIn()
	 * Scenario: Positive
	 * 
	 * Checks that the program can find an existing User, 
	 * set that User as the current User and return true.
	 */
	
	@Test
	public void submitSignInTest() {
		boolean result = smartBoard.submitSignIn("Carelle8", "welcome");
		assertEquals(true, result);
		assertEquals("Carelle8", smartBoard.getCurrentUser().getUsername());
		assertEquals("welcome", smartBoard.getCurrentUser().getPassword());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitSignIn()
	 * Scenario: Negative
	 * 
	 * When a user doesn't exist in the system tries to sign in, 
	 * the program should return false and not set a current user. 
	 */
	
	@Test
	public void submitSignInTest2() {
		boolean result = smartBoard.submitSignIn("PlantLover", "password1992");
		assertEquals(false, result);
		assertEquals(null, smartBoard.getCurrentUser());
	}	
		
	/*
	 * Tests: submitNewUser()
	 * Scenario: Positive
	 * 
	 * When the input is valid, the program should 
	 * create a new User object and return true. 
	 */
	
	@Test
	public void submitNewUserTest() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		boolean result = smartBoard.submitNewUser("Ryan1992", "password", "Ryan", "Morris", profilePhoto);
		assertEquals(true, result);
		assertEquals("Ryan1992", smartBoard.getUserList().get(1).getUsername());
		assertEquals("password", smartBoard.getUserList().get(1).getPassword());
		assertEquals("Ryan", smartBoard.getUserList().get(1).getFirstName());
		assertEquals("Morris", smartBoard.getUserList().get(1).getLastName());
		assertEquals("defaultProfilePhoto.jpg", smartBoard.getUserList().get(1).getProfilePhoto().getName());
		assertEquals(2, smartBoard.getUserList().size());
		smartBoard.getUserList().remove(1);
	}
	
	/*
	 * Tests: submitNewUser()
	 * Scenario: Negative
	 * 
	 * When the input is invalid, the program shouldn't create 
	 * a User object and should return false. In this test, you 
	 * cannot have two users with the same username. 
	 */
	
	@Test
	public void submitNewUserTest2() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		boolean result = smartBoard.submitNewUser("Carelle8", "welcome", "Carelle", "Mulawa-Richards", profilePhoto);
		assertEquals(false, result);
		assertEquals(1, smartBoard.getUserList().size());
	}

	/*
	 * Tests: submitEditProfile()
	 * Scenario: Positive
	 * 
	 * When the input is valid, the program should change 
	 * the User's profile information and return true.
	 */

	@Test
	public void submitEditProfileTest() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		smartBoard.submitSignIn("Carelle8", "welcome");
		boolean result = smartBoard.submitEditProfile("Carelle", "Richards", profilePhoto);
		assertEquals(true, result);
		assertEquals("Richards", smartBoard.getCurrentUser().getLastName());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitEditProfile()
	 * Scenario: Negative
	 * 
	 * When the input is invalid, the program should 
	 * return false and shouldn't make any profile changes. 
	 * In this test, a first name cannot contain numbers. 
	 */
	
	@Test
	public void submitEditProfileTest2() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		smartBoard.submitSignIn("Carelle8", "welcome");
		boolean result = smartBoard.submitEditProfile("123", "Richards", profilePhoto);
		assertEquals(false, result);
		assertEquals("Carelle", smartBoard.getCurrentUser().getFirstName());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitSignOut()
	 * Scenario: Positive
	 * 
	 * If a User is signed in, the program 
	 * should set the current user to null.
	 */
	
	@Test
	public void submitSignOutTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		assertEquals("Carelle8", smartBoard.getCurrentUser().getUsername());
		smartBoard.submitSignOut();
		assertEquals(null, smartBoard.getCurrentUser());
	}

	/*
	 * Tests: submitSignOut()
	 * Scenario: Negative
	 * 
	 * If there is no current User, the program 
	 * should keep the current User set to null.
	 */
	
	@Test
	public void submitSignOutTest2() {
		smartBoard.submitSignOut();
		assertEquals(null, smartBoard.getCurrentUser());
		smartBoard.submitSignOut();
		assertEquals(null, smartBoard.getCurrentUser());
	}
	
	/*
	 * Tests: generateRandomQuote()
	 * Scenario: Positive
	 * 
	 * A random String should be returned.
	 * There is no negative scenario tested for this method.
	 */
	
	@Test
	public void generateRandomQuoteTest() {
		String result = smartBoard.generateRandomQuote();
		assertFalse(result.isEmpty());
	}
	
	/*
	 * Tests: submitNewProjectBoard()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should create
	 * a new Project Board object and return true. 
	 */
	
	@Test
	public void submitNewProjectBoardTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		boolean result = smartBoard.submitNewProjectBoard("Learn CSS");
		assertEquals(true, result);
		assertEquals(3, smartBoard.getCurrentUser().getProjectBoards().size());
		smartBoard.getCurrentUser().getProjectBoards().remove(2);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitNewProjectBoard()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return false and 
	 * shouldn't create a new Project Board object. In this test, 
	 * you can't create two Project Board's with the same name.
	 */
	
	@Test
	public void submitNewProjectBoardTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		boolean result = smartBoard.submitNewProjectBoard("Learn Java");
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentUser().getProjectBoards().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitRenameProjectBoard()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should update
	 * the Project Board's name and return true.
	 */
	
	@Test
	public void submitRenameProjectBoard() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		boolean result = smartBoard.submitRenameProjectBoard("Learn JavaScript");
		assertEquals(true, result);
		assertEquals("Learn JavaScript", smartBoard.getCurrentUser().getProjectBoards().get(0).getProjectBoardName());
		smartBoard.submitRenameProjectBoard("Learn Java");
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitRenameProjectBoard()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return false
	 * and the Project Board's name should't be changed. In this 
	 * test, you can't have two Project Board's with the same name.
	 */
	
	@Test
	public void submitRenameProjectBoard2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		boolean result = smartBoard.submitRenameProjectBoard("Learn HTML");
		assertEquals(false, result);
		assertEquals("Learn Java", smartBoard.getCurrentUser().getProjectBoards().get(0).getProjectBoardName());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteProjectBoard()
	 * Scenario: Positive
	 * 
	 * If there is a current Project Board, it should 
	 * be removed from the system and return true.
	 */
	
	@Test
	public void deleteProjectBoardTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(1));
		boolean result = smartBoard.submitDeleteProjectBoard();
		assertEquals(true, result);
		assertEquals(1, smartBoard.getCurrentUser().getProjectBoards().size());
		assertFalse(smartBoard.getCurrentUser().getProjectBoards().get(0).getProjectBoardName().equals("Learn HTML"));
		assertEquals(null, smartBoard.getCurrentProjectBoard());
		smartBoard.submitNewProjectBoard("Learn HTML");
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteProjectBoard()
	 * Scenario: Negative
	 * 
	 * If there is no current Project Board, the program should 
	 * return false and no Project Boards should be removed.
	 */
	
	@Test
	public void deleteProjectBoardTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(null);
		boolean result = smartBoard.submitDeleteProjectBoard();
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentUser().getProjectBoards().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: setDefault()
	 * Scenario: Positive
	 * 
	 * When there is a current Project Board, the program should
	 * set it as default and return true. The default status 
	 * of all other Project Boards should be unset.
	 */
	
	@Test
	public void setDefaultTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		boolean result = smartBoard.setDefault();
		assertEquals(true, result);
		assertEquals(true, smartBoard.getCurrentProjectBoard().getDefaultStatus());
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(1).getDefaultStatus());
		smartBoard.unsetDefault();
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: setDefault()
	 * Scenario: Negative
	 * 
	 * If there is no current Project Board, the program should
	 * return false and no Project Boards should be changed. 
	 */
	
	@Test
	public void setDefaultTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(null);
		boolean result = smartBoard.setDefault();
		assertEquals(false, result);
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(0).getDefaultStatus());
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(1).getDefaultStatus());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: unsetDefault()
	 * Scenario: Positive
	 * 
	 * When there is a current Project Board, the program 
	 * should unset it as default and return true. 
	 */
	
	@Test
	public void unsetDefaultTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setDefault();
		assertEquals(true, smartBoard.getCurrentProjectBoard().getDefaultStatus());		
		boolean result = smartBoard.unsetDefault();
		assertEquals(true, result);
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(0).getDefaultStatus());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: unsetDefault()
	 * Scenario: Negative
	 * 
	 * When there is no current Project Board, the program should
	 * return false and no Project Boards should be changed.
	 */
	
	@Test
	public void unsetDefaultTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(null);	
		boolean result = smartBoard.unsetDefault();
		assertEquals(false, result);
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(0).getDefaultStatus());
		assertEquals(false, smartBoard.getCurrentUser().getProjectBoards().get(1).getDefaultStatus());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitNewColumn()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should create
	 * a new Column object and return true. 
	 */
	
	@Test
	public void submitNewColumnTest()  {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		boolean result = smartBoard.submitNewColumn("Complete");
		assertEquals(true, result);
		assertEquals(3, smartBoard.getCurrentProjectBoard().getColumns().size());
		smartBoard.getCurrentProjectBoard().getColumns().remove(2);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitNewColumn()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return false
	 * and shouldn't make a new Column object. In this test, 
	 * you can't create two Columns with the same name.
	 */
	
	@Test
	public void submitNewColumnTest2()  {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		boolean result = smartBoard.submitNewColumn("To do");
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentProjectBoard().getColumns().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitRenameColumn()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should update
	 * the Columns's name and return true.
	 */
	
	@Test
	public void submitRenameColumnTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		boolean result = smartBoard.submitRenameColumn("Complete");
		assertEquals(true, result);
		assertEquals("Complete", smartBoard.getCurrentColumn().getColumnName());
		smartBoard.submitRenameColumn("To do");
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitRenameColumn()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return
	 * false and shouldn't update the Columns's name. In this
	 * test, you can't have two Columns with the same name.
	 */
	
	@Test
	public void submitRenameColumnTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		boolean result = smartBoard.submitRenameColumn("To do");
		assertEquals(false, result);
		assertEquals("To do", smartBoard.getCurrentColumn().getColumnName());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteColumn()
	 * Scenario: Positive
	 * 
	 * If there is a current Column, the program 
	 * should removed it and return true.
	 */
	
	@Test
	public void submitDeleteColumnTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(1));
		boolean result = smartBoard.submitDeleteColumn();
		assertEquals(true, result);
		assertEquals(null, smartBoard.getCurrentColumn());
		assertEquals(1, smartBoard.getCurrentProjectBoard().getColumns().size());
		assertFalse(smartBoard.getCurrentUser().getProjectBoards().get(0).getProjectBoardName().equals("Doing"));
		smartBoard.submitNewColumn("Doing");
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteColumn()
	 * Scenario: Negative
	 * 
	 * If there is no current Column, the program should 
	 * return false and no Columns should be removed.
	 */
	
	@Test
	public void submitDeleteColumnTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(null);
		boolean result = smartBoard.submitDeleteColumn();
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentProjectBoard().getColumns().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitNewTask()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should create
	 * a new Task object and return true. 
	 */
	
	@Test
	public void submitNewTaskTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		boolean result = smartBoard.submitNewTask("Arrays and ArrayLists", 
				"Build lists of data with Java arrays and ArrayLists.", null, false);
		assertEquals(true, result);
		assertEquals(3, smartBoard.getCurrentColumn().getTasks().size());
		smartBoard.getCurrentColumn().getTasks().remove(2);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitNewTask()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return
	 * false and shouldn't make a new Task object. In this test, 
	 * you can't create two Tasks with the same name.
	 */
	
	@Test
	public void submitNewTaskTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		boolean result = smartBoard.submitNewTask("Learn Java syntax", "Aim to learn Java Syntax in a week.", null, false);
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentColumn().getTasks().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitEditTask()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should update
	 * the Task's name and description and return true.
	 */
	
	@Test
	public void submitEditTaskTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(0));
		boolean result = smartBoard.submitEditTask("Variables", "Learn about datatypes in Java and how we use them.", null, false);
		assertEquals(true, result);
		assertEquals("Variables", smartBoard.getCurrentTask().getTaskName());
		assertEquals("Learn about datatypes in Java and how we use them.", smartBoard.getCurrentTask().getTaskDescription());
		smartBoard.submitEditTask("Learn Java syntax", "Aim to learn Java Syntax in a week.", null, false);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitEditTask()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return 
	 * false and shouldn't update the Task's name and description.
	 * In this test, you can't have two Tasks with the same name.
	 */
	
	@Test
	public void submitEditTaskTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		boolean result = smartBoard.submitEditTask("Write first progam", "Aim to learn Java Syntax in a week.", null, false);
		assertEquals(false, result);
		assertEquals("Learn Java syntax", smartBoard.getCurrentTask().getTaskName());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteTask()
	 * Scenario: Positive
	 * 
	 * If there is a current Task, the program 
	 * should removed it and return true.
	 */
	
	@Test
	public void submitDeleteTaskTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		boolean result = smartBoard.submitDeleteTask();
		assertEquals(true, result);
		assertEquals(1, smartBoard.getCurrentColumn().getTasks().size());
		assertFalse(smartBoard.getCurrentColumn().getTasks().get(0).getTaskName().equals("Write first progam"));		
		smartBoard.submitNewTask("Write first progam", "Research and write a Hello World program.", null, false);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: submitDeleteTask()
	 * Scenario: Negative
	 * 
	 * If there is no current Task, the program should 
	 * return false and no Tasks should be removed.
	 */
	
	@Test
	public void submitDeleteTaskTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(null);
		boolean result = smartBoard.submitDeleteTask();
		assertEquals(false, result);
		assertEquals(2, smartBoard.getCurrentColumn().getTasks().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: checkProgressStatus()
	 * Scenario: Positive
	 * 
	 * If the Task is complete, it should return [COMPLETE].
	 * If the Task isn't complete and the due date is in 
	 * the future, it should return [ON TRACK]. 
	 * If the Task isn't complete and the due date is 
	 * in the past, it should return [OVERDUE].
	 */
	
	@Test
	public void checkProgressStatusTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		
		smartBoard.getCurrentTask().setTaskDueDate(LocalDate.of(2022, 10, 25));
		String result = smartBoard.checkProgressStatus(smartBoard.getCurrentTask().getTaskDueDate(), 
				smartBoard.getCurrentTask().getCompletionStatus());
		assertEquals("[ON TRACK]", result);
		
		smartBoard.getCurrentTask().setTaskDueDate(LocalDate.of(2020, 10, 25));
		result = smartBoard.checkProgressStatus(smartBoard.getCurrentTask().getTaskDueDate(), 
				smartBoard.getCurrentTask().getCompletionStatus());
		assertEquals("[OVERDUE]", result);
		
		smartBoard.getCurrentTask().setCompletionStatus(true);
		result = smartBoard.checkProgressStatus(smartBoard.getCurrentTask().getTaskDueDate(), 
				smartBoard.getCurrentTask().getCompletionStatus());
		assertEquals("[COMPLETE]", result);

		smartBoard.getCurrentTask().setCompletionStatus(false);
		smartBoard.getCurrentTask().setTaskDueDate(null);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: checkProgressStatus()
	 * Scenario: Negative
	 * 
	 * If the Task has no due date and isn't complete, 
	 * it should be set to "". If the Task has no due date
	 * and is complete, it should be set to [COMPLETE].
	 */
	
	@Test
	public void checkProgressStatusTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		
		smartBoard.getCurrentTask().setTaskDueDate(null);
		String result = smartBoard.checkProgressStatus(smartBoard.getCurrentTask().getTaskDueDate(), 
				smartBoard.getCurrentTask().getCompletionStatus());
		assertEquals("", result);
		
		smartBoard.getCurrentTask().setCompletionStatus(true);
		result = smartBoard.checkProgressStatus(smartBoard.getCurrentTask().getTaskDueDate(), 
				smartBoard.getCurrentTask().getCompletionStatus());
		assertEquals("[COMPLETE]", result);

		smartBoard.getCurrentTask().setCompletionStatus(false);
		smartBoard.submitSignOut();
	}

	/*
	 * Tests: makeTempActionItemList()
	 * Scenario: Positive
	 * 
	 * If the current Task has Action Items, it should
	 * copy the entire LinkedList to the tempActionItemList.
	 */
	
	@Test
	public void makeTempActionItemListTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		actionItems.add(new ActionItem("YouTube tutorials", false));
		actionItems.add(new ActionItem("Copy an example", false));
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		assertEquals(2, smartBoard.getTempActionItems().size());
		assertEquals("YouTube tutorials", smartBoard.getTempActionItems().get(0).getActionItemName());
		assertEquals("Copy an example", smartBoard.getTempActionItems().get(1).getActionItemName());
		smartBoard.getCurrentTask().setActionItems(null);
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: makeTempActionItemList()
	 * Scenario: Negative
	 * 
	 * If the current Task has no Action Items, the
	 * tempActionItemList should have no Action Items.
	 */	
	
	@Test
	public void makeTempActionItemListTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		smartBoard.getCurrentTask().setActionItems(null);
		smartBoard.makeTempActionItemList();
		assertEquals(0, smartBoard.getTempActionItems().size());
		smartBoard.submitSignOut();
	}
	
	/*
	 * Tests: addTempActionItem()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should create
	 * a new Action Item object and return true. 
	 */	
	
	@Test
	public void addTempActionItemTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		assertEquals(true, smartBoard.addTempActionItem("Follow a YouTube tutorial"));
		assertEquals("Follow a YouTube tutorial", smartBoard.getTempActionItems().get(0).getActionItemName());
	}
	
	/*
	 * Tests: addTempActionItem()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return
	 * false and shouldn't make a new ActionItem object. In this test, 
	 * you can't create two ActionItems with the same name.
	 */	
	
	@Test
	public void addTempActionItemTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.addTempActionItem("Follow a YouTube tutorial");
		assertEquals(false, smartBoard.addTempActionItem("Follow a YouTube tutorial"));
		assertEquals(1, smartBoard.getTempActionItems().size());
	}
	
	/*
	 * Tests: renameTempActionItem()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should update
	 * the Action Item object and return true. 
	 */	
	
	@Test
	public void renameTempActionItemTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.addTempActionItem("Follow a YouTube tutorial");
		smartBoard.setCurrentActionItem(smartBoard.getTempActionItems().get(0));
		assertEquals(true, smartBoard.renameTempActionItem("Come up with an idea for a program"));
		assertEquals("Come up with an idea for a program", smartBoard.getTempActionItems().get(0).getActionItemName());
		assertEquals(1, smartBoard.getTempActionItems().size());
	}
	
	/*
	 * Tests: renameTempActionItem()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return
	 * false and shouldn't make a new ActionItem object. In this test, 
	 * you can't create two ActionItems with the same name.
	 */	
	
	@Test
	public void renameTempActionItemTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.addTempActionItem("Follow a YouTube tutorial");
		smartBoard.addTempActionItem("Come up with an idea for a program");
		smartBoard.setCurrentActionItem(smartBoard.getTempActionItems().get(0));
		assertEquals(false, smartBoard.renameTempActionItem("Come up with an idea for a program"));
		assertEquals("Follow a YouTube tutorial", smartBoard.getTempActionItems().get(0).getActionItemName());
		assertEquals(2, smartBoard.getTempActionItems().size());
		smartBoard.getTempActionItems().clear();
	}
	
	/*
	 * Tests: deleteTempActionItem()
	 * Scenario: Positive
	 * 
	 * The program should removed the specified
	 * Action Item and return true.
	 */	
	
	@Test
	public void deleteTempActionItemTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.addTempActionItem("Follow a YouTube tutorial");
		smartBoard.setCurrentActionItem(smartBoard.getTempActionItems().get(0));
		assertEquals(true, smartBoard.deleteTempActionItem());
		assertEquals(0, smartBoard.getTempActionItems().size());
	}
	
	/*
	 * Tests: deleteTempActionItem()
	 * Scenario: Negative
	 * 
	 * If there is no specified Action Item, the program should 
	 * return false and no Action Items should be removed.
	 */	
	
	@Test
	public void deleteTempActionItemTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.setCurrentActionItem(null);
		assertEquals(false, smartBoard.deleteTempActionItem());
		assertEquals(0, smartBoard.getTempActionItems().size());
	}
	
	/*
	 * Tests: actionItemPercentage()
	 * Scenario: Positive
	 * 
	 * The percentage of complete Action Items
	 * should be calculated and returned. 
	 */	
	
	@Test
	public void actionItemPercentageTest() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		smartBoard.addTempActionItem("Follow a YouTube tutorial");
		smartBoard.addTempActionItem("Come up with an idea for a program");
		smartBoard.getTempActionItems().get(0).setActionItemCompletionStatus(true);
		smartBoard.getTempActionItems().get(1).setActionItemCompletionStatus(false);
		assertEquals(50, smartBoard.actionItemPercentage(), 0);
	}
	
	/*
	 * Tests: actionItemPercentage()
	 * Scenario: Positive
	 * 
	 * The percentage of complete Action Items should be calculated 
	 * and returned. If there are no Action Items, 0 should be returned. 
	 */	
	
	@Test
	public void actionItemPercentageTest2() {
		smartBoard.submitSignIn("Carelle8", "welcome");
		smartBoard.setCurrentProjectBoard(smartBoard.getCurrentUser().getProjectBoards().get(0));
		smartBoard.setCurrentColumn(smartBoard.getCurrentProjectBoard().getColumns().get(0));
		smartBoard.setCurrentTask(smartBoard.getCurrentColumn().getTasks().get(1));
		LinkedList<ActionItem> actionItems = new LinkedList<>();
		smartBoard.getCurrentTask().setActionItems(actionItems);
		smartBoard.makeTempActionItemList();
		assertEquals(0, smartBoard.actionItemPercentage(), 0);
	}
	
	/*
	 * Tests: nameValidation()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should return 
	 * true. Names can contain the characters a-z, A-Z, 
	 * apostrophe ('), period (.) and hyphen (-).
	 */
	
	@Test
	public void nameValidationTest() {
		assertEquals(true, smartBoard.nameValidation("Daniel"));
		assertEquals(true, smartBoard.nameValidation("Ellie-May"));
		assertEquals(true, smartBoard.nameValidation("O'Neil"));
		assertEquals(true, smartBoard.nameValidation("St. John"));
	}
	
	/*
	 * Tests: nameValidation()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return false. 
	 * Names can only contain the specified characters, they must
	 * start and end with a letter and they cannot be blank.
	 */
	
	@Test
	public void nameValidationTest2() {
		assertEquals(false, smartBoard.nameValidation("Dan1992"));
		assertEquals(false, smartBoard.nameValidation(" Ryan"));
		assertEquals(false, smartBoard.nameValidation("Ryan "));
		assertEquals(false, smartBoard.nameValidation("Dan-"));
		assertEquals(false, smartBoard.nameValidation(" "));
		assertEquals(false, smartBoard.nameValidation(" "));
	}
	
	/*
	 * Tests: textValidation()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should 
	 * return true. The input cannot be blank. 
	 */
	
	@Test
	public void textValidationTest() {
		assertEquals(true, smartBoard.textValidation("Complete"));
		assertEquals(true, smartBoard.textValidation("30 Day Activities"));
		assertEquals(true, smartBoard.textValidation("Due 18/12/2021"));
		assertEquals(true, smartBoard.textValidation("Daniel's Jobs"));
	}
	
	/*
	 * Tests: textValidation()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should 
	 * return false. The input cannot be blank. 
	 */
	
	@Test
	public void textValidationTest2() {
		assertEquals(false, smartBoard.textValidation(""));
		assertEquals(false, smartBoard.textValidation(" "));
		assertEquals(false, smartBoard.textValidation("  "));
	}
	
	/*
	 * Tests: usernameValidation()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should 
	 * return true. Usernames can contain the characters
	 * a-z, A-Z, 0-9, period (.) and underscore (_).
	 */
	
	@Test 
	public void usernameValidationTest() {
		assertEquals(true, smartBoard.usernameValidation("Buttermilk_Pantry"));
		assertEquals(true, smartBoard.usernameValidation("WinstonMonkey666"));
		assertEquals(true, smartBoard.usernameValidation("Lil.Robot.Bastion"));
	}
	
	/*
	 * Tests: usernameValidation()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should return false. 
	 * Usernames can only contain the specified characters and 
	 * they cannot be blank. They must also be > 5 characters long.
	 */
	
	@Test 
	public void usernameValidationTest2() {
		assertEquals(false, smartBoard.usernameValidation("Bacon&Eggs"));
		assertEquals(false, smartBoard.usernameValidation("Elle"));
		assertEquals(false, smartBoard.usernameValidation(" "));
		assertEquals(false, smartBoard.usernameValidation("      "));
	}

	/*
	 * Tests: passwordValidation()
	 * Scenario: Positive
	 * 
	 * If the input is valid, the program should 
	 * return true. Passwords can contain any character
	 * except a space and must be > 5 characters long.
	 */
	
	@Test
	public void passwordValidation() {
		assertEquals(true, smartBoard.passwordValidation("password"));
		assertEquals(true, smartBoard.passwordValidation("Gr33nPl4nT$"));
		assertEquals(true, smartBoard.passwordValidation("W0rld_0f_W@RcR@fT!"));
	}
	
	/*
	 * Tests: passwordValidation()
	 * Scenario: Negative
	 * 
	 * If the input is invalid, the program should 
	 * return false. Passwords cannot contain spaces 
	 * and must be > 5 characters long.
	 */
	
	@Test
	public void passwordValidation2() {
		assertEquals(false, smartBoard.passwordValidation("llamas with hats"));
		assertEquals(false, smartBoard.passwordValidation("$nake"));
		assertEquals(false, smartBoard.passwordValidation("      "));
		assertEquals(false, smartBoard.passwordValidation(" "));
	}
	
	/*
	 * Tests: fileValidation()
	 * Scenario: Positive
	 * 
	 * if the input is valid, the program should return true. 
	 * The file should be an image format, such as jpg and png.
	 */
	
	@Test
	public void fileValidationTest() {
		File profilePhoto = new File("defaultProfilePhoto.jpg");
		assertEquals(true, smartBoard.fileValidation(profilePhoto));
		profilePhoto = new File("SmartBoardLogo.png");
		assertEquals(true, smartBoard.fileValidation(profilePhoto));
	}
	
	/*
	 * Tests: fileValidation()
	 * Scenario: Negative
	 * 
	 * if the input is invalid, the program should return false. 
	 * The file should exist and be an image format.
	 */
	
	@Test
	public void fileValidationTest2() {
		File profilePhoto = null;
		assertEquals(false, smartBoard.fileValidation(profilePhoto));
		profilePhoto = new File("ProfilePhotoThatDoesntExist.jpg");
		assertEquals(false, smartBoard.fileValidation(profilePhoto));
		profilePhoto = new File("application/BasicInputController.class");
		assertEquals(false, smartBoard.fileValidation(profilePhoto));
	}
}
