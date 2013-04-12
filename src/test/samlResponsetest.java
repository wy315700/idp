package test;

import org.cas.iie.idp.user.UserRole;

import cc.saml.SAMLresponse;

public class samlResponsetest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		UserRole user = new UserRole();
		user.setUsername("aaaa");
		user.addUsergroup("grp1");
		user.addUsergroup("grp2");
		
		SAMLresponse responsehandle = new SAMLresponse(user, 
				"aaaaa", 
				"aaaa", 
				"bbbb");
		responsehandle.generateAuthnResponse();
		
		String result = responsehandle.getSamlResponse();
		
		System.out.println(result);
	}

}
