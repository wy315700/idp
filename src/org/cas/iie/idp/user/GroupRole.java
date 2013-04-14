package org.cas.iie.idp.user;

import java.util.ArrayList;
import java.util.List;

public class GroupRole {
	String groupname;
	String groupDN;
	List<String> users;
	public GroupRole(){
		users = new ArrayList<String>();
	}
	public String getGroupname() {
		return groupname;
	}
	public void addUser(String username){
		users.add(username);
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	
	public String getGroupDN() {
		return groupDN;
	}
	public void setGroupDN(String groupDN) {
		this.groupDN = groupDN;
	}
	@Override
	public String toString() {
		return "GroupRole [groupname=" + groupname + ", users=" + users + "]";
	}
	
}
