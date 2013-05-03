package org.cas.iie.idp.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserRole {
	public static String USERGROUP_KEY = "usergroup";
	private String username;
	private String realname;
	private Map<String , Set<String>> usergroup;
	private String password;
	private int userID;
	private String userDN;
	public UserRole() {
		usergroup = new HashMap<>();
		TenantConfigRole config = Configs.getthistenantconfig();
		for(Map.Entry<String, String> entry : config.getAttributeset().entrySet()){
			Set<String> set = new HashSet<String>();
			usergroup.put(entry.getKey(), set);
		}
	}
	public UserRole(String username, String password, int userID) {
		this.username = username;
		this.password = password;
		this.userID = userID;
		usergroup = new HashMap<>();
	}
	public boolean addUsergroups(String attrtype,Set<String> groups){
		if(attrtype != null && groups != null){
			usergroup.put(attrtype, groups);
			return true;
		}
		return false;
	}
	public boolean addUsergroup(String attrtype,String grpname){
		if(attrtype != null && grpname != null && usergroup.containsKey(attrtype)){
			return usergroup.get(attrtype).add(grpname);
		}
		else
			return false;
	}
	public Map<String , Set<String>> getUsergroup() {
		return usergroup;
	}
	public void setUsergroup( Map<String , Set<String>> usergroup) {
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

	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
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
