package org.cas.iie.idp.user;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cas.iie.idp.admin.tenantAdmin;

import com.mysql.jdbc.Connection;

public class Configs {
	public static Map<String, SamlConfigRole> samlconfigs;
	public static Map<String, TenantConfigRole> tenantconfigs;
	private static TenantConfigRole thistenantconfig;
	private static SamlConfigRole thissamlconfig;
	public static  Connection con;

	
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
				sta.setString(1,tenant.getTenantadminname());
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
}
