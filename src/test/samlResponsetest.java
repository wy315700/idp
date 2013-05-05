package test;

import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.UserRole;

import cc.saml.SAMLresponse;

public class samlResponsetest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configs.setthisconfig("iie");

		UserRole user = new UserRole();
		user.setUsername("aaaa");
		user.addUsergroup("aa","grp1");
		user.addUsergroup("aa","grp4");
		user.addUsergroup("role","grp2");
		
		SAMLresponse responsehandle = new SAMLresponse(user, 
				"aaaaa", 
				"aaaa", 
				"bbbb");
		responsehandle.generateAuthnResponse();
		
		String result = responsehandle.getSamlResponse();
		
		SAMLresponse responsehandle2 = new SAMLresponse(result);
		
		user = responsehandle2.readResponse();
		
		System.out.println(user.toString());
	}

}
