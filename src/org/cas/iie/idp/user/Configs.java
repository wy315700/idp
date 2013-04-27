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
	public static Map<String, ConfigRole> configs;
	private static ConfigRole thisconfig;
	public static  Connection con;

	
	private static String MYSQL_URL = "jdbc:mysql://127.0.0.1:3306/samlconfig";
	private static String MYSQL_USER = "root";
	private static String MYSQL_PASS = "root";
	private static String MYSQL_GETCONFIG_QUERY = "select * from config where tenant = ?";
	private static String MYSQL_UPDATECONFIG_QUERY = "update config set configvalue = ? where tenant = ? and configkey = ?";
	private static String MYSQL_INSERTCONFIG_QUERY = "insert into config (tenant,configkey,configvalue) values (?,?,?)";
	public Configs(){
	}
	static{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection(MYSQL_URL,MYSQL_USER,MYSQL_PASS);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		configs = new HashMap<String, ConfigRole>();
		tenantAdmin tenantadmin = new tenantAdmin();
		List<TenantRole> tenants = tenantadmin.getAllTenant();
		for(TenantRole tenant : tenants){
			ConfigRole config = new ConfigRole();
			config.setTenantname(tenant.getTenantname());
			PreparedStatement sta = null;
			try {
				sta = con.prepareStatement(MYSQL_GETCONFIG_QUERY);
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

			
			configs.put(tenant.getTenantname(), config);
		}
	}
	public static boolean saveconfig(ConfigRole config){
		PreparedStatement sta = null;
		boolean result = true;
		try {
			sta = con.prepareStatement(MYSQL_UPDATECONFIG_QUERY);
			sta.setString(2, config.getTenantname());
			
			sta.setString(3, "SAML_NOT_AFTER");
			sta.setString(1, String.valueOf(config.getSAML_NOT_AFTER()));
			
			int row = sta.executeUpdate();
			if(row != 1){
				PreparedStatement anothersta = con.prepareStatement(MYSQL_INSERTCONFIG_QUERY);
				anothersta.setString(1, config.getTenantname());
				anothersta.setString(2, "SAML_NOT_AFTER");
				anothersta.setString(3, String.valueOf(config.getSAML_NOT_AFTER()));
				row = anothersta.executeUpdate();
				if(row != 1){
					result = false;
				}
			}
			
			sta.setString(3, "SAML_NOT_BEFORE");
			sta.setString(1, String.valueOf(config.getSAML_NOT_BEFORE()));
			row = sta.executeUpdate();
			if(row != 1){
				PreparedStatement anothersta = con.prepareStatement(MYSQL_INSERTCONFIG_QUERY);
				anothersta.setString(1, config.getTenantname());
				anothersta.setString(2, "SAML_NOT_AFTER");
				anothersta.setString(3, String.valueOf(config.getSAML_NOT_AFTER()));
				row = anothersta.executeUpdate();
				if(row != 1){
					result = false;
				}
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		return result;

	}
	
	public static ConfigRole getConfig(String tenantname){
		ConfigRole config = configs.get(tenantname);
		return config;
	}
	public static void setthisconfig(String tenantname){
		thisconfig = configs.get(tenantname);
	}
	public static ConfigRole getthisconfig(){
		return thisconfig;
	}
}
