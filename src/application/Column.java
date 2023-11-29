
package application;
import java.io.Serializable;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class Column implements Serializable  {
	
	private String columnName;
	LinkedList<Task> tasks;
	
	public Column(String columnName, LinkedList<Task> tasks) {
		this.columnName = columnName;
		this.tasks = tasks;
	}
	
	public String getColumnName() {
		return columnName;
	}
	
	public void setColumnName(String newColumnName) {
		this.columnName = newColumnName;
	}
	
	public LinkedList<Task> getTasks() {
		return tasks;
	}
}
