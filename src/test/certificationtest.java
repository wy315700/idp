package test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
public class certificationtest {

	/**
	 * @param args
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws SignatureException 
	 * @throws InvalidKeyException 
	 * @throws OperatorCreationException 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException, CertificateException, IOException, InvalidKeyException, SignatureException, OperatorCreationException {
		// TODO Auto-generated method stub
	     Security.addProvider(new BouncyCastleProvider());  
	     KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA","BC");  
	        kpg.initialize(1024);  
	        KeyPair keyPair = kpg.generateKeyPair();  
	        KeyStore store = KeyStore.getInstance("JKS");  
	        store.load(null, null);  
	        String issuer = "C=CN,ST=Beijing,L=Beijing,O=IIE,OU=IIE,CN=Wang Yang CA,E=wy315700@163.com";  
	        String subject = issuer;  
	        //issuer 与 subject相同的证书就是CA证书  
	        Certificate cert = generateV3(issuer, subject,  
	                BigInteger.ZERO, new Date(System.currentTimeMillis() - 1000  
	                        * 60 * 60 * 24),  
	                new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24  
	                        * 365 * 32), keyPair.getPublic(),//待签名的公钥  
	                keyPair.getPrivate(), null);  
	        store.setKeyEntry("iie", keyPair.getPrivate(),  
	                "iie".toCharArray(), new Certificate[] { cert });  
	        cert.verify(keyPair.getPublic());  
	        File file = new File("iie-ca.jks");  
	        Base64 base64handle = new Base64();
	        System.out.println(X509Factory.BEGIN_CERT);
	        System.out.println(new String(base64handle.encode(cert.getEncoded())));
	        System.out.println(X509Factory.END_CERT);
	        if (file.exists() || file.createNewFile()){ 
	            store.store(new FileOutputStream(file), "iie".toCharArray());  
	        }
	        file = new File("iie-ca.crt");  
	        if (file.exists() || file.createNewFile()){ 
	        	
	            store.store(new FileOutputStream(file), "iie".toCharArray());  
	        }
	}
	public static Certificate generateV3(String issuer, String subject,  
	        BigInteger serial, Date notBefore, Date notAfter,  
	        PublicKey publicKey, PrivateKey privKey, List<Extension> extensions) throws IOException, OperatorCreationException, CertificateException, NoSuchProviderException  
	           {  
	  
	    X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(  
	            new X500Name(issuer), serial, notBefore, notAfter,  
	            new X500Name(subject), publicKey);  
	    ContentSigner sigGen = new JcaContentSignerBuilder("SHA1withDSA")  
	            .setProvider("BC").build(privKey);  
	    //privKey:使用自己的私钥进行签名，CA证书  
	    if (extensions != null)  
	        for (Extension ext : extensions) {  
	            builder.addExtension(new ASN1ObjectIdentifier(ext.getOid()),  
	                    ext.isCritical(),  
	                    ASN1Primitive.fromByteArray(ext.getValue()));  
	        }  
	    X509CertificateHolder holder = builder.build(sigGen);  
	    CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");  
	    InputStream is1 = new ByteArrayInputStream(holder.toASN1Structure()  
	            .getEncoded());  
	    X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);  
	    is1.close();  
	    return theCert;  
	}  
	public class Extension {  
	    private String oid;  
	    private boolean critical;  
	    private byte[] value;  
	  
	    public String getOid() {  
	        return oid;  
	    }  
	  
	    public byte[] getValue() {  
	        return value;  
	    }  
	  
	    public boolean isCritical() {  
	        return critical;  
	    }  
	}  

}
