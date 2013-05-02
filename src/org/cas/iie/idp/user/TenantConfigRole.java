package org.cas.iie.idp.user;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TenantConfigRole {
	Map<String,String> attributeset;
	String tenantname;
	public TenantConfigRole(){
		attributeset = new HashMap<String,String>(); 
	}
	
	public Map<String, String> getAttributeset() {
		return attributeset;
	}
	
	public void setAttributeset(Map<String, String> attributeset) {
		this.attributeset = attributeset;
	}
	public void delAttribute(String key){
		if(attributeset.containsKey(key)){
			attributeset.remove(key);
		}
	}
	public void addAttribute(String key,String value){
		attributeset.put(key, value);
	}
	public String getTenantname() {
		return tenantname;
	}
	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}

	@Override
	public String toString() {
		return "TenantConfigRole [attributeset=" + attributeset
				+ ", tenantname=" + tenantname + "]";
	}
	
}
