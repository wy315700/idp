package org.cas.iie.idp.user;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.spi.RootCategory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.cas.iie.idp.admin.tenantAdmin;

import sun.security.provider.X509Factory;

import com.mysql.jdbc.Connection;

public class Configs {
	public static Map<String, SamlConfigRole> samlconfigs;
	public static Map<String, TenantConfigRole> tenantconfigs;
	private static TenantConfigRole thistenantconfig;
	private static SamlConfigRole thissamlconfig;
	public static  Connection con;
	
	private static PublicKey rootpubkey;
	private static PrivateKey rootprikey;
	private static Certificate cert;
	private static String certstr;
	private static String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/configs";
	private static String MYSQL_USER = "root";
	private static String MYSQL_PASS = "root";
	
	private static String MYSQL_GETSAMLCONFIG_QUERY = "select * from samlconfig where tenant = ?";
	private static String MYSQL_UPDATESAMLCONFIG_QUERY = "update samlconfig set configvalue = ? where tenant = ? and configkey = ?";
	private static String MYSQL_INSERTSAMLCONFIG_QUERY = "insert into samlconfig (tenant,configkey,configvalue) values (?,?,?)";
	
	private static String MYSQL_GETATTRCONFIG_QUERY = "select * from tenantconfig where tenant = ?";
	private static String MYSQL_INSERTATTRCONFIG_QUERY = "insert into tenantconfig (tenant,attrname,attrdisplayname) values (?,?,?)";
	private static String MYSQL_DELATTRCONFIG_QUERY = "delete  from tenantconfig where tenant = ?";

	public Configs(){
	}
	static{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(MYSQL_URL,MYSQL_USER,MYSQL_PASS);
			Security.addProvider(new BouncyCastleProvider());
			getrootkeys();
			getallsamlconfigs();
			getallattrconfigs();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean saveconfig(SamlConfigRole config){
		boolean result = true;
		result = savesamlconfigs(config.getTenantname(),"SAML_NOT_AFTER",String.valueOf(config.getSAML_NOT_AFTER()));
		result = savesamlconfigs(config.getTenantname(),"SAML_NOT_BEFORE",String.valueOf(config.getSAML_NOT_BEFORE()));
		result = savesamlconfigs(config.getTenantname(),"SAML_PUBLIC_KEY",String.valueOf(config.getPublickeystr()));
		result = savesamlconfigs(config.getTenantname(),"SAML_PRIVATE_KEY",String.valueOf(config.getPrivatekeystr()));
		
		return result;
	}
	public static boolean saveconfig(TenantConfigRole config){
		boolean result = true;
		result = delattrconfig(config.getTenantname());
		for(Map.Entry<String, String> entry : config.getAttributeset().entrySet()){
			result = addattrconfig(config.getTenantname(), entry.getKey(), entry.getValue());
		}
		return result;
	}
	private static void getallattrconfigs(){
		tenantconfigs = new HashMap<String, TenantConfigRole>();
		tenantAdmin tenantadmin = new tenantAdmin();
		List<TenantRole> tenants = tenantadmin.getAllTenant();
		for(TenantRole tenant : tenants){
			TenantConfigRole config = new TenantConfigRole();
			config.setTenantname(tenant.getTenantname());
			PreparedStatement sta = null;
			try {
				sta = con.prepareStatement(MYSQL_GETATTRCONFIG_QUERY);
				sta.setString(1,tenant.getTenantname());
				ResultSet rs = sta.executeQuery();
				while(rs.next()){
					String attr = rs.getString("attrname");
					String attrdispname = rs.getString("attrdisplayname");
					config.addAttribute(attr,attrdispname);
				}
				System.out.println(config.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tenantconfigs.put(tenant.getTenantname(), config);
		}
	}
	private static boolean delattrconfig(String tenantname){
		PreparedStatement sta = null;
		boolean result = true;
			try {
				sta = con.prepareStatement(MYSQL_DELATTRCONFIG_QUERY);
				
				sta.setString(1, tenantname);
				
				
				int row = sta.executeUpdate();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				result = false;
				e.printStackTrace();
			}
		return result;
	}
	
	private static boolean addattrconfig(String tenantname,String attr,String attrdispname){
		PreparedStatement sta = null;
		boolean result = true;
			try {
				sta = con.prepareStatement(MYSQL_INSERTATTRCONFIG_QUERY);
				
				sta.setString(1, tenantname);
				
				sta.setString(2, String.valueOf(attr));
				sta.setString(3, String.valueOf(attrdispname));

				int row = sta.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				result = false;
				e.printStackTrace();
			}
		return result;

	}
	private static void getallsamlconfigs(){
		samlconfigs = new HashMap<String, SamlConfigRole>();
		tenantAdmin tenantadmin = new tenantAdmin();
		List<TenantRole> tenants = tenantadmin.getAllTenant();
		for(TenantRole tenant : tenants){
			SamlConfigRole config = new SamlConfigRole();
			config.setTenantname(tenant.getTenantname());
			PreparedStatement sta = null;
			try {
				sta = con.prepareStatement(MYSQL_GETSAMLCONFIG_QUERY);
				sta.setString(1, tenant.getTenantname());
				ResultSet rs = sta.executeQuery();
				while(rs.next()){
					String key = rs.getString("configkey");
					String values = rs.getString("configvalue");
					config.setvalue(key, values);
				}
				System.out.println(config.toString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			config.readKeyFromStr();
			saveconfig(config);
			generateCert(config);
			samlconfigs.put(tenant.getTenantname(), config);
		}
	}
	
	private static boolean savesamlconfigs(String tenantname,String key,String value){
		PreparedStatement sta = null;
		boolean result = true;
			try {
				sta = con.prepareStatement(MYSQL_UPDATESAMLCONFIG_QUERY);
				sta.setString(2, tenantname);
				
				sta.setString(3, key);
				sta.setString(1, String.valueOf(value));
				
				int row = sta.executeUpdate();
				if(row != 1){
					PreparedStatement anothersta = con.prepareStatement(MYSQL_INSERTSAMLCONFIG_QUERY);
					anothersta.setString(1, tenantname);
					anothersta.setString(2, key);
					anothersta.setString(3, String.valueOf(value));
					row = anothersta.executeUpdate();
					if(row != 1){
						result = false;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				result = false;
				e.printStackTrace();
			}
		return result;
	}
	public static void generateCert(SamlConfigRole config){
		config.generatecertificate((X509Certificate) cert, rootprikey);		
	}
	public static void generateNewCert(SamlConfigRole config){
		config.generateNewKey();
		generateCert(config);
	}
	private static void getrootkeys(){
	     KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA","BC");
		    kpg.initialize(1024);  
		    KeyPair keyPair = kpg.generateKeyPair();  
		    KeyStore store = KeyStore.getInstance("JKS");  
			InputStream in = Configs.class.getResourceAsStream("/iie-ca.jks");

		    store.load(in, "iie".toCharArray());  
		    
		    rootprikey = (PrivateKey)store.getKey("iie", "iie".toCharArray());
		    
		    cert = store.getCertificate("iie");
		    rootpubkey=cert.getPublicKey();
		    
		    StringBuffer cstr = new StringBuffer();
		    
		    cstr.append(X509Factory.BEGIN_CERT+"\n");
		    Base64 base64hendle = new Base64();
		    cstr.append(new String(base64hendle.encode(cert.getEncoded())));
		    cstr.append("\n");
		    cstr.append(X509Factory.END_CERT);
		    certstr = cstr.toString();
		    System.out.println(certstr);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException | CertificateException | IOException | UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}
	public static SamlConfigRole getSamlConfig(String tenantname){
		SamlConfigRole config = samlconfigs.get(tenantname);
		return config;
	}
	public static TenantConfigRole getTenantConfig(String tenantname){
		TenantConfigRole config = tenantconfigs.get(tenantname);
		return config;
	}
	public static void setthisconfig(String tenantname){
		thissamlconfig = samlconfigs.get(tenantname);
		thistenantconfig = tenantconfigs.get(tenantname);
	}
	public static TenantConfigRole getthistenantconfig(){
		return thistenantconfig;
	}
	public static SamlConfigRole getthissamlconfig(){
		return thissamlconfig;
	}
	public static String getCertstr() {
		return certstr;
	}
	
}
