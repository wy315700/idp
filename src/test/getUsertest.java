package test;

import java.util.List;

import org.cas.iie.idp.admin.userAdmin;
import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.authenticate.Impl.GetUserByLdap;
import org.cas.iie.idp.user.UserRole;

public class getUsertest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//IGetUser getuserhandle = new GetUserByLdap();
		//UserRole user = getuserhandle.getUserByName("wangyang", false);
		userAdmin useradmin = new userAdmin();
		//UserRole user = new UserRole();
		//user.setPassword("123456");
		//user.setRealname("temp");
		//user.setUsername("temp");
		//user.setUserDN("cn="+user.getUsername()+",ou=member");
		//useradmin.addUser(user);
		//List<UserRole> users = useradmin.getAllUsers(0,0);
		UserRole user = useradmin.getUserByName("wangyang");
		System.out.println(user.toString());
	}

}
