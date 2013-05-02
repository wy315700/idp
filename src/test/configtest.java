package test;

import org.cas.iie.idp.user.SamlConfigRole;
import org.cas.iie.idp.user.Configs;
import org.cas.iie.idp.user.TenantConfigRole;

public class configtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SamlConfigRole config = Configs.getSamlConfig("iie");
		//config.setSAML_NOT_AFTER(20);
		//config.setSAML_NOT_BEFORE(25);
		TenantConfigRole config = Configs.getTenantConfig("iie");
		config.addAttribute("group","群组");
		Configs.saveconfig(config);
	}

}
