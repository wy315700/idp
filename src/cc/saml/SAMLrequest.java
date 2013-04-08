package cc.saml;

public class SAMLrequest extends SAML {
	String samlRequest;

    String issuerURL;
    String requestID;
    String acsURL;
    String provideName;
	public SAMLrequest(String issuerURL, String provideName, String acsURL) {
		super();
		this.issuerURL = issuerURL;
		this.provideName = provideName;
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
	public String getprovideName() {
		return provideName;
	}
	public void setprovideName(String provideName) {
		this.provideName = acsURL;
	}
}
