package org.cas.iie.idp.user;

import java.util.ArrayList;
import java.util.List;

public class UserRole {

	private String username;
	
	private List<String> usergroup;
	
	private String password;
	private int userID;
	private String userDN;
	public UserRole() {
		usergroup = new ArrayList<String>();
	}
	public UserRole(String username, String password, int userID) {
		this.username = username;
		this.password = password;
		this.userID = userID;
	}
	public boolean addUsergroup(String grpname){
		if(grpname != null)
			return usergroup.add(grpname);
		else
			return false;
	}
	public List<String> getUsergroup() {
		return usergroup;
	}
	public void setUsergroup(List<String> usergroup) {
		this.usergroup = usergroup;
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

	public String getUserDN() {
		return userDN;
	}
	public void setUserDN(String userDN) {
		this.userDN = userDN;
	}
	@Override
	public String toString() {
		return "UserRole [username=" + username + ", usergroup=" + usergroup
				+ ", password=" + password + ", userID=" + userID + "]";
	}
	
}
