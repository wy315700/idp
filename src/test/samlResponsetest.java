package test;

import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.UserRole;
import org.joda.time.DateTime;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.security.CriteriaSet;
import org.opensaml.xml.security.SecurityHelper;
import org.opensaml.xml.security.SecurityTestHelper;
import org.opensaml.xml.security.credential.BasicCredential;
import org.opensaml.xml.security.credential.StaticCredentialResolver;
import org.opensaml.xml.security.criteria.EntityIDCriteria;
import org.opensaml.xml.security.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.signature.impl.ExplicitKeySignatureTrustEngine;
import org.opensaml.xml.signature.impl.SignatureBuilder;
import org.opensaml.xml.validation.ValidationException;

import com.google.gson.Gson;

import cc.saml.SAML;
import cc.saml.SAMLrequest;
import cc.saml.SAMLresponse;
import cc.xml.PrettyPrinter;

public class samlResponsetest extends SAML{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LDAPhelper.domain="o=iie";
		Configs.setthisconfig("iie");
		UserRole user = new UserRole();
		user.setUsername("admin");
		user.addUsergroup("group","群组");
		user.addUsergroup("group","grp2");
		user.addUsergroup("role","role");
		SAMLrequest requesthandle = new SAMLrequest("sp.example.org", "iie", "http://sp.example.org/");
		requesthandle.generateAuthnRequest();
		System.out.println(requesthandle.getSamlRequest());
		SAMLresponse responsehandle = new SAMLresponse(user, 
				"sp.example.org", 
				"123456789", 
				"http://sp.example.org/");
		responsehandle.generateAuthnResponse();
		
		String result = responsehandle.getSamlResponse();
		
		String[] keys   = {"islogin","action","samlResponse"};
		String[] values = {"true","submitresponse",result};
		String returnJsonMessage = generateJson(keys, values);

		
		
		SAMLresponse responsehandle2 = new SAMLresponse(result);
		
		user = responsehandle2.readResponse();
		System.out.println(returnJsonMessage);
		System.out.println(user.toString());
		
	}
	private static String generateJson(String[] keys,String[] values){
		Gson gson = new Gson();
		Map<String,String> userObj = new HashMap<String, String>();
		//userObj.put("username", username);
		//userObj.put("islogin", "true");
		if(keys.length != values.length){
			return null;
		}
		for(int i = 0 ; i < keys.length ;i++ ){
			userObj.put(keys[i],values[i]);
		}
		return gson.toJson(userObj);
	}

}
