package application;
import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class ProjectBoard implements Serializable {
	
	private String projectBoardName;
	private boolean defaultStatus = false;
	private LinkedList<Column> columns;
	
	public ProjectBoard(String projectBoardName, 
			boolean defaultStatus, LinkedList<Column> columns) {
		this.projectBoardName = projectBoardName;
		this.defaultStatus = defaultStatus;
		this.columns = columns;
	}
	
	public String getProjectBoardName() {
		return projectBoardName;
	}
	
	public void setProjectBoardName(String newProjectName) {
		this.projectBoardName = newProjectName;
	}
	
	public boolean getDefaultStatus() {
		return defaultStatus;
	}
	
	public void setDefaultStatus(boolean newDefaultStatus) {
		this.defaultStatus = newDefaultStatus;
	}
	
	public LinkedList<Column> getColumns() {
		return columns;
	}
}