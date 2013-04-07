package org.cas.iie.idp.user;

public class UserRole {
	private String username;
	private String password;
	private int userID;
	
	public UserRole() {
	}
	public UserRole(String username, String password, int userID) {
		this.username = username;
		this.password = password;
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
}
