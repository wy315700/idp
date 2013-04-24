package org.cas.iie.idp.user;

public class TenantRole {
	String tenantname;
	String tenanturl;
	String tenantDN;
	public String getTenantname() {
		return tenantname;
	}
	public void setTenantname(String tenantname) {
		this.tenantname = tenantname;
	}
	public String getTenanturl() {
		return tenanturl;
	}
	public void setTenanturl(String tenanturl) {
		this.tenanturl = tenanturl;
	}
	public String getTenantDN() {
		return tenantDN;
	}
	public void setTenantDN(String tenantDN) {
		this.tenantDN = tenantDN;
	}
	@Override
	public String toString() {
		return "TenantRole [tenantname=" + tenantname + ", tenanturl="
				+ tenanturl + ", tenantDN=" + tenantDN + "]";
	}
	
}
