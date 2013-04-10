package test;

import org.cas.iie.idp.authenticate.IGetUser;
import org.cas.iie.idp.authenticate.Impl.GetUserByLdap;
import org.cas.iie.idp.user.UserRole;

public class getUsertest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IGetUser getuserhandle = new GetUserByLdap();
		UserRole user = getuserhandle.getUserByName("wangyang", false);
		System.out.println(user.toString());
	}

}
