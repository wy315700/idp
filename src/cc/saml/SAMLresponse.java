package cc.saml;

import org.cas.iie.idp.user.UserRole;

public class SAMLresponse extends SAML {
	UserRole user;
    String issuerURL;
    String requestID;
    String acsURL;
    
    String samlResponse;
    
    public SAMLresponse(UserRole user, String issuerURL, String requestID,
			String acsURL) {
		super();
		this.user = user;
		this.issuerURL = issuerURL;
		this.requestID = requestID;
		this.acsURL = acsURL;
	}

	public SAMLresponse(String samlResponse) {
		super();
		this.samlResponse = samlResponse;
	}
	
	public void generateAuthnResponse(){
		
	}
	
	public String getSamlResponse() {
		return samlResponse;
	}

	public UserRole readFromResponse(){
		return user;
	}
}
