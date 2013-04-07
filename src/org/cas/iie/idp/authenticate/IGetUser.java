package org.cas.iie.idp.authenticate;

import org.cas.iie.idp.user.UserRole;

public interface IGetUser {
	public UserRole getUserByName(String username,boolean flag);//flag == true 时返回密码 否则不返回
	public UserRole getUserByID(int userID,boolean flag);
}
