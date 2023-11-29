
package application;
import java.io.Serializable;

@SuppressWarnings("serial")
public class ActionItem implements Serializable  {
	
	private String actionItemName;
	private boolean actionItemCompletionStatus;
	
	public ActionItem(String actionItemName, boolean actionItemCompletionStatus) {
		this.actionItemName = actionItemName;
		this.actionItemCompletionStatus = actionItemCompletionStatus;
	}
	
	public String getActionItemName() {
		return actionItemName;
	}
	
	public void setActionItemName(String newActionItemName) {
		this.actionItemName = newActionItemName;
	}
	
	public boolean getActionItemCompletionStatus() {
		return actionItemCompletionStatus;
	}
	
	public void setActionItemCompletionStatus(boolean newActionItemCompletionStatus) {
		this.actionItemCompletionStatus = newActionItemCompletionStatus;
	}
}