package test;

import java.io.IOException;
import java.security.KeyPair;

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

import cc.saml.SAML;
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
		System.out.println(result);
		System.out.println(user.toString());
		
	}
}
