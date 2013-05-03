package test;

import java.util.Date;
import java.util.List;

import org.cas.iie.idp.admin.userAdmin;
import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.authenticate.Impl.GetUserByLdap;
import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.UserRole;

public class getUsertest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//IGetUser getuserhandle = new GetUserByLdap();
		//UserRole user = getuserhandle.getUserByName("wangyang", false);
		//UserRole user = new UserRole();
		//user.setPassword("123456");
		//user.setRealname("temp");
		//user.setUsername("temp");
		//user.setUserDN("cn="+user.getUsername()+",ou=member");
		//useradmin.addUser(user);
		//List<UserRole> users = useradmin.getAllUsers(0,0);
		LDAPhelper.domain = "o=iie";
		Configs.setthisconfig("iie");
		userAdmin useradmin = new userAdmin();
		long start = System.currentTimeMillis();
		UserRole user = useradmin.getUserByName("12");
		user.setUsername("121");
		useradmin.addUser(user);
		
		useradmin.modifyUseGroup(user);
		long end = System.currentTimeMillis();
		System.out.println(end-start);
		System.out.println(user.toString());
	}

}
