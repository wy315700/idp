package cc.saml;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.cas.iie.idp.user.UserRole;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Subject;
import org.opensaml.xml.io.MarshallingException;

import LOG.Logger;

public class SAMLresponse extends SAML {
	UserRole user;
    String issuerURL;
    String requestID;
    String acsURL;
    private static String IDP_URL = "http://idp.example.org/";
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
	
	public boolean generateAuthnResponse(){
		try {
			Assertion assertion = createStockAuthnAssertion();
			samlResponse = showResponse(assertion);
			if(samlResponse != null){
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.writelog(e); 
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
    public String showResponse(Assertion assertion) 
    		throws IOException, MarshallingException, TransformerException{
    	Response response = createResponse (assertion);

        Issuer issuer = create (Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue (IDP_URL);
        response.setIssuer (issuer);
        
        response.setInResponseTo(requestID);
        return printToString(response);
    }
	
	/**
    Creates a file whose contents are a SAML authentication assertion.
    */
    public Assertion createStockAuthnAssertion ()
        throws Exception
    {
        DateTime now = new DateTime ();
        Issuer issueridp = create (Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issueridp.setValue(IDP_URL);
        
        Issuer issuersp = create (Issuer.class, Issuer.DEFAULT_ELEMENT_NAME);
        issuersp.setValue(issuerURL);
        
        NameID nameID = create (NameID.class, NameID.DEFAULT_ELEMENT_NAME);
        nameID.setValue(user.getUsername());
        
        
        Subject subject = create (Subject.class, Subject.DEFAULT_ELEMENT_NAME);
        subject.setNameID (nameID);


        AuthnContextClassRef ref = create (AuthnContextClassRef.class, 
            AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
        ref.setAuthnContextClassRef (AuthnContext.PPT_AUTHN_CTX);
        
        // As of this writing, OpenSAML doesn't model the wide range of
        // authentication context namespaces defined in SAML 2.0.
        // For a real project we'd probably move on to 
        //    XSAny objects, setting QNames and values each-by-each
        //    a JAXB mapping of the required schema
        //    DOM-building
        // For classroom purposes the road ends here ...
        
        AuthnContext authnContext = create 
            (AuthnContext.class, AuthnContext.DEFAULT_ELEMENT_NAME);
        authnContext.setAuthnContextClassRef (ref);

        AuthnStatement authnStatement = create 
            (AuthnStatement.class, AuthnStatement.DEFAULT_ELEMENT_NAME);
        authnStatement.setAuthnContext (authnContext);
        
        Assertion assertion = 
            create (Assertion.class, Assertion.DEFAULT_ELEMENT_NAME);
        //assertion.setID ("Assertion12345789");
        assertion.setID(generator.generateIdentifier ());
        assertion.setIssueInstant (now);
        assertion.setIssuer (issuersp);
        assertion.setSubject (subject);
        assertion.getStatements ().add (authnStatement);
        
        Conditions conditions = create 
        		(Conditions.class, Conditions.DEFAULT_ELEMENT_NAME);
        conditions.setNotBefore (now.minusSeconds (15));
        conditions.setNotOnOrAfter (now.plusSeconds (30));     
        assertion.setConditions(conditions);
        
        return assertion;
    }
	
	
	public String getSamlResponse() {
		return samlResponse;
	}

	public UserRole readFromResponse(){
		return user;
	}
}
