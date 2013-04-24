package org.cas.iie.idp.user;

public class TenantRole {
	String tenantname;
	String tenanturl;
	String tenantDN;
	String tenantadminname;
	String tenantadminpassword;
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
	
	public String getTenantadminname() {
		return tenantadminname;
	}
	public void setTenantadminname(String tenantadminname) {
		this.tenantadminname = tenantadminname;
	}
	public void setTenantadminpassword(String tenantadminpassword) {
		this.tenantadminpassword = tenantadminpassword;
	}
	
	public String getTenantadminpassword() {
		return tenantadminpassword;
	}
	@Override
	public String toString() {
		return "TenantRole [tenantname=" + tenantname + ", tenanturl="
				+ tenanturl + ", tenantDN=" + tenantDN + ", tenantadminname="
				+ tenantadminname + "]";
	}
}
