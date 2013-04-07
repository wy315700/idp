package cc.saml;

public class SAMLrequest extends SAML {
	String samlRequest;

    String issuerURL;
    String requestID;
    String acsURL;
	public SAMLrequest(String issuerURL, String requestID, String acsURL) {
		super();
		this.issuerURL = issuerURL;
		this.requestID = requestID;
		this.acsURL = acsURL;
	}
	public SAMLrequest(String request) {
		super();
		this.samlRequest = request;
	}
	
	
	public String generateAuthnRequest(){
		return samlRequest;
	}
	public void readFromRequest(){
		
	}
	
	
	
	
	public String getSamlRequest() {
		return samlRequest;
	}
	public void setSamlRequest(String samlRequest) {
		this.samlRequest = samlRequest;
	}
	public String getIssuerURL() {
		return issuerURL;
	}
	public void setIssuerURL(String issuerURL) {
		this.issuerURL = issuerURL;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getAcsURL() {
		return acsURL;
	}
	public void setAcsURL(String acsURL) {
		this.acsURL = acsURL;
	}
	
}
