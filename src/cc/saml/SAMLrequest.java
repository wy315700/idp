package cc.saml;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.joda.time.DateTime;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.xml.sax.SAXException;

import LOG.Logger;

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
		if(issuerURL == null || provideName == null || acsURL == null){
			return null;
		}
		Issuer issuer = create (Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue(issuerURL);
        DateTime now = new DateTime ();
        AuthnRequest authnrequest = (AuthnRequest) create(AuthnRequest.class
				,AuthnRequest.DEFAULT_ELEMENT_NAME);
		authnrequest.setIssuer(issuer);
		authnrequest.setID(generator.generateIdentifier());
		authnrequest.setAssertionConsumerServiceURL(acsURL);
		authnrequest.setIssueInstant(now);
		authnrequest.setProtocolBinding(SAMLConstants.SAML2_POST_BINDING_URI);
		authnrequest.setProviderName(provideName);
		try {
			samlRequest = printToString(authnrequest);
			SAMLencode encoder = new SAMLencode(samlRequest);
			samlRequest = encoder.doEncode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			samlRequest = null;
			Logger.writelog(e);
			e.printStackTrace();
		} catch (MarshallingException e) {
			// TODO Auto-generated catch block
			samlRequest = null;
			Logger.writelog(e);
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			samlRequest = null;
			Logger.writelog(e);
			e.printStackTrace();
		}

		return samlRequest;
	}
	public boolean readFromRequest(){
		if(samlRequest == null){
			return false;
		}
		SAMLdecode decoder = new SAMLdecode(samlRequest);
		String samlXmlRequest = decoder.doDecode();
		if(samlXmlRequest == null){
			return false;
		}
		try {
			AuthnRequest request = (AuthnRequest) readFromString(samlXmlRequest);
			
			issuerURL   = request.getIssuer().getValue();
			requestID   = request.getID();
			acsURL      = request.getAssertionConsumerServiceURL();
			provideName = request.getProviderName();
			return true;
			
		} catch (IOException | UnmarshallingException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logger.writelog(e);
		}
		
		return false;
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
