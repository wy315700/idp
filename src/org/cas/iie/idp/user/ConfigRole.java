package org.cas.iie.idp.user;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


public class ConfigRole {
	public String tenantname;
	public int SAML_NOT_BEFORE = 0;
	public int SAML_NOT_AFTER  = 0;
	public ConfigRole(String tenant){
		tenantname = tenant;
		Properties prop = new Properties();
		try {
			InputStream in = ConfigRole.class.getResourceAsStream("/config/"+tenant+"/prop.properties");
			prop.load(in);
			SAML_NOT_BEFORE = Integer.parseInt(prop.getProperty("SAML_NOT_BEFORE", "15"));
			SAML_NOT_AFTER = Integer.parseInt(prop.getProperty("SAML_NOT_AFTER", "30"));
			System.out.println("Config [SAML_NOT_BEFORE=" + SAML_NOT_BEFORE
				+ ", SAML_NOT_AFTER=" + SAML_NOT_AFTER + "]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return "Config [SAML_NOT_BEFORE=" + SAML_NOT_BEFORE
				+ ", SAML_NOT_AFTER=" + SAML_NOT_AFTER + "]";
	}
	
}
