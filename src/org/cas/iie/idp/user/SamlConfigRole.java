package org.cas.iie.idp.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;



public class SamlConfigRole {
	public String tenantname;
	public int SAML_NOT_BEFORE = 0;
	public int SAML_NOT_AFTER  = 0;
	public PrivateKey privatekey;
	public PublicKey publickey;
	public String privatekeystr = null;
	public String publickeystr  = null;
	public SamlConfigRole(){
		SAML_NOT_BEFORE = 15;
		SAML_NOT_AFTER  = 30;
	}
	
	public String getPrivatekeystr() {
		return privatekeystr;
	}

	public void setPrivatekeystr(String privatekeystr) {
		this.privatekeystr = privatekeystr;
	}

	public String getPublickeystr() {
		return publickeystr;
	}

	public void setPublickeystr(String publickeystr) {
		this.publickeystr = publickeystr;
	}

	public PrivateKey getPrivatekey() {
		return privatekey;
	}

	public void setPrivatekey(PrivateKey privatekey) {
		this.privatekey = privatekey;
	}

	public PublicKey getPublickey() {
		return publickey;
	}

	public void setPublickey(PublicKey publickey) {
		this.publickey = publickey;
	}
	public void generateNewKey(){
		KeyPairGenerator keygen;
		try {
			keygen = KeyPairGenerator.getInstance("DSA");
			keygen.initialize(1024);
	        KeyPair keyPair = keygen.genKeyPair();
	        privatekey = keyPair.getPrivate();
	        publickey  = keyPair.getPublic();
	        writeKeyToStr();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeKeyToStr(){
		if(privatekey == null || publickey == null){
			generateNewKey();
		}
        Base64 base64encoder = new Base64();
        //public 
        byte[] pub = base64encoder.encode(publickey.getEncoded());
        publickeystr = new String(pub);
        //private
        byte[] pri = base64encoder.encode(privatekey.getEncoded());
        privatekeystr = new String(pri);
	}
	public void readKeyFromStr(){
		if(privatekeystr == null || publickeystr == null){
			generateNewKey();
			writeKeyToStr();
			return ;
		}
        Base64 base64encoder = new Base64();
        KeyFactory keyfact;
		try {
			keyfact = KeyFactory.getInstance("DSA");
	        //public
			byte[] pub = base64encoder.decode(publickeystr.getBytes());

	        X509EncodedKeySpec pubkeyspec = new X509EncodedKeySpec(pub);
	        publickey = keyfact.generatePublic(pubkeyspec);
	        
	        //private
			byte[] pri = base64encoder.decode(privatekeystr.getBytes());
			PKCS8EncodedKeySpec prikeyspec = new PKCS8EncodedKeySpec(pri);
			privatekey = keyfact.generatePrivate(prikeyspec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			privatekey = null;
			publickey  = null;
		}

	}
	public boolean setvalue(String key,String values){
		if(key.equals("SAML_NOT_BEFORE")){
			SAML_NOT_BEFORE = Integer.parseInt(values);
		}else if(key.equals("SAML_NOT_AFTER")){
			SAML_NOT_AFTER  = Integer.parseInt(values);
		}else if(key.equals("SAML_PUBLIC_KEY")){
			publickeystr = values;
		}else if(key.equals("SAML_PRIVATE_KEY")){
			privatekeystr = values;
		}else{
			return false;
		}
		return true;
	}
	public boolean save(){
		return false;
	}
	public String getTenantname() {
		return tenantname;
	}

	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	public int getSAML_NOT_BEFORE() {
		return SAML_NOT_BEFORE;
	}

	public void setSAML_NOT_BEFORE(int sAML_NOT_BEFORE) {
		SAML_NOT_BEFORE = sAML_NOT_BEFORE;
	}

	public int getSAML_NOT_AFTER() {
		return SAML_NOT_AFTER;
	}

	public void setSAML_NOT_AFTER(int sAML_NOT_AFTER) {
		SAML_NOT_AFTER = sAML_NOT_AFTER;
	}
	@Override
	public String toString() {
		return "SamlConfigRole [tenantname=" + tenantname
				+ ", SAML_NOT_BEFORE=" + SAML_NOT_BEFORE + ", SAML_NOT_AFTER="
				+ SAML_NOT_AFTER + "]";
	}
}
