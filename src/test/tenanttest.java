package test;

import java.util.List;

import org.cas.iie.idp.admin.tenantAdmin;
import org.cas.iie.idp.user.TenantRole;

public class tenanttest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TenantRole tenant = new TenantRole();
		tenant.setTenantname("test2");
		tenantAdmin tenantadmin = new tenantAdmin();
		//tenantadmin.addTenant(tenant);
		//tenant = tenantadmin.getTenantByName("test");
		List<TenantRole> tenants = tenantadmin.getAllTenant();
		System.out.println(tenants.toString());
	}

}
