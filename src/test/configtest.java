package test;

import java.util.Set;

import org.cas.iie.idp.admin.configAdmin;
import org.cas.iie.idp.authenticate.LDAP.LDAPhelper;
import org.cas.iie.idp.user.SamlConfigRole;
import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.TenantConfigRole;

public class configtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LDAPhelper.domain = "o=iie";
		Configs.setthisconfig("iie");
		SamlConfigRole config = Configs.getSamlConfig("iie");
		//config.setSAML_NOT_AFTER(20);
		//config.setSAML_NOT_BEFORE(25);
		//TenantConfigRole config = Configs.getTenantConfig("iie");
		//config.addAttribute("group","群组");
		Configs.saveconfig(config);
		/*
		configAdmin configadmin = new configAdmin();
		
		configadmin.deleteAttribution("aa");
		Set<String> attrs = configadmin.getAllAttibutions();
		System.out.println(attrs.toString());
		
		configadmin.addAttribute("aa");
		attrs = configadmin.getAllAttibutions();
		System.out.println(attrs.toString());
		*/
	}

}
