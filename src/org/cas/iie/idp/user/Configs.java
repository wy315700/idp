package org.cas.iie.idp.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cas.iie.idp.admin.tenantAdmin;

public class Configs {
	public static Map<String, ConfigRole> configs;
	private static ConfigRole thisconfig;
	public Configs(){
	}
	static{
		configs = new HashMap<String, ConfigRole>();
		tenantAdmin tenantadmin = new tenantAdmin();
		List<TenantRole> tenants = tenantadmin.getAllTenant();
		for(TenantRole tenant : tenants){
			ConfigRole config = new ConfigRole(tenant.getTenantname());
			configs.put(tenant.getTenantname(), config);
		}
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
