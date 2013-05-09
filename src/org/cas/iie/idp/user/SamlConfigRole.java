package org.cas.iie.idp.user;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import sun.security.provider.X509Factory;




public class SamlConfigRole {
	public String tenantname;
	public int SAML_NOT_BEFORE = 0;
	public int SAML_NOT_AFTER  = 0;
	public PrivateKey privatekey;
	public PublicKey publickey;
	public Certificate certificate;
	public String certificatestr;
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
	
	public String getCertificatestr() {
		return certificatestr;
	}

	public void setCertificatestr(String certificatestr) {
		this.certificatestr = certificatestr;
	}

	public void generateNewKey(){
		KeyPairGenerator keygen;
		long start = System.currentTimeMillis();
		try {
			keygen = KeyPairGenerator.getInstance("DSA","BC");
			keygen.initialize(1024);
	        KeyPair keyPair = keygen.genKeyPair();
	        privatekey = keyPair.getPrivate();
	        publickey  = keyPair.getPublic();
	        writeKeyToStr();
			long end = System.currentTimeMillis();
			System.out.println("create keys using time: "+(end-start));
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
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
			keyfact = KeyFactory.getInstance("DSA","BC");
	        //public
			byte[] pub = base64encoder.decode(publickeystr.getBytes());

	        X509EncodedKeySpec pubkeyspec = new X509EncodedKeySpec(pub);
	        publickey = keyfact.generatePublic(pubkeyspec);
	        
	        //private
			byte[] pri = base64encoder.decode(privatekeystr.getBytes());
			PKCS8EncodedKeySpec prikeyspec = new PKCS8EncodedKeySpec(pri);
			privatekey = keyfact.generatePrivate(prikeyspec);
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			privatekey = null;
			publickey  = null;
		}

	}
	public void generatecertificate(X509Certificate caCert,PrivateKey prikey){
		String subject = "o="+tenantname+",dc=org";
        String issuer = caCert.getIssuerDN().toString();  
        Certificate cert = null;
		try {
			cert = generateV3(issuer, subject,  
			        BigInteger.ZERO, new Date(System.currentTimeMillis() - 1000  
			                * 60 * 60 * 24),  
			        new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24  
			                * 365 * 32), publickey,//待签名的公钥  
			                prikey//CA的私钥  
			        , null);
		} catch (OperatorCreationException | CertificateException | IOException e) {
			// TODO Auto-generated catch block
			cert = null;
			e.printStackTrace();
		}
		certificate = cert;
		writeCerttoString();
	}
	
	private void writeCerttoString(){
	    Base64 base64hendle = new Base64();
	    StringBuffer cstr = new StringBuffer();
	    String certstr = null;
	    try {
	    	cstr.append(X509Factory.BEGIN_CERT);
	    	cstr.append("\n");
			cstr.append(new String(base64hendle.encode(certificate.getEncoded())));
		    cstr.append("\n");
		    cstr.append(X509Factory.END_CERT);
		    certstr = cstr.toString();
		} catch (CertificateEncodingException e) {
			// TODO Auto-generated catch block
			certstr = null;
			e.printStackTrace();
		}
	    certificatestr = certstr;
	}
    public Certificate generateV3(String issuer, String subject,  
            BigInteger serial, Date notBefore, Date notAfter,  
            PublicKey publicKey, PrivateKey privKey, List<Extension> extensions)  
            throws OperatorCreationException, CertificateException, IOException {  
  
        X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(  
                new X500Name(issuer), serial, notBefore, notAfter,  
                new X500Name(subject), publicKey);  
        ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withDSA")  
                .setProvider("BC").build(privKey);  
        //privKey是CA的私钥，publicKey是待签名的公钥，那么生成的证书就是被CA签名的证书。  
        if (extensions != null)  
            for (Extension ext : extensions) {  
                builder.addExtension(new ASN1ObjectIdentifier(ext.getId()),  
                        ext.isCritical(),  
                        ASN1Primitive.fromByteArray(ext.getValue()));  
            }  
        X509CertificateHolder holder = builder.build(sigGen);  
        CertificateFactory cf = CertificateFactory.getInstance("X.509");  
        InputStream is1 = new ByteArrayInputStream(holder.toASN1Structure()  
                .getEncoded());  
        X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);  
        is1.close();  
        return theCert;  
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
