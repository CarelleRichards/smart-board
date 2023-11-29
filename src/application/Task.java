package application;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Locale;

@SuppressWarnings("serial")
public class Task implements Serializable {
	
	private String taskName;
	private String taskDescription;
	private LocalDate taskDueDate;
	private boolean completionStatus;
	private LinkedList<ActionItem> actionItems; 
	
	public Task(String taskName, String taskDescription, LocalDate taskDueDate, 
			boolean completionStatus, LinkedList<ActionItem> actionItems) {
		
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.taskDueDate = taskDueDate;
		this.completionStatus = completionStatus;
		this.actionItems = actionItems;
	}
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String newTaskName) {
		this.taskName = newTaskName;
	}
	
	public String getTaskDescription() {
		return taskDescription;
	}
	
	public void setTaskDescription(String newTaskDescription) {
		this.taskDescription = newTaskDescription;
	}
	
	public LocalDate getTaskDueDate() {
		return taskDueDate;
	}
	
	public void setTaskDueDate(LocalDate newTaskDueDate) {
		this.taskDueDate = newTaskDueDate;
	}
	
	public boolean getCompletionStatus() {
		return completionStatus;
	}
	
	public void setCompletionStatus(boolean newCompletionStatus) {
		this.completionStatus = newCompletionStatus;
	}
	
	public String getFormattedDueDate() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMM u", Locale.ENGLISH);
		return taskDueDate.format(dateFormatter);
	}
		
	public LinkedList<ActionItem> getActionItems() {
		return actionItems;
	}
	
	public void setActionItems(LinkedList<ActionItem> newActionItems) {
		this.actionItems = newActionItems;
	}
	
	public int actionItemsCompleted() {
		int completed = 0;
		for(ActionItem existingActionItem: actionItems) {
			if(existingActionItem.getActionItemCompletionStatus() == true) {
				completed++;
			}
		}
		return completed;
	}
}