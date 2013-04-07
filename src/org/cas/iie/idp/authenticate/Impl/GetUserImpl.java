package org.cas.iie.idp.authenticate.Impl;

import java.util.HashMap;
import java.util.Map;

import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.user.UserRole;

public class GetUserImpl implements IGetUser {
	static Map<String, UserRole> users;
	static{
		users = new HashMap<String, UserRole>();
		UserRole user1 = new UserRole("aaa", "bbb", 1);
		users.put(user1.getUsername(), user1);
		UserRole user2 = new UserRole("bbb", "bbb", 2);
		users.put(user2.getUsername(), user1);
		UserRole user3 = new UserRole("ccc", "bbb", 3);
		users.put(user3.getUsername(), user1);
	}
	@Override
	public UserRole getUserByName(String username, boolean flag) {
		// TODO Auto-generated method stub
		UserRole ans = new UserRole();
		if(users.containsKey(username)){
			ans = users.get(username);
		}
		if(flag == false){
			ans.setPassword(null);
		}
		return ans;
	}

	@Override
	public UserRole getUserByID(int userID, boolean flag) {
		// TODO Auto-generated method stub
		return null;
	}

}
