package application;
import java.io.*;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private File profilePhoto;
	private LinkedList<ProjectBoard> projectBoards; 
	
	public User(String username, String password, String firstName, String lastName, 
			File profilePhoto, LinkedList<ProjectBoard> projectBoards) {
		
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.profilePhoto = profilePhoto;
		this.projectBoards = projectBoards;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String newFirstName) {
		this.firstName = newFirstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String newLastName) {
		this.lastName = newLastName;
	}
	
	public File getProfilePhoto() {
		return profilePhoto;
	}
	
	public void setProfilePhoto(File newProfilePhoto) {
		this.profilePhoto = newProfilePhoto;
	}
	
	public LinkedList<ProjectBoard> getProjectBoards() {
		return projectBoards;
	}
}